<!--
  * 操作记录 列表
  *
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2022-06-02 20:23:08
  * @Wechat:    zhuda1024
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
-->
<template>
  <a-form class="smart-query-form">
    <a-row class="smart-query-form-row">
      <a-form-item label="请求时间" class="smart-query-form-item">
        <a-range-picker @change="changeCreateDate" v-model:value="createDateRange" :presets="defaultChooseTimeRange" style="width: 240px" />
      </a-form-item>

      <a-form-item label="快速筛选" class="smart-query-form-item">
        <a-radio-group v-model:value="queryForm.successFlag" @change="onSearch">
          <a-radio-button :value="undefined">全部</a-radio-button>
          <a-radio-button :value="true">成功</a-radio-button>
          <a-radio-button :value="false">失败</a-radio-button>
        </a-radio-group>
      </a-form-item>

      <a-form-item class="smart-query-form-item smart-margin-left10">
        <a-button-group>
          <a-button type="primary" @click="ajaxQuery">
            <template #icon>
              <ReloadOutlined />
            </template>
            查询
          </a-button>
          <a-button @click="resetQuery">
            <template #icon>
              <SearchOutlined />
            </template>
            重置
          </a-button>
        </a-button-group>
      </a-form-item>
    </a-row>
  </a-form>

  <a-table size="small" :loading="tableLoading" :dataSource="tableData" :columns="columns" bordered rowKey="operateLogId" :pagination="false">
    <template #bodyCell="{ text, record, column }">
      <template v-if="column.dataIndex === 'successFlag'">
        <a-tag :color="text ? 'success' : 'error'">{{ text ? '成功' : '失败' }}</a-tag>
      </template>
      <template v-if="column.dataIndex === 'userAgent'">
        <div>{{ record.browser }} / {{ record.os }} / {{ record.device }}</div>
      </template>
      <template v-if="column.dataIndex === 'action'">
        <div class="smart-table-operate">
          <a-button @click="showDetail(record.operateLogId)" type="link">详情</a-button>
        </div>
      </template>
    </template>
  </a-table>

  <div class="smart-query-table-page">
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
  </div>

  <OperateLogDetailModal ref="detailModal" />
</template>
<script setup>
  import { onMounted, reactive, ref } from 'vue';
  import OperateLogDetailModal from '/@/views/support/operate-log/operate-log-detail-modal.vue';
  import { operateLogApi } from '/@/api/support/operate-log-api';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { defaultTimeRanges } from '/@/lib/default-time-ranges';
  import uaparser from 'ua-parser-js';
  import { smartSentry } from '/@/lib/smart-sentry';

  const columns = ref([
    {
      title: '操作模块',
      dataIndex: 'module',
      ellipsis: true,
      width: 120,
    },
    {
      title: '操作内容',
      dataIndex: 'content',
      ellipsis: true,
    },
    {
      title: 'IP地区',
      dataIndex: 'ipRegion',
      ellipsis: true,
      width: 120,
    },
    {
      title: '客户端',
      dataIndex: 'userAgent',
      ellipsis: true,
      width: 140,
    },
    {
      title: '时间',
      dataIndex: 'createTime',
      width: 150,
    },
    {
      title: '结果',
      dataIndex: 'successFlag',
      width: 60,
    },
    {
      title: '操作',
      dataIndex: 'action',
      fixed: 'right',
      width: 60,
    },
  ]);

  const queryFormState = {
    userName: '',
    successFlag: undefined,
    startDate: undefined,
    endDate: undefined,
    pageNum: 1,
    pageSize: 10,
  };
  const queryForm = reactive({ ...queryFormState });
  const createDateRange = ref([]);
  const defaultChooseTimeRange = defaultTimeRanges;
  // 时间变动
  function changeCreateDate(dates, dateStrings) {
    queryForm.startDate = dateStrings[0];
    queryForm.endDate = dateStrings[1];
  }

  const tableLoading = ref(false);
  const tableData = ref([]);
  const total = ref(0);

  function resetQuery() {
    Object.assign(queryForm, queryFormState);
    createDateRange.value = [];
    ajaxQuery();
  }

  function onSearch() {
    queryForm.pageNum = 1;
    ajaxQuery();
  }

  async function ajaxQuery() {
    try {
      tableLoading.value = true;
      let responseModel = await operateLogApi.queryListLogin(queryForm);

      for (const e of responseModel.data.list) {
        if (!e.userAgent) {
          continue;
        }
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

  onMounted(ajaxQuery);

  // ---------------------- 详情 ----------------------
  const detailModal = ref();
  function showDetail(operateLogId) {
    detailModal.value.show(operateLogId);
  }
</script>
