<template>
  <div>
    <Card>
      <p slot="title">{{notice.title}}</p>
      <p>{{content}}</p>
      <p>{{notice.updateTime}}</p>
    </Card>
  </div>
</template>
<script>
import { noticeApi } from '@/api/notice';
export default {
  name: 'NoticeList',
  components: {},
  props: {},
  data() {
    //消息信息
    return {
      notice: {},
      content: ''
    };
  },
  mounted() {
    this.notice = this.$route.params.notice;
    this.getNoticeDetail(this.notice.id);
  },
  methods: {
    // 获取通知详情
    async getNoticeDetail(id) {
      try {
        let result = await noticeApi.getNoticeDetail(id);
        this.content = result.data.content;
      } catch (e) {
        //TODO zhuoda sentry
        console.error(e);
      }
    }
  }
};
</script>
<style lang="less" scoped>
.detail {
  margin-bottom: 20px;
}
.time {
  text-align: right;
  color: #999;
}
</style>
