<!--
  * 角色 表单 
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-09-12 22:34:00 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
  *
-->
<template>
  <a-modal :title="form.roleId ? '编辑角色' : '添加角色'" :width="600" :visible="modalVisible" @cancel="onClose" :footer="null">
    <a-form ref="formRef" :model="form" :rules="rules" :labelCol="{ span: 4 }">
      <a-form-item label="角色名称" name="roleName">
        <a-input style="width: 100%" placeholder="请输入角色名称" v-model:value="form.roleName" />
      </a-form-item>
      <a-form-item label="角色备注">
        <a-input style="width: 100%" placeholder="请输入角色备注" v-model:value="form.remark" />
      </a-form-item>
    </a-form>

    <div class="footer">
      <a-button style="margin-right: 8px" @click="onClose">取消</a-button>
      <a-button type="primary" @click="submitForm">提交</a-button>
    </div>
  </a-modal>
</template>

<script setup>
  import { message } from 'ant-design-vue';
  import { reactive, ref } from 'vue';
  import { roleApi } from '/@/api/system/role/role-api';
import { smartSentry } from '/@/lib/smart-sentry';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  // ----------------------- 以下是字段定义 emits props ---------------------
  let emits = defineEmits(['refresh']);

  defineExpose({
    showModal,
  });

  // ----------------------- modal 显示与隐藏 ---------------------
  const modalVisible = ref(false);

  function showModal(role) {
    Object.assign(form, formDefault);
    if (role) {
      Object.assign(form, role);
    }
    modalVisible.value = true;
  }

  function onClose() {
    Object.assign(form, formDefault);
    modalVisible.value = false;
  }

  // ----------------------- 表单 ---------------------

  const formRef = ref();

  const formDefault = {
    id: undefined,
    remark: undefined,
    roleName: undefined,
  };

  let form = reactive({ ...formDefault });

  // 表单规则
  const rules = {
    roleName: [{ required: true, message: '请输入角色名称' }],
  };

  // 提交表单
  async function submitForm() {
    formRef.value
      .validate()
      .then(async () => {
        SmartLoading.show();
        try {
          if (form.roleId) {
            await roleApi.updateRole(form);
          } else {
            await roleApi.addRole(form);
          }
          message.info(`${form.roleId ? '编辑' : '添加'}成功`);
          emits('refresh');
          onClose();
        } catch (e) {
          smartSentry.captureError(e);
        } finally {
          SmartLoading.hide();
        }
      })
      .catch((error) => {
        message.error('参数验证错误，请仔细填写表单数据!');
      });
  }
</script>

<style scoped lang="less">
  .footer {
    width: 100%;
    border-top: 1px solid #e9e9e9;
    padding: 10px 16px;
    background: #fff;
    text-align: right;
    z-index: 1;
  }
</style>
