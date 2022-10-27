<!--
  * 传统菜单
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-09-06 20:29:12 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
-->
<template>
  <!--左侧菜单分为两部分：1、顶部logo区域，包含 logo和名称;2、下方菜单区域-->

  <!-- 1、顶部logo区域 -->
  <div class="logo" @click="onGoHome" :style="sideMenuWidth" v-if="!collapsed">
    <img class="logo-img" :src="logoImg" />
    <div class="title title-light" v-if="sideMenuTheme === 'light'">{{websiteName}}</div>
    <div class="title title-dark" v-if="sideMenuTheme === 'dark'">{{websiteName}}</div>
  </div>
  <div class="min-logo" @click="onGoHome" v-if="collapsed">
    <img class="logo-img" :src="logoImg" />
  </div>

  <!-- 2、下方菜单区域： 这里使用一个递归菜单解决 -->
  <RecursionMenu :collapsed="collapsed" ref="menu" />
</template>

<script setup>
  import { computed, ref, watch } from 'vue';
  import { useRouter } from 'vue-router';
  import RecursionMenu from './recursion-menu.vue';
  import logoImg from '/@/assets/images/logo/smart-admin-logo.png';
  import { HOME_PAGE_NAME } from '/@/constants/system/home-const';
  import { useAppConfigStore } from '/@/store/modules/system/app-config';

  const websiteName = computed(() => useAppConfigStore().websiteName);
  const sideMenuWidth = computed(() => 'width:' + useAppConfigStore().sideMenuWidth + 'px');
  const sideMenuTheme = computed(() => useAppConfigStore().sideMenuTheme);

  const props = defineProps({
    collapsed: {
      type: Boolean,
      required: false,
      default: false,
    },
  });

  const menu = ref();

  watch(
    () => props.collapsed,
    (newValue, oldValue) => {
      // 如果是展开菜单的话，重新获取更新菜单的展开项: openkeys和selectKeys
      if (!newValue) {
        menu.value.updateOpenKeysAndSelectKeys();
      }
    }
  );

  const router = useRouter();
  function onGoHome() {
    router.push({ name: HOME_PAGE_NAME });
  }
</script>

<style lang="less" scoped>
  .shadow {
    box-shadow: 2px 0 6px rgba(0, 21, 41, 0.35);
  }
  .side-menu {
    min-height: 100vh;
    overflow-y: auto;
    z-index: 10;

    .min-logo {
      height: @header-user-height;
      line-height: @header-user-height;
      padding: 0px 15px 0px 15px;
      width: 100%;
      z-index: 9999;
      display: flex;
      justify-content: center;
      .logo-img {
        width: 32px;
        height: 32px;
      }
    }

    .logo {
      height: @header-user-height;
      line-height: @header-user-height;
      padding: 0px 15px 0px 15px;
      z-index: 9999;
      display: flex;
      cursor: pointer;
      justify-content:space-around;

      .logo-img {
        width: 40px;
        height: 40px;
      }

      .title {
        font-size: 16px;
        font-weight: 600;
        margin-left:8px;
      }
      .title-light {
        color:#001529;
      }
      .title-dark {
        color:#ffffff;
      }
    }
  }
  .menu {
    padding: 16px 0;
  }
</style>
