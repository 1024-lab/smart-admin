<!--
  * Relay 用户账户 - 充值弹窗
  *
  * @Author:    Mission
  * @Date:      2026-03-15
  * @Copyright  Mission
-->
<template>
  <a-modal
      title="账户充值"
      :width="450"
      :open="visibleFlag"
      @cancel="onClose"
      :maskClosable="false"
      :destroyOnClose="true"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 6 }">
        <a-form-item label="员工ID">
          <a-input :value="accountInfo.employeeId" disabled />
        </a-form-item>
        <a-form-item label="员工姓名">
          <a-input :value="accountInfo.employeeName" disabled />
        </a-form-item>
        <a-form-item label="当前余额">
          <a-input :value="accountInfo.quotaRemain" disabled />
        </a-form-item>
        <a-form-item label="充值金额" name="amount">
          <a-input-number style="width: 100%" v-model:value="form.amount" placeholder="请输入充值金额" :min="1" :precision="2" />
        </a-form-item>
    </a-form>

    <template #footer>
      <a-space>
        <a-button @click="onClose">取消</a-button>
        <a-button type="primary" @click="onSubmit">确认充值</a-button>
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
  const accountInfo = ref({});

  function show(record) {
    accountInfo.value = record;
    form.employeeId = record.employeeId;
    form.amount = undefined;
    visibleFlag.value = true;
    nextTick(() => {
      formRef.value.clearValidate();
    });
  }

  function onClose() {
    visibleFlag.value = false;
    form.amount = undefined;
  }

  const formRef = ref();
  let form = reactive({
    employeeId: undefined,
    amount: undefined,
  });

  const rules = {
    amount: [{ required: true, message: '充值金额必填' }],
  };

  async function onSubmit() {
    try {
      await formRef.value.validateFields();
      doRecharge();
    } catch (err) {
      message.error('参数验证错误，请仔细填写表单数据!');
    }
  }

  async function doRecharge() {
    SmartLoading.show();
    try {
      await relayAccountApi.recharge(form);
      message.success('充值成功');
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
