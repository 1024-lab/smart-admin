<!--
  * 公司 详情
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-08-15 20:15:49
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
-->
<template>
  <div class="detail-header">
    <a-page-header :title="detail.enterpriseName" :avatar="{ src: logo }">
      <template #extra>
        <a-button @click="showUpdate" type="primary">编辑</a-button>
      </template>
      <div>
        <a-descriptions size="small" :column="3">
          <a-descriptions-item label="统一社会信用代码">{{ detail.unifiedSocialCreditCode }}</a-descriptions-item>
          <a-descriptions-item label="联系人">{{ detail.contact }}</a-descriptions-item>
          <a-descriptions-item label="联系人电话">{{ detail.contactPhone }}</a-descriptions-item>
          <a-descriptions-item label="邮箱">{{ detail.email }}</a-descriptions-item>
          <a-descriptions-item label="所在城市">{{ area }}</a-descriptions-item>
          <a-descriptions-item label="详细地址">{{ detail.address }}</a-descriptions-item>
          <a-descriptions-item label="创建时间">{{ detail.createTime }}</a-descriptions-item>
          <a-descriptions-item label="创建人">{{ detail.createUserName }}</a-descriptions-item>
          <a-descriptions-item label="营业执照">
            <FilePreview :file-list="detail.businessLicense" />
          </a-descriptions-item>
        </a-descriptions>
      </div>
    </a-page-header>
  </div>
  <a-card class="smart-margin-top10" size="small">
    <a-tabs>
      <a-tab-pane key="employee" tab="员工信息">
        <EmployeeList :enterpriseId="enterpriseId" />
      </a-tab-pane>
      <a-tab-pane key="bank" tab="银行信息">
        <BankList :enterpriseId="enterpriseId" />
      </a-tab-pane>
      <a-tab-pane key="invoice" tab="发票信息">
        <InvoiceList :enterpriseId="enterpriseId" />
      </a-tab-pane>
      <a-tab-pane key="dataTracer" tab="变更记录">
        <DataTracer :dataId="enterpriseId" :type="DATA_TRACER_TYPE_ENUM.OA_ENTERPRISE.value" />
      </a-tab-pane>
    </a-tabs>
    <EnterpriseOperate ref="operateRef" @refresh="getDetail" />
  </a-card>
</template>

<script setup>
  import _ from 'lodash';
  import { computed, onMounted, ref } from 'vue';
  import { useRoute } from 'vue-router';
  import BankList from './components/enterprise-bank-list.vue';
  import EmployeeList from './components/enterprise-employee-list.vue';
  import InvoiceList from './components/enterprise-invoice-list.vue';
  import EnterpriseOperate from './components/enterprise-operate-modal.vue';
  import { enterpriseApi } from '/@/api/business/oa/enterprise-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import DataTracer from '/@/components/support/data-tracer/index.vue';
  import FilePreview from '/@/components/support/file-preview/index.vue';
  import { DATA_TRACER_TYPE_ENUM } from '/@/constants/support/data-tracer-const';
  import { smartSentry } from '/@/lib/smart-sentry';

  const route = useRoute();
  let enterpriseId = ref();
  onMounted(() => {
    if (route.query.enterpriseId) {
      enterpriseId.value = Number(route.query.enterpriseId);
      getDetail();
    }
  });

  //编辑
  const operateRef = ref();
  function showUpdate() {
    operateRef.value.showModal(enterpriseId.value);
  }

  // 详情
  let detail = ref({});

  async function getDetail() {
    try {
      let result = await enterpriseApi.detail(enterpriseId.value);
      detail.value = result.data;
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      SmartLoading.hide();
    }
  }

  // 地区
  const area = computed(() => {
    let area = '';
    if (!detail.value) {
      return area;
    }
    if (detail.value.provinceName) {
      area = area + detail.value.provinceName;
    }
    if (detail.value.cityName) {
      area = area + detail.value.cityName;
    }
    if (detail.value.districtName) {
      area = area + detail.value.districtName;
    }
    return area;
  });

  const logo = computed(() => {
    if (!detail.value) {
      return '';
    }
    if (!_.isEmpty(detail.value.enterpriseLogo)) {
      return detail.value.enterpriseLogo[0].fileUrl;
    }
    return '';
  });
</script>

<style lang="less" scoped>
  .detail-header {
    background-color: #fff;
    padding: 10px;
  }
</style>
