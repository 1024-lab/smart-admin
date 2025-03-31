<template>
  <CopyOutlined
    @click="copy"
    :style="{
      color: `${color}`,
    }"
    class="icon"
  />
</template>

<script setup lang="ts">
  import { message } from 'ant-design-vue';
  const props = defineProps({
    value: {
      type: [String, Number],
      default: '',
    },
    color: {
      type: String,
      default: '#1890ff',
    },
  });

  function copy() {
    const textarea = document.createElement('textarea');
    textarea.value = props.value;
    document.body.appendChild(textarea);
    textarea.select();
    document.execCommand('copy');
    document.body.removeChild(textarea);
    message.success('复制成功');
  }
</script>
<style scoped lang="less">
  .icon {
    margin: 0 10px;
  }
</style>
