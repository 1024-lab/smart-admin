<!--
  * 展开菜单
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-09-06 20:29:12 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
-->
<template>
  <div class="menu-container">
    <!-- logo 第一列：一级导航 -->
    <TopMenu ref="topMenu" class="topMenu" :menuTree="menuTree" />
    <!-- 第二列：导航 -->
    <RecursionMenu v-if="showRecursionMenu" class="recursion-menu" :selectedMenu="selectedMenu" />
  </div>
</template>
<script setup>
  import { computed, ref } from 'vue';
  import RecursionMenu from './recursion-menu.vue';
  import TopMenu from './top-menu.vue';
  import { useUserStore } from '/@/store/modules/system/user';

  defineEmits(['update:value']);
  const menuTree = computed(() => useUserStore().getMenuTree || []);

  const topMenu = ref();
  const selectedMenu = computed(() => {
    if (topMenu.value) {
      return topMenu.value.selectedMenu;
    }
    return {};
  });
  const showRecursionMenu = computed(() => {
    return selectedMenu.value && selectedMenu.value.children && selectedMenu.value.children.some((e) => e.visibleFlag);
  });
</script>
<style scoped lang="less">
  .menu-container {
    display: flex;
    height: 100%;
    .topMenu {
      width: 114px;
      flex-shrink: 0;
    }
    .recursion-menu {
      min-width: 126px;
      flex: 1;
    }
  }
</style>
