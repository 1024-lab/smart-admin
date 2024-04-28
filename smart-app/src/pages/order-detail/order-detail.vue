<template>
  <view class="container">
    <smart-detail-tabs :tabsList="tabsList" v-model="active" :fixed="true" @change="scrollTo" />
    <view class="smart-detail">
      <OrderDetailBaseInfo id="detail0" />
      <DetailModelPath id="detail1" />
      <OrderDetailSettle id="detail2" />
    </view>
  </view>
</template>

<script setup>
  import { ref } from 'vue';
  import SmartDetailTabs from '@/components/smart-detail-tabs/index.vue';
  import OrderDetailBaseInfo from './components/order-detail-base-info.vue';
  import DetailModelPath from './components/detail-model-path.vue';
  import OrderDetailSettle from './components/order-detail-settle.vue';

  const tabsList = [
    {
      label: '基本信息',
      value: 0,
    },
    {
      label: '物流信息',
      value: 1,
    },
    {
      label: '结算信息',
      value: 2,
    },
  ];

  const active = ref(0);

  function scrollTo(tab) {
    uni.pageScrollTo({
      selector: '#detail' + tab,
      duration: 300,
      success: console.log,
      fail: console.log,
    });
  }
</script>

<style lang="scss" scoped>
  .container {
    background-color: #f4f4f4;
    height: 100vh;
    overflow-y: scroll;
  }
</style>
