<!--
  * 登录、登出 日志
  *
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2022-06-02 20:23:08
  * @Wechat:    zhuda1024
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
-->
<template>
  <a-form class="smart-query-form" ref="queryFormRef">
    <a-row class="smart-query-form-row">
      <a-form-item label="时间" class="smart-query-form-item">
        <a-range-picker @change="changeCreateDate" v-model:value="createDateRange" :presets="defaultChooseTimeRange" style="width: 240px" />
      </a-form-item>

      <a-form-item class="smart-query-form-item smart-margin-left10">
        <a-button-group>
          <a-button type="primary" @click="onSearch">
            <template #icon>
              <SearchOutlined />
            </template>
            查询
          </a-button>
          <a-button @click="resetQuery">
            <template #icon>
              <ReloadOutlined />
            </template>
            重置
          </a-button>
        </a-button-group>
      </a-form-item>
    </a-row>
  </a-form>

  <a-table size="small" :dataSource="tableData" :columns="columns" bordered rowKey="loginLogId" :pagination="false" :loading="tableLoading">
    <template #bodyCell="{ text, record, column }">
      <template v-if="column.dataIndex === 'loginResult'">
        <template v-if="text === LOGIN_RESULT_ENUM.LOGIN_SUCCESS.value">
          <a-tag color="success">登录成功</a-tag>
        </template>
        <template v-if="text === LOGIN_RESULT_ENUM.LOGIN_FAIL.value">
          <a-tag color="error">登录失败</a-tag>
        </template>
        <template v-if="text === LOGIN_RESULT_ENUM.LOGIN_OUT.value">
          <a-tag color="processing">退出登录</a-tag>
        </template>
      </template>
      <template v-if="column.dataIndex === 'userAgent'">
        <div>{{ record.browser }} / {{ record.os }} / {{ record.device }}</div>
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
</template>
<script setup>
  import { onMounted, onUnmounted, reactive, ref } from 'vue';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { defaultTimeRanges } from '/@/lib/default-time-ranges';
  import uaparser from 'ua-parser-js';
  import { LOGIN_RESULT_ENUM } from '/@/constants/support/login-log-const';
  import { loginLogApi } from '/@/api/support/login-log-api';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { calcTableHeight } from '/@/lib/table-auto-height';

  const columns = ref([
    {
      title: '时间',
      dataIndex: 'createTime',
      width: 150,
    },
    {
      title: '登录方式',
      dataIndex: 'remark',
      ellipsis: true,
      width: 90,
    },
    {
      title: '登录设备',
      dataIndex: 'userAgent',
      ellipsis: true,
    },
    {
      title: 'IP地区',
      dataIndex: 'loginIpRegion',
      ellipsis: true,
    },
    {
      title: 'IP',
      dataIndex: 'loginIp',
      ellipsis: true,
      width: 120,
    },
    {
      title: '结果',
      dataIndex: 'loginResult',
      ellipsis: true,
      width: 90,
    },
  ]);

  const queryFormState = {
    userName: '',
    ip: '',
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
      let responseModel = await loginLogApi.queryListLogin(queryForm);

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

  // ----------------- 表格自适应高度 --------------------
  const scrollY = ref(100);
  const queryFormRef = ref();

  function autoCalcTableHeight() {
    calcTableHeight(scrollY, [queryFormRef], 10);
  }

  window.addEventListener('resize', autoCalcTableHeight);

  onMounted(() => {
    ajaxQuery();
    autoCalcTableHeight();
  });

  onUnmounted(() => {
    window.removeEventListener('resize', autoCalcTableHeight);
  });
</script>
