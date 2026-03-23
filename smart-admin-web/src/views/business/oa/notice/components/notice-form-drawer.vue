<!--
  * 通知  表单
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-08-21 19:52:43 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
-->
<template>
  <a-drawer
    :title="formData.noticeId ? '编辑' : '新建'"
    :open="visibleFlag"
    :width="1000"
    :footerStyle="{ textAlign: 'right' }"
    @close="onClose"
    :destroyOnClose="true"
  >
    <a-form ref="formRef" :model="formData" :rules="formRules" :label-col="{ span: 3 }" :wrapper-col="{ span: 20 }">
      <a-form-item label="公告标题" name="title">
        <a-input v-model:value="formData.title" placeholder="请输入公告标题" />
      </a-form-item>
      <a-form-item label="分类" name="noticeTypeId">
        <a-select v-model:value="formData.noticeTypeId" style="width: 100%" :showSearch="true" :allowClear="true">
          <a-select-option v-for="item in noticeTypeList" :key="item.noticeTypeId" :value="item.noticeTypeId">
            {{ item.noticeTypeName }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="文号">
        <a-input v-model:value="formData.documentNumber" placeholder="文号，如：1024创新实验室发〔2022〕字第36号" />
      </a-form-item>
      <a-form-item label="作者" name="author">
        <a-input v-model:value="formData.author" placeholder="请输入作者" />
      </a-form-item>
      <a-form-item label="来源" name="source">
        <a-input v-model:value="formData.source" placeholder="请输入来源" />
      </a-form-item>
      <a-form-item label="可见范围" name="allVisibleFlag">
        <a-select v-model:value="formData.allVisibleFlag" placeholder="请选择可见范围">
          <a-select-option :value="1">全部可见</a-select-option>
          <a-select-option :value="0">部分可见</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item v-show="!formData.allVisibleFlag" label="可见员工/部门">
        <a-button type="primary" @click="showNoticeVisibleModal">选择</a-button>
        <div class="visible-list">
          <div class="visible-item" v-for="(item, index) in formData.visibleRangeList" :key="item.dataId">
            <a-tag>
              <span>{{ item.dataName }}</span>
              <close-outlined @click="removeVisibleItem(index)" />
            </a-tag>
          </div>
        </div>
      </a-form-item>
      <a-form-item label="定时发布">
        <a-switch
          v-model:checked="formData.scheduledPublishFlag"
          checked-children="开"
          un-checked-children="关"
          @change="changesSheduledPublishFlag"
        />
      </a-form-item>
      <a-form-item v-show="formData.scheduledPublishFlag" label="发布时间">
        <a-date-picker
          v-model:value="releaseTime"
          :format="timeFormat"
          showTime
          :allowClear="false"
          placeholder="请选择发布时间"
          style="width: 200px"
          @change="changeTime"
        />
      </a-form-item>
      <a-form-item label="公告内容" name="contentHtml">
        <SmartWangeditor ref="contentRef" :modelValue="formData.contentHtml" :height="300" />
      </a-form-item>
      <a-form-item label="附件">
        <Upload
          :defaultFileList="defaultFileList"
          :maxUploadSize="10"
          :folder="FILE_FOLDER_TYPE_ENUM.NOTICE.value"
          buttonText="上传附件"
          listType="text"
          extraMsg="最多上传10个附件"
          @change="changeAttachment"
        />
      </a-form-item>
    </a-form>

    <template #footer>
      <a-space>
        <a-button @click="onClose">取消</a-button>
        <a-button type="primary" @click="onSubmit">保存</a-button>
      </a-space>
    </template>
  </a-drawer>

  <!-- 选择可见范围弹窗 -->
  <NoticeFormVisibleModal ref="noticeFormVisibleModal" @selectedFinish="finishCanSelectedVisibleRange" />
</template>

<script setup>
  import { reactive, ref, onMounted, watch, computed, nextTick } from 'vue';
  import { message, Modal } from 'ant-design-vue';
  import _ from 'lodash';
  import dayjs, { Dayjs } from 'dayjs';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/support/file-const';
  import { noticeApi } from '/@/api/business/oa/notice-api';
  import SmartWangeditor from '/@/components/framework/wangeditor/index.vue';
  import Upload from '/@/components/support/file-upload/index.vue';
  import NoticeFormVisibleModal from './notice-form-visible-modal.vue';
  import { smartSentry } from '/@/lib/smart-sentry';

  const emits = defineEmits(['reloadList']);

  // ------------------ 显示，关闭 ------------------
  // 显示
  const visibleFlag = ref(false);
  function showModal(noticeId) {
    Object.assign(formData, defaultFormData);
    releaseTime.value = null;
    defaultFileList.value = [];
    queryNoticeTypeList();
    if (noticeId) {
      getNoticeUpdate(noticeId);
    }

    visibleFlag.value = true;
    nextTick(() => {
      formRef.value.clearValidate();
    });
  }

  // 关闭
  function onClose() {
    visibleFlag.value = false;
  }

  // ------------------ 表单 ------------------

  const formRef = ref();
  const contentRef = ref();
  const noticeFormVisibleModal = ref();

  const defaultFormData = {
    noticeId: undefined,
    noticeTypeId: undefined,
    title: undefined, // 标题
    categoryId: undefined, // 分类
    source: undefined, // 来源
    documentNumber: undefined, // 文号
    author: undefined, // 作者
    allVisibleFlag: 1, // 是否全部可见
    visibleRangeList: [], // 可见范围
    scheduledPublishFlag: false, // 是否定时发布
    publishTime: undefined, // 发布时间
    attachment: [], // 附件
    contentHtml: '', // html内容
    contentText: '', // 纯文本内容
  };

  const formData = reactive({ ...defaultFormData });

  const formRules = {
    title: [{ required: true, message: '请输入' }],
    noticeTypeId: [{ required: true, message: '请选择分类' }],
    allVisibleFlag: [{ required: true, message: '请选择' }],
    source: [{ required: true, message: '请输入来源' }],
    author: [{ required: true, message: '请输入作者' }],
    contentHtml: [{ required: true, message: '请输入内容' }],
  };

  // 查询详情
  async function getNoticeUpdate(noticeId) {
    try {
      SmartLoading.show();
      const result = await noticeApi.getUpdateNoticeInfo(noticeId);
      const attachment = result.data.attachment;
      if (!_.isEmpty(attachment)) {
        defaultFileList.value = attachment;
      } else {
        defaultFileList.value = [];
      }
      Object.assign(formData, result.data);
      formData.allVisibleFlag = formData.allVisibleFlag ? 1 : 0;

      releaseTime.value = dayjs(result.data.publishTime);
      visibleFlag.value = true;
    } catch (err) {
      smartSentry.captureError(err);
    } finally {
      SmartLoading.hide();
    }
  }

  // 点击确定，验证表单
  async function onSubmit() {
    try {
      formData.contentHtml = contentRef.value.getHtml();
      formData.contentText = contentRef.value.getText();
      await formRef.value.validateFields();
      save();
    } catch (err) {
      message.error('参数验证错误，请仔细填写表单数据!');
    }
  }

  // 新建、编辑API
  async function save() {
    try {
      SmartLoading.show();
      if (formData.allVisibleFlag) {
        formData.visibleRangeList = [];
      }
      if (!formData.publishTime) {
        formData.publishTime = dayjs().format(timeFormat);
      }
      if (formData.noticeId) {
        await noticeApi.updateNotice(formData);
      } else {
        await noticeApi.addNotice(formData);
      }
      message.success('保存成功');
      emits('reloadList');
      onClose();
    } catch (err) {
      smartSentry.captureError(err);
    } finally {
      SmartLoading.hide();
    }
  }

  // ------------------ 通知分类 ------------------

  // 查询分类列表
  const noticeTypeList = ref([]);
  async function queryNoticeTypeList() {
    try {
      const result = await noticeApi.getAllNoticeTypeList();
      noticeTypeList.value = result.data;
      if (noticeTypeList.value.length > 0 && !formData.noticeId) {
        formData.noticeTypeId = noticeTypeList.value[0].noticeTypeId;
      }
    } catch (err) {
      smartSentry.captureError(err);
    }
  }

  // ----------------------- 可见员工/部门 ----------------------------
  // 点击显示选择可见员工/部门
  function showNoticeVisibleModal() {
    const visibleRangeList = formData.visibleRangeList || [];
    noticeFormVisibleModal.value.showModal(visibleRangeList);
  }

  // 选择完成回调
  function finishCanSelectedVisibleRange(selectedList) {
    formData.visibleRangeList = selectedList;
  }

  // 移除某个员工/部门
  function removeVisibleItem(index) {
    Modal.confirm({
      title: '提示',
      content: '确定移除吗？',
      onOk() {
        formData.visibleRangeList.splice(index, 1);
      },
    });
  }

  // ----------------------- 发布时间 ----------------------------
  const timeFormat = 'YYYY-MM-DD HH:mm:ss';
  const releaseTime = ref(null);
  function changeTime(date, dateString) {
    formData.publishTime = dateString;
  }
  function changesSheduledPublishFlag(checked) {
    releaseTime.value = checked ? dayjs() : null;
    formData.publishTime = checked ? dayjs().format(timeFormat) : null;
  }

  // ----------------------- 上传附件 ----------------------------
  // 已上传的附件列表
  const defaultFileList = ref([]);
  function changeAttachment(fileList) {
    defaultFileList.value = fileList;
    formData.attachment = _.isEmpty(fileList) ? [] : fileList;
  }

  // ----------------------- 以下是暴露的方法内容 ------------------------
  defineExpose({
    showModal,
  });
</script>

<style lang="less" scoped>
  .visible-list {
    display: flex;
    flex-wrap: wrap;
    .visible-item {
      padding-top: 8px;
    }
  }
</style>
