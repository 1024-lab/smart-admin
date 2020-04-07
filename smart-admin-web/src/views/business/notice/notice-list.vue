<template>
  <div>
    <Card class="warp-card" dis-hover>
      <Form class="tools" inline>
        <FormItem v-privilege="'notice-query'">
          <Input placeholder="请输入消息标题" v-model="searchData.title"></Input>
        </FormItem>
        <FormItem>
          <ButtonGroup>
            <Button
              @click="getNoticeList"
              icon="ios-search"
              type="primary"
              v-privilege="'notice-query'"
            >查询</Button>
            <Button
              @click="refresh"
              icon="md-refresh"
              type="default"
              v-privilege="'notice-query'"
            >重置</Button>
          </ButtonGroup>
        </FormItem>
        <FormItem>
          <Button
            @click="openModal('add')"
            icon="md-add"
            type="primary"
            v-privilege="'notice-add'"
          >添加消息</Button>
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
    <!-- 添加修改消息 -->
    <Modal
      :loading="saveLoading"
      :title="modalType=='add'?'添加':'修改'"
      @on-cancel="cancelSave"
      @on-ok="addOrUpdateNotice"
      v-model="editModal"
    >
      <Form :label-width="80" :model="formData" :rules="formValidate" ref="formRef">
        <FormItem label="标题" prop="title">
          <Input placeholder="请输入消息标题" v-model="formData.title"></Input>
        </FormItem>
        <FormItem label="内容" prop="content">
          <Input
            :autosize="{minRows: 2,maxRows: 5}"
            placeholder="请输入消息内容"
            type="textarea"
            v-model="formData.content"
          ></Input>
        </FormItem>
      </Form>
      </Modal>
      <!-- 添加修改消息 -->
  </div>
</template>

<script>
import { noticeApi } from '@/api/notice';
import { NOTICE_STATUS } from '@/constants/notice';
export default {
  name: 'NoticeList',
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
          key: 'createTime'
        },
        {
          title: '消息创建人',
          key: 'createUserName'
        },
        {
          title: '是否发送',
          key: 'sendStatus',
          render: (h, params) => {
            return h(
              'span',
              this.$enum.getDescByValue('NOTICE_STATUS', params.row.sendStatus)
            );
          }
        },
        {
          title: '操作',
          key: 'action',
          align: 'center',
          width: 200,
          className: 'action-hide',
          render: (h, params) => {
            let btns = [
              {
                title: '详情',
                directives: [
                  {
                    name: 'privilege',
                    value: 'notice-detail'
                  }
                ],
                action: () => {
                  this.$router.push({
                    name:'NoticeDetail',
                    params:{notice:params.row}
                  })
                }
              },
              {
                title: '删除',
                directives: [
                  {
                    name: 'privilege',
                    value: 'notice-delete'
                  }
                ],
                action: () => {
                  this.$Modal.confirm({
                    title: '删除',
                    content: '确认删除该条消息么？',
                    onOk: () => {
                      this.deleteNotice(params.row.id);
                    }
                  });
                }
              }
            ];
            let editBtn = {
              title: '编辑',
              directives: [
                {
                  name: 'privilege',
                  value: 'notice-edit'
                }
              ],
              action: () => {
                this.openModal('edit', params.row);
              }
            };
            let sendBtn = {
              title: '发送',
              directives: [
                {
                  name: 'privilege',
                  value: 'notice-send'
                }
              ],
              action: () => {
                this.$Modal.confirm({
                  title: '确认',
                  content: '确认发送条消息么？',
                  onOk: () => {
                    this.sendNotice(params.row.id);
                  }
                });
              }
            };
            if (params.row.sendStatus != NOTICE_STATUS['YES'].value) {
              btns.push(editBtn);
            }
            if (params.row.sendStatus != NOTICE_STATUS['YES'].value) {
              btns.push(sendBtn);
            }
            return this.$tableAction(h, btns);
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
    this.getNoticeList();
  },
  beforeCreate() {},
  beforeMount() {},
  beforeUpdate() {},
  updated() {},
  beforeDestroy() {},
  destroyed() {},
  activated() {},
  methods: {
    // 查询消息列表
    async getNoticeList() {
      try {
        this.loading = true;
        let result = await noticeApi.getNoticeList({
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
      } catch (e) {
        this.loading = true;
        //TODO zhuoda sentry
        console.error(e);
      }
    },
    // 添加消息
    openModal(type, data) {
      this.modalType = type;
      switch (type) {
        case 'add':
          this.formData = {};
          this.editModal = true;
          break;
        case 'edit':
          this.getNoticeDetail(data.id);
          this.editModal = true;
          break;
      }
    },
    // 分页
    changePage(pageNum) {
      this.pageNum = pageNum;
      this.getNoticeList();
    },
    // 更改分页查询条数
    changePageSize(pageSize) {
      this.pageNum = 1;
      this.pageSize = pageSize;
      this.getNoticeList();
    },
    // 删除通知
    async deleteNotice(id) {
      this.$Spin.show();
      let result = await noticeApi.deleteNotice(id);
      this.$Spin.hide();
      this.$Message.success('删除消息成功!');
      this.getNoticeList();
    },
    // 发送消息
    async sendNotice(id) {
      this.$Spin.show();
      let result = await noticeApi.sendNotice(id);
      this.$Message.success('发送消息成功!');
      this.$Spin.hide();
      this.getNoticeList();
    },
    // 保存任务
    addOrUpdateNotice() {
      try {
        this.$refs['formRef'].validate(valid => {
          if (valid) {
            if (this.modalType == 'add') {
              this.addNotice();
            } else {
              this.editNotice();
            }
          }
        });
      } catch (e) {
        //TODO zhuoda sentry
        console.error(e);
      }
    },
    // 修改消息
    async editNotice() {
      try {
        this.loading = true;
        let result = await noticeApi.updateNotice(this.formData);
        this.$Message.success('修改成功');
        this.editModal = false;
        this.getNoticeList();
      } catch (e) {
        //TODO zhuoda sentry
        console.error(e);
        this.loading = false;
      }
    },
    // 保存消息
    async addNotice() {
      try {
        this.loading = true;
        let result = await noticeApi.addNotice(this.formData);
        this.$Message.success('添加成功');
        this.editModal = false;
        this.getNoticeList();
        this.loading = false;
      } catch (e) {
        //TODO zhuoda sentry
        console.error(e);
        this.loading = false;
      }
    },
    // 取消添加/修改
    cancelSave() {
      this.editModal = false;
      this.$refs['formRef'].resetFields();
    },
    // 重置
    refresh() {
      this.pageNum = 1;
      this.searchData.title = '';
      this.getNoticeList();
    }
  }
};
</script>

