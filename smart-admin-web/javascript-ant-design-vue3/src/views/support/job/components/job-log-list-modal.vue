<!--
  * job log列表
  * @Author:    huke
  * @Date:      2024/06/25
-->
<template>
  <a-drawer v-model:open="showFlag" :width="1000" :title="title" placement="right" :destroyOnClose="true">
    <a-form class="smart-query-form">
      <a-row class="smart-query-form-row">
        <a-form-item label="关键字" class="smart-query-form-item">
          <a-input style="width: 200px" v-model:value="queryForm.searchWord" placeholder="请输入关键字" :maxlength="30" />
        </a-form-item>
        <a-form-item label="执行结果" class="smart-query-form-item">
          <a-select style="width: 100px" v-model:value="queryForm.successFlag" placeholder="请选择" allowClear>
            <a-select-option :key="1"> 成功 </a-select-option>
            <a-select-option :key="0"> 失败 </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="执行时间" class="smart-query-form-item">
          <a-space direction="vertical" :size="12">
            <a-range-picker v-model:value="searchDate" style="width: 220px" @change="dateChange" />
          </a-space>
        </a-form-item>

        <a-form-item class="smart-query-form-item smart-margin-left10">
          <a-button-group>
            <a-button type="primary" @click="onSearch">
              <template #icon>
                <ReloadOutlined />
              </template>
              查询
            </a-button>
            <a-button @click="resetQuery">
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
        <TableOperator class="smart-margin-bottom5" v-model="columns" :tableId="TABLE_ID_CONST.SUPPORT.JOB_LOG" :refresh="queryLogList" />
      </a-row>

      <a-table size="small" :loading="tableLoading" bordered :dataSource="tableData" :columns="columns" rowKey="jobLogId" :pagination="false">
        <template #bodyCell="{ record, column }">
          <template v-if="column.dataIndex === 'executeStartTime'">
            <div><a-tag color="green">始</a-tag>{{ record.executeStartTime }}</div>
            <div style="margin-top: 5px"><a-tag color="blue">终</a-tag>{{ record.executeEndTime }}</div>
          </template>
          <template v-if="column.dataIndex === 'executeTimeMillis'"> {{ record.executeTimeMillis }} ms </template>
          <template v-if="column.dataIndex === 'successFlag'">
            <div v-if="record.successFlag" style="color: #39c710"><CheckOutlined />成功</div>
            <div v-else style="color: #f50"><WarningOutlined />失败</div>
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
          @change="queryLogList"
          @showSizeChange="queryLogList"
          :show-total="(total) => `共${total}条`"
        />
      </div>
    </a-card>
  </a-drawer>
</template>
<script setup>
  import { reactive, ref } from 'vue';
  import { jobApi } from '/src/api/support/job-api';
  import { PAGE_SIZE_OPTIONS } from '/src/constants/common-const';
  import { smartSentry } from '/src/lib/smart-sentry';
  import TableOperator from '/src/components/support/table-operator/index.vue';
  import { TABLE_ID_CONST } from '/src/constants/support/table-id-const';

  const showFlag = ref(false);
  const title = ref('');

  function show(jobId, name) {
    queryForm.jobId = jobId;
    queryLogList();
    showFlag.value = true;
    title.value = name;
  }

  defineExpose({ show });

  const columns = ref([
    {
      title: '执行人',
      dataIndex: 'createName',
      width: 100,
      ellipsis: true,
    },
    {
      title: '执行参数',
      dataIndex: 'param',
      width: 80,
      ellipsis: true,
    },
    {
      title: '执行时间',
      dataIndex: 'executeStartTime',
      width: 200,
    },
    {
      title: '执行用时',
      dataIndex: 'executeTimeMillis',
      width: 100,
      ellipsis: true,
    },
    {
      title: '结果',
      dataIndex: 'successFlag',
      width: 80,
    },
    {
      title: '执行结果',
      dataIndex: 'executeResult',
      ellipsis: true,
      width: 100,
    },
    {
      title: 'ip',
      dataIndex: 'ip',
      width: 110,
    },
    {
      title: '进程id',
      dataIndex: 'processId',
      width: 60,
    },
    {
      title: '程序目录',
      dataIndex: 'programPath',
      ellipsis: true,
    },
  ]);

  // ---------------- 查询数据 -----------------------
  const queryFormState = {
    searchWord: '',
    jobId: null,
    successFlag: null,
    endTime: null,
    startTime: null,
    pageNum: 1,
    pageSize: 10,
  };
  const queryForm = reactive({ ...queryFormState });

  const tableLoading = ref(false);
  const tableData = ref([]);
  const total = ref(0);

  // 日期选择
  let searchDate = ref();

  function dateChange(dates, dateStrings) {
    queryForm.startTime = dateStrings[0];
    queryForm.endTime = dateStrings[1];
  }

  function resetQuery() {
    Object.assign(queryForm, queryFormState);
    queryLogList();
  }

  function onSearch() {
    queryForm.pageNum = 1;
    queryLogList();
  }

  async function queryLogList() {
    try {
      tableLoading.value = true;
      let responseModel = await jobApi.queryJobLog(queryForm);
      const list = responseModel.data.list;
      total.value = responseModel.data.total;
      tableData.value = list;
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      tableLoading.value = false;
    }
  }
</script>
