<!--
  * 文件预览
  * 
  * @Author:    1024创新实验室：善逸
  * @Date:      2022-07-19 23:19:39 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
  *
-->
<template>
  <div>
    <template v-if="type === 'text'">
      <a v-for="(item, index) in fileList" :key="index" @click="preview(item, index)">
        {{ item.fileName }}
        <span v-if="index !== fileList.length - 1" v-html="separator"></span>
      </a>
    </template>
    <a-space>
      <a-image-preview-group :preview="{ visible, onVisibleChange: setVisible, current: previewCurrent }">
        <a-image
          v-for="(item, index) in fileList"
          :key="index"
          :src="item.fileUrl"
          :style="{ display: type === 'text' ? 'none' : '', padding: '2px', height: '100px' }"
          :width="width"
          @click="preview(item, index)"
        />
      </a-image-preview-group>
    </a-space>
  </div>
</template>
<script setup>
  import { ref } from 'vue';
  import { fileApi } from '/src/api/support/file-api';

  let props = defineProps({
    fileList: {
      type: Array,
      default: () => {
        return [];
      },
    },
    // 类型 text,picture
    type: {
      type: String,
      default: 'text',
    },
    // image 宽度
    width: {
      type: Number,
      default: 100,
    },
    // 分隔符 可设置html标签 例如：<br/>
    separator: {
      type: String,
      default: '，',
    },
  });
  const imgFileType = ['jpg', 'jpeg', 'png', 'gif'];

  // 文件预览
  function preview(file, index) {
    if (imgFileType.some((e) => e === file.fileType)) {
      previewCurrent.value = index;
      visible.value = true;
    } else {
      fileApi.downLoadFile(file.fileKey);
    }
  }

  // 预览
  const visible = ref(false);
  const previewCurrent = ref(0);

  function setVisible(value) {
    visible.value = value;
  }
</script>
