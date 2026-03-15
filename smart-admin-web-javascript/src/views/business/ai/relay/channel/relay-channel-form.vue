<!--
  * Relay 渠道管理 - 新增/编辑表单
  *
  * @Author:    Mission
  * @Date:      2026-03-15
  * @Copyright  Mission
-->
<template>
  <a-modal
      :title="form.channelId ? '编辑渠道' : '新增渠道'"
      :width="600"
      :open="visibleFlag"
      @cancel="onClose"
      :maskClosable="false"
      :destroyOnClose="true"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }">
        <a-form-item label="渠道名称" name="channelName">
          <a-input style="width: 100%" v-model:value="form.channelName" placeholder="请输入渠道名称" />
        </a-form-item>
        <a-form-item label="供应商" name="providerType">
          <SmartEnumSelect v-model:value="form.providerType" enumName="PROVIDER_TYPE_ENUM" placeholder="请选择供应商" width="100%" />
        </a-form-item>
        <a-form-item label="基础URL" name="baseUrl">
          <a-input style="width: 100%" v-model:value="form.baseUrl" placeholder="例如: https://api.openai.com" />
        </a-form-item>
        <a-form-item label="API Key" name="apiKey">
          <a-input-password style="width: 100%" v-model:value="form.apiKey" :placeholder="form.channelId ? '留空则不更新' : '请输入API Key'" />
        </a-form-item>
        <a-form-item label="Organization" name="organization">
          <a-input style="width: 100%" v-model:value="form.organization" placeholder="可选，组织ID" />
        </a-form-item>
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="优先级" name="priority" :label-col="{ span: 10 }">
              <a-input-number style="width: 100%" v-model:value="form.priority" placeholder="数值越大优先级越高" :min="0" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="权重" name="weight" :label-col="{ span: 10 }">
              <a-input-number style="width: 100%" v-model:value="form.weight" placeholder="同优先级下的权重" :min="1" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="超时(ms)" name="timeoutMs" :label-col="{ span: 10 }">
              <a-input-number style="width: 100%" v-model:value="form.timeoutMs" placeholder="请求超时毫秒" :min="1000" :step="1000" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="最大重试" name="maxRetries" :label-col="{ span: 10 }">
              <a-input-number style="width: 100%" v-model:value="form.maxRetries" placeholder="最大重试次数" :min="0" :max="5" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-form-item label="备注" name="remark">
          <a-textarea v-model:value="form.remark" placeholder="请输入备注" :rows="3" />
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
  import { relayChannelApi } from '/@/api/business/ai/relay/relay-channel-api';
  import { smartSentry } from '/@/lib/smart-sentry';
  import SmartEnumSelect from '/@/components/framework/smart-enum-select/index.vue';

  const emits = defineEmits(['reloadList']);
  const visibleFlag = ref(false);

  function show(rowData) {
    Object.assign(form, formDefault);
    if (rowData && !_.isEmpty(rowData)) {
      Object.assign(form, rowData);
      form.apiKey = undefined; // 编辑时不回显apiKey
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

  const formRef = ref();

  const formDefault = {
      channelId: undefined,
      channelName: undefined,
      providerType: undefined,
      baseUrl: undefined,
      apiKey: undefined,
      organization: undefined,
      priority: 0,
      weight: 1,
      timeoutMs: 30000,
      maxRetries: 1,
      remark: undefined,
  };

  let form = reactive({ ...formDefault });

  const rules = {
      channelName: [{ required: true, message: '渠道名称必填' }],
      providerType: [{ required: true, message: '供应商必填' }],
      baseUrl: [{ required: true, message: '基础URL必填' }],
      apiKey: [{ required: true, message: 'API Key必填' }],
      priority: [{ required: true, message: '优先级必填' }],
  };

  async function onSubmit() {
    // 编辑时apiKey非必填
    if (form.channelId) {
      rules.apiKey = [];
    } else {
      rules.apiKey = [{ required: true, message: 'API Key必填' }];
    }
    try {
      await formRef.value.validateFields();
      save();
    } catch (err) {
      message.error('参数验证错误，请仔细填写表单数据!');
    }
  }

  async function save() {
    SmartLoading.show();
    try {
      if (form.channelId) {
        await relayChannelApi.update(form);
      } else {
        await relayChannelApi.add(form);
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

  defineExpose({ show });
</script>
