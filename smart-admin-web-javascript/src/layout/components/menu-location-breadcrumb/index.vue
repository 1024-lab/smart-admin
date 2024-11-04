<!--
  * 面包屑
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-09-06 20:29:12 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
-->
<template>
  <a-breadcrumb separator=">" v-if="breadCrumbFlag" class="breadcrumb">
    <a-breadcrumb-item v-for="(item, index) in parentMenuList" :key="index">{{ item.title }}</a-breadcrumb-item>
    <a-breadcrumb-item>{{ currentRoute.meta.title }}</a-breadcrumb-item>
  </a-breadcrumb>
</template>
<script setup>
  import { useRoute } from 'vue-router';
  import { useUserStore } from '/@/store/modules/system/user';
  import { computed } from 'vue';
  import { useAppConfigStore } from '/@/store/modules/system/app-config';

  // 是否显示面包屑
  const breadCrumbFlag = computed(() =>  useAppConfigStore().$state.breadCrumbFlag);

  let currentRoute = useRoute();
  //根据路由监听面包屑
  const parentMenuList = computed(() => {
    let currentName = currentRoute.name;
    if (!currentName || typeof currentName !== 'string') {
      return [];
    }
    let menuParentIdListMap = useUserStore().getMenuParentIdListMap;
    return menuParentIdListMap.get(currentName) || [];
  });
</script>
<style scoped lang="less">
.breadcrumb{
  line-height: @page-tag-height;
}
</style>