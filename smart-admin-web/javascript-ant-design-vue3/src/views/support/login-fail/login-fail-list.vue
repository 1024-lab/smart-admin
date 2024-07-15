<!--
  * 登录失败锁定
  *
  * @Author:    1024创新实验室-主任-卓大
  * @Date:      2023-10-17 18:02:37
  * @Copyright  1024创新实验室
-->
<template>
  <!---------- 查询表单form begin ----------->
  <a-form class="smart-query-form">
    <a-row class="smart-query-form-row">
      <a-form-item label="登录名" class="smart-query-form-item">
        <a-input style="width: 300px" v-model:value="queryForm.loginName" placeholder="登录名" />
      </a-form-item>
      <a-form-item label="快速筛选" class="smart-query-form-item">
        <a-radio-group v-model:value="queryForm.lockFlag" @change="onSearch" button-style="solid">
          <a-radio-button :value="undefined">全部</a-radio-button>
          <a-radio-button :value="true">已锁定</a-radio-button>
          <a-radio-button :value="false">未锁定</a-radio-button>
        </a-radio-group>
      </a-form-item>
      <a-form-item label="锁定时间" class="smart-query-form-item">
        <a-range-picker
          v-model:value="queryForm.loginLockBeginTime"
          :presets="defaultTimeRanges"
          style="width: 220px"
          @change="onChangeLoginLockBeginTime"
        />
      </a-form-item>
      <a-form-item class="smart-query-form-item">
        <a-button-group>
          <a-button type="primary" @click="onSearch">
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
        </a-button-group>
      </a-form-item>
    </a-row>
  </a-form>
  <!---------- 查询表单form end ----------->

  <a-card size="small" :bordered="false" :hoverable="true">
    <!---------- 表格操作行 begin ----------->
    <a-row class="smart-table-btn-block">
      <div class="smart-table-operate-block">
        <a-button @click="confirmBatchDelete" danger :disabled="selectedRowKeyList.length === 0">
          <template #icon>
            <DeleteOutlined />
          </template>
          解除锁定
        </a-button>
      </div>
      <div class="smart-table-setting-block">
        <TableOperator v-model="columns" :tableId="null" :refresh="queryData" />
      </div>
    </a-row>
    <!---------- 表格操作行 end ----------->

    <!---------- 表格 begin ----------->
    <a-table
      size="small"
      :dataSource="tableData"
      :columns="columns"
      rowKey="loginFailId"
      bordered
      :loading="tableLoading"
      :pagination="false"
      :row-selection="{ selectedRowKeys: selectedRowKeyList, onChange: onSelectChange }"
    >
      <template #bodyCell="{ text, column }">
        <template v-if="column.dataIndex === 'userType'">
          <span>{{ $smartEnumPlugin.getDescByValue('USER_TYPE_ENUM', text) }}</span>
        </template>
        <template v-if="column.dataIndex === 'lockFlag'">
          <template v-if="text">
            <a-tag color="error">已锁定</a-tag>
          </template>
          <template v-if="!text">
            <a-tag color="success">未锁定</a-tag>
          </template>
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
        @change="onSearch"
        @showSizeChange="onSearch"
        :show-total="(total) => `共${total}条`"
      />
    </div>
  </a-card>
</template>
<script setup>
  import { reactive, ref, onMounted } from 'vue';
  import { message, Modal } from 'ant-design-vue';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { loginFailApi } from '/@/api/support/login-fail-api';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { smartSentry } from '/@/lib/smart-sentry';
  import TableOperator from '/@/components/support/table-operator/index.vue';
  import { defaultTimeRanges } from '/@/lib/default-time-ranges';

  // ---------------------------- 表格列 ----------------------------

  const columns = ref([
    {
      title: '登录名',
      dataIndex: 'loginName',
    },
    {
      title: '用户类型',
      dataIndex: 'userType',
    },
    {
      title: '登录失败次数',
      dataIndex: 'loginFailCount',
    },
    {
      title: '锁定状态',
      dataIndex: 'lockFlag',
    },
    {
      title: '锁定开始时间',
      dataIndex: 'loginLockBeginTime',
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
    },
    {
      title: '更新时间',
      dataIndex: 'updateTime',
    },
  ]);

  // ---------------------------- 查询数据表单和方法 ----------------------------

  const queryFormState = {
    loginName: undefined, //登录名
    lockFlag: true, // 锁定状态
    loginLockBeginTime: [], //登录失败锁定时间
    loginLockBeginTimeBegin: undefined, //登录失败锁定时间 开始
    loginLockBeginTimeEnd: undefined, //登录失败锁定时间 结束
    pageNum: 1,
    pageSize: 10,
  };
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
    queryForm.lockFlag = undefined;
    queryData();
  }

  // 查询数据

  function onSearch() {
    queryForm.pageNum = 1;
    queryData();
  }

  async function queryData() {
    tableLoading.value = true;
    try {
      let queryResult = await loginFailApi.queryPage(queryForm);
      tableData.value = queryResult.data.list;
      total.value = queryResult.data.total;
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      tableLoading.value = false;
    }
  }

  function onChangeLoginLockBeginTime(dates, dateStrings) {
    queryForm.loginLockBeginTimeBegin = dateStrings[0];
    queryForm.loginLockBeginTimeEnd = dateStrings[1];
  }

  onMounted(queryData);

  // ---------------------------- 批量解除锁定 ----------------------------

  // 选择表格行
  const selectedRowKeyList = ref([]);

  function onSelectChange(selectedRowKeys) {
    selectedRowKeyList.value = selectedRowKeys;
  }

  // 批量解除锁定
  function confirmBatchDelete() {
    Modal.confirm({
      title: '提示',
      content: '确定要批量解除锁定这些数据吗?',
      okText: '解锁',
      okType: 'danger',
      onOk() {
        requestBatchDelete();
      },
      cancelText: '取消',
      onCancel() {},
    });
  }

  //请求批量删除
  async function requestBatchDelete() {
    try {
      SmartLoading.show();
      await loginFailApi.batchDelete(selectedRowKeyList.value);
      message.success('解锁成功');
      queryData();
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      SmartLoading.hide();
    }
  }
</script>
