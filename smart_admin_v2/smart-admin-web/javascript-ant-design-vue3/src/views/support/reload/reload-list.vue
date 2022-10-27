<!--
  * reload
  *
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2022-07-21 21:55:12
  * @Wechat:    zhuda1024
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
-->
<template>
  <a-card size="small" :bordered="false" :hoverable="true">
    <a-alert>
      <template v-slot:message>
        <h4>Smart-Reload 心跳服务介绍：</h4>
      </template>
      <template v-slot:description>
        <pre>
简介：SmartReload是一个可以在不重启进程的情况下动态重新加载配置或者执行某些预先设置的代码。

原理：
- Java后端会在项目启动的时候开启一个Daemon线程，这个Daemon线程会每隔几秒轮询t_smart_item表的状态。
- 如果【状态标识】与【上次状态标识】比较发生变化，会将参数传入SmartReload实现类，进行自定义操作。
用途：
· 用于刷新内存中的缓存
· 用于执行某些后门代码
· 用于进行Java热加载（前提是类结构不发生变化）
· 其他不能重启服务的应用
</pre
        >
      </template>
    </a-alert>

    <a-row justify="end">
      <TableOperator class="smart-margin-bottom5 smart-margin-top5" v-model="columns" :tableId="TABLE_ID_CONST.SUPPORT.RELOAD" :refresh="ajaxQuery" />
    </a-row>

    <a-table
      size="small"
      bordered
      class="smart-margin-top10"
      :dataSource="tableData"
      :loading="tableLoading"
      :columns="columns"
      rowKey="tag"
      :pagination="false"
    >
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button @click="doReload(record.tag)" v-privilege="'reload:execute'" type="link">执行</a-button>
            <a-button @click="showResultList(record.tag)" v-privilege="'reload:result'" type="link">查看结果</a-button>
          </div>
        </template>
      </template>
    </a-table>

    <DoReloadForm @refresh="ajaxQuery" ref="doReloadForm" />
    <ReloadResultList ref="reloadResultList" />
  </a-card>
</template>
<script setup>
  import { onMounted, reactive, ref } from 'vue';
  import DoReloadForm from './do-reload-form-modal.vue';
  import ReloadResultList from './reload-result-list.vue';
  import { reloadApi } from '/@/api/support/reload/reload-api';
  import { smartSentry } from '/@/lib/smart-sentry';
  import TableOperator from '/@/components/support/table-operator/index.vue';
  import { TABLE_ID_CONST } from '/@/constants/support/table-id-const';

  //------------------------ 表格渲染 ---------------------

  const columns = ref([
    {
      title: '标签',
      dataIndex: 'tag',
      width: 200,
    },
    {
      title: '运行标识',
      dataIndex: 'identification',
    },
    {
      title: '参数',
      dataIndex: 'args',
    },
    {
      title: '更新时间',
      dataIndex: 'updateTime',
      width: 150,
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      width: 150,
    },
    {
      title: '操作',
      dataIndex: 'action',
      fixed: 'right',
      width: 150,
    },
  ]);

  const tableLoading = ref(false);
  const tableData = ref([]);

  async function ajaxQuery() {
    try {
      tableLoading.value = true;
      let res = await reloadApi.queryList();
      tableData.value = res.data;
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      tableLoading.value = false;
    }
  }

  onMounted(ajaxQuery);

  // ------------------------------ 表格操作列： 执行 reload ------------------------------
  const doReloadForm = ref();
  function doReload(tag) {
    doReloadForm.value.showModal(tag);
  }

  // ------------------------------ 表格操作列： 查看执行结果 ------------------------------

  const reloadResultList = ref();
  function showResultList(tag) {
    reloadResultList.value.showModal(tag);
  }
</script>
