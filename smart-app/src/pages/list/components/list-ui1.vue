<template>
  <view class="list-view">
    <view class="item" v-for="item in data">
      <view class="item-top">
        <view class="item-top-info">
          <view class="name"> {{ item.name }} </view>
          <view class="mobile"> {{ item.phone }} </view>
        </view>
        <view class="item-top-phone"> <image src="/static/images/list/phone.png" mode=""></image> 联系Ta </view>
      </view>
      <view class="item-title"> 最后跟进内容：{{ item.content }} </view>
      <view class="item-bottom"> 最后跟进时间：{{ item.time }} </view>
    </view>
  </view>
</template>

<script setup>
  import { ref } from 'vue';
  const data = ref([
    {
      name: '卓大',
      phone: '18610241024',
      content: '上午跟客户谈了下合作意向，对方比较认可',
      time: '2023-01-03 12:14:10',
    },
    {
      name: '胡可',
      phone: '18610241024',
      content: '上午跟客户谈了下合作意向，对方比较认可',
      time: '2023-01-03 12:14:10',
    },
    {
      name: '开云',
      phone: '18610241024',
      content: '上午跟客户谈了下合作意向，对方比较认可',
      time: '2023-01-03 12:14:10',
    },
    {
      name: '清野',
      phone: '18610241024',
      content: '上午跟客户谈了下合作意向，对方比较认可',
      time: '2023-01-03 12:14:10',
    },
    {
      name: '善逸',
      phone: '18610241024',
      content: '上午跟客户谈了下合作意向，对方比较认可',
      time: '2023-01-03 12:14:10',
    },
    {
      name: '飞叶',
      phone: '18610241024',
      content: '上午跟客户谈了下合作意向，对方比较认可',
      time: '2023-01-03 12:14:10',
    },
    {
      name: '启阳',
      phone: '18610241024',
      content: '上午跟客户谈了下合作意向，对方比较认可',
      time: '2023-01-03 12:14:10',
    },
    {
      name: '卓大',
      phone: '18610241024',
      content: '上午跟客户谈了下合作意向，对方比较认可',
      time: '2023-01-03 12:14:10',
    },
    {
      name: '卓大',
      phone: '18610241024',
      content: '上午跟客户谈了下合作意向，对方比较认可',
      time: '2023-01-03 12:14:10',
    },
    {
      name: '卓大',
      phone: '18610241024',
      content: '上午跟客户谈了下合作意向，对方比较认可',
      time: '2023-01-03 12:14:10',
    },
    {
      name: '卓大',
      phone: '18610241024',
      content: '上午跟客户谈了下合作意向，对方比较认可',
      time: '2023-01-03 12:14:10',
    },
    {
      name: '卓大',
      phone: '18610241024',
      content: '上午跟客户谈了下合作意向，对方比较认可',
      time: '2023-01-03 12:14:10',
    },
  ]);
</script>

<style lang="scss" scoped>
  .list-view {
    margin: 20rpx 24rpx;
  }
  .item {
    box-sizing: border-box;
    width: 700rpx;
    background: #ffffff;
    border-radius: 12rpx;
    box-shadow: 0px 3px 4px 0px rgba(24, 144, 255, 0.06);
    padding: 24rpx 30rpx;
    border-bottom: 1rpx solid #ededed;
    margin-bottom: 24rpx;

    .item-top {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20rpx;
      .item-top-info {
        display: flex;
        align-items: center;
        .name {
          font-size: 34rpx;
          font-weight: 500;
          color: #444444;
          margin-right: 10rpx;
        }
        .mobile {
          font-size: 32rpx;
          font-weight: bold;
          text-align: left;
          color: #444444;
        }
      }
      .item-top-phone {
        color: #1a9aff;
        font-size: 28rpx;
        display: flex;
        align-items: center;
        image {
          width: 40rpx;
          height: 40rpx;
          margin-left: 4rpx;
        }
      }
    }

    .item-title {
      font-size: 28rpx;
      color: #777777;
      margin-bottom: 24rpx;
    }

    .item-bottom {
      font-size: 28rpx;
      color: #777777;
    }
  }
</style>
