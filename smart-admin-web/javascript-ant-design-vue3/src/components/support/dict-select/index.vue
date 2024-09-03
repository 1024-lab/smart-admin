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
      <a-select-option v-for="item in dictValueList" :key="item.valueCode" :value="item.valueCode">
        {{ item.valueName }}
      </a-select-option>
    </a-select>
  </div>
</template>

<script setup>
  import { computed, onMounted, ref, watch } from 'vue';
  import { dictApi } from '/src/api/support/dict-api';

  const props = defineProps({
    keyCode: String,
    value: [Array, String],
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
    disabled: {
      type: Boolean,
      default: false,
    },
  });

  // -------------------------- 查询 字典数据 --------------------------

  const dictValueList = ref([]);
  async function queryDict() {
    let res = await dictApi.valueList(props.keyCode);
    dictValueList.value = res.data;
  }

  onMounted(queryDict);

  // -------------------------- 选中 相关、事件 --------------------------

  const selectValue = ref(props.value);
  watch(
    () => props.value,
    (value) => {
      selectValue.value = value;
    }
  );

  const emit = defineEmits(['update:value', 'change']);
  function onChange(value) {
    if (!value) {
      emit('update:value', []);
      emit('change', []);
    }
    if (Array.isArray(value)) {
      emit('update:value', value);
      emit('change', value);
    } else {
      emit('update:value', [value]);
      emit('change', [value]);
    }
  }
</script>
