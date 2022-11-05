<!--
  * 单号 记录
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-07-21 21:55:12
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
-->
<template>
  <a-modal :visible="visible" title="每日生成结果记录" width="60%" :footer="null" @cancel="onClose">
    <a-table size="small" :dataSource="tableData" :columns="columns" bordered :pagination="false">
      <template #bodyCell="{ text, record, column }">
        <template v-if="column.dataIndex === 'successFlag'">
          <a-tag :color="text ? 'success' : 'error'">{{ text ? '成功' : '失败' }}</a-tag>
        </template>

        <template v-else-if="column.dataIndex === 'action'">
          <a-button @click="showDetail(record.operateLogId)" type="link">详情</a-button>
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
  </a-modal>
</template>
<script setup>
  import { reactive, ref } from 'vue';
  import { serialNumberApi } from '/@/api/support/serial-number/serial-number-api';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import { smartSentry } from '/@/lib/smart-sentry';

  defineExpose({
    showModal,
  });

  // ----------------------- 表单 隐藏 与 显示 ------------------------
  // 是否展示
  const visible = ref(false);

  function showModal(id) {
    queryForm.serialNumberId = id;
    queryForm.pageNum = 1;
    queryForm.pageSize = 10;
    ajaxQuery();
    visible.value = true;
  }

  function onClose() {
    visible.value = false;
  }

  // ----------------------- 表格 ------------------------
  const columns = reactive([
    {
      title: '单号ID',
      dataIndex: 'serialNumberId',
      width: 70,
    },
    {
      title: '日期',
      dataIndex: 'recordDate',
    },
    {
      title: '生成数量',
      dataIndex: 'count',
    },
    {
      title: '最后更新值',
      dataIndex: 'lastNumber',
    },
    {
      title: '上次生成时间',
      dataIndex: 'lastTime',
    },
  ]);

  const queryForm = reactive({
    serialNumberId: -1,
    pageNum: 1,
    pageSize: 10,
  });

  const tableLoading = ref(false);
  const tableData = ref([]);
  const total = ref(0);

  async function ajaxQuery() {
    try {
      tableLoading.value = true;
      let responseModel = await serialNumberApi.queryRecord(queryForm);
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
