<!--
  * Relay 用户账户 - 新增弹窗
  *
  * @Author:    Mission
  * @Date:      2026-03-15
  * @Copyright  Mission
-->
<template>
  <a-modal
      title="新增账户"
      :width="450"
      :open="visibleFlag"
      @cancel="onClose"
      :maskClosable="false"
      :destroyOnClose="true"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 6 }">
        <a-form-item label="员工" name="employeeId">
          <EmployeeSelect v-model:value="form.employeeId" placeholder="请选择员工" width="100%" :disabledFlag="0" />
        </a-form-item>
        <a-form-item label="初始额度" name="quotaRemain">
          <a-input-number style="width: 100%" v-model:value="form.quotaRemain" placeholder="请输入初始额度" :min="0" :precision="2" />
        </a-form-item>
    </a-form>

    <template #footer>
      <a-space>
        <a-button @click="onClose">取消</a-button>
        <a-button type="primary" @click="onSubmit">确认新增</a-button>
      </a-space>
    </template>
  </a-modal>
</template>
<script setup>
  import { reactive, ref, nextTick } from 'vue';
  import { message } from 'ant-design-vue';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import EmployeeSelect from '/@/components/system/employee-select/index.vue';
  import { relayAccountApi } from '/@/api/business/ai/relay/relay-account-api';
  import { smartSentry } from '/@/lib/smart-sentry';

  const emits = defineEmits(['reloadList']);
  const visibleFlag = ref(false);

  function show() {
    form.employeeId = undefined;
    form.quotaRemain = undefined;
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
  });

  const rules = {
    employeeId: [{ required: true, message: '请选择员工' }],
    quotaRemain: [{ required: true, message: '初始额度必填' }],
  };

  async function onSubmit() {
    try {
      await formRef.value.validateFields();
      doAdd();
    } catch (err) {
      message.error('参数验证错误，请仔细填写表单数据!');
    }
  }

  async function doAdd() {
    SmartLoading.show();
    try {
      await relayAccountApi.add(form);
      message.success('新增成功');
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
