<!--
  * 组织架构
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-08-08 20:46:18
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
-->
<template>
  <div class="height100">
    <a-row :gutter="16" class="height100">
      <a-col :span="6">
        <DepartmentTree ref="departmentTree" />
      </a-col>

      <a-col :span="18" class="height100">
        <div class="employee-box height100">
          <!-- <DepartmentChildren style="flex-grow: 1" :breadcrumb="breadcrumb" :selectedDepartmentChildren="selectedDepartmentChildren" /> -->
          <EmployeeList style="flex-grow: 2.5" class="employee" :departmentId="selectedDepartmentId" />
        </div>
      </a-col>
    </a-row>
  </div>
</template>
<script setup>
  import _ from 'lodash';
  import { computed, ref } from 'vue';
  import DepartmentChildren from './components/department-children/index.vue';
  import DepartmentTree from './components/department-tree/index.vue';
  import EmployeeList from './components/employee-list/index.vue';

  const departmentTree = ref();

  // 部门 面包屑
  const breadcrumb = computed(() => {
    if (departmentTree.value) {
      return departmentTree.value.breadcrumb;
    }
    return [];
  });

  // 当前选中部门的孩子
  const selectedDepartmentChildren = computed(() => {
    if (departmentTree.value) {
      return departmentTree.value.selectedDepartmentChildren;
    }
    return [];
  });

  // 当前选中的部门id
  const selectedDepartmentId = computed(() => {
    if (departmentTree.value) {
      let selectedKeys = departmentTree.value.selectedKeys;
      return _.isEmpty(selectedKeys) ? null : selectedKeys[0];
    }
    return null;
  });
</script>
<style scoped lang="less">
  .height100 {
    height: 100%;
  }
  .employee-box {
    display: flex;
    flex-direction: column;

    .employee {
      flex-grow: 2;
    }
  }
</style>
