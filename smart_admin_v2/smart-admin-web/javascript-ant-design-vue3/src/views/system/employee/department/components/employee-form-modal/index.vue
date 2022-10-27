<!--
  *  员工 表单 弹窗
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-08-08 20:46:18
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
-->
<template>
  <a-drawer
    :title="form.employeeId ? '编辑' : '添加'"
    :width="600"
    :visible="visible"
    :body-style="{ paddingBottom: '80px' }"
    @close="onClose"
    destroyOnClose
  >
    <a-form ref="formRef" :model="form" :rules="rules" layout="vertical">
      <a-form-item label="姓名" name="actualName">
        <a-input v-model:value.trim="form.actualName" placeholder="请输入姓名" />
      </a-form-item>
      <a-form-item label="手机号" name="phone">
        <a-input v-model:value.trim="form.phone" placeholder="请输入手机号" />
      </a-form-item>
      <a-form-item label="部门" name="departmentId">
        <DepartmentTreeSelect ref="departmentTreeSelect" width="100%" :init="false" v-model:value="form.departmentId" />
      </a-form-item>
      <a-form-item label="登录名" name="loginName">
        <a-input v-model:value.trim="form.loginName" placeholder="请输入登录名" />
        <p class="hint">初始密码默认为：随机</p>
      </a-form-item>
      <a-form-item label="性别" name="gender">
        <smart-enum-select style="width: 100%" v-model:value="form.gender" placeholder="请选择性别" enum-name="GENDER_ENUM" />
      </a-form-item>
      <a-form-item label="状态" name="disabledFlag">
        <a-select v-model:value="form.disabledFlag" placeholder="请选择状态">
          <a-select-option :value="0">启用</a-select-option>
          <a-select-option :value="1">禁用</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="角色" name="roleIdList">
        <a-select mode="multiple" v-model:value="form.roleIdList" optionFilterProp="title" placeholder="请选择角色">
          <a-select-option v-for="item in roleList" :key="item.roleId" :title="item.roleName">{{ item.roleName }}</a-select-option>
        </a-select>
      </a-form-item>
    </a-form>
    <div class="footer">
      <a-button style="margin-right: 8px" @click="onClose">取消</a-button>
      <a-button type="primary" style="margin-right: 8px" @click="onSubmit(false)">保存</a-button>
      <a-button v-if="!form.employeeId" type="primary" @click="onSubmit(true)">保存并继续添加</a-button>
    </div>
  </a-drawer>
</template>
<script setup>
  import { message } from 'ant-design-vue';
  import _ from 'lodash';
  import { nextTick, reactive, ref } from 'vue';
  import { employeeApi } from '/@/api/system/employee/employee-api';
  import { roleApi } from '/@/api/system/role/role-api';
  import DepartmentTreeSelect from '/@/components/system/department-tree-select/index.vue';
  import SmartEnumSelect from '/@/components/framework/smart-enum-select/index.vue';
  import { GENDER_ENUM } from '/@/constants/common-const';
  import { regular } from '/@/constants/regular-const';
  import { SmartLoading } from '/@/components/framework/smart-loading';
import { smartSentry } from '/@/lib/smart-sentry';
  // ----------------------- 以下是字段定义 emits props ---------------------
  const departmentTreeSelect = ref();
  // emit
  const emit = defineEmits(['refresh', 'show-account']);

  // ----------------------- 显示/隐藏 ---------------------

  const visible = ref(false); // 是否展示抽屉
  // 隐藏
  function onClose() {
    reset();
    visible.value = false;
  }
  // 显示
  async function showDrawer(rowData) {
    Object.assign(form, formDefault);
    if (rowData && !_.isEmpty(rowData)) {
      Object.assign(form, rowData);
    }
    visible.value = true;
    nextTick(() => {
      queryAllRole();
    });
  }

  // ----------------------- 表单显示 ---------------------

  const roleList = ref([]); //角色列表
  async function queryAllRole() {
    let res = await roleApi.queryAll();
    roleList.value = res.data;
  }

  const formRef = ref(); // 组件ref
  const formDefault = {
    id: undefined,
    actualName: undefined,
    departmentId: undefined,
    disabledFlag: 0,
    leaveFlag: 0,
    gender: GENDER_ENUM.MAN.value,
    loginName: undefined,
    phone: undefined,
    roleIdList: undefined,
  };

  let form = reactive(_.cloneDeep(formDefault));
  function reset() {
    Object.assign(form, formDefault);
    formRef.value.resetFields();
  }

  // ----------------------- 表单提交 ---------------------
  // 表单规则
  const rules = {
    actualName: [
      { required: true, message: '姓名不能为空' },
      { max: 30, message: '姓名不能大于30个字符', trigger: 'blur' },
    ],
    phone: [
      { required: true, message: '手机号不能为空' },
      { pattern: regular.phone, message: '请输入正确的手机号码', trigger: 'blur' },
    ],
    loginName: [
      { required: true, message: '登录账号不能为空' },
      { max: 30, message: '登录账号不能大于30个字符', trigger: 'blur' },
    ],
    gender: [{ required: true, message: '性别不能为空' }],
    departmentId: [{ required: true, message: '部门不能为空' }],
    disabledFlag: [{ required: true, message: '状态不能为空' }],
    leaveFlag: [{ required: true, message: '在职状态不能为空' }],
  };

  // 校验表单
  function validateForm(formRef) {
    return new Promise((resolve) => {
      formRef
        .validate()
        .then(() => {
          resolve(true);
        })
        .catch(() => {
          resolve(false);
        });
    });
  }

  // 提交数据
  async function onSubmit(keepAdding) {
    let validateFormRes = await validateForm(formRef.value);
    if (!validateFormRes) {
      message.error('参数验证错误，请仔细填写表单数据!');
      return;
    }
    SmartLoading.show();
    if (form.employeeId) {
      await updateEmployee(keepAdding);
    } else {
      await addEmployee(keepAdding);
    }
  }

  async function addEmployee(keepAdding) {
    try {
      let { data } = await employeeApi.addEmployee(form);
      message.success('添加成功');
      emit('show-account', form.loginName, data);
      if (keepAdding) {
        reset();
      } else {
        onClose();
      }
      emit('refresh');
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      SmartLoading.hide();
    }
  }
  async function updateEmployee(keepAdding) {
    try {
      let result = await employeeApi.updateEmployee(form);
      message.success('更新成功');
      if (keepAdding) {
        reset();
      } else {
        onClose();
      }
      emit('refresh');
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      SmartLoading.hide();
    }
  }

  // ----------------------- 以下是暴露的方法内容 ----------------------------
  defineExpose({
    showDrawer,
  });
</script>
<style scoped lang="less">
  .footer {
    position: absolute;
    right: 0;
    bottom: 0;
    width: 100%;
    border-top: 1px solid #e9e9e9;
    padding: 10px 16px;
    background: #fff;
    text-align: right;
    z-index: 1;
  }
  .hint {
    margin-top: 5px;
    color: #bfbfbf;
  }
</style>
