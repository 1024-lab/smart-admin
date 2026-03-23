<template>
  <a-modal v-model:open="showFlag" :cancelText="null" :width="800" title="消息内容" :closable="false" :maskClosable="false" :destroyOnClose="true" @ok="showFlag = false">
    <a-descriptions bordered :column="2" size="small">
      <a-descriptions-item :labelStyle="{ width: '80px' }" :span="1" label="类型"
        >{{ $smartEnumPlugin.getDescByValue('MESSAGE_TYPE_ENUM', messageDetail.messageType) }}
      </a-descriptions-item>
      <a-descriptions-item :labelStyle="{ width: '120px' }" :span="1" label="发送时间">{{ messageDetail.createTime }}</a-descriptions-item>
      <a-descriptions-item :labelStyle="{ width: '80px' }" :span="2" label="标题">{{ messageDetail.title }}</a-descriptions-item>
      <a-descriptions-item :labelStyle="{ width: '80px' }" :span="2" label="内容">
        <pre>{{ messageDetail.content }}</pre>
      </a-descriptions-item>
    </a-descriptions>
    <template #footer>
      <a-button  type="primary"  @click="showFlag = false">关闭</a-button>
    </template>
  </a-modal>
</template>
<script setup>
  import { reactive, ref } from 'vue';
  import { messageApi } from '/@/api/support/message-api.js';

  const emit = defineEmits(['refresh']);

  const messageDetail = reactive({
    messageType: '',
    title: '',
    content: '',
    createTime: '',
  });

  const showFlag = ref(false);

  function show(data) {
    Object.assign(messageDetail, data);
    showFlag.value = true;
    read(data);
  }

  async function read(message) {
    if (!message.readFlag) {
      await messageApi.updateReadFlag(message.messageId);
      emit('refresh');
    }
  }

  defineExpose({ show });
</script>
