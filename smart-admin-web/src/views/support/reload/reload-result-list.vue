<!--
  * reload 结果
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-07-21 21:55:12
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
-->
<template>
  <a-modal :open="visible" title="reload结果列表" width="60%" :footer="null" @cancel="onClose">
    <a-button type="primary" @click="ajaxQuery" size="small">
      <template #icon>
        <ReloadOutlined />
      </template>
      刷新
    </a-button>
    <a-table :scroll="{ y: 350 }" size="small" bordered rowKey="id" class="smart-margin-top10" :dataSource="tableData" :columns="columns">
      <template #bodyCell="{ text, column }">
        <template v-if="column.dataIndex === 'result'">
          <a-tag :color="text ? 'success' : 'error'">{{ text ? '成功' : '失败' }}</a-tag>
        </template>
      </template>
      <template #expandedRowRender="{ record }">
        <pre style="margin: 0; font-size: 12px">
          {{ record.exception }}
        </pre>
      </template>
    </a-table>
  </a-modal>
</template>
<script setup>
  import { reactive, ref } from 'vue';
  import { reloadApi } from '/@/api/support/reload-api';
import { smartSentry } from '/@/lib/smart-sentry';
  defineExpose({
    showModal,
  });

  // ----------------------- 表单 隐藏 与 显示 ------------------------
  // 是否展示
  const visible = ref(false);

  function showModal(tag) {
    queryTag = tag;
    ajaxQuery();
    visible.value = true;
  }

  function onClose() {
    visible.value = false;
  }

  //------------------------ 表格查询 ---------------------
  let queryTag = '';
  const tableLoading = ref(false);
  const tableData = ref([]);

  async function ajaxQuery() {
    try {
      tableLoading.value = true;
      let res = await reloadApi.queryReloadResult(queryTag);
      let count = 1;
      for (const item of res.data) {
        item.id = count++;
      }
      tableData.value = res.data;
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      tableLoading.value = false;
    }
  }

  //------------------------ 表格列 ---------------------

  const columns = reactive([
    {
      title: '标签',
      dataIndex: 'tag',
    },
    {
      title: '参数',
      dataIndex: 'args',
    },
    {
      title: '运行结果',
      dataIndex: 'result',
    },
    {
      title: '异常',
      dataIndex: 'exception',
      ellipsis: true,
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
    },
  ]);
</script>
