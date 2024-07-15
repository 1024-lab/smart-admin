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
          <a-input style="width: 300px" v-model:value="queryForm.searchWord" placeholder="请输入关键字"
                   :maxlength="30"/>
        </a-form-item>
        
        <a-form-item class="smart-query-form-item smart-margin-left10">
          <a-button-group>
            <a-button type="primary" @click="onSearch" v-privilege="'support:job:query'">
              <template #icon>
                <ReloadOutlined/>
              </template>
              查询
            </a-button>
            <a-button @click="resetQuery" v-privilege="'support:job:query'">
              <template #icon>
                <SearchOutlined/>
              </template>
              重置
            </a-button>
          </a-button-group>
        </a-form-item>
      </a-row>
    </a-form>
    
    <a-card size="small" :bordered="false" :hoverable="true">
      <a-row justify="end">
        <TableOperator class="smart-margin-bottom5" v-model="columns" :tableId="TABLE_ID_CONST.SUPPORT.JOB"
                       :refresh="ajaxQuery"/>
      </a-row>
      
      <a-table size="small" :loading="tableLoading" bordered :dataSource="tableData" :columns="columns" rowKey="jobId"
               :pagination="false">
        <template #bodyCell="{ record, column }">
          <template v-if="column.dataIndex === 'jobClass'">
            <a-tooltip>
              <template #title>{{ record.jobClass }}</template>
              {{ record.jobClass.split('.').pop() }}
            </a-tooltip>
          </template>
          <template v-if="column.dataIndex === 'triggerType'">
            <a-tag v-if="record.cron" color="success">CRON表达式</a-tag>
            <a-tag v-else color="processing">固定间隔</a-tag>
          </template>
          <template v-if="column.dataIndex === 'triggerTime'">
            <span>{{ record.cron ? record.cron : record.fixedDelay }}</span>
          </template>
          <template v-if="column.dataIndex === 'lastJob'">
            <div v-if="record.lastJobLog">
              <a-tooltip>
                <template #title>{{ handleExecuteResult(record.lastJobLog.executeResult) }}</template>
                <CheckOutlined v-if="record.lastJobLog.successFlag" style="color:#87d068"/>
                <WarningOutlined v-else style="color:#f50"/>
                {{ record.lastJobLog.executeStartTime }}
              </a-tooltip>
            </div>
          </template>
          <template v-if="column.dataIndex === 'nextJob'">
            <a-tooltip>
              <template #title>
                <div>下次执行</div>
                <div v-for="item in record.nextJobExecuteTimeList" :key="item">{{ item }}</div>
              </template>
              {{ record.nextJobExecuteTimeList[0] }}
            </a-tooltip>
          </template>
          <template v-if="column.dataIndex === 'enabledFlag'">
            <a-switch v-model:checked="record.enabledFlag"/>
          </template>
          <template v-if="column.dataIndex === 'action'">
            <div class="smart-table-operate">
              <a-button v-privilege="'support:job:execute'" type="link">执行</a-button>
              <a-button v-privilege="'support:job:update'" type="link">编辑</a-button>
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
          @change="ajaxQuery"
          @showSizeChange="ajaxQuery"
          :show-total="(total) => `共${total}条`"
        />
      </div>
    </a-card>
  </div>
</template>
<script setup>
import { computed, onMounted, reactive, ref } from 'vue';
import { jobApi } from '/@/api/support/job/job-api';
import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { smartSentry } from '/@/lib/smart-sentry';
import TableOperator from '/@/components/support/table-operator/index.vue';
import { TABLE_ID_CONST } from '/@/constants/support/table-id-const';

const columns = ref([
  {
    title: 'id',
    width: 50,
    dataIndex: 'jobId'
  },
  {
    title: '任务名称',
    dataIndex: 'jobName',
    width: 150,
    ellipsis: true
  },
  {
    title: '执行类',
    dataIndex: 'jobClass',
    width: 180
  },
  {
    title: '触发类型',
    dataIndex: 'triggerType',
    width: 110
  },
  {
    title: '触发时间',
    dataIndex: 'triggerTime',
    width: 150
  },
  {
    title: '上次执行',
    width: 180,
    dataIndex: 'lastJob'
  },
  {
    title: '下次执行',
    width: 150,
    dataIndex: 'nextJob'
  },
  {
    title: '状态',
    dataIndex: 'enabledFlag',
    width: 80
  },
  {
    title: '执行参数',
    dataIndex: 'param',
    width: 300,
    ellipsis: true
  },
  {
    title: '任务描述',
    dataIndex: 'remark',
    ellipsis: true,
    width: 180
  },
  {
    title: '更新人',
    dataIndex: 'updateName',
    width: 90
  },
  {
    title: '更新时间',
    dataIndex: 'updateTime',
    width: 150
  },
  {
    title: '操作',
    dataIndex: 'action',
    fixed: 'right',
    width: 100
  }
]);

// ---------------- 查询数据 -----------------------

const queryFormState = {
  searchWord: '',
  enabledFlag: null,
  pageNum: 1,
  pageSize: 10
};
const queryForm = reactive({...queryFormState});

const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);

function resetQuery() {
  Object.assign(queryForm, queryFormState);
  ajaxQuery();
}

function onSearch() {
  queryForm.pageNum = 1;
  ajaxQuery();
}

// 上次处理结果展示 最多展示300
function handleExecuteResult(result) {
  let num = 400;
  return result
    ? result.substring(0, num) + (result.length > num ? ' ...' : '')
    : '';
}

async function ajaxQuery() {
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

const jobClass = computed(() => {

});

// ------------------------- 表单操作 弹窗 ------------------------------


onMounted(ajaxQuery);
</script>
