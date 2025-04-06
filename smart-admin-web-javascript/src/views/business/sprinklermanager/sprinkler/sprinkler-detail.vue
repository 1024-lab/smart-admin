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
    <a-page-header :title="detail.sprinklerSerial" :avatar="{ src: logo }">
      <template #extra>
        <a-button @click="showUpdate" type="primary">编辑</a-button>
      </template>
      <div>
        <a-descriptions size="small" :column="3">
          <a-descriptions-item label="喷头序列号">{{ detail.sprinklerSerial }}</a-descriptions-item>
        </a-descriptions>
      </div>
    </a-page-header>
  </div>
  <a-card class="smart-margin-top10" size="small">
    <a-tabs>
      <a-tab-pane key="employee" tab="员工信息">
        <EmployeeList :sprinklerId="sprinklerId" />
      </a-tab-pane>
      <a-tab-pane key="bank" tab="银行信息">
        <BankList :sprinklerId="sprinklerId" />
      </a-tab-pane>
      <a-tab-pane key="invoice" tab="发票信息">
        <InvoiceList :sprinklerId="sprinklerId" />
      </a-tab-pane>
      <a-tab-pane key="dataTracer" tab="变更记录">
        <DataTracer :dataId="sprinklerId" :type="DATA_TRACER_TYPE_ENUM.OA_ENTERPRISE.value" />
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
  import {sprinklerApi} from "/@/api/business/sprinklermanager/sprinkler-api.js";

  const route = useRoute();
  let sprinklerId = ref();
  onMounted(() => {
    console.log(route.query)
    if (route.query.sprinklerId) {
      sprinklerId.value = Number(route.query.sprinklerId);
      getDetail();
    }
  });

  //编辑
  const operateRef = ref();
  function showUpdate() {
    operateRef.value.showModal(sprinklerId.value);
  }

  // 详情
  let detail = ref({});

  async function getDetail() {
    try {
      let result = await sprinklerApi.detail(sprinklerId.value);
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
