<!--
  *  帮助文档 
  *
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-09-06 20:40:16 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
-->
<template>
  <div class="help-doc-wrapper">
    <!-----头部---->
    <div class="help-doc-header">
      <strong>帮助文档</strong>
      <strong class="help-doc-close" @click="hideHelpDoc"><close-outlined /></strong>
    </div>

    <!-----联系客服区域---->
    <div class="help-doc-contact" @click="contactModal.show">
      <div class="help-doc-contact-left">
        <phone-outlined style="font-size: 23px; line-height: 50px; margin-top: 5px" />
      </div>
      <div class="help-doc-contact-right">
        <a>联系客服</a>
        <div class="help-doc-contac-time">9:00-17:00 5x7小时</div>
      </div>
    </div>

    <a-divider />

    <!-----意见反馈---->
    <div class="feedback">
      <div>反馈让您不满意的点，我们争取做到更好<smile-outlined style="margin-left: 5px" /></div>
      <div class="feedback-message-list">
        <div v-for="item in feedbackMessageList" :key="item.feedbackId" class="feedback-message">{{ item.feedbackContent }}</div>
      </div>
      <a @click="feedbackModal.show">我也要反馈</a>
    </div>

    <a-divider />

    <!-----文档列表---->
    <div class="help-doc-list">
      <div class="help-doc-item-all">
        <router-link tag="a" target="_blank" :to="{ path: '/help-doc/detail' }">系统帮助文档 >></router-link>
      </div>
      <div class="help-doc-item" v-for="item in helpDocList" :key="item.helpDocId">
        <router-link tag="a" target="_blank" :to="{ path: '/help-doc/detail', query: { helpDocId: item.helpDocId } }">{{ item.title }}</router-link>
      </div>
    </div>

    <!-----联系客服---->
    <ContactModal ref="contactModal" />
    <!----- 提交意见反馈 ---->
    <FeedbackModal ref="feedbackModal" />
  </div>
</template>
<script setup>
  import { onMounted, ref, watch, reactive } from 'vue';
  import { useRoute } from 'vue-router';
  import _ from 'lodash';
  import { helpDocApi } from '/src/api/support/help-doc-api';
  import ContactModal from './components/contact-modal.vue';
  import FeedbackModal from './components/feedback-modal.vue';
  import { useAppConfigStore } from '/@/store/modules/system/app-config';
  import { feedbackApi } from '/src/api/support/feedback-api';
  import { HOME_PAGE_NAME } from '/@/constants/system/home-const';
  import { smartSentry } from '/@/lib/smart-sentry';

  function hideHelpDoc() {
    useAppConfigStore().hideHelpDoc();
  }

  // ------------------ 联系客服 --------------------------
  const contactModal = ref();

  // ------------------ 意见反馈 --------------------------
  let feedbackMessageList = ref([]);
  onMounted(() => {
    // 首先查询多一些意见反馈
    queryFeedbackList();
    // 更换显示
    scheduleShowFeedback();
  });

  let scheduleShowInterval = null;
  let scheduleShowIndex = 0;

  function scheduleShowFeedback() {
    if (scheduleShowInterval != null) {
      return;
    }

    scheduleShowInterval = setInterval(() => {
      if (feedbackList.length === 0) {
        return;
      }

      // 显示两条意见反馈
      for (let i = 0; i < 2; i++) {
        if (scheduleShowIndex >= feedbackList.length) {
          scheduleShowIndex = 0;
        }
        feedbackMessageList.value[i] = feedbackList[scheduleShowIndex];
        scheduleShowIndex++;
      }
    }, 3000);
  }

  // 总页数
  let pages = 1;
  let currentPage = 1;
  let feedbackList = [];

  // 查询意见反馈列表
  async function queryFeedbackList() {
    try {
      let param = {
        pageNum: 1,
        pageSize: 20,
      };
      let result = await feedbackApi.queryFeedback(param);
      feedbackList = result.data.list;
      pages = Math.ceil(feedbackList.length / 2);
    } catch (e) {
      smartSentry.captureError(e);
    }
  }

  const feedbackModal = ref();
  // ----------------- 帮助文档列表 -------------------
  let currentRoute = useRoute();
  let helpDocList = ref([]);
  // 获取关联的文档集合
  async function queryHelpDocList(menuId) {
    let res = await helpDocApi.queryHelpDocByRelationId(menuId);
    helpDocList.value = res.data;
  }

  watch(
    currentRoute,
    () => {
      //SmartAdmin中 router的name 就是 后端存储menu的id
      let menuId = -1;
      try {
        if (currentRoute.name === HOME_PAGE_NAME) {
          menuId = 0;
        } else {
          menuId = _.toNumber(currentRoute.name);
        }
      } catch (e) {
        smartSentry.captureError(e);
      }
      if (menuId > -1) {
        queryHelpDocList(menuId);
      }
    },
    {
      immediate: true,
    }
  );
</script>

<style scoped lang="less">
  .help-doc-wrapper {
    border-left: 1px solid #ededed;
    height: 100vh;
    padding: 0 10px;

    .help-doc-header {
      line-height: @header-user-height;
      display: flex;
      justify-content: space-between;
      height: @header-user-height;
      border-bottom: 1px solid #f6f6f6;

      .help-doc-close {
        cursor: pointer;
      }
    }

    .help-doc-contact {
      height: 50px;
      display: flex;
      cursor: pointer;
      margin-top: 5px;
      justify-content: space-between;
      .help-doc-contact-left {
        width: 30px;
        margin-top: 10px;
      }
      .help-doc-contact-right {
        margin-top: 10px;
        width: calc(100% - 40px);
        .help-doc-contac-time {
          color: #888;
          font-size: 12px;
          margin-top: 10px;
        }
      }
    }

    .feedback {
      .feedback-message-list {
        margin: 12px 0px;
        height: 70px;
        position: relative;
        overflow: hidden;
        .feedback-message {
          margin: 10px 2px;
          color: #a9a9a9;
          font-size: 12px;
        }
      }
    }

    .help-doc-list {
      .help-doc-item-all {
        margin-top: 10px;
        color: @primary-color;
      }
      .help-doc-item {
        margin-top: 20px;
        a {
          color: #444;
        }
        a:hover {
          color: @primary-color;
          text-decoration: underline;
        }
      }
    }
  }
</style>
