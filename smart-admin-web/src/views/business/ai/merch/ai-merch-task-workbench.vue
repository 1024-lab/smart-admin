<template>
  <a-card size="small" :bordered="false" :hoverable="true">
    <a-row :gutter="16">
      <a-col :xs="24" :xl="10">
        <a-form layout="vertical">
          <a-form-item label="服装图片">
            <FileUpload
              :defaultFileList="sourceFileList"
              :folder="FILE_FOLDER_TYPE_ENUM.AI_MERCH.value"
              :maxUploadSize="1"
              :maxSize="20"
              accept=".jpg,.jpeg,.png,.gif,.bmp,.webp"
              buttonText="上传服装图片"
              @change="handleSourceChange"
            />
          </a-form-item>
          <a-form-item label="提示词模型">
            <AiModelSelect v-model:value="form.promptModelId" :provider="AI_MODEL_PROVIDER_ENUM.OPENAI.value" placeholder="请选择提示词模型" />
          </a-form-item>
          <a-form-item label="图片模型">
            <AiModelSelect v-model:value="form.imageModelId" :provider="AI_MODEL_PROVIDER_ENUM.OPENAI.value" placeholder="请选择图片模型" />
          </a-form-item>
          <a-form-item label="视频模型">
            <AiModelSelect v-model:value="form.videoModelId" :provider="AI_MODEL_PROVIDER_ENUM.OPENAI.value" placeholder="请选择视频模型" />
          </a-form-item>
          <a-form-item label="提示词1">
            <a-textarea v-model:value="form.promptTemplate" :rows="20" placeholder="请输入提示词模板" />
          </a-form-item>
          <a-space wrap>
            <a-button type="primary" v-privilege="'ai:merch:create'" @click="createTask">生成提示词</a-button>
            <a-button
              type="primary"
              v-privilege="'ai:merch:generateImage'"
              :disabled="!canGenerateImage(currentTask)"
              @click="generateImage(currentTask?.taskId)"
            >
              生成图片
            </a-button>
            <a-button
              type="primary"
              v-privilege="'ai:merch:generateVideo'"
              :disabled="!canGenerateVideo(currentTask)"
              @click="generateVideo(currentTask?.taskId)"
            >
              生成视频
            </a-button>
          </a-space>
        </a-form>
      </a-col>

      <a-col :xs="24" :xl="14">
        <div class="result-wrapper" v-if="currentTask">
          <a-descriptions title="当前任务" :column="1" size="small" bordered>
            <a-descriptions-item label="任务ID">{{ currentTask.taskId }}</a-descriptions-item>
            <a-descriptions-item label="提示词模型">{{ currentTask.promptModelName || '-' }}</a-descriptions-item>
            <a-descriptions-item label="图片模型">{{ currentTask.imageModelName || '-' }}</a-descriptions-item>
            <a-descriptions-item label="视频模型">{{ currentTask.videoModelName || '-' }}</a-descriptions-item>
            <a-descriptions-item label="提示词状态">
              <a-tag :color="statusColor(currentTask.promptStatus)">{{ getStatusDesc(currentTask.promptStatus) }}</a-tag>
            </a-descriptions-item>
            <a-descriptions-item label="图片状态">
              <a-tag :color="statusColor(currentTask.imageStatus)">{{ getStatusDesc(currentTask.imageStatus) }}</a-tag>
            </a-descriptions-item>
            <a-descriptions-item label="视频状态">
              <a-tag :color="statusColor(currentTask.videoStatus)">{{ getStatusDesc(currentTask.videoStatus) }}</a-tag>
            </a-descriptions-item>
          </a-descriptions>

          <a-alert
            v-if="currentTask.promptErrorMsg || currentTask.imageErrorMsg || currentTask.videoErrorMsg"
            class="smart-margin-top10"
            type="warning"
            show-icon
            :message="currentTask.promptErrorMsg || currentTask.imageErrorMsg || currentTask.videoErrorMsg"
          />

          <a-card class="smart-margin-top10" title="成品提示词" size="small">
            <a-textarea :value="currentTask.generatedPrompt || '暂无内容'" :rows="10" readonly />
          </a-card>

          <a-row class="smart-margin-top10" :gutter="12">
            <a-col :xs="24" :md="12">
              <a-card title="生成图片" size="small">
                <div class="media-card-body">
                  <img v-if="currentTask.imageFileUrl" :src="currentTask.imageFileUrl" class="media-preview" alt="生成图片" />
                  <a-empty v-else description="暂无图片" />
                </div>
                <a-button class="smart-margin-top10" block :disabled="!currentTask.imageFileKey" @click="downloadFile(currentTask.imageFileKey)">
                  下载图片
                </a-button>
              </a-card>
            </a-col>
            <a-col :xs="24" :md="12">
              <a-card title="生成视频" size="small">
                <div class="media-card-body">
                  <video v-if="currentTask.videoFileUrl" :src="currentTask.videoFileUrl" class="media-preview" controls />
                  <a-empty v-else description="暂无视频" />
                </div>
                <a-button class="smart-margin-top10" block :disabled="!currentTask.videoFileKey" @click="downloadFile(currentTask.videoFileKey)">
                  下载视频
                </a-button>
              </a-card>
            </a-col>
          </a-row>
        </div>
        <a-empty v-else description="上传服装图后，先生成提示词，再依次生成图片和视频" />
      </a-col>
    </a-row>
  </a-card>

  <a-card class="smart-margin-top10" size="small" :bordered="false" :hoverable="true" title="历史任务">
    <a-table
      size="small"
      rowKey="taskId"
      bordered
      :loading="tableLoading"
      :pagination="false"
      :columns="columns"
      :dataSource="tableData"
      :scroll="{ x: 1450 }"
    >
      <template #bodyCell="{ text, record, column }">
        <template v-if="column.dataIndex === 'sourceFileUrl'">
          <img v-if="record.sourceFileUrl" :src="record.sourceFileUrl" class="table-thumb" alt="源图" />
          <span v-else>-</span>
        </template>
        <template v-if="column.dataIndex === 'imageFileUrl'">
          <img v-if="record.imageFileUrl" :src="record.imageFileUrl" class="table-thumb" alt="图片" />
          <span v-else>-</span>
        </template>
        <template v-if="column.dataIndex === 'videoFileUrl'">
          <video v-if="record.videoFileUrl" :src="record.videoFileUrl" class="table-video" controls muted />
          <span v-else>-</span>
        </template>
        <template v-if="column.dataIndex === 'promptStatus' || column.dataIndex === 'imageStatus' || column.dataIndex === 'videoStatus'">
          <a-tag :color="statusColor(text)">{{ getStatusDesc(text) }}</a-tag>
        </template>
        <template v-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button type="link" @click="loadTask(record.taskId)">查看</a-button>
            <a-button type="link" v-privilege="'ai:merch:generateImage'" :disabled="!canGenerateImage(record)" @click="generateImage(record.taskId)"
              >生成图片</a-button
            >
            <a-button type="link" v-privilege="'ai:merch:generateVideo'" :disabled="!canGenerateVideo(record)" @click="generateVideo(record.taskId)"
              >生成视频</a-button
            >
            <a-button type="link" :disabled="!record.imageFileKey" @click="downloadFile(record.imageFileKey)">下载图</a-button>
            <a-button type="link" :disabled="!record.videoFileKey" @click="downloadFile(record.videoFileKey)">下载视频</a-button>
          </div>
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
        @change="queryData"
        @showSizeChange="queryData"
        :show-total="(totalCount) => `共${totalCount}条`"
      />
    </div>
  </a-card>
</template>

<script setup>
  import { onBeforeUnmount, onMounted, reactive, ref } from 'vue';
  import { message } from 'ant-design-vue';
  import AiModelSelect from '/@/components/business/ai/ai-model-select/index.vue';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import FileUpload from '/@/components/support/file-upload/index.vue';
  import { AI_MODEL_PROVIDER_ENUM } from '/@/constants/business/ai/model-const';
  import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/support/file-const';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { fileApi } from '/@/api/support/file-api';
  import { aiMerchTaskApi } from '/@/api/business/ai/merch/ai-merch-task-api';
  import { AI_MERCH_DEFAULT_PROMPT_TEMPLATE, AI_MERCH_TASK_STATUS_ENUM } from '/@/constants/business/ai/merch-const';
  import { smartSentry } from '/@/lib/smart-sentry';

  const form = reactive({
    sourceFileKey: undefined,
    promptModelId: undefined,
    imageModelId: undefined,
    videoModelId: undefined,
    promptTemplate: AI_MERCH_DEFAULT_PROMPT_TEMPLATE,
  });

  const sourceFileList = ref([]);
  const currentTask = ref();
  const currentTaskId = ref();
  const tableLoading = ref(false);
  const tableData = ref([]);
  const total = ref(0);
  const queryForm = reactive({
    pageNum: 1,
    pageSize: 10,
  });

  const columns = ref([
    { title: '任务ID', dataIndex: 'taskId', width: 120 },
    { title: '源图', dataIndex: 'sourceFileUrl', width: 110 },
    { title: '提示词状态', dataIndex: 'promptStatus', width: 120 },
    { title: '图片状态', dataIndex: 'imageStatus', width: 120 },
    { title: '视频状态', dataIndex: 'videoStatus', width: 120 },
    { title: '图片结果', dataIndex: 'imageFileUrl', width: 110 },
    { title: '视频结果', dataIndex: 'videoFileUrl', width: 160 },
    { title: '创建时间', dataIndex: 'createTime', width: 180 },
    { title: '操作', dataIndex: 'action', width: 360, fixed: 'right' },
  ]);

  let pollTimer = null;

  function handleSourceChange(fileList) {
    sourceFileList.value = fileList || [];
    form.sourceFileKey = sourceFileList.value.length ? sourceFileList.value[0].fileKey : undefined;
  }

  function getStatusDesc(status) {
    const enumItem = Object.values(AI_MERCH_TASK_STATUS_ENUM).find((item) => item.value === status);
    return enumItem ? enumItem.desc : status || '-';
  }

  function statusColor(status) {
    if (status === AI_MERCH_TASK_STATUS_ENUM.SUCCESS.value) {
      return 'success';
    }
    if (status === AI_MERCH_TASK_STATUS_ENUM.FAILED.value) {
      return 'error';
    }
    if (status === AI_MERCH_TASK_STATUS_ENUM.PROCESSING.value) {
      return 'processing';
    }
    return 'default';
  }

  function canGenerateImage(task) {
    return !!task && task.promptStatus === 'SUCCESS' && !['PROCESSING', 'SUCCESS'].includes(task.imageStatus);
  }

  function canGenerateVideo(task) {
    return !!task && task.imageStatus === 'SUCCESS' && !['PROCESSING', 'SUCCESS'].includes(task.videoStatus);
  }

  function downloadFile(fileKey) {
    if (!fileKey) {
      return;
    }
    fileApi.downLoadFile(fileKey);
  }

  function stopPolling() {
    if (pollTimer) {
      clearInterval(pollTimer);
      pollTimer = null;
    }
  }

  function startPolling() {
    stopPolling();
    if (!currentTask.value) {
      return;
    }
    const needPolling = currentTask.value.imageStatus === 'PROCESSING' || currentTask.value.videoStatus === 'PROCESSING';
    if (!needPolling) {
      return;
    }
    pollTimer = setInterval(() => {
      if (currentTaskId.value) {
        loadTask(currentTaskId.value, false);
      }
    }, 5000);
  }

  function syncCurrentTask(task) {
    currentTask.value = task;
    currentTaskId.value = task?.taskId;
    if (task) {
      form.promptTemplate = task.promptTemplate || AI_MERCH_DEFAULT_PROMPT_TEMPLATE;
      form.sourceFileKey = task.sourceFileKey;
      form.promptModelId = task.promptModelId;
      form.imageModelId = task.imageModelId;
      form.videoModelId = task.videoModelId;
      sourceFileList.value = task.sourceFileKey
        ? [
            {
              fileId: task.sourceFileId,
              fileKey: task.sourceFileKey,
              fileName: task.sourceFileName,
              fileType: task.sourceFileType,
              fileUrl: task.sourceFileUrl,
            },
          ]
        : [];
    }
    startPolling();
  }

  async function queryData() {
    tableLoading.value = true;
    try {
      const result = await aiMerchTaskApi.queryPage(queryForm);
      tableData.value = result.data.list;
      total.value = result.data.total;
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      tableLoading.value = false;
    }
  }

  async function loadTask(taskId, showLoading = true) {
    if (!taskId) {
      return;
    }
    if (showLoading) {
      SmartLoading.show();
    }
    try {
      const result = await aiMerchTaskApi.detail(taskId);
      syncCurrentTask(result.data);
      if (!showLoading) {
        queryData();
      }
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      if (showLoading) {
        SmartLoading.hide();
      }
    }
  }

  async function createTask() {
    if (!form.sourceFileKey) {
      message.warning('请先上传服装图片');
      return;
    }
    if (!form.promptTemplate || !form.promptTemplate.trim()) {
      message.warning('请输入提示词1');
      return;
    }
    if (!form.promptModelId) {
      message.warning('请选择提示词模型');
      return;
    }
    if (!form.imageModelId) {
      message.warning('请选择图片模型');
      return;
    }
    if (!form.videoModelId) {
      message.warning('请选择视频模型');
      return;
    }

    SmartLoading.show();
    try {
      const result = await aiMerchTaskApi.create({
        sourceFileKey: form.sourceFileKey,
        promptTemplate: form.promptTemplate.trim(),
        promptModelId: form.promptModelId,
        imageModelId: form.imageModelId,
        videoModelId: form.videoModelId,
      });
      syncCurrentTask(result.data);
      queryData();
      if (result.data.promptStatus === 'SUCCESS') {
        message.success('提示词生成完成');
      } else {
        message.warning(result.data.promptErrorMsg || '提示词生成失败');
      }
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      SmartLoading.hide();
    }
  }

  async function generateImage(taskId) {
    if (!taskId) {
      return;
    }
    SmartLoading.show();
    try {
      const result = await aiMerchTaskApi.generateImage(taskId);
      syncCurrentTask(result.data);
      queryData();
      if (result.data.imageStatus === 'SUCCESS') {
        message.success('图片生成完成');
      } else if (result.data.imageStatus === 'PROCESSING') {
        message.success('图片任务已提交，请稍后查看');
      } else {
        message.warning(result.data.imageErrorMsg || '图片生成失败');
      }
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      SmartLoading.hide();
    }
  }

  async function generateVideo(taskId) {
    if (!taskId) {
      return;
    }
    SmartLoading.show();
    try {
      const result = await aiMerchTaskApi.generateVideo(taskId);
      syncCurrentTask(result.data);
      queryData();
      if (result.data.videoStatus === 'SUCCESS') {
        message.success('视频生成完成');
      } else if (result.data.videoStatus === 'PROCESSING') {
        message.success('视频任务已提交，请稍后查看');
      } else {
        message.warning(result.data.videoErrorMsg || '视频生成失败');
      }
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      SmartLoading.hide();
    }
  }

  onMounted(queryData);
  onBeforeUnmount(stopPolling);
</script>

<style lang="less" scoped>
  .result-wrapper {
    min-height: 100%;
  }

  .media-card-body {
    min-height: 280px;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .media-preview {
    width: 100%;
    max-height: 300px;
    object-fit: contain;
    border-radius: 8px;
    background: #f7f8fa;
  }

  .table-thumb {
    width: 72px;
    height: 96px;
    object-fit: cover;
    border-radius: 6px;
    background: #f5f5f5;
  }

  .table-video {
    width: 120px;
    height: 90px;
    border-radius: 6px;
    background: #000;
  }
</style>
