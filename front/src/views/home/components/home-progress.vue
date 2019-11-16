<template>
  <div class="progress-list">
    <div :key="item.name"
         class="item"
         v-for="item in value">
      <p>
        <i :style="{background:item.color}"></i>
        {{item.name}}
        <span>{{item.value}}</span>
      </p>
      <Progress :percent="45"
                :stroke-color="item.color"
                :stroke-width="16"
                class="progress"
                hide-info
                status="active" />
    </div>
  </div>
</template>

<script>
export default {
  props: {
    value: Array
  }
};
</script>

<style lang="less">
.progress-list {
  height: 295px;
  padding: 28px;
  .item {
    display: flex;
    font-size: 16px;
    color: #595959;
    margin-bottom: 16px;
    span {
      color: #808080;
      margin: 0 29px;
    }
    p {
      width: 400px;
      i {
        display: inline-block;
        border-radius: 5px;
        width: 10px;
        height: 10px;
        margin-right: 13px;
        background: #f66;
      }
      padding-right: 200px;
    }
    .progress {
      flex: 1;
    }
  }
}
</style>
