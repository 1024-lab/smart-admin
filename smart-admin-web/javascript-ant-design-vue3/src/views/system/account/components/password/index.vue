<template>
  <div class="password-container">
    <!--  页面标题-->
    <div class="header-title">修改密码</div>
    <!--  内容区域-->
    <div class="password-form-area">
      <a-form ref="formRef" :model="form" :rules="rules" layout="vertical">
        <a-form-item label="原密码" name="oldPassword">
          <a-input class="form-item" v-model:value.trim="form.oldPassword" type="password" placeholder="请输入原密码" />
        </a-form-item>
        <a-form-item label="新密码" name="newPassword" :help="tips">
          <a-input class="form-item" v-model:value.trim="form.newPassword" type="password" placeholder="请输入新密码" />
        </a-form-item>
        <a-form-item label="确认密码" name="confirmPwd" :help="tips">
          <a-input class="form-item" v-model:value.trim="form.confirmPwd" type="password" placeholder="请输入确认密码" />
        </a-form-item>
      </a-form>
      <a-button type="primary" @click="onSubmit">修改密码</a-button>
    </div>
  </div>
</template>
<script setup>
  import { reactive, ref } from 'vue';
  import { message } from 'ant-design-vue';
  import { SmartLoading } from '/@/components/framework/smart-loading/index.js';
  import { employeeApi } from '/@/api/system/employee-api.js';
  import { smartSentry } from '/@/lib/smart-sentry.js';

  const formRef = ref();
  const tips = '密码长度8-20位且包含大写字母、小写字母、数字三种'; //校验规则
  const reg = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,20}$/;

  const rules = {
    oldPassword: [{ required: true, message: '请输入原密码' }],
    newPassword: [{ required: true, type: 'string', pattern: reg, message: '密码格式错误' }],
    confirmPwd: [{ required: true, type: 'string', pattern: reg, message: '请输入确认密码' }],
  };

  const formDefault = {
    oldPassword: '',
    newPassword: '',
  };
  let form = reactive({
    ...formDefault,
  });

  async function onSubmit() {
    formRef.value
      .validate()
      .then(async () => {
        if (form.newPassword !== form.confirmPwd) {
          message.error('新密码与确认密码不一致');
          return;
        }
        SmartLoading.show();
        try {
          await employeeApi.updateEmployeePassword(form);
          message.success('修改成功');
          form.oldPassword = '';
          form.newPassword = '';
          form.confirmPwd = '';
        } catch (error) {
          smartSentry.captureError(error);
        } finally {
          SmartLoading.hide();
        }
      })
      .catch((error) => {
        console.log('error', error);
        message.error('参数验证错误，请仔细填写表单数据!');
      });
  }
</script>
<style lang="less" scoped>
  .password-container {
    .header-title {
      font-size: 20px;
    }

    .password-form-area {
      margin-top: 30px;

      .form-item {
        width: 500px !important;
      }
    }
  }
</style>
