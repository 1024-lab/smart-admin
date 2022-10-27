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
  <template v-if="isButton">
    <a-radio-group v-model:value="selectValue" @change="handleChange" button-style="solid">
      <a-radio-button v-for="item in $smartEnumPlugin.getValueDescList(props.enumName)" :key="item.value" :value="item.value">
        {{ item.desc }}
      </a-radio-button>
    </a-radio-group>
  </template>
  <template v-else>
    <a-radio-group v-model:value="selectValue" @change="handleChange">
      <a-radio v-for="item in $smartEnumPlugin.getValueDescList(props.enumName)" :key="item.value" :value="item.value">
        {{ item.desc }}
      </a-radio>
    </a-radio-group>
  </template>
</template>

<script setup>
  import { ref, watch } from 'vue';

  const props = defineProps({
    enumName: String,
    value: [Number, String],
    width: {
      type: String,
      default: '100%',
    },
    size: {
      type: String,
      default: 'default',
    },
    isButton: {
      type: Boolean,
      default: false,
    },
  });

  const emit = defineEmits(['update:value', 'change']);

  const selectValue = ref(props.value);

  watch(
    () => props.value,
    (newValue) => {
      selectValue.value = newValue;
    }
  );

  function handleChange(e) {
    emit('update:value', e.target.value);
    emit('change', e.target.value);
  }
</script>
