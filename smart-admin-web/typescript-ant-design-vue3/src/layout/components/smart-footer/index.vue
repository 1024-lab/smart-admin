<!--
  * 底部：版权、公司等信息
  *
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2022-09-06 20:29:12
  * @Wechat:    zhuda1024
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
-->
<template>
  <div class="version" >
    <a target="_blank" href="https://www.1024lab.net">
      SmartAdmin V3.x @copyright 【1024创新实验室】 Since 2012-{{ currentYear }}
    </a>
  </div>
</template>
<script setup lang="ts">
  import dayjs from 'dayjs';

  const currentYear = dayjs().year();
</script>

<style lang="less" scoped>
  .version {
    font-size: 14px;
    color: rgba(0, 0, 0, 0.45);
    a {
      color: rgba(0, 0, 0, 0.45);
    }
    a:hover {
      color: @primary-color;
    }
  }
</style>
