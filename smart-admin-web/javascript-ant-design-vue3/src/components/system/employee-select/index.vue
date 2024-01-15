<!--
  * 员工下拉选择框 
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-09-12 15:09:02 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
-->
<template>
  <a-select
    v-model:value="selectValue"
    :style="`width: ${width}`"
    :placeholder="props.placeholder"
    :showSearch="true"
    :allowClear="true"
    :size="size"
    @change="onChange"
  >
    <a-select-option v-for="item in employeeList" :key="item.employeeId" :value="item.employeeId">
      {{ item.actualName }}
      <template v-if="item.departmentName"> （{{ item.departmentName }}） </template>
    </a-select-option>
  </a-select>
</template>

<script setup>
  import { onMounted, ref, watch } from 'vue';
  import { employeeApi } from '/src/api/system/employee-api';
  import { smartSentry } from '/@/lib/smart-sentry';

  // =========== 属性定义 和 事件方法暴露 =============

  const props = defineProps({
    value: [Number, Array],
    placeholder: {
      type: String,
      default: '请选择',
    },
    width: {
      type: String,
      default: '100%',
    },
    size: {
      type: String,
      default: 'default',
    },
    // 角色ID，可为空
    roleId: {
      type: Number,
      default: null,
    },
    // 禁用标识
    disabledFlag: {
      type: Number,
      default: null,
    },
  });

  const emit = defineEmits(['update:value', 'change']);

  // =========== 查询数据 =============

  //员工列表数据
  const employeeList = ref([]);
  async function query() {
    try {
      let params = {};
      if (props.roleId) {
        params = { roleId: props.roleId };
      }
      if (null != props.disabledFlag) {
        params.disabledFlag = props.disabledFlag;
      }
      let resp = await employeeApi.queryAll(params);
      employeeList.value = resp.data;
    } catch (e) {
      smartSentry.captureError(e);
    }
  }
  onMounted(query);

  // =========== 选择 监听、事件 =============
  
  const selectValue = ref(props.value);
  watch(
    () => props.value,
    (newValue) => {
      selectValue.value = newValue;
    }
  );

  function onChange(value) {
    emit('update:value', value);
    emit('change', value);
  }
</script>
