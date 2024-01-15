<!--
  * 代码生成 查询条件
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-09-22 21:50:41 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
-->
<template>
  <a-row>
    <a-button type="primary" @click="addQuery">添加查询条件</a-button>
  </a-row>
  <a-table
    size="small"
    bordered
    id="smartCodeQueryFieldsTable"
    class="smart-margin-top10"
    :dataSource="tableData"
    row-class-name="column-row"
    :columns="columns"
    rowKey="rowKey"
    :pagination="false"
  >
    <template #bodyCell="{ record, index, column }">
      <template v-if="column.dataIndex === 'drag'">
        <a-button type="text" class="handle" size="small" style="width: 100%; text-align: left">
          <template #icon> <drag-outlined /> </template>
        </a-button>
      </template>

      <template v-if="column.dataIndex === 'label'">
        <a-input v-model:value="record.label" placeholder="关键字查询" />
      </template>

      <template v-if="column.dataIndex === 'fieldName'">
        <a-input v-model:value="record.fieldName" placeholder="keywords" />
      </template>

      <template v-if="column.dataIndex === 'width'">
        <a-input v-model:value="record.width" placeholder="150px" />
      </template>

      <template v-if="column.dataIndex === 'queryTypeEnum'">
        <SmartEnumSelect
          @change="(value) => onChangeQueryType(value, record)"
          enumName="CODE_QUERY_FIELD_QUERY_TYPE_ENUM"
          v-model:value="record.queryTypeEnum"
          width="100%"
        />
      </template>

      <template v-if="column.dataIndex === 'columnNameList'">
        <a-select
          show-search
          :mode="record.queryTypeEnum && record.queryTypeEnum === CODE_QUERY_FIELD_QUERY_TYPE_ENUM.LIKE.value ? 'multiple' : ''"
          v-model:value="record.columnNameList"
          @change="onSelectColumn(record)"
          style="width: 100%"
        >
          <a-select-option v-for="item in tableColumns" :value="item.columnName" :key="item.columnName">
            {{ item.columnName }}
            <span v-show="item.columnComment"> （{{ item.columnComment }}） </span>
          </a-select-option>
        </a-select>
      </template>

      <template v-if="column.dataIndex === 'operate'">
        <div class="smart-table-operate">
          <a-button type="link" @click="onDelete(index)" danger>删除</a-button>
        </div>
      </template>
    </template>
  </a-table>
</template>

<script setup>
  import _ from 'lodash';
  import Sortable from 'sortablejs';
  import { inject, nextTick, ref } from 'vue';
  import SmartEnumSelect from '/@/components/framework/smart-enum-select/index.vue';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { CODE_QUERY_FIELD_QUERY_TYPE_ENUM } from '/@/constants/support/code-generator-const';
  import { convertLowerCamel } from '/@/utils/str-util';

  //------------------------ 全局数据 ---------------------
  const tableInfo = inject('tableInfo');

  //------------------------ 表格渲染 ---------------------

  const columns = ref([
    {
      title: '拖拽',
      dataIndex: 'drag',
      width: 60,
    },
    {
      title: '查询类型',
      dataIndex: 'queryTypeEnum',
      width: 130,
    },
    {
      title: '查询列',
      dataIndex: 'columnNameList',
    },

    {
      title: '条件名称',
      dataIndex: 'label',
      width: 150,
    },
    {
      title: '字段命名',
      dataIndex: 'fieldName',
      width: 150,
    },
    {
      title: '宽度',
      dataIndex: 'width',
      width: 150,
    },
    {
      title: '操作',
      dataIndex: 'operate',
      width: 60,
    },
  ]);

  const tableData = ref([]);

  const tableColumns = ref([]);

  //初始化设置数据
  function setData(tableColumnInfos, config) {
    rowKeyCounter = 1;
    let data = config && config.queryFields ? config.queryFields : [];
    for (let index = 0; index < data.length; index++) {
      data[index].rowKey = 'rowKey' + (index + 1);
      rowKeyCounter++;
    }
    tableData.value = data;
    tableColumns.value = tableColumnInfos;

    nextTick(() => {
      initDrag();
    });
  }

  // -------------------  增加、删除 -------------------
  let rowKeyCounter = 1;
  function addQuery() {
    tableData.value.push({
      rowKey: 'rowKey' + rowKeyCounter,
      label: '',
      fieldName: '',
      queryTypeEnum: '',
      columnNameList: null,
      width: '',
    });
    rowKeyCounter++;
  }

  function onDelete(index) {
    _.pullAt(tableData.value, index);
  }

  //初始化拖拽
  function initDrag() {
    let tbody = document.querySelector('#smartCodeQueryFieldsTable tbody');
    Sortable.create(tbody, {
      animation: 300,
      dragClass: 'smart-ghost-class', //设置拖拽样式类名
      ghostClass: 'smart-ghost-class', //设置拖拽停靠样式类名
      chosenClass: 'smart-ghost-class', //设置选中样式类名
      handle: '.handle',
    });
  }

  // -------------------  监听数据变化 -------------------
  function onChangeQueryType(queryType, record) {
    if (queryType === CODE_QUERY_FIELD_QUERY_TYPE_ENUM.LIKE.value) {
      record.columnNameList = [];
    } else {
      record.columnNameList = null;
    }
  }

  function onSelectColumn(record) {
    if (Array.isArray(record.columnNameList)) {
      return;
    }
    let columnName = record.columnNameList;
    let column = getConfigField(tableColumns.value, columnName);

    //表单
    record.fieldName = column && column.columnName ? convertLowerCamel(column.columnName) : '';
    record.label = column && column.columnComment ? convertLowerCamel(column.columnComment) : '';
  }

  // 获取配置过的字段信息
  function getConfigField(configFields, columnName) {
    if (!configFields) {
      return null;
    }

    let result = configFields.filter((e) => e.columnName === columnName);
    return result && result.length > 0 ? result[0] : null;
  }

  // -------------------  获取表单数据 -------------------

  // 获取表单数据
  function getFieldsForm() {
    let result = [];
    let trList = document.querySelectorAll('#smartCodeQueryFieldsTable tbody .column-row');
    if (trList && trList.length === 0) {
      return result;
    }

    for (let tr of trList) {
      let rowKey = tr.getAttribute('data-row-key');
      if (!rowKey) {
        continue;
      }
      if (rowKey && rowKey.indexOf('rowKey') === -1) {
        continue;
      }

      let index = parseInt(rowKey.substring(6));
      let tableItem = tableData.value[index - 1];
      let obj = {
        queryTypeEnum: tableItem.queryTypeEnum,
        label: tableItem.label,
        fieldName: tableItem.fieldName,
        columnNameList: tableItem.columnNameList,
        width: tableItem.width,
      };
      // 字符串转为数组
      if (obj.columnNameList && typeof obj.columnNameList === 'string') {
        obj.columnNameList = [obj.columnNameList];
      }
      result.push(obj);
    }
    return result;
  }

  defineExpose({
    setData,
    getFieldsForm,
  });
</script>

<style lang="less" scoped></style>
