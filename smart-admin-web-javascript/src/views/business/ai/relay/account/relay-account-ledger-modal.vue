<!--
  * Relay 用户账户 - 额度流水弹窗
  *
  * @Author:    Mission
  * @Date:      2026-03-15
  * @Copyright  Mission
-->
<template>
  <a-modal
      title="额度流水"
      :width="800"
      :open="visibleFlag"
      @cancel="onClose"
      :maskClosable="false"
      :footer="null"
  >
    <div style="margin-bottom: 12px">
      <span>员工：</span>
      <a-tag color="blue">{{ accountInfo.employeeName }} (ID: {{ accountInfo.employeeId }})</a-tag>
      <span style="margin-left: 12px">当前余额：</span>
      <a-tag color="green">{{ accountInfo.quotaRemain }}</a-tag>
    </div>

    <a-table
        size="small"
        :dataSource="tableData"
        :columns="columns"
        rowKey="ledgerId"
        bordered
        :loading="tableLoading"
        :pagination="false"
    >
      <template #bodyCell="{ text, column }">
        <template v-if="column.dataIndex === 'actionType'">
          {{ $smartEnumPlugin.getDescByValue('QUOTA_ACTION_TYPE_ENUM', text) }}
        </template>
        <template v-if="column.dataIndex === 'deltaQuota'">
          <span :style="{ color: text > 0 ? '#52c41a' : '#f5222d' }">
            {{ text > 0 ? '+' : '' }}{{ text }}
          </span>
        </template>
      </template>
    </a-table>

    <div class="smart-query-table-page" style="margin-top: 12px">
      <a-pagination
          showSizeChanger
          showQuickJumper
          show-less-items
          :pageSizeOptions="PAGE_SIZE_OPTIONS"
          :defaultPageSize="pageSize"
          v-model:current="pageNum"
          v-model:pageSize="pageSize"
          :total="total"
          @change="queryLedger"
          @showSizeChange="queryLedger"
          :show-total="(total) => `共${total}条`"
      />
    </div>
  </a-modal>
</template>
<script setup>
  import { ref } from 'vue';
  import { relayAccountApi } from '/@/api/business/ai/relay/relay-account-api';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { smartSentry } from '/@/lib/smart-sentry';

  const visibleFlag = ref(false);
  const accountInfo = ref({});
  const tableLoading = ref(false);
  const tableData = ref([]);
  const total = ref(0);
  const pageNum = ref(1);
  const pageSize = ref(10);

  const columns = [
    { title: 'ID', dataIndex: 'ledgerId', width: 80 },
    { title: '操作类型', dataIndex: 'actionType', width: 120 },
    { title: '变动额度', dataIndex: 'deltaQuota', width: 120 },
    { title: '变动后余额', dataIndex: 'balanceAfter', width: 120 },
    { title: '请求ID', dataIndex: 'requestId', width: 200, ellipsis: true },
    { title: '创建时间', dataIndex: 'createTime', width: 170, ellipsis: true },
  ];

  async function show(record) {
    accountInfo.value = record;
    pageNum.value = 1;
    visibleFlag.value = true;
    await queryLedger();
  }

  function onClose() {
    visibleFlag.value = false;
    tableData.value = [];
  }

  async function queryLedger() {
    tableLoading.value = true;
    try {
      let result = await relayAccountApi.queryLedger(accountInfo.value.employeeId, pageNum.value, pageSize.value);
      tableData.value = result.data.list;
      total.value = result.data.total;
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      tableLoading.value = false;
    }
  }

  defineExpose({ show });
</script>
