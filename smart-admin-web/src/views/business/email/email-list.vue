<template>
  <div>
    <Card class="warp-card" dis-hover>
      <Form :model="searchForm" class="tools" inline ref="searchForm">
        <FormItem prop="startDate">
          <DatePicker placeholder="请选择开始日期" type="datetime" v-model="searchForm.startDate"></DatePicker>
        </FormItem>
        <FormItem prop="endDate">
          <DatePicker placeholder="请选择截止日期" type="datetime" v-model="searchForm.endDate"></DatePicker>
        </FormItem>
        <FormItem prop="title">
          <Input placeholder="请输入标题" type="text" v-model="searchForm.title" />
        </FormItem>
        <FormItem prop="sendStatus">
          <Select style="width:200px" v-model="searchForm.sendStatus">
            <Option :value="1">已发送</Option>
            <Option :value="0">未发送</Option>
          </Select>
        </FormItem>
        <FormItem>
          <ButtonGroup>
            <Button @click="find" icon="ios-search" type="primary" v-privilege="'email-query'">查询</Button>
            <Button @click="reset" icon="md-refresh" type="default" v-privilege="'email-query'">重置</Button>
          </ButtonGroup>
        </FormItem>
        <FormItem>
          <Button @click="addNew" icon="md-add" type="primary" v-privilege="'email-add'">新增</Button>
        </FormItem>
      </Form>
      <Table :columns="columns" :data="tableData"></Table>
      <Page
        :current="searchForm.pageNum"
        :page-size="searchForm.pageSize"
        :page-size-opts="[10, 20, 30, 50, 100]"
        :total="total"
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
import { emailApi } from '@/api/email.js';
export default {
  name: 'EmailList',
  components: {},
  props: {},
  data() {
    return {
      // 数据量
      total: null,
      // 查询参数
      searchForm: {
        endDate: null,
        pageNum: 1,
        pageSize: 10,
        searchCount: true, // 是否查询总条数
        sendStatus: null, // 发送状态 0未发送 1已发送
        startDate: null,
        title: null
      },
      // 表头
      columns: [
        {
          title: '创建时间',
          key: 'createTime'
        },
        {
          title: '发送状态',
          key: 'sendStatus',
          render(h, params) {
            let str = params.row.sendStatus == 0 ? '未发送' : '已发送';
            return h('span', {}, str);
          }
        },
        {
          title: '接收人',
          key: 'toEmails'
        },
        {
          title: '标题',
          key: 'title'
        },
        {
          title: '更新时间',
          key: 'updateTime'
        },
        {
          title: '邮件内容',
          key: 'content'
        },
        {
          title: '操作',
          key: 'action',
          width: 160,
          align: 'center',
          className: 'action-hide',
          render: (h, params) => {
            return this.$tableAction(h, [
              {
                title: '编辑',
                directives: [
                  {
                    name: 'privilege',
                    value: 'email-update'
                  }
                ],
                action: () => {
                  this.editEmail(params);
                }
              },
              {
                title: '删除',
                directives: [
                  {
                    name: 'privilege',
                    value: 'email-delete'
                  }
                ],
                action: () => {
                  this.deleteEmail(params);
                }
              }
            ]);
          }
        }
      ],
      // table数据
      tableData: [],
      // 删除id
      delId: null
    };
  },
  computed: {},
  watch: {},
  filters: {},
  created() {
    this.getEmailMess();
  },
  methods: {
    // 编辑邮件
    editEmail(params) {
      this.$router.push({
        path: '/email/send-mail',
        query: { id: params.row.id }
      });
    },
    // 新增
    addNew() {
      this.$router.push({ path: '/email/send-mail' });
    },
    // 重置
    reset() {
      this.$refs.searchForm.resetFields();
      this.searchForm.startDate = null;
      this.searchForm.endDate = null;
      this.$set(this.searchForm, 'endDate', null);
      this.find();
    },
    // 删除确定
    async deleteSure() {
      this.$Spin.show();
      let res = await emailApi.deleteEmail(this.delId);
      this.$Message.success('删除成功');
      this.$Spin.hide();
      this.getEmailMess();
    },
    // 删除操作
    deleteEmail(params) {
      this.delId = params.row.id;
      this.$Modal.confirm({
        title: '友情提醒',
        content: '确定要删除吗？',
        onOk: () => {
          this.deleteSure(params.row.id);
        }
      });
    },
    // 查询
    find() {
      this.searchForm.pageNum = 1;
      this.searchForm.pageSize = 10;
      this.getEmailMess();
    },
    // 更改分页查询条数
    changePageSize(pageSize) {
      this.searchForm.pageNum = 1;
      this.searchForm.pageSize = pageSize;
      this.getEmailMess();
    },
    // 获取邮件数据
    async getEmailMess() {
      this.$Spin.show();
      let res = await emailApi.getEmail(this.searchForm);
      this.$Spin.hide();
      this.tableData = res.data.list;
      this.total = res.data.total;
      if (res.data.pages < this.searchForm.pageNum) {
        this.searchForm.pageNum = res.data.pages;
      }
    },
    // 页码改变
    changePage(pageNum) {
      this.searchForm.pageNum = pageNum;
      this.getEmailMess();
    }
  }
};
</script>
