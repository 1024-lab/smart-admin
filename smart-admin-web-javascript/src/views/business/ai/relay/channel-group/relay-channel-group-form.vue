<!--
  * Relay 渠道分组管理 - 新增/编辑表单
  *
  * @Author:    Mission
  * @Date:      2026-03-16
  * @Copyright  Mission
-->
<template>
  <a-modal
    :title="form.channelGroupId ? '编辑渠道分组' : '新增渠道分组'"
    :width="520"
    :open="visibleFlag"
    :maskClosable="false"
    :destroyOnClose="true"
    @cancel="onClose"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }">
      <a-form-item label="分组名称" name="channelName">
        <a-input v-model:value="form.channelName" style="width: 100%" placeholder="请输入分组名称" />
      </a-form-item>

      <a-form-item label="包含渠道" name="channelIdList">
        <RelayChannelSelect v-model:value="form.channelIdList" width="100%" :multiple="true" :disabledFlag="0" />
      </a-form-item>

      <a-form-item label="备注" name="remark">
        <a-textarea v-model:value="form.remark" placeholder="请输入备注" :rows="3" />
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
  import { nextTick, reactive, ref } from 'vue';
  import _ from 'lodash';
  import { message } from 'ant-design-vue';
  import { relayChannelGroupApi } from '/@/api/business/ai/relay/relay-channel-group-api';
  import RelayChannelSelect from '/@/components/business/relay/relay-channel-select/index.vue';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { smartSentry } from '/@/lib/smart-sentry';

  const emits = defineEmits(['reloadList']);
  const visibleFlag = ref(false);
  const formRef = ref();

  const formDefault = {
    channelGroupId: undefined,
    channelName: undefined,
    channelIdList: [],
    remark: undefined,
  };

  const form = reactive({ ...formDefault });

  const rules = {
      channelName: [{ required: true, message: '分组名称必填' }],
      channelIdList: [{ required: true, type: 'array', min: 1, message: '请至少选择一个渠道' }],
  };

  function show(rowData) {
    Object.assign(form, formDefault);
    form.channelIdList = [];
    if (rowData && !_.isEmpty(rowData)) {
      Object.assign(form, rowData);
      form.channelIdList = rowData.channelIdList ? [...rowData.channelIdList] : [];
    }
    visibleFlag.value = true;
    nextTick(() => {
      formRef.value.clearValidate();
    });
  }

  function onClose() {
    Object.assign(form, formDefault);
    form.channelIdList = [];
    visibleFlag.value = false;
  }

  async function onSubmit() {
    try {
      await formRef.value.validateFields();
      await save();
    } catch (err) {
      message.error('参数验证错误，请仔细填写表单数据!');
    }
  }

  async function save() {
    SmartLoading.show();
    try {
      if (form.channelGroupId) {
        await relayChannelGroupApi.update(form);
      } else {
        await relayChannelGroupApi.add(form);
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

  defineExpose({ show });
</script>
