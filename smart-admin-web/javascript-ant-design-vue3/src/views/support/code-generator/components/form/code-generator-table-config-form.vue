<!--
  * 代码生成 配置信息
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-09-22 21:50:41 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
-->
<template>
  <a-drawer
    title="代码配置"
    style=""
    :open="visibleFlag"
    :width="1000"
    :footerStyle="{ textAlign: 'right' }"
    @close="onClose"
    :maskClosable="false"
    :destroyOnClose="true"
  >
    <a-tabs v-model:activeKey="activeKey">
      <a-tab-pane key="1" :forceRender="true">
        <template #tab>
          <span>
            <info-circle-outlined />
            1.基础命名
          </span>
        </template>
        <CodeGeneratorTableConfigFormBasic ref="basicRef" />
      </a-tab-pane>
      <a-tab-pane key="2" :forceRender="true">
        <template #tab>
          <span>
            <unordered-list-outlined />
            2.字段列表
          </span>
        </template>
        <CodeGeneratorTableConfigFormField ref="fieldRef" />
      </a-tab-pane>
      <a-tab-pane key="3" :forceRender="true">
        <template #tab>
          <span>
            <save-outlined />
            3.增加、修改
          </span>
        </template>
        <CodeGeneratorTableConfigFormInsertAndUpdate ref="insertAndUpdateRef" />
      </a-tab-pane>
      <a-tab-pane key="4" :forceRender="true">
        <template #tab>
          <span>
            <delete-outlined />
            4.删除
          </span>
        </template>
        <CodeGeneratorTableConfigFormDelete ref="deleteRef" />
      </a-tab-pane>
      <a-tab-pane key="5" :forceRender="true">
        <template #tab>
          <span>
            <file-search-outlined />
            5、查询条件
          </span>
        </template>
        <CodeGeneratorTableConfigFormQueryField ref="queryRef" />
      </a-tab-pane>
      <a-tab-pane key="6" :forceRender="true">
        <template #tab>
          <span>
            <table-outlined />
            6、列表
          </span>
        </template>
        <CodeGeneratorTableConfigFormTableField ref="tableFieldRef" />
      </a-tab-pane>
    </a-tabs>

    <template #footer>
      <a-space>
        <a-button @click="onClose">取消</a-button>
        <a-button type="primary" @click="save">保存</a-button>
      </a-space>
    </template>
  </a-drawer>
</template>

<script setup>
  import { reactive, ref, provide, nextTick } from 'vue';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { smartSentry } from '/@/lib/smart-sentry';
  import CodeGeneratorTableConfigFormBasic from './code-generator-table-config-form-basic.vue';
  import { codeGeneratorApi } from '/@/api/support/code-generator-api';
  import CodeGeneratorTableConfigFormField from './code-generator-table-config-form-field.vue';
  import CodeGeneratorTableConfigFormInsertAndUpdate from './code-generator-table-config-form-insert-and-update.vue';
  import CodeGeneratorTableConfigFormDelete from './code-generator-table-config-form-delete.vue';
  import CodeGeneratorTableConfigFormQueryField from './code-generator-table-config-form-query-field.vue';
  import CodeGeneratorTableConfigFormTableField from './code-generator-table-config-form-table-field.vue';
  import { message } from 'ant-design-vue';

  // ------------------ 显示，关闭 ------------------
  // 显示
  const visibleFlag = ref(false);
  function showModal(table) {
    Object.assign(tableInfo, table);
    activeKey.value = '1';
    visibleFlag.value = true;
    nextTick(() => {
      getTableColumns();
    });
  }

  // 关闭
  function onClose() {
    visibleFlag.value = false;
  }

  // ------------------ 组件------------------
  const basicRef = ref();
  const fieldRef = ref();
  const insertAndUpdateRef = ref();
  const deleteRef = ref();
  const queryRef = ref();
  const tableFieldRef = ref();

  // ------------------ 表的列信息 、配置信息------------------
  const tableColumns = ref([]);
  const tableConfig = ref({});

  // 查询表的列
  async function getTableColumns() {
    try {
      SmartLoading.show();
      let columnResult = await codeGeneratorApi.getTableColumns(tableInfo.tableName);
      tableColumns.value = columnResult.data;

      let configResult = await codeGeneratorApi.getConfig(tableInfo.tableName);
      tableConfig.value = configResult.data;

      //基础命名
      basicRef.value.setData(tableConfig.value);
      //字段列表
      fieldRef.value.setData(tableColumns.value, tableConfig.value);
      //新增和修改
      insertAndUpdateRef.value.setData(tableColumns.value, tableConfig.value);
      //删除
      deleteRef.value.setData(tableColumns.value, tableConfig.value);
      //查询
      queryRef.value.setData(tableColumns.value, tableConfig.value);
      //表格列表
      tableFieldRef.value.setData(tableColumns.value, tableConfig.value);
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      SmartLoading.hide();
    }
  }

  // ------------------ 表信息 ------------------
  const tableInfo = reactive({
    tableName: '', //表名
    tableComment: '', //备注
    createTime: '', //表创建时间
    updateTime: '', //表修改时间
  });

  // ------------------ 标签页 ------------------
  const activeKey = ref('1');

  // ------------------ 提交表单 ------------------
  const emits = defineEmits(['reloadList']);
  async function save() {
    SmartLoading.show();
    try {
      let basicValidated = await basicRef.value.validateForm();
      let insertAndUpdateValidated = await insertAndUpdateRef.value.validateForm();
      let deleteValidated = await deleteRef.value.validateForm();

      if (!basicValidated || !insertAndUpdateValidated || !deleteValidated ) {
        return;
      }

      let fields = fieldRef.value.getFieldsForm();
      let basic = basicRef.value.getBasicForm();
      let insertAndUpdate = insertAndUpdateRef.value.getFieldsForm();
      let deleteInfo = deleteRef.value.getForm();
      let queryFields = queryRef.value.getFieldsForm();
      let tableFields = tableFieldRef.value.getTaleFieldsForm();

      await codeGeneratorApi.updateConfig({
        tableName: tableInfo.tableName,
        basic,
        fields,
        insertAndUpdate,
        deleteInfo: deleteInfo,
        queryFields,
        tableFields,
      });

      message.success('保存成功');
      emits('reloadList');
      onClose();
    } catch (e) {
      smartSentry.captureError(e);
    }finally{
      SmartLoading.hide();
    }
  }

  defineExpose({
    showModal,
  });

  // ------------------ provide ------------------

  provide('tableInfo', tableInfo);
  provide('tableColumns', tableColumns);
  provide('tableConfig', tableConfig);
</script>

<style lang="less" scoped>
  .visible-list {
    display: flex;
    flex-wrap: wrap;
    .visible-item {
      padding-top: 8px;
    }
  }
</style>
