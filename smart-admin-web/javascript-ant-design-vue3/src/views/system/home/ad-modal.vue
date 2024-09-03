<!--
  *  客服人员弹窗
  *
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2022-09-06 20:40:16
  * @Wechat:    zhuda1024
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
-->
<template>
  <a-modal :open="visible" width="600px" :bodyStyle="{height:'480px'}"   title="" :closable="false" :maskClosable="true">
    <a-row><div style="font-weight:bolder;margin: 0 auto;font-size: 16px">助力卓大抖音1000个粉丝，开播写代码🎉🎉</div> </a-row>
    <a-row><div style="font-weight:bolder;margin: 20px auto;font-size: 15px">和1024创新实验室一起，热爱代码，热爱生活，永远年轻，永远前行🎉🎉</div> </a-row>
    <br />
    <div class="app-qr-box">
      <div class="app-qr">
        <a-image
            :width="300"
            style="border-radius: 15px;"
            src="https://img.smartadmin.1024lab.net/wechat/douyin.png"
        />

        <span class="qr-desc strong"> 打开【抖音APP】-点击【左上角侧边栏】-【点击扫一扫】-【进行关注】</span>
      </div>
    </div>
    <template #footer>
      <a-button type="primary" @click="hide">知道了</a-button>
    </template>
  </a-modal>
</template>
<script setup>
import {ref} from 'vue';

defineExpose({
    show,
  });

  const visible = ref(false);
  function show() {
    visible.value = true;
  }
  function hide() {
    visible.value = false;
  }
</script>
<style lang="less" scoped>
  .app-qr-box {
    display: flex;
    align-items: center;
    justify-content: space-around;
    .app-qr {
      display: flex;
      align-items: center;
      justify-content: center;
      flex-direction: column;
      > img {
        width: 100%;
        height: 100%;
      }
      .strong {
        font-weight: 600;
      }
      .qr-desc {
        display: flex;
        margin-top: 20px;
        align-items: center;
        font-size: 13px;
        color: red;
        text-align: center;
        overflow-x: hidden;
        > img {
          width: 15px;
          height: 18px;
          margin-right: 9px;
        }
      }
    }
  }

  .ant-carousel :deep(.slick-slide) {
    text-align: center;
    height: 120px;
    line-height: 120px;
    width: 120px;
    background: #364d79;
    overflow: hidden;
  }

  .ant-carousel :deep(.slick-slide h3) {
    color: #fff;
  }
</style>
