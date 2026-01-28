<!--
  * ai应用管理表
  *
  * @Author:    Mission
  * @Date:      2025-12-21 16:00:44
  * @Copyright  Mission
-->
<template>
  <a-drawer
      :title="form.id ? '编辑' : '添加'"
      :width="800"
      :open="visibleFlag"
      @close="onClose"
      :maskClosable="false"
      :destroyOnClose="true"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }" >
<!--        <a-form-item label="id"  name="id">-->
<!--          <a-input style="width: 100%" v-model:value="form.id" placeholder="id" />-->
<!--        </a-form-item>-->
        <a-form-item label="应用名称"  name="name">
          <a-input style="width: 100%" v-model:value="form.name" placeholder="应用名称" />
        </a-form-item>
        <a-form-item label="应用描述"  name="description">
          <a-input style="width: 100%" v-model:value="form.description" placeholder="应用描述" />
        </a-form-item>
        <a-form-item label="智能体平台"  name="appProviderId">
          <AiAppProviderSelect
            v-model:value="form.appProviderId"
            @change="handleProviderChange"
          />
        </a-form-item>

        <!-- Dify 平台显示 API Key 输入 -->
        <a-form-item
          v-if="selectedProviderType === 'dify'"
          label="Dify API Key"
          name="appConfigJson"
          :rules="[{ required: true, message: 'Dify 应用需要配置 API Key' }]"
        >
          <a-input
            v-model:value="difyApiKey"
            placeholder="app-xxx123456"
            @blur="updateAppConfigJson"
          />
          <div style="color: #999; font-size: 12px; margin-top: 4px;">
            在 Dify 平台查看应用的 API Key
          </div>
        </a-form-item>
    </a-form>

    <template #footer>
      <a-space>
        <a-button @click="onClose">取消</a-button>
        <a-button type="primary" @click="onSubmit">保存</a-button>
      </a-space>
    </template>
  </a-drawer>
</template>
<script setup>
  import { reactive, ref, nextTick } from 'vue';
  import _ from 'lodash';
  import { message } from 'ant-design-vue';
  import { SmartLoading } from '/src/components/framework/smart-loading';
  import { aiAppApi } from '/src/api/business/ai/ai-app-api';
  import { smartSentry } from '/src/lib/smart-sentry';
  import AiAppProviderSelect from '/src/components/ai-app-provider-select/index.vue';

  // ------------------------ 事件 ------------------------

  const emits = defineEmits(['reloadList']);

  // ------------------------ 显示与隐藏 ------------------------
  // 是否显示
  const visibleFlag = ref(false);

  // 选中的供应商类型
  const selectedProviderType = ref('');
  const difyApiKey = ref('');

  function show(rowData) {
    Object.assign(form, formDefault);
    selectedProviderType.value = '';
    difyApiKey.value = '';

    if (rowData && !_.isEmpty(rowData)) {
      Object.assign(form, rowData);

      // 如果有应用配置，解析 Dify API Key
      if (rowData.appConfigJson) {
        try {
          const config = JSON.parse(rowData.appConfigJson);
          if (config.apiKey) {
            difyApiKey.value = config.apiKey;
          }
        } catch (e) {
          console.error('解析应用配置失败', e);
        }
      }
    }

    visibleFlag.value = true;
    nextTick(() => {
      formRef.value.clearValidate();
    });
  }

  function onClose() {
    Object.assign(form, formDefault);
    selectedProviderType.value = '';
    difyApiKey.value = '';
    visibleFlag.value = false;
  }

  // 处理供应商变化
  function handleProviderChange(providerId, providerData) {
    if (providerData) {
      selectedProviderType.value = providerData.name;

      // 如果切换到非 Dify 供应商，清空应用配置
      if (providerData.name !== 'dify') {
        form.appConfigJson = null;
        difyApiKey.value = '';
      }
    }
  }

  // 更新应用配置 JSON
  function updateAppConfigJson() {
    if (selectedProviderType.value === 'dify' && difyApiKey.value) {
      form.appConfigJson = JSON.stringify({ apiKey: difyApiKey.value });
    }
  }

  // ------------------------ 表单 ------------------------

  // 组件ref
  const formRef = ref();

  const formDefault = {
      id: undefined, //id
      name: undefined, //应用名称
      description: undefined, //应用描述
      appProviderId: undefined, //应用提供商id
      appConfigJson: undefined, //应用配置
  };

  let form = reactive({ ...formDefault });

  const rules = {
      id: [{ required: true, message: 'id 必填' }],
      name: [{ required: true, message: '应用名称 必填' }],
      description: [{ required: true, message: '应用描述 必填' }],
      appProviderId: [{ required: true, message: '智能体平台 必填' }],
  };

  // 点击确定，验证表单
  async function onSubmit() {
    try {
      // 在提交前确保 appConfigJson 是最新的
      updateAppConfigJson();

      await formRef.value.validateFields();
      save();
    } catch (err) {
      message.error('参数验证错误，请仔细填写表单数据!');
    }
  }

  // 新建、编辑API
  async function save() {
    SmartLoading.show();
    try {
      if (form.id) {
        await aiAppApi.update(form);
      } else {
        await aiAppApi.add(form);
      }
      message.success('操作成功');
      emits('reloadList');
      onClose();
    } catch (err) {
      smartSentry.captureError(err);
    } finally {
      SmartLoading.hide();
    }
  }

  defineExpose({
    show,
  });
</script>
