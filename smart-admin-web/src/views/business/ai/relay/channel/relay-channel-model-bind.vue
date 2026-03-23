<!--
  * Relay 渠道管理 - 绑定模型弹窗（左右布局 + 分页）
  *
  * @Author:    Mission
  * @Date:      2026-03-15
  * @Copyright  Mission
-->
<template>
  <a-modal
      title="绑定模型"
      :width="900"
      :open="visibleFlag"
      @cancel="onClose"
      :maskClosable="false"
      :destroyOnClose="true"
  >
    <div style="margin-bottom: 12px">
      <span>渠道：</span>
      <a-tag color="blue">{{ channelInfo.channelName }}</a-tag>
      <span style="margin-left: 12px">供应商：</span>
      {{ $smartEnumPlugin.getDescByValue('PROVIDER_TYPE_ENUM', channelInfo.providerType) }}
    </div>

    <div style="display: flex; gap: 16px;">
      <!-- 左侧：可选模型（分页） -->
      <div style="flex: 1; border: 1px solid #d9d9d9; border-radius: 4px; padding: 12px;">
        <div style="font-weight: 500; margin-bottom: 8px;">可选模型</div>
        <a-input-search
            v-model:value="queryForm.modelName"
            placeholder="搜索模型名称"
            style="margin-bottom: 8px;"
            @search="onSearch"
            allow-clear
            size="small"
        />
        <a-table
            size="small"
            :dataSource="tableData"
            :columns="leftColumns"
            rowKey="modelId"
            bordered
            :loading="tableLoading"
            :pagination="false"
        >
          <template #bodyCell="{ record, column }">
            <template v-if="column.dataIndex === 'action'">
              <a-button
                  v-if="!boundMap.has(record.modelId)"
                  type="link"
                  size="small"
                  @click="addModel(record)"
              >添加</a-button>
              <span v-else style="color: #999;">已添加</span>
            </template>
          </template>
        </a-table>
        <a-pagination
            style="margin-top: 8px; text-align: right;"
            size="small"
            showSizeChanger
            show-less-items
            :defaultPageSize="queryForm.pageSize"
            v-model:current="queryForm.pageNum"
            v-model:pageSize="queryForm.pageSize"
            :total="total"
            @change="queryData"
            @showSizeChange="queryData"
            :show-total="(total) => `共${total}条`"
        />
      </div>

      <!-- 右侧：已绑定模型 -->
      <div style="flex: 1; border: 1px solid #d9d9d9; border-radius: 4px; padding: 12px;">
        <div style="font-weight: 500; margin-bottom: 8px;">已绑定模型 ({{ boundList.length }})</div>
        <a-table
            size="small"
            :dataSource="boundList"
            :columns="rightColumns"
            rowKey="modelId"
            bordered
            :pagination="false"
            :scroll="{ y: 350 }"
        >
          <template #bodyCell="{ record, column }">
            <template v-if="column.dataIndex === 'action'">
              <a-button type="link" danger size="small" @click="removeModel(record)">移除</a-button>
            </template>
          </template>
        </a-table>
      </div>
    </div>

    <template #footer>
      <a-space>
        <a-button @click="onClose">取消</a-button>
        <a-button type="primary" @click="onSubmit" :loading="submitLoading">保存</a-button>
      </a-space>
    </template>
  </a-modal>
</template>
<script setup>
  import { ref, reactive, computed } from 'vue';
  import { message } from 'ant-design-vue';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { relayChannelApi } from '/@/api/business/ai/relay/relay-channel-api';
  import { relayModelApi } from '/@/api/business/ai/relay/relay-model-api';
  import { smartSentry } from '/@/lib/smart-sentry';

  const emits = defineEmits(['reloadList']);
  const visibleFlag = ref(false);
  const submitLoading = ref(false);
  const channelInfo = ref({});
  const tableData = ref([]);
  const tableLoading = ref(false);
  const total = ref(0);

  // 已绑定模型：用 Map 存储 modelId -> { modelId, modelName }
  const boundMap = ref(new Map());
  const boundList = computed(() => [...boundMap.value.values()]);

  const leftColumns = [
    { title: 'ID', dataIndex: 'modelId', width: 60 },
    { title: '模型名称', dataIndex: 'modelName', ellipsis: true },
    { title: '操作', dataIndex: 'action', width: 70 },
  ];

  const rightColumns = [
    { title: 'ID', dataIndex: 'modelId', width: 60 },
    { title: '模型名称', dataIndex: 'modelName', ellipsis: true },
    { title: '操作', dataIndex: 'action', width: 70 },
  ];

  const queryForm = reactive({
    modelName: undefined,
    disabledFlag: 0,
    pageNum: 1,
    pageSize: 10,
  });

  async function show(record) {
    channelInfo.value = record;
    boundMap.value = new Map();
    queryForm.modelName = undefined;
    queryForm.pageNum = 1;
    visibleFlag.value = true;
    await Promise.all([loadBoundModels(), queryData()]);
  }

  function onClose() {
    visibleFlag.value = false;
    channelInfo.value = {};
    tableData.value = [];
    boundMap.value = new Map();
  }

  async function loadBoundModels() {
    try {
      let result = await relayChannelApi.getBoundModelIds(channelInfo.value.channelId);
      let ids = result.data || [];
      if (ids.length === 0) return;
      // 查询这些模型的详细信息用于右侧展示
      let modelResult = await relayModelApi.queryPage({ pageNum: 1, pageSize: 500, disabledFlag: 0 });
      let allModels = modelResult.data.list || [];
      let idSet = new Set(ids);
      let map = new Map();
      allModels.forEach((m) => {
        if (idSet.has(m.modelId)) {
          map.set(m.modelId, { modelId: m.modelId, modelName: m.modelName });
        }
      });
      boundMap.value = map;
    } catch (e) {
      smartSentry.captureError(e);
    }
  }

  function onSearch() {
    queryForm.pageNum = 1;
    queryData();
  }

  async function queryData() {
    tableLoading.value = true;
    try {
      let result = await relayModelApi.queryPage(queryForm);
      tableData.value = result.data.list || [];
      total.value = result.data.total || 0;
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      tableLoading.value = false;
    }
  }

  function addModel(record) {
    boundMap.value.set(record.modelId, { modelId: record.modelId, modelName: record.modelName });
    // 触发响应式
    boundMap.value = new Map(boundMap.value);
  }

  function removeModel(record) {
    boundMap.value.delete(record.modelId);
    boundMap.value = new Map(boundMap.value);
  }

  async function onSubmit() {
    submitLoading.value = true;
    try {
      await relayChannelApi.bindModels({
        channelId: channelInfo.value.channelId,
        modelIds: [...boundMap.value.keys()],
      });
      message.success('绑定成功');
      emits('reloadList');
      onClose();
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      submitLoading.value = false;
    }
  }

  defineExpose({ show });
</script>
