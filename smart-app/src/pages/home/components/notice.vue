<template>
  <view class="container">
    <uni-card title="通知公告" :isFull="true" type="line" padding="0px" spacing="0px">
      <template #extra> <view @click="onMore">查看更多</view> </template>
      <uni-list>
        <uni-list-item :ellipsis="1" v-for="item in data" :rightText="item.publishDate" clickable @click="goto(item.noticeId)" :title="item.title" />
      </uni-list>
    </uni-card>
  </view>
</template>

<script setup>
  import { ref } from 'vue';
  import { noticeApi } from '@/api/business/oa/notice-api';
  import { smartSentry } from '@/lib/smart-sentry';
  import { onShow } from '@dcloudio/uni-app';

  const queryForm = {
    pageNum: 1,
    pageSize: 5,
    searchCount: false,
  };

  let data = ref([]);

  const loading = ref(false);
  // 查询列表
  async function queryNoticeList() {
    try {
      loading.value = true;
      const result = await noticeApi.queryEmployeeNotice(queryForm);
      data.value = result.data.list;
    } catch (err) {
      smartSentry.captureError(err);
    } finally {
      loading.value = false;
    }
  }

  // 跳转详情
  function goto(noticeId) {
    uni.navigateTo({ url: '/pages/notice/notice-detail?noticeId=' + noticeId });
  }

  onShow(() => {
    queryNoticeList();
  });

  // 查看更多
  function onMore() {
    uni.navigateTo({
      url: '/pages/notice/notice-index',
    });
  }
</script>

<style lang="scss" scoped>
  .container {
    width: 700rpx;
    margin: 0 auto 20rpx;
    border-radius: 12rpx;
    padding: 0;
    overflow: hidden;

    :deep(.uni-card__header-box) {
      font-weight: bold;
    }
    :deep(.uni-list--border-bottom) {
      background-color: transparent;
    }
    :deep(.uni-card__content) {
      padding: 0 !important;
    }
    :deep(.uni-card__header) {
      border: none;
    }
    :deep(.uni-card .uni-card__header .uni-card__header-content .uni-card__header-content-title) {
      font-size: 32rpx;
    }
    :deep(.uni-list-item__container) {
      padding: 16rpx 20rpx;
    }
    :deep(.uni-card__header) {
      background: linear-gradient(180deg, #e8f4ff, #f8fcff);
    }
    :deep(.uni-card__header-extra) {
      font-size: 30rpx;
      font-weight: 400;
      text-align: center;
      color: #1a9aff;
    }
    :deep(.uni-list-item__content-title) {
      font-size: 30rpx;
    }
  }
</style>
