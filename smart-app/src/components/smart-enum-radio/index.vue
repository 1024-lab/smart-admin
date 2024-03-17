<!--
  * 枚举 radio
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-08-08 20:32:30 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
  *
-->
<template>
  <radio-group @change="handleChange">
    <label v-for="item in $smartEnumPlugin.getValueDescList(props.enumName)" :key="item.value" class="smart-margin-right10">
      <radio :value="item.value + ''" :checked="item.value === modelValue">{{ item.desc }}</radio>
    </label>
  </radio-group>
</template>

<script setup>
  import { ref, watch } from 'vue';

  const props = defineProps({
    enumName: String,
    modelValue: [Number, String],
  });

  const emit = defineEmits(['update:modelValue', 'change']);

  const selectValue = ref(props.modelValue);

  watch(
    () => props.modelValue,
    (newValue) => {
      selectValue.value = newValue;
    }
  );

  function handleChange(e) {
    emit('update:modelValue', e.detail.value);
    emit('change', e.detail.value);
  }
</script>
