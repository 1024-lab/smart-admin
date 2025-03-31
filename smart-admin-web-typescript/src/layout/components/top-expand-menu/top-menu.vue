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
    <!-- 一级菜单展示 -->
    <a-menu :selectedKeys="selectedKeys" mode="horizontal" :theme="theme">
      <template v-for="item in menuTree" :key="item.menuId">
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
<script setup lang="ts">
  import _ from 'lodash';
  import { computed, ref } from 'vue';
  import { MENU_TYPE_ENUM } from '/@/constants/system/menu-const';
  import { router } from '/@/router';
  import { useAppConfigStore } from '/@/store/modules/system/app-config';
  import { useUserStore } from '/@/store/modules/system/user';

  import menuEmitter from './top-expand-menu-mitt';

  const websiteName = computed(() => useAppConfigStore().websiteName);
  const theme = computed(() => useAppConfigStore().$state.sideMenuTheme);
  const menuTree = computed(() => useUserStore().getMenuTree || []);

  const props = defineProps({
    collapsed: {
      type: Boolean,
      default: false,
    },
  });
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

  defineExpose({ updateSelectKey });

  // 动态计算当前导航宽度
  let hasSideMenu = ref(false);
  menuEmitter.on('sideMenuChange', (data) => {
    hasSideMenu.value = data;
  });

  const menuInfo = computed(() => {
    let width = '100vw';
    let right = '-100vw';
    if (hasSideMenu.value) {
      if (props.collapsed) {
        width = 'calc(100vw - 80px)';
        right = 'calc(-100vw + 80px)';
      } else {
        width = 'calc(100vw - 180px)';
        right = 'calc(-100vw + 180px)';
      }
    }
    return {
      width,
      right,
    };
  });
</script>
<style scoped lang="less">
  .top-menu {
    position: absolute;
    transition:
      all 0.2s,
      background 0s;
    top: 0;
    right: v-bind('menuInfo.right');
    width: v-bind('menuInfo.width');
    flex-shrink: 0;
  }
  .ant-menu-dark {
    background: #1677ff;
    color: #fff;
  }
  .ant-menu-light {
    background: #1677ff;
    color: #fff;
  }
  :deep(.ant-menu-item-selected){
    background: #0958d9 !important;
    color: #fff !important;
  }
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
