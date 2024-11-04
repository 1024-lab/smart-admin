<!--
  * 通知  详情 （员工列表）
  *
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2022-08-21 19:52:43
  * @Wechat:    zhuda1024
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
-->
<template>
  <a-form class="smart-query-form">
    <a-row class="smart-query-form-row">
      <a-form-item label="关键字" class="smart-query-form-item">
        <a-input style="width: 200px" v-model:value="queryForm.keywords" placeholder="标题、作者、来源、文号" />
      </a-form-item>

      <a-form-item label="发布时间" class="smart-query-form-item">
        <a-range-picker v-model:value="publishDate" @change="publishDateChange" style="width: 220px" />
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
    <a-row class="smart-query-form-row"> </a-row>
  </a-form>

  <a-card size="small" :bordered="false">
    <a-tabs @change="changeNotViewFlag" size="small">
      <a-tab-pane :key="0" tab="全部" />
      <a-tab-pane :key="1" tab="未读" />
    </a-tabs>

    <a-table rowKey="noticeId" :columns="tableColumns" :dataSource="tableData" :pagination="false" :loading="tableLoading" bordered size="small">
      <template #bodyCell="{ column, record, text }">
        <template v-if="column.dataIndex === 'title'">
          <span v-show="record.viewFlag">
            <a @click="toDetail(record.noticeId)" style="color: #8c8c8c">【{{ record.noticeTypeName }}】{{ text }}（已读）</a>
          </span>
          <span v-show="!record.viewFlag">
            <a @click="toDetail(record.noticeId)"
              >【{{ record.noticeTypeName }}】{{ text }}
              <span style="color: red">（未读）</span>
            </a>
          </span>
        </template>
        <template v-if="column.dataIndex === 'pageViewCount'"> {{ record.userViewCount }}人 / {{ record.pageViewCount }}次 </template>
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
        @change="queryNoticeList"
        @showSizeChange="queryNoticeList"
        :show-total="(total) => `共${total}条`"
      />
    </div>
  </a-card>
</template>

<script setup>
  import { onMounted, reactive, ref } from 'vue';
  import { useRouter } from 'vue-router';
  import { PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { noticeApi } from '/@/api/business/oa/notice-api';
  import { smartSentry } from '/@/lib/smart-sentry';

  const tableColumns = reactive([
    {
      title: `标题`,
      dataIndex: 'title',
      ellipsis: true,
    },
    {
      title: '访问量',
      dataIndex: 'pageViewCount',
      width: 90,
    },
    {
      title: `来源`,
      dataIndex: 'source',
      width: 150,
      ellipsis: true,
    },
    {
      title: `作者`,
      dataIndex: 'author',
      width: 80,
      ellipsis: true,
    },
    {
      title: '发布时间',
      dataIndex: 'publishTime',
      width: 150,
    },
  ]);

  // ------------------ 通知分类 ------------------

  // 查询分类列表
  const noticeTypeList = ref([]);
  async function queryNoticeTypeList() {
    try {
      const result = await noticeApi.getAllNoticeTypeList();
      noticeTypeList.value = result.data;
    } catch (err) {
      smartSentry.captureError(err);
    }
  }

  // ------------------ 查询相关 ------------------

  const queryFormState = {
    noticeTypeId: undefined, //分类
    keywords: '', //标题、作者、来源
    publishTimeBegin: null, //发布-开始时间
    publishTimeEnd: null, //发布-截止时间
    notViewFlag: false, //未读
    pageNum: 1,
    pageSize: PAGE_SIZE,
  };
  const queryForm = reactive({ ...queryFormState });

  const tableData = ref([]);
  const total = ref(0);
  const tableLoading = ref(false);

  onMounted(() => {
    queryNoticeTypeList();
    queryNoticeList();
  });

  function changeNotViewFlag(value) {
    queryForm.notViewFlag = value === 0 ? null : true;
    onSearch();
  }

  // 查询列表
  async function queryNoticeList() {
    try {
      tableLoading.value = true;
      const result = await noticeApi.queryEmployeeNotice(queryForm);
      tableData.value = result.data.list;
      total.value = result.data.total;
    } catch (err) {
      smartSentry.captureError(err);
    } finally {
      tableLoading.value = false;
    }
  }

  // 点击查询
  function onSearch() {
    queryForm.pageNum = 1;
    queryNoticeList();
  }

  // 点击重置
  function onReload() {
    Object.assign(queryForm, queryFormState);
    publishDate.value = [];
    createDate.value = [];
    queryNoticeList();
  }

  // 发布日期选择
  const publishDate = ref([]);
  function publishDateChange(dates, dateStrings) {
    queryForm.publishTimeBegin = dateStrings[0];
    queryForm.publishTimeEnd = dateStrings[1];
  }
  // 创建日期选择
  const createDate = ref([]);
  function createDateChange(dates, dateStrings) {
    queryForm.createTimeBegin = dateStrings[0];
    queryForm.createTimeEnd = dateStrings[1];
  }

  // ------------------ 详情 ------------------

  // 进入详情
  const router = useRouter();
  function toDetail(noticeId) {
    router.push({
      path: '/oa/notice/notice-employee-detail',
      query: { noticeId },
    });
  }
</script>

<style lang="less" scoped></style>
