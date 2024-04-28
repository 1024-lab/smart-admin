<template>
  <view class="container">
    <!-- 用户 -->
    <MineUserBlue v-if="blueUserFlag" />
    <MineUserWhite v-if="!blueUserFlag" />

    <!---功能菜单--->
    <MineMenu @change-style="onChangeStyle" />

    <!---退出--->
    <view class="logout-btn" @click="logout">
      <view class="label">退出登录</view>
    </view>
  </view>
</template>
<script setup>
  import MineMenu from './components/mine-menu.vue';
  import MineUserBlue from './components/mine-user-blue.vue';
  import MineUserWhite from './components/mine-user-white.vue';
  import { ref } from 'vue';
  import { useUserStore } from '@/store/modules/system/user';
  import { SmartLoading, SmartToast } from '@/lib/smart-support';
  import { smartSentry } from '@/lib/smart-sentry';

  const userStore = useUserStore();
  const blueUserFlag = ref(true);
  function onChangeStyle() {
    blueUserFlag.value = !blueUserFlag.value;
  }

  async function logout() {
    try {
      setTimeout(() => {
        userStore.logout();
        uni.navigateTo({ url: '/pages/login/login' });
      }, 500);
      await SmartLoading.show();
      SmartToast.toast('退出成功');
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      SmartLoading.hide();
    }
  }
</script>
<style lang="scss" scoped>
  .container {
    width: 100%;
    background: #f4f4f4;
    display: flex;
    flex-direction: column;
  }

  .logout-btn {
    margin: 24px 27px 50px;
    height: 44px;
    opacity: 0.5;
    background: $uni-color-error;
    border-radius: 22px;
    font-size: 15px;
    line-height: 44px;
    font-weight: 700;
    text-align: center;
    color: white;
  }
</style>
