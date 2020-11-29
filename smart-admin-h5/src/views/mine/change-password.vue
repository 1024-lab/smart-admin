<!-- home -->
<template>
  <div>
    <router-nav-bar path="/mine" title="修改密码" left-text="返回" left-arrow />

    <van-form @submit="onSubmit">
      <van-field
        v-model="formData.loginName"
        name="旧密码"
        label="旧密码"
        placeholder="旧密码"
        :rules="[{ required: true, message: '请填写旧密码' }]"
      />
      <van-field
        v-model="formData.loginPwd"
        type="password"
        name="新密码"
        label="新密码"
        placeholder="新密码"
        :rules="[{ required: true, message: '请填写新密码' }]"
      />
        <van-field
          style="width: 60%;float:left"
          v-model="formData.code"
          name="新密码"
          label="新密码"
          placeholder="新密码"
          :rules="[{ required: true, message: '请填写新密码' }]"
        />
      <div style="margin: 16px;">
        <van-button block type="info" native-type="submit" size="small">
          提交
        </van-button>
      </div>
    </van-form>
  </div>
</template>

<script>
import { loginApi } from '@/api/login';
import cookie from '@/lib/cookie';
import RouterNavBar from 'components/van-bar/RouterNavBar';

export default {
  components: {
    RouterNavBar
  },
  data() {
    return {
      formData: {
        loginName: '',
        loginPwd: '',
        code: '',
        codeUuid: ''
      },
      codeUrl: ''
    };
  },
  mounted() {
    this.getVerificationCode();
  },
  methods: {
    // 获取验证码
    async getVerificationCode() {
      try {
        const res = await loginApi.getVerificationCode();
        const data = res.data;
        this.formData.codeUuid = data.uuid;
        this.codeUrl = data.code;
        this.formData.code = '';
        console.log(this.codeUrl);
      } catch (e) {
        this.$smartSentry.captureException(e);
      }
    },
    // 提交表单
    async onSubmit() {
      this.$smart.loading();
      try {
        const res = await loginApi.login(this.formData);
        const loginInfo = res.data;
        this.$store.commit('user/updateSession', loginInfo);
        cookie.setToken(loginInfo.xaccessToken);
        this.$toast.success('登录成功');
        this.$router.replace('/home');
      } catch (e) {
        console.log(e);
        this.$smartSentry.captureException(e);
        await this.getVerificationCode();
      } finally {
        this.$smart.loadingClear();
      }
    }
  }
};
</script>
