<!--
  * 意见反馈
  * 
  * @Author:    1024创新实验室：开云
  * @Date:      2022-07-21 21:55:12
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
-->
<template>
  <a-form class="smart-query-form">
    <a-row class="smart-query-form-row">
      <a-form-item label="关键字" class="smart-query-form-item" style="margin-right: 20px">
        <a-input style="width: 240px" v-model:value.trim="queryForm.searchWord" placeholder="反馈内容/创建人" />
      </a-form-item>
      <a-form-item label="创建日期" class="smart-query-form-item" style="margin-right: 20px">
        <a-range-picker
          v-model:value="chooseTimeRange"
          @change="changeCreateDate"
          :ranges="defaultTimeRanges"
          format="YYYY-MM-DD"
          style="width: 240px"
        />
      </a-form-item>
      <a-form-item class="smart-query-form-item">
        <a-button-group v-privilege="'feedback:query'">
          <a-button type="primary" @click="onSearch">
            <template #icon>
              <SearchOutlined />
            </template>
            查询
          </a-button>
          <a-button @click="onReset">
            <template #icon>
              <ReloadOutlined />
            </template>
            重置
          </a-button>
        </a-button-group>
      </a-form-item>
    </a-row>
  </a-form>

  <a-card size="small">
    <a-table rowKey="feedbackId" :dataSource="tableData" :columns="tableColumns" :pagination="false" :loading="tableLoading" size="small" bordered>
      <template #bodyCell="{ text, record, column }">
        <template v-if="column.dataIndex === 'feedbackAttachment'">
          <FilePreview :fileList="text" />
        </template>
        <template v-if="column.dataIndex === 'userType'">
          <span>{{ $smartEnumPlugin.getDescByValue('USER_TYPE_ENUM', text) }}</span>
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
        @change="queryList"
        @showSizeChange="queryList"
        :show-total="(total) => `共${total}条`"
      />
    </div>
  </a-card>
</template>

<script setup>
  import { onMounted, reactive, ref } from 'vue';
  import { PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { defaultTimeRanges } from '/@/lib/default-time-ranges';
  import { feedbackApi } from '/@/api/support/feedback/feedback-api';
  import FilePreview from '/@/components/support/file-preview/index.vue';
  import { smartSentry } from '/@/lib/smart-sentry';

  // ----------------------- 表格列 --------------------------------------
  const tableColumns = reactive([
    {
      title: '编号',
      dataIndex: 'feedbackId',
      width: 80,
    },
    {
      title: '反馈内容',
      dataIndex: 'feedbackContent',
    },
    {
      title: '反馈图片',
      dataIndex: 'feedbackAttachment',
    },
    {
      title: '反馈人',
      dataIndex: 'userName',
      width: 100,
    },
    {
      title: '反馈人类型',
      dataIndex: 'userType',
      width: 100,
    },
    {
      title: '反馈时间',
      dataIndex: 'createTime',
      width: 150,
    },
  ]);

  // ----------------------- 查询参数 ------------------------------------
  const defaultQueryForm = {
    startDate: undefined,
    endDate: undefined,
    searchWord: undefined,
    pageNum: 1,
    pageSize: PAGE_SIZE,
  };
  const queryForm = reactive({ ...defaultQueryForm });
  const tableLoading = ref(false);
  const tableData = ref([]);
  const total = ref(0);

  onMounted(() => {
    queryList();
  });

  async function queryList() {
    try {
      tableLoading.value = true;
      const result = await feedbackApi.queryFeedback(queryForm);
      tableData.value = result.data.list;
      total.value = result.data.total;
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      tableLoading.value = false;
    }
  }

  // 处理选择日期范围
  const chooseTimeRange = ref([]);

  function changeCreateDate(value, dateString) {
    queryForm.startDate = dateString[0];
    queryForm.endDate = dateString[1];
  }

  // 点击查询
  function onSearch() {
    queryForm.pageNum = 1;
    queryList();
  }

  // 点击重置
  function onReset() {
    Object.assign(queryForm, defaultQueryForm);
    chooseTimeRange.value = [];
    queryList();
  }

  // ----------------------- 分页方法 ------------------------------------
</script>
