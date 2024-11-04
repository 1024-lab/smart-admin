<!--
  * 目录表单
  * 
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2022-07-21 21:55:12
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
-->
<template>
  <a-modal v-model:open="visible" :title="formState.helpDocCatalogId ? '编辑目录' : '添加目录'" @ok="handleOk" destroyOnClose>
    <a-form ref="formRef" :model="formState" :rules="rules" layout="vertical">
      <a-form-item label="上级目录" name="parentId" v-if="formState.parentId !== 0">
        <HelpDocCatalogTreeSelect ref="helpDocCatalogTreeSelect" v-model:value="formState.parentId" :defaultValueFlag="false" width="100%" />
      </a-form-item>
      <a-form-item label="目录名称" name="name">
        <a-input v-model:value.trim="formState.name" placeholder="请输入目录名称" />
      </a-form-item>
      <a-form-item label="目录排序 （值越小越靠前！）" name="sort">
        <a-input-number style="width: 100%" v-model:value="formState.sort" :min="0" placeholder="请输入目录名称" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script setup>
  import message from 'ant-design-vue/lib/message';
  import { reactive, ref } from 'vue';
  import { helpDocCatalogApi } from '/@/api/support/help-doc-catalog-api';
  import HelpDocCatalogTreeSelect from './help-doc-catalog-tree-select.vue';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { smartSentry } from '/@/lib/smart-sentry';

  // ----------------------- 对外暴漏 ---------------------

  defineExpose({
    showModal,
  });

  // ----------------------- modal 的显示与隐藏 ---------------------
  const emits = defineEmits(['refresh']);

  const visible = ref(false);
  function showModal(data) {
    visible.value = true;
    updateFormData(data);
  }
  function closeModal() {
    visible.value = false;
    resetFormData();
  }

  // ----------------------- form 表单操作 ---------------------
  const formRef = ref();
  const helpDocCatalogTreeSelect = ref();
  const defaultHelpDocCatalogForm = {
    helpDocCatalogId: undefined,
    name: undefined,
    parentId: undefined,
    sort: 0,
  };
  const employeeSelect = ref();

  let formState = reactive({
    ...defaultHelpDocCatalogForm,
  });
  // 表单校验规则
  const rules = {
    parentId: [{ required: true, message: '上级目录不能为空' }],
    name: [
      { required: true, message: '目录名称不能为空' },
      { max: 50, message: '目录名称不能大于20个字符', trigger: 'blur' },
    ],
  };
  // 更新表单数据
  function updateFormData(data) {
    Object.assign(formState, defaultHelpDocCatalogForm);
    if (data) {
      Object.assign(formState, data);
    }
    visible.value = true;
  }
  // 重置表单数据
  function resetFormData() {
    Object.assign(formState, defaultHelpDocCatalogForm);
  }

  async function handleOk() {
    try {
      await formRef.value.validate();
      if (formState.helpDocCatalogId) {
        updateHelpDocCatalog();
      } else {
        addHelpDocCatalog();
      }
    } catch (error) {
      message.error('参数验证错误，请仔细填写表单数据!');
    }
  }

  // ----------------------- form 表单  ajax 操作 ---------------------
  //添加目录ajax请求
  async function addHelpDocCatalog() {
    SmartLoading.show();
    try {
      await helpDocCatalogApi.add(formState);
      emits('refresh');
      closeModal();
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      SmartLoading.hide();
    }
  }

  //更新目录ajax请求
  async function updateHelpDocCatalog() {
    SmartLoading.show();
    try {
      if (formState.parentId === formState.helpDocCatalogId) {
        message.warning('上级菜单不能为自己');
        return;
      }
      await helpDocCatalogApi.update(formState);
      emits('refresh');
      closeModal();
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      SmartLoading.hide();
    }
  }
</script>
