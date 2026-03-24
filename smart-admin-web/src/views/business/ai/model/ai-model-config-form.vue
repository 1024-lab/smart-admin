<!--
  * 模型配置表
  *
  * @Author:    Mission
  * @Date:      2026-01-28 09:59:38
  * @Copyright  Mission
-->
<template>
  <a-modal
      :title="form.id ? '编辑' : '添加'"
      :width="640"
      :open="visibleFlag"
      @cancel="onClose"
      :maskClosable="false"
      :destroyOnClose="true"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }">
        <a-form-item label="供应商" name="provider">
          <a-select v-model:value="form.provider" placeholder="请选择供应商">
            <a-select-option v-for="item in providerList" :key="item.value" :value="item.value">
              {{ item.desc }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="Base URL" name="baseUrl">
          <a-input style="width: 100%" v-model:value="form.baseUrl" placeholder="例如 https://api.openai.com" />
        </a-form-item>
        <a-form-item label="API密钥" name="apiKey">
          <a-input-password style="width: 100%" v-model:value="form.apiKey" placeholder="请输入 API 密钥" />
        </a-form-item>
        <a-form-item label="配置名称" name="modelName">
          <a-input style="width: 100%" v-model:value="form.modelName" placeholder="用于页面识别" />
        </a-form-item>
        <a-form-item label="是否启用" name="isActive">
          <DictSelect width="100%" v-model:value="form.isActive" :dict-code="DICT_CODE_ENUM.YES_NO || 'YES_NO'" placeholder="是否激活"/>
        </a-form-item>
        <a-form-item label="模型编码" name="endPointModeName">
          <a-input style="width: 100%" v-model:value="form.endPointModeName" placeholder="上游接口使用的模型编码" />
        </a-form-item>
        <a-form-item label="提交路径" name="path">
          <a-input style="width: 100%" v-model:value="form.path" placeholder="可选，OpenAI 例如 /v1/chat/completions" />
        </a-form-item>
        <a-form-item label="模型类型" name="modelType">
          <a-select style="width: 100%" v-model:value="form.modelType" placeholder="请选择模型类型">
            <a-select-option v-for="item in MODEL_TYPE_LIST" :key="item.value" :value="item.value">
              {{ item.label }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item v-if="form.modelType === 'chat'" label="能力标签" name="modelFeatureList">
          <a-checkbox-group v-model:value="form.modelFeatureList" :options="MODEL_FEATURE_LIST" />
        </a-form-item>
        <a-form-item label="最大令牌" name="maxTokens">
          <a-input-number style="width: 100%" v-model:value="form.maxTokens" placeholder="可选" />
        </a-form-item>
        <a-form-item label="扩展JSON" name="extraConfigJson">
          <a-textarea v-model:value="form.extraConfigJson" :rows="5" placeholder='可选，例如 {"headers":{"X-Test":"1"}}' />
        </a-form-item>
    </a-form>

    <template #footer>
      <a-space>
        <a-button @click="onClose">取消</a-button>
        <a-button type="primary" @click="onSubmit">保存</a-button>
      </a-space>
    </template>
  </a-modal>
</template>
<script setup>
  import { reactive, ref, nextTick } from 'vue';
  import _ from 'lodash';
  import { message } from 'ant-design-vue';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { aiModelConfigApi } from '/@/api/business/ai/model/ai-model-config-api';
  import { smartSentry } from '/@/lib/smart-sentry';
  import DictSelect from '/@/components/support/dict-select/index.vue';
  import { AI_MODEL_PROVIDER_ENUM } from '/@/constants/business/ai/model-const';
  import { DICT_CODE_ENUM } from '/@/constants/support/dict-const.js';
  import { MODEL_TYPE_LIST, MODEL_FEATURE_LIST } from '/@/constants/business/ai/model/ai-model-config-const.js';

  // ------------------------ 事件 ------------------------

  const emits = defineEmits(['reloadList']);

  // ------------------------ 显示与隐藏 ------------------------
  // 是否显示
  const visibleFlag = ref(false);

  function show(rowData) {
    Object.assign(form, formDefault);
    if (rowData && !_.isEmpty(rowData)) {
      Object.assign(form, rowData);
      // 将逗号分隔的 modelFeatures 字符串还原为数组
      if (form.modelFeatures && form.modelFeatures.length > 0) {
        form.modelFeatureList = form.modelFeatures.split(',').filter(Boolean);
      }
    }
    visibleFlag.value = true;
    nextTick(() => {
      formRef.value.clearValidate();
    });
  }

  function onClose() {
    Object.assign(form, formDefault);
    visibleFlag.value = false;
  }

  // ------------------------ 表单 ------------------------

  // 组件ref
  const formRef = ref();

  const providerList = Object.values(AI_MODEL_PROVIDER_ENUM);

  const formDefault = {
      provider: undefined,
      baseUrl: undefined,
      apiKey: undefined,
      modelName: undefined,
      isActive: undefined,
      endPointModeName: undefined,
      path: undefined,
      modelType: undefined,
      modelFeatures: undefined,
      modelFeatureList: [],
      maxTokens: undefined,
      extraConfigJson: undefined,
  };

  let form = reactive({ ...formDefault });

  const rules = {
      provider: [{ required: true, message: '请选择供应商' }],
      baseUrl: [{ required: true, message: '请输入 Base URL' }],
      apiKey: [{ required: true, message: 'API密钥 必填' }],
      modelName: [{ required: true, message: '请输入配置名称' }],
      isActive: [{ required: true, message: '请选择是否启用' }],
      endPointModeName: [{ required: true, message: '请输入模型编码' }],
  };

  // 点击确定，验证表单
  async function onSubmit() {
    try {
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
      // 将能力标签数组序列化为逗号分隔字符串
      const payload = { ...form };
      payload.modelFeatures = form.modelFeatureList && form.modelFeatureList.length > 0
        ? form.modelFeatureList.join(',')
        : null;
      delete payload.modelFeatureList;

      if (form.id) {
        await aiModelConfigApi.update(payload);
      } else {
        await aiModelConfigApi.add(payload);
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
