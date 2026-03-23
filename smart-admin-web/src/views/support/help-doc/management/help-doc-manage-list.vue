<!--
  * 帮助文档 管理
  * 
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2022-07-21 21:55:12
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
-->
<template>
  <div class="height100">
    <a-row :gutter="16" class="height100">
      <a-col :span="6">
        <HelpDocCatalogTree ref="helpDocCatalogTreeRef" />
      </a-col>

      <a-col :span="18" class="height100">
        <div class="help-doc-box height100">
          <HelpDocList :helpDocCatalogId="selectedHelpDocCatalogId" />
        </div>
      </a-col>
    </a-row>
  </div>
</template>
<script setup>
  import _ from 'lodash';
  import { computed, ref } from 'vue';
  import HelpDocCatalogTree from './components/help-doc-catalog-tree.vue';
  import HelpDocList from './components/help-doc-list.vue';

  const helpDocCatalogTreeRef = ref();

  // 当前选中的目录id
  const selectedHelpDocCatalogId = computed(() => {
    if (helpDocCatalogTreeRef.value) {
      let selectedKeys = helpDocCatalogTreeRef.value.selectedKeys;
      return _.isEmpty(selectedKeys) ? null : selectedKeys[0];
    }
    return null;
  });
</script>
<style scoped lang="less">
  .height100 {
    height: 100%;
  }
  .help-doc-box {
    display: flex;
    flex-direction: column;

    .employee {
      flex-grow: 2;
      margin-top: 10px;
    }
  }
</style>
