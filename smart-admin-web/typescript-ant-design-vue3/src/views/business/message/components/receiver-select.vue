<template>
  <a-select
    v-model:value="selectValue"
    :style="`width: ${width}`"
    :placeholder="props.placeholder"
    :showSearch="true"
    :allowClear="true"
    :size="size"
    @change="handleChange"
    @focus="handleVisibleChange"
    :disabled="disabled"
    :mode="multiple ? 'multiple' : ''"
    optionFilterProp="label"
  >
    <a-select-option v-for="(item, index) in dataList" :key="receiverType == MESSAGE_RECEIVE_TYPE_ENUM.EMPLOYEE.value?item.employeeId : index">
      <span v-if="receiverType == MESSAGE_RECEIVE_TYPE_ENUM.EMPLOYEE.value">
        {{ item.actualName }}
      </span>
    </a-select-option>
  </a-select>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue';
import { employeeApi } from '/@/api/system/employee/employee-api';

import {MESSAGE_RECEIVE_TYPE_ENUM} from '/src/constants/business/message/message-const.ts'
import { message } from 'ant-design-vue';

const props = defineProps({
  value: [Number, String, Object],
  width: {
    type: String,
    default: '200px',
  },
  placeholder: {
    type: String,
    default: '请选择',
  },
  size: {
    type: String,
    default: 'default',
  },
  disabled: {
    type: Boolean,
    default: false,
  },
  multiple:{
    type: Boolean,
    default: false,
  },
  receiverType:{
    type: Number,
  },
  auditStatus: {
    type: Number
  },
});
const emit = defineEmits(['update:value', 'change']);

const selectValue = ref(props.value);

// 箭头value变化
watch(
  () => props.value,
  (newValue) => {
    selectValue.value = newValue;
  }
);
watch(
  () => props.receiverType,
  (newValue) => {
    selectValue.value=null
    dataList.value=[]
    queryData()
  }
);

function handleChange (value) {
  let obj = null;
  if (value) {
    if(props.receiverType == MESSAGE_RECEIVE_TYPE_ENUM.EMPLOYEE.value){
      obj = dataList.value.find(e => e.employeeId == value);
    }
  }
  emit('update:value', value);
  emit('change', value, obj);
}

const dataList = ref([]);
async function queryData() {
  if(props.receiverType == MESSAGE_RECEIVE_TYPE_ENUM.EMPLOYEE.value){
    const res = await employeeApi.queryAll();
    dataList.value = res.data;
    return;
  }
}
const handleVisibleChange=()=>{
  if(!props.receiverType){
    message.error('请先选择接收方类型')
  }

}
onMounted(queryData);
</script>
