<template>
  <view>
    <mescroll-body @init="mescrollInit" :down="{ auto: false }" @down="onDown" @up="onUp">
      <!--搜索框-->
      <uni-nav-bar :border="false" fixed :leftWidth="0" rightWidth="70px">
        <view class="input">
          <uni-easyinput
            prefixIcon="search"
            :clearable="true"
            trim="all"
            v-model="queryForm.searchWord"
            placeholder="搜索：商品名称"
            @confirm="search"
            @clear="search"
          />
        </view>
        <template #right>
          <view class="nav-right" @click="showQueryFormPopUp">
            <uni-icons type="settings" size="30"></uni-icons>
            <view class="nav-right-name"> 筛选 </view>
          </view>
        </template>
      </uni-nav-bar>

      <!--筛选条件提示-->
      <uni-notice-bar
        @close="onCloseQueryFormTips"
        v-if="showQueryFormTipsFlag"
        class="query-bar"
        background-color="#007aff"
        color="white"
        show-close
        single
        :text="queryFormTips"
      />

      <!-- 筛选条件表单弹窗 -->
      <QueryFormPopUp ref="queryFormPopUpRef" @close="onCloseQueryFormPopUp" />

      <!-- 列表 -->
      <NoticeList :list="listData" :margin-top="queryFormTipsMarginTop" />
    </mescroll-body>
  </view>
</template>

<script setup>
  import { reactive, ref } from 'vue';
  import QueryFormPopUp from './components/goods-query-form-popup.vue';
  import { goodsApi } from '@/api/business/goods/goods-api';
  import { onPageScroll, onReachBottom } from '@dcloudio/uni-app';
  import useMescroll from '@/uni_modules/uni-mescroll/hooks/useMescroll';
  import { smartSentry } from '@/lib/smart-sentry';
  import NoticeList from './components/goods-list.vue';
  import _ from 'lodash';

  // --------------------------- 筛选条件弹窗 ---------------------------------
  const queryFormPopUpRef = ref();

  /**
   * 显示 筛选弹窗
   */
  function showQueryFormPopUp() {
    queryFormPopUpRef.value.show();
  }

  /**
   * 监听 筛选弹窗 关闭
   */
  function onCloseQueryFormPopUp(param) {
    if (param === null) {
      return;
    }
    Object.assign(queryForm, param);
    showOrHideQueryFormTips();
    query(getMescroll(), true, buildQueryParam(1));
    uni.pageScrollTo({
      scrollTop: 0,
    });
  }

  // --------------------------- 筛选条件tips ---------------------------------
  const queryFormTips = ref(null);
  const showQueryFormTipsFlag = ref(false);
  const queryFormTipsMarginTop = ref('0px');

  /**
   * 查询提示
   */
  function buildQueryFormTips() {
    let tips = null;
    if (queryForm.searchWord) {
      tips = '搜索：' + queryForm.searchWord;
    }
    if (queryForm.goodsStatusName) {
      tips = tips ? tips + '，' : '';
      tips = tips + '商品状态：' + queryForm.goodsStatusName;
    }
    if (queryForm.placeName) {
      tips = tips ? tips + '，' : '';
      tips = tips + '产地：' + queryForm.placeName;
    }
    return tips;
  }

  /**
   * 显示或者隐藏tips
   */
  function showOrHideQueryFormTips() {
    let tips = buildQueryFormTips();
    queryFormTipsMarginTop.value = _.isEmpty(tips) ? '0px' : '50rpx';
    showQueryFormTipsFlag.value = !_.isEmpty(tips);
    queryFormTips.value = tips;
  }

  /**
   * 关闭筛选条件 tips，清空搜索条件
   */
  function onCloseQueryFormTips() {
    Object.assign(queryForm, defaultForm);
    queryFormTipsMarginTop.value = '0px';
    showQueryFormTipsFlag.value = false;
    queryFormTips.value = '';
    search();
  }

  // --------------------------- 查询 ---------------------------------

  const defaultForm = {
    searchWord: '',
    // 商品状态
    goodsStatus: undefined,
    goodsStatusName: undefined,
    // 产地
    place: undefined,
    placeName: undefined,
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
      let res = await goodsApi.queryGoodsList(param);
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
    showOrHideQueryFormTips();
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
  .input {
    width: 100%;
    height: 72rpx;
    background: #f7f8f9;
    border-radius: 4px;
    margin: 8rpx 0;
    display: flex;
    align-items: center;
  }

  .nav-right {
    width: 140px;
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

  .query-bar {
    position: fixed;
  }
</style>
