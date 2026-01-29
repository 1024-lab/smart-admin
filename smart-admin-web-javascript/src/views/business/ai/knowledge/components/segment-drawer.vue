<!--
  * 文档切片抽屉
  *
  * @Author:    Mission
  * @Date:      2026-01-29
  * @Copyright  Mission
-->
<template>
  <a-drawer
    title="文档切片"
    :width="800"
    :open="visible"
    @close="onClose"
    :maskClosable="false"
    :destroyOnClose="true"
  >
    <a-descriptions :column="1" bordered size="small" class="smart-margin-bottom10">
      <a-descriptions-item label="文档名称">{{ currentDocument?.name }}</a-descriptions-item>
      <a-descriptions-item label="总字数">{{ currentDocument?.wordCount }}</a-descriptions-item>
      <a-descriptions-item label="状态">
        <DictLabel :dict-code="DICT_CODE_ENUM.PROCESS_STATUS || 'PROCESS_STATUS'" :data-value="currentDocument?.status" />
      </a-descriptions-item>
    </a-descriptions>

    <!-- 切片列表 -->
    <a-table
      size="small"
      :dataSource="segmentList"
      :columns="segmentColumns"
      rowKey="id"
      bordered
      :loading="loading"
      :pagination="false"
      :scroll="{ y: 500 }"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'status'">
          <DictLabel :dict-code="DICT_CODE_ENUM.PROCESS_STATUS || 'PROCESS_STATUS'" :data-value="record.status" />
        </template>

        <template v-if="column.dataIndex === 'content'">
          <div style="max-height: 100px; overflow-y: auto; white-space: pre-wrap;">
            {{ record.content }}
          </div>
        </template>
      </template>
    </a-table>

    <!-- 分页 -->
    <div class="smart-query-table-page">
      <a-pagination
        v-model:current="queryForm.pageNum"
        v-model:pageSize="queryForm.pageSize"
        :total="total"
        @change="loadSegments"
        :show-total="(total) => `共${total}条`"
      />
    </div>
  </a-drawer>
</template>

<script setup>
import { ref, reactive } from 'vue';
import { SmartLoading } from '/@/components/framework/smart-loading';
import { aiDocumentSegmentsApi } from '/@/api/business/knowledge/ai-knowledge-api';
import { smartSentry } from '/@/lib/smart-sentry';
import { DICT_CODE_ENUM } from '/@/constants/support/dict-const.js';
import DictLabel from '/@/components/support/dict-label/index.vue';

const visible = ref(false);
const loading = ref(false);
const currentDocument = ref(null);
const segmentList = ref([]);
const total = ref(0);

const queryForm = reactive({
  documentId: null,
  pageNum: 1,
  pageSize: 10,
});

const segmentColumns = [
  {
    title: '序号',
    dataIndex: 'position',
    width: 80,
  },
  {
    title: '内容',
    dataIndex: 'content',
    ellipsis: true,
  },
  {
    title: '字数',
    dataIndex: 'wordCount',
    width: 80,
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 100,
  },
];

function show(document) {
  currentDocument.value = document;
  queryForm.documentId = document.id;
  queryForm.pageNum = 1;
  visible.value = true;
  loadSegments();
}

function onClose() {
  visible.value = false;
  currentDocument.value = null;
  segmentList.value = [];
}

async function loadSegments() {
  loading.value = true;
  try {
    const res = await aiDocumentSegmentsApi.queryPage(queryForm);
    segmentList.value = res.data.list;
    total.value = res.data.total;
  } catch (err) {
    smartSentry.captureError(err);
  } finally {
    loading.value = false;
  }
}

defineExpose({
  show,
});
</script>
