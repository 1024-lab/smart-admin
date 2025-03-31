<!--
  * 递归菜单
  *
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2022-09-06 20:29:12
  * @Wechat:    zhuda1024
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
-->
<template>
  <div class="recursion-container" v-show="topMenu.children && topMenu.children.length > 0">
    <!-- 顶部logo区域 -->
    <div class="logo" @click="onGoHome" :style="sideMenuWidth" v-if="!collapsed">
      <img class="logo-img" :src="logoImg" />
      <div class="title smart-logo title-light" v-if="isLight">{{ websiteName }}</div>
      <div class="title smart-logo title-dark" v-if="!isLight">{{ websiteName }}</div>
    </div>
    <div class="min-logo" @click="onGoHome" v-if="collapsed">
      <img class="logo-img" :src="logoImg" />
    </div>
    <!-- 次级菜单展示 -->
    <a-menu :selectedKeys="selectedKeys" :theme="theme" :openKeys="openKeys" mode="inline">
      <template v-for="item in topMenu.children" :key="item.menuId">
        <template v-if="item.visibleFlag">
          <template v-if="$lodash.isEmpty(item.children)">
            <a-menu-item :key="item.menuId.toString()" @click="turnToPage(item)">
              <template #icon v-if="item.icon">
                <component :is="$antIcons[item.icon]" />
              </template>
              {{ item.menuName }}
            </a-menu-item>
          </template>
          <template v-else>
            <SubMenu :menu-info="item" :key="item.menuId" @turnToPage="turnToPage" />
          </template>
        </template>
      </template>
    </a-menu>
  </div>
</template>
<script setup lang="ts">
  import { ref, computed, watch } from 'vue';
  import { HOME_PAGE_NAME } from '/@/constants/system/home-const';
  import SubMenu from './sub-menu.vue';
  import { router } from '/@/router';
  import { useRoute } from 'vue-router';
  import _ from 'lodash';
  import menuEmitter from './top-expand-menu-mitt';
  import { useAppConfigStore } from '/@/store/modules/system/app-config';
  import { useUserStore } from '/@/store/modules/system/user';
  import logoImg from '/@/assets/images/logo/smart-admin-logo.png';

  const websiteName = computed(() => useAppConfigStore().websiteName);
  const theme = computed(() => useAppConfigStore().$state.sideMenuTheme);

  const props = defineProps({
    collapsed: {
      type: Boolean,
      default: false,
    },
  });

  //菜单宽度
  const sideMenuWidth = computed(() => useAppConfigStore().$state.sideMenuWidth);

  // 选中的顶级菜单
  let topMenu = ref({});
  menuEmitter.on('selectTopMenu', onSelectTopMenu);

  //动态通知顶部菜单栏侧边栏状态
  watch(
    topMenu,
    (value) => {
      let hasSideMenu = value.children && value.children.length > 0;
      menuEmitter.emit('sideMenuChange', hasSideMenu);
    },
    { immediate: true, deep: true }
  );

  // 监听选中顶级菜单事件
  function onSelectTopMenu(selectedTopMenu) {
    topMenu.value = selectedTopMenu;
    if (selectedTopMenu.children && selectedTopMenu.children.length > 0) {
      openKeys.value = _.map(selectedTopMenu.children, 'menuId').map((e) => e.toString());
    } else {
      openKeys.value = [];
    }
    selectedKeys.value = [];
  }

  //展开的菜单
  let currentRoute = useRoute();
  const selectedKeys = ref([]);
  const openKeys = ref([]);

  function updateSelectKeyAndOpenKey(parentList, currentSelectKey) {
    if (!parentList) {
      return;
    }
    //获取需要展开的menu key集合
    openKeys.value = _.map(parentList, 'name');
    selectedKeys.value = [currentSelectKey];
  }

  watch(
    currentRoute,
    (value) => {
      selectedKeys.value = [value.name];
    },
    {
      immediate: true,
    }
  );
  // 页面跳转
  function turnToPage(route) {
    useUserStore().deleteKeepAliveIncludes(route.menuId.toString());
    router.push({ name: route.menuId.toString() });
  }

  function onGoHome() {
    router.push({ name: HOME_PAGE_NAME });
  }

  defineExpose({ updateSelectKeyAndOpenKey });

  const isLight = computed(() => useAppConfigStore().$state.sideMenuTheme === 'light');
  const color = computed(() => {
    let isLight = useAppConfigStore().$state.sideMenuTheme === 'light';
    return {
      background: isLight ? '#FFFFFF' : '#001529',
    };
  });
</script>
<style scoped lang="less">
  .recursion-container {
    height: 100%;
    background-color: v-bind('color.background');
  }

  .min-logo {
    height: @header-user-height;
    line-height: @header-user-height;
    padding: 0px 15px 0px 15px;
    // background-color: v-bind('color.background');

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
  .top-menu {
    overflow: hidden;
    display: flex;
    align-items: center;
    justify-content: center;
    height: @header-user-height;
    font-size: 16px;
    color: #515a6e;
    border-bottom: 1px solid #f3f3f3;
    border-right: 1px solid #f3f3f3;
  }
  .logo {
    height: @header-user-height;
    line-height: @header-user-height;
    padding: 0px 15px 0px 15px;
    width: 100%;
    z-index: 100;
    display: flex;
    justify-content: space-between;
    align-items: center;
    cursor: pointer;

    .logo-img {
      width: 30px;
      height: 30px;
    }

    .title {
      font-size: 16px;
      font-weight: 600;
      overflow: hidden;
      word-wrap: break-word;
      white-space: nowrap;
      color: v-bind('theme === "light" ? "#001529": "#ffffff"');
    }
  }
</style>
