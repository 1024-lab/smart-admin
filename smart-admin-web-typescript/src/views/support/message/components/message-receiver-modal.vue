<template>
  <a-modal v-model:open="visible" title="推送人" width="1100px" ok-text="确定" cancel-text="取消" @ok="onSubmit" @cancel="onClose" :zIndex="9999">
    <a-form class="smart-query-form">
      <a-row class="smart-query-form-row">
        <a-form-item label="关键词搜索" class="smart-query-form-item">
          <a-input v-model:value="queryParam.searchWord" :style="{ width: '250px' }" placeholder="请输入姓名" @change="selectSearchWord" />
        </a-form-item>
        <a-form-item class="smart-query-form-item">
          <a-button type="primary" @click="searchQuery">
            <template #icon>
              <SearchOutlined />
            </template>
            查询
          </a-button>
        </a-form-item>
        <a-form-item class="smart-query-form-item">
          <a-button @click="searchReset">
            <template #icon>
              <ReloadOutlined />
            </template>
            重置
          </a-button>
        </a-form-item>
      </a-row>
    </a-form>
    <a-table
      rowKey="employeeId"
      :loading="tableLoading"
      :columns="columns"
      :data-source="tableData"
      bordered
      :pagination="false"
      :row-selection="{
        selectedRowKeys: selectedRowKeyList,
        onChange: onSelectChange,
      }"
    >
    </a-table>
    <div class="smart-query-table-page">
      <a-pagination
        showSizeChanger
        showQuickJumper
        show-less-items
        :pageSizeOptions="PAGE_SIZE_OPTIONS"
        :defaultPageSize="queryParam.pageSize"
        v-model:current="queryParam.pageNum"
        v-model:pageSize="queryParam.pageSize"
        :total="total"
        @change="queryList"
        @showSizeChange="queryList"
        :show-total="(total) => `共${total}条`"
      />
    </div>
  </a-modal>
</template>

<script setup lang="ts">
  import { reactive, ref } from 'vue';
  import { PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { employeeApi } from '/@/api/system/employee-api';
  // ---------------查询条件----------------
  const queryParamState = {
    searchWord: null,
    keyword: null,
    pageNum: 1,
    pageSize: PAGE_SIZE,
  };

  const classId = ref();
  const queryParam = reactive({ ...queryParamState });
  const tableData = ref([]);
  let tableLoading = ref(false);
  const total = ref(0);

  // 搜索
  function searchQuery() {
    queryList();
  }

  // 弹窗打开
  const visible = ref(false);
  // 重置
  function searchReset() {
    Object.assign(queryParam, queryParamState);
    queryList();
  }
  function showModal(receiverIdList) {
    selectedRowKeyList.value = receiverIdList;
    queryList();
    visible.value = true;
  }
  function selectSearchWord(e) {
    queryParam.keyword = queryParam.searchWord;
  }
  const columns = [
    {
      title: '姓名',
      dataIndex: 'actualName',
      align: 'center',
    },
    {
      title: '手机号',
      dataIndex: 'phone',
      align: 'center',
    },
  ];
  // ----------查询------------
  async function queryList() {
    try {
      tableLoading.value = true;
      let res = await employeeApi.queryEmployee(queryParam);
      const list = res.data.list;
      total.value = res.data.total;
      tableData.value = list;
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      tableLoading.value = false;
    }
  }

  // 选择成绩表
  const selectedRowKeyList = ref([]);
  const selectedRowsList = ref([]);
  function onSelectChange(keyArray, selectedRows) {
    selectedRowKeyList.value = keyArray;
    selectedRowsList.value = selectedRows;
  }

  // 弹窗管理
  function onClose() {
    visible.value = false;
    let nameList = selectedRowsList.value.map((item) => item.actualName);
    emit('reloadList', selectedRowKeyList.value, nameList);
  }
  const emit = defineEmits(['reloadList']);

  // 提交
  function onSubmit() {
    try {
      onClose();
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      SmartLoading.hide();
    }
  }

  defineExpose({
    showModal,
  });
</script>

<style scoped lang="less"></style>
