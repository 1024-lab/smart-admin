<!--
  * 系统更新日志 查看
  *
  * @Author:    卓大
  * @Date:      2022-09-26 14:53:50
  * @Copyright  1024创新实验室
-->
<template>
  <a-modal title="更新日志" width="700px" :open="visibleFlag" @cancel="onClose">
    <div>
      <pre>{{ content }}</pre>
      <div v-if="link">
        链接：<a :href="link" target="_blank">{{ link }}</a>
      </div>
    </div>

    <template #footer>
      <a-space>
        <a-button type="primary" @click="onClose">关闭</a-button>
      </a-space>
    </template>
  </a-modal>
</template>
<script setup>
  import { ref } from 'vue';

  const visibleFlag = ref(false);
  const content = ref('');
  const link = ref('');

  function show(changeLog) {
    content.value = changeLog.content;
    link.value = changeLog.link;
    visibleFlag.value = true;
  }

  function onClose() {
    visibleFlag.value = false;
  }

  defineExpose({
    show,
  });
</script>
