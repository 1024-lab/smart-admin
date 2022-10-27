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
        <router-link tag="a" target="_blank" :to="{ path: '/help-doc/detail' }">查看全部文档 >></router-link>
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
  import { helpDocApi } from '/@/api/support/help-doc/help-doc-api';
  import ContactModal from './components/contact-modal.vue';
  import FeedbackModal from './components/feedback-modal.vue';
  import { useAppConfigStore } from '/@/store/modules/system/app-config';
  import { feedbackApi } from '/@/api/support/feedback/feedback-api';
import { smartSentry } from '/@/lib/smart-sentry';

  function hideHelpDoc() {
    useAppConfigStore().hideHelpDoc();
  }

  // ------------------ 联系客服 --------------------------
  const contactModal = ref();

  // ------------------ 意见反馈 --------------------------
  let feedbackMessageList = ref([]);
  onMounted(() => {
    // 默认先查询一次
    queryFeedbackList();
    scroll();
  });

  let scrollInterval = null;
  let swiper = 1;

  function scroll() {
    if (scrollInterval != null) {
      return;
    }

    scrollInterval = setInterval(() => {
      if (currentPage >= pages) {
        currentPage = 1;
      }
      if (pages == 0 || feedbackList.length == 0) {
        return;
      }
      let initValue = (currentPage - 1) * 2;
      feedbackMessageList.value[0] = feedbackList[initValue];
      if (feedbackList[initValue + 1]) {
        feedbackMessageList.value[1] = feedbackList[initValue + 1];
      }
      swiper = currentPage - 1;
      currentPage++;
    }, 2000);
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
      pages = Math.ceil(result.data.total / 2);
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
        menuId = _.toNumber(currentRoute.name);
      } catch (e) {
        smartSentry.captureError(e);
      }
      if (menuId > 0) {
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
    border-left:1px solid #ededed;
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
      }
      .help-doc-contact-right {
        margin-top: 10px;
        width: calc(100% - 40px);
        .help-doc-contac-time {
          color: #888;
          font-size: 12px;
        }
      }
    }

    .feedback {
      .feedback-message-list {
        margin: 6px 0px;
        height: 40px;
        position: relative;
        overflow: hidden;
        .feedback-message {
          margin: 2px 2px;
          color: #888;
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
