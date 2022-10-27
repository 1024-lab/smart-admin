<!--
  *  目录 树形选择组件
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-08-12 21:01:52 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
  *
-->
<template>
  <a-tree-select
    v-model:value="selectValue"
    :style="`width:${width}`"
    :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
    :tree-data="categoryTreeData"
    :placeholder="placeholder"
    tree-default-expand-all
    @change="onChange"
  />
</template>

<script setup>
  import { ref, watch, onMounted } from 'vue';
  import { categoryApi } from '/@/api/business/category/category-api';
  import { smartSentry } from '/@/lib/smart-sentry';

  const props = defineProps({
    value: Number,
    placeholder: {
      type: String,
      default: '请选择',
    },
    categoryType: Number,
    width: {
      type: String,
      default: '100%',
    },
  });

  const emit = defineEmits(['update:value', 'change']);

  // -----------------  查询 目录 数据 -----------------
  const categoryTreeData = ref([]);
  async function queryCategoryTree() {
    if (!props.categoryType) {
      categoryTreeData.value = [];
      return;
    }
    try {
      let param = {
        categoryType: props.categoryType,
      };
      let resp = await categoryApi.queryCategoryTree(param);
      categoryTreeData.value = resp.data;
    } catch (e) {
      smartSentry.captureError(e);
    }
  }

  // -----------------  选中相关监听、事件 -----------------
  const selectValue = ref(props.value);
  // 箭头value变化
  watch(
    () => props.value,
    (newValue) => {
      selectValue.value = newValue;
    }
  );

  // 监听类型变化
  watch(
    () => props.categoryType,
    () => {
      queryCategoryTree();
    }
  );

  function onChange(value) {
    emit('update:value', value);
    emit('change', value);
  }

  onMounted(queryCategoryTree);
</script>
