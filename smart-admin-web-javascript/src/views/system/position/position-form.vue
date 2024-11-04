<!--
  * 职务表
  *
  * @Author:    kaiyun
  * @Date:      2024-06-23 23:31:38
  * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
-->
<template>
  <a-modal
      :title="form.positionId ? '编辑' : '添加'"
      width="600px"
      :open="visibleFlag"
      @cancel="onClose"
      :maskClosable="false"
      :destroyOnClose="true"
      forceRender
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 6 }">
      <a-form-item label="职务名称" name="positionName">
        <a-input style="width: 100%" v-model:value="form.positionName" placeholder="职务名称"/>
      </a-form-item>
      <a-form-item label="职级" name="level">
        <a-input style="width: 100%" v-model:value="form.level" placeholder="职级"/>
      </a-form-item>
      <a-form-item label="排序" name="sort">
        <a-input-number :min="0" :step="1" :precision="0" style="width: 100%" v-model:value="form.sort" placeholder="排序"/>
      </a-form-item>
      <a-form-item label="备注" name="remark">
        <a-input style="width: 100%" v-model:value="form.remark" placeholder="备注"/>
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
import { positionApi } from '/@/api/system/position-api';
import { smartSentry } from '/@/lib/smart-sentry';

// ------------------------ 事件 ------------------------

const emits = defineEmits(['reloadList']);

// ------------------------ 显示与隐藏 ------------------------
// 是否显示
const visibleFlag = ref(false);

function show (rowData) {
  Object.assign(form, formDefault);
  if (rowData && !_.isEmpty(rowData)) {
    Object.assign(form, rowData);
  }
  visibleFlag.value = true;
  nextTick(() => {
    formRef.value.clearValidate();
  });
}

function onClose () {
  Object.assign(form, formDefault);
  visibleFlag.value = false;
}

// ------------------------ 表单 ------------------------

// 组件ref
const formRef = ref();

const formDefault = {
  positionId: undefined,
  positionName: undefined, //职务名称
  level: undefined,//职纪
  sort: 0,
  remark: undefined, //备注
};

let form = reactive({ ...formDefault });

const rules = {
  positionName: [{ required: true, message: '请输入职务名称' }],
};

// 点击确定，验证表单
async function onSubmit () {
  try {
    await formRef.value.validateFields();
    save();
  } catch (err) {
    message.error('参数验证错误，请仔细填写表单数据!');
  }
}

// 新建、编辑API
async function save () {
  SmartLoading.show();
  try {
    if (form.positionId) {
      await positionApi.update(form);
    } else {
      await positionApi.add(form);
    }
    message.success('操作成功');
    emits('reloadList');
    onClose();
  } catch (err) {
    smartSentry.captureError(err);
  } finally {
    SmartLoading.hide();
  }
}

defineExpose({
  show,
});
</script>
