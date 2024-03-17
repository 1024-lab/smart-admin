<template>
  <view class="list-container">
    <view class="list-item" @click="gotoDetail(item.goodsId)" v-for="item in list" :key="item.noticeId">
      <view class="title"> {{ item.goodsName }} </view>
      <view class="author" v-if="item.remark"> 备注：{{ item.remark }} </view>
      <view class="author" v-if="item.categoryName"> 分类：{{ item.categoryName }} </view>
      <view class="author"> 创建时间：{{ item.createTime }} </view>
    </view>
  </view>
</template>

<script setup>
  const props = defineProps({
    marginTop: {
      type: String,
      default: '0',
    },
    list: {
      type: Array,
      default: [],
    },
  });

  function gotoDetail(goodsId) {
    uni.navigateTo({ url: '/pages/goods/goods-detail?noticeId=' + goodsId });
  }
</script>

<style lang="scss" scoped>
  .list-container {
    margin-top: v-bind(marginTop);
    padding: 20rpx 10rpx;
  }
  .list-item {
    box-sizing: border-box;
    margin: 0 30rpx;
    padding: 30rpx 0;
    border-bottom: #cdcdcd 1px solid;

    .title {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20rpx;
      font-size: 16px;
      text-overflow: ellipsis;
      white-space: nowrap;
      overflow: hidden;
    }
    .author {
      margin-bottom: 14rpx;
      color: #999999;
      font-size: 12px;
      text-overflow: ellipsis;
    }
    .publish-time {
      font-size: 12px;
      color: #999999;
      display: flex;
      flex-direction: row;
      justify-content: space-between;

      .un-read {
        color: $uni-color-primary;
      }
    }
  }
</style>
