<template>
  <view class="list-container">
    <view class="list-item" @click="gotoDetail(item.noticeId)" v-for="item in list" :key="item.noticeId">
      <view class="title"> {{ item.noticeTypeName }} ： {{ item.title }} </view>
      <view class="author" v-if="item.author"> 作者：{{ item.author }} </view>
      <view class="author" v-if="item.source"> 来源：{{ item.source }} </view>
      <view class="publish-time">
        <view class="time">发布时间：{{ item.publishTime }}</view>
        <view class="un-read" v-if="!item.viewFlag">未读</view>
        <view class="read" v-if="item.viewFlag">已读</view>
      </view>
    </view>
  </view>
</template>

<script setup>
  const props = defineProps({
    marginTop: {
      type: String,
      default: '0',
    },
    list: {
      type: Array,
      default: [],
    },
  });

  function gotoDetail(noticeId) {
    uni.navigateTo({ url: '/pages/notice/notice-detail?noticeId=' + noticeId });
  }
</script>

<style lang="scss" scoped>
  .list-container {
    margin-top: v-bind(marginTop);
    padding: 20rpx 10rpx;
  }
  .list-item {
    box-sizing: border-box;
    margin: 0 30rpx;
    padding: 30rpx 0;
    border-bottom: #cdcdcd 1px solid;

    .title {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20rpx;
      font-size: 16px;
      text-overflow: ellipsis;
      white-space: nowrap;
      overflow: hidden;
    }
    .author {
      margin-bottom: 14rpx;
      color: #999999;
      font-size: 12px;
      text-overflow: ellipsis;
    }
    .publish-time {
      font-size: 12px;
      color: #999999;
      display: flex;
      flex-direction: row;
      justify-content: space-between;

      .un-read {
        color: $uni-color-primary;
      }
    }
  }
</style>
