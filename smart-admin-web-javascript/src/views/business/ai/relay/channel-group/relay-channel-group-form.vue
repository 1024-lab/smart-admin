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
      @cancel="onClose"
      :maskClosable="false"
      :destroyOnClose="true"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }">
        <a-form-item label="分组名称" name="channelName">
          <a-input style="width: 100%" v-model:value="form.channelName" placeholder="请输入分组名称" />
        </a-form-item>
        <a-form-item label="包含渠道" name="channelIdList">
          <a-select
              style="width: 100%"
              v-model:value="form.channelIdList"
              mode="multiple"
              placeholder="请选择渠道"
              :options="channelOptions"
              :field-names="{ label: 'channelName', value: 'channelId' }"
              :loading="channelLoading"
              show-search
              :filter-option="filterChannelOption"
          />
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
  import { reactive, ref, nextTick } from 'vue';
  import _ from 'lodash';
  import { message } from 'ant-design-vue';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { relayChannelGroupApi } from '/@/api/business/ai/relay/relay-channel-group-api';
  import { relayChannelApi } from '/@/api/business/ai/relay/relay-channel-api';
  import { smartSentry } from '/@/lib/smart-sentry';

  const emits = defineEmits(['reloadList']);
  const visibleFlag = ref(false);

  // ---------------------------- 渠道下拉数据 ----------------------------
  const channelOptions = ref([]);
  const channelLoading = ref(false);

  async function loadChannelOptions() {
    channelLoading.value = true;
    try {
      let result = await relayChannelApi.queryList({ disabledFlag: 0 });
      channelOptions.value = result.data || [];
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      channelLoading.value = false;
    }
  }

  function filterChannelOption(input, option) {
    return option.channelName.toLowerCase().includes(input.toLowerCase());
  }

  // ---------------------------- 表单 ----------------------------
  const formRef = ref();

  const formDefault = {
      channelGroupId: undefined,
      channelName: undefined,
      channelIdList: [],
      remark: undefined,
  };

  let form = reactive({ ...formDefault });

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
    loadChannelOptions();
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
      save();
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
