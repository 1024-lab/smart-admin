<template>
  <view class="container">
    <view class="top-view">
      <view class="login"> 登录 </view>
      <view class="logo">
        <image src="@/static/images/login/login-logo.png" />
      </view>
    </view>
    <view class="bottom-view">
      <view class="input-view smart-margin-top10">
        <image src="@/static/images/login/login-username.png"></image>
        <uni-easyinput
          class="input"
          placeholder="请输入用户名"
          :clearable="true"
          placeholderStyle="color:#CCCCCC"
          border="none"
          v-model="loginForm.loginName"
        />
      </view>

      <view class="input-view smart-margin-top10">
        <image src="@/static/images/login/login-password.png"></image>
        <uni-easyinput
          class="input"
          placeholder="请输入密码"
          :clearable="true"
          :password="true"
          placeholderStyle="color:#CCCCCC"
          border="none"
          v-model="loginForm.password"
        />
      </view>

      <view class="input-view smart-margin-top10">
        <image src="@/static/images/login/login-password.png"></image>
        <uni-easyinput
          class="input captcha-input"
          placeholder="请输入验证码"
          :clearable="true"
          :password="false"
          placeholderStyle="color:#CCCCCC"
          border="none"
          v-model="loginForm.captchaCode"
        />
        <img class="captcha-img" :src="captchaBase64Image" @click="getCaptcha" />
      </view>

      <view class="code-login-view smart-margin-top10">
        <text class="code-text">验证码登录</text>
        <text class="forget-text">忘记密码？</text>
      </view>

      <view @click="login" class="button login-btn smart-margin-top20"> 登录 </view>
      <view @click="login" class="button register-btn smart-margin-top20"> 创建账号 </view>
      <OtherWayBox />
      <LoginCheckBox class="login-check-box" ref="loginCheckBoxRef" />
    </view>
  </view>
</template>
<script setup>
  import { reactive, ref } from 'vue';
  import { onShow } from '@dcloudio/uni-app';
  import OtherWayBox from './components/other-way-box.vue';
  import LoginCheckBox from './components/login-check-box.vue';
  import { loginApi } from '@/api/system/login-api';
  import { LOGIN_DEVICE_ENUM } from '@/constants/system/login-device-const';
  import { encryptData } from '@/lib/encrypt';
  import { useUserStore } from '@/store/modules/system/user';
  import { smartSentry } from '@/lib/smart-sentry';

  const loginForm = reactive({
    loginName: 'admin',
    password: '123456',
    captchaCode: '',
    captchaUuid: '',
    loginDevice: LOGIN_DEVICE_ENUM.H5.value,
  });

  const loginCheckBoxRef = ref();
  async function login() {
    if (!loginCheckBoxRef.value.agreeFlag) {
      uni.showToast({
        icon: 'none',
        title: '请阅读并同意《用户协议》、《隐私政策》',
      });
      return;
    }
    if (!loginForm.loginName) {
      uni.showToast({
        icon: 'none',
        title: '请输入用户名',
      });
      return;
    }
    if (!loginForm.password) {
      uni.showToast({
        icon: 'none',
        title: '请输入密码',
      });
      return;
    }

    try {
      uni.showLoading({ title: '登录中' });
      // 密码加密
      let encryptPasswordForm = Object.assign({}, loginForm, {
        password: encryptData(loginForm.password),
      });
      const res = await loginApi.login(encryptPasswordForm);
      stopRefreshCaptchaInterval();
      uni.showToast({ title: '登录成功' });
      //更新用户信息到 pinia
      useUserStore().setUserLoginInfo(res.data);

      uni.switchTab({ url: '/pages/home/index' });
    } catch (e) {
      if (e.data && e.data.code !== 0) {
        loginForm.captchaCode = '';
        getCaptcha();
      }
      smartSentry.captureError(e);
      uni.hideLoading();
    }
  }

  //--------------------- 验证码 ---------------------------------

  const captchaBase64Image = ref('');

  async function getCaptcha() {
    try {
      let captchaResult = await loginApi.getCaptcha();
      captchaBase64Image.value = captchaResult.data.captchaBase64Image;
      console.log(captchaResult.data.captchaBase64Image, 2);
      loginForm.captchaUuid = captchaResult.data.captchaUuid;
      beginRefreshCaptchaInterval(captchaResult.data.expireSeconds);
    } catch (e) {
      console.log(e);
    }
  }

  let refreshCaptchaInterval = null;

  function beginRefreshCaptchaInterval(expireSeconds) {
    if (refreshCaptchaInterval === null) {
      refreshCaptchaInterval = setInterval(getCaptcha, (expireSeconds - 5) * 1000);
    }
  }

  function stopRefreshCaptchaInterval() {
    if (refreshCaptchaInterval != null) {
      clearInterval(refreshCaptchaInterval);
      refreshCaptchaInterval = null;
    }
  }

  onShow(getCaptcha);
</script>
<style lang="scss" scoped>
  .bottom-view {
    box-sizing: border-box;
    margin-top: -280rpx;
    border-radius: 20rpx 20rpx 0 0;
    width: 100%;
    background-color: white;
    padding: 0 60rpx;
    .input-view {
      display: flex;
      flex-direction: row;
      align-items: center;
      background-color: $page-bg-color;
      border-radius: 4px;
      height: 100rpx;
      .captcha-img {
        margin-left: 5px;
        height: 100rpx;
        width: 40%;
      }
      image {
        margin-left: 30rpx;
        width: 44rpx;
        height: 44rpx;
      }
      .input {
        margin: 0 16rpx;
        background-color: $page-bg-color;
      }
      .captcha-input {
        width: 50%;
      }
    }
    .code-login-view {
      margin: 50rpx 0 0;
      height: 40rpx;
      display: flex;
      flex-direction: row;
      align-items: center;
      justify-content: space-between;
      .code-text {
        height: 40rpx;
        font-size: $main-size;
        font-weight: 400;
        text-align: left;
        color: $main-font-color;
      }
      .forget-text {
        height: 40rpx;
        font-size: $main-size;
        font-weight: 400;
        text-align: right;
        color: $second-font-color;
      }
    }
  }
  .button {
    flex-shrink: 0;
    width: 100%;
    height: 90rpx;
    border-radius: 4px;
    box-shadow: 0px 5px 8px 0px rgba(58, 121, 255, 0.2);
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: $main-size;

    &.disabled {
      opacity: 0.4;
    }
    &.login-btn {
      background: $main-color;
      color: #ffffff;
    }

    &.register-btn {
      background: white;
      color: $main-color;
      border: 0.5px solid $main-color;
      border-color: rgba(26, 154, 255, 0.3);
    }
  }

  .logo {
    flex-shrink: 0;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: row;
    height: 220rpx;

    image {
      width: 208rpx;
      height: 220rpx;
    }
  }

  ::v-deep .uni-easyinput__content {
    background-color: transparent !important;
  }
  ::v-deep .is-input-border {
    border: none;
  }
  .container {
    display: flex;
    align-items: center;
    flex-direction: column;
    min-height: 100vh;
    width: 100vw;
    .back-icon {
      width: 18px;
      height: 18px;
    }
    .top-view {
      display: flex;
      flex-direction: column;
      align-items: center;
      width: 100%;
      height: 720rpx;
      background-image: url('~@/static/images/login/login-top-back.png');
      .login {
        font-weight: bold;
        margin-top: 70rpx;
      }
      .logo {
        width: 260rpx;
        height: 260rpx;
      }
    }
  }

  .login-check-box {
    flex-shrink: 0;
    margin-top: 150rpx;
    margin-bottom: 120rpx;
    align-self: flex-start;
  }
</style>
