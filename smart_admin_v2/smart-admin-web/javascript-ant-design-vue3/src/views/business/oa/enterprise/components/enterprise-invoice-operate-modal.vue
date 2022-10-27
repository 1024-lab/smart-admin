<!--
  * 企业 发票 表单
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-08-15 20:15:49
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
-->
<template>
  <a-modal :visible="visible" :title="form.invoiceId ? '编辑' : '添加'" ok-text="确认" cancel-text="取消" @ok="onSubmit" @cancel="onClose">
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }">
      <a-form-item label="开票抬头" name="invoiceHeads">
        <a-input v-model:value="form.invoiceHeads" placeholder="请输入开票抬头" />
      </a-form-item>
      <a-form-item label="纳税人识别号" name="taxpayerIdentificationNumber">
        <a-input v-model:value="form.taxpayerIdentificationNumber" placeholder="请输入纳税人识别号" />
      </a-form-item>
      <a-form-item label="银行账号" name="accountNumber">
        <a-input v-model:value="form.accountNumber" placeholder="请输入银行账号" />
      </a-form-item>
      <a-form-item label="开户行" name="bankName">
        <a-input v-model:value="form.bankName" placeholder="请输入开户行" />
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
  import { invoiceApi } from '/@/api/business/oa/invoice-api';
import { smartSentry } from '/@/lib/smart-sentry';

  const props = defineProps({
    enterpriseId: {
      type: Number,
      default: null,
    },
  });
  // emit
  const emit = defineEmits(['reloadList']);

  // --------------------- modal 显示与隐藏 ---------------------
  // 是否展示
  const visible = ref(false);
  const enabledChecked = ref(true);

  function enabledCheckedChange(checked) {
    form.disabledFlag = !checked;
  }

  function showModal(rowData) {
    Object.assign(form, formDefault);
    if (rowData) {
      Object.assign(form, rowData);
      enabledChecked.value = !rowData.disabledFlag;
    }
    form.enterpriseId = props.enterpriseId;
    visible.value = true;
  }

  function onClose() {
    formRef.value.resetFields();
    Object.assign(form, formDefault);
    visible.value = false;
  }

  // --------------------- 表单 ---------------------

  //  组件
  const formRef = ref();

  const formDefault = {
    invoiceId: undefined,
    enterpriseId: undefined,
    bankName: '',
    accountNumber: '',
    invoiceHeads: '',
    taxpayerIdentificationNumber: '',
    disabledFlag: false,
    remark: '',
  };
  let form = reactive({ ...formDefault });
  const rules = {
    invoiceHeads: [{ required: true, message: '请输入开票抬头' }],
    taxpayerIdentificationNumber: [{ required: true, message: '请输入纳税人识别号' }],
    accountNumber: [{ required: true, message: '请输入银行账号' }],
    bankName: [{ required: true, message: '请输入开户行' }],
  };

  function onSubmit() {
    formRef.value
      .validate()
      .then(async () => {
        SmartLoading.show();
        try {
          if (form.invoiceId) {
            await invoiceApi.update(form);
          } else {
            await invoiceApi.create(form);
          }
          message.success(`${form.invoiceId ? '修改' : '添加'}成功`);
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
