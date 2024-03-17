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
      <image src="/@/static/images/mine/no-vip-flag.png" mode=""></image>
    </view>
  </view>
</template>
<script setup>
  import { useUserStore } from '/@/store/modules/system/user';
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
    padding: 20px 15px;
    display: flex;
    flex-direction: row;
    align-items: center;
    .user-icon {
      width: 58px;
      height: 58px;
      display: flex;
      flex-direction: row;
      align-items: center;
      justify-content: center;
      border-radius: 29px;
      background-color: white;
      .user-image {
        width: 54px;
        height: 54px;
        border-radius: 27px;
      }
    }
    .user-info {
      display: flex;
      flex-direction: column;
      align-items: flex-start;
      flex-shrink: 0;
      margin: 5px auto 0 12px;
      align-self: flex-start;
      .user-name {
        height: 25px;
        font-size: 18px;
        font-weight: 600;
        text-align: center;
        color: #323333;
      }
      .user-phone {
        margin-top: 4px;
        height: 20px;
        font-size: 14px;
        font-weight: 400;
        text-align: left;
        color: #777777;
        line-height: 20px;
      }
    }
    .vip-flag {
      align-self: flex-start;
      width: 83px;
      height: 29px;
      margin: 3px 0 0 auto;
      image {
        width: 100%;
        height: 100%;
      }
    }
  }
</style>
