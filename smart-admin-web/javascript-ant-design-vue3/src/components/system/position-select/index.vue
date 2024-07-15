<!--
  * 职位
  *
  * @Author:    开云
  * @Date:      2024-06-27 23:09:02
  * @Wechat:    kaiyun
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
-->
<template>
  <a-select
    v-model:value="selectValue"
    :style="`width: ${width}`"
    :placeholder="props.placeholder"
    :showSearch="true"
    :allowClear="true"
    :size="size"
    @change="onChange"
  >
    <a-select-option v-for="item in positionList" :key="item.positionId" :value="item.positionId">
      {{ item.positionName }}
    </a-select-option>
  </a-select>
</template>

<script setup>
  import { onMounted, ref, watch } from 'vue';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { positionApi } from '/@/api/system/position-api.js';

  // =========== 属性定义 和 事件方法暴露 =============

  const props = defineProps({
    value: [Number, Array],
    placeholder: {
      type: String,
      default: '请选择',
    },
    width: {
      type: String,
      default: '100%',
    },
    size: {
      type: String,
      default: 'default',
    },
  });

  const emit = defineEmits(['update:value', 'change']);

  // =========== 查询数据 =============

  //员工列表数据
  const positionList = ref([]);
  async function query() {
    try {
      let resp = await positionApi.queryList();
      positionList.value = resp.data;
    } catch (e) {
      smartSentry.captureError(e);
    }
  }
  onMounted(query);

  // =========== 选择 监听、事件 =============

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
