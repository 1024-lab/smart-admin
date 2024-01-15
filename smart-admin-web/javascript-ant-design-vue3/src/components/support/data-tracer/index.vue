<!--
  *  数据变动记录 表格 组件
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-08-12 21:01:52 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
  *
-->
<template>
  <a-form class="smart-query-form">
    <a-row class="smart-query-form-row">
      <a-form-item label="关键字" class="smart-query-form-item">
        <a-input style="width: 300px" v-model:value="queryForm.keywords" placeholder="变更内容" />
      </a-form-item>

      <a-form-item class="smart-query-form-item smart-margin-left10">
        <a-button-group>
          <a-button type="primary" @click="onSearch">
            <template #icon>
              <SearchOutlined />
            </template>
            查询
          </a-button>
          <a-button @click="onReload">
            <template #icon>
              <ReloadOutlined />
            </template>
            重置
          </a-button>
        </a-button-group>
      </a-form-item>
    </a-row>
  </a-form>

  <a-card size="small" :bordered="false">
    <!---以表格形式 显示-->
    <!-- <DataTracerTable :tableData="tableData" @showDetail="showDetail" /> -->

    <!---以 timeline 时间轴形式 显示-->
    <DataTracerTimeline :tableData="tableData" @showDetail="showDetail" />

    <a-pagination
      showSizeChanger
      showQuickJumper
      show-less-items
      :pageSizeOptions="PAGE_SIZE_OPTIONS"
      :defaultPageSize="queryForm.pageSize"
      v-model:current="queryForm.pageNum"
      v-model:pageSize="queryForm.pageSize"
      :total="total"
      @change="ajaxQuery"
      @showSizeChange="ajaxQuery"
      :show-total="(total) => `共${total}条`"
    />
    <a-modal v-model:open="visibleDiff" width="90%" title="数据比对" :footer="null">
      <div v-html="prettyHtml"></div>
    </a-modal>
  </a-card>
</template>
<script setup>
  import * as Diff from 'diff';
  import * as Diff2Html from 'diff2html';
  import 'diff2html/bundles/css/diff2html.min.css';
  import uaparser from 'ua-parser-js';
  import { nextTick, reactive, ref, watch } from 'vue';
  import { dataTracerApi } from '/src/api/support/data-tracer-api';
  import { PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { smartSentry } from '/@/lib/smart-sentry';
  import DataTracerTimeline from './data-tracer-timeline.vue';

  let props = defineProps({
    // 数据id
    dataId: {
      type: Number,
    },
    // 数据 类型
    type: {
      type: Number,
    },
  });

  // --------------- 查询表单、查询方法 ---------------

  const queryFormState = {
    pageNum: 1,
    pageSize: PAGE_SIZE,
    searchCount: true,
    keywords: undefined,
  };
  const queryForm = reactive({ ...queryFormState });
  const tableLoading = ref(false);
  const tableData = ref([]);
  const total = ref(0);

  function onReload() {
    Object.assign(queryForm, queryFormState);
    onSearch();
  }

  function onSearch() {
    queryForm.pageNum = 1;
    ajaxQuery();
  }

  async function ajaxQuery() {
    try {
      tableLoading.value = true;
      let responseModel = await dataTracerApi.queryList(Object.assign({}, queryForm, { dataId: props.dataId, type: props.type }));
      for (const e of responseModel.data.list) {
        if (!e.userAgent) {
          continue;
        }
        // e.content = e.content.replaceAll('<br/>','；');
        let ua = uaparser(e.userAgent);
        e.browser = ua.browser.name;
        e.os = ua.os.name;
        e.device = ua.device.vendor ? ua.device.vendor + ua.device.model : '';
      }
      const list = responseModel.data.list;
      total.value = responseModel.data.total;
      tableData.value = list;
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      tableLoading.value = false;
    }
  }

  // ========= 定义 watch 监听 ===============
  watch(
    () => props.dataId,
    (e) => {
      if (e) {
        queryForm.dataId = e;
        onSearch();
      }
    },
    { immediate: true }
  );

  // --------------- diff 特效 ---------------
  // diff
  const visibleDiff = ref(false);
  let prettyHtml = ref('');
  function showDetail(record) {
    visibleDiff.value = true;
    let diffOld = record.diffOld.replaceAll('<br/>', '\r\n');
    let diffNew = record.diffNew.replaceAll('<br/>', '\r\n');
    const args = ['', diffOld, diffNew, '变更前', '变更后'];

    let diffPatch = Diff.createPatch(...args);
    let html = Diff2Html.html(diffPatch, {
      drawFileList: false,
      matching: 'words',
      diffMaxChanges: 1000,
      outputFormat: 'side-by-side',
    });

    prettyHtml.value = html;
    nextTick(() => {
      let diffDiv = document.querySelectorAll('.d2h-file-side-diff');
      if (diffDiv.length > 0) {
        let left = diffDiv[0],
          right = diffDiv[1];
        left.addEventListener('scroll', function (e) {
          if (left.scrollLeft !== right.scrollLeft) {
            right.scrollLeft = left.scrollLeft;
          }
        });
        right.addEventListener('scroll', function (e) {
          if (left.scrollLeft !== right.scrollLeft) {
            left.scrollLeft = right.scrollLeft;
          }
        });
      }
    });
  }
</script>
