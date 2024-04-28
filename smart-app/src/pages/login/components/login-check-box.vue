<template>
  <view class="check-box">
    <view class="check-item">
      <image @click="agree" :src="!agreeFlag ? checkOutImg : checkInImg" />
      <span>
        我已阅读并同意
        <span class="link" @click="openProtocol('user_agreement')">《用户协议》</span>
        与
        <span class="link" @click="openProtocol('privacy_terms')">《隐私政策》</span>
      </span>
    </view>
  </view>
</template>
<script setup>
  import checkOutImg from '/static/images/login/check-out.png';
  import checkInImg from '/static/images/login/check-in.png';

  import { ref } from 'vue';

  const agreeFlag = ref(true);

  function agree() {
    agreeFlag.value = !agreeFlag.value;
  }

  function openProtocol(protocolKey) {
    uni.navigateTo({
      url: `/pages/protocol/index?key=${protocolKey}`,
    });
  }

  defineExpose({
    agreeFlag,
  });
</script>
<style lang="scss" scoped>
  .check-box {
    .check-item {
      display: flex;
      align-items: center;
      font-size: 12px;
      font-weight: 400;
      color: #999999;
      margin-bottom: 20rpx;

      image {
        width: 24px;
        height: 24px;
        margin-right: 2px;
      }

      .link {
        color: $main-color;
      }
    }
  }
</style>
