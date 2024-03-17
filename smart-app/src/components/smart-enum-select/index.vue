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
    <uni-data-select :localdata="dataList" v-model="selectValue" :clear="true" @change="onChange" />
  </div>
</template>

<script setup>
  import { inject, onMounted, ref, watch } from 'vue';

  const smartEnumPlugin = inject('smartEnumPlugin');

  const props = defineProps({
    enumName: String,
    modelValue: [Number, String],
  });

  // -------------------------- 枚举数据列表 --------------------------
  const dataList = ref([]);
  function getEnumData() {
    dataList.value = smartEnumPlugin.getValueDescList(props.enumName).map((e) => Object.assign({}, { text: e.desc, value: e.value }));
  }
  onMounted(getEnumData);

  // -------------------------- 选中 相关、事件 --------------------------

  const selectValue = ref(props.modelValue);
  watch(
    () => props.modelValue,
    (value) => {
      selectValue.value = value;
    }
  );

  const emit = defineEmits(['update:modelValue', 'change']);
  function onChange(value) {
    emit('update:modelValue', value);
    emit('change', value, smartEnumPlugin.getDescByValue(props.enumName, value));
  }
</script>
