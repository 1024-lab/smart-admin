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
  <a-form class="smart-query-form" v-privilege="'operateLog:query'">
    <a-row class="smart-query-form-row">
      <a-form-item label="用户名称" class="smart-query-form-item">
        <a-input style="width: 300px" v-model:value="queryForm.userName" placeholder="用户名称" />
      </a-form-item>

      <a-form-item label="请求时间" class="smart-query-form-item">
        <a-range-picker @change="changeCreateDate" v-model:value="createDateRange" :ranges="defaultChooseTimeRange" style="width: 240px" />
      </a-form-item>

      <a-form-item label="快速筛选" class="smart-query-form-item">
        <a-radio-group v-model:value="queryForm.successFlag" @change="ajaxQuery">
          <a-radio-button :value="undefined">全部</a-radio-button>
          <a-radio-button :value="true">成功</a-radio-button>
          <a-radio-button :value="false">失败</a-radio-button>
        </a-radio-group>
      </a-form-item>

      <a-form-item class="smart-query-form-item smart-margin-left10">
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
      </a-form-item>
    </a-row>
  </a-form>

  <a-card size="small" :bordered="false" :hoverable="true" style="height:100%">
    <a-row justify="end">
      <TableOperator class="smart-margin-bottom5" v-model="columns" :tableId="TABLE_ID_CONST.SUPPORT.CONFIG" :refresh="ajaxQuery" />
    </a-row>
    <a-table size="small" :loading="tableLoading" :dataSource="tableData" :columns="columns" bordered rowKey="operateLogId" :pagination="false" >
      <template #bodyCell="{ text, record, column }">
        <template v-if="column.dataIndex === 'successFlag'">
          <a-tag :color="text ? 'success' : 'error'">{{ text ? '成功' : '失败' }}</a-tag>
        </template>

        <template v-if="column.dataIndex === 'userAgent'">
          <div>{{ record.browser }} / {{ record.os }} / {{ record.device }}</div>
        </template>

        <template v-if="column.dataIndex === 'operateUserType'">
          <div>{{ $smartEnumPlugin.getDescByValue('USER_TYPE_ENUM', text) }}</div>
        </template>

        <template v-else-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button @click="showDetail(record.operateLogId)" type="link" v-privilege="'operateLog:detail'">详情</a-button>
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
  </a-card>
</template>
<script setup>
  import { onMounted, reactive, ref } from 'vue';
  import OperateLogDetailModal from './operate-log-detail-modal.vue';
  import { operateLogApi } from '/@/api/support/operate-log/operate-log-api';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { defaultTimeRanges } from '/@/lib/default-time-ranges';
  import uaparser from 'ua-parser-js';
  import { smartSentry } from '/@/lib/smart-sentry';
  import TableOperator from '/@/components/support/table-operator/index.vue';
  import { TABLE_ID_CONST } from '/@/constants/support/table-id-const';

  const columns = ref([
    {
      title: '用户',
      dataIndex: 'operateUserName',
      width: 70,
    },
    {
      title: '类型',
      dataIndex: 'operateUserType',
      width: 50,
      ellipsis: true,
    },
    {
      title: '操作模块',
      dataIndex: 'module',
      ellipsis: true,
    },
    {
      title: '操作内容',
      dataIndex: 'content',
      ellipsis: true,
    },
    {
      title: '请求路径',
      dataIndex: 'url',
      ellipsis: true,
    },
    {
      title: 'IP',
      dataIndex: 'ip',
      ellipsis: true,
    },
    {
      title: '客户端',
      dataIndex: 'userAgent',
      ellipsis: true,
    },
    {
      title: '请求方法',
      dataIndex: 'method',
      ellipsis: true,
    },
    {
      title: '请求结果',
      dataIndex: 'successFlag',
      width: 80,
    },
    {
      title: '时间',
      dataIndex: 'createTime',
      width: 150,
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
  async function ajaxQuery() {
    try {
      tableLoading.value = true;
      let responseModel = await operateLogApi.queryList(queryForm);

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
