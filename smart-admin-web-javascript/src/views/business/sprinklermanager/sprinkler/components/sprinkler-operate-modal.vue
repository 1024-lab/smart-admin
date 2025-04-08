<template>
  <a-modal :open="visible" title="添加/编辑" :width="700" forceRender ok-text="确认" cancel-text="取消" @ok="onSubmit" @cancel="onClose">
    <a-form ref="formRef" :model="form" :rules="rules" :label-col="{ span: 6 }">
      <a-form-item label="喷头序列号" name="sprinklerSerial">
        <a-input v-model:value="form.sprinklerSerial" placeholder="请输入喷头序列号" />
      </a-form-item>

      <a-form-item label="入库日期" class="smart-query-form-item">
        <a-space direction="vertical" :size="12">
          <a-date-picker valueFormat="YYYY-MM-DD" v-model:value="form.warehouseDate" style="width: 150px" />
        </a-space>
      </a-form-item>
      <a-form-item label="启用状态" name="disabledFlag">
        <a-switch v-model:checked="enabledChecked" @change="enabledCheckedChange" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup>
  import { message } from 'ant-design-vue';
  import _ from 'lodash';
  import { nextTick, reactive, ref } from 'vue';
  import AreaCascader from '/@/components/framework/area-cascader/index.vue';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import Upload from '/@/components/support/file-upload/index.vue';
  import { regular } from '/@/constants/regular-const';
  import { smartSentry } from '/@/lib/smart-sentry';
  import SmartEnumSelect from '/@/components/framework/smart-enum-select/index.vue';
  import {sprinklerApi} from "/@/api/business/sprinklermanager/sprinkler-api.js";
  import {defaultTimeRanges} from "/@/lib/default-time-ranges.js";

  defineExpose({
    showModal,
  });
  const emit = defineEmits(['refresh']);

  // --------------------- modal 显示与隐藏 ---------------------
  // 是否展示
  const visible = ref(false);

  function showModal(sprinklerId) {
    Object.assign(form, formDefault);
    area.value = [];
    if (sprinklerId) {
      detail(sprinklerId);
    }
    visible.value = true;
    nextTick(() => {
      // 解决弹窗错误信息显示,没有可忽略
      const domArr = document.getElementsByClassName('ant-modal');
      if (domArr && domArr.length > 0) {
        Array.from(domArr).forEach((item) => {
          if (item.childNodes && item.childNodes.length > 0) {
            Array.from(item.childNodes).forEach((child) => {
              if (child.setAttribute) {
                child.setAttribute('aria-hidden', 'false');
              }
            });
          }
        });
      }
    });
  }

  function onClose() {
    visible.value = false;
  }

  async function detail(sprinklerId) {
    try {
      let result = await sprinklerApi.detail(sprinklerId);
      let data = result.data;
      Object.assign(form, data);
      console.log(form)
      nextTick(() => {
        // 省市区不存在，不需要赋值
        if (!data.provinceName) {
          return;
        }
        area.value = [
          {
            value: data.province,
            label: data.provinceName,
          },
          {
            value: data.city,
            label: data.cityName,
          },
          {
            value: data.district,
            label: data.districtName,
          },
        ];
      });
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      SmartLoading.hide();
    }
  }

  // --------------------- 表单 ---------------------

  //  组件
  const formRef = ref();

  const formDefault = {
    sprinklerId: undefined,
    sprinklerSerial: undefined,
    warehouseDate: undefined,
    disabledFlag: false,
  };
  let form = reactive({ ...formDefault });


  const rules = {
    sprinklerSerial: [{ required: true, message: '请输入喷头序列号' }],
    warehouseDate: [{ required: true, message: '请输入入仓时间' }],
  };

  function onSubmit() {
    formRef.value
      .validate()
      .then(async () => {
        SmartLoading.show();
        try {
          if (form.sprinklerId) {
            await sprinklerApi.update(form);
          } else {
            await sprinklerApi.create(form);
          }
          message.success(`${form.sprinklerId ? '修改' : '添加'}成功`);
          emit('refresh');
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

  // 状态
  const enabledChecked = ref(true);

  function enabledCheckedChange(checked) {
    form.disabledFlag = !checked;
  }

  // 地区
  const area = ref([]);

  function changeArea(value, selectedOptions) {
    Object.assign(form, {
      province: '',
      provinceName: '',
      city: '',
      cityName: '',
      district: '',
      districtName: '',
    });
    if (!_.isEmpty(selectedOptions)) {
      // 地区信息
      form.province = area.value[0].value;
      form.provinceName = area.value[0].label;

      form.city = area.value[1].value;
      form.cityName = area.value[1].label;
      if (area.value[2]) {
        form.district = area.value[2].value;
        form.districtName = area.value[2].label;
      }
    }
  }


</script>

<style lang="less" scoped>
  .form-width {
    width: 100% !important;
  }

  .footer {
    width: 100%;
    display: flex;
    justify-content: flex-end;
  }

  :deep(.ant-card-body) {
    padding: 10px;
  }
</style>
