<template>
  <view class="container">
    <view class="smart-form">
      <uni-forms ref="formRef" :label-width="100" :modelValue="form" label-position="left" :rules="rules">
        <view class="smart-form-group">
          <view class="smart-form-group-title"> 反馈内容 </view>
          <view class="smart-form-group-content">
            <uni-forms-item class="smart-form-item" label="意见反馈：" name="feedbackContent" required>
              <uni-easyinput type="textarea" trim="all" v-model="form.feedbackContent" placeholder="请输入 宝贵的意见和建议" />
            </uni-forms-item>
            <uni-forms-item class="smart-form-item" label="相关图片：" name="unifiedSocialCreditCode">
              <uni-file-picker
                limit="9"
                title="最多选择9个图片"
                @delete="onDeleteFile"
                v-model="feedbackFile"
                @select="onSelectFile"
              ></uni-file-picker>
            </uni-forms-item>
          </view>
        </view>
      </uni-forms>

      <view class="smart-form-submit smart-margin-top20 bottom-button">
        <button class="smart-form-submit-btn smart-margin-right20" type="default" @click="cancel">取消</button>
        <button class="smart-form-submit-btn" type="primary" @click="submit">保存</button>
      </view>
    </view>
  </view>
</template>

<script setup>
  import { reactive, ref } from 'vue';
  import { enterpriseApi } from '@/api/business/oa/enterprise-api';
  import { smartSentry } from '@/lib/smart-sentry';
  import { SmartLoading, SmartToast } from '@/lib/smart-support';
  import { onLoad, onReady } from '@dcloudio/uni-app';
  import { fileApi } from '@/api/support/file-api';
  import { FILE_FOLDER_TYPE_ENUM } from '@/constants/support/file-const';
  import _ from 'lodash';
  import { feedbackApi } from '@/api/support/feedback-api';

  // --------------------- 表单 ---------------------

  const defaultFormData = {
    feedbackContent: '',
    feedbackAttachment: [],
  };
  let form = reactive({ ...defaultFormData });
  const feedbackFile = ref([]);

  const rules = {
    feedbackContent: {
      rules: [{ required: true, errorMessage: '请输入反馈意见' }],
    },
  };

  // --------------------- 文件 ---------------------
  function onSelectFile(param) {
    let tempFilePaths = param.tempFilePaths;
    for (const tempFilePath of tempFilePaths) {
      upload(tempFilePath);
    }

    console.log(param, 2);
    console.log(feedbackFile, 2);
  }

  async function upload(tempFilePath) {
    try {
      SmartLoading.show();
      let res = await fileApi.upload(tempFilePath, FILE_FOLDER_TYPE_ENUM.FEEDBACK.value);
      res.data.tempFilePath = tempFilePath;
      form.feedbackAttachment.push(res.data);
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      SmartLoading.hide();
    }
  }

  function onDeleteFile(param) {
    if (!param.tempFilePath) {
      return;
    }
    _.remove(form.feedbackAttachment, (e) => e.tempFilePath === param.tempFilePath);
  }

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

  // 确定
  function submit() {
    formRef.value
      .validate()
      .then(async () => {
        SmartLoading.show();
        try {
          await feedbackApi.addFeedback(form);
          SmartToast.success('提交反馈成功');
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
