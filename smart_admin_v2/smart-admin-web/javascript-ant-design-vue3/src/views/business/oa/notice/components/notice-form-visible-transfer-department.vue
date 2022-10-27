<!--
  * 通知  可见范围 选择部门
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-08-21 19:52:43 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
-->
<template>
  <div class="wrapper">
    <div class="sider-fl">
      <a-tree :tree-data="treeData" :fieldNames="{ title: 'name', key: 'departmentId' }" :selectable="false" v-model:expandedKeys="expandedKeys">
        <template #switcherIcon="{ switcherCls }">
          <caret-down-outlined :class="switcherCls" />
        </template>
        <template #title="{ name, departmentId }">
          <div class="list-item" :class="{ active: checkExists(departmentId) }">
            <div class="list-item-title">{{ name }}</div>
            <check-circle-filled class="check-icon-style" @click="onSelectAdd(name, departmentId)" />
          </div>
        </template>
      </a-tree>
    </div>
    <div class="sider-fr">
      <div class="selected-list">
        <div class="list-item" v-for="(item, index) in selectedList" :key="item.dataId">
          <div class="list-item-title">{{ item.dataName }}</div>
          <close-circle-two-tone @click="onRemove(index)" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, watch, computed, nextTick } from 'vue';
import lodash from 'lodash';
import { NOTICE_VISIBLE_RANGE_DATA_TYPE_ENUM } from '/@/constants/business/oa/notice-const';
import { departmentApi } from '/@/api/system/department/department-api';
import { smartSentry } from '/@/lib/smart-sentry';

const props = defineProps({
  // 已选择的部门数据列表
  departmentList: {
    type: Array,
    default: () => [],
  },
});

const emits = defineEmits(['onChange']);

const treeData = ref([]);
async function queryDepartmentTree() {
  try {
    const result = await departmentApi.queryDepartmentTree();
    if (!lodash.isEmpty(result.data)) {
      treeData.value = result.data;
      setExpanded();
    }
  } catch (err) {
    smartSentry.captureError(err);
  }
}

// 设置默认展开的节点
const expandedKeys = ref([]);
function setExpanded() {
  expandedKeys.value = [treeData.value[0].departmentId];
}

// 选择的部门列表数据
const selectedList = ref([]);

// 选择的部门列表Ids
const selectedIds = computed(() => {
  return selectedList.value.map((item) => item.dataId);
});

watch(
  () => props.departmentList,
  (newVal) => {
    selectedList.value = newVal;
  },
  { immediate: true }
);

// 检查是否已选
function checkExists(dataId) {
  return selectedIds.value.includes(dataId);
}

// 点击左边添加
function onSelectAdd(name, departmentId) {
  if (checkExists(departmentId)) {
    return;
  }
  selectedList.value.push({
    dataName: name,
    dataId: departmentId,
    dataType: NOTICE_VISIBLE_RANGE_DATA_TYPE_ENUM.DEPARTMENT.value,
  });
  onChangeEmit();
}

// 点击右边移除
function onRemove(index) {
  selectedList.value.splice(index, 1);
  onChangeEmit();
}

function onChangeEmit() {
  emits('onChange', { selectedList: selectedList.value, selectedIds: selectedIds.value });
}

onMounted(() => {
  queryDepartmentTree();
});
</script>

<style lang="less" scoped>
:deep(.ant-tree-list-holder-inner) {
  display: block !important;
  .ant-tree-treenode {
    align-items: center;
    padding-bottom: 0;
    &:hover {
      background-color: #f9f9f9;
    }
    .ant-tree-switcher {
      display: flex;
      align-items: center;
      justify-content: center;
      .ant-tree-switcher-icon {
        font-size: 12px;
      }
    }
    .ant-tree-node-content-wrapper {
      display: block;
      flex: 1;
      &:hover {
        cursor: auto;
      }
      .ant-tree-title {
        display: block;
      }
    }
  }
}

.wrapper {
  display: flex;
  .sider-fl,
  .sider-fr {
    flex: 1;
    height: 500px;
    border: 1px solid #d9d9d9;
    overflow: hidden;
    overflow-y: auto;
    &::-webkit-scrollbar {
      width: 6px;
    }
    &::-webkit-scrollbar-track-piece {
      background-color: #ededed;
    }
    &::-webkit-scrollbar-thumb {
      height: 50px;
      background-color: #a1a1a1;
      border-radius: 4px;
    }
  }
  .sider-fr {
    margin-left: 15px;
    .list-item {
      padding-left: 14px;
    }
  }
}
.list-item {
  display: flex;
  align-items: center;
  padding: 0 14px 0 0;
  height: 32px;
  &:hover {
    background-color: #f9f9f9;
  }
  &.active {
    .check-icon-style {
      cursor: auto;
      color: @primary-color;
    }
  }
  .list-item-title {
    flex: 1;
    margin-right: 10px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  .check-icon-style {
    color: #d9d9d9;
  }
}
</style>
