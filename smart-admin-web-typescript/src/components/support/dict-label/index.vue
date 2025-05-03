<template>
  <span>{{ dataLabels }}</span>
</template>

<script setup lang="ts">
  import { computed } from 'vue';
  import { useDictStore } from '/@/store/modules/system/dict.js';

  const props = defineProps({
    dictCode: String,
    dataValue: [String, Number],
  });
  const dataLabels = computed(() => {
    return useDictStore().getDataLabels(props.dictCode, props.dataValue);
  });
</script>
