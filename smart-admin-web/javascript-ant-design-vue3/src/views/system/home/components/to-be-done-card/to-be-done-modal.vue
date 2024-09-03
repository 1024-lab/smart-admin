<template>
  <div>
    <a-modal v-model:open="visible" title="新建待办" @close="onClose">
      <a-form ref="formRef" :model="form" :rules="rules">
        <a-form-item label="标题" name="title">
          <a-input v-model:value="form.title" placeholder="请输入标题" />
        </a-form-item>
      </a-form>
      <template #footer>
        <a-button @click="onClose">取消</a-button>
        <a-button type="primary" @click="onSubmit">确定</a-button>
      </template>
    </a-modal>
  </div>
</template>
<script setup>
  import { reactive, ref } from 'vue';
  import { message } from 'ant-design-vue';
  import _ from 'lodash';

  defineExpose({
    showModal,
  });

  const emit = defineEmits(['addToBeDone']);

  // 组件ref
  const formRef = ref();

  const formDefault = {
    title: undefined,
    doneFlag: false,
    starFlag: false,
    starTime: 0,
  };
  let form = reactive({ ...formDefault });
  const rules = {
    title: [{ required: true, message: '标题不能为空' }],
  };

  const visible = ref(false);

  function showModal() {
    visible.value = true;
  }

  function onClose() {
    Object.assign(form, formDefault);
    visible.value = false;
  }

  function onSubmit() {
    formRef.value
      .validate()
      .then(() => {
        emit('addToBeDone', _.cloneDeep(form));
        onClose();
      })
      .catch((error) => {
        console.log('error', error);
        message.error('参数验证错误，请仔细填写表单数据!');
      });
  }
</script>
<style lang="less" scoped></style>
