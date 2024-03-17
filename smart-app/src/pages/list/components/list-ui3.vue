<template>
  <view class="list-view">
    <view class="item">
      <view class="item-header">
        <view class="item-top">
          <view class="item-top-left"> 2023-08-20 19:15 </view>
          <view class="item-top-right">
            <view class="tag" type="warring"> 未付款 </view>
          </view>
        </view>
        <view class="line-border-bottom"> </view>
        <view class="item-center">
          <view class="item-center-left">
            <view class="item-top-z"> 装 </view>
            <view class="item-center"> </view>
            <view class="item-bottom-x"> 卸 </view>
          </view>
          <view class="item-center-address">
            <view class="m-b-16">
              <view class="item-title"> 装货地址 </view>
              <view class="item-address"> 河南省洛阳市洛龙区110号 </view>
            </view>
            <view class="m-b-16">
              <view class="item-title"> 卸货地址 </view>
              <view class="item-address"> 河南省洛阳市西工区120号 </view>
            </view>
          </view>
        </view>

        <view class="line-border-bottom"> </view>
        <view class="item-footer">
          <view class="item-footer-left">
            <text>￥586.00</text>
          </view>
          <view class="item-footer-right">
            <view class="camera">
              <image src="/static/images/list/camera.png" mode=""></image>
              拍照
            </view>
            <view class="ok">
              <image src="/static/images/list/success.png" mode=""></image>
              完成
            </view>
          </view>
        </view>
      </view>
    </view>

    <view class="item">
      <view class="item-header">
        <view class="item-top">
          <view class="item-top-left"> 2023-08-20 19:15 </view>
          <view class="item-top-right">
            <view class="tag" type="warring"> 未付款 </view>
          </view>
        </view>
        <view class="line-border-bottom"> </view>
        <view class="item-center">
          <view class="item-center-left">
            <view class="item-top-z"> 装 </view>
            <view class="item-center"> </view>
            <view class="item-bottom-x"> 卸 </view>
          </view>
          <view class="item-center-address">
            <view class="m-b-16">
              <view class="item-title"> 装货地址 </view>
              <view class="item-address"> 河南省洛阳市洛龙区110号 </view>
            </view>
            <view class="m-b-16">
              <view class="item-title"> 卸货地址 </view>
              <view class="item-address"> 河南省洛阳市西工区120号 </view>
            </view>
          </view>
        </view>

        <view class="line-border-bottom"> </view>
        <view class="item-footer">
          <view class="item-footer-left">
            <text>￥586.00</text>
          </view>
          <view class="item-footer-right">
            <view class="camera">
              <image src="/static/images/list/camera.png" mode=""></image>
              拍照
            </view>
            <view class="ok">
              <image src="/static/images/list/success.png" mode=""></image>
              完成
            </view>
          </view>
        </view>
      </view>
    </view>

    <view class="item">
      <view class="item-header">
        <view class="item-top">
          <view class="item-top-left"> 2023-08-20 19:15 </view>
          <view class="item-top-right">
            <view class="tag" type="warring"> 未付款 </view>
          </view>
        </view>
        <view class="line-border-bottom"> </view>
        <view class="item-center">
          <view class="item-center-left">
            <view class="item-top-z"> 装 </view>
            <view class="item-center"> </view>
            <view class="item-bottom-x"> 卸 </view>
          </view>
          <view class="item-center-address">
            <view class="m-b-16">
              <view class="item-title"> 装货地址 </view>
              <view class="item-address"> 河南省洛阳市洛龙区110号 </view>
            </view>
            <view class="m-b-16">
              <view class="item-title"> 卸货地址 </view>
              <view class="item-address"> 河南省洛阳市西工区120号 </view>
            </view>
          </view>
        </view>

        <view class="line-border-bottom"> </view>
        <view class="item-footer">
          <view class="item-footer-left">
            <text>￥586.00</text>
          </view>
          <view class="item-footer-right">
            <view class="camera">
              <image src="/static/images/list/camera.png" mode=""></image>
              拍照
            </view>
            <view class="ok">
              <image src="/static/images/list/success.png" mode=""></image>
              完成
            </view>
          </view>
        </view>
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
  .list-view {
    margin: 20rpx 24rpx;
  }

  .item {
    .item-header {
      padding: 0 30rpx;
    }

    box-sizing: border-box;
    width: 700rpx;
    background: #ffffff;
    border-radius: 12rpx;
    box-shadow: 0px 3px 4px 0px rgba(24, 144, 255, 0.06);
    padding-top: 24rpx;
    margin-bottom: 30rpx;
    overflow: hidden;

    .item-top {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20rpx;

      .item-top-left {
        font-size: 28rpx;
        font-weight: 400;
        color: #777777;
      }

      .item-top-right {
        .tag {
          width: 112rpx;
          height: 40rpx;
          border-radius: 4rpx;
          text-align: center;
          line-height: 40rpx;
          font-size: 24rpx;

          &[type='warring'] {
            color: #ff6c00;
            background: #fff0ed;
          }
          &[type='success'] {
            color: #1a9aff;
            background: #f2f9ff;
          }
        }
      }
    }

    .item-center {
      margin-top: 20rpx;
      display: flex;

      .item-center-left {
        margin-top: 5rpx;
        margin-right: 12rpx;
        display: flex;
        flex-direction: column;
        align-items: center;

        .item-top-z {
          position: relative;
          z-index: 10;
          width: 34rpx;
          height: 34rpx;
          background: #1a9aff;
          border-radius: 12rpx;
          text-align: center;
          font-size: 24rpx;
          color: #fff;
        }

        .item-center {
          height: 75rpx;
          border-left: 1rpx dashed #777777;
          margin-top: -5rpx;
          border-spacing: 6rpx;
          border-width: 2rpx;
        }

        .item-bottom-x {
          width: 34rpx;
          height: 34rpx;
          background: #ff6c00;
          border-radius: 12rpx;
          text-align: center;
          font-size: 24rpx;
          color: #fff;
        }
      }

      .item-center-address {
        .m-b-16 {
          margin-bottom: 16rpx;
        }
        .item-title {
          font-size: 34rpx;
          color: #444444;
          font-weight: bold;
          margin-bottom: 4rpx;
        }

        .item-address {
          font-size: 28rpx;
          color: #777777;
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

  .item-footer {
    padding: 20rpx 0;
    display: flex;
    justify-content: space-between;
    align-items: center;

    .item-footer-left {
      font-size: 34rpx;
      color: #777777;

      text {
        color: #ff3924;
        font-weight: bold;
      }
    }

    .item-footer-right {
      display: flex;
      align-items: center;

      view {
        width: 160rpx;
        height: 64rpx;
        border-radius: 8rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        color: #fff;

        image {
          width: 40rpx;
          height: 40rpx;
        }

        &.ok {
          background: #1a9aff;
          margin-left: 24rpx;
        }

        &.camera {
          background: linear-gradient(107deg, #57d697 9%, #40c87e 86%);
        }
      }
    }
  }

  .line-border-bottom {
    border-bottom: 1rpx solid #ededed;
  }
</style>
