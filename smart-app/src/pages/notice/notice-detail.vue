<template>
  <view class="container">
    <view class="title">
      <uni-title type="h1" align="center" :title="noticeDetail.title"></uni-title>
      <uni-title
        type="h4"
        align="center"
        color="#999999"
        v-if="noticeDetail.documentNumber"
        :title="'(' + noticeDetail.documentNumber + ')'"
      ></uni-title>
      <uni-title type="h4" align="center" color="#999999" :title="noticeDetail.subTitle"></uni-title>
    </view>
    <view class="content">
      <rich-text :nodes="noticeDetail.content" />
    </view>
  </view>
</template>

<script setup>
  import { reactive } from 'vue';
  import { noticeApi } from '@/api/business/oa/notice-api';
  import { onLoad } from '@dcloudio/uni-app';
  import { smartSentry } from '@/lib/smart-sentry';

  const noticeDetail = reactive({
    title: '',
    subTitle: '',
    documentNumber: '',
    content: '',
  });

  async function getNoticeDetail(noticeId) {
    try {
      uni.showLoading({ title: '加载中' });
      let res = await noticeApi.view(noticeId);
      noticeDetail.title = res.data.title;
      noticeDetail.content = res.data.contentHtml;
      noticeDetail.documentNumber = res.data.documentNumber;
      let subTitleArray = [];
      if (res.data.author) {
        subTitleArray.push(res.data.author);
      }
      if (res.data.publishTime) {
        subTitleArray.push(res.data.publishTime);
      }
      noticeDetail.subTitle = subTitleArray.join(' | ');
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      uni.hideLoading();
    }
  }

  onLoad((option) => {
    uni.pageScrollTo({
      scrollTop: 0,
    });
    getNoticeDetail(option.noticeId);
  });
</script>

<style lang="scss" scoped>
  .container {
    height: 100vh;
    padding: 20rpx;

    .content {
      border-top: #cccccc 1px solid;
      margin-top: 50rpx;
      padding: 50rpx 16rpx 50rpx 16rpx;
      line-height: 30px;
      color: #333333;
    }
  }
</style>
