<template>
  <div>
    <!-- Card 岗位管理内容区 start -->
    <Card class="warp-card" dis-hover>
      <!-- Form 搜索按钮区 start -->
      <Form class="tools" inline>
        <FormItem>
          <Input
            placeholder="请输入"
            v-model="searchFrom.positionName"
            v-privilege="'search-position'"
          >
            <Button @click="searchData" icon="ios-search" slot="append"></Button>
          </Input>
        </FormItem>
        <FormItem>
          <Button
            @click="clearSearch"
            icon="md-refresh"
            type="default"
            v-privilege="'search-position'"
          >重置</Button>
        </FormItem>
        <FormItem>
          <Button
            @click="isShowAddModal=true"
            icon="md-add"
            type="primary"
            v-privilege="'add-position'"
          >添加</Button>
        </FormItem>
      </Form>
      <!-- Form 搜索按钮区 end -->
      <Table :columns="columns" :data="data" :loading="isShowTablesLoading" ref="tablesMain"></Table>
      <Page
        :current="searchFrom.pageNum"
        :page-size="searchFrom.pageSize"
        :show-total="true"
        :total="pageTotal"
        @on-change="changePage"
        @on-page-size-change="changePageSize"
        style="margin:24px 0;text-align:right;"
      ></Page>
    </Card>
    <!-- Card 内容区 end -->
    <!-- Modal 编辑岗位弹窗 start -->
    <Modal
      :loading="isShowUpdateLoading"
      @on-cancel="cancelUpdateData"
      @on-ok="validateAndUpdataPosition"
      title="编辑岗位"
      v-model="isShowEditModal"
    >
      <Form :label-width="80" :model="updateItem" :rules="updateValidate" ref="updateRef">
        <FormItem label="岗位名称" prop="positionName">
          <Input placeholder="请输入岗位名称(必填)" v-model="updateItem.positionName"></Input>
        </FormItem>
        <FormItem label="岗位描述" prop="remark">
          <Input
            :autosize="{minRows: 4}"
            :maxlength="200"
            placeholder="请输入岗位描述(必填)"
            type="textarea"
            v-model="updateItem.remark "
          ></Input>
        </FormItem>
      </Form>
    </Modal>
    <!-- Modal 编辑岗位弹窗 end -->
    <!-- Modal 添加岗位弹窗 start -->
    <Modal
      :loading="isShowSaveLoading"
      @on-cancel="cancelSaveData"
      @on-ok="validateAndAddPosition"
      title="添加岗位"
      v-model="isShowAddModal"
    >
      <Form :label-width="80" :model="saveItem" :rules="saveValidate" ref="saveRef">
        <FormItem label="岗位名称" prop="positionName">
          <Input :maxlength="30" placeholder="请输入岗位名称(必填)" v-model="saveItem.positionName"></Input>
        </FormItem>
        <FormItem label="岗位描述" prop="remark">
          <Input
            :autosize="{minRows: 4}"
            :maxlength="200"
            placeholder="请输入岗位描述(必填)"
            type="textarea"
            v-model="saveItem.remark "
          ></Input>
        </FormItem>
      </Form>
    </Modal>
    <!-- Modal 添加岗位弹窗 end -->
  </div>
</template>

<script>
import { positionApi } from '@/api/position';
export default {
  name: 'PositionList',
  components: {},
  props: {},
  data() {
    return {
      isShowPage: true,
      searchValue: '',
      isShowEditModal: false,
      isShowAddModal: false,
      // table是否Loading
      isShowTablesLoading: true,
      isShowSaveLoading: true,
      isShowUpdateLoading: true,
      pageTotal: 0,
      // 更新的数据
      updateItem: {
        id: 0,
        positionName: 'positionName',
        remark: ''
      },
      // 添加保存的数据
      saveItem: {
        positionName: '',
        remark: ''
      },
      saveItemInt: {},
      // table表头
      columns: [
        {
          title: 'id',
          key: 'id',
          width: 100
        },
        {
          title: '岗位名称',
          key: 'positionName',
          width: 200
        },
        {
          title: '岗位描述',
          key: 'remark'
        },
        {
          title: '操作',
          key: 'action',
          width: 150,
          align: 'center',
          className: 'action-hide',
          render: (h, params) => {
            return this.$tableAction(h, [
              {
                title: '编辑',
                directives: [
                  {
                    name: 'privilege',
                    value: 'update-position'
                  }
                ],
                action: () => {
                  this.updateItem = {
                    id: params.row.id,
                    positionName: params.row.positionName,
                    remark: params.row.remark
                  };
                  this.isShowEditModal = true;
                }
              },
              {
                title: '删除',
                directives: [
                  {
                    name: 'privilege',
                    value: 'delete-position'
                  }
                ],
                action: () => {
                  this.$Modal.confirm({
                    title: '友情提醒',
                    content: '确定要删除吗？',
                    onOk: () => {
                      this.deletePositionById(params.row.id);
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
      updateValidate: {
        positionName: [
          { required: true, message: '请输入岗位名称', trigger: 'blur' }
        ],
        remark: [{ required: true, message: '请输入岗位描述', trigger: 'blur' }]
      },
      saveValidate: {
        positionName: [
          { required: true, message: '请输入岗位名称', trigger: 'blur' }
        ],
        remark: [{ required: true, message: '请输入岗位描述', trigger: 'blur' }]
      },
      searchFrom: {
        positionName: '',
        pageNum: 1,
        pageSize: 10,
        searchCount: true,
        sort: false
      },
      searchFromInt: {},
      isShowdeleteLoading: false
    };
  },
  computed: {},
  watch: {},
  filters: {},
  created() {},
  mounted() {
    Object.assign(this.searchFromInt, this.searchFrom);
    Object.assign(this.saveItemInt, this.saveItem);
    this.getPositionListPage();
  },
  beforeCreate() {},
  beforeMount() {},
  beforeUpdate() {},
  updated() {},
  beforeDestroy() {},
  destroyed() {},
  activated() {},
  methods: {
    // 分页查询所有岗位
    async getPositionListPage() {
      try {
        this.isShowTablesLoading = true;
        this.isShowPage = true;
        let result = await positionApi.getPositionListPage(this.searchFrom);
        this.isShowTablesLoading = false;
        let datas = result.data;
        this.data = datas.list;
        this.pageTotal = datas.total;
      } catch (e) {
        this.isShowTablesLoading = false;
        //TODO zhuoda sentry
        console.error(e);
      }
    },
    // 页码改变
    changePage(pageNum) {
      this.searchFrom.pageNum = pageNum;
      this.getPositionListPage();
    },
    // 改变每页显示数据条数
    changePageSize(pageSize) {
      this.searchFrom.pageNum = 1;
      this.searchFrom.pageSize = pageSize;
      this.getPositionListPage();
    },
    // 检验参数后 更新岗位
    validateAndUpdataPosition() {
      this.$refs['updateRef'].validate(valid => {
        this.isShowUpdateLoading = true;
        if (valid) {
          this.updatePosition();
        } else {
          this.isShowUpdateLoading = false;
          this.$nextTick(() => {
            this.isShowUpdateLoading = true;
          });
        }
      });
    },
    // 更新岗位
    async updatePosition() {
      try {
        let result = await positionApi.updatePosition(this.updateItem);
        this.$Message.success('修改成功');
        await this.getPositionListPage();
        this.cancelUpdateData();
      } catch (e) {
        this.isShowUpdateLoading = false;
        this.$nextTick(() => {
          this.isShowUpdateLoading = true;
        });
        //TODO zhuoda sentry
        console.error(e);
      } finally {
        this.isShowUpdateLoading = false;
      }
    },
    // 清除编辑模态框数据
    cancelUpdateData() {
      this.updateItem = {};
      // 清空form规则检查
      this.$refs['updateRef'].resetFields();
      this.isShowEditModal = false;
    },
    // 搜索
    searchData() {
      this.pageNum = 1;
      this.getPositionListPage();
    },
    // 重置
    clearSearch() {
      Object.assign(this.searchFrom, this.searchFromInt);
      this.getPositionListPage();
    },
    // 添加岗位
    validateAndAddPosition() {
      try {
        this.$refs['saveRef'].validate(valid => {
          this.isShowSaveLoading = true;
          if (valid) {
            this.addPosition();
          } else {
            this.isShowSaveLoading = false;
            this.$nextTick(() => {
              this.isShowSaveLoading = true;
            });
          }
        });
      } catch (e) {
        //TODO zhuoda sentry
        console.error(e);
      }
    },
    // 添加岗位 - 异步
    async addPosition() {
      try {
        let result = await positionApi.addPosition(this.saveItem);
        this.$Message.success('添加成功');
        this.isShowSaveLoading = true;
        await this.getPositionListPage();
        this.cancelSaveData();
      } catch (e) {
        //TODO zhuoda sentry
        console.error(e);
        this.isShowSaveLoading = false;
      }
    },
    // 清除添加模态框数据
    cancelSaveData() {
      Object.assign(this.saveItem, this.saveItemInt);
      // 清空form规则检查
      this.$refs['saveRef'].resetFields();
      this.isShowAddModal = false;
    },
    // 根据ID删除岗位
    async deletePositionById(id) {
      try {
        this.isShowdeleteLoading = true;
        let result = await positionApi.deletePosition(id);
        this.isShowdeleteLoading = false;
        this.$Message.success('删除成功');
        await this.getPositionListPage();
        this.cancelSaveData();
      } catch (e) {
        //TODO zhuoda sentry
        console.error(e);
        this.isShowdeleteLoading = false;
      }
    }
  }
};
</script>
<style lang="less" scoped>
</style>
