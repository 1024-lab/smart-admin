<template>
  <view>
    <view class="select-all">
      <view class="circle" v-if="false"> </view>
      <image v-else src="/src/static/images/select-people/select.png" mode=""></image>
      <view class=""> 全选 </view>
    </view>
    <view class="item" v-for="(item, index) in 5">
      <view class="item-btn">
        <view class="circle" v-if="false"> </view>
        <image v-else src="/src/static/images/select-people/select.png" mode=""></image>
      </view>
      <view class="item-content">
        <view class="item-name"> 卓大 </view>
        <view class="item-desc"> 架构师 </view>
      </view>
    </view>
    <view class="h-168"> </view>
    <view class="footer">
      <view class="footer-content">
        <view class="select"> 已选择(2) </view>
        <view class="select-item-card">
          <view class="card-item" v-for="(item, index) in 3" :key="index"> 卓大{{ index == 2 ? '...' : '' }} </view>
        </view>
        <view class="submit">
          <view class="btn"> 确认 (5/12) </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
  export default {
    data() {
      return {};
    },
    methods: {},
  };
</script>

<style lang="scss" scoped>
  page {
    background: #f5f6f8;
  }
  .select-all {
    height: 104rpx;
    width: 100%;
    background: #fff;
    display: flex;
    align-items: center;
    padding-left: 55rpx;
    font-size: 32rpx;
    color: #000000;
  }

  .circle {
    width: 44rpx;
    height: 44rpx;
    border-radius: 50%;
    border: 1rpx solid #ccc;
    margin-right: 20rpx;
  }
  image {
    width: 44rpx;
    height: 44rpx;
    margin-right: 20rpx;
  }

  .item {
    box-sizing: border-box;
    width: 700rpx;
    height: 140rpx;
    margin: 16rpx auto 0;
    padding: 30rpx 24rpx;
    display: flex;
    background-color: #fff;
    border-radius: 12rpx;
    .item-name {
      font-size: 32rpx;
      font-weight: 600;
      color: #444444;
      margin-bottom: 8rpx;
    }
    .item-desc {
      font-size: 28rpx;
      color: #777777;
    }
  }
  .h-168 {
    height: 168rpx;
  }
  .footer {
    position: fixed;
    bottom: 0;
    width: 100%;
    height: 168rpx;
    background-color: #fff;
    padding: 16rpx 24rpx;
    box-sizing: border-box;
    .footer-content {
      height: 80rpx;
      width: 100%;
      display: flex;
      align-items: center;
      .select {
        font-size: 28rpx;
        color: #444444;
        margin-right: 32rpx;
      }
      .select-item-card {
        display: flex;
        view {
          height: 44rpx;
          padding: 0 12rpx;
          line-height: 44rpx;
          font-size: 22rpx;
          color: #1a9aff;
          border: 1px solid #2291f9;
          border-radius: 8rpx;
          margin-left: -12rpx;
          position: relative;
          background-color: #fff;
        }
      }
      .submit {
        flex: 1;
        display: flex;
        justify-content: flex-end;
        view {
          height: 80rpx;
          width: 232rpx;
          background: #1a9aff;
          border-radius: 8rpx;
          font-size: 30rpx;
          line-height: 80rpx;
          text-align: center;
          color: #fff;
        }
      }
    }
  }
</style>
