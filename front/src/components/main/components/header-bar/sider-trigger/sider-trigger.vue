<template>
  <a @click="handleChange" type="text" :class="['sider-trigger-a', collapsed ? 'collapsed' : '']">
    <Icon :type="icon" :size="size"/>
  </a>
</template>
<script>
export default {
  name: 'SiderTrigger',
  props: {
    // 折叠状态
    collapsed: {
      type: Boolean,
      require: false
    },
    // 图标名称
    icon: {
      type: String,
      default: 'navicon-round'
    },
    // 图标尺寸
    size: {
      type: Number,
      default: 20
    }
  },
  methods: {
    handleChange () {
      this.$emit('on-change', !this.collapsed);
    }
  }
};
</script>
<style lang="less">
@import "./sider-trigger.less";
</style>
