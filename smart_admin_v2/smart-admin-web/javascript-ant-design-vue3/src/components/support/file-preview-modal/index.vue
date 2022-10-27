<!--
  * 文件预览 弹窗
  * 
  * @Author:    1024创新实验室：善逸
  * @Date:      2022-09-02 20:19:39 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
  *
-->
<template>
  <a-modal title="文件预览" v-model:visible="visibleFlag" :width="768" @cancel="onClose">
    <div class="container">
      <img class="img-prev" :src="previewUrl" />
    </div>
    <template #footer>
      <a-button @click="onClose">关闭</a-button>
    </template>
  </a-modal>
</template>

<script setup>
  import { ref } from 'vue';
  import { download } from '/@/lib/axios';
  import { fileApi } from '/@/api/support/file/file-api';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { SmartLoading } from '/@/components/framework/smart-loading';

  const visibleFlag = ref(false);
  const imgFileType = ['jpg', 'jpeg', 'png', 'gif'];
  const previewUrl = ref();

  function showPreview(fileItem) {
    if (!fileItem.fileUrl) {
      (async () => {
        SmartLoading.show();
        try {
          let res = await fileApi.getUrl(fileItem.fileKey);
          fileItem.fileUrl = res.data;
          showFile(fileItem);
        } catch (e) {
          smartSentry.captureError(e);
        } finally {
          SmartLoading.hide();
        }
      })();
    } else {
      showFile(fileItem);
    }
  }

  function showFile(fileItem) {
    if (isImg(fileItem.fileType)) {
      previewUrl.value = fileItem.fileUrl;
      visibleFlag.value = true;
      return;
    }
    download(fileItem.fileName, fileItem.fileUrl);
  }

  // 判断图片类型
  function isImg(fileType) {
    return imgFileType.includes(fileType);
  }

  function onClose() {
    visibleFlag.value = false;
  }

  defineExpose({
    showPreview,
  });
</script>

<style lang="less" scoped>
  .container {
    display: flex;
    justify-content: center;
    align-items: center;
    .img-prev {
      display: block;
      width: 100%;
      height: 600px;
      object-fit: contain;
    }
  }
</style>
