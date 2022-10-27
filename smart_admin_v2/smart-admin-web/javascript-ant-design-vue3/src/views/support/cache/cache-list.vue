<!--
  * 缓存
  *
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2022-06-08 21:50:41
  * @Wechat:    zhuda1024
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
-->
<template>
  <a-card size="small" :bordered="false" :hoverable="true">
    <a-alert>
      <template v-slot:message>
        <h4>缓存 介绍：</h4>
      </template>
      <template v-slot:description>
        <pre>
简介：SmartAdmin使用的是SpringCache进行管理缓存，SpringCache有多种实现方式，本项目默认采用的是caffeine。
Caffeine ：
- Caffeine是一个进程内部缓存框架，使用了Java 8最新的[StampedLock]乐观锁技术，极大提高缓存并发吞吐量，一个高性能的 Java 缓存库，被称为最快缓存。
其他：
· 对于分布式、集群等应用实现方式可以改为 Redis、CouchBase等
</pre
        >
      </template>
    </a-alert>

    <a-table size="small" bordered class="smart-margin-top10" :dataSource="tableData" :columns="columns" rowKey="tag" :pagination="false" >
      <template #bodyCell="{ record, column }">
        <template v-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button @click="remove(record.key)" v-privilege="'support:cache:delete'" type="link">清除</a-button>
            <a-button @click="getAllKeys(record.key)" v-privilege="'support:cache:keys'" type="link">获取所有key</a-button>
          </div>
        </template>
      </template>
    </a-table>
  </a-card>
</template>
<script setup>
  import { message } from 'ant-design-vue';
  import { onMounted, reactive, ref, h } from 'vue';
  import { cacheApi } from '/@/api/support/cache/cache-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { Modal } from 'ant-design-vue';
  import _ from 'lodash';
  import { smartSentry } from '/@/lib/smart-sentry';

  //------------------------ 删除 ---------------------

  async function remove(key) {
    try {
      await cacheApi.remove(key);
      message.success('删除成功');
      ajaxQuery();
    } catch (e) {
      smartSentry.captureError(e);
    }
  }

  //------------------------ 获取所有key ---------------------
  async function getAllKeys(cacheName) {
    SmartLoading.show();
    try {
      let res = await cacheApi.getKeys(cacheName);
      SmartLoading.hide();
      Modal.info({
        title: '所有Key:' + cacheName,
        content: h('div', {}, [h('p', _.join(res.data, ' , '))]),
        onOk() {
          ajaxQuery();
        },
      });
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      SmartLoading.hide();
    }
  }

  //------------------------ 表格渲染 ---------------------

  const columns = reactive([
    {
      title: 'Key',
      dataIndex: 'key',
    },
    {
      title: '操作',
      dataIndex: 'action',
      fixed: 'right',
      width: 160,
    },
  ]);

  const tableLoading = ref(false);
  const tableData = ref([]);

  async function ajaxQuery() {
    try {
      tableLoading.value = true;
      let res = await cacheApi.getAllCacheNames();
      tableData.value = res.data.map((e) => Object.assign({}, { key: e }));
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      tableLoading.value = false;
    }
  }

  onMounted(ajaxQuery);
</script>
