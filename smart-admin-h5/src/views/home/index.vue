<!-- home -->
<template>
  <div>
    <div style="background-color: #1989fa;height: ">
      <h4>
        <center>首页</center>
      </h4>
      <van-row gutter="20">
        <van-col span="12">span: 8</van-col>
        <van-col span="12">span: 8</van-col>
      </van-row>

    </div>

  </div>
</template>

<script>

export default {
  data() {
    return {
      list: []
    };
  },

  computed: {},

  mounted() {
  },

  methods: {}
};
</script>
<style lang="scss" scoped>
.index-container {
  .warpper {
    padding: 12px;
    background: #fff;

    .demo-home__title {
      margin: 0 0 6px;
      font-size: 32px;

      .demo-home__title img,
      .demo-home__title span {
        display: inline-block;
        vertical-align: middle;
      }

      img {
        width: 32px;
      }

      span {
        margin-left: 16px;
        font-weight: 500;
      }
    }

    .demo-home__desc {
      margin: 0 0 20px;
      color: rgba(69, 90, 100, 0.6);
      font-size: 14px;
    }
  }
}
</style>
