<!--
  * JOB 列表
  * @Author:    huke
  * @Date:      2024/06/25
-->
<template>
  <div>
    <a-form class="smart-query-form">
      <a-row class="smart-query-form-row">
        <a-form-item label="关键字" class="smart-query-form-item">
          <a-input style="width: 200px" v-model:value="queryForm.searchWord" placeholder="请输入关键字" :maxlength="30" />
        </a-form-item>
        <a-form-item label="触发类型" class="smart-query-form-item">
          <a-select style="width: 155px" v-model:value="queryForm.triggerType" placeholder="请选择触发类型" allowClear>
            <a-select-option v-for="item in $smartEnumPlugin.getValueDescList('TRIGGER_TYPE_ENUM')" :key="item.value" :value="item.value">
              {{ item.desc }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="状态" class="smart-query-form-item">
          <a-select style="width: 150px" v-model:value="queryForm.enabledFlag" placeholder="请选择状态" allowClear>
            <a-select-option :key="1"> 开启 </a-select-option>
            <a-select-option :key="0"> 停止 </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item class="smart-query-form-item smart-margin-left10">
          <a-button-group>
            <a-button type="primary" @click="onSearch" v-privilege="'support:job:query'">
              <template #icon>
                <ReloadOutlined />
              </template>
              查询
            </a-button>
            <a-button @click="resetQuery" v-privilege="'support:job:query'">
              <template #icon>
                <SearchOutlined />
              </template>
              重置
            </a-button>
          </a-button-group>
        </a-form-item>
      </a-row>
    </a-form>

    <a-card size="small" :bordered="false" :hoverable="true">
      <a-row justify="end">
        <TableOperator class="smart-margin-bottom5" v-model="columns" :tableId="TABLE_ID_CONST.SUPPORT.JOB" :refresh="queryJobList" />
      </a-row>

      <a-table
        :scroll="{ x: 1800 }"
        size="small"
        :loading="tableLoading"
        bordered
        :dataSource="tableData"
        :columns="columns"
        rowKey="jobId"
        :pagination="false"
      >
        <template #bodyCell="{ record, column }">
          <template v-if="column.dataIndex === 'jobClass'">
            <a-tooltip>
              <template #title>{{ record.jobClass }}</template>
              {{ handleJobClass(record.jobClass) }}
            </a-tooltip>
          </template>
          <template v-if="column.dataIndex === 'triggerType'">
            <a-tag v-if="record.triggerType === TRIGGER_TYPE_ENUM.CRON.value" color="success">{{ record.triggerTypeDesc }}</a-tag>
            <a-tag v-else-if="record.triggerType === TRIGGER_TYPE_ENUM.FIXED_DELAY.value" color="processing">{{ record.triggerTypeDesc }}</a-tag>
            <a-tag v-else color="pink">{{ record.triggerTypeDesc }}</a-tag>
          </template>
          <template v-if="column.dataIndex === 'lastJob'">
            <div v-if="record.lastJobLog">
              <a-tooltip>
                <template #title>{{ handleExecuteResult(record.lastJobLog.executeResult) }}</template>
                <CheckOutlined v-if="record.lastJobLog.successFlag" style="color: #39c710" />
                <WarningOutlined v-else style="color: #f50" />
                {{ record.lastJobLog.executeStartTime }}
              </a-tooltip>
            </div>
          </template>
          <template v-if="column.dataIndex === 'nextJob'">
            <a-tooltip v-if="record.enabledFlag && record.nextJobExecuteTimeList">
              <template #title>
                <div>下次执行(预估时间)</div>
                <div v-for="item in record.nextJobExecuteTimeList" :key="item">{{ item }}</div>
              </template>
              {{ record.nextJobExecuteTimeList[0] }}
            </a-tooltip>
          </template>
          <template v-if="column.dataIndex === 'enabledFlag'">
            <a-switch
              v-model:checked="record.enabledFlag"
              @change="(checked) => handleEnabledUpdate(checked, record)"
              :loading="record.enabledLoading"
            />
          </template>
          <template v-if="column.dataIndex === 'action'">
            <div class="smart-table-operate">
              <a-button v-privilege="'support:job:update'" @click="openUpdateModal(record)" type="link">编辑</a-button>
              <a-button v-privilege="'support:job:execute'" type="link" @click="openExecuteModal(record)">执行</a-button>
              <a-button v-privilege="'support:job:log:query'" @click="openJobLogModal(record.jobId, record.jobName)" type="link">记录</a-button>
            </div>
          </template>
        </template>
      </a-table>

      <div class="smart-query-table-page">
        <a-pagination
          showSizeChanger
          showQuickJumper
          show-less-items
          :pageSizeOptions="PAGE_SIZE_OPTIONS"
          :defaultPageSize="queryForm.pageSize"
          v-model:current="queryForm.pageNum"
          v-model:pageSize="queryForm.pageSize"
          :total="total"
          @change="queryJobList"
          @showSizeChange="queryJobList"
          :show-total="(total) => `共${total}条`"
        />
      </div>
    </a-card>

    <!-- 表单操作 -->
    <JobFormModal ref="jobFormModal" @reloadList="queryJobList" />

    <!-- 记录 -->
    <JobLogListModal ref="jobLogModal" />
  </div>
</template>
<script setup>
  import { message } from 'ant-design-vue';
  import { onMounted, reactive, ref } from 'vue';
  import { jobApi } from '/@/api/support/job-api';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { smartSentry } from '/@/lib/smart-sentry';
  import TableOperator from '/@/components/support/table-operator/index.vue';
  import { TABLE_ID_CONST } from '/@/constants/support/table-id-const';
  import { useRouter } from 'vue-router';
  import { TRIGGER_TYPE_ENUM } from '/@/constants/support/job-const.js';
  import JobFormModal from './components/job-form-modal.vue';
  import JobLogListModal from './components/job-log-list-modal.vue';

  const columns = ref([
    {
      title: 'id',
      width: 50,
      dataIndex: 'jobId',
    },
    {
      title: '任务名称',
      dataIndex: 'jobName',
      minWidth: 150,
      ellipsis: true,
    },
    {
      title: '执行类',
      dataIndex: 'jobClass',
      minWidth: 180,
      ellipsis: true,
    },
    {
      title: '触发类型',
      dataIndex: 'triggerType',
      width: 110,
    },
    {
      title: '触发配置',
      dataIndex: 'triggerValue',
      width: 150,
    },
    {
      title: '上次执行',
      width: 180,
      dataIndex: 'lastJob',
    },
    {
      title: '下次执行',
      width: 150,
      dataIndex: 'nextJob',
    },
    {
      title: '状态',
      dataIndex: 'enabledFlag',
      width: 80,
    },
    {
      title: '执行参数',
      dataIndex: 'param',
      ellipsis: true,
    },
    {
      title: '任务描述',
      dataIndex: 'remark',
      ellipsis: true,
    },
    {
      title: '排序',
      dataIndex: 'sort',
      width: 65,
    },
    {
      title: '更新人',
      dataIndex: 'updateName',
      width: 90,
    },
    {
      title: '更新时间',
      dataIndex: 'updateTime',
      width: 150,
    },
    {
      title: '操作',
      dataIndex: 'action',
      fixed: 'right',
      width: 130,
    },
  ]);

  // ---------------- 查询数据 -----------------------

  const queryFormState = {
    searchWord: '',
    enabledFlag: null,
    triggerType: null,
    pageNum: 1,
    pageSize: 10,
  };
  const queryForm = reactive({ ...queryFormState });

  const tableLoading = ref(false);
  const tableData = ref([]);
  const total = ref(0);

  function resetQuery() {
    Object.assign(queryForm, queryFormState);
    queryJobList();
  }

  function onSearch() {
    queryForm.pageNum = 1;
    queryJobList();
  }

  // 处理执行类展示 默认返回类
  function handleJobClass(jobClass) {
    return jobClass.split('.').pop();
  }

  // 上次处理结果展示 最多展示300
  function handleExecuteResult(result) {
    let num = 400;
    return result ? result.substring(0, num) + (result.length > num ? ' ...' : '') : '';
  }

  async function queryJobList() {
    try {
      tableLoading.value = true;
      let responseModel = await jobApi.queryJob(queryForm);
      const list = responseModel.data.list;
      total.value = responseModel.data.total;
      tableData.value = list;
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      tableLoading.value = false;
    }
  }

  onMounted(queryJobList);

  // 更新状态
  async function handleEnabledUpdate(checked, record) {
    record.enabledLoading = true;
    try {
      let updateForm = {
        jobId: record.jobId,
        enabledFlag: checked,
      };
      await jobApi.updateJobEnabled(updateForm);
      // 重新查询任务详情
      let jobInfo = await queryJobInfo(record.jobId);
      Object.assign(record, jobInfo);
      message.success('更新成功');
    } catch (e) {
      record.enabledFlag = !checked;
      smartSentry.captureError(e);
    } finally {
      record.enabledLoading = false;
    }
  }

  // 查询任务详情
  async function queryJobInfo(jobId) {
    try {
      let res = await jobApi.queryJobInfo(jobId);
      return res.data;
    } catch (e) {
      smartSentry.captureError(e);
    }
  }

  // ------------------------------------ 执行记录 -------------------------------------
  const jobLogModal = ref();
  function openJobLogModal(jobId, name) {
    jobLogModal.value.show(jobId, name);
  }

  // ------------------------------------ 表单操作 -------------------------------------
  const jobFormModal = ref();

  // 打开更新表单
  function openUpdateModal(record) {
    jobFormModal.value.openUpdateModal(record);
  }
  // 打开执行表单
  function openExecuteModal(record) {
    jobFormModal.value.openExecuteModal(record);
  }
</script>
