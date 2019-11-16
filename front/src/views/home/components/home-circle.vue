<template>
  <div class="circle-main">
    <i-circle :percent="80"
              :size="164"
              :trail-width="1"
              stroke-color="#3AA1FF"
              stroke-linecap="round"
              trail-color="#ededed">
      <span class="demo-Circle-inner"
            style="font-size:24px">80%</span>
    </i-circle>
    <div>
      <button>查看详情</button>
    </div>
  </div>
</template>

<script>
export default {};
</script>

<style lang="less">
.circle-main {
  background: #fff;
  height: 295px;
  text-align: center;
  padding-top: 20px;
  button {
    margin-top: 45px;
    width: 156px;
    cursor: pointer;
    outline: none;
    height: 36px;
    background: rgba(241, 241, 241, 1);
    border-radius: 18px;
    color: #808080;
    font-size: 18px;
    border: none;
  }
}
</style>
