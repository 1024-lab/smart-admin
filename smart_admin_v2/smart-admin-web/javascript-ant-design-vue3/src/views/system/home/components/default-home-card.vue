<!--
  * 首页 card 插槽
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-09-12 22:34:00 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
  *
-->
<template>
  <div class="card-container">
    <a-card size="small">
      <template #title>
        <div class="title">
          <component :is="$antIcons[props.icon]" v-if="props.icon" :style="{ fontSize: '18px' }" />
          <slot name="title"></slot>
          <span v-if="!$slots.title" class="smart-margin-left10">{{ props.title }}</span>
        </div>
      </template>
      <template v-if="props.extra" #extra>
        <slot name="extra"></slot>
        <a v-if="!$slots.extra" @click="extraClick">{{ props.extra }}</a>
      </template>
      <slot></slot>
    </a-card>
  </div>
</template>
<script setup>
let props = defineProps({
  icon: String,
  title: String,
  extra: String,
});
let emits = defineEmits(['extraClick']);

function extraClick() {
  emits('extraClick');
}
</script>
<style lang="less" scoped>
.card-container {
  background-color: #fff;
  height: 100%;

  .title {
    display: flex;
    align-items: center;
    &::before {
      content: '';
      position: absolute;
      top: 3px;
      left: 0;
      width: 3px;
      height: 30px;
      background-color: @primary-color;
    }
  }
}
</style>
