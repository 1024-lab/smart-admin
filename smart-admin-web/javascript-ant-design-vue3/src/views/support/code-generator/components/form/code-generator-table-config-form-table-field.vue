<!--
  * 代码生成 列表
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-09-22 21:50:41 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
-->
<template>
  <a-alert :closable="true" message="请务必将每一个字段的 “ 字段名词 ” 填写完整！！！" type="success" show-icon>
    <template #icon><smile-outlined /></template>
  </a-alert>
  <a-table size="small" bordered  :scroll="{ x: 1000 }" class="smart-margin-top10" :dataSource="tableData" :columns="columns" rowKey="columnName" :pagination="false">
    <template #bodyCell="{ text, record, index, column }">
      <template v-if="column.dataIndex === 'no'">
        {{ index + 1 }}
      </template>

      <template v-if="column.dataIndex === 'showFlag'">
        <a-checkbox v-model:checked="record.showFlag" />
      </template>

      <template v-if="column.dataIndex === 'fieldName'">
        <a-input v-model:value="record.fieldName" />
      </template>

      <template v-if="column.dataIndex === 'label'">
        <a-input v-model:value="record.label" />
      </template>

      <template v-if="column.dataIndex === 'width'">
        <a-input-number v-model:value="record.width" />
      </template>

      <template v-if="column.dataIndex === 'ellipsisFlag'">
        <a-switch v-model:checked="record.ellipsisFlag" checked-children="自动省略" un-checked-children="换行显示" />
      </template>
    </template>
  </a-table>
</template>

<script setup>
  import { inject, ref } from 'vue';
  import { convertLowerCamel } from '/@/utils/str-util';

  //------------------------ 全局数据 ---------------------
  const tableInfo = inject('tableInfo');

  //------------------------ 表格渲染 ---------------------

  const columns = ref([
    {
      title: '序号',
      dataIndex: 'no',
      width: 60,
    },
    {
      title: '列名',
      dataIndex: 'columnName',
      width: 120,
      ellipsis: true,
    },
    {
      title: '列描述',
      dataIndex: 'columnComment',
      width: 120,
      ellipsis: true,
    },
    {
      title: '显示',
      dataIndex: 'showFlag',
      width: 50,
    },
    {
      title: '字段名词',
      dataIndex: 'label',
      width: 120,
    },
    {
      title: '字段命名',
      dataIndex: 'fieldName',
      width: 120,
    },

    {
      title: '宽度',
      dataIndex: 'width',
      width: 80,
    },
    {
      title: 'ellipsis',
      dataIndex: 'ellipsisFlag',
      width: 100,
    },
  ]);

  const tableData = ref([]);

  // -------------------  表格数据 -------------------
  //初始化设置数据
  function setData(tableColumns, config) {
    let fields = [];

    for (let column of tableColumns) {
      let configField = getConfigField(config.tableFields, column.columnName);

      let field = {
        columnName: column.columnName,
        columnComment: column.columnComment,
        dataType: column.dataType,
        //表单
        showFlag: configField ? configField.showFlag : true,
        label: configField ? configField.label : column.columnComment,
        fieldName: configField ? configField.fieldName : convertLowerCamel(column.columnName),
        width: configField ? configField.width : null,
        ellipsisFlag: configField ? configField.ellipsisFlag : true,
      };
      fields.push(field);
    }

    tableData.value = fields;
  }

  // 获取配置过的字段信息
  function getConfigField(configFields, columnName) {
    if (!configFields) {
      return null;
    }

    let result = configFields.filter((e) => e.columnName === columnName);
    return result && result.length > 0 ? result[0] : null;
  }

  // 获取表单数据
  function getTaleFieldsForm() {
    return tableData.value.map((e) => {
      return {
        columnName: e.columnName,
        label: e.label,
        fieldName: e.fieldName,
        showFlag: e.showFlag,
        width: e.width,
        ellipsisFlag: e.ellipsisFlag,
      };
    });
  }

  defineExpose({
    setData,
    getTaleFieldsForm,
  });
</script>
