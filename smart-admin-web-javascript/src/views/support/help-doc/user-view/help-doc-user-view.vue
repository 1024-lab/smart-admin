<!--
  * 帮助文档详情
  * 
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2022-07-21 21:55:12
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
-->
<template>
  <a-card size="small" :bordered="false">
    <div v-if="helpDocDetail">
      <div class="content-header">
        <!--startprint-->
        <div class="content-header-title">
          {{ helpDocDetail.title }}
        </div>
        <div class="content-header-info">
          <span>阅读量：{{ helpDocDetail.pageViewCount }}</span>
          <span v-show="helpDocDetail.author">作者：{{ helpDocDetail.author }}</span>
          <span>发布于：{{ helpDocDetail.createTime }}</span>
          <span>修改于：{{ helpDocDetail.updateTime }}</span>
          <span @click="print">【打印本页】</span>
        </div>
      </div>
      <div class="content-html" v-html="helpDocDetail.contentHtml"></div>
      <!--endprint-->
    </div>
    <a-divider v-if="helpDocDetail.attachment && helpDocDetail.attachment.length > 0" />
    <div v-if="helpDocDetail.attachment && helpDocDetail.attachment.length > 0">附件：<FilePreview :fileList="helpDocDetail.attachment" /></div>
  </a-card>

  <a-card title="阅读记录" size="small" class="smart-margin-top10" :bordered="false">
    <HelpDocViewRecordList ref="helpDocViewRecordListRef" :helpDocId="route.query.helpDocId" />
  </a-card>

  <!-- 预览附件 -->
  <FilePreview ref="filePreviewRef" />
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import HelpDocViewRecordList from './components/help-doc-view-record-list.vue';
import { helpDocApi } from '/@/api/support/help-doc-api';
import { SmartLoading } from '/@/components/framework/smart-loading';
import FilePreview from '/@/components/support/file-preview/index.vue';
import { smartSentry } from '/@/lib/smart-sentry';

const route = useRoute();

const activeKey = ref(1);

const helpDocDetail = ref({});

onMounted(() => {
  if (route.query.helpDocId) {
    queryHelpDocDetail();
  }
});

const helpDocViewRecordListRef = ref();

// 查询详情
async function queryHelpDocDetail() {
  try {
    SmartLoading.show();
    const result = await helpDocApi.view(route.query.helpDocId);
    helpDocDetail.value = result.data;

    helpDocViewRecordListRef.value.onSearch();
  } catch (err) {
    smartSentry.captureError(err);
  } finally {
    SmartLoading.hide();
  }
}

// 预览附件
const filePreviewRef = ref();
function onPrevFile(fileItem) {
  filePreviewRef.value.showPreview(fileItem);
}

// 打印
function print() {
  let bdhtml = window.document.body.innerHTML;
  let sprnstr = '<!--startprint-->'; //必须在页面添加<!--startprint-->和<!--endprint-->而且需要打印的内容必须在它们之间
  let eprnstr = '<!--endprint-->';
  let prnhtml = bdhtml.substr(bdhtml.indexOf(sprnstr));
  prnhtml = prnhtml.substring(0, prnhtml.indexOf(eprnstr));
  let newWin = window.open(''); //新打开一个空窗口
  newWin.document.body.innerHTML = prnhtml;
  newWin.document.close(); //在IE浏览器中使用必须添加这一句
  newWin.focus(); //在IE浏览器中使用必须添加这一句
  newWin.print(); //打印
  newWin.close(); //关闭窗口
}
</script>

<style lang="less" scoped>
:deep(.ant-descriptions-item-content) {
  flex: 1;
  overflow: hidden;
}
.file-list {
  width: 100%;
  display: flex;
  flex-wrap: wrap;
  .file-item {
    display: block;
    margin-right: 10px;
  }
}
.visible-list {
  display: flex;
  flex-wrap: wrap;
  .visible-item {
    margin-right: 10px;
    color: #666;
  }
}
.content-header {
  .content-header-title {
    margin: 10px 0px;
    font-size: 20px;
    font-weight: bold;
    text-align: center;
  }
  .content-header-info {
    margin: 10px 0px;
    font-size: 14px;
    color: #888;
    text-align: center;
    span {
      margin: 0 10px;
      cursor: pointer;
    }
  }
}
/*样式深入*/
:deep(.content-html) {
  margin-top: 30px;
  padding: 0 8px;
  line-height: 28px;
  font-size: 16px;
  border: #1e1e1e;

  img {
    max-width: 100%;
  }

  body {
    margin: 0 auto;
    color: #ccd1d8;
    line-height: 1.5;
    padding: 16px;
    background-color: #333842;
    font-size: 16px;
  }

  h1, h2, h3, h4, h5, h6 {
    color: #0D366F;
    font-weight: bold;
    margin-top: 20px;
    margin-bottom: 10px;
    padding: 0;
  }

  p {
    padding: 0;
    margin-bottom: 16px;
  }

  h1 {
    font-size: 26px;
  }

  h2 {
    font-size: 24px;
  }

  h3 {
    font-size: 22px;
  }

  h4 {
    font-size: 20px;
  }

  h5 {
    font-size: 19px;
  }

  h6 {
    font-size: 18px;
  }

  a {
    color: #61afef;
    margin: 0;
    padding: 0;
    vertical-align: baseline;
    text-decoration: none;
    word-break: break-word;
  }

  a:hover {
    text-decoration: underline;
  }

  a:visited {
    color: #ba68c8;
  }

  ul, ol {
    padding: 0;
    padding-left: 24px;
  }

  li {
    line-height: 24px;
  }

  li ul, li ol {
    margin-left: 16px;
  }

  p, ul, ol {
    font-size: 16px;
    line-height: 24px;
  }

  mark {
    color: #000000;
    background-color: #c4c400;
  }

  pre {
    display: block;
    overflow-y: hidden;
    overflow-x: auto;
    -moz-tab-size: 4;
    tab-size: 4;
  }

  code {
    color: #98c379;
    word-break: break-word;
  }

  pre code {
    display: block;
    padding-left: 0.5em;
    padding-right: 0.5em;
    color: #98c379;
    background-color: #2d323b;
    line-height: 1.5;
    white-space: pre;
    -moz-tab-size: 4;
    tab-size: 4;
  }

  aside {
    display: block;
    float: right;
    width: 390px;
  }

  blockquote {
    color: #abb2bf;
    border-left: .5em solid #abb2bf;
    padding: 0 1em;
    margin-left: 0;
  }

  blockquote p {
    color: #abb2bf;
  }

  hr {
    display: block;
    text-align: left;
    margin: 1em 0;
    border: none;
    height: 2px;
    background-color: #4c5562;
  }

  table {
    padding: 0;
    margin: 1rem 0.5rem;
    border-collapse: collapse;
  }

  table tr {
    border-top: 1px solid #4c5562;
    margin: 0;
    padding: 0;
  }

  table tr:hover {
    background-color: #DBE5F2;
  }

  table tr th {
    font-weight: bold;
    background-color: #90BFFF;
    border: 1px solid #4c5562;
    margin: 0;
    padding: 6px 13px;
  }

  table tr td {
    border: 1px solid #4c5562;
    margin: 0;
    padding: 6px 13px;
  }

  table tr th :first-child, table tr td :first-child {
    margin-top: 0;
  }

  table tr th :last-child, table tr td :last-child {
    margin-bottom: 0;
  }
}
</style>
