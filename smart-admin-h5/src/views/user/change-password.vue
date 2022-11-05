<!-- home -->
<template>
  <div>
    <router-nav-bar path="/dashboard/user" title="修改密码" left-text="返回" left-arrow />

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
          type="password"
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
  },
  methods: {
    // 提交表单
    onSubmit() {
      this.$smart.loading();
      try {
        this.$toast.success('暂未实现');
        this.$router.replace('/dashboard/user');
      } catch (e) {
        console.log(e);
        this.$smartSentry.captureException(e);
      } finally {
        this.$smart.loadingClear();
      }
    }
  }
};
</script>
