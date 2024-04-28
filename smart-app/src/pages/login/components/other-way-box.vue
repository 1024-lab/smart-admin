<template>
  <view class="other-way-box">
    <view class="title">
      <span style="margin: 0 10px">—— 第三方账号登录 ——</span>
    </view>
    <view class="other-way">
      <!-- 手机号登录 -->
      <view v-if="phoneLoginFlag" @click="navigateTo('/pages/login/phone-login')" class="item">
        <image src="@/static/images/login/phone-login-icon.png" />
      </view>
      <!-- 微信登录 -->
      <view v-if="wxLoginFlag" @click="toWeChatLogin" class="item">
        <image src="@/static/images/login/wx-login-icon.png" />
      </view>
      <!-- 苹果账号登录 -->
      <view v-if="iosFlag" @click="toAppleLogin" class="item apple">
        <image src="@/static/images/login/ios-login-icon.png" />
      </view>
    </view>
  </view>
</template>
<script setup>
  import { computed } from 'vue';

  const emit = defineEmits(['wechatLogin', 'appleLogin']);

  const phoneLoginFlag = computed(() => {
    let currentPages = getCurrentPages();
    let currentPage = currentPages[currentPages.length - 1];
    return currentPage.route === 'pages/login/phone-login';
  });
  const wxLoginFlag = computed(() => {
    let currentPages = getCurrentPages();
    let currentPage = currentPages[currentPages.length - 1];
    return currentPage.route === 'pages/login/login';
  });
  const iosFlag = computed(() => {
    // #ifdef APP-PLUS
    let systemInfoSync = uni.getSystemInfoSync();
    let platform = systemInfoSync.platform;
    return platform === 'ios';
    // #endif
    return true;
  });

  function navigateTo(url) {
    uni.navigateTo({ url });
  }
  // 微信登录
  function toWeChatLogin() {
    emit('wechatLogin');
  }

  // 苹果登录
  function toAppleLogin() {
    //方法二
    var appleOauth = null;
    plus.oauth.getServices(
      (services) => {
        for (var i in services) {
          var service = services[i];
          // 获取苹果授权登录对象，苹果授权登录id 为 'apple' iOS13以下系统，不会返回苹果登录对应的 service
          if (service.id == 'apple') {
            appleOauth = service;
            break;
          }
        }
        appleOauth.logout(
          (success) => {
            // console.log("退出登录成功：", JSON.stringify(success))

            appleOauth.login(
              (oauth) => {
                // 授权成功，苹果授权返回的信息在 oauth.target.appleInfo 中
                let info = oauth.target.appleInfo || {};
                // console.log("登录数据：", JSON.stringify(info))
                emit('appleLogin', info);
              },
              (err) => {
                // 授权失败 error
              },
              {
                // 默认只会请求用户名字信息，如需请求用户邮箱信息，需要设置 scope: 'email'
                scope: 'email',
              }
            );
          },
          (err) => {
            console.log('退出登录失败');
          }
        );
      },
      (err) => {
        // 获取 services 失败
      }
    );
  }
</script>
<style lang="scss" scoped>
  .other-way-box {
    flex-shrink: 0;
    margin-top: 82rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: column;

    .title {
      font-size: $small-size;
      font-weight: 400;
      color: #cccccc;
    }

    .other-way {
      margin-top: 42rpx;
      height: 80rpx;
      display: flex;
      align-items: center;
      justify-content: center;

      .item {
        display: flex;
        align-items: center;
        justify-content: center;
        flex-direction: column;
        font-size: $main-size;
        font-weight: 400;
        color: #666666;

        &.apple {
          margin-left: 40px;
        }

        image {
          width: 70rpx;
          height: 70rpx;
        }
      }
    }
  }
</style>
