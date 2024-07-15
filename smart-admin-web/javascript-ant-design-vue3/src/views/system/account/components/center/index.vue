<template>
  <div class="center-container">
    <!--  页面标题-->
    <div class="header-title">个人中心</div>

    <!--  内容区域-->
    <div class="center-form-area">
      <a-row>
        <a-col flex="350px">
          <a-form ref="formRef" :model="form" :rules="rules" layout="vertical">
            <a-form-item label="登录账号" name="loginName">
              <a-input class="form-item" v-model:value.trim="form.loginName" placeholder="请输入登录账号" disabled />
            </a-form-item>
            <a-form-item label="员工名称" name="actualName">
              <a-input class="form-item" v-model:value.trim="form.actualName" placeholder="请输入员工名称" />
            </a-form-item>
            <a-form-item label="性别" name="gender">
              <smart-enum-select class="form-item" v-model:value="form.gender" placeholder="请选择性别" enum-name="GENDER_ENUM" />
            </a-form-item>
            <a-form-item label="手机号码" name="phone">
              <a-input class="form-item" v-model:value.trim="form.phone" placeholder="请输入手机号码" />
            </a-form-item>
            <a-form-item label="部门" name="departmentId">
              <DepartmentTreeSelect class="form-item" ref="departmentTreeSelect" width="100%" :init="false" v-model:value="form.departmentId" />
            </a-form-item>
            <a-form-item label="备注" name="remark">
              <a-textarea class="form-item" v-model:value="form.remark" placeholder="请输入备注" :rows="4" />
            </a-form-item>
          </a-form>
          <a-button type="primary" @click="onSubmit">更新个人信息</a-button>
        </a-col>
        <a-col flex="auto">
          <a-form style="padding-left: 80px" layout="vertical">
            <a-form-item label="头像" name="avatar">
              <br />
              <a-upload
                name="avatar"
                list-type="picture-card"
                class="avatar-uploader"
                :show-upload-list="false"
                :headers="{ 'x-access-token': useUserStore().getToken }"
                :customRequest="customRequest"
                :before-upload="beforeUpload"
              >
                <div v-if="avatarUrl" class="avatar-container">
                  <img :src="avatarUrl" class="avatar-image" alt="avatar" />
                  <div class="overlay">
                    <span>更新头像</span>
                  </div>
                </div>
                <div v-else>
                  <loading-outlined v-if="updateAvatarLoading" />
                  <plus-outlined v-else />
                  <div class="ant-upload-text">上传头像</div>
                </div>
              </a-upload>
            </a-form-item>
          </a-form>
        </a-col>
      </a-row>
    </div>
  </div>
</template>
<script setup>
  import { onMounted, reactive, ref } from 'vue';
  import { regular } from '/@/constants/regular-const.js';
  import DepartmentTreeSelect from '/@/components/system/department-tree-select/index.vue';
  import SmartEnumSelect from '/@/components/framework/smart-enum-select/index.vue';
  import { loginApi } from '/@/api/system/login-api.js';
  import { useUserStore } from '/@/store/modules/system/user.js';
  import { message } from 'ant-design-vue';
  import { smartSentry } from '/@/lib/smart-sentry.js';
  import { employeeApi } from '/@/api/system/employee-api';
  import { SmartLoading } from '/@/components/framework/smart-loading/index.js';
  import { fileApi } from '/@/api/support/file-api.js';
  import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/support/file-const.js';

  // 组件ref
  const formRef = ref();

  const formDefault = {
    // 员工ID
    employeeId: undefined,
    // 头像
    avatar: undefined,
    // 登录账号
    loginName: '',
    // 员工名称
    actualName: '',
    // 性别
    gender: undefined,
    // 手机号码
    phone: '',
    // 部门id
    departmentId: undefined,
    // 是否启用
    disabledFlag: undefined,
    // 备注
    remark: '',
  };
  let form = reactive({ ...formDefault });
  const rules = {
    actualName: [
      { required: true, message: '姓名不能为空' },
      { max: 30, message: '姓名不能大于30个字符', trigger: 'blur' },
    ],
    phone: [
      { required: true, message: '手机号不能为空' },
      { pattern: regular.phone, message: '请输入正确的手机号码', trigger: 'blur' },
    ],
    gender: [{ required: true, message: '性别不能为空' }],
    departmentId: [{ required: true, message: '部门不能为空' }],
  };
  // 头像地址
  let avatarUrl = ref();

  // 查询登录信息
  async function getLoginInfo() {
    try {
      //获取登录用户信息
      const res = await loginApi.getLoginInfo();
      let data = res.data;
      //更新用户信息到pinia
      useUserStore().setUserLoginInfo(data);
      // 当前form展示
      form.employeeId = data.employeeId;
      form.loginName = data.loginName;
      form.actualName = data.actualName;
      form.gender = data.gender;
      form.phone = data.phone;
      form.departmentId = data.departmentId;
      form.disabledFlag = data.disabledFlag;
      form.remark = data.remark;
      // 头像展示
      avatarUrl.value = data.avatar;
    } catch (e) {
      smartSentry.captureError(e);
    }
  }

  // 头像上传
  const accept = ref('.jpg,.jpeg,.png,.gif');
  const maxSize = ref(10);
  const folder = ref(FILE_FOLDER_TYPE_ENUM.COMMON.value);
  let updateAvatarLoading = ref(false);
  function beforeUpload(file, files) {
    const suffixIndex = file.name.lastIndexOf('.');
    const fileSuffix = file.name.substring(suffixIndex <= -1 ? 0 : suffixIndex);
    if (accept.value.indexOf(fileSuffix) === -1) {
      message.error(`只支持上传 ${accept.value.replaceAll(',', ' ')} 格式的文件`);
      return false;
    }

    const isLimitSize = file.size / 1024 / 1024 < maxSize.value;
    if (!isLimitSize) {
      message.error(`单个文件大小必须小于 ${maxSize.value} Mb`);
      return false;
    }
    return true;
  }

  async function customRequest(options) {
    updateAvatarLoading.value = true;
    try {
      const formData = new FormData();
      formData.append('file', options.file);
      let res = await fileApi.uploadFile(formData, folder.value);
      let file = res.data;
      avatarUrl.value = file.fileUrl;
      // 更新头像
      let updateAvatarForm = { avatar: file.fileKey };
      await employeeApi.updateAvatar(updateAvatarForm);
      message.success('更新成功');
      // 重新获取详情，刷新整体缓存
      await getLoginInfo();
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      updateAvatarLoading.value = false;
    }
  }

  // 更新员工信息
  async function updateEmployee() {
    SmartLoading.show();
    try {
      await employeeApi.updateByLogin(form);
      message.success('更新成功');
      // 重新获取详情，刷新整体缓存
      await getLoginInfo();
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      SmartLoading.hide();
    }
  }

  // 表单提交
  function onSubmit() {
    formRef.value
      .validate()
      .then(() => {
        updateEmployee();
      })
      .catch((error) => {
        console.log('error', error);
        message.error('参数验证错误，请仔细填写表单数据!');
      });
  }

  onMounted(() => {
    getLoginInfo();
  });
</script>
<style lang="less" scoped>
  .center-container {
    .header-title {
      font-size: 20px;
    }

    .center-form-area {
      margin-top: 20px;

      .avatar-container {
        position: relative;
        border-radius: 50%;
        overflow: hidden;
        width: 100%;
        height: 100%;

        .avatar-image {
          width: 100%;
          height: 100%;
          object-fit: cover;
        }

        .overlay {
          position: absolute;
          top: 0;
          left: 0;
          right: 0;
          bottom: 0;
          background-color: rgba(0, 0, 0, 0.5);
          opacity: 0;
          transition: opacity 0.3s ease;
          display: flex;
          justify-content: center;
          align-items: center;
          color: #ffffff;
          font-size: 17px;
        }

        &:hover .overlay {
          opacity: 1; /* 鼠标悬停时显示蒙版 */
        }
      }

      .avatar-uploader {
        :deep(.ant-upload) {
          border-radius: 50%;
          width: 150px;
          height: 150px;
        }
      }

      .ant-upload-select-picture-card i {
        font-size: 32px;
        color: #999;
      }

      .ant-upload-select-picture-card .ant-upload-text {
        margin-top: 8px;
        color: #666;
      }
    }
  }
</style>
