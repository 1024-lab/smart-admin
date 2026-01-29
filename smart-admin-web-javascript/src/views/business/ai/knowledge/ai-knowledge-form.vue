<!--
  * 知识库
  *
  * @Author:    Mission
  * @Date:      2026-01-28 10:24:07
  * @Copyright  Mission
-->
<template>
  <a-drawer
      :title="form.id ? '编辑' : '添加'"
      :width="500"
      :open="visibleFlag"
      @close="onClose"
      :maskClosable="false"
      :destroyOnClose="true"
  >
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }" >
        <a-form-item label="知识库名称"  name="name">
          <a-input style="width: 100%" v-model:value="form.name" placeholder="知识库名称" />
        </a-form-item>
        <a-form-item label="描述"  name="description">
          <a-input style="width: 100%" v-model:value="form.description" placeholder="描述" />
        </a-form-item>
        <a-form-item label="权限"  name="permission">
            <DictSelect width="100%" v-model:value="form.permission" :dict-code="DICT_CODE_ENUM.DATASET_PERMISSION || 'DATASET_PERMISSION'" placeholder="权限"/>
        </a-form-item>
        <a-form-item label="向量模型"  name="embeddingModel">
          <a-select style="width: 100%" v-model:value="form.embeddingModel" placeholder="请选择向量模型">
            <a-select-option v-for="model in embeddingModelList" :key="model.id" :value="model.id">
              {{ model.modelName }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="向量模型"  name="enableRerank">
          <a-input-number style="width: 100%" v-model:value="form.enableRerank" placeholder="向量模型" />
        </a-form-item>
        <a-form-item label="reRank模型 当启动rerank时使用"  name="rerankModel">
          <a-input style="width: 100%" v-model:value="form.rerankModel" placeholder="reRank模型 当启动rerank时使用" />
        </a-form-item>
    </a-form>

    <template #footer>
      <a-space>
        <a-button @click="onClose">取消</a-button>
        <a-button type="primary" @click="onSubmit">保存</a-button>
      </a-space>
    </template>
  </a-drawer>
</template>
<script setup>
  import { reactive, ref, nextTick } from 'vue';
  import _ from 'lodash';
  import { message } from 'ant-design-vue';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { aiKnowledgeApi } from '/@/api/business/knowledge/ai-knowledge-api';
  import { aiModelConfigApi } from '/@/api/business/ai/ai-model-config-api';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { DICT_CODE_ENUM } from '/@/constants/support/dict-const.js';
  import DictSelect from '/@/components/support/dict-select/index.vue';

  // ------------------------ 事件 ------------------------

  const emits = defineEmits(['reloadList']);

  // ------------------------ 显示与隐藏 ------------------------
  // 是否显示
  const visibleFlag = ref(false);

  // 向量模型列表
  const embeddingModelList = ref([]);

  // 加载向量模型列表
  async function loadEmbeddingModels() {
    try {
      const res = await aiModelConfigApi.queryListByModelType('embedding');
      embeddingModelList.value = res.data || [];
    } catch (err) {
      smartSentry.captureError(err);
    }
  }

  function show(rowData) {
    Object.assign(form, formDefault);
    if (rowData && !_.isEmpty(rowData)) {
      Object.assign(form, rowData);
    }
    // 使用字典时把下面这注释修改成自己的字典字段 有多个字典字段就复制多份同理修改 不然打开表单时不显示字典初始值
    // if (form.status && form.status.length > 0) {
    //   form.status = form.status.map((e) => e.valueCode);
    // }
    visibleFlag.value = true;
    loadEmbeddingModels();
    nextTick(() => {
      formRef.value.clearValidate();
    });
  }

  function onClose() {
    Object.assign(form, formDefault);
    visibleFlag.value = false;
  }

  // ------------------------ 表单 ------------------------

  // 组件ref
  const formRef = ref();

  const formDefault = {
      name: undefined, //知识库名称
      description: undefined, //描述
      permission: undefined, //权限 private-私有 public-公开
      embeddingModel: undefined, //向量模型
      enableRerank: undefined, //向量模型
      rerankModel: undefined, //reRank模型 当启动rerank时使用
  };

  let form = reactive({ ...formDefault });

  const rules = {
      name: [{ required: true, message: '知识库名称 必填' }],
      permission: [{ required: true, message: '权限 private-私有 public-公开 必填' }],
      embeddingModel: [{ required: true, message: '向量模型 必填' }],
  };

  // 点击确定，验证表单
  async function onSubmit() {
    try {
      await formRef.value.validateFields();
      save();
    } catch (err) {
      message.error('参数验证错误，请仔细填写表单数据!');
    }
  }

  // 新建、编辑API
  async function save() {
    SmartLoading.show();
    try {
      if (form.id) {
        await aiKnowledgeApi.update(form);
      } else {
        await aiKnowledgeApi.add(form);
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
