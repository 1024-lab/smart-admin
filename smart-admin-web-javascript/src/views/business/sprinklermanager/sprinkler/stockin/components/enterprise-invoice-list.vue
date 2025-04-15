<!--
  * 企业 发票信息
  *
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2022-08-15 20:15:49
  * @Wechat:    zhuda1024
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
-->
<template>
  <a-form class="smart-query-form">
    <a-row class="smart-query-form-row">
      <a-form-item label="关键字" class="smart-query-form-item">
        <a-input style="width: 300px" v-model:value="queryForm.keywords" placeholder="开票抬头/银行账户/创建人" />
      </a-form-item>

      <a-form-item label="创建时间" class="smart-query-form-item">
        <a-space direction="vertical" :size="12">
          <a-range-picker v-model:value="searchDate" @change="dateChange" />
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

        <a-button @click="addOrUpdate()" type="primary" class="smart-margin-left20">
          <template #icon>
            <PlusOutlined />
          </template>
          新建发票
        </a-button>
      </a-form-item>
    </a-row>
  </a-form>

  <a-card size="small" :bordered="false" :hoverable="false">
    <a-row justify="end">
      <TableOperator class="smart-margin-bottom5" v-model="columns" :tableId="TABLE_ID_CONST.BUSINESS.OA.ENTERPRISE_INVOICE" :refresh="ajaxQuery" />
    </a-row>
    <a-table :scroll="{ x: 1300 }" size="small" :dataSource="tableData" :columns="columns" rowKey="invoiceId" :pagination="false" bordered>
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'disabledFlag'">
          {{ record.disabledFlag ? '禁用' : '启用' }}
        </template>
        <template v-else-if="column.dataIndex === 'action'">
          <div class="smart-table-operate">
            <a-button @click="addOrUpdate(record)" type="link">编辑</a-button>
            <a-button @click="confirmDelete(record.invoiceId)" type="link" danger>删除</a-button>
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
    <!--新建编辑modal-->
    <InvoiceOperateModal ref="operateModal" :enterpriseId="enterpriseId" @reloadList="ajaxQuery" />
  </a-card>
</template>
<script setup>
  import { reactive, ref, watch } from 'vue';
  import { invoiceApi } from '/@/api/business/oa/invoice-api';
  import { PAGE_SIZE, PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import InvoiceOperateModal from './enterprise-invoice-operate-modal.vue';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { message, Modal } from 'ant-design-vue';
  import { smartSentry } from '/@/lib/smart-sentry';
  import TableOperator from '/@/components/support/table-operator/index.vue';
  import { TABLE_ID_CONST } from '/@/constants/support/table-id-const';

  const props = defineProps({
    enterpriseId: {
      type: Number,
      default: null,
    },
  });

  const columns = ref([
    {
      title: 'ID',
      width: 50,
      dataIndex: 'invoiceId',
    },
    {
      title: '开票抬头',
      dataIndex: 'invoiceHeads',
      ellipsis: true,
    },
    {
      title: '纳税人识别号',
      dataIndex: 'taxpayerIdentificationNumber',
      ellipsis: true,
    },
    {
      title: '银行账号',
      width: 100,
      dataIndex: 'accountNumber',
      ellipsis: true,
    },
    {
      title: '开户行',
      width: 120,
      dataIndex: 'bankName',
    },
    {
      title: '状态',
      width: 80,
      dataIndex: 'disabledFlag',
    },
    {
      title: '备注',
      width: 100,
      dataIndex: 'remark',
    },
    {
      title: '创建人',
      width: 100,
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
      fixed: 'right',
      width: 100,
    },
  ]);

  const queryFormState = {
    enterpriseId: props.enterpriseId,
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
  const operateModal = ref();

  // 日期选择
  let searchDate = ref();

  function dateChange(dates, dateStrings) {
    queryForm.startTime = dateStrings[0];
    queryForm.endTime = dateStrings[1];
  }

  function resetQuery() {
    searchDate.value = [];
    Object.assign(queryForm, queryFormState);
    ajaxQuery();
  }

  function onSearch() {
    queryForm.pageNum = 1;
    ajaxQuery();
  }

  async function ajaxQuery() {
    try {
      tableLoading.value = true;
      let responseModel = await invoiceApi.pageQuery(queryForm);
      const list = responseModel.data.list;
      total.value = responseModel.data.total;
      tableData.value = list;
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      tableLoading.value = false;
    }
  }

  function confirmDelete(invoiceId) {
    Modal.confirm({
      title: '确定要删除吗？',
      content: '删除后，该信息将不可恢复',
      okText: '删除',
      okType: 'danger',
      onOk() {
        del(invoiceId);
      },
      cancelText: '取消',
      onCancel() {},
    });
  }

  async function del(invoiceId) {
    try {
      SmartLoading.show();
      await invoiceApi.delete(invoiceId);
      message.success('删除成功');
      ajaxQuery();
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      SmartLoading.hide();
    }
  }

  function addOrUpdate(rowData) {
    operateModal.value.showModal(rowData);
  }

  watch(
    () => props.enterpriseId,
    (value) => {
      if (value) {
        queryForm.enterpriseId = value;
        ajaxQuery();
      }
    },
    {
      immediate: true,
    }
  );
</script>
