<template>
  <view class="banner" v-if="!$lodash.isEmpty(bannerList)">
    <swiper class="swiper" circular :indicator-dots="true" :autoplay="true" indicator-active-color="#fff">
      <swiper-item v-for="(item, index) in bannerList" :key="index">
        <image class="swiper-item" :src="item.image" mode=""></image>
      </swiper-item>
    </swiper>
  </view>
</template>

<script setup>
  import { ref } from 'vue';
  const bannerList = ref([
    {
      image: '/static/images/index/banner.png',
    },
    {
      image: '/static/images/index/banner.png',
    },
    {
      image: '/static/images/index/banner.png',
    },
    {
      image: '/static/images/index/banner.png',
    },
  ]);
</script>

<style lang="scss" scoped>
  .banner {
    border-radius: 16rpx;
    overflow: hidden;
    width: 700rpx;
    height: 300rpx;
    margin: 10rpx auto 20rpx;
    .swiper {
      width: 100%;
      .swiper-item {
        width: 100%;
        height: 100%;
      }
    }
  }
</style>
