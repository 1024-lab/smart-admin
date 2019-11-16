<template>
  <div>
    <Card class="warp-card" dis-hover>
      <Form class="tools" inline v-privilege="'online-user-search'">
        <FormItem>
          <Input placeholder="请输入用户姓名" v-model="searchData.actualName">
            <Button @click="getOnlineUserList" icon="ios-search" slot="append"></Button>
          </Input>
        </FormItem>
        <FormItem>
          <Button @click="refresh" type="primary">重置</Button>
        </FormItem>
      </Form>
      <p class="online-num">
        当前在线人数:
        <span>
          <count-to :end="pageTotal" />
        </span>人
      </p>
      <Table :columns="columns" :data="data" :loading="loading"></Table>
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
  </div>
</template>

<script>
import { onlineUserApi } from '@/api/online-user';
import CountTo from '_c/count-to';
export default {
  name: 'OnlineUser',
  props: {},
  components: {
    CountTo
  },
  data() {
    return {
      searchData: {
        actualName: ''
      },
      modalType: 'add',
      editModal: false,
      addModal: false,
      loading: true,
      pageNum: 1,
      pageSize: 10,
      pageTotal: 0,
      formData: {
        title: '',
        content: ''
      },
      // table表头
      columns: [
        {
          title: 'ID',
          key: 'id',
          width: 60,
          align: 'center'
        },
        {
          title: '姓名',
          key: 'actualName'
        },
        {
          title: '登录账户',
          key: 'loginName'
        },
        {
          title: '手机号',
          key: 'phone'
        },

        {
          title: '部门',
          key: 'departmentName'
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
    this.getOnlineUserList();
  },
  methods: {
    // 获取在线用户集合
    async getOnlineUserList() {
      this.loading = true;
      try {
        let result = await onlineUserApi.getOnlineUserList({
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
        this.loading = true;
        //TODO zhuoda sentry
        console.error(e);
      }
    },
    // 分页
    changePage(pageNum) {
      this.pageNum = pageNum;
      this.getOnlineUserList();
    },
    changePageSize(pageSize) {
      this.pageNum = 1;
      this.pageSize = pageSize;
      this.getOnlineUserList();
    },
    refresh() {
      this.pageNum = 1;
      this.searchData.actualName = '';
      this.getOnlineUserList();
    }
  }
};
</script>

<style lang="less" scoped>
.online-num {
  display: flex;
  margin: 10px 0;
  line-height: 0;
  font-size: 15px;
  padding: 10px 0;
  span {
    font-size: 18px;
    color: #2d8cf0;
    padding: 0 10px;
  }
}
</style>
