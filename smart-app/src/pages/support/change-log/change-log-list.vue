<template>
  <view class="container">
    <mescroll-body @init="mescrollInit" :down="{ auto: false }" @down="onDown" @up="onUp" :up="{ toTop: { src: '' } }">
      <!--搜索框-->
      <uni-nav-bar :border="false" fixed :leftWidth="0" rightWidth="70px">
        <view class="input">
          <uni-easyinput
            :clearable="true"
            trim="all"
            v-model="queryForm.keyword"
            placeholder="搜索： 更新内容 等"
            @confirm="search"
            @clear="search"
          />
        </view>
        <template #right>
          <view class="nav-right" @click="search">
            <uni-icons type="search" size="30"></uni-icons>
            <view class="nav-right-name"> 搜索 </view>
          </view>
        </template>
      </uni-nav-bar>

      <!-- 列表 -->
      <view class="list-container">
        <view class="list-item" @click="gotoDetail(item.changeLogId)" v-for="item in listData" :key="item.changeLogId">
          <view class="list-item-row">
            <view class="list-item-content bolder"
              >{{ item.version }}版本{{ $smartEnumPlugin.getDescByValue('CHANGE_LOG_TYPE_ENUM', item.type) }}</view
            >
            <uni-tag
              :text="$smartEnumPlugin.getDescByValue('CHANGE_LOG_TYPE_ENUM', item.type)"
              :type="$smartEnumPlugin.getObjectByValue('CHANGE_LOG_TYPE_ENUM', item.type).type"
            />
          </view>
          <view class="list-item-row">
            <view class="list-item-label">发布日期：{{ item.publicDate }}</view>
          </view>
        </view>
      </view>
    </mescroll-body>
  </view>
</template>

<script setup>
  import { reactive, ref } from 'vue';
  import { changeLogApi } from '@/api/support/change-log-api';
  import { onPageScroll, onReachBottom, onShow } from '@dcloudio/uni-app';
  import useMescroll from '@/uni_modules/uni-mescroll/hooks/useMescroll';
  import { smartSentry } from '@/lib/smart-sentry';
  import _ from 'lodash';

  // --------------------------- 查询 ---------------------------------

  const defaultForm = {
    keyword: '', //标题、内容
    pageNum: 1,
    pageSize: 10,
  };

  // 查询表单
  const queryForm = reactive({ ...defaultForm });
  // 列表数据
  const listData = ref([]);

  function buildQueryParam(pageNum) {
    queryForm.pageNum = pageNum;
    return Object.assign({}, queryForm, { pageNum });
  }

  async function query(mescroll, isDownFlag, param) {
    try {
      let res = await changeLogApi.queryPage(param);
      if (!isDownFlag) {
        listData.value = listData.value.concat(res.data.list);
      } else {
        listData.value = res.data.list;
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

  onShow(() => {
    search();
  });

  // --------------------------- 详情 ---------------------------------

  function gotoDetail(id) {
    uni.navigateTo({ url: '/pages/support/change-log/change-log-detail?changeLogId=' + id });
  }
</script>

<style lang="scss" scoped>
  .container {
    background-color: #f4f4f4;
  }
  .input {
    width: 100%;
    height: 60rpx;
    background: #f7f8f9;
    border-radius: 4px;
    margin: 8rpx 0;
    display: flex;
    align-items: center;
  }

  .nav-right {
    width: 140rpx;
    display: flex;
    height: 88rpx;
    flex-direction: row;
    line-height: 88rpx;
    .nav-right-name {
      margin-left: 5px;
      line-height: 88rpx;
      font-size: 30rpx;
    }
  }

  .list-container {
    padding: 10rpx 20rpx;
    margin-top: 10rpx;

    .list-item {
      background: #ffffff;
      box-shadow: 0px 3px 4px 0px rgba(24, 144, 255, 0.06);
      margin-bottom: 20rpx;
      padding: 30rpx 40rpx;

      .list-item-row {
        display: flex;
        flex-direction: row;
        margin-bottom: 16rpx;
        justify-content: space-between;

        .list-item-label {
          font-size: 30rpx;
          font-weight: 400;
          text-align: left;
          color: $uni-text-color-grey;
        }
        .bolder {
          font-weight: 500 !important;
          font-size: 34rpx !important;
        }
        .list-item-content {
          font-size: 30rpx;
          font-weight: 500;
          text-align: left;
        }
        .list-item-content-container {
          font-size: 30rpx;
          font-weight: 500;
          text-align: left;
          color: #323333;
          height: 40px;
        }
        .list-item-phone {
          color: $uni-color-primary;
          margin-left: auto;
        }
      }
    }
  }
</style>
