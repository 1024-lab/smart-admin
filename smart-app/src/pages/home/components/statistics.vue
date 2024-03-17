<template>
  <view class="statistics-view">
    <view class="view-top">
      <view class="view-top-item-left">
        <view class="item-left-top"> 本月销售额（元） </view>
        <view class="item-left-bottom"> ¥32,226.00 </view>
      </view>
      <view class="view-top-item-right">
        <view class="item-right-top"> 本月销售目标 </view>
        <view class="item-right-bottom"> ¥50,000.00 </view>
      </view>
    </view>
    <view class="view-bottom">
      <view class="view-bottom-item">
        <view class="item-bottom-top"> 本月完成率 </view>
        <view class="item-bottom-bottom"> 85% </view>
      </view>
      <view class="view-item">
        <view class="item-bottom-top"> 本月新增客户 </view>
        <view class="item-bottom-bottom">+1024 </view>
      </view>
      <view class="view-item">
        <view class="item-bottom-top"> 本月累计通话 </view>
        <view class="item-bottom-bottom"> 16:00小时 </view>
      </view>
    </view>
  </view>
</template>

<script></script>

<style lang="scss" scoped>
  .statistics-view {
    width: 700rpx;
    height: 300rpx;
    margin: 10rpx auto 20rpx;
    background: linear-gradient(133deg, #045ff0 0%, #2291f9 55%, #2168ec 100%);
    border-radius: 6px;
    .view-top {
      display: flex;
      padding: 36rpx 44rpx;
      .view-top-item-left {
        margin-right: 100rpx;
        .item-left-top {
          opacity: 0.6;
          font-size: 26rpx;
          color: #ffffff;
          margin-bottom: 5rpx;
        }
        .item-left-bottom {
          font-size: 52rpx;
          font-weight: 700;
          color: #ffffff;
        }
      }
      .view-top-item-right {
        .item-right-top {
          opacity: 0.6;
          font-size: 26rpx;
          color: #ffffff;
          margin-bottom: 20rpx;
        }
        .item-right-bottom {
          font-size: 30rpx;
          font-weight: 700;
          color: #ffffff;
        }
      }
    }
    .view-bottom {
      padding: 26rpx 44rpx;
      display: flex;
      justify-content: space-between;
      .item-bottom-top {
        opacity: 0.6;
        font-size: 24rpx;
        font-weight: 400;
        color: #ffffff;
      }
      .item-bottom-bottom {
        font-size: 30rpx;
        font-weight: 500;
        color: #ffffff;
      }
    }
  }
</style>
