<!--
  * 喷头列表
  *
  * @Author:    海印:芦苇
-->
<template>
  <a-form class="smart-query-form" v-privilege="'sprinklermanager:sprinkler:query'">
    <a-row class="smart-query-form-row">
      <a-form-item label="关键字" class="smart-query-form-item">
        <a-input style="width: 300px" v-model:value="queryForm.keywords" placeholder="企业名称/联系人/联系电话" />
      </a-form-item>

      <a-form-item label="创建时间" class="smart-query-form-item">
        <a-space direction="vertical" :size="12">
          <a-range-picker v-model:value="searchDate" :presets="defaultTimeRanges" @change="dateChange" />
        </a-space>
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

  <a-card size="small" :bordered="false" :hoverable="true">
    <a-row class="smart-table-btn-block">
      <div class="smart-table-operate-block">
        <a-button @click="add()" v-privilege="'sprinkler:add'" type="primary">
          <template #icon>
            <PlusOutlined />
          </template>
          新建喷头
        </a-button>
        <a-button @click="exportExcel()" v-privilege="'oa:enterprise:exportExcel'" type="primary">
          <template #icon>
            <FileExcelOutlined />
          </template>
          导出数据（带水印）
        </a-button>
      </div>
      <div class="smart-table-setting-block">
        <TableOperator v-model="columns" :tableId="TABLE_ID_CONST.BUSINESS.SPRINKLERMANAGER.SPRINKLER" :refresh="ajaxQuery" />
      </div>
    </a-row>

    <a-table
      :scroll="{ x: 1300 }"
      size="small"
      :dataSource="tableData"
      :columns="columns"
      rowKey="sprinklerId"
      :pagination="false"
      :loading="tableLoading"
      bordered
    >
      <template #bodyCell="{ column, record, text }">
        <template v-if="column.dataIndex === 'disabledFlag'">
          {{ text ? '禁用' : '启用' }}
        </template>
        <template v-if="column.dataIndex === 'sprinklerSerial'">
          <a @click="detail(record.sprinklerId)">{{ record.sprinklerSerial }}</a>
        </template>
        <template v-if="column.dataIndex === 'type'">
          <span>{{ $smartEnumPlugin.getDescByValue('ENTERPRISE_TYPE_ENUM', text) }}</span>
        </template>
        <template v-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button @click="update(record.sprinklerId)" size="small" v-privilege="'oa:enterprise:update'" type="link">编辑</a-button>
            <a-button @click="confirmDelete(record.sprinklerId)" size="small" danger v-privilege="'oa:enterprise:delete'" type="link">删除</a-button>
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
    <EnterpriseOperate ref="operateRef" @refresh="ajaxQuery" />
  </a-card>
</template>
<script setup>
  import { reactive, ref, onMounted } from 'vue';
  import { message, Modal } from 'ant-design-vue';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { enterpriseApi } from '/@/api/business/oa/enterprise-api';
  import { PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { useRouter } from 'vue-router';
  import EnterpriseOperate from './components/enterprise-operate-modal.vue';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { defaultTimeRanges } from '/@/lib/default-time-ranges';
  import TableOperator from '/@/components/support/table-operator/index.vue';
  import { TABLE_ID_CONST } from '/@/constants/support/table-id-const';
  import {sprinklerApi} from "/@/api/business/sprinklermanager/sprinkler-api.js";

  // --------------------------- 企业表格 列 ---------------------------

  const columns = ref([
    {
      title: '喷头序列号',
      dataIndex: 'sprinklerSerial',
      minWidth: 180,
      ellipsis: true,
    },
    {
      title: '入仓日期',
      dataIndex: 'warehouseDate',
      minWidth: 170,
      ellipsis: true,
    },
    {
      title: '状态',
      width: 50,
      dataIndex: 'disabledFlag',
    },
    {
      title: '创建人',
      width: 60,
      dataIndex: 'createUserName',
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      width: 150,
    },
    {
      title: '操作',
      dataIndex: 'action',
      // fixed: 'right',
      width: 100,
    },
  ]);

  // --------------------------- 查询 ---------------------------

  const queryFormState = {
    keywords: '',
    endTime: null,
    startTime: null,
    pageNum: 1,
    pageSize: PAGE_SIZE,
    searchCount: true,
  };
  const queryForm = reactive({ ...queryFormState });
  const tableLoading = ref(false);
  const tableData = ref([]);
  const total = ref(0);

  // 日期选择
  let searchDate = ref();

  function dateChange(dates, dateStrings) {
    queryForm.startTime = dateStrings[0];
    queryForm.endTime = dateStrings[1];
  }

  function onSearch() {
    queryForm.pageNum = 1;
    ajaxQuery();
  }

  function resetQuery() {
    searchDate.value = [];
    Object.assign(queryForm, queryFormState);
    ajaxQuery();
  }

  async function ajaxQuery() {
    try {
      tableLoading.value = true;
      let responseModel = await sprinklerApi.pageQuery(queryForm);
      const list = responseModel.data.list;
      total.value = responseModel.data.total;
      tableData.value = list;
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      tableLoading.value = false;
    }
  }

  // --------------------------- 导出 ---------------------------
  async function exportExcel() {
    await enterpriseApi.exportExcel(queryForm);
  }

  // --------------------------- 删除 ---------------------------

  function confirmDelete(sprinklerId) {
    Modal.confirm({
      title: '确定要删除吗？',
      content: '删除后，该信息将不可恢复',
      okText: '删除',
      okType: 'danger',
      onOk() {
        del(sprinklerId);
      },
      cancelText: '取消',
      onCancel() {},
    });
  }

  async function del(sprinklerId) {
    try {
      SmartLoading.show();
      await enterpriseApi.delete(sprinklerId);
      message.success('删除成功');
      ajaxQuery();
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      SmartLoading.hide();
    }
  }

  // --------------------------- 增加、修改、详情 ---------------------------

  let router = useRouter();
  const operateRef = ref();
  function add() {
    operateRef.value.showModal();
  }

  function update(sprinklerId) {
    operateRef.value.showModal(sprinklerId);
  }

  function detail(sprinklerId) {
    router.push({ path: '/sprinklermanager/sprinkler/sprinkler-detail', query: { sprinklerId: sprinklerId } });
  }

  onMounted(ajaxQuery);
</script>
