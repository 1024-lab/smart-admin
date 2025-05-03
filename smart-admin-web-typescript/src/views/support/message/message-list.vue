<template>
  <!---------- 查询表单form begin ----------->
  <a-form class="smart-query-form">
    <a-row class="smart-query-form-row">
      <a-form-item label="关键词" class="smart-query-form-item">
        <a-input style="width: 150px" v-model:value="queryForm.searchWord" placeholder="关键词" />
      </a-form-item>
      <a-form-item label="类型" class="smart-query-form-item">
        <smart-enum-select style="width: 150px" v-model:value="queryForm.messageType" placeholder="消息类型" enum-name="MESSAGE_TYPE_ENUM" />
      </a-form-item>
      <a-form-item label="是否已读" class="smart-query-form-item">
        <SmartEnumSelect width="120px" enum-name="FLAG_NUMBER_ENUM" v-model:value="queryForm.readFlag" />
      </a-form-item>
      <a-form-item label="创建时间" class="smart-query-form-item">
        <a-range-picker v-model:value="queryForm.createTime" style="width: 200px" @change="onChangeCreateTime" />
      </a-form-item>
      <a-form-item class="smart-query-form-item">
        <a-button type="primary" @click="searchQuery">
          <template #icon>
            <SearchOutlined />
          </template>
          查询
        </a-button>
        <a-button @click="resetQuery" class="smart-margin-left10">
          <template #icon>
            <ReloadOutlined />
          </template>
          重置
        </a-button>
      </a-form-item>
    </a-row>
  </a-form>
  <!---------- 查询表单form end ----------->

  <a-card size="small" :bordered="false" :hoverable="true">
    <!---------- 表格操作行 begin ----------->
    <a-row class="smart-table-btn-block">
      <div class="smart-table-operate-block">
        <a-button @click="showForm" type="primary">
          <template #icon>
            <PlusOutlined />
          </template>
          发送消息
        </a-button>
      </div>
      <div class="smart-table-setting-block">
        <TableOperator v-model="columns" :tableId="TABLE_ID_CONST.SUPPORT.MAIL" :refresh="queryData" />
      </div>
    </a-row>
    <!---------- 表格操作行 end ----------->

    <!---------- 表格 begin ----------->
    <a-table size="small" :dataSource="tableData" :columns="columns" rowKey="telephoneId" bordered :loading="tableLoading" :pagination="false">
      <template #bodyCell="{ record, column, text }">
        <template v-if="column.dataIndex === 'readFlag'">
          {{ text ? '已读' : '未读' }}
        </template>
        <template v-if="column.dataIndex === 'messageType'">
          {{ $smartEnumPlugin.getDescByValue('MESSAGE_TYPE_ENUM', text) }}
        </template>
        <template v-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button @click="onDelete(record)" danger type="link">删除</a-button>
          </div>
        </template>
      </template>
    </a-table>
    <!---------- 表格 end ----------->

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
        @change="queryData"
        @showSizeChange="queryData"
        :show-total="(total) => `共${total}条`"
      />
    </div>

    <MessageSendForm ref="formRef" @reloadList="queryData" />
  </a-card>
</template>
<script setup lang="ts">
  import { onMounted, reactive, ref } from 'vue';
  import { message, Modal } from 'ant-design-vue';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { messageApi } from '/@/api/support/message-api';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { smartSentry } from '/@/lib/smart-sentry';
  import SmartEnumSelect from '/@/components/framework/smart-enum-select/index.vue';
  import TableOperator from '/@/components/support/table-operator/index.vue';
  import MessageSendForm from './components/message-send-form.vue';
  import { TABLE_ID_CONST } from '/@/constants/support/table-id-const';
  // ---------------------------- 表格列 ----------------------------

  const columns = ref([
    {
      title: '消息类型',
      dataIndex: 'messageType',
      ellipsis: true,
      width: 100,
    },
    {
      title: '消息标题',
      dataIndex: 'title',
      ellipsis: true,
    },
    {
      title: '消息内容',
      dataIndex: 'content',
      ellipsis: true,
    },
    {
      title: '接收人ID',
      dataIndex: 'receiverUserId',
      ellipsis: true,
      width: 100,
    },
    {
      title: '已读',
      dataIndex: 'readFlag',
      width: 50,
    },
    {
      title: '已读时间',
      dataIndex: 'readTime',
      width: 150,
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      width: 150,
    },
    {
      title: '操作',
      dataIndex: 'action',
      fixed: 'right',
      width: 60,
    },
  ]);

  // ---------------------------- 查询数据表单和方法 ----------------------------

  const queryFormState = {
    searchWord: undefined, //关键词
    pageNum: 1,
    pageSize: 10,
    createTime: [],
    readFlag: null,
    startDate: null,
    endDate: null,
    messageType: null,
  };
  function onChangeCreateTime(dates, dateStrings) {
    queryForm.startDate = dateStrings[0];
    queryForm.endDate = dateStrings[1];
  }
  // 查询表单form
  const queryForm = reactive({ ...queryFormState });
  // 表格加载loading
  const tableLoading = ref(false);
  // 表格数据
  const tableData = ref([]);
  // 总数
  const total = ref(0);

  // 重置查询条件
  function resetQuery() {
    let pageSize = queryForm.pageSize;
    Object.assign(queryForm, queryFormState);
    queryForm.pageSize = pageSize;
    queryData();
  }

  function searchQuery() {
    queryForm.pageNum = 1;
    queryData();
  }

  // 查询数据
  async function queryData() {
    tableLoading.value = true;
    try {
      let queryResult = await messageApi.queryAdminMessage(queryForm);
      tableData.value = queryResult.data.list;
      total.value = queryResult.data.total;
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      tableLoading.value = false;
    }
  }

  onMounted(queryData);

  // ---------------------------- 添加/修改 ----------------------------
  const formRef = ref();

  function showForm(data) {
    formRef.value.show(data);
  }

  //确认删除
  function onDelete(data) {
    Modal.confirm({
      title: '提示',
      content: '确定要删除选吗?',
      okText: '删除',
      okType: 'danger',
      onOk() {
        requestDelete(data);
      },
      cancelText: '取消',
      onCancel() {},
    });
  }

  async function requestDelete(data) {
    try {
      SmartLoading.show();
      await messageApi.deleteMessage(data.messageId);
      message.success('删除成功');
      queryData();
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      SmartLoading.hide();
    }
  }
</script>
