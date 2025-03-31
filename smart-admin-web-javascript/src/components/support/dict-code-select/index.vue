<!---
  * 字典key 下拉选择框
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
      :showSearch="true"
      :allowClear="true"
      :size="size"
      @change="onChange"
      :disabled="disabled"
    >
      <a-select-option v-for="item in dictList" :key="item.dictCode" :value="item.dictCode">
        {{ item.dictName }}
      </a-select-option>
    </a-select>
  </div>
</template>

<script setup>
  import { onMounted, ref, watch } from 'vue';
  import { dictApi } from '/@/api/support/dict-api';

  defineExpose({
    queryDict,
  });

  const props = defineProps({
    value: [Array, String],
    placeholder: {
      type: String,
      default: '请选择字典',
    },
    width: {
      type: String,
      default: '100%',
    },
    size: {
      type: String,
      default: 'default',
    },
    // 禁用标识
    disabled: {
      type: Number,
      default: null,
    },
  });

  // -------------------------- 查询 字典数据 --------------------------

  const dictList = ref([]);
  async function queryDict() {
    let response = await dictApi.getAllDict();
    dictList.value = response.data;
  }

  onMounted(queryDict);

  // -------------------------- 选中 相关、事件 --------------------------
  const emit = defineEmits(['update:value', 'change']);
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
