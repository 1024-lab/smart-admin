<!--
  * 角色 管理
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-09-12 22:34:00 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
  *
-->
<template>
  <div class="height100">
    <a-row :gutter="10" type="flex" class="height100">
      <a-col flex="200px">
        <RoleList ref="roleList" />
      </a-col>
      <a-col flex="1">
        <RoleSetting />
      </a-col>
    </a-row>
  </div>
</template>
<script setup>
  import { computed, provide, ref } from 'vue';
  import RoleList from './components/role-list/index.vue';
  import RoleSetting from './components/role-setting/index.vue';
  defineProps({
    value: Object,
  });
  defineEmits('update:value');

  let roleList = ref();
  const selectRoleId = computed(() => {
    if (!roleList.value) {
      return null;
    }
    return roleList.value.selectRoleId;
  });
  provide('selectRoleId', selectRoleId);
</script>
<style scoped lang="less">
  .height100 {
    height: 100%;
  }
</style>
