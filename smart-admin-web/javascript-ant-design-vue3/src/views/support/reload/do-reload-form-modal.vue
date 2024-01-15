<!--
  * reload 表单
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-07-21 21:55:12
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
-->
<template>
  <a-modal :open="visible" title="执行Reload" ok-text="确认" cancel-text="取消" @ok="onSubmit" @cancel="onClose">
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }">
      <a-form-item label="标签">
        <a-input v-model:value="form.tag" :disabled="true" />
      </a-form-item>
      <a-form-item label="运行标识" name="identification">
        <a-input v-model:value="form.identification" placeholder="请输入运行标识" />
      </a-form-item>
      <a-form-item label="参数" name="args">
        <a-input v-model:value="form.args" placeholder="请输入参数" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script setup>
  import { message } from 'ant-design-vue';
  import { reactive, ref } from 'vue';
  import { reloadApi } from '/@/api/support/reload-api';
import { smartSentry } from '/@/lib/smart-sentry';
  import { SmartLoading } from '/@/components/framework/smart-loading';

  // emit
  const emit = defineEmits(['refresh']);
  defineExpose({
    showModal,
  });

  // ----------------------- 表单 隐藏 与 显示 ------------------------
  // 是否展示
  const visible = ref(false);
  function showModal(tag) {
    form.tag = tag;
    form.identification = '';
    form.args = '';
    visible.value = true;
  }

  function onClose() {
    Object.assign(form, formDefault);
    visible.value = false;
  }

  //  组件
  const formRef = ref();

  const formDefault = {
    tag: '',
    identification: '',
    args: '',
  };
  let form = reactive({ ...formDefault });
  const rules = {
    identification: [{ required: true, message: '请输入运行标识' }],
    args: [{ required: true, message: '请输入参数值' }],
  };

  // ----------------------- 提交 ------------------------

  function onSubmit() {
    formRef.value
      .validate()
      .then(async () => {
        SmartLoading.show();
        try {
          await reloadApi.reload(form);
          message.success('reload成功');
          emit('refresh');
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
</script>
