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
    <div class="title smart-logo title-light" v-if="sideMenuTheme === 'light'">{{ websiteName }}</div>
    <div class="title smart-logo title-dark" v-if="sideMenuTheme === 'dark'">{{ websiteName }}</div>
  </div>
  <div class="min-logo" @click="onGoHome" v-if="collapsed">
    <img class="logo-img" :src="logoImg" />
  </div>

  <!-- 2、下方菜单区域： 这里使用一个递归菜单解决 -->
  <div class="menu">
    <RecursionMenu :collapsed="collapsed" ref="menuRef" />
  </div>
</template>

<script setup>
  import { computed, nextTick, ref, watch } from 'vue';
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

  const menuRef = ref();

  watch(
    () => props.collapsed,
    (newValue, oldValue) => {
      // 如果是展开菜单的话，重新获取更新菜单的展开项: openkeys和selectKeys
      if (!newValue) {
        nextTick(() => menuRef.value.updateOpenKeysAndSelectKeys());
      }
    }
  );

  const router = useRouter();
  function onGoHome() {
    router.push({ name: HOME_PAGE_NAME });
  }

  const color = computed(() => {
    let isLight = useAppConfigStore().$state.sideMenuTheme === 'light';
    return {
      background: isLight ? '#FFFFFF' : '#001529',
    };
  });
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
      background-color: v-bind('color.background');
      position: fixed;
      width: 80px;
      z-index: 21;
      display: flex;
      justify-content: center;
      align-items: center;
      .logo-img {
        width: 30px;
        height: 30px;
      }
    }

    .logo {
      height: @header-user-height;
      line-height: @header-user-height;
      background-color: v-bind('color.background');
      padding: 0px 15px 0px 15px;
      position: fixed;
      z-index: 21;
      display: flex;
      cursor: pointer;
      justify-content: center;
      align-items: center;

      .logo-img {
        width: 30px;
        height: 30px;
      }

      .title {
        font-size: 16px;
        font-weight: 600;
        margin-left: 8px;
      }
      .title-light {
        color: #001529;
      }
      .title-dark {
        color: #ffffff;
      }
    }
  }
  .menu {
    margin-top: @header-user-height;
  }
</style>
