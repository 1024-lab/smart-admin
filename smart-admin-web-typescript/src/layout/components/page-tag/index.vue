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
  <div id="smartAdminPageTag">
    <DefaultTab v-if="pageTagStyle === PAGE_TAG_ENUM.DEFAULT.value" />
    <AntdTab v-if="pageTagStyle === PAGE_TAG_ENUM.ANTD.value" />
    <ChromeTab v-if="pageTagStyle === PAGE_TAG_ENUM.CHROME.value" />
  </div>
</template>

<script setup lang="ts">
  import { computed } from 'vue';
  import { useAppConfigStore } from '/@/store/modules/system/app-config';
  import DefaultTab from './components/default-tab.vue';
  import AntdTab from './components/antd-tab.vue';
  import ChromeTab from './components/chrome-tab.vue';
  import { PAGE_TAG_ENUM } from '/@/constants/layout-const.js';

  const pageTagStyle = computed(() => useAppConfigStore().$state.pageTagStyle);
</script>
