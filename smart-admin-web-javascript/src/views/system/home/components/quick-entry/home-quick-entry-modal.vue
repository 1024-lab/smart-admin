<template>
  <a-modal v-model:open="visible" title="新建快捷入口" @close="onClose">
    <a-form ref="formRef" :model="form" :rules="rules">
      <a-form-item label="图标" name="icon">
        <IconSelect @updateIcon="selectIcon">
          <template #iconSelect>
            <a-input v-model:value="form.icon" placeholder="请输入菜单图标" style="width: 200px" />
            <component :is="$antIcons[form.icon]" class="smart-margin-left15" style="font-size: 20px" />
          </template>
        </IconSelect>
      </a-form-item>
      <a-form-item label="标题" name="title">
        <a-input v-model:value="form.title" placeholder="请输入标题" />
      </a-form-item>
      <a-form-item label="路径" name="path">
        <a-input v-model:value="form.path" placeholder="请输入路径" />
      </a-form-item>
    </a-form>
    <template #footer>
      <a-button @click="onClose">取消</a-button>
      <a-button type="primary" @click="onSubmit">提交</a-button>
    </template>
  </a-modal>
</template>
<script setup>
  import { reactive, ref } from 'vue';
  import { message } from 'ant-design-vue';
  import IconSelect from '/@/components/framework/icon-select/index.vue';
  import _ from 'lodash';

  defineExpose({
    showModal,
  });

  const emit = defineEmits(['addQuickEntry']);

  // 组件ref
  const formRef = ref();

  const formDefault = {
    icon: undefined,
    title: '',
    path: '',
  };
  let form = reactive({ ...formDefault });
  const rules = {
    icon: [{ required: true, message: '请选择图标' }],
    title: [{ required: true, message: '标题不能为空' }],
    path: [{ required: true, message: '路径不能为空' }],
  };

  const visible = ref(false);

  function showModal() {
    visible.value = true;
  }

  function selectIcon(icon) {
    form.icon = icon;
  }

  function onClose() {
    Object.assign(form, formDefault);
    visible.value = false;
  }

  function onSubmit() {
    formRef.value
      .validate()
      .then(() => {
        emit('addQuickEntry', _.cloneDeep(form));
        onClose();
      })
      .catch((error) => {
        console.log('error', error);
        message.error('参数验证错误，请仔细填写表单数据!');
      });
  }
</script>
<style lang="less" scoped></style>
