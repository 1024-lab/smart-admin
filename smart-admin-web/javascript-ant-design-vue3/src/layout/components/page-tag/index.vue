<!--
  * 标签页 入口，支持三种模式：默认、a-tabs, chrome-tabs
  *
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2024-06-12 20:55:04
  * @Wechat:    zhuda1024
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
-->
<template>
  <DefaultTab v-if="pageTagStyle === PAGE_TAG_ENUM.DEFAULT.value" />
  <AntdTab v-if="pageTagStyle === PAGE_TAG_ENUM.ANTD.value" />
</template>

<script setup>
  import { computed } from 'vue';
  import { useAppConfigStore } from '/@/store/modules/system/app-config';
  import DefaultTab from './components/default-tab.vue';
  import AntdTab from './components/antd-tab.vue';
  import { PAGE_TAG_ENUM } from '/@/constants/layout-const.js';

  const pageTagStyle = computed(() => useAppConfigStore().$state.pageTagStyle);
</script>
