<!--
  * 部门树形结构
  *
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2022-08-08 20:46:18
  * @Wechat:    zhuda1024
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
-->
<template>
  <a-card class="tree-container">
    <a-row>
      <a-input v-model:value.trim="keywords" placeholder="请输入部门名称" />
    </a-row>
    <a-row class="sort-flag-row" v-if="props.showMenu">
      显示排序字段
      <template v-if="showSortFlag"> （值越大越靠前） </template>
      ：<a-switch v-model:checked="showSortFlag" />
    </a-row>
    <a-tree
      v-if="!_.isEmpty(departmentTreeData)"
      v-model:selectedKeys="selectedKeys"
      v-model:checkedKeys="checkedKeys"
      class="tree"
      :treeData="departmentTreeData"
      :fieldNames="{ title: 'name', key: 'departmentId', value: 'departmentId' }"
      style="width: 100%; overflow-x: auto"
      :style="[!height ? '' : { height: `${height}px`, overflowY: 'auto' }]"
      :showLine="!props.checkable"
      :checkable="props.checkable"
      :checkStrictly="props.checkStrictly"
      :selectable="!props.checkable"
      :defaultExpandAll="true"
      @select="treeSelectChange"
    >
      <template #title="item">
        <a-popover placement="right" v-if="props.showMenu">
          <template #content>
            <div style="display: flex; flex-direction: column">
              <a-button type="text" @click="addDepartment(item.dataRef)" v-privilege="'system:department:add'">添加下级</a-button>
              <a-button type="text" @click="updateDepartment(item.dataRef)" v-privilege="'system:department:update'">修改</a-button>
              <a-button
                type="text"
                v-if="item.departmentId != topDepartmentId"
                @click="deleteDepartment(item.departmentId)"
                v-privilege="'system:department:delete'"
                >删除</a-button
              >
            </div>
          </template>
          {{ item.name }}
          <!--显示排序字段-->
          <template v-if="showSortFlag">
            <span class="sort-span">({{ item.sort }})</span>
          </template>
        </a-popover>
        <div v-else>{{ item.name }}</div>
      </template>
    </a-tree>
    <div class="no-data" v-else>暂无结果</div>
    <!-- 添加编辑部门弹窗 -->
    <DepartmentFormModal ref="departmentFormModal" @refresh="refresh" />
  </a-card>
</template>
<script setup>
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { onUnmounted, ref, watch } from 'vue';
  import { Modal } from 'ant-design-vue';
  import _ from 'lodash';
  import { createVNode, onMounted } from 'vue';
  import DepartmentFormModal from '../department-form-modal/index.vue';
  import { departmentApi } from '/@/api/system/department/department-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import departmentEmitter from '/@/views/system/employee/department/department-mitt';
  import { smartSentry } from '/@/lib/smart-sentry';

  const DEPARTMENT_PARENT_ID = 0;

  // ----------------------- 组件参数 ---------------------

  const props = defineProps({
    // 是否可以选中
    checkable: {
      type: Boolean,
      default: false,
    },
    // 父子节点选中状态不再关联
    checkStrictly: {
      type: Boolean,
      default: false,
    },
    // 树高度 超出出滚动条
    height: Number,
    // 显示菜单
    showMenu: {
      type: Boolean,
      default: true,
    },
  });

  // ----------------------- 部门树的展示 ---------------------
  const topDepartmentId = ref();
  // 所有部门列表
  const departmentList = ref([]);
  // 部门树形数据
  const departmentTreeData = ref([]);
  // 存放部门id和部门，用于查找
  const idInfoMap = ref(new Map());
  // 是否显示排序字段
  const showSortFlag = ref(false);

  onMounted(() => {
    queryDepartmentTree();
  });

  // 刷新
  async function refresh() {
    await queryDepartmentTree();
    if (currentSelectedDpartmentId.value) {
      selectTree(currentSelectedDpartmentId.value);
    }
  }

  // 查询部门列表并构建 部门树
  async function queryDepartmentTree() {
    let res = await departmentApi.queryAllDepartment();
    let data = res.data;
    departmentList.value = data;
    departmentTreeData.value = buildDepartmentTree(data, DEPARTMENT_PARENT_ID);

    data.forEach((e) => {
      idInfoMap.value.set(e.departmentId, e);
    });

    // 默认显示 最顶级ID为列表中返回的第一条数据的ID
    if (!_.isEmpty(departmentTreeData.value) && departmentTreeData.value.length > 0) {
      topDepartmentId.value = departmentTreeData.value[0].departmentId;
    }

    selectTree(departmentTreeData.value[0].departmentId);
  }

  // 构建部门树
  function buildDepartmentTree(data, parentId) {
    let children = data.filter((e) => e.parentId === parentId) || [];
    children.forEach((e) => {
      e.children = buildDepartmentTree(data, e.departmentId);
    });
    updateDepartmentPreIdAndNextId(children);
    return children;
  }

  // 更新树的前置id和后置id
  function updateDepartmentPreIdAndNextId(data) {
    for (let index = 0; index < data.length; index++) {
      if (index === 0) {
        data[index].nextId = data.length > 1 ? data[1].departmentId : undefined;
        continue;
      }

      if (index === data.length - 1) {
        data[index].preId = data[index - 1].departmentId;
        data[index].nextId = undefined;
        continue;
      }

      data[index].preId = data[index - 1].departmentId;
      data[index].nextId = data[index + 1].departmentId;
    }
  }

  // ----------------------- 树的选中 ---------------------
  const selectedKeys = ref([]);
  const checkedKeys = ref([]);
  const breadcrumb = ref([]);
  const currentSelectedDpartmentId = ref();
  const selectedDepartmentChildren = ref([]);

  departmentEmitter.on('selectTree', selectTree);

  function selectTree(id) {
    selectedKeys.value = [id];
    treeSelectChange(selectedKeys.value);
  }

  function treeSelectChange(idList) {
    if (_.isEmpty(idList)) {
      breadcrumb.value = [];
      selectedDepartmentChildren.value = [];
      return;
    }
    let id = idList[0];
    selectedDepartmentChildren.value = departmentList.value.filter((e) => e.parentId == id);
    let filterDepartmentList = [];
    recursionFilterDepartment(filterDepartmentList, id, true);
    breadcrumb.value = filterDepartmentList.map((e) => e.name);
  }

  // -----------------------  筛选 ---------------------
  const keywords = ref('');
  watch(
    () => keywords.value,
    () => {
      onSearch();
    }
  );

  // 筛选
  function onSearch() {
    if (!keywords.value) {
      departmentTreeData.value = buildDepartmentTree(departmentList.value, DEPARTMENT_PARENT_ID);
      return;
    }
    let originData = departmentList.value.concat();
    if (!originData) {
      return;
    }
    // 筛选出名称符合的部门
    let filterDepartmenet = originData.filter((e) => e.name.indexOf(keywords.value) > -1);
    let filterDepartmentList = [];
    // 循环筛选出的部门 构建部门树
    filterDepartmenet.forEach((e) => {
      recursionFilterDepartment(filterDepartmentList, e.departmentId, false);
    });

    departmentTreeData.value = buildDepartmentTree(filterDepartmentList, DEPARTMENT_PARENT_ID);
  }

  // 根据ID递归筛选部门
  function recursionFilterDepartment(resList, id, unshift) {
    let info = idInfoMap.value.get(id);
    if (!info || resList.some((e) => e.departmentId == id)) {
      return;
    }
    if (unshift) {
      resList.unshift(info);
    } else {
      resList.push(info);
    }
    if (info.parentId && info.parentId != 0) {
      recursionFilterDepartment(resList, info.parentId, unshift);
    }
  }

  // ----------------------- 表单操作：添加部门/修改部门/删除部门/上下移动 ---------------------
  const departmentFormModal = ref();

  // 添加
  function addDepartment(e) {
    let data = {
      departmentId: 0,
      name: '',
      parentId: e.departmentId,
    };
    currentSelectedDpartmentId.value = e.departmentId;
    departmentFormModal.value.showModal(data);
  }
  // 编辑
  function updateDepartment(e) {
    currentSelectedDpartmentId.value = e.departmentId;
    departmentFormModal.value.showModal(e);
  }

  // 删除
  function deleteDepartment(id) {
    Modal.confirm({
      title: '提醒',
      icon: createVNode(ExclamationCircleOutlined),
      content: '确定要删除该部门吗?',
      okText: '删除',
      okType: 'danger',
      async onOk() {
        SmartLoading.show();
        try {
          // 若删除的是当前的部门 先找到上级部门
          let selectedKey = null;
          if (!_.isEmpty(selectedKeys.value)) {
            selectedKey = selectedKeys.value[0];
            if (selectedKey == id) {
              let selectInfo = departmentList.value.find((e) => e.departmentId == id);
              if (selectInfo && selectInfo.parentId) {
                selectedKey = selectInfo.parentId;
              }
            }
          }
          await departmentApi.deleteDepartment(id);
          await queryDepartmentTree();
          // 刷新选中部门
          if (selectedKey) {
            selectTree(selectedKey);
          }
        } catch (error) {
          smartSentry.captureError(error);
        } finally {
          SmartLoading.hide();
        }
      },
      cancelText: '取消',
      onCancel() {},
    });
  }

  onUnmounted(() => {
    departmentEmitter.all.clear();
  });

  // ----------------------- 以下是暴露的方法内容 ----------------------------
  defineExpose({
    queryDepartmentTree,
    selectedDepartmentChildren,
    breadcrumb,
    selectedKeys,
    checkedKeys,
    keywords,
  });
</script>
<style scoped lang="less">
  .tree-container {
    height: 100%;
    .tree {
      height: 618px;
      margin-top: 10px;
      overflow-x: hidden;
    }

    .sort-flag-row {
      margin-top: 10px;
      margin-bottom: 10px;
    }

    .sort-span {
      margin-left: 5px;
      color: @success-color;
    }
    .no-data {
      margin: 10px;
    }
  }
</style>
