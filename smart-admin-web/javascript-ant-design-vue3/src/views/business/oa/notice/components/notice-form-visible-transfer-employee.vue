<!--
  * 通知  可见范围 选择员工
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-08-21 19:52:43 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
-->
<template>
  <div class="wrapper">
    <div class="sider-left">
      <a-tree :tree-data="treeData" :fieldNames="{ title: 'name' }" :selectable="false" v-model:expandedKeys="expandedKeys">
        <template #switcherIcon="{ switcherCls }">
          <caret-down-outlined :class="switcherCls" />
        </template>
        <template #title="{ name, id, dataType }">
          <div class="list-item" :class="{ active: checkExists(id) }">
            <div class="list-item-title">
              <user-outlined v-if="dataType === NOTICE_VISIBLE_RANGE_DATA_TYPE_ENUM.EMPLOYEE.value" />
              {{ name }}
            </div>
            <check-circle-filled
              v-if="dataType === NOTICE_VISIBLE_RANGE_DATA_TYPE_ENUM.EMPLOYEE.value"
              class="check-icon-style"
              @click="onSelectAdd(name, id, dataType)"
            />
          </div>
        </template>
      </a-tree>
    </div>
    <div class="sider-right">
      <div class="selected-list">
        <div class="list-item" v-for="(item, index) in selectedList" :key="item.id">
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
import { employeeApi } from '/@/api/system/employee/employee-api';
import { smartSentry } from '/@/lib/smart-sentry';

const props = defineProps({
  // 已选择的员工数据列表
  employeeList: {
    type: Array,
    default: () => [],
  },
});

const emits = defineEmits(['onChange']);

const treeData = ref([]);

// 查询部门树形
async function queryDepartmentTree() {
  try {
    const departmentResult = await departmentApi.queryDepartmentTree();
    const employeeResult = await employeeApi.queryAll();
    const departmentTree = departmentResult.data;
    buildDepartmentEmployeeTree(departmentTree, employeeResult.data);

    if (!lodash.isEmpty(departmentTree)) {
      treeData.value = departmentTree;
      console.log(treeData.value);
      nextTick(() => {
        setExpanded();
      });
    }
  } catch (err) {
    smartSentry.captureError(err);
  }
}

// 递归构建部门员工树
function buildDepartmentEmployeeTree(departmentTree, employeeList) {
  for (const department of departmentTree) {
    if (department.dataType && department.dataType === NOTICE_VISIBLE_RANGE_DATA_TYPE_ENUM.EMPLOYEE.value) {
      continue;
    }

    department.id = department.departmentId;
    department.key = 'department_' + department.departmentId;
    department.dataType = NOTICE_VISIBLE_RANGE_DATA_TYPE_ENUM.DEPARTMENT.value;
    let employeeChildren = employeeList
      .filter((e) => e.departmentId === department.departmentId)
      .map((e) =>
        Object.assign(
          {},
          {
            id: e.employeeId,
            key: 'employee_' + e.employeeId,
            name: e.actualName,
            dataType: NOTICE_VISIBLE_RANGE_DATA_TYPE_ENUM.EMPLOYEE.value,
          }
        )
      );
    if (!department.children) {
      department.children = [];
    }
    department.children.push(...employeeChildren);
    buildDepartmentEmployeeTree(department.children, employeeList);
  }
}

// 设置默认展开的节点
const expandedKeys = ref([]);
function setExpanded() {
  expandedKeys.value = [treeData.value[0].key];
}

// 选择的员工列表数据
const selectedList = ref([]);

// 选择的员工列表Ids
const selectedIds = computed(() => {
  return selectedList.value.map((item) => item.dataId);
});

watch(
  () => props.employeeList,
  (newVal) => {
    selectedList.value = newVal;
  },
  { immediate: true }
);

// 检查是否已选
function checkExists(id) {
  return selectedIds.value.includes(id);
}

// 点击左边添加
function onSelectAdd(name, id, dataType) {
  if (checkExists(id)) {
    return;
  }
  selectedList.value.push({
    dataName: name,
    dataId: id,
    dataType: NOTICE_VISIBLE_RANGE_DATA_TYPE_ENUM.EMPLOYEE.value,
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
  .sider-left,
  .sider-right {
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
  .sider-right {
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
