<!--
  * 三级等保配置
  *
  * @Author:    1024创新实验室-主任-卓大
  * @Date:      2024-07-31 22:02:37
  * @Copyright  1024创新实验室
-->
<template>
  <a-alert closable>
    <template v-slot:message>
      <h4>三级等保：</h4>
    </template>
    <template v-slot:description>
      <pre>
  1.三级等保是中国国家等级保护认证中的最高级别认证，该认证包含了五个等级保护安全技术要求和五个安全管理要求，共涉及测评分类73类，要求非常严格。
  2.三级等保是地市级以上国家机关、重要企事业单位需要达成的认证，在金融行业中，可以看作是除了银行机构以外最高级别的信息安全等级保护。
  3.具体三级等保要求，请查看“1024创新实验室”写的相关文档 <a href="https://smartadmin.vip/asd">三级等保文档</a></pre>
    </template>
  </a-alert>
  <br />
  <!---------- 三级等保配置表单 begin ----------->
  <a-card title="三级等保配置">
    <a-form
      :model="form"
      :rules="rules"
      ref="formRef"
      style="width: 800px"
      :label-col="{ span: 6 }"
      :wrapper-col="{ span: 18 }"
      autocomplete="off"
      class="smart-query-form"
    >
      <a-form-item
        label="配置双因子登录模式"
        class="smart-query-form-item"
        extra="在用户登录时，需要同时提供用户名和密码以及其他形式的身份验证信息，例如短信验证码等"
      >
        <a-switch v-model:checked="form.twoFactorLoginEnabled" checked-children="开启 " un-checked-children="关闭 " />
      </a-form-item>
      <a-form-item
        label="最大连续登录失败次数"
        class="smart-query-form-item"
        extra="连续登录失败超过一定次数，则需要锁定；默认5次；0则不锁定；"
        name="loginFailMaxTimes"
      >
        <a-input-number :min="0" :max="10" v-model:value="form.loginFailMaxTimes" placeholder="最大连续登录失败次数" addon-after="次" />
      </a-form-item>
      <a-form-item
        name="loginFailLockMinutes"
        label="连续登录失败锁定分钟"
        class="smart-query-form-item"
        extra="连续登录失败锁定的时间；默认30分钟，0则不锁定"
      >
        <a-input-number :min="0" v-model:value="form.loginFailLockMinutes" placeholder="连续登录失败锁定时分钟" addon-after="分钟" />
      </a-form-item>
      <a-form-item
        name="loginActiveTimeoutMinutes"
        label="登录后无操作自动退出的分钟"
        class="smart-query-form-item"
        extra="如：登录1小时没操作自动退出当前登录状态；默认30分钟"
      >
        <a-input-number :min="-1" v-model:value="form.loginActiveTimeoutMinutes" placeholder="登录后无操作自动退出的分钟" addon-after="分钟" />
      </a-form-item>
      <a-form-item
        label="开启密码复杂度"
        class="smart-query-form-item"
        extra="密码长度为8-20位且必须包含字母、数字、特殊符号（如：@#$%^&*()_+-=）等三种字符"
      >
        <a-switch v-model:checked="form.passwordComplexityEnabled" checked-children="开启 " un-checked-children="关闭 " />
      </a-form-item>
      <a-form-item
        name="regularChangePasswordMonths"
        label="定期修改密码时间间隔"
        class="smart-query-form-item"
        extra="定期修改密码时间间隔，默认3个月"
      >
        <a-input-number :min="-1" :max="6" v-model:value="form.regularChangePasswordMonths" placeholder="定期修改密码时间间隔" addon-after="月" />
      </a-form-item>
      <a-form-item
        name="regularChangePasswordNotAllowRepeatTimes"
        label="定期修改密码不允许重复次数"
        class="smart-query-form-item"
        extra="定期修改密码不允许重复次数，默认：3次以内密码不能相同"
      >
        <a-input-number
          :min="-1"
          :max="6"
          v-model:value="form.regularChangePasswordNotAllowRepeatTimes"
          placeholder="相同密码不允许重复次数"
          addon-after="次"
        />
      </a-form-item>
      <a-form-item
        label="文件安全检测"
        class="smart-query-form-item"
        extra="对文件类型、恶意文件进行检测；（具体请看后端： SecurityFileService 类 checkFile 方法 ）"
      >
        <a-switch v-model:checked="form.fileDetectFlag" checked-children="开启 " un-checked-children="关闭 " />
      </a-form-item>
      <a-form-item
        name="maxUploadFileSizeMb"
        label="上传文件大小限制"
        class="smart-query-form-item"
        extra="上传文件大小限制，默认 50 mb ( 0 表示不限制)"
      >
        <a-input-number :min="0" v-model:value="form.maxUploadFileSizeMb" placeholder="上传文件大小限制" addon-after="mb(兆)" />
      </a-form-item>
      <br />
      <a-form-item :wrapper-col="{ span: 14, offset: 6 }">
        <a-button type="primary" style="margin-right: 20px" @click.prevent="onSubmit">保存配置</a-button>
        <a-button style="margin-right: 20px" @click="reset">恢复三级等保默认配置</a-button>
        <a-button danger @click="clear">清除所有配置</a-button>
      </a-form-item>
    </a-form>
  </a-card>
  <!---------- 请求参数加密 end ----------->
</template>
<script setup>
  import { onMounted, reactive, ref } from 'vue';
  import { level3ProtectApi } from '/@/api/support/level3-protect-api.js';
  import { SmartLoading } from '/@/components/framework/smart-loading/index.js';
  import { smartSentry } from '/@/lib/smart-sentry.js';
  import { message, Modal } from 'ant-design-vue';

  // 三级等保的默认值
  const protectDefaultValues = {
    // 连续登录失败次数则锁定
    loginFailMaxTimes: 5,
    // 连续登录失败锁定分钟
    loginFailLockMinutes: 30,
    // 最低活跃时间分钟
    loginActiveTimeoutMinutes: 30,
    // 密码复杂度
    passwordComplexityEnabled: true,
    // 定期修改密码时间间隔 月份
    regularChangePasswordMonths: 3,
    // 定期修改密码不允许重复次数，默认：3次以内密码不能相同
    regularChangePasswordNotAllowRepeatTimes: 3,
    // 开启双因子登录
    twoFactorLoginEnabled: true,
    // 文件检测，默认：不开启
    fileDetectFlag: true,
    // 文件大小限制，单位 mb ，(默认：50 mb)
    maxUploadFileSizeMb: 50,
  };

  // 三级等保的不保护的默认值
  const noProtectDefaultValues = {
    // 连续登录失败次数则锁定
    loginFailMaxTimes: 0,
    // 连续登录失败锁定分钟
    loginFailLockMinutes: 0,
    // 最低活跃时间分钟
    loginActiveTimeoutMinutes: 0,
    // 密码复杂度
    passwordComplexityEnabled: false,
    // 定期修改密码时间间隔 月份
    regularChangePasswordMonths: 0,
    // 定期修改密码不允许重复次数，
    regularChangePasswordNotAllowRepeatTimes: 0,
    // 开启双因子登录
    twoFactorLoginEnabled: false,
    // 文件大小限制，单位 mb ，
    maxUploadFileSizeMb: 0,
  };

  // 三级等保配置表单
  const form = reactive({
    ...protectDefaultValues,
  });

  const rules = {
    loginFailMaxTimes: [{ required: true, message: '请输入 最大连续登录失败次数' }],
    loginFailLockMinutes: [{ required: true, message: '请输入 连续登录失败锁定分钟' }],
    loginActiveTimeoutMinutes: [{ required: true, message: '请输入 最低活跃时间分钟' }],
    regularChangePasswordMonths: [{ required: true, message: '请输入 定期修改密码时间间隔' }],
    regularChangePasswordNotAllowRepeatTimes: [{ required: true, message: '请输入 定期修改密码时间间隔' }],
    maxUploadFileSizeMb: [{ required: true, message: '请输入 上传文件大小限制' }],
  };

  //获取配置
  async function getConfig() {
    SmartLoading.show();
    try {
      let res = await level3ProtectApi.getConfig();
      if (!res.data) {
        message.warn('当前未配置三级等保');
        return;
      }
      let json = JSON.parse(res.data);
      form.loginFailMaxTimes = json.loginFailMaxTimes;
      form.loginFailLockMinutes = json.loginFailLockMinutes;
      form.loginActiveTimeoutMinutes = json.loginActiveTimeoutMinutes;
      form.passwordComplexityEnabled = json.passwordComplexityEnabled;
      form.regularChangePasswordMonths = json.regularChangePasswordMonths;
      form.regularChangePasswordNotAllowRepeatTimes = json.regularChangePasswordNotAllowRepeatTimes;
      form.twoFactorLoginEnabled = json.twoFactorLoginEnabled;
      form.maxUploadFileSizeMb = json.maxUploadFileSizeMb;
      form.fileDetectFlag = json.fileDetectFlag;
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      SmartLoading.hide();
    }
  }

  onMounted(getConfig);

  const formRef = ref();
  // 提交修改
  function onSubmit() {
    formRef.value
      .validate()
      .then(save)
      .catch((error) => {
        message.error('参数验证错误，请仔细填写表单数据!');
      });
  }

  // 提交修改配置
  async function save() {
    SmartLoading.show();
    try {
      let res = await level3ProtectApi.updateConfig(form);
      message.success(res.msg);
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      SmartLoading.hide();
    }
  }

  // 重置
  function reset() {
    Object.assign(form, protectDefaultValues);
    save();
  }

  // 清除所有配置
  function clear() {
    Modal.confirm({
      title: '提示',
      content: '确定要清除三级等保配置吗?这样系统不安全哦',
      okText: '清除三级等保配置',
      okType: 'danger',
      onOk() {
        Object.assign(form, noProtectDefaultValues);
        save();
      },
      cancelText: '取消',
      onCancel() {},
    });
  }
</script>
