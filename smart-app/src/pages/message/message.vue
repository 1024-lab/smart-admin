<template>
  <view class="container">
    <mescroll-body @init="mescrollInit" :down="{ auto: false }" @down="onDown" @up="onUp">
      <mescroll-empty v-if="messageListData.length === 0"></mescroll-empty>
      <view class="message" v-for="(item, index) in messageListData">
        <view class="message-header">
          <view class="header-left">
            <image src="/src/static/images/message/message.png" mode=""></image>
            <view>
              {{ $smartEnumPlugin.getDescByValue('MESSAGE_TYPE_ENUM', item.messageType) }}
            </view>
          </view>
          <view class="header-time"> {{item.createTime}} </view>
        </view>
        <view class="content">
          <view class="message-title">
            <uni-icons v-if="!item.readFlag" color="red" class="smart-margin-right10" type="info-filled" :size="14"></uni-icons>
            {{ item.title }}
          </view>
          <view class="message-body"> {{item.content}} </view>
        </view>
      </view>
    </mescroll-body>
  </view>
</template>

<script setup>
  import { reactive, ref } from 'vue';
  import { onPageScroll, onReachBottom } from '@dcloudio/uni-app';
  import useMescroll from '@/uni_modules/uni-mescroll/hooks/useMescroll';
  import { smartSentry } from '@/lib/smart-sentry';
  import { messageApi } from '@/api/support/message-api';

  // --------------------------- 查询 ---------------------------------

  const defaultForm = {
    searchWord: '',
    messageType: null,
    dataId: null,
    readFlag: null,
    endDate: null,
    startDate: null,
    pageNum: 1,
    pageSize: 10,
    searchCount: true,
    receiverType: null,
    receiverId: null,
  };

  // 查询表单
  const queryForm = reactive({ ...defaultForm });
  // 通知列表数据
  const messageListData = ref([]);

  function buildQueryParam(pageNum) {
    queryForm.pageNum = pageNum;
    return Object.assign({}, queryForm, { pageNum });
  }

  async function query(mescroll, isDownFlag, param) {
    try {
      let res = await messageApi.queryMessage(param);
      res.data.list.map(e => e.content = e.content.substr(0,50));
      if (!isDownFlag) {
        messageListData.value = messageListData.value.concat(res.data.list);
      } else {
        messageListData.value = res.data.list;
      }
      mescroll.endSuccess(res.data.list.length, res.data.pages > res.data.pageNum);
    } catch (e) {
      smartSentry.captureError(e);
      //联网失败, 结束加载
      mescroll.endErr();
    }
  }

  const { mescrollInit, getMescroll } = useMescroll(onPageScroll, onReachBottom);

  /**
   * 搜索
   */
  function search() {
    query(getMescroll(), true, buildQueryParam(1));
    uni.pageScrollTo({
      scrollTop: 0,
    });
  }

  /**
   * 下拉刷新
   */
  function onDown(mescroll) {
    queryForm.pageNum = 1;
    query(mescroll, true, buildQueryParam(1));
  }

  /**
   * 上拉加载更多
   */
  function onUp(mescroll) {
    query(mescroll, false, buildQueryParam(mescroll.num));
  }
</script>

<style lang="scss" scoped>
  .message {
    width: 700rpx;
    background: #ffffff;
    border-radius: 12rpx;
    box-shadow: 0px 3px 4px 0px rgba(24, 144, 255, 0.06);
    margin: 24rpx auto 0;
    box-sizing: border-box;
    padding: 30rpx 30rpx 24rpx;
    .message-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 24rpx;
      .header-left {
        align-items: center;
        display: flex;
        image {
          width: 48rpx;
          height: 48rpx;
          margin-right: 14rpx;
        }
        font-size: 34rpx;
        color: #000000;
      }
      .header-time {
        font-size: 28rpx;
        font-weight: 400;
        color: #999999;
      }
    }
    .content {
      box-sizing: border-box;
      padding: 24rpx;
      background-color: #f7f8f9;
      width: 100%;
      border-radius: 8rpx;
      .message-title {
        color: #323333;
        font-size: 34rpx;
        font-weight: bold;
        margin-bottom: 8rpx;
        display: flex;
        flex-direction: row;
        justify-content: flex-start;
        align-items: center;
        white-space: nowrap; /* 确保文本不会换行 */
        overflow: hidden; /* 隐藏溢出的内容 */
        text-overflow: ellipsis; /* 在文本溢出处显示省略号 */
      }
      .message-body {
        font-size: 28rpx;
        max-height: 3.2rem;
        color: #777777;
      }
    }
  }

  page {
    background-color: #f5f5f5;
  }
</style>
