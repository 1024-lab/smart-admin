<template>
  <div>
    <Card class="warp-card" dis-hover>
      <Form class="tools" inline>
        <FormItem v-privilege="'person-notice-query'">
          <Input placeholder="请输入消息标题" v-model="searchData.title">
            <Button @click="getPersonNoticeList" icon="ios-search" slot="append"></Button>
          </Input>
        </FormItem>
        <FormItem>
          <Button
            @click="refresh"
            icon="md-refresh"
            type="primary"
            v-privilege="'person-notice-query'"
          >重置</Button>
        </FormItem>
      </Form>
      <Table :columns="columns" :data="data" :loading="loading" border></Table>
      <Page
        :current="pageNum"
        :page-size="pageSize"
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
    <Modal :loading="saveLoading" :title="formData.title" class="detail-modal" v-model="editModal">
      <div class="detail">{{formData.content}}</div>
      <p class="time">{{formData.updateTime}}</p>
      <div slot="footer">
        <Button @click="cancelSave" size="large" type="primary">知道了</Button>
      </div>
    </Modal>
  </div>
</template>

<script>
import { noticeApi } from '@/api/notice';
export default {
  name: 'PersonNotice',
  components: {},
  props: {},
  data() {
    return {
      searchData: {
        title: ''
      },
      modalType: 'add',
      editModal: false,
      addModal: false,
      loading: true,
      saveLoading: true,
      updateLoading: true,
      pageNum: 1,
      pageSize: 10,
      pageTotal: 0,
      updateItem: {},
      saveItem: {},
      formData: {
        title: '',
        content: ''
      },
      // table表头
      columns: [
        {
          title: 'ID',
          width: 60,
          key: 'id'
        },
        {
          title: '消息标题',
          key: 'title'
        },
        {
          title: '创建时间',
          key: 'receiveTime'
        },
        {
          title: '消息创建人',
          key: 'createUserName'
        },
        {
          title: '状态',
          key: 'createUser',
          render: (h, params) => {
            return h('span', params.row.readStatus ? '已读' : '未读');
          }
        },
        {
          title: '操作',
          key: 'action',
          align: 'center',
          width: 200,
          className: 'action-hide',
          render: (h, params) => {
            return this.$tableAction(h, [
              {
                title: '查看',
                directives: [
                  {
                    name: 'privilege',
                    value: 'person-notice-detail'
                  }
                ],
                action: () => {
                  this.getNoticeDetail(params.row.id);
                }
              }
            ]);
          }
        }
      ],
      // table数据
      data: [],
      formValidate: {
        title: [{ required: true, message: '请输入消息标题', trigger: 'blur' }],
        content: [
          { required: true, message: '请输入消息内容', trigger: 'blur' }
        ]
      }
    };
  },
  computed: {},
  watch: {},
  filters: {},
  created() {},
  mounted() {
    this.getPersonNoticeList();
  },
  beforeCreate() {},
  beforeMount() {},
  beforeUpdate() {},
  updated() {},
  beforeDestroy() {},
  destroyed() {},
  activated() {},
  methods: {
    // 查询个人消息列表
    async getPersonNoticeList() {
      try {
        this.loading = true;
        let result = await noticeApi.getPersonNoticeList({
          ...this.searchData,
          pageNum: this.pageNum,
          pageSize: this.pageSize
        });
        this.loading = false;
        this.data = result.data.list;
        this.pageTotal = result.data.total;
        this.pageNum = result.data.pageNum;
        this.pageSize = result.data.pageSize;
      } catch (e) {
        //TODO zhuoda sentry
        this.loading = false;
        console.error(e);
      }
    },
    // 获取通知详情
    async getNoticeDetail(id) {
      try {
        this.loading = true;
        let result = await noticeApi.getNoticeDetail(id);
        this.loading = false;
        this.formData = result.data;
        this.openModal(result.data);
      } catch (e) {
        //TODO zhuoda sentry
        this.loading = false;
        console.error(e);
      }
    },
    // 标记已读
    async openModal(data) {
      this.editModal = true;
      this.$Spin.show();
      let result = await noticeApi.addNoticeRecord(data.id);
      this.$Spin.hide();
    },
    // 分页
    changePage(pageNum) {
      this.pageNum = pageNum;
      this.getPersonNoticeList();
    },
    // 更改分页查询条数
    changePageSize(pageSize) {
      this.pageNum = 1;
      this.pageSize = pageSize;
      this.getPersonNoticeList();
    },
    // 重置
    refresh() {
      this.pageNum = 1;
      this.searchData.title = '';
      this.getPersonNoticeList();
    },
    // 关闭详情
    cancelSave() {
      this.editModal = false;
      this.getPersonNoticeList();
    }
  }
};
</script>

<style lang="less" scoped>
.detail-modal {
  .detail {
    margin-bottom: 20px;
  }
  .time {
    text-align: right;
    color: #999;
  }
}
</style>
