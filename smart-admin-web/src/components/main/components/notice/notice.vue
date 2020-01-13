<template>
  <div class="notice">
    <Tooltip content="消息" placement="bottom">
      <Badge :count="noticeNumber" class-name="demo-badge-alone">
        <Icon @click="openNotice" class="demo-badge" size="18" type="icon iconfont iconnews"></Icon>
      </Badge>
    </Tooltip>
    <div :class="modalOpen?'show':''" class="notice-main">
      <div :class="modalOpen?'show':''" @click="modalOpen=false" class="notice-bg"></div>
      <div :class="modalOpen?'show':''" class="notice-box">
        <div class="header">
          <div class="item">消息通知({{noticeNumber}})</div>
          <Icon @click="modalClose" class="close" size="24" type="ios-close" />
        </div>

        <div class="notice-list" ref="noticeList">
          <div :key="item.id" @click.stop="getDetail(item)" class="item" v-for="item in noticeList">
            <img alt src="@/assets/images/message.png" />
            <div class="info">
              <p class="title">{{item.title}}</p>
              <p class="time">{{item.updateTime}}</p>
            </div>
          </div>
          <InfiniteLoading @infinite="scroll" ref="infiniteLoading">
            <span slot="no-more">暂时没有新消息啦！</span>
          </InfiniteLoading>
        </div>
      </div>
      <div :class="detailModalOpen?'show':''" class="notice-detail">
        <div class="title">
          <div class="item">{{noticeDetail.title}}</div>
          <Icon @click="detailModalOpen=false" class="close" type="md-close" />
        </div>
        <div class="detail">{{noticeDetail.content}}</div>
        <p class="time">{{noticeDetail.updateTime}}</p>
      </div>
    </div>
  </div>
</template>

<script>
import { noticeApi } from '@/api/notice';
import { socketBaseUrl } from '@/lib/http';
import InfiniteLoading from 'vue-infinite-loading';
export default {
  name: 'Notice',
  components: {
    InfiniteLoading
  },
  data() {
    return {
      loading: false,
      // Websocket心跳间隔
      heartTimer: null,
      // 是否展示消息列表
      modalOpen: false,
      socketBaseUrl,
      // websocket 实例
      socket: null,
      // 是否展示消息详情
      detailModalOpen: false,
      searchData: {
        pageSize: 10,
        pageNum: 1
      },
      noticeDetail: {
        title: '',
        content: '',
        updateTime: ''
      }
    };
  },
  mounted() {
    this.initWebSocket();
  },
  computed: {
    // 消息集合
    noticeList() {
      return this.$store.state.notice.noticeList;
    },
    // 消息数量
    noticeNumber() {
      return this.$store.state.notice.noticeNumber;
    },
    userInfo() {
      return this.$store.state.user.userLoginInfo;
    }
  },
  methods: {
    initWebSocket() {
      this.socket = new WebSocket(
        `${this.socketBaseUrl}webSocket/${this.userInfo.id}`
      );
      this.socket.onopen = this.websocketOnopen;
      this.socket.onerror = this.websocketOnerror;
      this.socket.onmessage = this.websocketOnmessage;
      this.socket.onclose = this.websocketClose;
    },
    // socket连接成功
    websocketOnopen() {
      this.heartTimer = setInterval(() => {
        this.heartbeat();
      }, 5000);
      this.$once('hook:beforeDestroy', () => {
        clearInterval(this.heartTimer);
        this.$store.commit('restNotice');
      });
    },
    // socket连接出错
    websocketOnerror() {
      console.log('socket出错啦');
    },
    // socket收到消息
    websocketOnmessage(e) {
      this.$store.commit('updateNoticeNum', Number(e.data));
    },
    // socket关闭
    websocketClose() {
      console.log('socket掉线啦');
    },
    // 心跳包
    heartbeat() {
      let data = {
        jsonStr: '',
        messageType: 3
      };
      let subStr = {
        employeeId: this.userInfo.id
      };
      data.jsonStr = JSON.stringify(subStr);
      this.socket.send(JSON.stringify(data));
    },
    async getDetail(item) {
      try {
        let result = await noticeApi.getNoticeDetail(item.id);
        this.noticeDetail = result.data;
      } catch (error) {
        //TODO zhuoda sentry
        console.error(e);
      }
      if (this.detailModalOpen) {
        this.detailModalOpen = false;
        setTimeout(() => {
          this.detailModalOpen = true;
        }, 100);
      } else {
        this.detailModalOpen = true;
      }

      try {
        let result = await noticeApi.addNoticeRecord(item.id);
      } catch (error) {
        //TODO zhuoda sentry
        console.error(e);
      }
    },
    scroll($state) {
      this.getNoticeList($state);
    },
    async getNoticeList($state) {
      const result = await noticeApi.getNoticeUnreadList({
        ...this.searchData
      });
      // 请求通知成功，全局更改消息数量
      this.$store.commit('updateNoticeNum', result.data.total);
      $state && $state.loaded();
      if (!result.data.list.length) {
        $state && $state.complete();
      } else {
        this.searchData.pageNum++;
      }
      this.$store.commit('updateNotice', result.data.list);
    },
    openNotice() {
      this.searchData.pageNum = 1;
      this.$store.commit('restNotice');
      this.getNoticeList();
      this.modalOpen = true;
    },
    modalClose() {
      this.getNoticeList();
      this.detailModalOpen = false;
      this.modalOpen = false;
    }
  }
};
</script>

<style lang='less'>
.notice {
  &:hover {
    background: #f7f7f8;
    cursor: pointer;
  }
}
.demo-badge {
  margin: 0 10px;
  border-radius: 6px;
  display: inline-block;
  color: #909ca4;
  font-size: 18px;
  cursor: pointer;
}
.demo-badge-alone {
  background: #f47f92 !important;
  top: 5px !important;
  right: 15px !important;
}
.notice-main {
  position: fixed;
  z-index: -100;
  opacity: 0;
  width: 100vw;
  height: 100vh;
  transition: all 0.5s;
  top: 0;
  left: 0;
  &.show {
    z-index: 993;
    opacity: 1;
  }
}
.notice-bg {
  position: fixed;
  z-index: -100;
  opacity: 0;
  width: 100vw;
  height: 100vh;
  transition: all 0.5s;
  top: 0;
  left: 0;
  background: rgba(0, 0, 0, 0.2);
  &.show {
    z-index: 994;
    opacity: 1;
  }
}
.notice-box {
  position: fixed;
  right: 0;
  bottom: -650px;
  background: #fff;
  z-index: 997;
  width: 336px;
  height: 650px;
  transition: all 0.5s;
  box-shadow: 1px 1px 10px rgba(0, 0, 0, 0.1) !important;
  border: none !important;
  overflow: auto;
  &.show {
    bottom: 0;
  }
  .header {
    position: relative;
    height: 50px;
    border-bottom: 1px solid #eee;
    .close {
      cursor: pointer;
      position: absolute;
      right: 25px;
      top: 15px;
    }
    .item {
      margin: 0 20px;
      line-height: 50px;
      text-align: center;
      font-size: 14px;
      color: #333;
      //   border-bottom: 1px #e8e8e8 solid;
    }
  }
  .notice-list {
    height: calc(~'100% - 50px') !important;
    padding-top: 10px;
    overflow: auto;
    .no-data {
      text-align: center;
    }
    .item {
      position: relative;
      height: 70px;
      display: flex;
      padding: 0 20px;
      align-content: center;
      align-items: center;
      cursor: pointer;
      border-bottom: 1px #e8e8e8 solid;
      &:hover {
        background: #f4f4f4;
        &::before {
          position: absolute;
          left: 0;
          content: '';
          width: 2px;
          height: 100%;
          background: #5cb85c;
        }
      }
      img {
        width: 40px;
        height: 40px;
      }
      .info {
        height: 100%;
        display: flex;
        flex-direction: column;
        flex: 1;
        padding: 0 10px;
        align-content: flex-start;
        justify-content: center;
        .title {
          line-height: 1.8;
          font-size: 14px;
          font-weight: bold;
        }
        .time {
          line-height: 1.5;
          color: #999;
        }
      }
    }
  }
}
.notice-detail {
  position: fixed;
  z-index: 995;
  border-right: 1px #e8e8e8 solid;
  bottom: 0;
  right: -336px;
  width: 400px;
  height: 650px;
  background: #fff;
  opacity: 0;
  transition: all 0.5s;
  &.show {
    right: 336px;
    opacity: 1;
  }
  .title {
    height: 50px;
    line-height: 50px;
    text-align: center;
    font-size: 14px;
    border-bottom: 1px solid #eee;
    .close {
      cursor: pointer;
      position: absolute;
      right: 25px;
      top: 12px;
    }
    .item {
      margin: 0 20px;
      line-height: 50px;
      text-align: center;
      font-size: 14px;
      color: #009688;
      //   border-bottom: 1px #e8e8e8 solid;
    }
  }
  .detail {
    font-size: 14px;
    padding: 20px;
    line-height: 20px;
  }
  .time {
    padding: 0 20px;
    color: #999;
    text-align: right;
  }
}
</style>
