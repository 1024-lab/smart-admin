<template>
  <view class="list-view">
    <view class="item">
      <view class="item-top">
        <view class="item-top-item">
          <view class="item-top-label"> 创建时间： </view>
          <view class="item-title"> 2023-08-27 21:33 </view>
          <view class="item-state">
            <view class="tag" type="warring"> 未付款 </view>
          </view>
        </view>
        <view class="item-top-item">
          <view class="item-top-label"> 客户名称： </view>
          <view class="item-title"> 六边形工程师 </view>
        </view>
        <view class="item-top-item">
          <view class="item-top-label"> 供应商名称： </view>
          <view class="item-title"> 1024数字科技有限公司 </view>
        </view>
        <view class="item-top-item">
          <view class="item-top-label"> 客户经理： </view>
          <view class="item-title"> 卓大 </view>
        </view>
        <view class="item-top-item">
          <view class="item-top-label"> 生产厂商： </view>
          <view class="item-title"> 1024工厂 </view>
        </view>
      </view>

      <view class="item-footer">
        <view class="item-footer-left"> 付款金额： </view>
        <view class="item-footer-right"> ￥300000.00 </view>
      </view>
    </view>

    <view class="item">
      <view class="item-top">
        <view class="item-top-item">
          <view class="item-top-label"> 创建时间： </view>
          <view class="item-title"> 2023-08-27 21:33 </view>
          <view class="item-state">
            <view class="tag" type="success"> 已付款 </view>
          </view>
        </view>
        <view class="item-top-item">
          <view class="item-top-label"> 客户名称： </view>
          <view class="item-title"> 六边形工程师 </view>
        </view>
        <view class="item-top-item">
          <view class="item-top-label"> 供应商名称： </view>
          <view class="item-title"> 1024数字科技有限公司 </view>
        </view>
        <view class="item-top-item">
          <view class="item-top-label"> 客户经理： </view>
          <view class="item-title"> 卓大 </view>
        </view>
        <view class="item-top-item">
          <view class="item-top-label"> 生产厂商： </view>
          <view class="item-title"> 1024工厂 </view>
        </view>
      </view>

      <view class="item-footer">
        <view class="item-footer-left"> 付款金额： </view>
        <view class="item-footer-right"> ￥300000.00 </view>
      </view>
    </view>

    <view class="item">
      <view class="item-top">
        <view class="item-top-item">
          <view class="item-top-label"> 创建时间： </view>
          <view class="item-title"> 2024-01-11 21:33 </view>
          <view class="item-state">
            <view class="tag" type="warring"> 未付款 </view>
          </view>
        </view>
        <view class="item-top-item">
          <view class="item-top-label"> 客户名称： </view>
          <view class="item-title"> 孙传芳 </view>
        </view>
        <view class="item-top-item">
          <view class="item-top-label"> 供应商名称： </view>
          <view class="item-title"> 1024数字科技有限公司 </view>
        </view>
        <view class="item-top-item">
          <view class="item-top-label"> 客户经理： </view>
          <view class="item-title"> 胡克 </view>
        </view>
        <view class="item-top-item">
          <view class="item-top-label"> 生产厂商： </view>
          <view class="item-title"> 数字工厂 </view>
        </view>
      </view>

      <view class="item-footer">
        <view class="item-footer-left"> 付款金额： </view>
        <view class="item-footer-right"> ￥300000.00 </view>
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
  .decollator {
    height: 30rpx;
    position: relative;
    display: flex;
    align-items: center;
    .decollator-left {
      top: 2rpx;
      position: absolute;
      width: 60rpx;
      height: 60rpx;
      border-radius: 50%;
      background: #f5f5f5;
      z-index: 10;
      left: -30rpx;
    }
    .decollator-right {
      top: 2rpx;
      width: 60rpx;
      height: 60rpx;
      border-radius: 50%;
      background: #f5f5f5;
      position: absolute;
      z-index: 10;
      right: -30rpx;
    }
    .decollator-center {
      width: 100%;
      position: absolute;
      bottom: 0;
      z-index: 1;
      border-top: 1rpx dashed #ededed;
    }
  }

  .list-view {
    margin: 20rpx 24rpx;
  }
  .item {
    box-sizing: border-box;
    width: 700rpx;
    background: #ffffff;
    border-radius: 12rpx;
    box-shadow: 0px 3px 4px 0px rgba(24, 144, 255, 0.06);
    padding-top: 24rpx;
    margin-bottom: 30rpx;
    overflow: hidden;

    .item-top {
      padding: 0 30rpx;
      .item-top-item {
        display: flex;
        align-items: center;
        margin-bottom: 16rpx;
      }
      margin-bottom: 20rpx;
      .item-top-label {
        width: 168rpx;
        margin-right: 10rpx;
        font-size: 28rpx;
        color: #777777;
      }
      .item-title {
        font-size: 28rpx;
        color: #323333;
        font-weight: bold;
      }
      .item-state {
        flex: 1;
        display: flex;
        justify-content: flex-end;
        .tag {
          width: 112rpx;
          height: 40rpx;
          border-radius: 4rpx;
          text-align: center;
          line-height: 40rpx;
          font-size: 24rpx;
          &[type='warring'] {
            background-color: $error-color;
            color: white;
          }
          &[type='success'] {
            background-color: $uni-color-success;
            color: white;
          }
        }
      }
    }

    .item-sub-title {
      font-size: 28rpx;
      color: #777777;
      margin-bottom: 10rpx;
      &:last-child {
        padding-bottom: 20rpx;
        margin-bottom: 0;
      }
    }

    .order-end-time {
      height: 40rpx;
      background: #ff6c00;
      box-shadow: 0px 3px 4px 0px rgba(24, 144, 255, 0.06);
      color: #fff;
      text-align: center;
      font-size: 24rpx;
      line-height: 40rpx;
    }
  }

  .line-border-bottom {
    border-bottom: 1rpx solid #ededed;
  }

  .item-footer {
    height: 120rpx;
    display: flex;
    border-top: 1rpx dashed #ccc;
    //background-color: #F7FAFD;
    align-items: center;
    justify-content: space-between;
    margin: 0 30rpx;
    .item-footer-left {
      font-size: 28rpx;
      color: #777;
    }
    .item-footer-right {
      font-size: 34rpx;
      color: red;
      font-weight: bold;
    }
  }
</style>
