<!--
  * 文档列表抽屉
  *
  * @Author:    Mission
  * @Date:      2026-01-29
  * @Copyright  Mission
-->
<template>
  <a-drawer
    title="文档列表"
    :width="900"
    :open="visible"
    @close="onClose"
    :maskClosable="false"
    :destroyOnClose="true"
  >
    <!-- 上传文档 -->
    <a-row class="smart-margin-bottom10">
      <a-upload
        :customRequest="handleUpload"
        :showUploadList="false"
        accept=".pdf,.doc,.docx,.txt,.md"
      >
        <a-button type="primary">
          <template #icon>
            <UploadOutlined />
          </template>
          上传文档
        </a-button>
      </a-upload>
    </a-row>

    <!-- 文档列表 -->
    <a-table
      size="small"
      :dataSource="documentList"
      :columns="documentColumns"
      rowKey="id"
      bordered
      :loading="loading"
      :pagination="false"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'status'">
          <DictLabel :dict-code="DICT_CODE_ENUM.PROCESS_STATUS || 'PROCESS_STATUS'" :data-value="record.status" />
        </template>

        <template v-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button @click="showSegments(record)" type="link">切片</a-button>
            <a-button @click="deleteDocument(record)" danger type="link">删除</a-button>
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
        @change="loadDocuments"
        :show-total="(total) => `共${total}条`"
      />
    </div>

    <!-- 文档切片抽屉 -->
    <SegmentDrawer ref="segmentDrawerRef" />
  </a-drawer>
</template>

<script setup>
import { ref, reactive } from 'vue';
import { message, Modal } from 'ant-design-vue';
import { UploadOutlined } from '@ant-design/icons-vue';
import { SmartLoading } from '/@/components/framework/smart-loading';
import { aiDocumentsApi } from '/@/api/business/knowledge/ai-knowledge-api';
import { fileApi } from '/@/api/support/file-api';
import { smartSentry } from '/@/lib/smart-sentry';
import { DICT_CODE_ENUM } from '/@/constants/support/dict-const.js';
import DictLabel from '/@/components/support/dict-label/index.vue';
import SegmentDrawer from './segment-drawer.vue';

const visible = ref(false);
const loading = ref(false);
const currentKnowledge = ref(null);
const documentList = ref([]);
const total = ref(0);

const queryForm = reactive({
  knowledgeId: null,
  pageNum: 1,
  pageSize: 10,
});

const documentColumns = [
  {
    title: '文档名称',
    dataIndex: 'name',
    ellipsis: true,
  },
  {
    title: '字数',
    dataIndex: 'wordCount',
    width: 100,
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 100,
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
    width: 120,
  },
];

function show(knowledge) {
  currentKnowledge.value = knowledge;
  queryForm.knowledgeId = knowledge.id;
  queryForm.pageNum = 1;
  visible.value = true;
  loadDocuments();
}

function onClose() {
  visible.value = false;
  currentKnowledge.value = null;
  documentList.value = [];
}

async function loadDocuments() {
  loading.value = true;
  try {
    const res = await aiDocumentsApi.queryPage(queryForm);
    documentList.value = res.data.list;
    total.value = res.data.total;
  } catch (err) {
    smartSentry.captureError(err);
  } finally {
    loading.value = false;
  }
}

async function handleUpload({ file }) {
  SmartLoading.show();
  try {
    // 1. 上传文件到文件服务
    const formData = new FormData();
    formData.append('file', file);

    const uploadRes = await fileApi.uploadFile(formData, 1); // folder=1

    // 2. 创建文档记录
    await aiDocumentsApi.upload({
      knowledgeId: currentKnowledge.value.id,
      name: file.name,
      fileId: uploadRes.data.fileId,
    });

    message.success('文档上传成功，正在处理中...');
    loadDocuments();
  } catch (err) {
    smartSentry.captureError(err);
  } finally {
    SmartLoading.hide();
  }
}

function deleteDocument(record) {
  Modal.confirm({
    title: '提示',
    content: '确定要删除该文档吗？',
    okText: '删除',
    okType: 'danger',
    async onOk() {
      SmartLoading.show();
      try {
        await aiDocumentsApi.delete(record.id);
        message.success('删除成功');
        loadDocuments();
      } catch (err) {
        smartSentry.captureError(err);
      } finally {
        SmartLoading.hide();
      }
    },
  });
}

const segmentDrawerRef = ref();

function showSegments(record) {
  segmentDrawerRef.value.show(record);
}

defineExpose({
  show,
});
</script>
