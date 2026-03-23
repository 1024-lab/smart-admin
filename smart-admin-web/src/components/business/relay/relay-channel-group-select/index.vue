<!--
  * Relay 渠道组下拉选择组件
  -->
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
    <a-select-option
      v-for="item in dataList"
      :key="item.channelGroupId"
      :value="item.channelGroupId"
      :label="item.channelName"
    >
      {{ item.channelName }}
    </a-select-option>
  </a-select>
</template>

<script setup>
  import { onMounted, ref, watch } from 'vue';
  import { relayChannelGroupApi } from '/@/api/business/ai/relay/relay-channel-group-api';
  import { smartSentry } from '/@/lib/smart-sentry';

  const props = defineProps({
    value: [Number, String],
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
    disabledFlag: {
      type: Number,
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
    () => props.disabledFlag,
    () => {
      queryData();
    }
  );

  function handleChange(value) {
    emit('update:value', value);
    emit('change', value);
  }

  async function queryData() {
    try {
      const res = await relayChannelGroupApi.queryList({ disabledFlag: props.disabledFlag });
      dataList.value = res.data || [];
    } catch (e) {
      smartSentry.captureError(e);
    }
  }

  onMounted(queryData);
</script>
