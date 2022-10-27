<!--
  * 通知  详情 （员工）
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-08-21 19:52:43 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
-->
<template>
  <a-card size="small">
    <div>
      <div class="content-header">
        <!--startprint-->
        <div class="content-header-title">
          {{ noticeDetail.title }}
        </div>
        <div class="content-header-info">
          <span v-show="noticeDetail.author">作者：{{ noticeDetail.author }}</span>
          <span v-show="noticeDetail.source">来源：{{ noticeDetail.source }}</span>
          <span>发布时间：{{ noticeDetail.publishTime }}</span>
          <span>阅读量：{{ noticeDetail.pageViewCount }}</span>
          <span @click="print">【打印本页】</span>
        </div>
      </div>
      <div class="content-html" v-html="noticeDetail.contentHtml"></div>
      <!--endprint-->
    </div>
    <a-divider />
    <div>附件：<file-preview :fileList="noticeDetail.attachment" /></div>
  </a-card>

  <a-card title="记录" size="small" class="smart-margin-top10">
    <NoticeViewRecordList ref="noticeViewRecordList" :noticeId="route.query.noticeId" />
  </a-card>

  <!-- 预览附件 -->
  <FilePreviewModal ref="filePreviewRef" />
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import NoticeViewRecordList from './components/notice-view-record-list.vue';
import { noticeApi } from '/@/api/business/oa/notice-api';
import { SmartLoading } from '/@/components/framework/smart-loading';
import FilePreviewModal from '/@/components/support/file-preview-modal/index.vue';
import FilePreview from '/@/components/support/file-preview/index.vue';
import { smartSentry } from '/@/lib/smart-sentry';

const route = useRoute();

const activeKey = ref(1);

const noticeDetail = ref({});

onMounted(() => {
  if (route.query.noticeId) {
    queryNoticeDetail();
  }
});

const noticeViewRecordList = ref();

// 查询详情
async function queryNoticeDetail() {
  try {
    SmartLoading.show();
    const result = await noticeApi.view(route.query.noticeId);
    noticeDetail.value = result.data;

    noticeViewRecordList.value.onSearch();
  } catch (err) {
    smartSentry.captureError(err);
  } finally {
    SmartLoading.hide();
  }
}

// 点击编辑
const noticeFormDrawerRef = ref();
function onEdit() {
  noticeFormDrawerRef.value.showModal(noticeDetail.value.noticeId);
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
    font-size: 18px;
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
.content-html {
  img {
    max-width: 100%;
  }
}
</style>
