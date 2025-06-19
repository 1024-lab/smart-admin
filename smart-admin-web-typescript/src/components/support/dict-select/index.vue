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
    <a-select
      v-model:value="selectValue"
      :style="`width: ${width}`"
      :placeholder="props.placeholder"
      :allowClear="true"
      :size="size"
      :mode="mode"
      @change="onChange"
      :disabled="disabled"
    >
      <a-select-option v-for="item in dictDataList" :key="item.dataValue" :value="item.dataValue" :disabled="disabledOption.includes(item.valueCode)">
        {{ item.dataLabel }}
      </a-select-option>
    </a-select>
  </div>
</template>

<script setup lang="ts">
  import { computed, ref, watch } from 'vue';
  import { useDictStore } from '/@/store/modules/system/dict.js';

  const props = defineProps({
    dictCode: String,
    value: [Array, String, Number],
    mode: {
      type: String,
      default: 'combobox',
    },
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
    // 禁用整个下拉选择框
    disabled: {
      type: Boolean,
      default: false,
    },
    // 需要禁用的选项字典值编码
    disabledOption: {
      type: Array,
      default: () => [],
    },
    // 需要隐藏的选项字典值编码
    hiddenOption: {
      type: Array,
      default: () => [],
    },
  });

  // -------------------------- 查询 字典数据 --------------------------

  const dictDataList = computed(() =>
    useDictStore()
      .getDictData(props.dictCode)
      .filter((item) => !props.hiddenOption.includes(item.dataValue) && !item.disabledFlag)
  );

  // -------------------------- 选中 相关、事件 --------------------------

  const selectValue = ref(props.value);

  watch(
    () => props.value,
    (newValue) => {
      // 如果传入的值是被禁用或被隐藏的选项，则移除这些选项
      if (Array.isArray(newValue)) {
        selectValue.value = newValue.filter((item) => !props.disabledOption.includes(item) && !props.hiddenOption.includes(item));
      } else {
        selectValue.value = props.hiddenOption.includes(newValue) || props.disabledOption.includes(newValue) ? undefined : newValue;
      }
    },
    { immediate: true }
  );

  const emit = defineEmits(['update:value', 'change']);

  function onChange(value) {
    emit('update:value', value);
    emit('change', value);
  }
</script>
