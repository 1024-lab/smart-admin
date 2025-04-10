<!--
  * 字典 数据 表单 弹窗
  *
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2025-03-21 21:50:41
  * @Wechat:    zhuda1024
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
-->
<template>
  <a-modal :open="visible" :title="form.dictDataId ? '编辑字典值' : '添加字典值'" ok-text="确认" cancel-text="取消" @ok="onSubmit" @cancel="onClose">
    <br />
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }" :wrapper-col="{ span: 16 }">
      <a-form-item label="字典项名称" name="dataLabel">
        <a-input v-model:value="form.dataLabel" placeholder="请输入 字典项名称" />
      </a-form-item>
      <a-form-item label="字典项值" name="dataValue">
        <a-input v-model:value="form.dataValue" placeholder="请输入 字典项值" />
      </a-form-item>
      <a-form-item label="排序" name="sort" help="值越大越靠前">
        <a-input-number style="width: 100%" v-model:value="form.sortOrder" :min="0" :max="1000" />
      </a-form-item>
      <a-form-item label="备注" name="remark">
        <a-textarea v-model="form.remark" style="width: 100%; height: 100px; outline: none" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script setup lang="ts">
  import { ref, reactive } from 'vue';
  import { message } from 'ant-design-vue';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { dictApi } from '/@/api/support/dict-api';
  import { smartSentry } from '/@/lib/smart-sentry';

  // emit
  const emit = defineEmits(['reloadList']);

  //  组件
  const formRef = ref();

  const formDefault = {
    dictId: undefined,
    dictCode: undefined,
    dictDataId: undefined,
    sortOrder: 0,
    dataValue: '',
    dataLabel: '',
    remark: '',
  };
  let form = reactive({ ...formDefault });
  const rules = {
    dataValue: [{ required: true, message: '请输入 字典项值' }],
    dataLabel: [{ required: true, message: '请输入 字典项名称' }],
    sortOrder: [{ required: true, message: '请输入排序' }],
  };
  // 是否展示
  const visible = ref(false);

  function showModal(rowData, dictId, dictCode) {
    Object.assign(form, formDefault);
    if (rowData) {
      Object.assign(form, rowData);
    }
    form.dictId = dictId;
    form.dictCode = dictCode;
    visible.value = true;
  }

  function onClose() {
    Object.assign(form, formDefault);
    visible.value = false;
  }

  function onSubmit() {
    formRef.value
      .validate()
      .then(async () => {
        SmartLoading.show();
        try {
          if (form.dictDataId) {
            await dictApi.updateDictData(form);
          } else {
            await dictApi.addDictData(form);
          }
          message.success(`${form.dictDataId ? '修改' : '添加'}成功`);
          emit('reloadList');
          onClose();
        } catch (error) {
          smartSentry.captureError(error);
        } finally {
          SmartLoading.hide();
        }
      })
      .catch((error) => {
        console.log('error', error);
        message.error('参数验证错误，请仔细填写表单数据!');
      });
  }

  defineExpose({
    showModal,
  });
</script>
