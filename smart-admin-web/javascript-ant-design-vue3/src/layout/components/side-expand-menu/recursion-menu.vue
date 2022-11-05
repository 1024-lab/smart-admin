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
  <div class="resursion-container">
    <!-- 顶部顶级菜单名称 -->
    <div class="top-menu">
      <span class="ant-menu">{{ props.selectedMenu?.menuName }}</span>
    </div>
    <!-- 次级菜单展示 -->
    <a-menu :selectedKeys="selectedKeys" :openKeys="openKeys" mode="inline">
      <template v-for="item in props.selectedMenu?.children" :key="item.menuId">
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
  import { computed } from 'vue';
  import { useRoute } from 'vue-router';
  import { router } from '/@/router';
  import SubMenu from './sub-menu.vue';
  import { HOME_PAGE_NAME } from '/@/constants/system/home-const';
  import { useUserStore } from '/@/store/modules/system/user';
  import _ from 'lodash';

  let props = defineProps({
    selectedMenu: Object,
  });

  defineEmits('update:value');

  //展开的菜单
  let currentRoute = useRoute();
  const selectedKeys = computed(() => {
    return [currentRoute.name];
  });

  const parentMenuList = computed(() => {
    let currentName = currentRoute.name;
    if (!currentName || typeof currentName !== 'string') {
      return [];
    }
    let menuParentIdListMap = useUserStore().getMenuParentIdListMap;
    return menuParentIdListMap?.get(currentName) || [];
  });

  const openKeys = computed(() => {
    // // 仅展开当前页面
    // return parentMenuList.value.map((e) => e.name);
    // 展开所有
    let children = props.selectedMenu?.children;
    if (!children || _.isEmpty(children)) {
      return [];
    }
    return children.map((e) => e.menuId.toString());
  });
  // 页面跳转
  function turnToPage(route) {
    router.push({ name: route.menuId.toString() });
  }

  function goHome() {
    router.push({ name: HOME_PAGE_NAME });
  }
</script>
<style scoped lang="less">
  .resursion-container {
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
