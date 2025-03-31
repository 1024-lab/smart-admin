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
    <a-radio-group v-model:value="selectValue" @change="handleChange" button-style="solid" :disabled="disabled">
      <a-radio-button v-for="item in valueDescList" :key="item.value" :value="item.value" :disabled="disabledOption.includes(item.value)">
        {{ item.desc }}
      </a-radio-button>
    </a-radio-group>
  </template>
  <template v-else>
    <a-radio-group v-model:value="selectValue" @change="handleChange" :disabled="disabled">
      <a-radio v-for="item in valueDescList" :key="item.value" :value="item.value" :disabled="disabledOption.includes(item.value)">
        {{ item.desc }}
      </a-radio>
    </a-radio-group>
  </template>
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
    size: {
      type: String,
      default: 'default',
    },
    isButton: {
      type: Boolean,
      default: false,
    },
    // 禁用整个单选框
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

  function handleChange(e) {
    emit('update:value', e.target.value);
    emit('change', e.target.value);
  }
</script>
