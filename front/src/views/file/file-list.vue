<template>
  <div>
    <Card class="warp-card" dis-hover>
      <Form :model="searchForm" class="tools" inline ref="searchForm">
        <FormItem prop="fileName">
          <Input placeholder="请输入文件名" type="text" v-model="searchForm.fileName" />
        </FormItem>
        <FormItem prop="sendStatus">
          <Select placeholder="请选择业务类型" style="width:200px" v-model="searchForm.moduleType">
            <Option value>全部</Option>
            <Option :key="item.value" :value="item.value" v-for="item in moduleTypes">{{item.desc}}</Option>
          </Select>
        </FormItem>
        <FormItem prop="sendStatus">
          <Select placeholder="请选择文件上传位置" style="width:200px" v-model="searchForm.fileLocationType">
            <Option value>全部</Option>
            <Option
              :key="item.value"
              :value="item.value"
              v-for="item in fileLocationTypes"
            >{{item.desc}}</Option>
          </Select>
        </FormItem>
        <FormItem>
          <ButtonGroup>
            <Button
              @click="find"
              icon="ios-search"
              type="primary"
              v-privilege="'file-filePage-query'"
            >查询</Button>
            <Button
              @click="reset"
              icon="md-refresh"
              type="default"
              v-privilege="'file-filePage-query'"
            >重置</Button>
          </ButtonGroup>
        </FormItem>
        <FormItem v-privilege="'file-filePage-upload'">
          <Upload
            :action="uploadUrl"
            :headers="uploadHeader"
            :onError="uploadError"
            :onSuccess="uploadSuccess"
            :showUploadList="false"
          >
            <Button icon="ios-cloud-upload-outline" type="primary">上传新文件</Button>
          </Upload>
        </FormItem>
      </Form>
      <Table :columns="columns" :data="tableData" :loading="tableLoading"></Table>
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
import { fileApi } from '@/api/file.js';
import { FILE_TYPE, SERVICE_TYPE } from '@/constants/file';
import Cookies from 'js-cookie';
import { TOKEN_KEY } from '@/lib/cookie';
export default {
  name: 'FileList',
  data() {
    let that = this;
    return {
      // 数据量
      total: null,
      // 文件表格加载
      tableLoading: false,
      // 查询参数
      searchForm: {
        pageNum: 1,
        pageSize: 10,
        // 是否查询总条数
        searchCount: true,
        fileName: null,
        // 业务类型 BACK_USER:1 backUser/config
        moduleType: 1,
        // 文件位置 LOCAL:1 本地文件服务 ALI_OSS:2 阿里OSS文件服务 QI_NIU_OSS:3七牛文件服务
        fileLocationType: null
      },
      // 表头
      columns: [
        {
          title: '业务类型',
          key: 'moduleType',
          render(h, params) {
            return h('span', {}, that.getModuleTypeDesc(params.row.moduleType));
          }
        },
        {
          title: '文件位置',
          key: 'fileLocationType',
          render(h, params) {
            return h(
              'span',
              {},
              that.getFileLocationTypeDesc(params.row.fileLocationType)
            );
          }
        },
        {
          title: '文件名称',
          key: 'fileName'
        },
        {
          title: '上传时间',
          key: 'createTime'
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
                title: '下载',
                directives: [
                  {
                    name: 'privilege',
                    value: 'file-filePage-download'
                  }
                ],
                action: () => {
                  this.downloadFile(params.row.id);
                }
              }
            ]);
          }
        }
      ],
      // table数据
      tableData: []
    };
  },
  computed: {
    uploadHeader: function() {
      let header = {
        'x-access-token': Cookies.get(TOKEN_KEY)
      };
      return header;
    },
    uploadUrl: function() {
      let baseUrl = fileApi.fileUploadLocalUrl;
      switch (this.searchForm.fileLocationType) {
        case 2:
          baseUrl = fileApi.fileUploadAliUrl;
          break;
        case 3:
          baseUrl = fileApi.fileUploadQiNiuUrl;
          break;
        default:
          break;
      }
      let url = baseUrl + this.searchForm.moduleType;
      return url;
    },
    // 文件业务类型
    moduleTypes: function() {
      let array = [];
      for (let item in SERVICE_TYPE) {
        let obj = {};
        obj.desc = SERVICE_TYPE[item].desc;
        obj.value = SERVICE_TYPE[item].value;
        array.push(obj);
      }
      return array;
    },
    fileLocationTypes: function() {
      let array = [];
      for (const item in FILE_TYPE) {
        let obj = {};
        obj.desc = FILE_TYPE[item].desc;
        obj.value = FILE_TYPE[item].value;
        array.push(obj);
      }
      return array;
    }
  },
  created() {
    this.getFileList();
  },
  methods: {
    // 上传成功钩子
    async uploadSuccess(e) {
      if (!e.success) {
        console.error(e);
        return this.uploadError();
      }
      this.$Spin.show();
      let reqBody = {
        moduleId: 1,
        moduleType: this.searchForm.moduleType ? this.searchForm.moduleType : 1,
        fileLocationType: this.searchForm.fileLocationType
          ? this.searchForm.fileLocationType
          : 1,
        fileName: e.data.fileName,
        filePath: e.data.filePath
      };
      let rep = await fileApi.addFile(reqBody);
      this.$Spin.hide();
      this.$Message.success('上传成功');
      this.find();
    },
    // 上传失败钩子
    uploadError(e) {
      this.$Message.error('上传出错，请重试！');
      console.error(e);
      this.find();
    },
    // 下载文件
    downloadFile(id) {
      fileApi.downLoadFile(id);
    },
    // 根据数字获取业务类型描述
    getModuleTypeDesc(value) {
      return this.$enum.getDescByValue('SERVICE_TYPE', value);
    },
    // 根据数字获取文件位置描述
    getFileLocationTypeDesc(value) {
      return this.$enum.getDescByValue('FILE_TYPE', value);
    },
    // 重置
    reset() {
      this.searchForm.fileName = null;
      this.searchForm.moduleType = 1;
      this.searchForm.fileLocationType = null;
      this.$refs.searchForm.resetFields();
      this.find();
    },
    // 查询
    find() {
      this.searchForm.pageNum = 1;
      this.searchForm.pageSize = 10;
      this.getFileList();
    },
    // 更改分页查询条数
    changePageSize(pageSize) {
      this.searchForm.pageNum = 1;
      this.searchForm.pageSize = pageSize;
      this.getFileList();
    },
    // 获取文件数据
    async getFileList() {
      try {
        this.tableLoading = true;
        let res = await fileApi.queryFileList(this.searchForm);
        this.tableData = res.data.list;
        this.total = res.data.total;
        this.tableLoading = false;
      } catch (e) {
        //TODO zhuoda sentry
        console.error(e);
        this.tableLoading = false;
      }
    },
    // 页码改变
    changePage(pageNum) {
      this.searchForm.pageNum = pageNum;
      this.getFileList();
    }
  }
};
</script>
