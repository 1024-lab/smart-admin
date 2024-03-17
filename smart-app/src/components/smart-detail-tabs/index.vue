<template>
  <scroll-view class="scroll-view" scroll-x="true" :show-scrollbar="false">
    <view class="item" :class="active === item.value ? 'active' : ''" v-for="item in tabsList" :key="item.value" @click="change(item.value)">
      {{ item.label }}
    </view>
  </scroll-view>
</template>

<script setup>
  import { ref, watch } from 'vue';
  const props = defineProps({
    modelValue: {
      type: Number,
      default: 0,
    },
    position: {
      type: String,
      default: 'fixed',
    },
    tabsList: {
      type: Array,
      default: [],
    },
  });

  const position = ref(props.position);
  const active = ref(props.modelValue);

  watch(
    () => props.modelValue,
    (newValue) => {
      active.value = newValue;
    }
  );

  const emit = defineEmits(['update:modelValue', 'change']);

  const change = (value) => {
    active.value = value;
    emit('update:modelValue', value);
    emit('change', value);
  };
</script>

<style lang="scss" scoped>
  .scroll-view {
    position: v-bind(position);
    background-color: #f4f4f4;
    z-index: 998;

    :deep(::-webkit-scrollbar) {
      height: 0 !important;
      width: 0 !important;
      background: transparent;
    }

    .item {
      padding: 0 24rpx;
      height: 72rpx;
      font-size: 30rpx;
      color: #777;
      background: #fff;
      display: inline-block;
      border-radius: 8rpx;
      line-height: 72rpx;
      margin: 24rpx 0 24rpx 24rpx;
    }
    .active {
      color: #ffffff;
      background: #1a9aff;
    }
  }
</style>
