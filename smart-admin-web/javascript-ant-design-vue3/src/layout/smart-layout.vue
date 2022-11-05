<!--
  *  layout 多种模式
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-09-06 20:40:16 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
-->
<template>
  <!--左侧菜单 模式-->
  <SmartSideLayout v-if="layout === LAYOUT_ENUM.SIDE.value" />
  <!--左侧展开菜单 模式-->
  <SmartSideExpandLayout v-if="layout === LAYOUT_ENUM.SIDE_EXPAND.value" />
</template>
<script setup>
  import { computed } from 'vue';
  import SmartSideExpandLayout from './smart-side-expand-layout.vue';
  import { LAYOUT_ENUM } from '/@/constants/layout-const';
  import SmartSideLayout from '/@/layout/smart-side-layout.vue';
  import { useAppConfigStore } from '/@/store/modules/system/app-config';

  const layout = computed(() => useAppConfigStore().$state.layout);
</script>
