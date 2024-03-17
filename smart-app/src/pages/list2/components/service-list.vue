<template>
  <view class="item" v-for="(item, index) in 6" :key="index">
    <view class="header">
      <view class="header-left">
        <image class="header-left-image" src="/static/images/pure-list/maintain.png" mode=""></image>
        <view class="header-left-title"> 反馈信息 </view>
      </view>
      <view class="header-right-id"> 保修单还：12876 </view>
    </view>
    <view class="footer">
      <view class="footer-item">
        <view class="label"> 商品名称：</view>
        <view class="info"> 家用净水器 </view>
      </view>
      <view class="footer-item">
        <view class="label"> 描述情况：</view>
        <view class="info"> 水有杂志且存在异味 </view>
      </view>
      <view class="footer-item">
        <view class="label">提交人：</view>
        <view class="info"> 刘邦</view>
      </view>
      <view class="footer-item">
        <view class="label"> 受理人员： </view>
        <view class="info"> 迪丽热巴 </view>
      </view>
      <view class="footer-item">
        <view class="label"> 提交时间： </view>
        <view class="info"> 2023-01-01 12:12:12 </view>
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
    position: relative;
    width: 700rpx;
    margin: 20rpx auto 0;
    background: #ffffff;
    border-radius: 12rpx;
    box-shadow: 0px 3px 4px 0px rgba(24, 144, 255, 0.06);
    padding: 26rpx 40rpx 20rpx;
    box-sizing: border-box;
    .header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding-bottom: 20rpx;
      border-bottom: 1rpx solid #ededed;
      margin-bottom: 16rpx;

      .header-left {
        display: flex;
        align-items: center;
        .header-left-image {
          width: 48rpx;
          height: 48rpx;
          margin-right: 16rpx;
        }
        .header-left-title {
          font-size: 34rpx;
          font-weight: bold;
          color: #444444;
        }
      }
      .header-right-id {
        font-size: 28rpx;
        color: #777777;
      }
    }

    .footer {
      .footer-item {
        display: flex;
        align-items: center;
        margin-bottom: 16rpx;
        .label {
          width: 80px;
          font-size: 28rpx;
          color: #777777;
        }
        .info {
          font-size: 28rpx;
          color: #323333;
          font-weight: bold;
        }
      }
    }
  }
</style>
