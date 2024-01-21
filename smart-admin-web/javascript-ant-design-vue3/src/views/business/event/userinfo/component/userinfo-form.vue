<!--
  * 用户表
  *
  * @Author:    my
  * @Date:      2024-01-10 15:39:54
  * @Copyright  my
-->
<template>
    <a-modal
      :title="form.id ? '编辑' : '添加'"
      width="200"
      :visible="visibleFlag"
      @cancel="onClose"
      :maskClosable="false"
      :destroyOnClose="true"
    >
        <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }" >
            <a-row>
                    <a-form-item label="ID"  name="id">
                        <a-input-number style="width: 100%" v-model:value="form.id" placeholder="ID" />
                    </a-form-item>
            </a-row>

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
    import { userinfoApi } from '/@/api/business/userinfo/userinfo-api';
    import { smartSentry } from '/@/lib/smart-sentry';

    // ------------------------ 事件 ------------------------

    const emits = defineEmits(['reloadList']);

    // ------------------------ 显示与隐藏 ------------------------
    // 是否显示
    const visibleFlag = ref(false);

    function show(rowData) {
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

    // ------------------------ 表单 ------------------------

    // 组件ref
    const formRef = ref();

    const formDefault = {
        id: undefined,
        id: undefined, //ID
    };

    let form = reactive({ ...formDefault });

    const rules = {
        id: [{ required: true, message: 'ID 必填' }],
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
                await userinfoApi.update(form);
            } else {
                await userinfoApi.add(form);
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
