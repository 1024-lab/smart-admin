<template>
  <a-form class="smart-query-form">
    <a-row class="smart-query-form-row">
      <a-form-item label="部门名称" class="smart-query-form-item">
        <a-input style="width: 300px" v-model:value="keywords" placeholder="请输入部门名称" />
      </a-form-item>

      <a-form-item class="smart-query-form-item smart-margin-left10">
        <a-button-group>
          <a-button v-privilege="'support:department:query'" type="primary" @click="onSearch">
            <template #icon>
              <ReloadOutlined />
            </template>
            查询
          </a-button>
          <a-button v-privilege="'support:department:query'" @click="resetQuery">
            <template #icon>
              <SearchOutlined />
            </template>
            重置
          </a-button>
        </a-button-group>
        <a-button v-privilege="'system:department:add'" type="primary" @click="addDepartment" class="smart-margin-left20">
          <template #icon>
            <PlusOutlined />
          </template>
          新建
        </a-button>
      </a-form-item>
    </a-row>
  </a-form>

  <a-card size="small" :bordered="true">
    <a-table
      size="small"
      bordered
      :loading="tableLoading"
      rowKey="departmentId"
      :columns="columns"
      :data-source="departmentTreeData"
      :defaultExpandAllRows="false"
      :defaultExpandedRowKeys="defaultExpandedRowList"
      :pagination="false"
    >
      <template #bodyCell="{ record, column }">
        <template v-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button @click="addDepartment(record)" v-privilege="'system:department:add'" type="link">添加下级</a-button>
            <a-button @click="updateDepartment(record)" v-privilege="'system:department:update'" type="link">编辑</a-button>
            <a-button
              danger
              v-if="record.departmentId !== topDepartmentId"
              v-privilege="'system:department:delete'"
              @click="deleteDepartment(record.departmentId)"
              type="link"
              >删除</a-button
            >
          </div>
        </template>
      </template>
    </a-table>
    <!-- 添加编辑部门弹窗 -->
    <DepartmentFormModal ref="departmentFormModal" @refresh="queryDepartmentTree" />
  </a-card>
</template>

<script setup>
  import { onMounted, reactive, ref, watch, createVNode } from 'vue';
  import { departmentApi } from '/src/api/system/department-api';
  import { Modal } from 'ant-design-vue';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import _ from 'lodash';
  import { SmartLoading } from '/src/components/framework/smart-loading';
  import DepartmentFormModal from './components/department-form-modal.vue';
  import { smartSentry } from '/src/lib/smart-sentry';

  const DEPARTMENT_PARENT_ID = 0;

  // -----------------------  筛选 ---------------------
  const keywords = ref('');

  // ----------------------- 部门树的展示 ---------------------
  const tableLoading = ref(false);

  const topDepartmentId = ref();
  // 所有部门列表
  const departmentList = ref([]);
  // 部门树形数据
  const departmentTreeData = ref([]);
  // 存放部门id和部门，用于查找
  const idInfoMap = ref(new Map());
  // 默认展开的行
  const defaultExpandedRowList = reactive([]);

  const columns = ref([
    {
      title: '部门名称',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: '负责人',
      dataIndex: 'managerName',
      key: 'managerName',
      width: 100,
    },
    {
      title: '排序',
      dataIndex: 'sort',
      key: 'sort',
      width: 100,
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      width: 150,
    },
    {
      title: '更新时间',
      dataIndex: 'updateTime',
      width: 150,
    },
    {
      title: '操作',
      dataIndex: 'action',
      fixed: 'right',
      width: 200,
    },
  ]);

  onMounted(() => {
    queryDepartmentTree();
  });

  // 查询部门列表并构建 部门树
  async function queryDepartmentTree() {
    try {
      tableLoading.value = true;
      let res = await departmentApi.queryAllDepartment();
      let data = res.data;

      data.forEach((e) => {
        idInfoMap.value.set(e.departmentId, e);
      });

      departmentList.value = data;
      departmentTreeData.value = buildDepartmentTree(data, DEPARTMENT_PARENT_ID);

      // 默认显示 最顶级ID为列表中返回的第一条数据的ID
      if (!_.isEmpty(departmentTreeData.value) && departmentTreeData.value.length > 0) {
        topDepartmentId.value = departmentTreeData.value[0].departmentId;
      }

      defaultExpandedRowList.value = [];
      defaultExpandedRowList.push(topDepartmentId.value);
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      tableLoading.value = false;
    }
  }

  // 构建部门树
  function buildDepartmentTree(data, parentId) {
    let children = data.filter((e) => e.parentId === parentId) || [];
    if (!_.isEmpty(children)) {
      children.forEach((e) => {
        e.children = buildDepartmentTree(data, e.departmentId);
      });
      return children;
    }
    return null;
  }

  // 重置
  function resetQuery() {
    keywords.value = '';
    onSearch();
  }

  // 搜索
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
    let filterDepartment = originData.filter((e) => e.name.indexOf(keywords.value) > -1);
    let filterDepartmentList = [];
    // 循环筛选出的部门 构建部门树
    filterDepartment.forEach((e) => {
      recursionFilterDepartment(filterDepartmentList, e.departmentId, false);
    });
    departmentTreeData.value = buildDepartmentTree(filterDepartmentList, DEPARTMENT_PARENT_ID);
  }

  // 根据ID递归筛选部门
  function recursionFilterDepartment(resList, id, unshift) {
    let info = idInfoMap.value.get(id);
    if (!info || resList.some((e) => e.departmentId === id)) {
      return;
    }
    if (unshift) {
      resList.unshift(info);
    } else {
      resList.push(info);
    }
    if (info.parentId && info.parentId !== 0) {
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
      parentId: e.departmentId || null,
    };
    departmentFormModal.value.showModal(data);
  }
  // 编辑
  function updateDepartment(e) {
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
          await departmentApi.deleteDepartment(id);
          await queryDepartmentTree();
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
</script>

<style scoped lang="less"></style>
