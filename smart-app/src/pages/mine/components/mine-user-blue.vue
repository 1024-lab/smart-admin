<template>
  <!-- 顶部背景 -->
  <view class="nav-container">
    <view class="title"> 我的 </view>
  </view>
  <!-- 用户信息 -->
  <view class="user-info-box">
    <view class="user-icon">
      <image src="https://img.smartadmin.1024lab.net/hexagon/logo.png" class="user-image"> </image>
    </view>
    <view class="user-info">
      <view class="user-name">{{ actualName }}</view>
      <view class="user-phone">{{ departmentName }}</view>
    </view>
    <view class="vip-flag">
      <image src="@/static/images/mine/no-vip-flag.png" mode=""></image>
    </view>
  </view>
</template>
<script setup>
  import { useUserStore } from '@/store/modules/system/user';
  import { computed } from 'vue';

  const actualName = computed(() => {
    return useUserStore().actualName;
  });
  const phone = computed(() => {
    return useUserStore().phone;
  });
  const departmentName = computed(() => {
    return useUserStore().departmentName;
  });
</script>
<style scoped lang="scss">
  .nav-container {
    flex-shrink: 0;
    width: 100%;
    height: 200rpx;
    background: url('@/static/images/mine/top-background.png') center/100% no-repeat;
    position: relative;
    .title {
      text-align: center;
      margin-top: 30rpx;
      font-size: 36rpx;
      color: #fff;
      font-weight: bold;
    }
  }

  .user-info-box {
    z-index: 10;
    margin: -60rpx 12px 0;
    background: #ffffff;
    border-radius: 6px;
    box-shadow: 0px 3px 4px 0px rgba(24, 144, 255, 0.06);
    backdrop-filter: blur(5.98px);
    padding: 40rpx 30rpx;
    display: flex;
    flex-direction: row;
    align-items: center;
    .user-icon {
      width: 120rpx;
      height: 120rpx;
      display: flex;
      flex-direction: row;
      align-items: center;
      justify-content: center;
      border-radius: 60rpx;
      background-color: white;
      .user-image {
        width: 108rpx;
        height: 108rpx;
        border-radius: 54rpx;
      }
    }
    .user-info {
      display: flex;
      flex-direction: column;
      align-items: flex-start;
      flex-shrink: 0;
      margin: 10rpx auto 0 24rpx;
      align-self: flex-start;
      .user-name {
        height: 50rpx;
        font-size: 40rpx;
        font-weight: 600;
        text-align: center;
        color: #323333;
      }
      .user-phone {
        margin-top: 8rpx;
        height: 40rpx;
        font-size: 30rpx;
        font-weight: 400;
        text-align: left;
        color: #777777;
        line-height: 40rpx;
      }
    }
    .vip-flag {
      align-self: flex-start;
      width: 170rpx;
      height: 60rpx;
      margin: 6rpx 0 0 auto;
      image {
        width: 100%;
        height: 100%;
      }
    }
  }
</style>
