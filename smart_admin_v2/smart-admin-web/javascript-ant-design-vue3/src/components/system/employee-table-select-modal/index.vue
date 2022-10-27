<!--
  * 员工 表格 弹窗 选择框 
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-08-19 23:09:02 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
-->
<template>
  <a-modal v-model:visible="visible" :width="900" title="选择人员" @cancel="closeModal" @ok="onSelectEmployee">
    <a-form class="smart-query-form">
      <a-row class="smart-query-form-row">
        <a-form-item label="关键字" class="smart-query-form-item">
          <a-input style="width: 150px" v-model:value="params.keyword" placeholder="关键字" />
        </a-form-item>
        <a-form-item label="部门" class="smart-query-form-item">
          <DepartmentTreeSelect style="width: 200px" ref="departmentTreeSelect" v-model:value="params.departmentId" />
        </a-form-item>
        <a-form-item label="状态" class="smart-query-form-item">
          <a-select style="width: 120px" v-model:value="params.disabledFlag" placeholder="请选择状态" allowClear>
            <a-select-option :key="1"> 禁用 </a-select-option>
            <a-select-option :key="0"> 启用 </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item class="smart-query-form-item smart-margin-left10">
          <a-button type="primary" @click="queryEmployee">
            <template #icon>
              <SearchOutlined />
            </template>
            查询
          </a-button>
          <a-button @click="reset" class="smart-margin-left10">
            <template #icon>
              <ReloadOutlined />
            </template>
            重置
          </a-button>
        </a-form-item>
      </a-row>
    </a-form>
    <a-table
      :row-selection="{ selectedRowKeys: selectedRowKeyList, onChange: onSelectChange }"
      :loading="tableLoading"
      size="small"
      :columns="columns"
      :data-source="tableData"
      :pagination="false"
      bordered
      rowKey="employeeId"
      :scroll="{ y: 300 }"
    >
      <template #bodyCell="{ text, column }">
        <template v-if="column.dataIndex === 'disabledFlag'">
          <a-tag :color="text ? 'error' : 'processing'">{{ text ? '禁用' : '启用' }}</a-tag>
        </template>

        <template v-if="column.dataIndex === 'gender'">
          <span>{{ $smartEnumPlugin.getDescByValue('GENDER_ENUM', text) }}</span>
        </template>
      </template>
    </a-table>
    <div class="smart-query-table-page">
      <a-pagination
        showSizeChanger
        showQuickJumper
        show-less-items
        :pageSizeOptions="PAGE_SIZE_OPTIONS"
        :defaultPageSize="params.pageSize"
        v-model:current="params.pageNum"
        v-model:pageSize="params.pageSize"
        :total="total"
        @change="queryEmployee"
        @showSizeChange="queryEmployee"
        :show-total="(total) => `共${total}条`"
      />
    </div>
  </a-modal>
</template>
<script setup>
  import { message } from 'ant-design-vue';
  import { computed, reactive, ref } from 'vue';
  import { employeeApi } from '/@/api/system/employee/employee-api';
  import { PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import DepartmentTreeSelect from '/@/components/system/department-tree-select/index.vue';
  import { smartSentry } from '/@/lib/smart-sentry';

  // ----------------------- 以下是字段定义 emits props ---------------------
  const emits = defineEmits('selectData');
  defineExpose({
    showModal,
  });

  // ----------------------- modal  显示与隐藏 ---------------------

  const visible = ref(false);
  async function showModal(selectEmployeeId) {
    selectedRowKeyList.value = selectEmployeeId || [];
    visible.value = true;
    queryEmployee();
  }
  function closeModal() {
    Object.assign(params, defaultParams);
    selectedRowKeyList.value = [];
    visible.value = false;
  }
  // ----------------------- 员工查询表单与查询 ---------------------
  const tableLoading = ref(false);
  const departmentTreeSelect = ref();
  const total = ref();

  let defaultParams = {
    departmentId: undefined,
    disabledFlag: undefined,
    employeeIdList: undefined,
    keyword: undefined,
    searchCount: undefined,
    pageNum: 1,
    pageSize: PAGE_SIZE,
    sortItemList: undefined,
  };
  const params = reactive({ ...defaultParams });
  function reset() {
    Object.assign(params, defaultParams);
    queryEmployee();
  }
  async function queryEmployee() {
    tableLoading.value = true;
    try {
      let res = await employeeApi.queryEmployee(params);
      tableData.value = res.data.list;
      total.value = res.data.total;
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      tableLoading.value = false;
    }
  }

  // ----------------------- 员工表格选择 ---------------------
  let selectedRowKeyList = ref([]);
  const hasSelected = computed(() => selectedRowKeyList.value.length > 0);

  function onSelectChange(selectedRowKeys) {
    selectedRowKeyList.value = selectedRowKeys;
  }

  function onSelectEmployee() {
    if (!hasSelected.value) {
      message.warning('请选择角色人员');
      return;
    }
    emits('selectData', selectedRowKeyList.value);
    closeModal();
  }

  // ----------------------- 员工表格渲染 ---------------------
  const tableData = ref([]);
  //字段
  const columns = [
    {
      title: '姓名',
      dataIndex: 'actualName',
    },
    {
      title: '手机号',
      dataIndex: 'phone',
    },
    {
      title: '性别',
      dataIndex: 'gender',
    },
    {
      title: '登录账号',
      dataIndex: 'loginName',
    },
    {
      title: '状态',
      dataIndex: 'disabledFlag',
    },
  ];
</script>
