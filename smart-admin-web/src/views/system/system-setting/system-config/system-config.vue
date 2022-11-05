<template>
  <div>
    <Card class="warp-card" dis-hover>
      <Form class="tools" inline>
        <FormItem>
          <Input placeholder="请输入" v-model="searchValue" v-privilege="'system-params-search'">
            <Select slot="prepend" style="width: 120px" v-model="searchType">
              <Option value="BY_KEY">按key</Option>
              <Option value="BY_GROUP">按参数类别</Option>
            </Select>
          </Input>
        </FormItem>
        <FormItem>
          <ButtonGroup>
            <Button
              @click="searchSystemConfigList"
              icon="ios-search"
              type="primary"
              v-privilege="'system-params-search'"
            >查询</Button>
            <Button
              @click="refresh"
              icon="md-refresh"
              type="default"
              v-privilege="'system-params-search'"
            >重置</Button>
          </ButtonGroup>
        </FormItem>
        <FormItem>
          <Button
            @click="addModal=true"
            icon="md-add"
            type="primary"
            v-privilege="'system-params-add'"
          >添加</Button>
        </FormItem>
      </Form>
      <Table
        :columns="columns"
        :data="data"
        :loading="loading"
        :pageNumber="pageNum"
        :pageShow="pageShow"
        border
      ></Table>
      <Page
        :current="pageNum"
        :page-size="pageSize"
        :total="pageTotal"
        @on-change="changePage"
        @on-page-size-change="changePageSize"
        show-elevator
        show-total
        style="margin:24px 0;text-align:right;"
      />
    </Card>
    <Modal @on-cancel="cancelUpdate" @on-ok="handleUpdate" title="编辑" v-model="editModal">
      <Form :label-width="80" :model="updateItem" :rules="updateValidate" ref="updateRef">
        <FormItem label="Key">
          <Input disabled="disabled" v-model="updateItem.configKey"></Input>
        </FormItem>
        <FormItem label="标题">
          <Input disabled="disabled" type="textarea" v-model="updateItem.configName" />
        </FormItem>
        <FormItem label="参数类别" prop="configGroup">
          <Input disabled="disabled" placeholder="请输入数据类型(必填)" v-model="updateItem.configGroup" />
        </FormItem>
        <FormItem label="值" prop="configValue">
          <Input
            :autosize="{minRows: 5}"
            placeholder="请输入值(必填)"
            type="textarea"
            v-model="updateItem.configValue"
          ></Input>
        </FormItem>

        <FormItem label="备注" prop="remark">
          <Input
            :autosize="{minRows: 4}"
            placeholder="请输入备注"
            type="textarea"
            v-model="updateItem.remark "
          ></Input>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button @click="cancelUpdate" type="text">取消</Button>
        <Button @click="handleUpdate" type="primary">确定</Button>
      </div>
    </Modal>
    <Modal title="添加" v-model="addModal">
      <Form :label-width="80" :model="saveItem" :rules="saveValidate" ref="saveRef">
        <FormItem label="Key" prop="configKey">
          <Input placeholder="请输入key(必填)" v-model="saveItem.configKey" />
        </FormItem>
        <FormItem label="标题" prop="configName">
          <Input placeholder="请输入标题(必填)" v-model="saveItem.configName" />
        </FormItem>
        <FormItem label="参数类别" prop="configGroup">
          <Input placeholder="请输入数据类型(必填)" v-model="saveItem.configGroup" />
        </FormItem>
        <FormItem label="值" prop="configValue">
          <Input
            :autosize="{minRows: 5}"
            placeholder="请输入值(必填)"
            type="textarea"
            v-model="saveItem.configValue"
          ></Input>
        </FormItem>
        <FormItem label="备注" prop="remark">
          <Input
            :autosize="{minRows: 4}"
            placeholder="请输入备注"
            type="textarea"
            v-model="saveItem.remark "
          ></Input>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button @click="cancelSave" type="text">取消</Button>
        <Button @click="handleSave" type="primary">确定</Button>
      </div>
    </Modal>

    <Modal :closable="false" :mask-closable="false" title="值查看" v-model="valueModal">
      <json-viewer :expand-depth="10" :value="valueData" boxed copyable sort></json-viewer>
      <div slot="footer">
        <Button @click="cancelValueModal" type="primary">关闭</Button>
      </div>
    </Modal>
  </div>
</template>

<script>
import { systemConfigApi } from '@/api/system-config';
export default {
  name: 'SystemConfig',
  components: {},
  props: {},
  data() {
    return {
      pageShow: true,
      searchValue: '',
      searchType: 'BY_KEY',
      editModal: false,
      addModal: false,
      valueModal: false,
      // table是否loading
      loading: true,
      saveLoading: true,
      updateLoading: true,
      pageNum: 1,
      pageSize: 10,
      pageTotal: 0,
      updateItem: {
        id: 0,
        configKey: '',
        configName: '',
        configValue: '',
        remark: ''
      },
      saveItem: {
        configKey: '',
        configName: '',
        configValue: '',
        configGroup: '',
        remark: ''
      },
      valueData: '',
      // table表头
      columns: [
        {
          title: 'ID',
          key: 'id',
          width: 60
        },
        {
          title: 'Key',
          key: 'configKey'
        },
        {
          title: '标题',
          key: 'configName'
        },
        {
          title: '值',
          key: 'configValue',
          width: 200,
          ellipsis: true,
          tooltip: true
        },
        {
          title: '参数类别',
          key: 'configGroup'
        },
        {
          title: '备注',
          key: 'remark'
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
                title: '编辑',
                directives: [
                  {
                    name: 'privilege',
                    value: 'system-config-update'
                  }
                ],
                action: () => {
                  this.updateItem.id = params.row.id;
                  this.updateItem.configGroup = params.row.configGroup;
                  this.updateItem.configKey = params.row.configKey;
                  this.updateItem.configName = params.row.configName;
                  this.updateItem.configValue = params.row.configValue;
                  this.updateItem.remark = params.row.remark;
                  this.editModal = true;
                }
              },
              {
                title: '查看值',
                directives: [
                  {
                    name: 'privilege',
                    value: 'system-config-search'
                  }
                ],
                action: () => {
                  try {
                    let valueData = JSON.parse(params.row.configValue);
                    if (typeof valueData === 'object' && valueData) {
                      this.valueData = valueData;
                    } else {
                      this.valueData = params.row.configValue;
                    }
                  } catch (e) {
                    //TODO zhuoda sentry
                    console.error(e);
                    this.valueData = params.row.configValue;
                  }
                  this.valueModal = true;
                }
              }
            ]);
          }
        }
      ],
      // table数据
      data: [],
      // 验证规则
      updateValidate: {
        configValue: [{ required: true, message: '请输入值', trigger: 'blur' }]
      },
      saveValidate: {
        configKey: [{ required: true, message: '请输入Key', trigger: 'blur' }],
        configName: [
          { required: true, message: '请输入标题', trigger: 'blur' }
        ],
        configValue: [{ required: true, message: '请输入值', trigger: 'blur' }],
        configGroup: [
          { required: true, message: '请输入参数类别', trigger: 'blur' }
        ]
      }
    };
  },
  computed: {},
  watch: {},
  filters: {},
  created() {},
  mounted() {
    this.getSystemConfigList();
  },
  beforeCreate() {},
  beforeMount() {},
  beforeUpdate() {},
  updated() {},
  beforeDestroy() {},
  destroyed() {},
  activated() {},
  methods: {
    // 查询系统参数列表
    async getSystemConfigList() {
      this.loading = true;
      this.pageShow = true;
      let searchForm = {};
      if (this.searchType === 'BY_KEY') {
        searchForm['key'] = this.searchValue;
      } else {
        searchForm['configGroup'] = this.searchValue;
      }
      let result = await systemConfigApi.getSystemConfigList({
        ...searchForm,
        pageNum: this.pageNum,
        pageSize: this.pageSize
      });
      this.loading = false;
      this.data = result.data.list;
      this.pageTotal = result.data.total;
      this.pageNum = result.data.pageNum;
      this.pageSize = result.data.pageSize;
    },
    // 分页
    changePage(pageNum) {
      this.pageNum = pageNum;
      this.getSystemConfigList();
    },
    // 更改分页查询条数
    changePageSize(pageSize) {
      this.pageNum = 1;
      this.pageSize = pageSize;
      this.getSystemConfigList();
    },
    // 修改
    handleUpdate() {
      this.$refs['updateRef'].validate(valid => {
        if (valid) {
          this.updateSystemConfig();
        }
      });
    },
    // 修改系统设置信息
    async updateSystemConfig() {
      this.$Spin.show();
      let result = await systemConfigApi.updateSystemConfig(this.updateItem);
      this.$Spin.hide();
      this.$Message.success('修改成功');
      this.getSystemConfigList();
      this.cancelUpdate();
    },
    // 搜索
    searchSystemConfigList() {
      this.pageNum = 1;
      this.getSystemConfigList();
    },
    // 保存系统设置信息
    handleSave() {
      try {
        this.$refs['saveRef'].validate(valid => {
          if (valid) {
            this.addSystemConfig();
          }
        });
      } catch (e) {
        //TODO zhuoda sentry
        console.log(e);
      }
    },
    // 新增
    async addSystemConfig() {
      try {
        this.saveLoading = true;
        let result = await systemConfigApi.addSystemConfig(this.saveItem);
        this.saveLoading = false;
        this.$Message.success('添加成功');
        this.getSystemConfigList();
        this.cancelSave();
      } catch (e) {
        //TODO zhuoda sentry
        console.log(e);
        this.saveLoading = false;
      }
    },
    // 取消
    cancelSave() {
      this.saveItem = {};
      // 清空form规则检查
      this.$refs['saveRef'].resetFields();
      this.addModal = false;
    },
    // 取消模态框
    cancelValueModal() {
      this.valueData = '';
      this.valueModal = false;
    },
    // 取消
    cancelUpdate() {
      this.updateItem = {};
      // 清空form规则检查
      this.$refs['updateRef'].resetFields();
      this.editModal = false;
    },
    // 刷新
    refresh() {
      this.pageNum = 1;
      this.searchType = 'BY_KEY';
      this.searchValue = '';
      this.getSystemConfigList();
    }
  }
};
</script>
