<!--
  * 数据脱敏
  *
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2024-08-02 20:23:08
  * @Wechat:    zhuda1024
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
-->
<template>
  <a-card size="small" :bordered="false" :hoverable="true">
    <a-alert>
      <template v-slot:message>
        <h4>数据脱敏 Data Masking介绍：</h4>
      </template>
      <template v-slot:description>
        <pre>
简介：《信息安全技术 网络安全等级保护基本要求》明确规定，二级以上保护则需要对敏感数据进行脱敏处理。
原理：数据脱敏是指对某些敏感信息通过脱敏规则进行数据的变形，实现敏感隐私数据的可靠保护。
举例：在不违反系统规则条件下，身份证号、手机号、卡号、客户号等个人信息都需要进行数据脱敏。

使用方式：
1）脱敏注解 @DataMasking ，支持数据类型如：用户ID、手机号、密码、地址、银行卡、车牌号等；
2）脱敏工具类： SmartDataMaskingUtil ；
</pre
        >
      </template>
    </a-alert>

    <a-form class="smart-query-form">
      <a-row class="smart-query-form-row">
        <a-form-item class="smart-query-form-item smart-margin-left10">
          <a-button-group>
            <a-button type="primary" @click="onSearch">
              <template #icon>
                <ReloadOutlined />
              </template>
              查询
            </a-button>
          </a-button-group>
        </a-form-item>
      </a-row>
    </a-form>

    <a-table
      size="small"
      bordered
      :scroll="{ x: 1100 }"
      :loading="tableLoading"
      class="smart-margin-top10"
      :dataSource="tableData"
      :columns="columns"
      :pagination="false"
    />
  </a-card>
</template>
<script setup>
  import { onMounted, reactive, ref } from 'vue';
  import { heartBeatApi } from '/@/api/support/heart-beat-api';
  import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
  import { defaultTimeRanges } from '/@/lib/default-time-ranges';
  import { smartSentry } from '/@/lib/smart-sentry';
  import TableOperator from '/@/components/support/table-operator/index.vue';
  import { TABLE_ID_CONST } from '/@/constants/support/table-id-const';
  import { dataMaskingApi } from '/@/api/support/data-masking-api.js';

  //------------------------ 表格渲染 ---------------------

  const columns = ref([
    {
      title: '用户ID',
      dataIndex: 'userId',
      width: 70,
    },
    {
      title: '默认',
      dataIndex: 'other',
      width: 100,
    },
    {
      title: '手机号',
      dataIndex: 'phone',
      width: 100,
    },
    {
      title: '身份证',
      dataIndex: 'idCard',
      width: 150,
    },
    {
      title: '密码',
      dataIndex: 'password',
      width: 100,
    },
    {
      title: '邮箱',
      dataIndex: 'email',
      width: 120,
    },
    {
      title: '车牌号',
      dataIndex: 'carLicense',
      width: 120,
    },
    {
      title: '银行卡',
      dataIndex: 'bankCard',
      width: 170,
    },
    {
      title: '地址',
      dataIndex: 'address',
      width: 210,
    },
  ]);

  const tableLoading = ref(false);
  const tableData = ref([]);

  function onSearch() {
    ajaxQuery();
  }

  async function ajaxQuery() {
    try {
      tableLoading.value = true;
      let responseModel = await dataMaskingApi.query();
      tableData.value = responseModel.data;
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      tableLoading.value = false;
    }
  }

  onMounted(ajaxQuery);
</script>
