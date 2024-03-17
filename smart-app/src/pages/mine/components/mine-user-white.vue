<template>
  <view class="user-info-box">
    <view class="user-icon">
      <image class="user-image" src="https://img.smartadmin.1024lab.net/hexagon/logo.png"> </image>
    </view>
    <view class="user-info">
      <view class="user-name">{{ actualName }}</view>
      <view class="user-phone">{{ departmentName }}</view>
    </view>
  </view>

  <view class="vip-card">
    <view class="card-left">
      <image class="vip-icon" src="/static/images/mine/vip-icon.png" mode=""></image>
      <view class=""> SmartAdmin </view>
    </view>
    <image class="open-vip" src="/static/images/mine/open-vip.png" mode=""></image>
  </view>

  <view class="grid-menu">
    <uni-grid :column="4" :showBorder="false">
      <uni-grid-item v-for="(item, index) in menuList" :index="index" :key="index">
        <view class="grid-item-box" style="background-color: #fff">
          <image class="image" :src="item.image" mode=""></image>
          <text class="text">{{ item.title }}</text>
        </view>
      </uni-grid-item>
    </uni-grid>
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
  const menuList = [
    {
      title: '地址',
      image: '/static/images/mine/mine-menu-address.png',
    },
    {
      title: '优惠券',
      image: '/static/images/mine/mine-menu-coupon.png',
    },
    {
      title: '收藏',
      image: '/static/images/mine/mine-menu-collect.png',
    },
    {
      title: '余额',
      image: '/static/images/mine/mine-menu-balance.png',
    },
  ];
</script>
<style scoped lang="scss">
  .user-info-box {
    z-index: 10;
    padding: 32px 12px 15px;
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
  }

  .vip-card {
    background-image: url('/@/static/images/mine/vip-bg.png');
    height: 80rpx;
    background-repeat: no-repeat;
    width: 700rpx;
    margin: 0 auto;
    background-size: 700rpx 80rpx;
    display: flex;
    align-items: center;
    justify-content: space-between;
    .card-left {
      display: flex;
      align-items: center;
      .vip-icon {
        width: 50rpx;
        height: 46rpx;
        margin-left: 32rpx;
        margin-right: 8rpx;
      }
      view {
        color: #f5cc8f;
        font-size: 30rpx;
        font-weight: 600;
      }
    }

    .open-vip {
      width: 136rpx;
      height: 46rpx;
      display: flex;
      margin-right: 32rpx;
    }
  }

  .grid-menu {
    width: 700rpx;
    margin: 20rpx auto;
  }
  .menu-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    .item-image {
      width: 80rpx;
      height: 80rpx;
    }
  }

  .image {
    width: 84rpx;
    height: 84rpx;
  }

  .text {
    font-size: 14px;
    margin-top: 5px;
  }
  .grid-item-box {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 15px 0;
  }
</style>
