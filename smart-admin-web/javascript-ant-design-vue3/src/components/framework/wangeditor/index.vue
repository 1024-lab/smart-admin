<!-- 
  * 编辑器
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-09-12 15:34:33 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
  *
-->
<template>
  <div style="border: 1px solid #ccc">
    <Toolbar style="border-bottom: 1px solid #ccc" :editor="editorRef" />
    <Editor
      style="overflow-y: hidden"
      :style="{ height: `${height}px` }"
      v-model="editorHtml"
      :defaultConfig="editorConfig"
      @onCreated="handleCreated"
      @onChange="handleChange"
    />
  </div>
</template>
<script setup>
  import { shallowRef, onBeforeUnmount, watch, ref } from 'vue';
  import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/support/file-const';
  import { fileApi } from '/@/api/support/file/file-api';
  import '@wangeditor/editor/dist/css/style.css';
  import { Editor, Toolbar } from '@wangeditor/editor-for-vue';
  import { smartSentry } from '/@/lib/smart-sentry';

  //菜单
  const editorConfig = { MENU_CONF: {} };

  //上传
  let customUpload = {
    async customUpload(file, insertFn) {
      try {
        const formData = new FormData();
        formData.append('file', file);
        let res = await fileApi.uploadFile(formData, FILE_FOLDER_TYPE_ENUM.COMMON.value);
        let data = res.data;
        insertFn(data.fileUrl);
      } catch (error) {
        smartSentry.captureError(error);
      }
    },
  };
  editorConfig.MENU_CONF['uploadImage'] = customUpload;
  editorConfig.MENU_CONF['uploadVideo'] = customUpload;

  // ----------------------- 以下是公用变量 emits props ----------------
  const editorHtml = ref();
  let props = defineProps({
    modelValue: String,
    height: {
      type: Number,
      default: 500,
    },
  });
  watch(
    () => props.modelValue,
    (nVal) => {
      console.log(nVal);
      editorHtml.value = nVal;
    },
    {
      immediate: true,
      deep: true,
    }
  );

  // 获取编辑器实例html
  const emit = defineEmits(['update:modelValue']);
  const editorRef = shallowRef();
  const handleCreated = (editor) => {
    editorRef.value = editor;
  };
  const handleChange = (editor) => {
    emit('update:modelValue', editorHtml.value);
  };

  function getHtml() {
    const htmlContent = editorRef.value.getHtml();
    return htmlContent === '<p><br></p>' ? '' : htmlContent;
  }
  function getText() {
    return editorRef.value.getText();
  }

  // 组件销毁时，也及时销毁编辑器
  onBeforeUnmount(() => {
    const editor = editorRef.value;
    if (editor == null) return;
    editor.destroy();
  });

  defineExpose({
    editorRef,
    getHtml,
    getText,
  });

</script>
<style scoped>
  .w-e-full-screen-container {
    z-index: 9999 !important;
  }
</style>
