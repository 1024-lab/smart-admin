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
    <a-select-opt-group v-for="group in groupedList" :key="group.provider" :label="group.providerLabel">
      <a-select-option
        v-for="item in group.items"
        :key="item.id"
        :value="item.id"
        :label="buildLabel(item)"
      >
        {{ buildLabel(item) }}
      </a-select-option>
    </a-select-opt-group>
  </a-select>
</template>

<script setup>
  import { computed, onMounted, ref, watch } from 'vue';
  import { aiModelConfigApi } from '/@/api/business/ai/model/ai-model-config-api';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { AI_MODEL_PROVIDER_ENUM } from '/@/constants/business/ai/model-const';

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
    // 以下筛选参数均为可选
    provider: {
      type: String,
      default: undefined,
    },
    // true=只查启用, false=只查停用, undefined=不限
    active: {
      type: Boolean,
      default: true,
    },
    modelType: {
      type: String,
      default: undefined,
    },
    modelFeatures: {
      type: String,
      default: undefined,
    },
  });

  const emit = defineEmits(['update:value', 'change']);

  const selectValue = ref(props.value);
  const dataList = ref([]);

  // 供应商 code → 显示名称映射（从已有枚举中取，取不到则原样显示）
  const providerLabelMap = Object.fromEntries(
    Object.values(AI_MODEL_PROVIDER_ENUM).map((e) => [e.value, e.desc])
  );

  // 按 provider 分组
  const groupedList = computed(() => {
    const map = new Map();
    for (const item of dataList.value) {
      const key = item.provider || '';
      if (!map.has(key)) {
        map.set(key, {
          provider: key,
          providerLabel: providerLabelMap[key] || key,
          items: [],
        });
      }
      map.get(key).items.push(item);
    }
    return [...map.values()];
  });

  watch(
    () => props.value,
    (newValue) => {
      selectValue.value = newValue;
    }
  );

  watch(
    () => [props.provider, props.active, props.modelType, props.modelFeatures],
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
      // active(Boolean) → isActive('1'/'0'/undefined)
      const isActive = props.active === true ? '1' : props.active === false ? '0' : undefined;
      const res = await aiModelConfigApi.queryList({
        provider: props.provider,
        isActive,
        modelType: props.modelType,
        modelFeatures: props.modelFeatures,
      });
      dataList.value = res.data;
    } catch (e) {
      smartSentry.captureError(e);
    }
  }

  onMounted(queryData);
</script>
