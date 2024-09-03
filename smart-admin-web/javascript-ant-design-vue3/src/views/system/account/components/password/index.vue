<template>
  <div class="password-container">
    <!--  内容区域-->
    <div class="password-form-area">
      <a-form ref="formRef" :model="form" :rules="rules" layout="vertical">
        <a-form-item label="原密码" name="oldPassword">
          <a-input-password class="form-item" v-model:value.trim="form.oldPassword" type="password" placeholder="请输入原密码" />
        </a-form-item>
        <a-form-item label="新密码" name="newPassword" :help="tips">
          <a-input-password class="form-item" v-model:value.trim="form.newPassword" type="password" placeholder="请输入新密码" />
        </a-form-item>
        <a-form-item label="确认密码" name="confirmPwd" :help="tips">
          <a-input-password class="form-item" v-model:value.trim="form.confirmPwd" type="password" placeholder="请输入确认密码" />
        </a-form-item>
      </a-form>
      <a-button type="primary" style="margin: 20px 0 0 250px" @click="onSubmit">修改密码</a-button>
    </div>
  </div>
</template>
<script setup>
  import { computed, onMounted, reactive, ref } from 'vue';
  import { message } from 'ant-design-vue';
  import { SmartLoading } from '/@/components/framework/smart-loading/index.js';
  import { employeeApi } from '/@/api/system/employee-api.js';
  import { smartSentry } from '/@/lib/smart-sentry.js';

  const emits = defineEmits(['onSuccess']);

  const formRef = ref();
  const passwordComplexityEnabledTips = '密码长度8-20位，必须包含字母、数字、特殊符号（如：@#$%^&*()_+-=）等三种字符'; //校验规则
  const passwordTips = '密码长度至少8位';
  const tips = ref(passwordTips);
  const reg =
    /^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z\W_!@#$%^&*`~()-+=]+$)(?![a-z0-9]+$)(?![a-z\W_!@#$%^&*`~()-+=]+$)(?![0-9\W_!@#$%^&*`~()-+=]+$)[a-zA-Z0-9\W_!@#$%^&*`~()-+=]{8,20}$/;

  // 获取系统的密码复杂度
  const passwordComplexityEnabledFlag = ref(false);

  async function getPasswordComplexityEnabled() {
    try {
      SmartLoading.show();
      let res = await employeeApi.getPasswordComplexityEnabled();
      passwordComplexityEnabledFlag.value = res.data;
      tips.value = passwordComplexityEnabledFlag.value ? passwordComplexityEnabledTips : passwordTips;
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      SmartLoading.hide();
    }
  }
  onMounted(getPasswordComplexityEnabled);

  const passwordComplexityEnabledRules = {
    oldPassword: [{ required: true, message: '请输入原密码' }],
    newPassword: [{ required: true, type: 'string', pattern: reg, message: '密码格式错误' }],
    confirmPwd: [{ required: true, type: 'string', pattern: reg, message: '请输入确认密码' }],
  };
  const commonRules = {
    oldPassword: [{ required: true, message: '请输入原密码' }],
    newPassword: [
      { required: true, message: '密码格式错误' },
      { min: 8, message: '密码长度至少8位' },
    ],
    confirmPwd: [
      { required: true, message: '密码格式错误' },
      { min: 8, message: '密码长度至少8位' },
    ],
  };

  const rules = computed(() => {
    return passwordComplexityEnabledFlag.value ? passwordComplexityEnabledRules : commonRules;
  });

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

          emits('onSuccess');
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
        width: 550px !important;
      }
    }
  }
</style>
