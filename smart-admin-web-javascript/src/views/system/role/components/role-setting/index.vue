<!--
  * 角色 设置
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-09-12 22:34:00 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
  *
-->
<template>
  <a-card class="role-container">
    <a-tabs v-model:activeKey="activeKey">
      <a-tab-pane key="1" tab="角色-功能权限">
        <RoleTree />
      </a-tab-pane>
      <a-tab-pane key="2" tab="角色-数据范围">
        <RoleDataScope />
      </a-tab-pane>
      <a-tab-pane key="3" tab="角色-员工列表">
        <RoleEmployeeList />
      </a-tab-pane>
    </a-tabs>
  </a-card>
</template>
<script setup>
  import { ref } from 'vue';
  import RoleDataScope from '../role-data-scope/index.vue';
  import RoleEmployeeList from '../role-employee-list/index.vue';
  import RoleTree from '../role-tree/index.vue';

  defineProps({
    value: Number,
  });

  defineEmits(['update:value']);

  let activeKey = ref();
</script>
<style scoped lang="less">
  .role-container {
    height: 100%;
  }
</style>
