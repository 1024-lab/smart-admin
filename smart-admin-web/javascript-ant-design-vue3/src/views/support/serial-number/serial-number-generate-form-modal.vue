<!--
  * 生成
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-07-21 21:55:12
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
-->
<template>
  <a-modal :visible="visible" title="生成单号" ok-text="生成" cancel-text="关闭" @ok="onSubmit" @cancel="onClose">
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }">
      <a-form-item label="业务">
        <a-input v-model:value="form.businessName" :disabled="true" />
      </a-form-item>
      <a-form-item label="格式">
        <a-input v-model:value="form.format" :disabled="true" />
      </a-form-item>
      <a-form-item label="循环周期">
        <a-input v-model:value="form.ruleType" :disabled="true" />
      </a-form-item>
      <a-form-item label="上次产生单号">
        <a-input v-model:value="form.lastNumber" :disabled="true" />
      </a-form-item>
      <a-form-item label="生成数量" name="count">
        <a-input-number v-model:value="form.count" />
      </a-form-item>
      <a-form-item label="生成结果">
        <a-textarea v-model:value="generateResult" :rows="2" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script setup>
  import { message } from 'ant-design-vue';
  import { reactive, ref } from 'vue';
  import { serialNumberApi } from '/@/api/support/serial-number/serial-number-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import _ from 'lodash';
  import { smartSentry } from '/@/lib/smart-sentry';

  // emit
  const emit = defineEmits(['refresh']);
  defineExpose({
    showModal,
  });

  // ----------------------- 表单 隐藏 与 显示 ------------------------
  // 是否展示
  const visible = ref(false);
  function showModal(data) {
    form.serialNumberId = data.serialNumberId;
    form.businessName = data.businessName;
    form.format = data.format;
    form.ruleType = data.ruleType;
    form.lastNumber = data.lastNumber;
    form.count = 1;
    generateResult.value = '';
    visible.value = true;
  }

  function onClose() {
    visible.value = false;
    emit('refresh');
  }

  // ----------------------- 表单 ------------------------
  const rules = {
    count: [{ required: true, message: '请输入数量' }],
  };

  //生成结果
  const generateResult = ref('');

  //  组件
  const formRef = ref();

  const form = reactive({
    serialNumberId: -1,
    businessName: '',
    format: '',
    ruleType: '',
    lastNumber: -1,
    count: 1,
  });

  function onSubmit() {
    formRef.value
      .validate()
      .then(async () => {
        SmartLoading.show();
        try {
          let res = await serialNumberApi.generate(form);
          message.success('生成成功');
          generateResult.value = _.join(res.data, ', ');
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
</script>
