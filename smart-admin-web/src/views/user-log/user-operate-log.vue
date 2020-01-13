<template>
  <div>
    <Card class="warp-card" dis-hover>
      <Form
        :model="searchform"
        class="tools"
        inline
        ref="searchform"
        v-privilege="'user-operate-log-search'"
      >
        <FormItem prop="startDate">
          <DatePicker
            placeholder="开始日期"
            style="width: 200px"
            type="date"
            v-model="searchform.startDate"
          ></DatePicker>
        </FormItem>
        <FormItem prop="endDate">
          <DatePicker
            placeholder="结束日期"
            style="width: 200px"
            type="date"
            v-model="searchform.endDate"
          ></DatePicker>
        </FormItem>
        <FormItem prop="userName">
          <Input placeholder="请输入用户名" type="text" v-model="searchform.userName"></Input>
        </FormItem>
        <FormItem>
          <ButtonGroup>
            <Button @click="search" icon="ios-search" type="primary">查询</Button>
            <Button @click="reset" icon="md-refresh" type="default">重置</Button>
          </ButtonGroup>
        </FormItem>
      </Form>
      <Table :columns="columns" :data="data" :loading="tableLoading" :total="pageTotal"></Table>
      <Page
        :current="searchform.pageNum"
        :page-size="searchform.pageSize"
        :page-size-opts="[10, 20, 30, 50, 100]"
        :total="pageTotal"
        @on-change="changePage"
        @on-page-size-change="changePageSize"
        show-elevator
        show-sizer
        show-total
        style="margin:24px 0;text-align:right;"
      ></Page>
    </Card>

    <Modal title="日志详情" v-model="detailModal">
      <Form :label-width="80" :model="logDetail">
        <FormItem label="用户名称：">{{logDetail.userName}}</FormItem>
        <FormItem label="用户类型：">{{logDetail.userType?'前台':'后台'}}</FormItem>
        <FormItem label="操作模块：">{{logDetail.module}}</FormItem>
        <FormItem label="操作内容：">{{logDetail.content}}</FormItem>
        <FormItem label="请求方法：">{{logDetail.method}}</FormItem>
        <FormItem label="请求路径：">{{logDetail.url}}</FormItem>
        <FormItem label="请求参数：">{{logDetail.param}}</FormItem>
        <FormItem label="请求结果：">{{logDetail.result?'成功':'失败'}}</FormItem>
        <FormItem label="失败原因：">
          <div style="max-height:120px;overflow-y:scroll;">{{logDetail.failReason}}</div>
        </FormItem>
      </Form>
    </Modal>
  </div>
</template>

<script>
import { userLogApi } from '@/api/user-log';
export default {
  name: 'UserOperateLog',
  components: {},
  props: {},
  data() {
    return {
      tableLoading: false,
      searchform: {
        startDate: '',
        endDate: '',
        pageNum: 1,
        pageSize: 10,
        searchCount: true,
        sort: false,
        userName: ''
      },
      pageTotal: 0,
      // table表头
      columns: [
        {
          title: '用户名称',
          key: 'userName',
          width: 150
        },
        {
          title: '操作模块',
          key: 'module',
          tooltip: true,
          width: 180
        },
        {
          title: '操作内容',
          key: 'content',
          tooltip: true,
          width: 260
        },
        {
          title: '请求方法',
          key: 'method',
          tooltip: true,
          width: 200
        },
        {
          title: '请求路径',
          key: 'url',
          tooltip: true,
          width: 200
        },
        {
          title: '请求参数',
          key: 'param',
          tooltip: true,
          width: 200
        },
        {
          title: '请求结果',
          key: 'result',
          width: 100,
          render: (h, param) => {
            if (param.row.result) {
              return h('div', '成功');
            }
            return h('div', '失败');
          }
        },
        {
          title: '-'
        },
        {
          title: '操作',
          key: 'action',
          width: 160,
          align: 'center',
          className: 'action-hide',
          fixed: 'right',
          render: (h, param) => {
            return this.$tableAction(h, [
              {
                title: '详情',
                directives: [
                  {
                    name: 'privilege',
                    value: 'user-operate-log-detail'
                  }
                ],
                action: () => {
                  this.showLogDetail(param.row.id);
                }
              },
              {
                title: '删除',
                directives: [
                  {
                    name: 'privilege',
                    value: 'user-operate-log-delete'
                  }
                ],
                action: () => {
                  this.$Modal.confirm({
                    title: '友情提醒',
                    content: '确定要删除吗？',
                    onOk: () => {
                      this.deleteLog(param.row.id);
                    }
                  });
                }
              }
            ]);
          }
        }
      ],
      // table数据
      data: [],
      detailModal: false,
      logDetail: {}
    };
  },
  computed: {},
  watch: {},
  filters: {},
  mounted() {
    this.getUserOperateLogPage();
  },
  methods: {
    // 查询用户操作日志
    async getUserOperateLogPage() {
      this.tableLoading = true;
      try {
        let result = await userLogApi.getUserOperateLogPage(this.searchform);
        this.tableLoading = false;
        this.data = result.data.list;
        this.pageTotal = result.data.total;
      } catch (e) {
        //TODO zhuoda sentry
        console.error(e);
        this.tableLoading = false;
      }
    },
    changePage(pageNum) {
      this.searchform.pageNum = pageNum;
      this.getUserOperateLogPage();
    },
    changePageSize(pageSize) {
      this.searchform.pageNum = 1;
      this.searchform.pageSize = pageSize;
      this.getUserOperateLogPage();
    },
    // 搜索
    search() {
      this.searchform.pageNum = 1;
      this.getUserOperateLogPage();
    },
    reset() {
      this.$refs.searchform.resetFields();
      this.search();
    },
    // 获取并查看详情
    async showLogDetail(id) {
      this.$Spin.show();
      let res = await userLogApi.detailUserOperateLog(id);
      this.$Spin.hide();
      this.logDetail = res.data;
      this.detailModal = true;
    },
    // 删除日志
    async deleteLog(id) {
      this.$Spin.show();
      await userLogApi.deleteUserOperateLog(id);
      this.$Spin.hide();
      this.$Message.success('删除成功');
      this.getUserOperateLogPage();
    }
  }
};
</script>

<style scoped>
.ivu-form-item {
  word-break: break-word;
}
</style>
