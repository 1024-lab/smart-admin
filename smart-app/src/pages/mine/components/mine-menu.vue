<template>
  <view>
    <view class="menu-list">
      <uni-list>
        <uni-list-item title="切换样式" link showBadge @click="changeStyle">
          <template #header>
            <image class="icon" src="/static/images/mine/mine-account.png" mode=""></image>
          </template>
        </uni-list-item>
        <uni-list-item title="消息通知" link="switchTab" showBadge badgeText="6" badgeType="error" to="/pages/message/message">
          <template #header>
            <image class="icon" src="/static/images/mine/mine-message.png" mode=""></image>
          </template>
        </uni-list-item>

        <uni-list-item title="意见反馈" link rightText="欢迎吐槽" showBadge to="/pages/support/feedback/feedback-form">
          <template #header>
            <image class="icon" src="/static/images/mine/mine-feedback.png" mode=""></image>
          </template>
        </uni-list-item>
        <uni-list-item title="联系客服" showBadge clickable @click="callService">
          <template #header>
            <image class="icon" src="/static/images/mine/mine-service.png" mode=""></image>
          </template>
          <template #footer>
            <view style="font-size: 30rpx; color: #1a9aff; line-height: 45rpx"> 18810241024 </view>
          </template>
        </uni-list-item>
        <uni-list-item title="账号管理" showBadge link>
          <template #header>
            <image class="icon" src="/static/images/mine/mine-account.png" mode=""></image>
          </template>
        </uni-list-item>
      </uni-list>
    </view>

    <view class="menu-list">
      <uni-list>
        <uni-list-item title="关于我们" link showBadge>
          <template #header>
            <image class="icon" src="/static/images/mine/mine-about-us.png" mode=""></image>
          </template>
        </uni-list-item>
        <uni-list-item title="版本信息" link rightText="V1.0" showBadge>
          <template #header>
            <image class="icon" src="/static/images/mine/mine-version-info.png" mode=""></image>
          </template>
        </uni-list-item>
        <uni-list-item title="隐私条款" link showBadge>
          <template #header>
            <image class="icon" src="/static/images/mine/user-agreement-icon.png" mode=""></image>
          </template>
        </uni-list-item>
        <uni-list-item title="用户协议" link showBadge>
          <template #header>
            <image class="icon" src="/static/images/mine/mine-about-us.png" mode=""></image>
          </template>
        </uni-list-item>
        <uni-list-item title="设置" showBadge clickable @click="developing">
          <template #header>
            <image class="icon" src="/static/images/mine/mine-protocol.png"></image>
          </template>
        </uni-list-item>
      </uni-list>
    </view>
  </view>
</template>
<script setup>
  import { SmartToast } from '@/lib/smart-support';

  const emits = defineEmits(['changeStyle']);

  function changeStyle() {
    emits('changeStyle');
  }

  function developing() {
    SmartToast.toast('敬请期待');
  }

  function callService() {
    uni.makePhoneCall({
      phoneNumber: '18637925892',
    });
  }
</script>
<style scoped lang="scss">
  .menu-list {
    box-sizing: border-box;
    background-color: #fff;
    margin: 16px 12px 0 12px;
    border-radius: 6px;
    overflow: hidden;
    box-shadow: 0 3px 4px 0 rgba(24, 144, 255, 0.06);
    padding: 0 10rpx;
    :deep(.uni-list-item__content-title) {
      font-size: 30rpx;
    }
    :deep(.uni-list-item__extra-text) {
      font-size: 28rpx;
    }
    .icon {
      width: 56rpx;
      height: 56rpx;
      margin-right: 20rpx;
    }
  }
</style>
