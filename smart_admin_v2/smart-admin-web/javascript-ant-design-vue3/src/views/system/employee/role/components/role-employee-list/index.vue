<!--
  * 角色 员工 列表
  *
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2022-09-12 22:34:00
  * @Wechat:    zhuda1024
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
  *
-->
<template>
  <div>
    <div class="header">
      <div>
        关键字：
        <a-input style="width: 250px" v-model:value="queryForm.keywords" placeholder="姓名/手机号/登录账号" />

        <a-button class="button-style" v-if="selectRoleId" type="primary" @click="queryRoleEmployee">搜索</a-button>
        <a-button class="button-style" v-if="selectRoleId" type="default" @click="resetQueryRoleEmployee">重置</a-button>
      </div>

      <div>
        <a-button class="button-style" v-if="selectRoleId" type="primary" @click="addRoleEmployee" v-privilege="'system:role:employee:add'"
          >添加员工</a-button
        >
        <a-button class="button-style" v-if="selectRoleId" type="primary" danger @click="batchDelete" v-privilege="'system:role:employee:batch:delete'"
          >批量移除</a-button
        >
      </div>
    </div>
    <a-table
      :loading="tableLoading"
      :dataSource="tableData"
      :columns="columns"
      :pagination="false"
      :scroll="{ y: 400 }"
      rowKey="employeeId"
      :row-selection="{ selectedRowKeys: selectedRowKeyList, onChange: onSelectChange }"
      size="small"
      bordered
    >
      <template #bodyCell="{ text, record, column }">
        <template v-if="column.dataIndex === 'disabledFlag'">
          <a-tag :color="text ? 'error' : 'processing'">{{ text ? '禁用' : '启用' }}</a-tag>
        </template>
        <template v-else-if="column.dataIndex === 'gender'">
          <span>{{ $smartEnumPlugin.getDescByValue('GENDER_ENUM', text) }}</span>
        </template>
        <template v-if="column.dataIndex === 'operate'">
          <a @click="deleteEmployeeRole(record.employeeId)" v-privilege="'system:role:employee:delete'">移除</a>
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
        @change="queryRoleEmployee"
        @showSizeChange="queryRoleEmployee"
        :show-total="showTableTotal"
      />
    </div>
    <EmployeeTableSelectModal ref="selectEmployeeModal" @selectData="selectData" />
  </div>
</template>
<script setup>
  import { message, Modal } from 'ant-design-vue';
  import _ from 'lodash';
  import { computed, inject, onMounted, reactive, ref, watch } from 'vue';
  import { roleApi } from '/@/api/system/role/role-api';
  import { PAGE_SIZE, showTableTotal, PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import EmployeeTableSelectModal from '/@/components/system/employee-table-select-modal/index.vue';
  import { smartSentry } from '/@/lib/smart-sentry';

  // ----------------------- 以下是字段定义 emits props ---------------------
  let selectRoleId = inject('selectRoleId');

  // ----------------------- 员工列表：显示和搜索 ------------------------
  watch(
    () => selectRoleId.value,
    () => queryRoleEmployee()
  );

  onMounted(queryRoleEmployee);

  const defaultQueryForm = {
    pageNum: 1,
    pageSize: PAGE_SIZE,
    roleId: undefined,
    keywords: undefined,
  };
  // 查询表单
  const queryForm = reactive({ ...defaultQueryForm });
  // 总数
  const total = ref(0);
  // 表格数据
  const tableData = ref([]);
  // 表格loading效果
  const tableLoading = ref(false);

  function resetQueryRoleEmployee() {
    queryForm.keywords = '';
    queryRoleEmployee();
  }

  async function queryRoleEmployee() {
    try {
      tableLoading.value = true;
      queryForm.roleId = selectRoleId.value;
      let res = await roleApi.queryRoleEmployee(queryForm);
      tableData.value = res.data.list;
      total.value = res.data.total;
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      tableLoading.value = false;
    }
  }

  const columns = reactive([
    {
      title: '姓名',
      dataIndex: 'actualName',
    },
    {
      title: '手机号',
      dataIndex: 'phone',
    },
    {
      title: '登录账号',
      dataIndex: 'loginName',
    },
    {
      title: '部门',
      dataIndex: 'departmentName',
    },
    {
      title: '状态',
      dataIndex: 'disabledFlag',
    },
    {
      title: '操作',
      dataIndex: 'operate',
      width: 60,
    },
  ]);

  // ----------------------- 添加成员 ---------------------------------
  const selectEmployeeModal = ref();

  async function addRoleEmployee() {
    let res = await roleApi.getRoleAllEmployee(selectRoleId.value);
    let selectedIdList = res.data.map((e) => e.roleId) || [];
    selectEmployeeModal.value.showModal(selectedIdList);
  }

  async function selectData(list) {
    if (_.isEmpty(list)) {
      message.warning('请选择角色人员');
      return;
    }
    SmartLoading.show();
    try {
      let params = {
        employeeIdList: list,
        roleId: selectRoleId.value,
      };
      await roleApi.batchAddRoleEmployee(params);
      message.success('添加成功');
      await queryRoleEmployee();
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      SmartLoading.hide();
    }
  }

  // ----------------------- 移除成员 ---------------------------------
  // 删除角色成员方法
  async function deleteEmployeeRole(employeeId) {
    Modal.confirm({
      title: '提示',
      content: '确定要删除该角色成员么？',
      okText: '确定',
      okType: 'danger',
      async onOk() {
        SmartLoading.show();
        try {
          await roleApi.deleteEmployeeRole(employeeId, selectRoleId.value);
          message.success('移除成功');
          await queryRoleEmployee();
        } catch (e) {
          smartSentry.captureError(e);
        } finally {
          SmartLoading.hide();
        }
      },
      cancelText: '取消',
      onCancel() {},
    });
  }

  // ----------------------- 批量删除 ---------------------------------

  const selectedRowKeyList = ref([]);
  const hasSelected = computed(() => selectedRowKeyList.value.length > 0);

  function onSelectChange(selectedRowKeys) {
    selectedRowKeyList.value = selectedRowKeys;
  }

  // 批量移除
  function batchDelete() {
    if (!hasSelected.value) {
      message.warning('请选择要删除的角色成员');
      return;
    }
    Modal.confirm({
      title: '提示',
      content: '确定移除这些角色成员吗？',
      okText: '确定',
      okType: 'danger',
      async onOk() {
        SmartLoading.show();
        try {
          let params = {
            employeeIdList: selectedRowKeyList.value,
            roleId: selectRoleId.value,
          };
          await roleApi.batchRemoveRoleEmployee(params);
          message.success('移除成功');
          selectedRowKeyList.value = [];
          await queryRoleEmployee();
        } catch (e) {
          smartSentry.captureError(e);
        } finally {
          SmartLoading.hide();
        }
      },
      cancelText: '取消',
      onCancel() {},
    });
  }
</script>

<style scoped lang="less">
  .header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin: 20px 0;
  }
  .button-style {
    margin: 0 10px;
  }
</style>
