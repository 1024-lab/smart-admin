<!--
  *  员工 修改密码的 显示密码弹窗
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-08-08 20:46:18
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
-->
<template>
  <a-modal v-model:visible="visible" :zIndex="9999" :width="500" title="提示" :closable="false" :maskClosable="false">
    <!--  -->
    <ul>
      <li>登录名: {{ showLoginName }}</li>
      <li>密码: {{ showLoginPassword }}</li>
    </ul>
    <template #footer>
      <a-button
        type="primary"
        class="account-copy"
        :data-clipboard-text="`登录名：${showLoginName} 
密码：${showLoginPassword}`"
        size="middle"
        @click="copy"
        >复制密码并关闭</a-button
      >
    </template>
  </a-modal>
</template>
<script setup>
  import { message } from 'ant-design-vue';
  import Clipboard from 'clipboard';
  import { ref } from 'vue';

  let visible = ref(false); // 是否展示抽屉
  let showLoginName = ref(''); //登录名
  let showLoginPassword = ref(''); //登录密码

  function copy() {
    handleCopy();
    visible.value = false;
  }
  function showModal(loginName, loginPassword) {
    visible.value = true;
    showLoginName.value = loginName;
    showLoginPassword.value = loginPassword;
  }
  function handleCopy() {
    let clipboard = new Clipboard('.account-copy');
    clipboard.on('success', (e) => {
      message.info('复制成功');
      console.log('复制成功');
      //  释放内存
      clipboard.destroy();
    });
    clipboard.on('error', (e) => {
      // 不支持复制
      message.error('浏览器不支持复制，请您手动选择复制');
      // 释放内存
      clipboard.destroy();
    });
  }
  defineExpose({
    showModal,
  });
</script>
<style lang="less" scoped>
  ul {
    margin: 0;
    padding: 0;
    list-style: none;
    padding-left: 32%;
    li {
      font-weight: bold;
      font-size: 16px;
    }
  }
</style>
>
