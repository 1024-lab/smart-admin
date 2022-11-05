<!--
  * 心跳记录
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-06-02 20:23:08 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
-->
<template>
  <a-card size="small" :bordered="false" :hoverable="true">
    <a-alert>
      <template v-slot:message>
        <h4>Smart-Heart-Beat 心跳服务介绍：</h4>
      </template>
      <template v-slot:description>
        <pre>
简介：Smart-Heart-Beat 是心跳服务，用于监测Java应用的状态等其他信息。
原理：Java后端会在项目启动的时候开启一个线程，每隔一段时间将该应用的IP、进程号更新到数据库t_heart_beat_record表中。

用途：
1）在各个环境（无论开发、测试、生产）能统一看到所有启动的服务列表；
2）检测Java应用是否存活；
3）当某些业务只允许有一个服务启动的时候，用于排查是否别人也启动的服务；
4） ※强烈推荐※</pre
        >
      </template>
    </a-alert>

    <a-form class="smart-query-form">
      <a-row class="smart-query-form-row">
        <a-form-item label="关键字" class="smart-query-form-item">
          <a-input style="width: 300px" v-model:value="queryForm.keywords" placeholder="关键字" />
        </a-form-item>

        <a-form-item label="心跳时间" class="smart-query-form-item">
          <a-range-picker @change="changeCreateDate" v-model:value="createDateRange" :ranges="defaultChooseTimeRange" style="width: 240px" />
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
      <a-row justify="end">
        <TableOperator class="smart-margin-bottom5" v-model="columns" :tableId="TABLE_ID_CONST.SUPPORT.HEART_BEAT" :refresh="ajaxQuery" />
      </a-row>
      <a-table
        size="small"
        bordered
        :loading="tableLoading"
        class="smart-margin-top10"
        :dataSource="tableData"
        :columns="columns"
        rowKey="goodsId"
        :pagination="false"
      />
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
  </a-card>
</template>
<script setup>
  import { onMounted, reactive, ref } from 'vue';
  import { heartBeatApi } from '/@/api/support/heart-beat/heart-beat-api';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { defaultTimeRanges } from '/@/lib/default-time-ranges';
  import { smartSentry } from '/@/lib/smart-sentry';
  import TableOperator from '/@/components/support/table-operator/index.vue';
  import { TABLE_ID_CONST } from '/@/constants/support/table-id-const';

  //------------------------ 时间选择 ---------------------
  const defaultChooseTimeRange = defaultTimeRanges;
  const createDateRange = ref([]);
  // 时间变动
  function changeCreateDate(dates, dateStrings) {
    queryForm.startDate = dateStrings[0];
    queryForm.endDate = dateStrings[1];
  }

  //------------------------ 表格渲染 ---------------------

  const columns = ref([
    {
      title: '项目路径',
      dataIndex: 'projectPath',
      ellipsis: true,
    },
    {
      title: '服务器ip',
      dataIndex: 'serverIp',
      ellipsis: true,
    },
    {
      title: '进程号',
      dataIndex: 'processNo',
      width:100
    },
    {
      title: '进程开启时间',
      dataIndex: 'processStartTime',
      width:150
    },
    {
      title: '心跳当前时间',
      dataIndex: 'heartBeatTime',
      width:150
    },
  ]);

  const queryFormState = {
    pageNum: 1,
    pageSize: 10,
    keywords: '',
    startDate: undefined,
    endDate: undefined,
  };
  const queryForm = reactive({ ...queryFormState });
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
      let responseModel = await heartBeatApi.queryList(queryForm);
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
</script>
