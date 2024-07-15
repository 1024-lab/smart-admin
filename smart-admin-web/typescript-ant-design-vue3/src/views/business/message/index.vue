<template>
  <a-form class="smart-query-form">
    <a-row class="smart-query-form-row">
      <a-form-item label="关键字" class="smart-query-form-item">
        <a-input style="width: 200px" v-model:value.trim="queryForm.searchWord" placeholder="标题/内容"/>
      </a-form-item>

      <a-form-item label="消息类型" class="smart-query-form-item">
        <smart-enum-select v-model:value="queryForm.messageType" placeholder="请选择消息类型"
                           enum-name="MESSAGE_TYPE_ENUM"/>
      </a-form-item>

      <a-form-item label="接收方类型" class="smart-query-form-item">
        <smart-enum-select v-model:value="queryForm.receiverType" placeholder="请选择接收方类型"
                           enum-name="MESSAGE_RECEIVE_TYPE_ENUM"/>
      </a-form-item>

      <a-form-item class="smart-query-form-item" label="接收者">
        <ReceiverSelect v-model:value="queryForm.receiverId" placeholder="请选择接收者" :receiverType="queryForm.receiverType"/>
      </a-form-item>

      <a-form-item label="创建时间" class="smart-query-form-item">
        <a-space direction="vertical" :size="12">
          <a-range-picker v-model:value="searchDate" @change="dateChange"/>
        </a-space>
      </a-form-item>

      <a-form-item label="是否已读" class="smart-query-form-item">
        <a-radio-group v-model:value="queryForm.readFlag" @change="quickQuery">
          <a-radio-button :value="null">全部</a-radio-button>
          <a-radio-button :value="false">未读</a-radio-button>
          <a-radio-button :value="true">已读</a-radio-button>
        </a-radio-group>
      </a-form-item>

      <a-form-item class="smart-query-form-item smart-margin-left10">
        <a-button type="primary" @click="quickQuery">
          <template #icon>
            <SearchOutlined/>
          </template>
          查询
        </a-button>
        <a-button @click="resetQuery">
          <template #icon>
            <ReloadOutlined/>
          </template>
          重置
        </a-button>
      </a-form-item>
    </a-row>
  </a-form>

  <a-card size="small" :bordered="false" :hoverable="true">

    <a-table
        :scroll="{ x: 1800 }"
        size="small"
        :dataSource="tableData"
        :columns="columns"
        rowKey="messageId"
        :pagination="false"
        bordered
    >
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'messageType'">
          <span>{{ $smartEnumPlugin.getDescByValue('MESSAGE_TYPE_ENUM', text) }}</span>
        </template>
        <template v-if="column.dataIndex === 'receiverType'">
          <span>{{ $smartEnumPlugin.getDescByValue('MESSAGE_RECEIVE_TYPE_ENUM', text) }}</span>
        </template>
        <template v-if="column.dataIndex === 'readFlag'">
          <span>{{ record.readFlag ? '已读' : '未读' }}</span>
        </template>
        <template v-else-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button type="link" @click="toHandle(record)">去处理</a-button>
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
</template>
<script setup>
import {reactive, ref, onMounted} from 'vue';
import {messageApi} from '/@/api/business/message/message-api';
import {PAGE_SIZE, PAGE_SIZE_OPTIONS} from '/@/constants/common-const';
import SmartEnumSelect from '/@/components/framework/smart-enum-select//index.vue';
import {messageSetup} from "./components/message-handle-setup.js";
import ReceiverSelect from './components/receiver-select.vue';

const columns = reactive([
  {
    title: 'ID',
    width: 50,
    dataIndex: 'messageId',
  },
  {
    title: '消息类型',
    width: 80,
    dataIndex: 'messageType',
  },
  {
    title: '接收方',
    width: 150,
    dataIndex: 'receiverType',
  },
  {
    title: '接收者',
    width: 150,
    dataIndex: 'receiveName',
  },
  {
    title: '是否已读',
    width: 80,
    dataIndex: 'readFlag',
  },
  {
    title: '消息标题',
    dataIndex: 'title',
    width: 250,
  },
  {
    title: '消息内容',
    dataIndex: 'content',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 180
  },
  {
    title: '操作',
    dataIndex: 'action',
    fixed: 'right',
    width: 60
  },
]);

const queryFormState = {
  searchWord: '',
  messageType: null,
  dataId: null,
  readFlag: null,
  endDate: null,
  startDate: null,
  pageNum: 1,
  pageSize: PAGE_SIZE,
  searchCount: true,
  receiverType: null,
  receiverId:null
};
const queryForm = reactive({...queryFormState});
const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);
const operateModal = ref();

// 日期选择
let searchDate = ref();

function dateChange(dates, dateStrings) {
  queryForm.startDate = dateStrings[0];
  queryForm.endDate = dateStrings[1];
}

function resetQuery() {
  searchDate.value = [];
  Object.assign(queryForm, queryFormState);
  ajaxQuery();
}

function quickQuery() {
  queryForm.pageNum = 1;
  ajaxQuery();
}

// 查询 管理员查全部
async function ajaxQuery() {
  try {
    tableLoading.value = true;
    let responseModel = await messageApi.queryMessage(queryForm);
    const list = responseModel.data.list;
    total.value = responseModel.data.total;
    tableData.value = list;
  } catch (e) {
    console.log(e);
  } finally {
    tableLoading.value = false;
  }
}

let {toHandle} = messageSetup();




onMounted(ajaxQuery);
</script>
