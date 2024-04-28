<template>
  <view class="container">
    <uni-card title="品质商品" :isFull="true" padding="0px" spacing="0px">
      <view class="course">
        <scroll-view class="scroll-view" scroll-x="true">
          <view class="scroll-view-item" v-for="item in goodsList" :key="item.goodsId">
            <div style="display: flex">
              <image class="scroll-item-left" src="/static/logo.png" mode=""></image>
              <view class="scroll-item-right">
                <view class="item-title"> {{ item.categoryName }}-{{ item.goodsName }} </view>
                <view class="item-right-bottom">
                  <view class="item-time"> 产地：{{ item.place[0].valueName }}</view>
                </view>
                <view class="item-right-bottom">
                  <view class="item-time"> {{ item.remark }}</view>
                  <view class="item-reserved"> 预约 </view>
                </view>
              </view>
            </div>
          </view>
        </scroll-view>
      </view>
    </uni-card>
  </view>
</template>

<script setup>
  import { ref } from 'vue';
  import { smartSentry } from '@/lib/smart-sentry';
  import { onShow } from '@dcloudio/uni-app';
  import { goodsApi } from '@/api/business/goods/goods-api';

  const queryForm = {
    pageNum: 1,
    pageSize: 5,
    searchCount: false,
  };

  const goodsList = ref([]);
  async function queryGoodsList() {
    try {
      const result = await goodsApi.queryGoodsList(queryForm);
      goodsList.value = result.data.list;
    } catch (err) {
      smartSentry.captureError(err);
    }
  }

  onShow(() => {
    queryGoodsList();
  });

  // 查看更多
  function onMore() {
    router.push({
      path: '/oa/notice/notice-employee-list',
    });
  }

  function toDetail(noticeId) {
    router.push({
      path: '/oa/notice/notice-employee-detail',
      query: { noticeId },
    });
  }
</script>

<style lang="scss" scoped>
  .container {
    width: 700rpx;
    margin: 0 auto 20rpx;
    border-radius: 12rpx;
    overflow: hidden;
    :deep(.uni-card__header-box) {
      font-weight: bold;
    }
    :deep(.uni-card__content) {
      padding: 0 !important;
    }
    :deep(.uni-card__header) {
      background: linear-gradient(180deg, #e8f4ff, #f8fcff);
    }
  }

  .course {
    padding: 30rpx 0 30rpx 30rpx;
    background-color: #fff;
  }
  .scroll-view-item {
    display: inline-block;
    width: 570rpx;
    height: 128rpx;
    overflow: hidden;
    font-size: 36rpx;
    border-right: 1rpx solid #ededed;
    margin-right: 30rpx;
    .scroll-item-left {
      width: 128rpx;
      height: 128rpx;
      margin-right: 20rpx;
    }

    .scroll-item-right {
      .item-title {
        white-space: pre-wrap;
        font-size: 30rpx;
        width: 390rpx;
      }

      .item-right-bottom {
        display: flex;
        justify-content: space-between;

        .item-time {
          font-size: 12px;
          font-weight: 400;
          color: #777777;
          letter-spacing: -0.02px;
        }

        .item-reserved {
          width: 100rpx;
          height: 40rpx;
          background: #1a9aff;
          border-radius: 4px;
          box-shadow: 0 5rpx 8rpx 0 rgba(58, 121, 255, 0.2);
          color: #fff;
          text-align: center;
          font-size: 26rpx;
          line-height: 40rpx;
        }
      }
    }
  }
</style>
