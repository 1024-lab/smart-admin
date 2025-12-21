<!--
  * 应用提供商
  *
  * @Author:    Mission
  * @Date:      2025-12-21 16:06:19
  * @Copyright  Mission
-->
<template>
  <a-drawer
      :title="form.id ? '编辑' : '添加'"
      :width="500"
      :open="visibleFlag"
      @close="onClose"
      :maskClosable="false"
      :destroyOnClose="true"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }" >
        <a-form-item label="id"  name="id">
          <a-input style="width: 100%" v-model:value="form.id" placeholder="id" />
        </a-form-item>
        <a-form-item label="名称"  name="name">
          <a-input style="width: 100%" v-model:value="form.name" placeholder="名称" />
        </a-form-item>
        <a-form-item label="baseUrl"  name="baseUrl">
          <a-input style="width: 100%" v-model:value="form.baseUrl" placeholder="baseUrl" />
        </a-form-item>
        <a-form-item label="key"  name="key">
          <a-input style="width: 100%" v-model:value="form.key" placeholder="key" />
        </a-form-item>
    </a-form>

    <template #footer>
      <a-space>
        <a-button @click="onClose">取消</a-button>
        <a-button type="primary" @click="onSubmit">保存</a-button>
      </a-space>
    </template>
  </a-drawer>
</template>
<script setup>
  import { reactive, ref, nextTick } from 'vue';
  import _ from 'lodash';
  import { message } from 'ant-design-vue';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { aiAppProviderApi } from '/@/api/business/ai-app-provider/ai-app-provider-api';
  import { smartSentry } from '/@/lib/smart-sentry';

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
    // 使用字典时把下面这注释修改成自己的字典字段 有多个字典字段就复制多份同理修改 不然打开表单时不显示字典初始值
    // if (form.status && form.status.length > 0) {
    //   form.status = form.status.map((e) => e.valueCode);
    // }
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
      id: undefined, //id
      name: undefined, //名称
      baseUrl: undefined, //baseUrl
      key: undefined, //key
  };

  let form = reactive({ ...formDefault });

  const rules = {
      id: [{ required: true, message: 'id 必填' }],
      name: [{ required: true, message: '名称 必填' }],
      baseUrl: [{ required: true, message: 'baseUrl 必填' }],
      key: [{ required: true, message: 'key 必填' }],
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
      if (form.id) {
        await aiAppProviderApi.update(form);
      } else {
        await aiAppProviderApi.add(form);
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
