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
    <!-- 顶部顶级菜单名称 -->
    <div class="top-menu">
      <span class="ant-menu">{{ topMenu.menuName }}</span>
    </div>
    <!-- 次级菜单展示 -->
    <a-menu :selectedKeys="selectedKeys" :openKeys="openKeys" mode="inline">
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
<script setup>
  import { ref } from 'vue';
  import { HOME_PAGE_NAME } from '/@/constants/system/home-const';
  import SubMenu from './sub-menu.vue';
  import { router } from '/@/router';
  import _ from 'lodash';
  import menuEmitter from './side-expand-menu-mitt';
  import { useUserStore } from '/@/store/modules/system/user';

  // 选中的顶级菜单
  let topMenu = ref({});
  menuEmitter.on('selectTopMenu', onSelectTopMenu);

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

  // 页面跳转
  function turnToPage(route) {
    useUserStore().deleteKeepAliveIncludes(route.menuId.toString());
    router.push({ name: route.menuId.toString() });
  }

  function goHome() {
    router.push({ name: HOME_PAGE_NAME });
  }

  defineExpose({ updateSelectKeyAndOpenKey });
</script>
<style scoped lang="less">
  .recursion-container {
    height: 100%;
    background: #ffffff;
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
</style>
