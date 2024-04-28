<template>
  <view class="container">
    <view>
      <smart-detail-tabs :tabsList="tabs" v-model="smartTabIndex" @change="onTabChange" :fixed="true" />
    </view>
    <view class="smart-detail smart-margin-top60 content" id="detail1">
      <view class="smart-detail-card">
        <view class="smart-detail-card-title"> 企业基础信息</view>
        <view class="smart-detail-card-cell">
          <view class="smart-detail-card-label"> 企业名称 </view>
          <view class="smart-detail-card-value">
            {{ data.enterpriseName }}
          </view>
        </view>
        <view class="smart-detail-card-cell">
          <view class="smart-detail-card-label"> 统一社会信用代码 </view>
          <view class="smart-detail-card-value">
            {{ data.unifiedSocialCreditCode }}
          </view>
        </view>
        <view class="smart-detail-card-cell">
          <view class="smart-detail-card-label"> 类型 </view>
          <view class="smart-detail-card-value">
            {{ $smartEnumPlugin.getDescByValue('ENTERPRISE_TYPE_ENUM', data.type) }}
          </view>
        </view>
        <view class="smart-detail-card-cell">
          <view class="smart-detail-card-label"> 企业ID </view>
          <view class="smart-detail-card-value">
            {{ data.enterpriseId }}
          </view>
        </view>
      </view>

      <view class="smart-detail-card" id="detail2">
        <view class="smart-detail-card-title"> 图片信息</view>

        <view class="smart-detail-card-cell" v-if="data.enterpriseLogo && data.enterpriseLogo.length > 0">
          <view class="smart-detail-card-label"> 企业logo </view>
          <view class="smart-detail-card-value" @click="preview([data.enterpriseLogo[0].fileUrl])">
            <image :src="data.enterpriseLogo[0].fileUrl"></image>
          </view>
        </view>

        <view class="smart-detail-card-cell" v-if="data.businessLicense && data.businessLicense.length > 0">
          <view class="smart-detail-card-label"> 营业执照 </view>
          <view class="smart-detail-card-value" @click="preview([data.businessLicense[0].fileUrl])">
            <image :src="data.businessLicense[0].fileUrl"></image>
          </view>
        </view>
      </view>

      <view class="smart-detail-card" id="detail3">
        <view class="smart-detail-card-title"> 联系方式</view>

        <view class="smart-detail-card-cell">
          <view class="smart-detail-card-label"> 联系人 </view>
          <view class="smart-detail-card-value">
            {{ data.contact }}
          </view>
        </view>

        <view class="smart-detail-card-cell">
          <view class="smart-detail-card-label"> 联系人电话 </view>
          <view class="smart-detail-card-value">
            {{ data.contactPhone }}
          </view>
        </view>
        <view class="smart-detail-card-cell">
          <view class="smart-detail-card-label"> 邮箱 </view>
          <view class="smart-detail-card-value">
            {{ data.email }}
          </view>
        </view>
      </view>
    </view>

    <view class="btn-group">
      <button class="btn" type="warn" @click="onDelete">删除</button>
      <button class="btn" type="primary" @click="onChange">修改</button>
    </view>
  </view>
</template>

<script setup>
  import SmartDetailTabs from '@/components/smart-detail-tabs/index.vue';
  import { ref, reactive } from 'vue';
  import { enterpriseApi } from '@/api/business/oa/enterprise-api';
  import { onShow, onLoad } from '@dcloudio/uni-app';
  import { smartSentry } from '@/lib/smart-sentry';
  import { SmartLoading, SmartToast } from '@/lib/smart-support';

  // ----------------------- tab -----------------------

  const tabs = ref([
    {
      label: '基础信息',
      value: 1,
    },
    {
      label: '图片信息',
      value: 2,
    },
    {
      label: '联系人',
      value: 3,
    },
  ]);

  const smartTabIndex = ref(1);

  function onTabChange(tabIndex) {
    console.log(12, tabIndex);
    uni.pageScrollTo({
      selector: '#detail' + tabIndex,
      duration: 300,
      success: console.log,
      fail: console.log,
    });
  }

  // ----------------------- 详情 -----------------------

  const data = reactive({
    //企业ID
    enterpriseId: '',
    //企业名称
    enterpriseName: '',
    //统一社会信用代码
    unifiedSocialCreditCode: '',
    //类型
    type: '',
    //联系人
    contact: '',
    //联系人电话
    contactPhone: '',
    //邮箱
    email: '',
    //详细地址
    address: '',
    //企业logo
    enterpriseLogo: undefined,
    //营业执照
    businessLicense: undefined,
  });

  async function getDetail(id) {
    try {
      SmartLoading.show();
      let res = await enterpriseApi.detail(id);
      data.enterpriseId = res.data.enterpriseId;
      data.enterpriseName = res.data.enterpriseName;
      data.unifiedSocialCreditCode = res.data.unifiedSocialCreditCode;
      data.type = res.data.type;
      data.contact = res.data.contact;
      data.contactPhone = res.data.contactPhone;
      data.email = res.data.email;
      data.address = res.data.address;
      data.enterpriseLogo = res.data.enterpriseLogo;
      data.businessLicense = res.data.businessLicense;
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      SmartLoading.hide();
    }
  }

  function preview(urls) {
    uni.previewImage({
      urls: urls,
    });
  }

  onLoad((options) => {
    if (options.enterpriseId) {
      data.enterpriseId = options.enterpriseId;
      getDetail(options.enterpriseId);
    }
  });

  onShow(() => {
    getDetail(data.enterpriseId);
  });

  function onChange() {
    uni.navigateTo({ url: '/pages/enterprise/enterprise-form?enterpriseId=' + data.enterpriseId });
  }

  function onDelete() {
    uni.showModal({
      title: '提示',
      content: '确定要删除吗？',
      confirmText: '确定删除',
      success: function (res) {
        if (res.confirm) {
          doDelete();
        }
      },
    });
  }

  async function doDelete() {
    try {
      SmartLoading.show();
      await enterpriseApi.delete(data.enterpriseId);
      SmartToast.success('删除成功');
      setTimeout(() => {
        uni.redirectTo({ url: '/pages/enterprise/enterprise-list' });
      }, 500);
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      SmartLoading.hide();
    }
  }
</script>

<style lang="scss" scoped>
  .container {
    background-color: #f4f4f4;
    height: 100vh;
    overflow-y: scroll;
  }

  .content {
    margin-bottom: 120px;
  }

  .btn-group {
    border-top: #eee 1px solid;
    height: 80px;
    display: flex;
    flex-direction: row;
    align-items: center;
    position: fixed;
    bottom: 0;
    background-color: white;
    width: 100%;

    .btn {
      margin: 10px;
      flex: 1;
    }
  }
</style>
