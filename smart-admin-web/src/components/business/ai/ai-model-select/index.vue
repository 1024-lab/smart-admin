<template>
  <a-select
    v-model:value="selectValue"
    :style="`width: ${width}`"
    :placeholder="props.placeholder"
    :showSearch="true"
    :allowClear="true"
    :size="size"
    :disabled="disabled"
    optionFilterProp="label"
    @change="handleChange"
  >
    <a-select-option v-for="item in dataList" :key="item.id" :value="item.id" :label="buildLabel(item)">
      {{ buildLabel(item) }}
    </a-select-option>
  </a-select>
</template>

<script setup>
  import { onMounted, ref, watch } from 'vue';
  import { aiModelConfigApi } from '/@/api/business/ai/model/ai-model-config-api';
  import { smartSentry } from '/@/lib/smart-sentry';

  const props = defineProps({
    value: [Number, String],
    width: {
      type: String,
      default: '100%',
    },
    placeholder: {
      type: String,
      default: '请选择模型',
    },
    size: {
      type: String,
      default: 'default',
    },
    disabled: {
      type: Boolean,
      default: false,
    },
    provider: {
      type: String,
      default: undefined,
    },
  });

  const emit = defineEmits(['update:value', 'change']);

  const selectValue = ref(props.value);
  const dataList = ref([]);

  watch(
    () => props.value,
    (newValue) => {
      selectValue.value = newValue;
    }
  );

  watch(
    () => props.provider,
    () => {
      queryData();
    }
  );

  function handleChange(value) {
    emit('update:value', value);
    emit('change', value);
  }

  function buildLabel(item) {
    if (!item) {
      return '';
    }
    return item.endPointModeName ? `${item.modelName} (${item.endPointModeName})` : item.modelName;
  }

  async function queryData() {
    try {
      const res = await aiModelConfigApi.queryList({
        provider: props.provider,
      });
      dataList.value = res.data;
    } catch (e) {
      smartSentry.captureError(e);
    }
  }

  onMounted(queryData);
</script>
