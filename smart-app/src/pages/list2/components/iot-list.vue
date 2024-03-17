<template>
  <view class="item" v-for="(item, index) in 12" :key="index" :class="type[index]">
    <view class="header"> PLC控制柜 </view>
    <view class="footer">
      <view class="footer-left"> 气路总阀：{{ date }} </view>
      <view class="footer-state" :class="type[index]"> 关闭 </view>
    </view>
  </view>
</template>

<script setup>
  import dayjs from 'dayjs';

  defineProps({
    list: {
      type: Array,
      default: [],
    },
  });

  const date = dayjs().format('YYYY-MM-DD HH:mm:ss');

  const type = [
    'info',
    'primary',
    'warning',
    '',
    'info',
    'primary',
    'warning',
    '',
    'info',
    'primary',
    'warning',
    '',
    'info',
    'primary',
    'warning',
    '',
  ];
</script>

<style lang="scss" scoped>
  .item {
    width: 700rpx;
    margin: 20rpx auto 0;
    background: #ffffff;
    border-radius: 12rpx;
    box-shadow: 0px 3px 4px 0px rgba(24, 144, 255, 0.06);
    padding: 26rpx 40rpx 20rpx;
    box-sizing: border-box;
    border-left: 8rpx solid #4f4d4d;
    &.primary {
      border-color: #007ff2;
    }
    &.warning {
      border-color: #ff6c00;
    }
    &.info {
      border-color: #cccccc;
    }

    .header {
      display: flex;
      justify-content: space-between;
      padding-bottom: 20rpx;
      border-bottom: 1rpx solid #ededed;
      margin-bottom: 19rpx;
      font-size: 34rpx;
      color: #444;
    }

    .footer {
      display: flex;
      justify-content: space-between;
      font-size: 28rpx;
      .footer-left {
        color: #777777;
      }
      .footer-state {
        &.primary {
          color: #007ff2;
        }
        &.warning {
          color: #ff6c00;
        }
        &.info {
          color: #cccccc;
        }
        color: #4f4d4d;
      }
    }
  }
</style>
