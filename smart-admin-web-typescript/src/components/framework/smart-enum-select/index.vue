<!--
  * 枚举 下拉框
  *
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2022-08-08 20:32:30
  * @Wechat:    zhuda1024
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
  *
-->
<template>
  <a-select
    v-model:value="selectValue"
    :style="`width: ${width}`"
    :placeholder="props.placeholder"
    :showSearch="true"
    :allowClear="true"
    :size="size"
    @change="handleChange"
    :disabled="disabled"
  >
    <a-select-option v-for="item in valueDescList" :key="item.value" :value="item.value" :disabled="disabledOption.includes(item.value)">
      {{ item.desc }}
    </a-select-option>
  </a-select>
</template>

<script setup lang="ts">
  import { ref, watch, onMounted, getCurrentInstance } from 'vue';

  const props = defineProps({
    enumName: String,
    value: [Number, String],
    width: {
      type: String,
      default: '100%',
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
    // 需要禁用的选项枚举值
    disabledOption: {
      type: Array,
      default: () => [],
    },
    // 需要隐藏的选项枚举值
    hiddenOption: {
      type: Array,
      default: () => [],
    },
  });

  const valueDescList = ref([]);

  onMounted(() => {
    const internalInstance = getCurrentInstance(); // 有效  全局
    const smartEnumPlugin = internalInstance.appContext.config.globalProperties.$smartEnumPlugin;
    valueDescList.value = smartEnumPlugin.getValueDescList(props.enumName).filter((item) => !props.hiddenOption.includes(item.value));
  });

  const selectValue = ref(props.value);

  watch(
    () => props.value,
    (newValue) => {
      // 如果传入的值是被禁用或被隐藏的选项，则移除该选项
      selectValue.value = props.disabledOption.includes(newValue) || props.hiddenOption.includes(newValue) ? undefined : newValue;
    },
    { immediate: true }
  );

  const emit = defineEmits(['update:value', 'change']);

  function handleChange(value) {
    emit('update:value', value);
    emit('change', value);
  }
</script>
