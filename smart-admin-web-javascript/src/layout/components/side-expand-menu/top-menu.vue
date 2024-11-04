<!--
  * 第一列菜单
  *
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2022-09-06 20:29:12
  * @Wechat:    zhuda1024
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
-->
<template>
  <div class="top-menu-container">
    <!-- 顶部logo区域 -->
    <div class="logo" @click="onGoHome">
      <img class="logo-img" :src="logoImg" />
      <div class="title smart-logo">{{ websiteName }}</div>
    </div>
    <!-- 一级菜单展示 -->
    <a-menu :selectedKeys="selectedKeys" mode="inline" :theme="theme">
      <template v-for="item in menuTree" :key="item.menuId">
        <template v-if="item.visibleFlag">
          <a-menu-item :key="item.menuId.toString()" @click="onSelectMenu(item)">
            <template #icon>
              <component :is="$antIcons[item.icon]" />
            </template>
            {{ menuNameAdapter(item.menuName) }}
          </a-menu-item>
        </template>
      </template>
    </a-menu>
  </div>
</template>
<script setup>
  import _ from 'lodash';
  import { computed, ref } from 'vue';
  import { HOME_PAGE_NAME } from '/@/constants/system/home-const';
  import { MENU_TYPE_ENUM } from '/@/constants/system/menu-const';
  import { router } from '/@/router';
  import { useAppConfigStore } from '/@/store/modules/system/app-config';
  import { useUserStore } from '/@/store/modules/system/user';
  import logoImg from '/@/assets/images/logo/smart-admin-logo.png';
  import menuEmitter from './side-expand-menu-mitt';

  const websiteName = computed(() => useAppConfigStore().websiteName);
  const theme = computed(() => useAppConfigStore().$state.sideMenuTheme);
  const menuTree = computed(() => useUserStore().getMenuTree || []);

  // 展开菜单的顶级目录名字适配，只展示两个字为好
  function menuNameAdapter(name) {
    return name.substr(0, 2);
  }

  // 选中的顶级菜单
  const selectedKeys = ref([]);

  // 选中菜单，页面跳转
  function onSelectMenu(menuItem) {
    selectedKeys.value = [menuItem.menuId.toString()];
    if (menuItem.menuType === MENU_TYPE_ENUM.MENU.value && (_.isEmpty(menuItem.children) || menuItem.children.every((e) => !e.visibleFlag))) {
      useUserStore().deleteKeepAliveIncludes(menuItem.menuId.toString());
      router.push({ name: menuItem.menuId.toString() });
    }
    menuEmitter.emit('selectTopMenu', menuItem);
  }

  // 更新选中的菜单
  function updateSelectKey(key) {
    selectedKeys.value = [key];
    let selectMenu = _.find(menuTree.value, { menuId: Number(key) });
    if (selectMenu) {
      menuEmitter.emit('selectTopMenu', selectMenu);
    }
  }

  //点击logo回到首页
  function onGoHome() {
    router.push({ name: HOME_PAGE_NAME });
  }

  defineExpose({ updateSelectKey });
</script>
<style scoped lang="less">
  .top-menu-container {
    height: 100%;
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
