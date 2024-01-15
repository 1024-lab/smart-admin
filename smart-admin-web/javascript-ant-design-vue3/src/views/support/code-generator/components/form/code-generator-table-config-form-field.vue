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
  <a-alert :closable="true" message="请务必将每一个字段的 “ 字段名词 ” 填写完整！！！" type="success" show-icon>
    <template #icon><smile-outlined /></template>
  </a-alert>
  <a-table
    :scroll="{ x: 1300 }"
    size="small"
    bordered
    class="smart-margin-top10"
    :dataSource="tableData"
    :columns="columns"
    rowKey="columnName"
    :pagination="false"
  >
    <template #bodyCell="{ text, record, index, column }">
      <template v-if="column.dataIndex === 'no'">
        {{ index + 1 }}
      </template>

      <template v-if="column.dataIndex === 'columnName'">
        <span>
          <template v-if="record.primaryKeyFlag">
            <a-tag color="#f50" style="line-height: 12px">主键</a-tag>
          </template>
          <template v-if="record.autoIncreaseFlag">
            <a-tag color="#f50" style="line-height: 12px">自增</a-tag>
          </template>
          <br />
          {{ text }}
        </span>
      </template>

      <template v-if="column.dataIndex === 'nullableFlag'">
        <a-tag color="error" v-if="text">非空</a-tag>
      </template>

      <template v-if="column.dataIndex === 'fieldName'">
        <a-input v-model:value="record.fieldName" />
      </template>

      <template v-if="column.dataIndex === 'label'">
        <a-input v-model:value="record.label" />
      </template>

      <template v-if="column.dataIndex === 'javaType'">
        <a-select v-model:value="record.javaType" style="width: 100%">
          <a-select-option v-for="item in JavaTypeList" :value="item" :key="item">{{ item }}</a-select-option>
        </a-select>
      </template>

      <template v-if="column.dataIndex === 'jsType'">
        <a-select v-model:value="record.jsType" style="width: 100%">
          <a-select-option v-for="item in JsTypeList" :value="item" :key="item">{{ item }}</a-select-option>
        </a-select>
      </template>

      <template v-if="column.dataIndex === 'dict'">
        <DictKeySelect v-model:value="record.dict" />
      </template>

      <template v-if="column.dataIndex === 'enumName'">
        <a-input v-model:value="record.enumName" />
      </template>
    </template>
  </a-table>
</template>

<script setup>
  import { inject, ref } from 'vue';
  import { checkExistEnum, convertJavaEnumName, getJavaType, getJsType, JavaTypeList, JsTypeList } from '../../code-generator-util';
  import DictKeySelect from '/@/components/support/dict-key-select/index.vue';
  import { convertUpperCamel, convertLowerCamel } from '/@/utils/str-util';
  import _ from 'lodash';

  //------------------------ 全局数据 ---------------------
  const tableInfo = inject('tableInfo');

  //------------------------ 表格渲染 ---------------------

  const columns = ref([
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
      title: '列类型',
      dataIndex: 'dataType',
      width: 100,
      ellipsis: true,
    },
    {
      title: '非空',
      dataIndex: 'nullableFlag',
      width: 60,
    },

    {
      title: '字段命名',
      dataIndex: 'fieldName',
      width: 150,
    },
    {
      title: '字段名词',
      dataIndex: 'label',
      width: 150,
    },
    {
      title: 'Java类型',
      dataIndex: 'javaType',
      width: 150,
    },
    {
      title: '前端类型',
      dataIndex: 'jsType',
      width: 130,
    },
    {
      title: '字典',
      dataIndex: 'dict',
      width: 150,
    },
    {
      title: '枚举',
      dataIndex: 'enumName',
      width: 150,
    },
  ]);

  const tableData = ref([]);

  // -------------------  表格数据 -------------------
  //初始化设置数据
  function setData(tableColumns, config) {
    let fields = [];
    //基础信息
    let basic = config.basic;

    //命名
    let removePrefixTableName = tableInfo.tableName;
    if (_.startsWith(tableInfo.tableName, 't_')) {
      removePrefixTableName = _.trim(removePrefixTableName, '_t');
    }
    let moduleName = basic && basic.moduleName ? basic.moduleName : removePrefixTableName;
    moduleName = convertUpperCamel(moduleName);

    for (let column of tableColumns) {
      let configField = getConfigField(config.fields, column.columnName);

      let field = {
        columnName: column.columnName,
        columnComment: column.columnComment,
        dataType: column.dataType,
        nullableFlag: column.isNullable === 'NO',
        primaryKeyFlag: column.columnKey === 'PRI',
        autoIncreaseFlag: column.extra === 'auto_increment',
        //表单
        fieldName: configField ? configField.fieldName : convertLowerCamel(column.columnName),
        label: configField ? configField.label : column.columnComment,
        javaType: configField ? configField.javaType : getJavaType(column.dataType),
        jsType: configField ? configField.jsType : getJsType(column.dataType),
        dict: configField ? configField.dict : null,
        enumName: configField
          ? configField.enumName
          : checkExistEnum(column.columnComment)
          ? convertJavaEnumName(moduleName, column.columnName)
          : null,
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
  function getFieldsForm() {
    return tableData.value.map((e) => {
      return {
        columnName: e.columnName,
        columnComment: e.columnComment,
        label: e.label,
        fieldName: e.fieldName,
        javaType: e.javaType,
        jsType: e.jsType,
        dict: e.dict,
        enumName: e.enumName,
        primaryKeyFlag: e.primaryKeyFlag,
        autoIncreaseFlag: e.autoIncreaseFlag,
      };
    });
  }

  defineExpose({
    setData,
    getFieldsForm,
  });
</script>

<style lang="less" scoped></style>
