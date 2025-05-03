<template>
  <a-modal title="添加" :width="700" :open="visibleFlag" @cancel="onClose" :maskClosable="false" :destroyOnClose="true">
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }" :wrapper-col="{ span: 16 }">
      <a-form-item label="标题" name="title">
        <a-input style="width: 100%" v-model:value="form.title" placeholder="标题" />
      </a-form-item>
      <a-form-item label="接收人" name="receiverUserIdList">
        <a-button @click="selectReceiver" type="primary"> 选择接收人 </a-button>
        <div>{{ nameListString }}</div>
      </a-form-item>
      <a-form-item label="消息类型" name="messageType">
        <SmartEnumSelect width="100%" v-model:value="form.messageType" placeholder="请选择类型" enum-name="MESSAGE_TYPE_ENUM" />
      </a-form-item>
      <a-form-item label="推送内容" name="content">
        <a-textarea style="width: 100%" v-model:value="form.content" placeholder="推送内容" />
      </a-form-item>
    </a-form>
    <template #footer>
      <a-space>
        <a-button @click="onClose">取消</a-button>
        <a-button type="primary" @click="onSubmit">保存</a-button>
      </a-space>
    </template>
  </a-modal>
  <MessageReceiverModal ref="receiverModalRef" @reloadList="addReceiverIdList" />
</template>
<script setup lang="ts">
  import { nextTick, reactive, ref } from 'vue';
  import { message } from 'ant-design-vue';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { smartSentry } from '/@/lib/smart-sentry';
  import SmartEnumSelect from '/@/components/framework/smart-enum-select/index.vue';
  import MessageReceiverModal from './message-receiver-modal.vue';
  import { USER_TYPE_ENUM } from '/@/constants/common-const';
  import { messageApi } from '/@/api/support/message-api';
  // ------------------------ 事件 ------------------------

  const emits = defineEmits(['reloadList']);

  // ------------------------ 显示与隐藏 ------------------------
  // 是否显示
  const visibleFlag = ref(false);

  function show(rowData) {
    Object.assign(form, formDefault);
    nameListString.value = null;
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
    title: undefined,
    receiverUserIdList: null,
    content: null,
    messageType: null,
    receiverUserType: USER_TYPE_ENUM.ADMIN_EMPLOYEE.value,
  };

  let form = reactive({ ...formDefault });

  const rules = {
    title: [{ required: true, message: '标题 必填' }],
    content: [{ required: true, message: '推送内容 必填' }],
    receiverUserIdList: [{ required: true, message: '推送人 必填' }],
    messageType: [{ required: true, message: '消息类型 必填' }],
  };

  const nameListString = ref();
  function addReceiverIdList(idList, nameList) {
    form.receiverUserIdList = idList;
    nameListString.value = nameList.join(',');
  }

  const receiverModalRef = ref();
  function selectReceiver() {
    receiverModalRef.value.showModal(form.receiverUserIdList);
  }

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
      let messageList = [];
      for (const userId of form.receiverUserIdList) {
        messageList.push({
          title: form.title,
          receiverUserId: userId,
          content: form.content,
          messageType: form.messageType,
          receiverUserType: USER_TYPE_ENUM.ADMIN_EMPLOYEE.value,
        });
      }
      await messageApi.sendMessages(messageList);
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
