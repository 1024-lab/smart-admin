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
  <a-modal :open="visible" width="600px" :bodyStyle="{height:'400px'}"   title="" :closable="false" :maskClosable="true">
    <a-row><div style="font-weight:bolder;margin: 0 auto;font-size: 16px;color: red;">重磅更新：国产数据库支持🎉🎉</div> </a-row>
    <a-row><div style="font-weight:bolder;margin: 10px auto;font-size: 16px;color: red;">国产数据库：达梦、人大金仓、华为高斯GaussDB 等🎉🎉</div> </a-row>
    <a-row><div style="font-weight:bolder;margin: 0 auto;font-size: 16px;color: red;">主流数据库：Mysql、PostgreSQL 等🎉🎉</div> </a-row>
    <br />
    <div class="app-qr-box">
      <div class="app-qr">
        <a-image
            :width="200"
            style="border-radius: 15px;"
            src="https://img.smartadmin.1024lab.net/wechat/zhuoda-wechat.jpg"
        />

        <span class="qr-desc strong"> 添加“卓大”微信，备注对应数据库，如 达梦</span>
      </div>
    </div>
    <template #footer>
      <a-button type="default" @click="hide">知道了</a-button>
      <a-button danger type="default" @click="goto" target="_blank" href="https://smartadmin.vip/views/other/china-db/">去看看国产数据库，了解一下</a-button>
    </template>
  </a-modal>
</template>
<script setup lang="ts">
import {ref} from 'vue';

defineExpose({
    show,
  });

  const visible = ref(true);
  function goto(){

  }
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
        font-size: 15px;
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
