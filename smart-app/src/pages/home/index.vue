<template>
  <view class="page">
    <uni-nav-bar title="首页" :border="false" fixed>
      <template #right>
        <view class="right">
          <view class="">
            <image src="@/static/images/index/ic_scan.png" mode=""></image>
          </view>
          <view class="">
            <image src="@/static/images/index/ic_search.png" mode=""></image>
          </view>
        </view>
      </template>
    </uni-nav-bar>

    <!-- 数据表 -->
    <Statistics v-if="!showBannerFlag" />

    <!-- Banner -->
    <Banner v-if="showBannerFlag" />

    <!-- 功能菜单 -->
    <Menu @changeHome="changeHome" />

    <!-- 通知公告 -->
    <Notice />

    <!-- 商品 -->
    <Goods />
  </view>
</template>

<script setup>
  import Banner from './components/banner.vue';
  import Menu from './components/menu.vue';
  import Statistics from './components/statistics.vue';
  import Notice from './components/notice.vue';
  import Goods from './components/goods.vue';
  import { ref } from 'vue';
  import { onShow } from '@dcloudio/uni-app';

  const showBannerFlag = ref(false);
  function changeHome() {
    showBannerFlag.value = !showBannerFlag.value;
  }

  onShow(() => {
    uni.pageScrollTo({
      scrollTop: 0,
      duration: 300,
    });
  });
</script>

<style lang="scss" scoped>
  .page {
    background-color: #f5f5f5;
  }

  :deep(.uni-navbar__header-btns) {
    width: 150rpx !important;
  }

  .right {
    position: relative;
    display: flex;
    z-index: 999;

    view {
      width: 56rpx;
      height: 56rpx;
      margin-left: 36rpx;

      image {
        width: 100%;
        height: 100%;
      }
    }
  }
</style>
