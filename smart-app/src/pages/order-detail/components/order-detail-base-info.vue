<template>
  <view class="smart-detail-card smart-margin-top60">
    <view class="smart-detail-card-title"> 商品</view>
    <view class="smart-detail-card-cell">
      <view class="smart-detail-card-label"> 商品名称 </view>
      <view class="smart-detail-card-value"> 华为MateBook 14</view>
    </view>
    <view class="smart-detail-card-cell">
      <view class="smart-detail-card-label"> 价格 </view>
      <view class="smart-detail-card-value uni-error">12,345元 </view>
    </view>
    <view class="smart-detail-card-cell">
      <view class="smart-detail-card-label"> 颜色/产地 </view>
      <view class="smart-detail-card-value">玫瑰金/中国·洛阳 </view>
    </view>
    <view class="smart-detail-card-cell">
      <view class="smart-detail-card-label"> 材质/电压 </view>
      <view class="smart-detail-card-value">金属/220V </view>
    </view>
  </view>
</template>

<script setup>
  const props = defineProps({
    List: {
      type: Array,
      default: [],
    },
  });
</script>

<style lang="scss" scoped></style>
