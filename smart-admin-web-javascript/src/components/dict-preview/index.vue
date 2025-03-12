<template>
  <div>
    <template v-for="(item, index) in options">
      <template v-if="values.includes(item.valueCode)">
        {{ item.valueName }}
        <span> </span>
      </template>
    </template>
  </div>
</template>

<script setup>
  import { computed } from 'vue';

  const props = defineProps({
    // 数据
    options: {
      type: Array,
      default: null,
    },
    // 当前的值
    value: [Number, String, Array],
  });
  const values = computed(() => {
    if (props.value === null || typeof props.value === 'undefined' || props.value === '') return [];
    return Array.isArray(props.value) ? props.value.map((item) => item.valueCode) : props.value.split(',');
  });
</script>
