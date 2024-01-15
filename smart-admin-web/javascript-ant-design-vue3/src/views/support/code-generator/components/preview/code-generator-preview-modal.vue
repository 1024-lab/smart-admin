<!--
  * 代码生成 预览代码
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-09-22 21:50:41 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
-->
<template>
  <a-drawer
    title="代码预览"
    :open="visibleFlag"
    :width="1200"
    :footerStyle="{ textAlign: 'right' }"
    :bodyStyle="{ padding: '8px 24px' }"
    @close="onClose"
    :maskClosable="false"
    :destroyOnClose="true"
  >
    <a-row justify="space-between" class="smart-margin-bottom10">
      <a-radio-group v-model:value="languageType" button-style="solid" @change="onChangeLanguageType">
        <a-radio-button :value="LANGUAGE_LIST[0]">JavaScript代码</a-radio-button>
        <a-radio-button :value="LANGUAGE_LIST[1]">TypeScript代码</a-radio-button>
        <a-radio-button :value="LANGUAGE_LIST[2]">Java代码</a-radio-button>
      </a-radio-group>

      <a-button type="link" @click="download" danger size="small"><strong>下载代码</strong></a-button>
    </a-row>
    <a-tabs v-model:activeKey="fileKey" size="small" @change="onChangeTab">
      <a-tab-pane v-for="item in tabList" :key="item" :tab="item" />
    </a-tabs>
    <div>
      <pre><code :class="codeClass">{{resultCode}}</code></pre>
    </div>
  </a-drawer>
</template>

<script setup>
  import { computed, nextTick, ref, watch } from 'vue';
  import { codeGeneratorApi } from '/@/api/support/code-generator-api';
  import { JAVA_FILE_LIST, LANGUAGE_LIST, JS_FILE_LIST,TS_FILE_LIST, JAVA_DOMAIN_FILE_LIST } from '../../code-generator-util';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { lineNumbersBlock } from '/@/lib/highlight-line-number';
  import hljs from 'highlight.js';
  import 'highlight.js/styles/github-dark.css';
  import javascript from 'highlight.js/lib/languages/javascript';
  import typescript from 'highlight.js/lib/languages/typescript';
  import java from 'highlight.js/lib/languages/java';
  import { message } from 'ant-design-vue';

  // ------------------ 显示，关闭 ------------------

  // 显示
  const visibleFlag = ref(false);
  function showModal(tableInfo) {
    tableName.value = tableInfo.tableName;
    tableComment.value = tableInfo.tableComment;
    visibleFlag.value = true;
    nextTick(() => {
      onChangeTab(fileKey.value);
    });
  }

  // 关闭
  function onClose() {
    visibleFlag.value = false;
  }

  // ------------------ 表------------------
  const tableName = ref('');
  const tableComment = ref('');

  // ------------------ 标签页 ------------------
  const languageType = ref(LANGUAGE_LIST[0]);
  const tabList = computed(() => {
    if(languageType.value === LANGUAGE_LIST[0]){
      return JS_FILE_LIST;
    }else if(languageType.value === LANGUAGE_LIST[1]){
      return TS_FILE_LIST;
    }else{
      return JAVA_FILE_LIST;
    }
  });

  const fileKey = ref(tabList.value[0]);

  function getLanguage() {
    if(languageType.value === LANGUAGE_LIST[0]){
      return 'javascript';
    }else if(languageType.value === LANGUAGE_LIST[1]){
      return 'typescript';
    }else{
      return 'java';
    }
  }

  function onChangeLanguageType(e){
    if(e.target.value === LANGUAGE_LIST[0]){
      fileKey.value = JS_FILE_LIST[0];
    }else if(e.target.value === LANGUAGE_LIST[1]){
      fileKey.value = TS_FILE_LIST[0];
    }else{
      fileKey.value = JAVA_FILE_LIST[0];
    }
    onChangeTab(fileKey.value);
  }

  // ------------------ 下载代码 ------------------

  function download(rowData) {
    codeGeneratorApi.downloadCode(tableName.value);
  }

  // ------------------ 查询代码 ------------------
  const codeClass = ref('language-javascript');
  function onChangeTab(tab) {
    let templateFile = tab;

    let language = getLanguage();
    hljs.registerLanguage(language, language == 'java' ? java : javascript);
    codeClass.value = 'language-' + language;

    console.log(templateFile);
    nextTick(() => {
      generateCode(templateFile, tableName.value);
    });
  }

  const resultCode = ref('');
  async function generateCode(templateFile, tableName) {
    try {
      let result = await codeGeneratorApi.preview({
        templateFile,
        tableName,
      });
      resultCode.value = result.data;
      nextTick(() => {
        document.querySelectorAll('pre code').forEach((block) => {
          block.setAttribute('highlighted', 'true');
          hljs.highlightElement(block);
          lineNumbersBlock(block);
          block.innerHTML =
            "<div><div style='padding: 5px 0px 10px 20px;float:right'><span style='margin-right: 10px;padding: 5px;border: white solid 1px;color:white;border-radius: 2px'>" +
            block.className.match(/(?<=language-).*(?= hljs)/).toString() +
            "</span><button class='ant-btn ant-btn-sm' >复制代码</button></div>" +
            block.innerHTML +
            '</div>';
          let copyButton = block.querySelector('button');
          if (copyButton != null) {
            copyButton.onclick = function () {
              copy(resultCode.value);
              message.success('复制成功！');
            };
          }
        });
      });
    } catch (e) {
      smartSentry.captureError(e);
    }
  }

  function copy(value) {
    let textarea = document.createElement('textarea');
    document.body.appendChild(textarea);
    textarea.value = value;
    textarea.select();
    document.execCommand('Copy');
    document.body.removeChild(textarea);
  }

  defineExpose({
    showModal,
  });
</script>

<style lang="less" scoped>
  .preview-title {
    font-weight: 600;
    margin: 5px 0px;
  }

  .preview-block {
    font-size: 12px;
    background-color: #f9f9f9;
    padding: 10px 5px;
  }

  :deep(.hljs) {
    .hljs-ln-numbers {
      text-align: center;
      color: #ccc;
      border-right: 1px solid #ccc;
      vertical-align: top;
      padding-right: 5px !important;
    }

    .hljs-ln-code {
      padding-left: 5px !important;
    }
  }
</style>
