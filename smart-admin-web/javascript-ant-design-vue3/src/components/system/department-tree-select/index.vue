<!-- 
  * 部门 树形选择框

  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-09-12 23:05:43 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
  *
-->
<template>
  <a-tree-select
    :value="props.value"
    :treeData="treeData"
    :fieldNames="{ label: 'name', key: 'departmentId', value: 'departmentId' }"
    show-search
    style="width: 100%"
    :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
    placeholder="请选择部门"
    allow-clear
    tree-default-expand-all
    :multiple="props.multiple"
    @change="onChange"
  />
</template>
<script setup>
  import { onMounted, ref } from 'vue';
  import _ from 'lodash';
  import { departmentApi } from '/@/api/system/department/department-api';

  const props = defineProps({
    // 绑定值
    value: Number,
    // 单选多选
    multiple: {
      type: Boolean,
      default: false,
    },
  });

  const emit = defineEmits(['update:value']);

  let treeData = ref([]);
  onMounted(queryDepartmentTree);

  async function queryDepartmentTree() {
    let res = await departmentApi.queryDepartmentTree();
    treeData.value = res.data;
  }

  function onChange(e) {
    emit('update:value', e);
  }

  defineExpose({
    queryDepartmentTree,
  });
</script>
