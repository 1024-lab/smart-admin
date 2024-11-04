<!--
  * 系统更新日志
  *
  * @Author:    卓大
  * @Date:      2022-09-26 14:53:50
  * @Copyright  1024创新实验室
-->
<template>
  <a-modal
    :title="form.changeLogId ? '编辑' : '添加'"
    width="600px"
    :closable="true"
    :open="visibleFlag"
    @close="onClose"
    :onCancel="onClose"
    :maskClosable="false"
    :destroyOnClose="true"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }">
      <a-form-item label="版本" name="version">
        <a-input style="width: 100%" v-model:value="form.version" placeholder="版本" />
      </a-form-item>
      <a-form-item label="更新类型" name="type">
        <SmartEnumSelect width="100%" v-model:value="form.type" enumName="CHANGE_LOG_TYPE_ENUM" placeholder="更新类型" />
      </a-form-item>
      <a-form-item label="发布人" name="publishAuthor">
        <a-input style="width: 100%" v-model:value="form.publishAuthor" placeholder="发布人" />
      </a-form-item>
      <a-form-item label="发布日期" name="publicDate">
        <a-date-picker valueFormat="YYYY-MM-DD" v-model:value="form.publicDate" style="width: 100%" placeholder="发布日期" />
      </a-form-item>
      <a-form-item label="跳转链接" name="link">
        <a-input style="width: 100%" v-model:value="form.link" placeholder="跳转链接" />
      </a-form-item>
      <a-form-item label="更新内容" name="content">
        <a-textarea style="width: 100%" :rows="15"  v-model:value="form.content" placeholder="更新内容" />
      </a-form-item>
    </a-form>

    <template #footer>
      <a-space>
        <a-button @click="onClose">取消</a-button>
        <a-button type="primary" @click="onSubmit">保存</a-button>
      </a-space>
    </template>
  </a-modal>
</template>
<script setup>
  import { reactive, ref, nextTick } from 'vue';
  import _ from 'lodash';
  import { message } from 'ant-design-vue';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { changeLogApi } from '/@/api/support/change-log-api';
  import { smartSentry } from '/@/lib/smart-sentry';
  import SmartEnumSelect from '/@/components/framework/smart-enum-select/index.vue';

  // ------------------------ 事件 ------------------------

  const emits = defineEmits(['reloadList']);

  // ------------------------ 显示与隐藏 ------------------------
  // 是否显示
  const visibleFlag = ref(false);

  function show(rowData) {
    Object.assign(form, formDefault);
    if (rowData && !_.isEmpty(rowData)) {
      Object.assign(form, rowData);
    }
    visibleFlag.value = true;
    nextTick(() => {
      formRef.value.clearValidate();
    });
  }

  function onClose() {
    Object.assign(form, formDefault);
    visibleFlag.value = false;
  }

  // ------------------------ 表单 ------------------------

  // 组件ref
  const formRef = ref();

  const formDefault = {
    changeLogId: undefined,
    version: undefined, //版本
    type: undefined, //更新类型:[1:特大版本功能更新;2:功能更新;3:bug修复]
    publishAuthor: undefined, //发布人
    publicDate: undefined, //发布日期
    content: undefined, //更新内容
    link: undefined, //跳转链接
  };

  let form = reactive({ ...formDefault });

  const rules = {
    version: [{ required: true, message: '版本 必填' }],
    type: [{ required: true, message: '更新类型:[1:特大版本功能更新;2:功能更新;3:bug修复] 必填' }],
    publishAuthor: [{ required: true, message: '发布人 必填' }],
    publicDate: [{ required: true, message: '发布日期 必填' }],
    content: [{ required: true, message: '更新内容 必填' }],
  };

  // 点击确定，验证表单
  async function onSubmit() {
    try {
      await formRef.value.validateFields();
      save();
    } catch (err) {
      message.error('参数验证错误，请仔细填写表单数据!');
    }
  }

  // 新建、编辑API
  async function save() {
    SmartLoading.show();
    try {
      if (form.changeLogId) {
        await changeLogApi.update(form);
      } else {
        await changeLogApi.add(form);
      }
      message.success('操作成功');
      emits('reloadList');
      onClose();
    } catch (err) {
      smartSentry.captureError(err);
    } finally {
      SmartLoading.hide();
    }
  }

  defineExpose({
    show,
  });
</script>
