<template>
  <view class="item" v-for="item in 10">
    <view class="item-header">
      <view class="header-title"> 商业环境快速变化带来的挑战 </view>
      <view class="header-right"> 共15讲> </view>
    </view>
    <view class="item-tags">
      <view class="primary"> 房地产 </view>
      <view class="success"> 国债 </view>
      <view class="warning"> 贸易 </view>
    </view>
    <view class="content">
      <view class="content-box">
        <view class="c-top"> 授课老师 </view>
        <view class="c-bottom"> 秦始皇 </view>
      </view>
      <view class="content-box">
        <view class="c-top"> 开课时间 </view>
        <view class="c-bottom"> 2023.08.20 20:00 </view>
      </view>
    </view>
  </view>
</template>

<script setup>
  defineProps({
    list: {
      type: Array,
      default: [],
    },
  });
</script>

<style lang="scss" scoped>
  .item {
    width: 700rpx;
    margin: 20rpx auto 0;
    background: #ffffff;
    border-radius: 12rpx;
    box-shadow: 0px 3px 4px 0px rgba(24, 144, 255, 0.06);
    padding: 26rpx 30rpx 20rpx;
    box-sizing: border-box;
    .item-header {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      .header-title {
        width: 480rpx;
        font-size: 34rpx;
        color: #444;
        font-weight: 600;
      }
      .header-right {
        margin-top: 4rpx;
        font-size: 28rpx;
        color: #777777;
      }
    }
    .item-tags {
      display: flex;
      margin: 10rpx 0;
      view {
        width: 112rpx;
        height: 42rpx;
        border-radius: 4rpx;
        text-align: center;
        line-height: 42rpx;
        font-size: 24rpx;
        margin-right: 24rpx;
        &.primary {
          background: #eef7fd;
          color: #1a9aff;
        }
        &.success {
          color: #1ce36d;
          background: #e5f8e9;
        }
        &.warning {
          background: #fff0ed;
          color: #ff6c00;
        }
      }
    }
    .content {
      width: 100%;
      height: 140rpx;
      background: #f7f8f9;
      border-radius: 4px;
      display: flex;
      justify-content: space-around;
      align-items: center;
      .content-box {
        display: flex;
        flex-direction: column;
        align-items: center;
      }
      .c-top {
        font-size: 24rpx;
        color: #777777;
        margin-bottom: 4rpx;
      }
      .c-bottom {
        font-size: 30rpx;
        color: #444444;
      }
    }
  }
</style>
