<!--
  * 商品表单
  *
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2022-07-21 21:55:12
  * @Wechat:    zhuda1024
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
-->
<template>
  <a-drawer :title="form.goodsId ? '编辑' : '添加'" :width="500" :open="visible" :body-style="{ paddingBottom: '80px' }" @close="onClose">
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 5 }">
      <a-form-item label="商品分类" name="categoryId">
        <CategoryTree v-model:value="form.categoryId" placeholder="请选择商品分类" :categoryType="CATEGORY_TYPE_ENUM.GOODS.value" />
      </a-form-item>
      <a-form-item label="商品名称" name="goodsName">
        <a-input v-model:value="form.goodsName" placeholder="请输入商品名称" />
      </a-form-item>
      <a-form-item label="商品状态" name="goodsStatus">
        <SmartEnumSelect enum-name="GOODS_STATUS_ENUM" v-model:value="form.goodsStatus" />
      </a-form-item>
      <a-form-item label="产地" name="place">
        <DictSelect key-code="GODOS_PLACE" v-model:value="form.place" />
      </a-form-item>
      <a-form-item label="上架状态" name="shelvesFlag">
        <a-radio-group v-model:value="form.shelvesFlag">
          <a-radio :value="true">上架</a-radio>
          <a-radio :value="false">下架</a-radio>
        </a-radio-group>
      </a-form-item>
      <a-form-item label="商品价格" name="price">
        <a-input-number style="width: 100%" placeholder="请输入商品价格" v-model:value="form.price" :min="0" />
      </a-form-item>
      <a-form-item label="备注" name="remark">
        <a-input style="width: 100%" placeholder="请输入备注" v-model:value="form.remark" />
      </a-form-item>
    </a-form>
    <div
      :style="{
        position: 'absolute',
        right: 0,
        bottom: 0,
        width: '100%',
        borderTop: '1px solid #e9e9e9',
        padding: '10px 16px',
        background: '#fff',
        textAlign: 'right',
        zIndex: 1,
      }"
    >
      <a-button style="margin-right: 8px" @click="onClose">取消</a-button>
      <a-button type="primary" @click="onSubmit">提交</a-button>
    </div>
  </a-drawer>
</template>
<script setup>
  import { ref, nextTick, reactive } from 'vue';
  import CategoryTree from '/@/components/business/category-tree-select/index.vue';
  import { CATEGORY_TYPE_ENUM } from '/@/constants/business/erp/category-const';
  import { message } from 'ant-design-vue';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { GOODS_STATUS_ENUM } from '/@/constants/business/erp/goods-const';
  import _ from 'lodash';
  import { goodsApi } from '/@/api/business/goods/goods-api';
  import { smartSentry } from '/@/lib/smart-sentry';
  import SmartEnumSelect from '/@/components/framework/smart-enum-select/index.vue';
  import DictSelect from '/@/components/support/dict-select/index.vue';

  // emit
  const emit = defineEmits(['reloadList']);

  // 组件ref
  const formRef = ref();

  const formDefault = {
    //商品分类
    categoryId: undefined,
    //商品名称
    goodsName: undefined,
    //商品状态
    goodsStatus: GOODS_STATUS_ENUM.APPOINTMENT.value,
    //产地
    place: undefined,
    //商品价格
    price: undefined,
    //上架状态
    shelvesFlag: true,
    //备注
    remark: '',
    //商品id
    goodsId: undefined,
  };
  let form = reactive({ ...formDefault });
  const rules = {
    categoryId: [{ required: true, message: '请选择商品分类' }],
    goodsName: [{ required: true, message: '商品名称不能为空' }],
    goodsStatus: [{ required: true, message: '商品状态不能为空' }],
    price: [{ required: true, message: '商品价格不能为空' }],
    place: [{ required: true, message: '产地不能为空' }],
  };
  // 是否展示抽屉
  const visible = ref(false);

  function showDrawer(rowData) {
    Object.assign(form, formDefault);
    if (rowData && !_.isEmpty(rowData)) {
      Object.assign(form, rowData);
    }
    if (form.place && form.place.length > 0) {
      form.place = form.place[0].valueCode;
    }
    console.log(form);
    visible.value = true;
    nextTick(() => {
      formRef.value.clearValidate();
    });
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
          let params = _.cloneDeep(form);
          if (params.place && Array.isArray(params.place) && params.place.length > 0) {
            params.place = params.place[0].valueCode;
          }
          if (form.goodsId) {
            await goodsApi.updateGoods(params);
          } else {
            await goodsApi.addGoods(params);
          }
          message.success(`${form.goodsId ? '修改' : '添加'}成功`);
          onClose();
          emit('reloadList');
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
    showDrawer,
  });
</script>
