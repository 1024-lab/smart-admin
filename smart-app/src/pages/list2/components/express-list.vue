<template>
  <view class="item" v-for="(item, index) in 6">
    <view class="item-header">
      <view class="header-title"> 快递号：KD12382387 </view>
      <view class="header-right" :class="index == 1 ? 'pay' : 'nopay'">
        {{ index == 1 ? '已支付' : '未支付' }}
      </view>
    </view>
    <view class="address">
      <view class="start-address">
        <view class="atom start">
          <view> </view>
        </view>
        <view class="start-address-right">
          <view class="start-address-text"> 出发地：洛阳市六边形科技园 </view>
          <view class="start-time"> 出发时间：2023.08.23 </view>
        </view>
      </view>
      <view class="end-address">
        <view class="atom end">
          <view> </view>
        </view>
        <view class="start-address-text"> 目的地：上海市壹零贰肆软件园 </view>
      </view>
    </view>
    <view class="footer">
      <view class="footer-order-time"> 下单时间：2023.08.23 </view>
      <view class="footer-order-price"> 订单金额<text class="pay" :class="index == 1 ? 'pay' : 'nopay'">¥450.00</text> </view>
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
    margin: 30rpx auto 0;
    background: #ffffff;
    border-radius: 12rpx;
    box-shadow: 0px 3px 4px 0px rgba(24, 144, 255, 0.06);
    padding: 26rpx 30rpx 20rpx;
    box-sizing: border-box;
    .item-header {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      position: relative;
      margin-bottom: 26rpx;
      .header-title {
        width: 480rpx;
        font-size: 28rpx;
        font-weight: bold;
        color: #444;
      }
      .header-right {
        position: absolute;
        right: -40rpx;
        background-size: 138rpx 60rpx;
        width: 138rpx;
        height: 60rpx;
        font-size: 28rpx;
        color: #fff;
        line-height: 50rpx;
        text-indent: 24rpx;
        &.pay {
          background-image: url('/static/images/pure-list/blue.png');
        }
        &.nopay {
          background-image: url('/static/images/pure-list/orange.png');
        }
      }
    }
    .address {
      width: 100%;
      background: #f7f8f9;
      border-radius: 4px;
      padding: 20rpx 24rpx;
      box-sizing: border-box;
      margin-bottom: 30rpx;
      .atom {
        width: 30rpx;
        height: 30rpx;
        border-radius: 50%;
        display: flex;
        justify-content: center;
        align-items: center;
        margin-right: 10rpx;
        margin-top: 6rpx;
        view {
          width: 14rpx;
          height: 14rpx;
          border-radius: 50%;
        }
        &.start {
          background: rgba(26, 154, 255, 0.2);
          view {
            background: rgb(26, 154, 255);
          }
        }
        &.end {
          background: rgba(255, 108, 0, 0.2);
          view {
            background: rgb(255, 108, 0);
          }
        }
      }
      .end-address {
        display: flex;
        align-items: flex-start;
      }
      .start-address-text {
        font-size: 30rpx;
        font-weight: bold;
        color: #444444;
        margin-bottom: 4rpx;
      }
      .start-address {
        display: flex;
        align-items: flex-start;

        .start-time {
          font-size: 28rpx;
          color: #777777;
        }
        margin-bottom: 20rpx;
      }
    }
    .footer {
      display: flex;
      justify-content: space-between;
      .footer-order-time {
        font-size: 28rpx;
        color: #777;
      }
      .footer-order-price {
        color: #777;
        font-size: 28rpx;
        text {
          font-weight: bold;
          margin-left: 4rpx;
          &.pay {
            color: #323333;
          }
          &.nopay {
            color: #ff3924;
          }
        }
      }
    }
  }
</style>
