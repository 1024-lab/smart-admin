<template>
  <view class="menu-container">
    <uni-grid :column="5" :highlight="true" :show-border="false" customStyle="display: block;">
      <!--------------------------------- 第一排--------------------------------->
      <uni-grid-item class="menu-grid" style="width: 134rpx; height: 134rpx">
        <view class="menu-item" @click="changeHome">
          <image class="item-image" src="@/static/images/index/ic_home_menu1.png"></image>
          <view class="item-text"> 首页切换 </view>
        </view>
      </uni-grid-item>

      <uni-grid-item class="menu-grid" style="width: 134rpx; height: 134rpx">
        <view class="menu-item" @click="navigateTo('/pages/notice/notice-index')">
          <image class="item-image" src="@/static/images/index/ic_home_menu2.png"></image>
          <view class="item-text"> 通知公告 </view>
        </view>
      </uni-grid-item>
      <uni-grid-item class="menu-grid" style="width: 134rpx; height: 134rpx">
        <view class="menu-item" @click="navigateTo('/pages/enterprise/enterprise-list')">
          <image class="item-image" src="@/static/images/index/ic_home_menu3.png"></image>
          <view class="item-text"> 客户线索 </view>
        </view>
      </uni-grid-item>
      <uni-grid-item class="menu-grid" style="width: 134rpx; height: 134rpx">
        <view class="menu-item" @click="navigateTo('/pages/goods/goods-index')">
          <image class="item-image" src="@/static/images/index/ic_home_menu4.png"></image>
          <view class="item-text"> 品质商品 </view>
        </view>
      </uni-grid-item>
      <uni-grid-item class="menu-grid" style="width: 134rpx; height: 134rpx">
        <view class="menu-item" @click="navigateTo('/pages/support/change-log/change-log-list')">
          <image class="item-image" src="@/static/images/index/ic_home_menu5.png"></image>
          <view class="item-text"> 版本更新 </view>
        </view>
      </uni-grid-item>

      <!--------------------------------- 第二排--------------------------------->
      <uni-grid-item class="menu-grid" style="width: 134rpx; height: 134rpx">
        <view class="menu-item" @click="navigateTo('/pages/form/form')">
          <image class="item-image" src="@/static/images/index/ic_home_menu6.png"></image>
          <view class="item-text"> 复杂表单 </view>
        </view>
      </uni-grid-item>
      <uni-grid-item class="menu-grid" style="width: 134rpx; height: 134rpx">
        <view class="menu-item" @click="navigateTo('/pages/order-detail/order-detail')">
          <image class="item-image" src="@/static/images/index/ic_home_menu7.png"></image>
          <view class="item-text"> 复杂详情 </view>
        </view>
      </uni-grid-item>
      <uni-grid-item class="menu-grid" style="width: 134rpx; height: 134rpx">
        <view class="menu-item" @click="switchTab('/pages/list/list')">
          <image class="item-image" src="@/static/images/index/ic_home_menu9.png"></image>
          <view class="item-text"> 列表样式1 </view>
        </view>
      </uni-grid-item>

      <uni-grid-item class="menu-grid" style="width: 134rpx; height: 134rpx">
        <view class="menu-item" @click="switchTab('/pages/list2/list')">
          <image class="item-image" src="@/static/images/index/ic_home_menu8.png"></image>
          <view class="item-text"> 列表样式2 </view>
        </view>
      </uni-grid-item>
      <uni-grid-item class="menu-grid" style="width: 134rpx; height: 134rpx">
        <view class="menu-item" @click="developing">
          <image class="item-image" src="@/static/images/index/ic_home_menu10.png"></image>
          <view class="item-text"> 接口加密 </view>
        </view>
      </uni-grid-item>
    </uni-grid>
  </view>
</template>

<script setup>
  import { SmartToast } from '@/lib/smart-support';
  const emit = defineEmits(['changeHome']);

  function changeHome() {
    emit('changeHome');
  }

  function navigateTo(url) {
    uni.navigateTo({
      url,
    });
  }
  function switchTab(url) {
    uni.switchTab({
      url,
    });
  }

  function developing() {
    SmartToast.toast('敬请期待');
  }
</script>

<style lang="scss" scoped>
  .menu-container {
    .menu-grid {
      &:nth-child(n + 6) {
        margin-top: 20rpx;
      }
    }
    height: 340rpx;
    box-sizing: border-box;
    border-radius: 16rpx;
    width: 700rpx;
    margin: 0 auto 20rpx;
    display: flex;
    justify-content: space-between;
    padding: 30rpx 10rpx;
    flex-wrap: wrap;
    background-color: #fff;
    .menu-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      width: 134rpx;
      height: 134rpx;
      .item-image {
        width: 80rpx;
        height: 80rpx;
        margin-bottom: 10rpx;
      }
      .item-text {
        font-size: 26rpx;
        color: #353535;
      }
    }
  }
</style>
