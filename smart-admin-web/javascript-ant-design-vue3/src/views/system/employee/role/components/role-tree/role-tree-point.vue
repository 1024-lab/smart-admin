<!--
  * 角色 功能点
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-09-12 22:34:00 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
  *
-->
<template>
  <div class="point">
    <div class="point-label">
      <template v-for="module in props.tree" :key="module.menuId">
        <a-checkbox @change="emits('selectCheckbox', module)" :value="module.menuId">{{ module.menuName }} </a-checkbox>
      </template>
    </div>
  </div>
</template>
<script setup>
  const props = defineProps({
    tree: {
      type: Array,
      default: [],
    },
    index: {
      type: Number,
      default: 0,
    },
  });
  let emits = defineEmits('selectCheckbox');
</script>
<style scoped lang="less"></style>
