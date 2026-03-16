<!--
  * Relay channel select
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
    :mode="multiple ? 'multiple' : undefined"
    optionFilterProp="label"
    @change="handleChange"
  >
    <a-select-option v-for="item in dataList" :key="item.channelId" :value="item.channelId" :label="item.channelName">
      {{ item.channelName }}
    </a-select-option>
  </a-select>
</template>

<script setup>
  import { onMounted, ref, watch } from 'vue';
  import { relayChannelApi } from '/@/api/business/ai/relay/relay-channel-api';
  import { smartSentry } from '/@/lib/smart-sentry';

  const props = defineProps({
    value: [Number, String, Array],
    width: {
      type: String,
      default: '200px',
    },
    placeholder: {
      type: String,
      default: '\u8bf7\u9009\u62e9',
    },
    size: {
      type: String,
      default: 'default',
    },
    disabled: {
      type: Boolean,
      default: false,
    },
    multiple: {
      type: Boolean,
      default: false,
    },
    disabledFlag: {
      type: Number,
      required: true,
    },
    providerType: {
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
    () => [props.disabledFlag, props.providerType],
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
      const params = {
        disabledFlag: props.disabledFlag,
        providerType: props.providerType,
      };
      const res = await relayChannelApi.queryList(params);
      dataList.value = res.data;
    } catch (e) {
      smartSentry.captureError(e);
    }
  }

  onMounted(queryData);
</script>
