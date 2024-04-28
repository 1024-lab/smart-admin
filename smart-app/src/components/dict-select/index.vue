<!---
  * 字段 下拉选择框
  * 
  * @Author:    1024创新实验室：罗伊
  * @Date:      2022-09-12 22:06:45 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
  *
-->
<template>
  <div>
    <uni-data-select :localdata="dictValueList" v-model="selectValue" :placeholder="props.placeholder" :clear="true" @change="onChange" />
  </div>
</template>

<script setup>
  import { onMounted, ref, watch } from 'vue';
  import { dictApi } from '@/api/support/dict-api';

  const props = defineProps({
    keyCode: String,
    modelValue: [String],
    placeholder: {
      type: String,
      default: '请选择',
    },
  });

  // -------------------------- 查询 字典数据 --------------------------

  const dictValueList = ref([]);
  async function queryDict() {
    let res = await dictApi.valueList(props.keyCode);
    dictValueList.value = res.data.map((e) => Object.assign({}, { text: e.valueName, value: e.valueCode }));
  }
  onMounted(queryDict);

  // -------------------------- 选中 相关、事件 --------------------------

  const selectValue = ref(props.modelValue);
  watch(
    () => props.modelValue,
    (value) => {
      selectValue.value = value;
    }
  );

  const emit = defineEmits(['update:modelValue', 'change']);
  function onChange(selectValue) {
    let find = dictValueList.value.filter((e) => e.value === selectValue)[0];
    emit('update:modelValue', find.value);
    emit('change', find.value, find.text);
  }
</script>
