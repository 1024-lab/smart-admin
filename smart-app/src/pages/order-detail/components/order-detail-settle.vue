<template>
  <view class="smart-detail-card">
    <view class="smart-detail-card-title"> 结算信息 </view>
    <view class="view-item">
      <view class="item-left"> 订单号： </view>
      <view class="item-right"> TYD2023080979 </view>
    </view>
    <view class="view-item">
      <view class="item-left"> 付款状态： </view>
      <view class="item-right">
        <view class="success"> 已审核 </view>
      </view>
    </view>
    <view class="view-item">
      <view class="item-left"> 应付金额： </view>
      <view class="item-right"> 500 </view>
    </view>
    <view class="view-item">
      <view class="item-left"> 已付金额： </view>
      <view class="item-right"> 200 </view>
    </view>
    <view class="view-item">
      <view class="item-left"> 收款账户： </view>
      <view class="item-right"> 项羽/61248348910384 </view>
    </view>
  </view>
</template>

<script setup></script>

<style lang="scss" scoped>
  .view {
    width: 94%;
    background: #fff;
    border-radius: 16rpx;
    box-sizing: border-box;
    padding: 0 30rpx 24rpx;
    margin: 0 auto 24rpx;
  }

  .view-title {
    height: 84rpx;
    display: flex;
    align-items: center;
    margin-bottom: 16rpx;
    .title-left-bg {
      height: 28rpx;
      width: 6rpx;
      background: #1a9aff;
      border-radius: 4rpx;
      margin-right: 14rpx;
    }
    .title-text {
      font-size: 32rpx;
      color: #323333;
      font-weight: bold;
    }
    .sub-title {
      flex: 1;
      display: flex;
      justify-content: flex-end;
      view {
        height: 42rpx;
        width: 112rpx;
        text-align: center;
        line-height: 42rpx;
        font-size: 24rpx;
        margin-left: 24rpx;
      }
      .success {
        background: #e5f8e9;
        color: #1ce36d;
      }
      .warning {
        background: #fff0ed;
        color: #ff6c00;
      }
    }
  }

  .view-item {
    display: flex;
    height: 40rpx;
    align-items: center;
    margin-bottom: 16rpx;
    &:last-child {
      margin-bottom: 0;
    }
    .item-left {
      width: 180rpx;
      margin-right: 20rpx;
      font-size: 28rpx;
      color: #777777;
    }
    .item-right {
      font-size: 30rpx;
      color: #323333;
      font-weight: bold;
      font-size: 28rpx;
    }
  }
</style>
