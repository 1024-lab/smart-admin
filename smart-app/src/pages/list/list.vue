<template>
  <view>
    <y-tabs v-model="active" sticky :offsetTop="43" color="#007aff">
      <y-tab v-for="item in tabsList" :key="item.value" :title="item.title"> </y-tab>
    </y-tabs>

    <ListUI1 v-if="active === 0" />
    <ListUI2 v-if="active === 1" />
    <ListUI3 v-if="active === 2" />
    <ListUI4 v-if="active === 3" />
  </view>
</template>

<script setup>
  import { ref } from 'vue';
  import ListUI1 from './components/list-ui1.vue';
  import ListUI2 from './components/list-ui2.vue';
  import ListUI3 from './components/list-ui3.vue';
  import ListUI4 from './components/list-ui4.vue';

  const active = ref(0);

  const tabsList = [
    {
      title: '线索列表',
      value: 0,
    },
    {
      title: '我的订单',
      value: 1,
    },
    {
      title: '我的运单',
      value: 2,
    },
    {
      title: '付款单',
      value: 3,
    },
  ];
</script>

<style lang="scss" scoped>
  page {
    background-color: #f5f5f5;
  }
</style>
