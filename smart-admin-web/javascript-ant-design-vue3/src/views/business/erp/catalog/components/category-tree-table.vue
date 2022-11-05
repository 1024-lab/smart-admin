<!--
  * 目录 表格
  *
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2022-08-21 19:52:43
  * @Wechat:    zhuda1024
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
-->
<template>
  <a-card size="small" :bordered="false" :hoverable="true">
    <a-row class="smart-table-btn-block">
      <div class="smart-table-operate-block">
        <a-button @click="addCategory()" type="primary" size="small" v-privilege="`${privilegePrefix}Category:add`">
          <template #icon>
            <PlusOutlined />
          </template>
          新建
        </a-button>
      </div>
      <div class="smart-table-setting-block"></div>
    </a-row>

    <a-table
      :scroll="{ x: 1000 }"
      size="small"
      :dataSource="tableData"
      :columns="columns"
      rowKey="categoryId"
      bordered
      :pagination="false"
      @expandedRowsChange="changeExand"
      :expanded-row-keys="expandedRowKeys"
    >
      <template #bodyCell="{ record, column }">
        <template v-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button @click="addCategory(record.categoryId)" type="link" v-privilege="`${privilegePrefix}Category:addChild`">增加子分类</a-button>
            <a-button @click="addCategory(undefined, record)" type="link" v-privilege="`${privilegePrefix}Category:edit`">编辑</a-button>
            <a-button @click="confirmDeleteCategory(record.categoryId)" danger type="link" v-privilege="`${privilegePrefix}Category:delete`">删除</a-button>
          </div>
        </template>
      </template>
    </a-table>
    <CategoryFormModal ref="formModal" @reloadList="reloadList" />
  </a-card>
</template>
<script setup>
  import { reactive, ref, onMounted, computed } from 'vue';
  import { Modal, message } from 'ant-design-vue';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import CategoryFormModal from './category-form-modal.vue';
  import { categoryApi } from '/@/api/business/category/category-api';
  import { CATEGORY_TYPE_ENUM } from '/@/constants/business/erp/category-const';
  import { smartSentry } from '/@/lib/smart-sentry';

  const columnNameList = [
    {
      categoryType: CATEGORY_TYPE_ENUM.GOODS.value,
      columnName: '商品分类',
    },
    {
      categoryType: CATEGORY_TYPE_ENUM.DEMO.value,
      columnName: '演示分类',
    },
  ];
  const columName = computed(() => {
    let find = columnNameList.find((e) => e.categoryType == props.categoryType);
    return find ? find.columnName : '';
  });

  const props = defineProps({
    // 分组类型
    categoryType: Number,
    privilegePrefix: {
      type: String,
      default: 'goods'
    }
  });

  // ------------------------------ 查询 ------------------------------
  const tableLoading = ref(false);
  const tableData = ref([]);
  const columns = reactive([
    {
      title: columName,
      dataIndex: 'categoryName',
    },
    {
      title: '操作',
      dataIndex: 'action',
      width: 200,
    },
  ]);

  async function queryList() {
    try {
      tableLoading.value = true;
      let queryForm = {
        categoryType: props.categoryType,
      };
      let responseModel = await categoryApi.queryCategoryTree(queryForm);
      const list = responseModel.data;
      tableData.value = list;
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      tableLoading.value = false;
    }
  }

  const expandedRowKeys = ref([]);
  function reloadList(parentId) {
    queryList();
    if (parentId) {
      expandedRowKeys.value.push(parentId);
    }
  }

  onMounted(queryList);
  defineExpose({
    queryList,
  });

  function changeExand(val) {
    expandedRowKeys.value = val;
  }

  // ------------------------------ 添加 ------------------------------

  const formModal = ref();
  function addCategory(parentId, rowData) {
    formModal.value.showModal(props.categoryType, parentId, rowData);
  }

  // ------------------------------ 删除 ------------------------------

  function confirmDeleteCategory(categoryId) {
    Modal.confirm({
      title: '提示',
      content: '确定要删除当前分类吗?',
      okText: '确定',
      okType: 'danger',
      async onOk() {
        deleteCategory(categoryId);
      },
      cancelText: '取消',
      onCancel() {},
    });
  }
  async function deleteCategory(categoryId) {
    try {
      SmartLoading.show();
      await categoryApi.deleteCategoryById(categoryId);
      message.success('撤销成功');
      queryList();
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      SmartLoading.hide();
    }
  }
</script>
