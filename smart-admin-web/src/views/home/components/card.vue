<template>
  <div class="card-main">
    <div class="title">
      {{title}}
      <span>{{desc}}</span>
    </div>
    <slot></slot>
  </div>
</template>

<script>
export default {
  props: {
    title: {
      type: String,
      default: '标题'
    },
    desc: {
      type: String,
      default: '描述'
    }
  }
};
</script>

<style lang='less'>
.card-main {
  border-radius: 8px;
  background: #fff;
  margin-bottom: 20px;
  padding-bottom: 10px;
}
.title {
  color: #060606;
  font-size: 16px;
  padding: 20px 32px;
  span {
    padding-left: 17px;
    font-size: 12px;
    color: #dededf;
  }
}
</style>
