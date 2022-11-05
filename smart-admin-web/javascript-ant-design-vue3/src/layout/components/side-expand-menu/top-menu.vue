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
      <div class="title">SmartAdmin 2.X</div>
    </div>
    <!-- 一级菜单展示 -->
    <a-menu :selectedKeys="selectedKeys" mode="inline" :theme="theme">
      <template v-for="item in props.menuTree" :key="item.menuId">
        <template v-if="item.visibleFlag">
          <a-menu-item :key="item.menuId.toString()" @click="onSelectMenu(item)">
            <template #icon>
              <component :is="$antIcons[item.icon]" />
            </template>
            {{ item.menuName }}
          </a-menu-item>
        </template>
      </template>
    </a-menu>
  </div>
</template>
<script setup>
  import _ from 'lodash';
  import { computed, ref, watch } from 'vue';
  import { useRoute } from 'vue-router';
  import logoImg from '/@/assets/images/logo/smart-admin-logo.png';
  import { HOME_PAGE_NAME } from '/@/constants/system/home-const';
  import { MENU_TYPE_ENUM } from '/@/constants/system/menu-const';
  import { router } from '/@/router';
  import { useAppConfigStore } from '/@/store/modules/system/app-config';
  import { useUserStore } from '/@/store/modules/system/user';

  const theme = computed(() => useAppConfigStore().$state.sideMenuTheme);

  const props = defineProps({
    menuTree: Array,
  });
  const selectedMenu = ref();
  let currentRoute = useRoute();

  const parentMenuList = computed(() => {
    let currentName = currentRoute.name;
    if (!currentName || typeof currentName !== 'string') {
      return [];
    }
    let menuParentIdListMap = useUserStore().getMenuParentIdListMap;
    return menuParentIdListMap.get(currentName) || [];
  });

  const selectedKeys = computed(() => {
    if (selectedMenu.value) {
      return [selectedMenu.value.menuId.toString()];
    }
    return parentMenuList.value.map((e) => e.name);
  });

  //监听路由的变化，进行更新菜单展开项目
  watch(
    currentRoute,
    () => {
      selectedMenu.value = undefined;
      let menuList = props.menuTree.map((e) => e.menuId.toString());
      let parentIdList = _.intersection(menuList, selectedKeys.value);
      if (parentIdList.length > 0) {
        let parentId = parentIdList[0];
        let parentItem = props.menuTree.find((e) => e.menuId == Number(parentId));
        selectedMenu.value = parentItem;
      }
    },
    {
      immediate: true,
    }
  );
  // 选中菜单，页面跳转
  function onSelectMenu(route) {
    selectedMenu.value = route;
    if (route.menuType == MENU_TYPE_ENUM.MENU.value && (_.isEmpty(route.children) || route.children.every((e) => !e.visibleFlag))) {
      router.push({ name: route.menuId.toString() });
    }
  }
  //点击logo回到首页
  function onGoHome() {
    router.push({ name: HOME_PAGE_NAME });
  }
  defineExpose({
    selectedMenu,
  });
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
    z-index: 9999;
    display: flex;
    justify-content: space-between;
    cursor: pointer;

    .logo-img {
      width: 40px;
      height: 40px;
    }

    .title {
      font-size: 16px;
      font-weight: 600;
      overflow: hidden;
      color: v-bind('theme === "light" ? "#001529": "#ffffff"');
    }
  }
</style>
