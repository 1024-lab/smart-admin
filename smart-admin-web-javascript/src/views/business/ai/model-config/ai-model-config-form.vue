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
    :width="500"
    :open="visibleFlag"
    @cancel="onClose"
    :maskClosable="false"
    :destroyOnClose="true"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }" >
      <a-form-item label="厂商标识"  name="provider">
        <DictSelect width="100%" v-model:value="form.provider" :dict-code="DICT_CODE_ENUM.MODEL_PROVIDER || 'MODEL_PROVIDER'" placeholder="厂商标识 (方便前端展示回显，实际调用主要靠 baseUrl)"/>
      </a-form-item>
      <a-form-item label="BaseUrl"  name="baseUrl">
        <a-input style="width: 100%" v-model:value="form.baseUrl" placeholder="BaseUrl" />
      </a-form-item>
      <a-form-item label="API密钥"  name="apiKey">
        <a-input style="width: 100%" v-model:value="form.apiKey" placeholder="API密钥" />
      </a-form-item>
      <a-form-item label="模型名称"  name="modelName">
        <a-input style="width: 100%" v-model:value="form.modelName" placeholder="模型名称" />
      </a-form-item>
      <a-form-item label="温度参数"  name="temperature">
        <a-input-number style="width: 100%" v-model:value="form.temperature" placeholder="温度参数" />
      </a-form-item>
      <a-form-item label="是否激活"  name="isActive">
        <a-input-number style="width: 100%" v-model:value="form.isActive" placeholder="是否激活" />
      </a-form-item>
      <a-form-item label="输出响应最大令牌数"  name="maxTokens">
        <a-input-number style="width: 100%" v-model:value="form.maxTokens" placeholder="输出响应最大令牌数" />
      </a-form-item>
      <a-form-item label="模型类型 (CHAT/EMBEDDING)"  name="modelType">
        <DictSelect width="100%" v-model:value="form.modelType" :dict-code="DICT_CODE_ENUM.MODEL_TYPE || 'MODEL_TYPE'" placeholder="模型类型 (CHAT/EMBEDDING)"/>
      </a-form-item>
      <a-form-item label="Chat模型专用。附加到 Base URL 的路径。例如OpenAi的/v1/chat/completions"  name="completionsPath">
        <a-input style="width: 100%" v-model:value="form.completionsPath" placeholder="Chat模型专用。附加到 Base URL 的路径。例如OpenAi的/v1/chat/completions" />
      </a-form-item>
      <a-form-item label="嵌入模型专用。附加到 Base URL 的路径。"  name="embeddingsPath">
        <a-input style="width: 100%" v-model:value="form.embeddingsPath" placeholder="嵌入模型专用。附加到 Base URL 的路径。" />
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
import { aiModelConfigApi } from '/@/api/business/ai/ai-model-config-api';
import { smartSentry } from '/@/lib/smart-sentry';
import DictSelect from '/@/components/support/dict-select/index.vue';
import { DICT_CODE_ENUM } from '/@/constants/support/dict-const.js';

// ------------------------ 事件 ------------------------

const emits = defineEmits(['reloadList']);

// ------------------------ 显示与隐藏 ------------------------
// 是否显示
const visibleFlag = ref(false);

function show(rowData) {
  Object.assign(form, formDefault);
  if (rowData && !_.isEmpty(rowData)) {
    Object.assign(form, rowData);
  }
  // 使用字典时把下面这注释修改成自己的字典字段 有多个字典字段就复制多份同理修改 不然打开表单时不显示字典初始值
  // if (form.status && form.status.length > 0) {
  //   form.status = form.status.map((e) => e.valueCode);
  // }
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

const formDefault = {
  provider: undefined, //厂商标识 (方便前端展示回显，实际调用主要靠 baseUrl)
  baseUrl: undefined, //关键配置
  apiKey: undefined, //API密钥
  modelName: undefined, //模型名称
  temperature: undefined, //温度参数
  isActive: undefined, //是否激活
  maxTokens: undefined, //输出响应最大令牌数
  modelType: undefined, //模型类型 (CHAT/EMBEDDING)
  completionsPath: undefined, //Chat模型专用。附加到 Base URL 的路径。例如OpenAi的/v1/chat/completions
  embeddingsPath: undefined, //嵌入模型专用。附加到 Base URL 的路径。
};

let form = reactive({ ...formDefault });

const rules = {
  provider: [{ required: true, message: '厂商标识 (方便前端展示回显，实际调用主要靠 baseUrl) 必填' }],
  baseUrl: [{ required: true, message: '关键配置 必填' }],
  apiKey: [{ required: true, message: 'API密钥 必填' }],
  modelName: [{ required: true, message: '模型名称 必填' }],
  modelType: [{ required: true, message: '模型类型 (CHAT/EMBEDDING) 必填' }],
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
    if (form.id) {
      await aiModelConfigApi.update(form);
    } else {
      await aiModelConfigApi.add(form);
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
