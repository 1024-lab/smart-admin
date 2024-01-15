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
  <a-modal :open="visible" width="600px" title="联系客服人员" :closable="false" :maskClosable="true">
    <a-row><div style="margin-left: 180px;font-weight:bolder">客服(卓大)电话：18637925892 ;</div> </a-row>
    <br />
    <div class="app-qr-box">
      <div class="app-qr">
        <img :src="zhuoda" />
        <span class="qr-desc strong"> 卓大的微信号！ </span>
        <span class="qr-desc"> 骚扰卓大 :) </span>
      </div>
      <div class="app-qr">
        <img :src="xiaozhen" />
        <span class="qr-desc strong"> 六边形工程师 </span>
        <span class="qr-desc"> 赚钱、代码、生活 </span>
      </div>
      <div class="app-qr">
        <img :src="lab1024" />
        <span class="qr-desc strong"> 1024创新实验室 </span>
        <span class="qr-desc"> 官方账号 </span>
      </div>
    </div>
    <template #footer>
      <a-button type="primary" @click="hide">知道了</a-button>
    </template>
  </a-modal>
</template>
<script setup>
  import { ref, reactive, nextTick, computed } from 'vue';
  import zhuoda from '/@/assets/images/1024lab/zhuoda-wechat.jpg';
  import lab1024 from '/@/assets/images/1024lab/1024lab-gzh.jpg';
  import xiaozhen from '/@/assets/images/1024lab/gzh.jpg';

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
    height: 170px;
    align-items: center;
    justify-content: space-around;
    .app-qr {
      display: flex;
      align-items: center;
      width: 33%;
      justify-content: center;
      flex-direction: column;
      > img {
        width: 100%;
        max-width: 150px;
        height: 100%;
        max-height: 150px;
      }
      .strong {
        font-weight: 600;
      }
      .qr-desc {
        display: flex;
        align-items: center;
        font-size: 12px;
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
