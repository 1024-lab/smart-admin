<!--
  * 操作记录 详情
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-06-02 20:23:08 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
-->
<template>
  <a-modal :open="visible" title="请求详情" width="60%" :footer="null" @cancel="close">
    <div class="info-box">
      <a-row class="smart-margin-top10">
        <a-col :span="16">
          <a-row class="detail-info">
            <a-col :span="12"> 用户id：{{ detail.operateUserId }}</a-col>
            <a-col :span="12"> 用户名称： {{ detail.operateUserName }}</a-col>
          </a-row>
          <a-row class="detail-info">
            <a-col :span="12"> 请求url： {{ detail.url }}</a-col>
            <a-col :span="12"> 请求日期： {{ detail.createTime }}</a-col>
          </a-row>
          <a-row class="detail-info">
            <a-col :span="12"> 请求IP： {{ detail.ip }}</a-col>
            <a-col :span="12"> IP地区： {{ detail.ipRegion }}</a-col>
          </a-row>
          <a-row class="detail-info">
            <a-col :span="12"> 客户端： {{ detail.os }} / {{ detail.browser }}  {{ detail.device ? '/' + detail.device : detail.device }}</a-col>
          </a-row>
        </a-col>
        <a-col :span="8">
          <p class="detail-right-title">请求状态</p>
          <a-typography-text class="detail-right" :type="detail.successFlag ? 'success' : 'danger'">
            {{ detail.successFlag ? '成功' : '失败' }}
          </a-typography-text>
        </a-col>
      </a-row>
      <a-row class="detail-info">
        <a-col :span="24"> 方法： {{ detail.method }}</a-col>
      </a-row>
      <a-row class="detail-info">
        <a-col :span="24"> 说明： {{ detail.module }} - {{ detail.content }}</a-col>
      </a-row>
    </div>
    <div class="info-box">
      <h4>请求参数：</h4>
      <JsonViewer :value="detail.param ? JSON.parse(detail.param) : ''" :expanded="true" :expandDepth="10" copyable boxed sort theme="light" />
    </div>
    <div class="info-box" v-if="detail.successFlag">
      <h4>返回结果：</h4>
      <JsonViewer :value="detail.response ? JSON.parse(detail.response) : ''" :expanded="true" :expandDepth="10" copyable boxed sort theme="light" />
    </div>
    <div class="info-box" v-if="detail.failReason">
      <h4>请求失败原因：</h4>
      <a-card>
        {{ detail.failReason }}
      </a-card>
    </div>
  </a-modal>
</template>

<script setup>
  import { reactive, ref } from 'vue';
  import { JsonViewer } from 'vue3-json-viewer';
  import { operateLogApi } from '/@/api/support/operate-log-api';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import uaparser from 'ua-parser-js';

  defineExpose({
    show,
  });

  const visible = ref(false);

  function show(operateLogId) {
    visible.value = true;
    clear(detail);
    getDetail(operateLogId);
  }

  const clear = (info) => {
    const keys = Object.keys(info);
    let obj = {};
    keys.forEach((item) => {
      obj[item] = '';
    });
    Object.assign(info, obj);
  };

  function close() {
    visible.value = false;
  }

  let detail = reactive({
    param: '',
    url: '',
  });

  async function getDetail(operateLogId) {
    try {
      SmartLoading.show();
      let res = await operateLogApi.detail(operateLogId);
      detail = Object.assign(detail, res.data);
      let ua = uaparser(res.data.userAgent);
      detail.browser = ua.browser.name;
      detail.os = ua.os.name;
      detail.device = ua.device.vendor ? ua.device.vendor + ua.device.model : '';
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      SmartLoading.hide();
    }
  }
</script>

<style scoped lang="less">
  .detail-title {
    display: flex;
    align-items: center;
    font-size: 20px;
    font-weight: bold;
  }

  .info-box {
    padding: 10px 8px;
  }

  .detail-info {
    .ant-col {
      line-height: 1.46;
      margin-bottom: 12px;
      padding-right: 5px;
    }
  }

  .detail-right-title {
    text-align: right;
    color: grey;
  }

  :deep(.ant-modal-body) {
    padding: 10px !important;
  }

  .detail-right {
    padding-left: 5px;
    font-size: 20px;
    font-weight: bold;
    text-align: right;
    float: right;
  }
</style>
