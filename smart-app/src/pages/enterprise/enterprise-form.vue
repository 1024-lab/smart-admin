<template>
  <view class="container">
    <view class="smart-form">
      <uni-forms ref="formRef" :label-width="100" :modelValue="form" label-position="left" :rules="rules">
        <view class="smart-form-group">
          <view class="smart-form-group-title"> 基本信息 </view>
          <view class="smart-form-group-content">
            <uni-forms-item class="smart-form-item" label="企业名称：" name="enterpriseName" required>
              <uni-easyinput class="uni-mt-5" trim="all" v-model="form.enterpriseName" placeholder="请输入 企业名称" />
            </uni-forms-item>
            <uni-forms-item class="smart-form-item" label="统一社会信用代码：" name="unifiedSocialCreditCode" required>
              <uni-easyinput class="uni-mt-5" trim="all" v-model="form.unifiedSocialCreditCode" placeholder="请输入 统一社会信用代码" />
            </uni-forms-item>
            <uni-forms-item class="smart-form-item" label="企业类型：" name="type" required>
              <smart-enum-radio v-model="form.type" enumName="ENTERPRISE_TYPE_ENUM" />
            </uni-forms-item>

            <uni-forms-item class="smart-form-item" label="公司地址：" name="address">
              <uni-easyinput class="uni-mt-5" trim="all" v-model="form.address" placeholder="请输入 公司地址" />
            </uni-forms-item>
          </view>
        </view>

        <view class="smart-form-group">
          <view class="smart-form-group-title"> 联系方式 </view>
          <view class="smart-form-group-content">
            <uni-forms-item class="smart-form-item" label="联系人" name="contact" required>
              <uni-easyinput class="uni-mt-5" trim="all" v-model="form.contact" placeholder="请输入 联系人" />
            </uni-forms-item>
            <uni-forms-item class="smart-form-item" label="联系人电话" name="contactPhone" required>
              <uni-easyinput class="uni-mt-5" trim="all" v-model="form.contactPhone" placeholder="请输入 联系人电话" />
            </uni-forms-item>
            <uni-forms-item class="smart-form-item" label="邮箱" name="email">
              <uni-easyinput class="uni-mt-5" trim="all" v-model="form.email" placeholder="请输入 邮箱" />
            </uni-forms-item>
          </view>
        </view>
      </uni-forms>

      <view class="smart-form-submit smart-margin-top20 bottom-button">
        <button class="smart-form-submit-btn smart-margin-right20" type="default" @click="cancel">取消</button>
        <button class="smart-form-submit-btn" type="warn" @click="reset">重置</button>
        <button class="smart-form-submit-btn" type="primary" @click="ok">保存</button>
      </view>
    </view>
  </view>
</template>

<script setup>
  import { reactive, ref } from 'vue';
  import { enterpriseApi } from '@/api/business/oa/enterprise-api';
  import { regular } from '@/constants/regular-const';
  import SmartEnumRadio from '@/components/smart-enum-radio/index.vue';
  import { smartSentry } from '@/lib/smart-sentry';
  import { SmartLoading, SmartToast } from '@/lib/smart-support';
  import { onLoad, onReady, onShow } from '@dcloudio/uni-app';

  // --------------------- 表单 ---------------------

  const defaultFormData = {
    enterpriseId: undefined,
    enterpriseName: undefined,
    unifiedSocialCreditCode: undefined,
    type: undefined,
    contact: undefined,
    contactPhone: undefined,
    email: undefined,
    address: undefined,
    disabledFlag: false,
  };
  let form = reactive({ ...defaultFormData });

  const rules = {
    enterpriseName: {
      rules: [{ required: true, errorMessage: '请输入企业名称' }],
    },
    unifiedSocialCreditCode: {
      rules: [{ required: true, errorMessage: '请输入 统一社会信用代码' }],
    },
    contact: {
      rules: [{ required: true, errorMessage: '请输入 联系人' }],
    },
    contactPhone: {
      rules: [
        { required: true, errorMessage: '请输入联系人电话' },
        { pattern: regular.phone, errorMessage: '电话格式错误' },
      ],
    },
    type: {
      rules: [{ required: true, errorMessage: '请选择 类型' }],
    },
  };

  // --------------------- 详情 ---------------------

  onLoad((options) => {
    if (options.enterpriseId) {
      form.enterpriseId = options.enterpriseId;
      getDetail(options.enterpriseId);
    }
  });

  onReady(() => {
    if (form.enterpriseId) {
      uni.setNavigationBarTitle({
        title: '修改客户',
      });
    }
  });

  async function getDetail(id) {
    try {
      SmartLoading.show();
      let res = await enterpriseApi.detail(id);
      form.enterpriseId = res.data.enterpriseId;
      form.enterpriseName = res.data.enterpriseName;
      form.unifiedSocialCreditCode = res.data.unifiedSocialCreditCode;
      form.type = res.data.type;
      form.contact = res.data.contact;
      form.contactPhone = res.data.contactPhone;
      form.email = res.data.email;
      form.address = res.data.address;
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      SmartLoading.hide();
    }
  }

  // ----------------------- 表单操作 ------------------------

  const formRef = ref();

  // 取消
  function cancel() {
    close();
    uni.navigateBack();
  }

  // 重置
  function reset() {
    Object.assign(form, defaultFormData);
    formRef.value.clearValidate();
  }

  // 确定
  function ok() {
    formRef.value
      .validate()
      .then(async () => {
        SmartLoading.show();
        try {
          if (form.enterpriseId) {
            await enterpriseApi.update(form);
          } else {
            await enterpriseApi.create(form);
          }
          SmartToast.success(`${form.enterpriseId ? '修改' : '添加'}成功`);

          uni.navigateBack();
        } catch (error) {
          smartSentry.captureError(error);
        } finally {
          SmartLoading.hide();
        }
      })
      .catch((error) => {
        console.log('error', error);
        SmartToast.toast('参数验证错误，请仔细填写表单数据!');
      });
  }
</script>

<style lang="scss" scoped>
  .query-form-pop {
    height: 800rpx;
    overflow-y: scroll;
  }

  .bottom-button {
    position: fixed;
    bottom: 0;
  }
</style>
