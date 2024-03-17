<template>
  <view class="smart-form">
    <uni-forms :label-width="100" :modelValue="formData" label-position="left">
      <view class="smart-form-group">
        <view class="smart-form-group-title"> 常用功能 </view>
        <view class="smart-form-group-content">
          <uni-forms-item class="smart-form-item" label="姓名" name="name" required>
            <uni-easyinput trim="all" v-model="formData.name" placeholder="请输入姓名" />
          </uni-forms-item>
          <uni-forms-item class="smart-form-item" label="手机号码" name="name" required>
            <uni-easyinput trim="all" v-model="formData.name" placeholder="请输入手机号码" />
          </uni-forms-item>
          <uni-forms-item class="smart-form-item" label="邮箱地址" name="name">
            <uni-easyinput trim="all" v-model="formData.name" placeholder="请输入邮箱地址" />
          </uni-forms-item>
          <uni-forms-item class="smart-form-item" label="性别" required>
            <uni-data-checkbox v-model="formData.sex" :localdata="sexs" />
          </uni-forms-item>
          <uni-forms-item class="smart-form-item" label="出生日期" name="name">
            <uni-datetime-picker type="date" return-type="timestamp" v-model="formData.datetimesingle" />
          </uni-forms-item>
        </view>
      </view>
      <view class="smart-form-group">
        <view class="smart-form-group-title"> 兴趣爱好 </view>
        <view class="smart-form-group-content">
          <uni-forms-item class="smart-form-item" label="兴趣爱好" name="interest">
            <uni-data-checkbox mode="button" multiple v-model="formData.interest" :localdata="interestList"></uni-data-checkbox>
          </uni-forms-item>
        </view>
      </view>
      <view class="smart-form-group">
        <view class="smart-form-group-title"> 屏幕设置 </view>
        <view class="smart-form-group-content">
          <uni-forms-item class="smart-form-item" label="亮度调整" name="name">
            <slider style="width: 100%" value="50" activeColor="#2291F9" backgroundColor="#f5f6f8" block-color="#2291F9" block-size="20" />
          </uni-forms-item>
        </view>
      </view>
      <view class="smart-form-group">
        <view class="smart-form-group-title"> 自我介绍 </view>
        <view class="smart-form-group-content">
          <uni-forms-item class="smart-form-item" label="兴趣爱好" name="interest">
            <uni-easyinput type="textarea" autoHeight v-model="value" placeholder="请输入自我介绍" />
          </uni-forms-item>

          <uni-forms-item class="smart-form-item" label="上传图片" name="interest">
            <uni-file-picker limit="9" title="最多选择9张图片" />
          </uni-forms-item>
        </view>
      </view>
    </uni-forms>
    <view class="smart-form-submit fixed-bottom-button">
      <button class="smart-form-submit-btn smart-margin-right20" type="default">取消</button>
      <button class="smart-form-submit-btn" type="warn">重置</button>
      <button class="smart-form-submit-btn" type="primary">确定</button>
    </view>
  </view>
</template>

<script setup>
  import { reactive } from 'vue';

  const sexs = [
    {
      text: '男',
      value: 0,
    },
    {
      text: '女',
      value: 1,
    },
    {
      text: '你懂的',
      value: 2,
    },
  ];

  const interestList = [
    { text: '唱歌', value: 1 },
    { text: '足球', value: 2 },
    { text: '篮球', value: 3 },
    { text: '跑步', value: 4 },
    { text: '写字', value: 5 },
    { text: '美术', value: 6 },
    { text: '射击', value: 7 },
    { text: '健身', value: 8 },
    { text: '马术', value: 9 },
    { text: '美食', value: 10 },
  ];
  const formData = reactive({});
</script>

<style lang="scss" scoped></style>
