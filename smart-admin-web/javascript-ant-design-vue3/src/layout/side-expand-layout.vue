<!--
  *  展开菜单模式
  *
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2022-09-06 20:40:16
  * @Wechat:    zhuda1024
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
-->
<template>
  <a-layout class="admin-layout" style="min-height: 100%">
    <!-- 侧边菜单 side-menu -->
    <a-layout-sider :theme="theme" class="side-menu" :collapsed="collapsed" :trigger="null">
      <!-- 左侧菜单 -->
      <SideExpandMenu :collapsed="collapsed" />
    </a-layout-sider>

    <!--中间内容，一共三部分：1、顶部;2、中间内容区域;3、底部（一般是公司版权信息）;-->
    <a-layout class="admin-layout-main" :style="`height: ${windowHeight}px`" id="smartAdminMain">
      <!-- 顶部头部信息 -->
      <a-layout-header class="smart-layout-header">
        <a-row justify="space-between" class="smart-layout-header-user">
          <a-col class="smart-layout-header-left">
            <span class="collapsed-button">
              <menu-unfold-outlined v-if="collapsed" class="trigger" @click="() => (collapsed = !collapsed)" />
              <menu-fold-outlined v-else class="trigger" @click="() => (collapsed = !collapsed)" />
            </span>
            <a-tooltip placement="bottom">
              <template #title>首页</template>
              <span class="home-button" @click="goHome">
                <home-outlined class="trigger" />
              </span>
            </a-tooltip>
            <span class="location-breadcrumb">
              <MenuLocationBreadcrumb />
            </span>
          </a-col>
          <!---用戶操作区域-->
          <a-col class="smart-layout-header-right">
            <HeaderUserSpace />
          </a-col>
        </a-row>
        <PageTag />
      </a-layout-header>

      <!--中间内容-->
      <a-layout-content class="admin-layout-content" id="smartAdminLayoutContent">
        <!--不keepAlive的iframe使用单个iframe组件-->
        <IframeIndex v-show="iframeNotKeepAlivePageFlag" :key="route.name" :name="route.name" :url="route.meta.frameUrl" />
        <!--keepAlive的iframe 每个页面一个iframe组件-->
        <IframeIndex
          v-for="item in keepAliveIframePages"
          v-show="route.name == item.name"
          :key="item.name"
          :name="item.name"
          :url="item.meta.frameUrl"
        />
        <!--非iframe使用router-view-->
        <div v-show="!iframeNotKeepAlivePageFlag && keepAliveIframePages.every((e) => route.name != e.name)">
          <router-view v-slot="{ Component }">
            <keep-alive :include="keepAliveIncludes">
              <component :is="Component" :key="route.name" />
            </keep-alive>
          </router-view>
        </div>
      </a-layout-content>
      <!-- footer 版权公司信息 -->
      <a-layout-footer class="smart-layout-footer" v-show="footerFlag"> <SmartFooter /></a-layout-footer>
      <!---- 回到顶部 --->
      <a-back-top :target="backTopTarget" :visibilityHeight="80" />
    </a-layout>
    <!-- 右侧帮助文档 help-doc -->
    <a-layout-sider
      v-if="helpDocFlag"
      v-show="helpDocExpandFlag"
      theme="light"
      :width="180"
      class="help-doc-sider"
      :trigger="null"
      style="min-height: 100%"
    >
      <SideHelpDoc />
    </a-layout-sider>
  </a-layout>
</template>
<script setup>
  import { computed, onMounted, ref, watch } from 'vue';
  import HeaderUserSpace from './components/header-user-space/index.vue';
  import MenuLocationBreadcrumb from './components/menu-location-breadcrumb/index.vue';
  import PageTag from './components/page-tag/index.vue';
  import SideExpandMenu from './components/side-expand-menu/index.vue';
  import SmartFooter from './components/smart-footer/index.vue';
  import { smartKeepAlive } from './components/smart-keep-alive';
  import IframeIndex from '/@/components/framework/iframe/iframe-index.vue';
  import watermark from '../lib/smart-watermark';
  import { useAppConfigStore } from '/@/store/modules/system/app-config';
  import { useUserStore } from '/@/store/modules/system/user';
  import SideHelpDoc from './components/side-help-doc/index.vue';
  import { useRouter } from 'vue-router';
  import { HOME_PAGE_NAME } from '/@/constants/system/home-const';

  const windowHeight = ref(window.innerHeight);

  //主题颜色
  const theme = computed(() => useAppConfigStore().$state.sideMenuTheme);
  //是否显示标签页
  const pageTagFlag = computed(() => useAppConfigStore().$state.pageTagFlag);
  // 是否显示帮助文档
  const helpDocFlag = computed(() => useAppConfigStore().$state.helpDocFlag);
  // 是否默认展开帮助文档
  const helpDocExpandFlag = computed(() => useAppConfigStore().$state.helpDocExpandFlag);
  // 是否显示页脚
  const footerFlag = computed(() => useAppConfigStore().$state.footerFlag);
  // 是否显示水印
  const watermarkFlag = computed(() => useAppConfigStore().$state.watermarkFlag);
  // 多余高度
  const dueHeight = computed(() => {
    let due = 40;
    if (useAppConfigStore().$state.pageTagFlag) {
      due = due + 40;
    }
    if (useAppConfigStore().$state.footerFlag) {
      due = due + 40;
    }
    return due;
  });
  //是否隐藏菜单
  const collapsed = ref(false);

  //页面初始化的时候加载水印
  onMounted(() => {
    if (watermarkFlag.value) {
      watermark.set('smartAdminLayoutContent', useUserStore().actualName);
    } else {
      watermark.clear();
    }
  });

  watch(
    () => watermarkFlag.value,
    (newValue) => {
      if (newValue) {
        watermark.set('smartAdminLayoutContent', useUserStore().actualName);
      } else {
        watermark.clear();
      }
    }
  );

  window.addEventListener('resize', function () {
    windowHeight.value = window.innerHeight;
  });

  //回到顶部
  const backTopTarget = () => {
    return document.getElementById('smartAdminMain');
  };
  // ----------------------- keep-alive相关 -----------------------
  let { route, keepAliveIncludes, iframeNotKeepAlivePageFlag, keepAliveIframePages } = smartKeepAlive();
  const router = useRouter();
  function goHome() {
    router.push({ name: HOME_PAGE_NAME });
  }
</script>
<style scoped lang="less">
  :deep(.ant-layout-header) {
    height: auto;
  }
  :deep(.layout-header) {
    height: auto;
  }

  .smart-layout-header {
    background: #fff;
    padding: 0;
    z-index: 21;
  }

  .smart-layout-header-user {
    height: @header-user-height;
    border-bottom: 1px solid #f6f6f6;
  }

  .smart-layout-header-left {
    display: flex;
    height: @header-user-height;

    .collapsed-button {
      margin-left: 10px;
      line-height: @header-user-height;
    }

    .home-button {
      margin-left: 15px;
      cursor: pointer;
      padding: 0 5px;
      line-height: @header-user-height;
    }

    .home-button:hover {
      background-color: #efefef;
    }

    .location-breadcrumb {
      margin-left: 15px;
      line-height: @header-user-height;
    }
  }

  .smart-layout-header-right {
    display: flex;
    height: @header-user-height;
  }

  .admin-layout {
    .side-menu {
      flex: 0 !important;
      min-width: inherit !important;
      max-width: none !important;
      width: auto !important;
      &.fixed-side {
        position: fixed;
        height: 100vh;
        left: 0;
        top: 0;
      }
    }

    .help-doc-sider {
      flex: 0 !important;
      min-width: 100px;
      height: 100vh;
      max-width: 100px;
      width: auto !important;
      &.fixed-side {
        position: fixed;
        height: 100vh;
        right: 0;
        top: 0;
      }
    }

    .virtual-side {
      transition: all 0.2s;
    }

    .virtual-header {
      transition: all 0.2s;
      opacity: 0;

      &.fixed-tabs.multi-page:not(.fixed-header) {
        height: 0;
      }
    }

    .admin-layout-main {
      overflow-x: hidden;
    }

    .admin-layout-content {
      background-color: inherit;
      min-height: auto;
      position: relative;
      padding: 10px 10px 0px 10px;
      height: calc(100% - v-bind(dueHeight) px);
      overflow-x: hidden;
    }
  }

  .smart-layout-footer {
    position: relative;
    padding: 10px 0;
    display: flex;
    justify-content: center;
  }
</style>
