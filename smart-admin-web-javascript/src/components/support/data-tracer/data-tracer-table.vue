<!--
  * 数据变更记录，以table形式显示
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-08-12 21:01:52 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
-->
<template>
  <a-table size="small" :dataSource="tableData" :columns="columns" rowKey="dataTracerId" :pagination="false" bordered>
    <template #bodyCell="{ record, index, column }">
      <template v-if="column.dataIndex === 'dataTracerId'">
        <div>{{ index + 1 }}</div>
      </template>
      <template v-if="column.dataIndex === 'userName'">
        <div>{{ record.userName }} ({{ $smartEnumPlugin.getDescByValue('USER_TYPE_ENUM', record.userType) }})</div>
      </template>
      <template v-if="column.dataIndex === 'userAgent'">
        <div>{{ record.browser }} / {{ record.os }} / {{ record.device }}</div>
      </template>
      <template v-if="column.dataIndex === 'content'">
        <div class="operate-content" v-html="record.content"></div>
      </template>
      <template v-else-if="column.dataIndex === 'action'">
        <a-button v-if="record.diffOld || record.diffNew" @click="showDetail(record)" type="link">详情 </a-button>
      </template>
    </template>
  </a-table>
</template>
<script setup>
  import { reactive } from 'vue';

  const props = defineProps({
    tableData: {
      type: Array,
    },
  });

  const emit = defineEmits(['showDetail']);
  function showDetail(record) {
    emit('showDetail', record);
  }

  const columns = reactive([
    {
      title: '序号',
      dataIndex: 'dataTracerId',
      width: 50,
    },
    {
      title: '操作时间',
      dataIndex: 'createTime',
      width: 150,
    },
    {
      title: '操作人',
      dataIndex: 'userName',
      width: 100,
      ellipsis: true,
    },
    {
      title: 'IP',
      dataIndex: 'ip',
      ellipsis: true,
      width: 100,
    },
    {
      title: 'IP地区',
      dataIndex: 'ipRegion',
      ellipsis: true,
      width: 100,
    },
    {
      title: '客户端',
      dataIndex: 'userAgent',
      ellipsis: true,
      width: 150,
    },
    {
      title: '操作内容',
      dataIndex: 'content',
    },
    {
      title: '操作',
      dataIndex: 'action',
      fixed: 'right',
      width: 80,
    },
  ]);
</script>
<style scoped lang="less">
  .operate-content {
    font-size: 14px;
    display: inline;
  }
</style>
