<!-- home -->
<template>
  <div>
    <van-nav-bar title="审批中心">
      <template #right>
        <van-icon name="search" size="18" style="margin-right: 20px" />
        <van-icon name="filter-o" size="18" />
      </template>
    </van-nav-bar>

    <van-tabs v-model="bpmStatusActive" sticky>
      <van-tab title="标签 1">
        <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
          <van-list
            v-model="loading"
            :finished="finished"
            finished-text="没有更多了"
            @load="onLoad"
          >

            <van-cell title="单元格" value="内容" size="large" label="描述信息" />
            <van-cell title="单元格" value="内容" size="large" label="描述信息" />
            <van-cell title="单元格" value="内容" size="large" label="描述信息" />
            <van-cell title="单元格" value="内容" size="large" label="描述信息" />

          </van-list>
        </van-pull-refresh>
      </van-tab>
      <van-tab title="标签 2">内容 2</van-tab>
      <van-tab title="标签 3">内容 3</van-tab>
      <van-tab title="标签 4">内容 4</van-tab>
    </van-tabs>

  </div>
</template>

<script>
export default {
  data() {
    return {
      list: ['asdasd', 'asdasd', 'asdasd'],
      bpmStatusActive: 0,
      finished: true,
      refreshing: true,
      loading: true
    };
  },

  computed: {},

  mounted() {
  },

  methods: {
    onRefresh() {

    },
    onLoad() {
      this.finished = true;
    }
  }
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
