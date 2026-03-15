<!--
  * Relay 用户账户 - 编辑弹窗
  *
  * @Author:    Mission
  * @Date:      2026-03-15
  * @Copyright  Mission
-->
<template>
  <a-modal
      title="编辑账户"
      :width="450"
      :open="visibleFlag"
      @cancel="onClose"
      :maskClosable="false"
      :destroyOnClose="true"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 6 }">
        <a-form-item label="员工ID">
          <a-input :value="form.employeeId" disabled />
        </a-form-item>
        <a-form-item label="员工姓名">
          <a-input :value="employeeName" disabled />
        </a-form-item>
        <a-form-item label="剩余额度" name="quotaRemain">
          <a-input-number style="width: 100%" v-model:value="form.quotaRemain" placeholder="请输入剩余额度" :min="0" />
        </a-form-item>
        <a-form-item label="状态" name="disabledFlag">
          <a-radio-group v-model:value="form.disabledFlag">
            <a-radio :value="0">启用</a-radio>
            <a-radio :value="1">禁用</a-radio>
          </a-radio-group>
        </a-form-item>
    </a-form>

    <template #footer>
      <a-space>
        <a-button @click="onClose">取消</a-button>
        <a-button type="primary" @click="onSubmit">确认</a-button>
      </a-space>
    </template>
  </a-modal>
</template>
<script setup>
  import { reactive, ref, nextTick } from 'vue';
  import { message } from 'ant-design-vue';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { relayAccountApi } from '/@/api/business/ai/relay/relay-account-api';
  import { smartSentry } from '/@/lib/smart-sentry';

  const emits = defineEmits(['reloadList']);
  const visibleFlag = ref(false);
  const employeeName = ref('');

  function show(record) {
    employeeName.value = record.employeeName;
    form.employeeId = record.employeeId;
    form.quotaRemain = record.quotaRemain;
    form.disabledFlag = record.disabledFlag;
    visibleFlag.value = true;
    nextTick(() => {
      formRef.value.clearValidate();
    });
  }

  function onClose() {
    visibleFlag.value = false;
  }

  const formRef = ref();
  let form = reactive({
    employeeId: undefined,
    quotaRemain: undefined,
    disabledFlag: undefined,
  });

  const rules = {
    quotaRemain: [{ required: true, message: '剩余额度必填' }],
    disabledFlag: [{ required: true, message: '状态必填' }],
  };

  async function onSubmit() {
    try {
      await formRef.value.validateFields();
      doUpdate();
    } catch (err) {
      message.error('参数验证错误，请仔细填写表单数据!');
    }
  }

  async function doUpdate() {
    SmartLoading.show();
    try {
      await relayAccountApi.update(form);
      message.success('修改成功');
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
