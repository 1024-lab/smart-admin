<template>
  <Card class="warp-card" dis-hover>
    <Form class="tools" inline>
      <FormItem>
        <Button
          @click="getSmartReloadList"
          icon="md-refresh"
          type="primary"
          v-privilege="'smart-reload-search'"
        >刷新</Button>
      </FormItem>
    </Form>
    <Alert>
      <h3>SmartReload介绍：</h3>
      <pre>
简介：SmartReload是一个可以在不重启进程的情况下动态重新加载配置或者执行某些预先设置的代码。

原理：
- Java后端会在项目启动的时候开启一个Daemon线程，这个Daemon线程会每隔几秒轮询t_smart_item表的状态。
- 如果【状态标识】与【上次状态标识】比较发生变化，会将参数传入SmartReload实现类，进行自定义操作。
用途：
· 用于刷新内存中的缓存
· 用于执行某些后门代码
· 用于进行Java热加载（前提是类结构不发生变化）
· 其他不能重启服务的应用
      </pre>
    </Alert>
    <Table :columns="columns" :data="tableData" :loading="tableLoading" border></Table>
    <Modal
      :closable="false"
      :mask-closable="false"
      @on-cancel="editModal=false"
      @on-ok="updateSmartReloadData"
      title="编辑"
      v-model="editModal"
    >
      <Form :label-width="80" :model="formData">
        <FormItem label="标签">
          <Input disabled="disabled" v-model="formData.tag" />
        </FormItem>
        <FormItem label="状态标识">
          <Input required v-model="formData.identification" />
        </FormItem>
        <FormItem label="Reload参数(非必填)">
          <Input
            :autosize="{minRows: 2,maxRows: 5}"
            placeholder="传入SmartReload的参数"
            type="textarea"
            v-model="formData.args"
          />
        </FormItem>
      </Form>
    </Modal>
    <Modal
      :closable="false"
      :mask-closable="false"
      title="执行结果"
      v-model="showResultModal"
      width="1000px"
    >
      <Table :columns="resultColumns" :data="resultData" height="500"></Table>
      <div slot="footer">
        <Button @click="showResultModal=false" type="primary">关闭</Button>
      </div>
    </Modal>
  </Card>
</template>
<script>
import { smartReloadApi } from '@/api/smart-reload';
export default {
  name: 'SmartReloadList',
  components: {},
  props: {},
  data() {
    return {
      //表格loading
      tableLoading: false,
      //reload结果弹出框
      showResultModal: false,
      // 修改弹窗
      editModal: false,
      formData: {
        tag: '',
        identification: 0,
        args: ''
      },
      tableData: [],
      columns: [
        {
          title: '标签',
          key: 'tag'
        },
        {
          title: '状态标识',
          key: 'identification'
        },
        {
          title: '更新时间',
          key: 'updateTime'
        },
        {
          title: '创建时间',
          key: 'createTime'
        },
        {
          title: '操作',
          width: 200,
          align: 'center',
          render: (h, param) => {
            return this.$tableAction(h, [
              {
                title: '执行Reload',
                directives: [
                  {
                    name: 'privilege',
                    value: 'smart-reload-update'
                  }
                ],
                action: () => {
                  this.editModal = true;
                  this.formData.tag = param.row.tag;
                  this.formData.identification = param.row.identification;
                }
              },
              {
                title: '查看结果',
                directives: [
                  {
                    name: 'privilege',
                    value: 'smart-reload-result'
                  }
                ],
                action: () => {
                  this.getSmartReloadResult(param.row.tag);
                }
              }
            ]);
            s;
          }
        }
      ],
      resultData: [],
      resultColumns: [
        {
          type: 'expand',
          width: 50,
          render: (h, params) => {
            return h('pre', {}, params.row.exception);
          }
        },
        {
          title: '标签',
          key: 'tag',
          width: 150
        },
        {
          title: '状态标识',
          key: 'identification'
        },
        {
          title: '参数',
          key: 'args',
          width: 100
        },
        {
          title: '结果',
          key: 'result',
          render: (h, params) => {
            if (params.row.result) {
              return h(
                'span',
                {
                  style: {
                    color: '#19be6b'
                  }
                },
                '成功'
              );
            } else {
              return h(
                'span',
                {
                  style: {
                    color: '#ed4014'
                  }
                },
                '失败'
              );
            }
          }
        },
        {
          title: '异常信息',
          key: 'exception',
          width: 200,
          render: (h, params) => {
            if (params.row.result) {
              return h(
                'span',
                {
                  style: {
                    color: '#19be6b'
                  }
                },
                '无异常信息'
              );
            } else {
              return h(
                'span',
                {
                  style: {
                    color: '#ed4014'
                  }
                },
                '有异常信息，请点开Expand'
              );
            }
          }
        },
        {
          title: '创建时间',
          key: 'createTime',
          width: 180
        }
      ]
    };
  },
  computed: {},
  watch: {},
  filters: {},
  created() {},
  mounted() {
    this.getSmartReloadList();
  },
  methods: {
    // 查询
    async getSmartReloadList() {
      try {
        this.tableLoading = true;
        let result = await smartReloadApi.getSmartReloadList();
        this.tableData = result.data;
        this.tableLoading = false;
      } catch (e) {
        //TODO zhuoda sentry
        console.error(e);
        this.tableLoading = false;
      }
    },
    // 修改
    async updateSmartReloadData() {
      if (this.formData.identification) {
        this.$Spin.show();
        let result = await smartReloadApi.updateSmartReloadData(this.formData);
        this.$Spin.hide();
        this.$Message.success('更新成功');
        this.formData = {};
        this.getSmartReloadList();
      } else {
        this.$Message.error('状态标示不能为空');
        this.show = true;
      }
    },
    // 获取执行结果
    async getSmartReloadResult(tag) {
      this.$Spin.show();
      this.showResultModal = true;
      let result = await smartReloadApi.getSmartReloadResult(tag);
      this.$Spin.hide();
      this.resultData = result.data;
    }
  }
};
</script>
