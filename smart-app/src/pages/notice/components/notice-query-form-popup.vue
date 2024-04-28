<template>
  <uni-popup ref="popupRef" background-color="#fff" type="bottom" :is-mask-click="false">
    <view class="query-form-pop">
      <view class="smart-form">
        <uni-forms :label-width="100" :modelValue="form" label-position="left">
          <view class="smart-form-group">
            <view class="smart-form-group-title"> 分类 </view>
            <view class="smart-form-group-content">
              <uni-forms-item class="smart-form-item" label="类型：">
                <uni-data-select v-model="form.noticeTypeId" :localdata="noticeTypeList" @change="changeNoticeType" :clear="true" />
              </uni-forms-item>
            </view>
          </view>

          <view class="smart-form-group">
            <view class="smart-form-group-title"> 发布日期 </view>
            <view class="smart-form-group-content">
              <uni-forms-item class="smart-form-item" label="开始日期">
                <uni-datetime-picker type="date" clear-icon v-model="form.publishTimeBegin" />
              </uni-forms-item>
              <uni-forms-item class="smart-form-item" label="截止日期">
                <uni-datetime-picker type="date" clear-icon v-model="form.publishTimeEnd" />
              </uni-forms-item>
            </view>
          </view>
        </uni-forms>

        <view class="smart-form-submit smart-margin-top20">
          <button class="smart-form-submit-btn smart-margin-right20" type="default" @click="cancel">取消</button>
          <button class="smart-form-submit-btn" type="warn" @click="reset">重置</button>
          <button class="smart-form-submit-btn" type="primary" @click="ok">确定</button>
        </view>
      </view>
    </view>
  </uni-popup>
</template>

<script setup>
  import { onMounted, reactive, ref, toRaw } from 'vue';
  import { noticeApi } from '@/api/business/oa/notice-api';
  import _ from 'lodash';

  const emits = defineEmits(['close']);
  defineExpose({ show });

  // --------------------- 显示 隐藏 ---------------------
  const popupRef = ref();

  function show() {
    popupRef.value.open();
  }

  function close() {
    popupRef.value.close();
  }

  // --------------------- 表单 ---------------------

  const defaultFormData = {
    // 分类
    noticeTypeId: undefined,
    noticeTypeName: undefined,
    // 发布开始时间
    publishTimeBegin: undefined,
    // 发布截止时间
    publishTimeEnd: undefined,
  };

  const form = reactive({ ...defaultFormData });

  // --------------------- 通知类型 ---------------------

  const noticeTypeList = ref([]);
  async function queryNoticeTypeList() {
    let res = await noticeApi.getAllNoticeTypeList();
    noticeTypeList.value = res.data.map((e) => Object.assign({}, { text: e.noticeTypeName, value: e.noticeTypeId }));
  }

  onMounted(() => {
    queryNoticeTypeList();
  });

  function changeNoticeType(e) {
    form.noticeTypeId = e;
    form.noticeTypeName = _.find(noticeTypeList.value, { value: form.noticeTypeId }).text;
  }

  // ----------------------- 表单操作 ------------------------

  // 取消
  function cancel() {
    close();
    emits('close', null);
  }

  // 重置
  function reset() {
    Object.assign(form, defaultFormData);
    close();
    emits('close', toRaw(form));
  }

  // 确定
  function ok() {
    close();
    emits('close', toRaw(form));
  }
</script>

<style lang="scss" scoped>
  .query-form-pop {
    height: 800rpx;
    overflow-y: scroll;
  }
</style>
