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
  <a-alert
    :closable="true"
    message="默认数据库表名前缀为：t_， 如果想修改默认前缀，请修改前端 code-generator-table-config-form-basic.vue 文件的 tablePrefix 变量"
    type="success"
    show-icon
  >
    <template #icon><smile-outlined /></template>
  </a-alert>
  <a-row type="flex" class="smart-margin-top10">
    <a-col flex="350px">
      <a-form ref="formRef" :model="formData" :rules="formRules" :label-col="{ span: 5 }" :wrapper-col="{ span: 16 }">
        <a-form-item label="表"> {{ tableInfo.tableName }} </a-form-item>
        <a-form-item label="表备注"> {{ tableInfo.tableComment }} </a-form-item>
        <a-form-item label="表前缀" name="tablePrefix">
          <a-input v-model:value="tablePrefix" @change="onChangeTablePrefix" placeholder="请输入 表前缀 " />
        </a-form-item>
        <a-form-item label="单词命名" name="moduleName">
          <a-input v-model:value="formData.moduleName" placeholder="请输入 单词命名 " />
        </a-form-item>
        <a-form-item label="Java包名" name="javaPackageName">
          <a-input v-model:value="formData.javaPackageName" placeholder="请输入 Java包名 " />
        </a-form-item>
        <a-form-item label="注释信息" name="description">
          <a-input v-model:value="formData.description" placeholder="请输入 注释信息 " />
        </a-form-item>
        <a-form-item label="前端作者" name="frontAuthor">
          <a-input v-model:value="formData.frontAuthor" placeholder="请输入 前端作者" />
        </a-form-item>
        <a-form-item label="前端时间" name="frontDate">
          <a-date-picker
            style="width: 100%"
            show-time
            format="YYYY-MM-DD HH:mm:ss"
            v-model:value="formData.frontDate"
            placeholder="请输入 前端时间"
          />
        </a-form-item>
        <a-form-item label="后端作者" name="backendAuthor">
          <a-input v-model:value="formData.backendAuthor" placeholder="请输入 后端作者" />
        </a-form-item>
        <a-form-item label="后端时间" name="backendDate">
          <a-date-picker
            style="width: 100%"
            show-time
            format="YYYY-MM-DD HH:mm:ss"
            v-model:value="formData.backendDate"
            placeholder="请输入 后端时间"
          />
        </a-form-item>
        <a-form-item label="版权信息" name="copyright">
          <a-input v-model:value="formData.copyright" placeholder="请输入 版权信息" />
        </a-form-item>
      </a-form>
    </a-col>
    <a-col flex="auto" style="height: 100vh; overflow-y: scroll">
      <a-tabs v-model:activeKey="activeKey" size="small">
        <a-tab-pane key="1" tab="前端文件命名">
          <div class="preview-title">前端文件名</div>
          <div class="preview-block">
            <div v-for="item in frontNameList" :key="item">
              {{ item }}
            </div>
          </div>
          <div class="preview-title">前端Vue文件注释</div>
          <div>
            <pre class="preview-block">
&lt;!--
  * {{ formData.description }}
  * 
  * @Author:     {{ formData.frontAuthor }}
  * @Date:       {{ formData.frontDate }}
  * @Copyright   {{ formData.copyright }}
--&gt;</pre
            >
          </div>
          <div class="preview-title">前端Js文件注释</div>
          <div>
            <pre class="preview-block">
/*
 * {{ formData.description }}
 *
 * @Author:     {{ formData.frontAuthor }}
 * @Date:       {{ formData.frontDate }}
 * @Copyright   {{ formData.copyright }}
 */
              </pre
            >
          </div>
        </a-tab-pane>
        <a-tab-pane key="2" tab="后端文件命名">
          <div>
            <div class="preview-title">后端-四层代码：</div>
            <div class="preview-block">
              <div v-for="item in backendMvcNameList" :key="item">
                {{ item }}
              </div>
            </div>
            <div class="preview-title">JavaBean代码：</div>
            <div class="preview-block">
              <div v-for="item in backendJavaBeanNameList" :key="item">
                {{ item }}
              </div>
            </div>
            <div class="preview-title">常量代码：</div>
            <div class="preview-block">
              <div v-for="item in backendConstNameList" :key="item">
                {{ item }}
              </div>
            </div>
            <div class="preview-title">注释：</div>
            <pre class="preview-block">
/**
 * {{ formData.description }}
 *
 * @Author:     {{ formData.backendAuthor }}
 * @Date:       {{ formData.backendDate }}
 * @Copyright   {{ formData.copyright }}
 */
              </pre
            >
          </div>
        </a-tab-pane>
      </a-tabs>
    </a-col>
  </a-row>
</template>

<script setup>
  import { message } from 'ant-design-vue';
  import dayjs from 'dayjs';
  import _ from 'lodash';
  import { computed, inject, reactive, ref } from 'vue';
  import { convertLowerHyphen, convertUpperCamel } from '/@/utils/str-util';

  const tableInfo = inject('tableInfo');

  const activeKey = ref('1');

  // ------------- 表单 -------------

  const formRef = ref();

  const defaultFormData = {
    moduleName: undefined, // 单词命名
    javaPackageName: undefined, // java包名
    description: undefined, // 注释信息
    frontAuthor: undefined, // 前端作者
    frontDate: undefined, // 前端时间
    backendAuthor: undefined, // 后端作者
    backendDate: undefined, // 后端时间
    copyright: undefined, //版权
  };

  const tablePrefix = ref('t_');
  const formData = reactive({ ...defaultFormData });

  const formRules = {
    moduleName: [{ required: true, message: '请输入 单词命名' }],
    javaPackageName: [{ required: true, message: '请输入 java包名' }],
    frontAuthor: [{ required: true, message: '请输入 前端作者' }],
    frontDate: [{ required: true, message: '请输入 前端时间' }],
    backendAuthor: [{ required: true, message: '请输入 后端作者' }],
    backendDate: [{ required: true, message: '请输入 后端时间' }],
    copyright: [{ required: true, message: '请输入 版权' }],
  };

  //初始化设置数据
  function setData(config) {
    //基础信息
    let basic = config.basic;

    //命名
    let removePrefixTableName = tableInfo.tableName;
    if (_.startsWith(tableInfo.tableName, tablePrefix.value)) {
      removePrefixTableName = _.trim(removePrefixTableName, tablePrefix.value);
    }
    formData.moduleName = basic && basic.moduleName ? basic.moduleName : removePrefixTableName;
    formData.moduleName = convertUpperCamel(formData.moduleName);

    //注释
    formData.description = basic && basic.description ? basic.description : tableInfo.tableComment;
    //时间
    formData.frontDate = basic && basic.frontDate ? basic.frontDate : tableInfo.createTime;
    formData.frontDate = dayjs(formData.frontDate);
    formData.backendDate = basic && basic.backendDate ? basic.backendDate : tableInfo.createTime;
    formData.backendDate = dayjs(formData.backendDate);

    //其他字段
    formData.frontAuthor = basic && basic.frontAuthor ? basic.frontAuthor : null;
    formData.javaPackageName = basic && basic.javaPackageName ? basic.javaPackageName : null;
    formData.backendAuthor = basic && basic.backendAuthor ? basic.backendAuthor : null;
    formData.copyright = basic && basic.copyright ? basic.copyright : null;
  }

  function onChangeTablePrefix(e) {
    let removePrefixTableName = tableInfo.tableName;
    if (_.startsWith(tableInfo.tableName, tablePrefix.value)) {
      removePrefixTableName = _.trim(removePrefixTableName, tablePrefix.value);
    }
    formData.moduleName = convertUpperCamel(removePrefixTableName);
  }

  // 获取表单数据
  const timeFormat = 'YYYY-MM-DD HH:mm:ss';
  function getBasicForm() {
    return Object.assign({}, formData, {
      frontDate: dayjs(formData.frontDate).format(timeFormat),
      backendDate: dayjs(formData.backendDate).format(timeFormat),
    });
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
          message.error(' 请检查【1.基础命名】表单，有参数验证错误');
          reject(error);
        });
    });
  }

  defineExpose({
    setData,
    getBasicForm,
    validateForm,
  });

  // ------------- 预览 -------------

  const frontName = computed(() => convertLowerHyphen(formData.moduleName));

  const frontNameList = computed(() => {
    return [
      '请求：' + frontName.value + '-api.js', //
      '常量：' + frontName.value + '-const.js', //
      '列表：' + frontName.value + '-list.vue', //
      '表单：' + frontName.value + '-form-modal.vue', //
      '详情：' + frontName.value + '-detail.vue', //
    ];
  });

  const backendMvcNameList = computed(() => {
    return [
      '控制层：' + formData.moduleName + 'Controller.java', //
      '业务层：' + formData.moduleName + 'Service.java', //
      '中间层：' + formData.moduleName + 'Manager.java', //
      '持久层：' + formData.moduleName + 'Dao.java', //
      'SQL层： ' + formData.moduleName + 'Mapper.xml', //
    ];
  });
  const backendJavaBeanNameList = computed(() => {
    return [
      '实体类：' + formData.moduleName + 'Entity.java', //
      '表现类：' + formData.moduleName + 'VO.java', //
      '新建类：' + formData.moduleName + 'AddForm.java', //
      '更新类：' + formData.moduleName + 'UpdateForm.java', //
      '查询类：' + formData.moduleName + 'QueryForm.java', //
    ];
  });
  const backendConstNameList = computed(() => {
    return [
      //
      '枚举类：' + formData.moduleName + 'Enum.java', //
      '常量类：' + formData.moduleName + 'Const.java', //
    ];
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
