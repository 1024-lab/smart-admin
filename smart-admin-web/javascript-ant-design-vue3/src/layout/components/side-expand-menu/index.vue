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
    <!-- 第一列：一级导航 -->
    <TopMenu ref="topMenuRef" class="top-menu" />
    <!-- 第二列：二级导航 -->
    <RecursionMenu ref="recursionMenuRef" class="recursion-menu" />
  </div>
</template>
<script setup>
  import { onMounted, ref, watch } from 'vue';
  import { useRoute } from 'vue-router';
  import RecursionMenu from './recursion-menu.vue';
  import TopMenu from './top-menu.vue';
  import { useUserStore } from '/@/store/modules/system/user';

  const props = defineProps({
    placeholder: {
      type: String,
      default: '请选择',
    },
  });

  // 选中的顶级菜单
  const topMenuRef = ref();
  // 二级菜单引用
  const recursionMenuRef = ref();

  let currentRoute = useRoute();

  // 根据路由更新菜单展开和选中状态
  function updateSelectKeyAndOpenKey() {
    // 第一步，根据路由 更新选中 顶级菜单
    let parentList = useUserStore().menuParentIdListMap.get(currentRoute.name) || [];
    if (parentList.length === 0) {
      topMenuRef.value.updateSelectKey(currentRoute.name);
      return;
    }
    topMenuRef.value.updateSelectKey(parentList[0].name);

    //第二步，根据路由 更新 二级菜单的selectKey和openKey
    recursionMenuRef.value.updateSelectKeyAndOpenKey(parentList, currentRoute.name);
  }

  onMounted(updateSelectKeyAndOpenKey);

  //监听路由的变化，进行更新菜单展开项目
  watch(currentRoute, () => {
    updateSelectKeyAndOpenKey();
  });
</script>
<style scoped lang="less">
  .menu-container {
    display: flex;
    height: 100%;

    .top-menu {
      width: 114px;
      flex-shrink: 0;
    }

    .recursion-menu {
      min-width: 126px;
      flex: 1;
    }
  }
</style>
