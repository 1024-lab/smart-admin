<template>
  <i :class="`iconfont icon-${type}`" :style="styles"></i>
</template>

<script>
export default {
  name: 'Icons',
  props: {
    // 图标名
    type: {
      type: String,
      required: true
    },
    // 图标颜色
    color: {
      type: String,
      default: '#5c6b77'
    },
    // 图标尺寸
    size: {
      type: Number,
      default: 16
    }
  },
  computed: {
    styles () {
      return {
        fontSize: `${this.size}px`,
        color: this.color
      };
    }
  }
};
</script>

<style>

</style>
