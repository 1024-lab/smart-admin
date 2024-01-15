<!--
  *  意见反馈提交弹窗 
  *
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-09-06 20:40:16 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
-->
<template>
  <a-modal :open="visible" title="意见反馈" :closable="false" :maskClosable="true" >
    <a-form :labelCol="{ span: 6 }">
      <a-form-item label="我要吐槽/建议：">
        <a-textarea v-model:value="form.feedbackContent" placeholder="请输入让您不满意的点，我们争取做到更好～" :rows="3"/>
      </a-form-item>
      <a-form-item label="反馈图片：">
        <Upload
            accept=".jpg,.jpeg,.png,.gif"
            :maxUploadSize="3"
            buttonText="点击上传反馈图片"
            :default-file-list="form.feedbackAttachment || []"
            listType="picture-card"
            @change="changeAttachment"
            :folder="FILE_FOLDER_TYPE_ENUM.FEEDBACK.value"
        />
      </a-form-item>
    </a-form>
    <template #footer>
      <a-button @click="hide">取消</a-button>
      <a-button type="primary" @click="submit">提交</a-button>
    </template>
  </a-modal>
</template>
<script setup>
import { reactive, ref } from 'vue';
import { SmartLoading } from '/@/components/framework/smart-loading';
import { feedbackApi } from '/src/api/support/feedback-api';
import { message } from 'ant-design-vue';
import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/support/file-const';
import Upload from '/@/components/support/file-upload/index.vue';
import { smartSentry } from '/@/lib/smart-sentry';

defineExpose({
  show,
});

const visible = ref(false);

function show () {
  Object.assign(form, formDefault);
  console.log(form)
  visible.value = true;
}

function hide () {
  visible.value = false;
}

const formDefault = {
  feedbackContent:'',
  feedbackAttachment: ''
}
const form = reactive({ ...formDefault });

async function submit () {
  try {
    SmartLoading.show();
    if(!form.feedbackContent){
      message.warn('请填写具体内容');
      return;
    }
    await feedbackApi.addFeedback(form);
    message.success('提交成功');
    hide();
  } catch (e) {
    smartSentry.captureError(e);
  } finally {
    SmartLoading.hide();
  }
}

function changeAttachment (fileList) {
  form.feedbackAttachment = fileList;
}
</script>
