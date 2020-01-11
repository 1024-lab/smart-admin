<template>
  <div>
    <Form :model="formData" :rules="rules" @keydown.enter.native="login" ref="loginForm">
      <FormItem prop="loginName">
        <Input placeholder="请输入用户名" v-model="formData.loginName"></Input>
      </FormItem>
      <FormItem prop="loginPwd">
        <Input placeholder="请输入密码" type="password" v-model="formData.loginPwd"></Input>
      </FormItem>
      <FormItem prop="code">
        <Input class="code-input" placeholder="请输入验证码" v-model="formData.code"></Input>
        <img :src="codeUrl" @click="verificationCode" class="codeUrl" v-if="codeUrl" />
      </FormItem>
      <FormItem class="remember">
        <Checkbox>记住密码</Checkbox>
      </FormItem>
      <FormItem>
        <Button :loading="btnLoading" @click="login" long type="primary">登录</Button>
      </FormItem>
      <div class="other-way">
        <p class="inline" style="float:left">其他方式登陆</p>
        <div class="inline align" style="float:right">
          <img alt class="marginLeft" src="../../../assets/images/login-taobao.png" />
          <img alt class="marginLeft" src="../../../assets/images/login-alipay.png" />
          <img alt class="marginLeft" src="../../../assets/images/login-sina.png" />
        </div>
      </div>
    </Form>
  </div>
</template>
<script>
import { loginApi } from '@/api/login';
import { PRIVILEGE_TYPE } from '@/constants/login';
export default {
  name: 'LoginForm',
  props: {
    // 登录名规则
    loginNameRules: {
      type: Array,
      default: () => {
        return [{ required: true, message: '账号不能为空', trigger: 'blur' }];
      }
    },
    // 密码规则
    loginPwdRules: {
      type: Array,
      default: () => {
        return [{ required: true, message: '密码不能为空', trigger: 'blur' }];
      }
    },
    // 验证码规则
    codedRules: {
      type: Array,
      default: () => {
        return [{ required: true, message: '验证码不能为空', trigger: 'blur' }];
      }
    }
  },
  data() {
    return {
      // 防止重复提交 按钮加载状态
      btnLoading: false,
      formData: {
        code: '',
        codeUuid: '',
        loginName: 'sa',
        loginPwd: '123456'
      },
      codeUrl: ''
    };
  },
  computed: {
    rules() {
      return {
        loginName: this.loginNameRules,
        loginPwd: this.loginPwdRules,
        code: this.codedRules
      };
    }
  },
  mounted() {
    this.verificationCode();
  },
  methods: {
    // 获取验证码
    async verificationCode() {
      let result = await loginApi.getVerificationCode();
      let datas = result.data;
      this.formData.codeUuid = datas.uuid;
      this.codeUrl = datas.code;
    },
    // 登录
    login() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loginSuccess();
        }
      });
    },
    // 登录 - 异步
    async loginSuccess() {
      try {
        this.btnLoading = true;
        let loginResult = await loginApi.login(this.formData);
        let loginInfo = loginResult.data;
        localStorage.clear();
        this.$store.commit('setToken', loginInfo.xaccessToken);
        // 保存用户登录
        this.$store.commit('setUserLoginInfo', {
          id: loginInfo.id,
          loginName: loginInfo.loginName,
          nickName: loginInfo.nickName,
          actualName: loginInfo.actualName,
          phone: loginInfo.phone,
          isSuperMan: loginInfo.isSuperMan
        });
        //设置权限
        this.$store.commit('setUserPrivilege', loginInfo.privilegeList);
        this.btnLoading = false;
        // 跳转到首页
        this.$router.push({
          name: this.$config.homeName
        });
      } catch (e) {
        //TODO zhuoda sentry
        console.error(e);
        this.btnLoading = false;
        this.verificationCode();
      }
    }
  }
};
</script>