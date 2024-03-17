<template>
  <view class="smart-detail-card">
    <view class="smart-detail-card-title"> 物流信息</view>
    <view class="view-item">
      <view class="title">
        <view class="title-icon-text" type="default"> 提 </view>
        <view class="title-content"> 河南省洛阳市洛龙区龙门石窟</view>
      </view>
      <view class="sub-title"> 河南省洛阳市洛龙区开元大道1024号1024大厦10层1024室 </view>
      <view class="sub-title"> 2023-11-23 12:12:12，快递员：刘备（188123123123）</view>
    </view>
    <view class="view-item">
      <view class="title">
        <view class="title-icon-text" type="warning"> 装 </view>
        <view class="title-content"> 山东省济南市高新区齐鲁软件园 </view>
      </view>
      <view class="sub-title"> 山东省济南市高新区六边形大楼10层</view>
      <view class="sub-title"> 快递员：卓大 188123123123</view>
    </view>
    <view class="view-item">
      <view class="title">
        <view class="title-icon-text" type="error"> 卸 </view>
        <view class="title-content"> 河南省郑州市郑东新区龙子湖大厦 </view>
      </view>
      <view class="sub-title"> 河南省郑州市郑东新区高铁路120号 </view>
    </view>
    <view class="view-item">
      <view class="title">
        <view class="title-icon-text" type="success"> 还 </view>
        <view class="title-content"> 河南省洛阳市洛龙区白马寺 （胡克） </view>
      </view>
      <view class="sub-title"> 河南省洛阳市洛龙区119号 </view>
    </view>
  </view>
</template>

<script setup></script>

<style lang="scss" scoped>
  .line {
    position: absolute;
    height: 100px;
    border-left: 1rpx dashed #444;
    z-index: 0;
    top: 40rpx;
  }

  .view-item {
    margin-top: 20px;
    position: relative;
    z-index: 1;
    :before {
      content: '';
      position: absolute;
      left: 20rpx;
      right: 0;
      top: 44rpx;
      height: 100%;
      width: 1px;
      background: #eeeeee;
    }
    .sub-title {
      font-size: 28rpx;
      color: #777777;
      margin-left: 58rpx;
      margin-top: 10px;
    }
    .title {
      display: flex;
      align-items: center;
    }
    .title-content {
      font-size: 32rpx;
      color: #444;
      font-weight: bold;
      padding-bottom: 5rpx;
      flex: 1;
    }
    .title-icon-text {
      width: 40rpx;
      height: 40rpx;
      border-radius: 12rpx;
      text-align: center;
      line-height: 40rpx;
      color: #fff;
      font-size: 26rpx;
      margin-right: 18rpx;
      &[type='default'] {
        background: #1a9aff;
      }
      &[type='warning'] {
        background: #f3c35b;
      }
      &[type='error'] {
        background: #ff6c00;
      }
      &[type='success'] {
        background: #6ec98a;
      }

      &:last-child {
        margin-bottom: 0;
      }
    }
  }

  .view-item:last-child {
    :before {
      content: '';
      position: absolute;
      left: 20rpx;
      right: 0;
      top: 44rpx;
      height: 100%;
      width: 0;
    }
  }
</style>
