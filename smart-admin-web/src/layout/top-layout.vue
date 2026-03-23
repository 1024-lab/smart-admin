<template>
  <a-layout class="admin-layout">
    <!-- 顶部菜单  -->
    <a-layout-header class="top-menu" :theme="theme" :id="LAYOUT_ELEMENT_IDS.menu" v-if="!fullScreenFlag">
      <TopMenu />
    </a-layout-header>

    <!--中间内容-->
    <a-layout-content :id="LAYOUT_ELEMENT_IDS.content" class="admin-layout-content">
      <!---标签页-->
      <div class="page-tag-div" v-show="(pageTagFlag && !fullScreenFlag) || breadCrumbFlag" :id="LAYOUT_ELEMENT_IDS.header">
        <PageTag />
      </div>
      <div class="bread-crumb-div" v-if="breadCrumbFlag">
        <MenuLocationBreadcrumb />
      </div>

      <!--不keepAlive的iframe使用单个iframe组件-->
      <IframeIndex v-if="iframeNotKeepAlivePageFlag" :key="route.name" :name="route.name" :url="route.meta.frameUrl" />

      <!--keepAlive的iframe 每个页面一个iframe组件-->
      <IframeIndex
        v-for="item in keepAliveIframePages"
        v-show="route.name === item.name"
        :key="item.name"
        :name="item.name"
        :url="item.meta.frameUrl"
      />

      <!--非iframe使用router-view-->
      <div
        v-show="!iframeNotKeepAlivePageFlag && keepAliveIframePages.every((e) => route.name !== e.name)"
        :style="{ height: contentBoxHeight + 'px' }"
        class="admin-content"
      >
        <router-view v-slot="{ Component }">
          <keep-alive :include="keepAliveIncludes">
            <component :is="Component" :key="route.name" />
          </keep-alive>
        </router-view>
      </div>
    </a-layout-content>

    <!-- footer 版权公司信息 -->
    <a-layout-footer class="layout-footer" v-show="footerFlag">
      <smart-footer />
    </a-layout-footer>
    <!--- 回到顶部 -->
    <a-back-top :target="backTopTarget" :visibilityHeight="80" />
  </a-layout>
</template>

<script setup>
  import { computed, onMounted, ref, watch } from 'vue';
  import { useAppConfigStore } from '../store/modules/system/app-config';
  import PageTag from './components/page-tag/index.vue';
  import TopMenu from './components/top-menu/index.vue';
  import SmartFooter from './components/smart-footer/index.vue';
  import { smartKeepAlive } from './components/smart-keep-alive';
  import IframeIndex from '/@/components/framework/iframe/iframe-index.vue';
  import watermark from '../lib/smart-watermark';
  import { useUserStore } from '/@/store/modules/system/user';
  import { useRouter } from 'vue-router';
  import { HOME_PAGE_NAME } from '/@/constants/system/home-const';
  import { LAYOUT_ELEMENT_IDS } from '/@/layout/layout-const.js';
  import MenuLocationBreadcrumb from './components/menu-location-breadcrumb/index.vue';
  import { theme as antDesignTheme } from 'ant-design-vue';

  const windowHeight = ref(window.innerHeight);
  //主题颜色
  const theme = computed(() => useAppConfigStore().$state.sideMenuTheme);
  const color = computed(() => {
    let isLight = useAppConfigStore().$state.sideMenuTheme === 'light';
    return {
      color: isLight ? '#001529' : '#FFFFFF',
      background: isLight ? '#FFFFFF' : '#001529',
    };
  });

  //是否全屏
  const fullScreenFlag = computed(() => useAppConfigStore().$state.fullScreenFlag);
  //是否显示标签页
  const pageTagFlag = computed(() => useAppConfigStore().$state.pageTagFlag);
  // 是否显示页脚
  const footerFlag = computed(() => useAppConfigStore().$state.footerFlag);
  // 是否显示水印
  const watermarkFlag = computed(() => useAppConfigStore().$state.watermarkFlag);
  // 面包屑
  const breadCrumbFlag = computed(() => useAppConfigStore().$state.breadCrumbFlag);
  // 页面宽度
  const pageWidth = computed(() => useAppConfigStore().$state.pageWidth);

  let contentBoxHeight = ref();
  // 页面内容区域的高度
  const contentTop = computed(() => {
    let due = 45;
    let existComponentCount = 0;
    if (useAppConfigStore().$state.pageTagFlag) {
      existComponentCount++;
    }
    if (useAppConfigStore().$state.breadCrumbFlag) {
      existComponentCount++;
      due = 40;
    }
    return due + existComponentCount * 40 + 'px';
  });

  // 面包屑高度
  const breadCrumbTop = computed(() => {
    if (useAppConfigStore().$state.pageTagFlag) {
      return '88px';
    } else {
      return '45px';
    }
  });

  watch(
    () => contentTop.value,
    () => {
      let dom = document.querySelector('.admin-layout-content');
      contentBoxHeight.value = dom.offsetHeight - 20 - contentTop.value.split('px')[0];
    }
  );

  onMounted(() => {
    let dom = document.querySelector('.admin-layout-content');
    contentBoxHeight.value = dom.offsetHeight - 20 - contentTop.value.split('px')[0];
  });

  //页面初始化的时候加载水印
  onMounted(() => {
    if (watermarkFlag.value) {
      watermark.set(LAYOUT_ELEMENT_IDS.content, useUserStore().actualName);
    } else {
      watermark.clear();
    }
  });

  watch(
    () => watermarkFlag.value,
    (newValue) => {
      if (newValue) {
        watermark.set(LAYOUT_ELEMENT_IDS.content, useUserStore().actualName);
      } else {
        watermark.clear();
      }
    }
  );

  //回到顶部
  const backTopTarget = () => {
    return document.getElementById(LAYOUT_ELEMENT_IDS.main);
  };

  const router = useRouter();

  function goHome() {
    router.push({ name: HOME_PAGE_NAME });
  }

  window.addEventListener('resize', function () {
    windowHeight.value = window.innerHeight;
  });

  // ----------------------- keep-alive相关 -----------------------
  let { route, keepAliveIncludes, iframeNotKeepAlivePageFlag, keepAliveIframePages } = smartKeepAlive();

  const { useToken } = antDesignTheme;
  const { token } = useToken();
</script>

<style lang="less" scoped>
  @color-border-secondary: v-bind('token.colorBorderSecondary');
  @color-bg-container: v-bind('token.colorBgContainer');
  .admin-layout {
    min-height: 100%;

    .top-menu {
      padding: 0px;
      height: 48px;
      line-height: 48px;
      width: 100%;
      z-index: 3;
      right: 0;
      position: fixed;
      background-color: v-bind('color.background');
    }

    .admin-layout-content {
      background-color: inherit;
      min-height: auto;
      position: relative;
      overflow-x: hidden;
      padding: 10px 0;
      width: v-bind(pageWidth);
      margin-top: v-bind(contentTop);
      margin-left: auto;
      margin-right: auto;

      .page-tag-div {
        background: @color-bg-container;
        position: fixed;
        top: 48px;
        width: v-bind(pageWidth);
        height: 40px;
        line-height: 40px;
        z-index: 3;
      }

      .bread-crumb-div {
        position: fixed;
        top: v-bind(breadCrumbTop);
        width: v-bind(pageWidth);
        height: 40px;
        line-height: 40px;
        z-index: 3;
      }
    }
  }

  .layout-footer {
    position: relative;
    padding: 7px 0px;
    display: flex;
    justify-content: center;
  }
</style>
