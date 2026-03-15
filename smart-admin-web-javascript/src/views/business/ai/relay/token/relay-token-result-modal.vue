<!--
  * Relay Token管理 - 创建结果弹窗（一次性展示Token）
  *
  * @Author:    Mission
  * @Date:      2026-03-15
  * @Copyright  Mission
-->
<template>
  <a-modal
      title="Token创建成功"
      :width="520"
      :open="visibleFlag"
      @cancel="onClose"
      :maskClosable="false"
      :footer="null"
  >
    <a-alert
        message="请妥善保存以下Token，此Token仅展示一次，关闭后将无法再次查看！"
        type="warning"
        show-icon
        style="margin-bottom: 16px"
    />
    <a-descriptions :column="1" bordered size="small">
      <a-descriptions-item label="Token名称">{{ tokenData.tokenName }}</a-descriptions-item>
      <a-descriptions-item label="Token前缀">{{ tokenData.tokenPrefix }}</a-descriptions-item>
      <a-descriptions-item label="完整Token">
        <div style="display: flex; align-items: center; gap: 8px; word-break: break-all;">
          <span>{{ tokenData.plainToken }}</span>
          <a-button type="primary" size="small" @click="copyToken">复制</a-button>
        </div>
      </a-descriptions-item>
    </a-descriptions>
    <div style="text-align: center; margin-top: 16px">
      <a-button type="primary" @click="onClose">我已保存，关闭</a-button>
    </div>
  </a-modal>
</template>
<script setup>
  import { ref, reactive } from 'vue';
  import { message } from 'ant-design-vue';

  const visibleFlag = ref(false);
  const tokenData = reactive({
    tokenId: undefined,
    tokenName: '',
    tokenPrefix: '',
    plainToken: '',
  });

  function show(data) {
    Object.assign(tokenData, data);
    visibleFlag.value = true;
  }

  function onClose() {
    visibleFlag.value = false;
  }

  async function copyToken() {
    try {
      await navigator.clipboard.writeText(tokenData.plainToken);
      message.success('已复制到剪贴板');
    } catch (e) {
      // fallback
      const textarea = document.createElement('textarea');
      textarea.value = tokenData.plainToken;
      document.body.appendChild(textarea);
      textarea.select();
      document.execCommand('copy');
      document.body.removeChild(textarea);
      message.success('已复制到剪贴板');
    }
  }

  defineExpose({ show });
</script>
