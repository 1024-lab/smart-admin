<template>
  <view class="item">
    <view class="header">
      <view class="header-left">
        <view class="header-left-title"> 手机优惠券 </view>
        <view class="header-left-time"> 有效期至2024.08.28 </view>
      </view>
      <view class="header-right-price"> ￥<text>10</text> </view>
    </view>
    <view class="footer">
      <view class="footer-left"> 适用于：双11商品 </view>
      <view v-if="index != 1" class="footer-right"> 去使用 </view>
    </view>
  </view>
  <view class="item">
    <view class="forbidden">
      <image src="/static/images/pure-list/employ.png" mode=""></image>
    </view>
    <view class="header">
      <view class="header-left">
        <view class="header-left-title"> 笔记本优惠券 </view>
        <view class="header-left-time"> 有效期至2024.08.28 </view>
      </view>
      <view class="header-right-price grayscale"> ￥<text>99</text> </view>
    </view>
    <view class="footer">
      <view class="footer-left"> 适用于：双11商品 </view>
    </view>
  </view>
  <view class="item">
    <view class="header">
      <view class="header-left">
        <view class="header-left-title"> 汽车优惠券 </view>
        <view class="header-left-time"> 有效期至2024.08.28 </view>
      </view>
      <view class="header-right-price"> ￥<text>9999</text> </view>
    </view>
    <view class="footer">
      <view class="footer-left"> 适用于：双11商品 </view>
      <view v-if="index != 1" class="footer-right"> 去使用 </view>
    </view>
  </view>
  <view class="item">
    <view class="forbidden">
      <image src="/static/images/pure-list/employ.png" mode=""></image>
    </view>
    <view class="header">
      <view class="header-left">
        <view class="header-left-title"> 笔记本优惠券 </view>
        <view class="header-left-time"> 有效期至2024.08.28 </view>
      </view>
      <view class="header-right-price grayscale"> ￥<text>99</text> </view>
    </view>
    <view class="footer">
      <view class="footer-left"> 适用于：双11商品 </view>
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
  .forbidden {
    position: absolute;
    width: 100%;
    height: 100%;
    background: rgba(255, 255, 255, 0.5);
    left: 0;
    top: 0;
    image {
      position: absolute;
      right: 0;
      bottom: 0;
      width: 184rpx;
      height: 160rpx;
    }
  }
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
      border-bottom: 1rpx dashed #ededed;
      margin-bottom: 16rpx;
      .header-left {
        .header-left-title {
          font-size: 34rpx;
          font-weight: 500;
          text-align: left;
          color: #444444;
          margin-bottom: 8rpx;
        }
        .header-left-time {
          font-size: 28rpx;
          color: #777;
        }
      }
      .header-right-price {
        color: #ff3924;
        font-size: 42rpx;
        font-weight: bold;
        text {
          font-size: 72rpx;
        }
      }
      .grayscale {
        color: #ccc;
      }
    }

    .footer {
      height: 64rpx;
      display: flex;
      justify-content: space-between;
      align-items: center;
      .footer-left {
        font-size: 28rpx;
        color: #777777;
      }
      .footer-right {
        width: 144rpx;
        height: 64rpx;
        background: linear-gradient(112deg, #f7681d 9%, #f64226 84%);
        border-radius: 8rpx;
        box-shadow: 0px 5px 8px 0px rgba(236, 79, 40, 0.2);
        color: #fff;
        font-size: 28rpx;
        text-align: center;
        line-height: 64rpx;
      }
    }
  }
</style>
