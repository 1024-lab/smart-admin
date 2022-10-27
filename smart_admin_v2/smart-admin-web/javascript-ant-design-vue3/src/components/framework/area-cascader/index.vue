<!-- 
  * 地区选择框
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-09-12 15:22:45 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
  *
-->

<template>
  <a-cascader
    :style="`width:${width}`"
    v-model:value="areaValue"
    :show-search="{ filter }"
    :options="areaOptionData"
    :placeholder="placeholder"
    :size="size"
    @change="handleChange"
  />
</template>

<script setup>
  import { PROVINCE_CITY_DISTRICT } from "./province-city-district";
  import { PROVINCE_CITY } from "./province-city";
  import { ref, toRaw, watch } from "vue";

  // ============ 组件属性 ============

  const TYPE_PROVINCE_CITY_DISTRICT = "province_city_district";
  const TYPE_PROVINCE_CITY = "province_city";

  const props = defineProps({
    type: String,
    value: [Number, Array],
    width: {
      type: String,
      default: '200px',
    },
    placeholder: {
      type: String,
      default: '请选择地区',
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

  const emit = defineEmits(['update:value', 'change']);

  // ============ 组件业务 ============
  const areaOptionData =
    props.type === TYPE_PROVINCE_CITY_DISTRICT ? PROVINCE_CITY_DISTRICT : PROVINCE_CITY;

  // 绑定地区数据
  const areaValue = ref([]);
  // 监听value变化
  watch(
      () => props.value,
      (newValue) => {
        if (newValue) {
          let array = [];
          for (let index = 0; index < 3; index++) {
            if (newValue[index]) {
              array.push(newValue[index].value);
            }
          }
        areaValue.value = array;
      } else {
        areaValue.value = [];
      }
    }
  );

  function handleChange(value, selectedOptions){
    emit("update:value", toRaw(selectedOptions));
    emit("change", value, toRaw(selectedOptions));
  }

  const filter = (inputValue, path) => {
    return path.some(
      (option) => option.label.toLowerCase().indexOf(inputValue.toLowerCase()) > -1
    );
  };
</script>
