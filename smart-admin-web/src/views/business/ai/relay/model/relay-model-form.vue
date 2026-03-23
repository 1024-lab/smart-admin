<!--
  * Relay 模型管理 - 新增/编辑表单
  *
  * @Author:    Mission
  * @Date:      2026-03-15
  * @Copyright  Mission
-->
<template>
  <a-modal
      :title="form.modelId ? '编辑模型' : '新增模型'"
      :width="500"
      :open="visibleFlag"
      @cancel="onClose"
      :maskClosable="false"
      :destroyOnClose="true"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 6 }">
        <a-form-item label="模型名称" name="modelName">
          <a-input style="width: 100%" v-model:value="form.modelName" placeholder="请输入模型名称" />
        </a-form-item>
        <a-form-item label="端点类型" name="endpointType">
          <SmartEnumSelect v-model:value="form.endpointType" enumName="ENDPOINT_TYPE_ENUM" placeholder="请选择端点类型" width="100%" />
        </a-form-item>
        <a-form-item label="输入价格" name="inputPricePerK">
          <a-input-number style="width: 100%" v-model:value="form.inputPricePerK" placeholder="每千token价格" :min="0" :precision="6" />
        </a-form-item>
        <a-form-item label="输出价格" name="outputPricePerK">
          <a-input-number style="width: 100%" v-model:value="form.outputPricePerK" placeholder="每千token价格" :min="0" :precision="6" />
        </a-form-item>
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
  import { relayModelApi } from '/@/api/business/ai/relay/relay-model-api';
  import { smartSentry } from '/@/lib/smart-sentry';
  import SmartEnumSelect from '/@/components/framework/smart-enum-select/index.vue';

  // ------------------------ 事件 ------------------------
  const emits = defineEmits(['reloadList']);

  // ------------------------ 显示与隐藏 ------------------------
  const visibleFlag = ref(false);

  function show(rowData) {
    Object.assign(form, formDefault);
    if (rowData && !_.isEmpty(rowData)) {
      Object.assign(form, rowData);
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
  const formRef = ref();

  const formDefault = {
      modelId: undefined,
      modelName: undefined,
      endpointType: undefined,
      inputPricePerK: undefined,
      outputPricePerK: undefined,
      remark: undefined,
  };

  let form = reactive({ ...formDefault });

  const rules = {
      modelName: [{ required: true, message: '模型名称必填' }],
      endpointType: [{ required: true, message: '端点类型必填' }],
  };

  async function onSubmit() {
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
      if (form.modelId) {
        await relayModelApi.update(form);
      } else {
        await relayModelApi.add(form);
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
