<template>
  <div>
    <Card class="warp-card" dis-hover>
      <Form
        :model="searchform"
        class="tools"
        inline
        ref="searchform"
        v-privilege="'user-login-log-search'"
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
      <Table :columns="columns" :data="data" :loading="loading"></Table>
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
  </div>
</template>

<script>
import { userLogApi } from '@/api/user-log';
export default {
  name: 'UserLoginLog',
  components: {},
  props: {},
  data() {
    return {
      loading: false,
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
          key: 'userName'
        },
        {
          title: '浏览器',
          key: 'remoteBrowser'
        },
        {
          title: '操作系统',
          key: 'remoteOs'
        },
        {
          title: 'Ip',
          key: 'remoteIp'
        },
        {
          title: '用户端口',
          key: 'remotePort'
        },
        {
          title: '创建时间',
          key: 'createTime'
        },
        {
          title: '操作',
          key: 'action',
          align: 'center',
          className: 'action-hide',
          render: (h, param) => {
            return this.$tableAction(h, [
              {
                title: '删除',
                directives: [
                  {
                    name: 'privilege',
                    value: 'user-login-log-delete'
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
      data: []
    };
  },
  computed: {},
  watch: {},
  filters: {},
  created() {},
  mounted() {
    this.getUserLoginLogPage();
  },
  methods: {
    // 查询用户登录日志
    async getUserLoginLogPage() {
      try {
        this.loading = true;
        let result = await userLogApi.getUserLoginLogPage(this.searchform);
        this.loading = false;
        this.data = result.data.list;
        this.pageTotal = result.data.total;
      } catch (e) {
        //TODO zhuoda sentry
        console.error(e);
        this.loading = false;
      }
    },
    // 翻页
    changePage(pageNum) {
      this.searchform.pageNum = pageNum;
      this.getUserLoginLogPage();
    },
    // 改变一页展示数
    changePageSize(pageSize) {
      this.searchform.pageNum = 1;
      this.searchform.pageSize = pageSize;
      this.getUserLoginLogPage();
    },
    // 搜索
    search() {
      this.searchform.pageNum = 1;
      this.getUserLoginLogPage();
    },
    // 重置
    reset() {
      this.$refs.searchform.resetFields();
      this.search();
    },
    // 删除日志
    async deleteLog(id) {
      this.$Spin.show();
      await userLogApi.deleteUserLoginLog(id);
      this.$Spin.hide();
      this.$Message.success('删除成功');
      this.getUserLoginLogPage();
    }
  }
};
</script>
