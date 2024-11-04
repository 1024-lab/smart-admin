<!--
  * 数据变更记录，以 timeline 形式显示
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-08-12 21:01:52 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
-->
<template>
  <a-timeline>
    <a-timeline-item v-for="record in tableData" :key="record.dataTracerId">
      <div class="trace-div">
        <div>
          <!-- <div class="operate-content" >{{ record.content }}</div> -->
          <div class="operate-content" v-html="record.content"></div>
          <a href="javascript:void(0)" v-if="record.diffOld || record.diffNew" @click="showDetail(record)">（查看修改）</a>
        </div>
        <div class="ip-font">
          {{ record.createTime }} | {{ record.userName }} | {{ record.ipRegion }} | {{ record.ip }} | {{ record.browser }} | {{ record.os }}
        </div>
      </div>
    </a-timeline-item>
  </a-timeline>
</template>
<script setup>
  const props = defineProps({
    tableData: {
      type: Array,
    },
  });

  const emit = defineEmits(['showDetail']);
  function showDetail(record) {
    emit('showDetail', record);
  }
</script>
<style scoped lang="less">
  .operate-content {
    font-size: 14px;
    display: inline;
  }

  .ip-font {
    margin-top: 5px;
    font-size: 12px;
    color: #999;
    display: block;
  }
</style>
