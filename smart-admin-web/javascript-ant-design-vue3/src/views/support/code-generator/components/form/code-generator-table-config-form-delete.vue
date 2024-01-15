<!--
  * 代码生成 删除
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-09-22 21:50:41 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
-->
<template>
  <a-form ref="formRef" :model="formData" :rules="formRules" :label-col="{ span: 6 }" style="width: 600px">
    <a-form-item label="数据库表名词"> {{ tableInfo.tableName }} </a-form-item>
    <a-form-item label="数据库表备注"> {{ tableInfo.tableComment }} </a-form-item>
    <a-form-item label="是否允许删除" name="isSupportDelete">
      <a-radio-group v-model:value="formData.isSupportDelete" button-style="solid">
        <a-radio-button :value="true">支持删除</a-radio-button>
        <a-radio-button :value="false">不允许删除</a-radio-button>
      </a-radio-group>
    </a-form-item>
    <a-form-item label="是否为物理删除" name="isPhysicallyDeleted" v-if="formData.isSupportDelete">
      <a-radio-group v-model:value="formData.isPhysicallyDeleted" button-style="solid">
        <a-radio-button :value="true">物理删除</a-radio-button>
        <a-radio-button :value="false">假删</a-radio-button>
      </a-radio-group>
      <div class="smart-margin-top10" v-if="!formData.isPhysicallyDeleted">
        <span v-if="deleteFlagColumnName"> 假删字段为：{{ deleteFlagColumnName }} </span>
        <span stlye="color:red" v-else> 系统未检测出假删字段，假删字段名词应该为 ： <strong>deleted_flag</strong> </span>
      </div>
    </a-form-item>
    <a-form-item label="删除类型" name="deleteEnum" v-if="formData.isSupportDelete">
      <SmartEnumSelect enumName="CODE_DELETE_ENUM" v-model:value="formData.deleteEnum" width="200px" />
    </a-form-item>
  </a-form>
</template>

<script setup>
  import { message } from 'ant-design-vue';
  import _ from 'lodash';
  import { inject, reactive, ref } from 'vue';
  import SmartEnumSelect from '/@/components/framework/smart-enum-select/index.vue';
  import { CODE_DELETE_ENUM } from '/@/constants/support/code-generator-const';

  const tableInfo = inject('tableInfo');

  // ------------- 表单 -------------

  const deleteFlagColumnName = ref('');
  const formRef = ref();

  const defaultFormData = {
    isSupportDelete: true, // 是否支持删除
    isPhysicallyDeleted: undefined, // 是否为物理删除
    deleteEnum: undefined, // 删除类型
  };

  const formData = reactive({ ...defaultFormData });

  const formRules = {
    isSupportDelete: [{ required: true, message: '请输入 isSupportDelete' }],
    isPhysicallyDeleted: [{ required: true, message: '请输入 是否为物理删除' }],
    deleteEnum: [{ required: true, message: '请输入 删除类型' }],
  };

  //初始化设置数据
  function setData(tableColumns, config) {
    //删除字段
    let deletedFlagColumn = getDeleteFlagColumn(tableColumns);
    if (deletedFlagColumn) {
      deleteFlagColumnName.value = deletedFlagColumn.columnName;
    }

    //表单
    let deleteInfo = config.delete;

    formData.isSupportDelete = deleteInfo && deleteInfo.isSupportDelete ? deleteInfo.isSupportDelete : true;
    formData.isPhysicallyDeleted = deleteInfo && deleteInfo.isPhysicallyDeleted ? deleteInfo.isPhysicallyDeleted : !deletedFlagColumn;
    formData.deleteEnum = deleteInfo && deleteInfo.deleteEnum ? deleteInfo.deleteEnum : CODE_DELETE_ENUM.SINGLE_AND_BATCH.value;
  }

  // 获取配置过的字段信息
  function getDeleteFlagColumn(configFields) {
    if (!configFields) {
      return null;
    }

    let result = configFields.filter((e) => _.startsWith(e.columnName, 'deleted_flag' || _.startsWith(e.columnName, 'delete_flag')));
    return result && result.length > 0 ? result[0] : null;
  }

  // 获取表单数据
  function getForm() {
    return Object.assign({}, formData);
  }

  // 校验表单
  function validateForm() {
    return new Promise((resolve, reject) => {
      formRef.value
        .validate()
        .then(() => {
          resolve(true);
        })
        .catch((error) => {
          message.error(' 请检查【4.删除】表单，有参数验证错误');
          reject(error);
        });
    });
  }

  defineExpose({
    setData,
    getForm,
    validateForm,
  });
</script>

<style lang="less" scoped>
  .preview-title {
    font-weight: 600;
    margin: 5px 0px;
  }

  .preview-block {
    font-size: 14px;
    background-color: #f9f9f9;
    padding: 10px 5px;
  }
</style>
