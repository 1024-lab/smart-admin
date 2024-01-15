<!--
  * 企业 银行 表单
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-08-15 20:15:49
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
-->
<template>
  <a-modal :open="visible" :title="form.bankId ? '编辑' : '添加'" ok-text="确认" cancel-text="取消" @ok="onSubmit" @cancel="onClose">
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="开户银行" name="bankName">
        <a-input v-model:value="form.bankName" placeholder="请输入开户银行" />
      </a-form-item>
      <a-form-item label="账户名称" name="accountName">
        <a-input v-model:value="form.accountName" placeholder="请输入账户名称" />
      </a-form-item>
      <a-form-item label="账号" name="accountNumber">
        <a-input v-model:value="form.accountNumber" placeholder="请输入账号" />
      </a-form-item>
      <a-form-item label="是否对公" name="businessFlag">
        <a-switch v-model:checked="businessFlagChecked" @change="businessFlagCheckedChange" />
      </a-form-item>
      <a-form-item label="启用状态" name="disabledFlag">
        <a-switch v-model:checked="enabledChecked" @change="enabledCheckedChange" />
      </a-form-item>
      <a-form-item label="备注" name="remark">
        <a-textarea v-model:value="form.remark" :rows="2" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script setup>
  import { ref, reactive } from 'vue';
  import { message } from 'ant-design-vue';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { bankApi } from '/@/api/business/oa/bank-api';
  import { smartSentry } from '/@/lib/smart-sentry';

  const props = defineProps({
    enterpriseId: {
      type: Number,
      default: null,
    },
  });
  // emit
  const emit = defineEmits(['reloadList']);

  // ---------------------- 显示、隐藏 ----------------------
  // 是否展示
  const visible = ref(false);
  function showModal(rowData) {
    Object.assign(form, formDefault);
    if (rowData) {
      Object.assign(form, rowData);
      businessFlagChecked.value = rowData.businessFlag;
      enabledChecked.value = !rowData.disabledFlag;
    }
    form.enterpriseId = props.enterpriseId;
    visible.value = true;
  }

  function onClose() {
    Object.assign(form, formDefault);
    formRef.value.resetFields();
    visible.value = false;
  }

  // ---------------------- 表单 ----------------------

  //  组件
  const formRef = ref();

  const formDefault = {
    bankId: undefined,
    enterpriseId: undefined,
    bankName: '',
    accountName: '',
    accountNumber: '',
    businessFlag: false,
    disabledFlag: false,
    remark: '',
  };
  let form = reactive({ ...formDefault });
  const rules = {
    bankName: [{ required: true, message: '请输入开户银行' }],
    accountName: [{ required: true, message: '请输入账户名称' }],
    accountNumber: [{ required: true, message: '请输入账号' }],
  };

  const businessFlagChecked = ref(false);
  const enabledChecked = ref(true);

  function businessFlagCheckedChange(checked) {
    form.businessFlag = checked;
  }
  function enabledCheckedChange(checked) {
    form.disabledFlag = !checked;
  }

  function onSubmit() {
    formRef.value
      .validate()
      .then(async () => {
        SmartLoading.show();
        try {
          if (form.bankId) {
            await bankApi.update(form);
          } else {
            await bankApi.create(form);
          }
          message.success(`${form.bankId ? '修改' : '添加'}成功`);
          emit('reloadList');
          onClose();
        } catch (error) {
          smartSentry.captureError(error);
        } finally {
          SmartLoading.hide();
        }
      })
      .catch((error) => {
        console.log('error', error);
        message.error('参数验证错误，请仔细填写表单数据!');
      });
  }

  defineExpose({
    showModal,
  });
</script>
