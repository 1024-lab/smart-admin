<!-- home -->
<template>
  <div style="padding: 0.4rem">
    <h1 align="center">Smart-Admin-H5</h1>
    <van-form @submit="onSubmit">
      <van-field
        v-model="formData.loginName"
        name="用户名"
        label="用户名"
        placeholder="用户名"
        :rules="[{ required: true, message: '请填写用户名' }]"
      />
      <van-field
        v-model="formData.loginPwd"
        type="password"
        name="密码"
        label="密码"
        placeholder="密码"
        :rules="[{ required: true, message: '请填写密码' }]"
      />
      <div style="margin: 16px;">
        <van-button  block type="info" native-type="submit">
          登录
        </van-button>
      </div>
    </van-form>
  </div>
</template>

<script>
import { loginApi } from '@/api/login';
import cookie from '@/lib/cookie';

export default {
  data() {
    return {
      formData: {
        loginName: '',
        loginPwd: ''
      },
    };
  },
  mounted() {
  },
  methods: {
    // 提交登录表单
    async onSubmit() {
      this.$smart.loading();
      try {
        const res = await loginApi.login(this.formData);
        const loginInfo = res.data;
        this.$store.commit('user/updateSession', loginInfo);
        cookie.setToken(loginInfo.xaccessToken);
        this.$toast.success('登录成功');
        this.$router.replace('/dashboard/user');
        // this.$router.replace('/contact-company');
      } catch (e) {
        this.$smartSentry.captureException(e);
        await this.getVerificationCode();
      } finally {
        this.$smart.loadingClear();
      }
    }
  }
};
</script>
<style lang="scss" scoped>
</style>
