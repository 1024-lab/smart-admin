<!--
  * Relay Token管理 - 新增/编辑表单
  *
  * @Author:    Mission
  * @Date:      2026-03-15
  * @Copyright  Mission
-->
<template>
  <a-modal
      :title="form.tokenId ? '编辑Token' : '新建Token'"
      :width="500"
      :open="visibleFlag"
      @cancel="onClose"
      :maskClosable="false"
      :destroyOnClose="true"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }">
        <a-form-item label="员工" name="employeeId" v-if="!form.tokenId">
          <EmployeeSelect v-model:value="form.employeeId" placeholder="请选择员工" width="100%" :disabledFlag="0" />
        </a-form-item>
        <a-form-item label="Token名称" name="tokenName">
          <a-input style="width: 100%" v-model:value="form.tokenName" placeholder="请输入Token名称" />
        </a-form-item>
        <a-form-item label="过期时间" name="expiresTime">
          <a-date-picker
              style="width: 100%"
              v-model:value="form.expiresTime"
              show-time
              format="YYYY-MM-DD HH:mm:ss"
              value-format="YYYY-MM-DD HH:mm:ss"
              placeholder="留空则永不过期"
          />
        </a-form-item>
        <a-form-item label="状态" name="disabledFlag" v-if="form.tokenId">
          <SmartEnumSelect v-model:value="form.disabledFlag" enumName="FLAG_NUMBER_ENUM" placeholder="请选择" width="100%" />
        </a-form-item>
        <a-form-item label="备注" name="remark">
          <a-textarea v-model:value="form.remark" placeholder="请输入备注" :rows="3" />
        </a-form-item>
    </a-form>

    <template #footer>
      <a-space>
        <a-button @click="onClose">取消</a-button>
        <a-button type="primary" @click="onSubmit">保存</a-button>
      </a-space>
    </template>
  </a-modal>
</template>
<script setup>
  import { reactive, ref, nextTick } from 'vue';
  import _ from 'lodash';
  import { message } from 'ant-design-vue';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { relayTokenApi } from '/@/api/business/ai/relay/relay-token-api';
  import { smartSentry } from '/@/lib/smart-sentry';
  import SmartEnumSelect from '/@/components/framework/smart-enum-select/index.vue';
  import EmployeeSelect from '/@/components/system/employee-select/index.vue';

  const emits = defineEmits(['reloadList']);
  const visibleFlag = ref(false);
  let resultModalRef = null;

  function show(rowData, resultModal) {
    resultModalRef = resultModal;
    Object.assign(form, formDefault);
    if (rowData && !_.isEmpty(rowData)) {
      Object.assign(form, rowData);
    }
    visibleFlag.value = true;
    nextTick(() => {
      formRef.value.clearValidate();
    });
  }

  function onClose() {
    Object.assign(form, formDefault);
    visibleFlag.value = false;
  }

  const formRef = ref();

  const formDefault = {
      tokenId: undefined,
      employeeId: undefined,
      tokenName: undefined,
      expiresTime: undefined,
      disabledFlag: undefined,
      remark: undefined,
  };

  let form = reactive({ ...formDefault });

  const rules = {
      employeeId: [{ required: true, message: '请选择员工' }],
      tokenName: [{ required: true, message: 'Token名称必填' }],
  };

  async function onSubmit() {
    try {
      await formRef.value.validateFields();
      save();
    } catch (err) {
      message.error('参数验证错误，请仔细填写表单数据!');
    }
  }

  async function save() {
    SmartLoading.show();
    try {
      if (form.tokenId) {
        await relayTokenApi.update(form);
        message.success('操作成功');
      } else {
        let result = await relayTokenApi.add(form);
        message.success('创建成功');
        // 展示创建结果弹窗
        if (resultModalRef && result.data) {
          resultModalRef.show(result.data);
        }
      }
      emits('reloadList');
      onClose();
    } catch (err) {
      smartSentry.captureError(err);
    } finally {
      SmartLoading.hide();
    }
  }

  defineExpose({ show });
</script>
