<template>
  <div>
    <Card class="warp-card" dis-hover>
      <Form class="tools" inline>
        <FormItem>
          <Button
            @click="refresh"
            icon="md-refresh"
            type="primary"
            v-privilege="'task-refresh'"
          >刷新任务</Button>
        </FormItem>
        <FormItem>
          <Button @click="addModal=true" icon="md-add" type="primary" v-privilege="'task-add'">添加任务</Button>
        </FormItem>
      </Form>
      <Tables
        :columns="columns"
        :current="pageNum"
        :loading="loading"
        :page-size="pageSize"
        :pageShow="true"
        :total="pageTotal"
        :value="data"
        @on-change="changePage"
        border
        show-elevator
      ></Tables>
    </Card>
    <!-- 编辑任务 -->
    <Modal
      :loading="updateLoading"
      @on-cancel="cancelUpdate"
      @on-ok="handleUpdate"
      title="编辑"
      v-model="editModal"
    >
      <Form :label-width="80" :model="updateItem" :rules="updateValidate" ref="updateRef">
        <FormItem label="taskBean" prop="taskBean">
          <Input placeholder="请输入taskBean(必填)" v-model="updateItem.taskBean"></Input>
        </FormItem>
        <FormItem label="taskCron" prop="taskCron">
          <Input placeholder="请输入taskCron(必填)" v-model="updateItem.taskCron"></Input>
        </FormItem>
        <FormItem label="任务标题" prop="taskName">
          <Input placeholder="请输入任务标题(必填)" v-model="updateItem.taskName"></Input>
        </FormItem>
        <FormItem label="任务参数" prop="taskParams">
          <Input placeholder="请输入任务参数(必填)" v-model="updateItem.taskParams"></Input>
        </FormItem>
      </Form>
    </Modal>
    <!-- 添加任务 -->
    <Modal
      :loading="saveLoading"
      @on-cancel="cancelSave"
      @on-ok="handleSave"
      title="添加"
      v-model="addModal"
    >
      <Form :label-width="80" :model="saveItem" :rules="saveValidate" ref="saveRef">
        <FormItem label="taskBean" prop="taskBean">
          <Input placeholder="请输入taskBean(必填)" v-model="saveItem.taskBean"></Input>
        </FormItem>
        <FormItem label="taskCron" prop="taskCron">
          <Input placeholder="请输入taskCron(必填)" v-model="saveItem.taskCron"></Input>
        </FormItem>
        <FormItem label="任务标题" prop="taskName">
          <Input placeholder="请输入任务标题(必填)" v-model="saveItem.taskName"></Input>
        </FormItem>
        <FormItem label="任务参数" prop="taskParams">
          <Input placeholder="请输入任务参数" v-model="saveItem.taskParams"></Input>
        </FormItem>
      </Form>
    </Modal>
    <!-- 任务日志 -->
    <Modal :closable="false" :mask-closable="false" title="任务运行日志" v-model="logModal" width="850px">
      <Row class="log-modal-header" slot="header">
        <Col :span="18">
          <span>任务调度日志</span>
        </Col>
      </Row>
      <Table border :columns="logColumns" :data="logData"></Table>
       <Page show-total @on-change="changeLogPageNum" :current="logPageNum" :total="logTotal" show-sizer />
      <div slot="footer">
        <Button @click="closeLog" type="primary">关闭</Button>
      </div>
    </Modal>
  </div>
</template>

<script>
import Tables from '@/components/tables';
import { taskApi } from '@/api/task-manage';
export default {
  name: 'taskManage',
  components: {
    Tables
  },
  props: {},
  data() {
    return {
      hasLog: false,
      editModal: false,
      addModal: false,
      logModal: false,
      // 当前所查看的任务Id
      taskId: '',
      // table是否loading
      loading: true,
      saveLoading: true,
      updateLoading: true,
      pageNum: 1,
      pageSize: 10,
      pageTotal: 0,
      logPageNum: 1,
      logPageSize: 10,
      logTotal: 0,
      updateItem: {
        taskBean: '',
        taskCron: '',
        taskName: '',
        taskParams: '',
        taskStatus: ''
      },
      saveItem: {
        taskBean: '',
        taskCron: '',
        taskName: '',
        taskParams: '',
        taskStatus: ''
      },
      // table表头
      columns: [
        {
          title: 'ID',
          width: 60,
          key: 'id'
        },
        {
          title: 'taskBean',
          key: 'taskBean'
        },
        {
          title: 'taskCron',
          key: 'taskCron'
        },
        {
          title: '任务标题',
          key: 'taskName'
        },
        {
          title: '任务参数',
          key: 'taskParams'
        },
        {
          title: '任务状态',
          key: 'taskStatus',
          render: (h, params) => {
            return h('span', params.row.taskStatus === 0 ? '正常' : '暂停');
          }
        },
        {
          title: '操作',
          key: 'action',
          align: 'center',
          className: 'action-hide',
          width: 180,
          render: (h, params) => {
            return this.$tableAction(h, [
              {
                title: '编辑',
                directives: [
                  {
                    name: 'privilege',
                    value: 'taskUpdate'
                  }
                ],
                action: () => {
                  this.updateItem.taskBean = params.row.taskBean;
                  this.updateItem.id = params.row.id;
                  this.updateItem.taskCron = params.row.taskCron;
                  this.updateItem.taskName = params.row.taskName;
                  this.updateItem.taskParams = params.row.taskParams;
                  this.updateItem.taskStatus = params.row.taskStatus;
                  this.editModal = true;
                }
              },
              {
                title: '删除',
                directives: [
                  {
                    name: 'privilege',
                    value: 'taskDelete'
                  }
                ],
                action: () => {
                  this.$Modal.confirm({
                    title: '删除',
                    content: '确认删除该条任务么？',
                    onOk: () => {
                      this.deleteTask(params.row.id);
                    }
                  });
                }
              },
              {
                title: '立即开始任务',
                directives: [
                  {
                    name: 'privilege',
                    value: 'taskRun'
                  }
                ],
                action: () => {
                  this.$Modal.confirm({
                    title: '确认',
                    content: '确认立即开始任务么？',
                    onOk: () => {
                      this.controlTask('RUN', params.row.id);
                    }
                  });
                }
              },
              {
                title: '暂停任务',
                directives: [
                  {
                    name: 'privilege',
                    value: 'taskPause'
                  }
                ],
                action: () => {
                  this.$Modal.confirm({
                    title: '确认',
                    content: '确认暂停任务么？',
                    onOk: () => {
                      this.controlTask('PAUSE', params.row.id);
                    }
                  });
                }
              },
              {
                title: '恢复任务',
                directives: [
                  {
                    name: 'privilege',
                    value: 'taskResume'
                  }
                ],
                action: () => {
                  this.$Modal.confirm({
                    title: '确认',
                    content: '确认恢复任务么？',
                    onOk: () => {
                      this.controlTask('RESUME', params.row.id);
                    }
                  });
                }
              },
              {
                title: '查看日志',
                directives: [
                  {
                    name: 'privilege',
                    value: 'taskQueryLog'
                  }
                ],
                action: () => {
                  this.taskId = params.row.id;
                  this.logPageNum = 1;
                  this.getTaskLog();
                }
              }
            ]);
          }
        }
      ],
      //   日志表格表头
      logColumns: [
        {
          type: 'index',
          width: 60,
          align: 'center'
        },
        {
          title: '任务名',
          key: 'taskName'
        },
        {
          title: '任务参数',
          key: 'taskParams'
        },
        {
          title: '主机IP',
          key: 'ipAddress'
        },
        {
          title: '处理结果',
          key: 'processStatus',
          render: (h, params) => {
            return h('span', params.row.processStatus === 0 ? '成功' : '失败');
          }
        },
        {
          title: '处理时长',
          key: 'processDuration',
          render: (h, params) => {
            return h('span', params.row.processDuration + 'ms');
          }
        },
        {
          title: '失败日志',
          key: 'processLog',
          render: (h, params) => {
            return h(
              'span',
              params.row.processLog ? params.row.processLog : '无'
            );
          }
        },
        {
          title: '运行时间',
          width: 190,
          key: 'createTime'
        }
      ],
      // table数据
      data: [],
      //   日志数据
      logData: [],
      updateValidate: {
        taskBean: [{ required: true, message: '请输入值', trigger: 'blur' }],
        taskCron: [{ required: true, message: '请输入值', trigger: 'blur' }],
        taskName: [
          { required: true, message: '请输入任务标题', trigger: 'blur' }
        ]
      },
      saveValidate: {
        taskBean: [{ required: true, message: '请输入值', trigger: 'blur' }],
        taskCron: [{ required: true, message: '请输入值', trigger: 'blur' }],
        taskName: [
          { required: true, message: '请输入任务标题', trigger: 'blur' }
        ]
      }
    };
  },
  mounted() {
    this.getTaskList();
  },
  methods: {
    // 查询任务调度列表
    async getTaskList() {
      try {
        this.loading = true;
        let result = await taskApi.getTaskList({
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
      } finally {
        this.loading = false;
      }
    },
    // 查询任务运行日志
    async getTaskLog() {
      let result = await taskApi.getTaskLog({
        pageNum: this.logPageNum,
        pageSize: this.logPageSize,
        taskId: this.taskId
      });
      this.logData = result.data.list;
      this.logTotal = result.data.total;
      this.logPageNum = result.data.pageNum;
      this.logPageSize = result.data.pageSize;
      this.hasLog = true;
      this.logModal = true;
    },
    // 分页
    changePage(pageNum) {
      this.pageNum = pageNum;
      this.getTaskList();
    },
    // 分页
    changePageSize(pageSize) {
      this.pageNum = 1;
      this.pageSize = pageSize;
      this.getTaskList();
    },
    // 日志分页
    changeLogPageNum(pageNum) {
      this.logPageNum = pageNum;
      this.getTaskLog();
    },
    // 删除任务
    async deleteTask(id) {
      this.$Spin.show();
      try{
        let result = await taskApi.deleteTask(id);
        this.$Message.success('删除任务成功!');
        this.getTaskList();
      } catch (error) {
        console.error(e);
      } finally {
        this.$Spin.hide();
      }
    },
    // 操作任务
    async controlTask(type, id) {
      this.$Spin.show();
      try{
        switch (type) {
          case 'RUN':
            await taskApi.updateTaskRun(id);
            break;
          case 'PAUSE':
            await taskApi.updateTaskPause(id);
            break;
          case 'RESUME':
            await taskApi.updateTaskResume(id);
            break;
        }
        this.$Message.success('操作成功');
        this.getTaskList();
      } catch (error) {
        console.error(e);
      } finally {
        this.$Spin.hide();
      }
    },
    // 触发更新
    handleUpdate() {
      this.$refs['updateRef'].validate(valid => {
        if (valid) {
          this.$Spin.show();
          try{
            this.updateTask();
          } catch (error) {
            console.error(e);
          } finally {
            this.$Spin.hide();
          }
        } else {
          this.$Message.success('验证信息不通过');
        }
      });
    },
    // 修改任务
    async updateTask() {
      try {
        this.updateLoading = true;
        let result = await taskApi.addOrUpdateTask(this.updateItem);
        this.updateLoading = false;
        this.$Message.success('修改成功');
        this.getTaskList();
        this.cancelUpdate();
      } catch (e) {
        //TODO zhuoda sentry
        console.error(e);
      } finally {
        this.updateLoading = false;
      }
    },
    // 保存任务
    handleSave() {
      try {
        this.$refs['saveRef'].validate(valid => {
          this.saveLoading = true;
          if (valid) {
            this.saveTask();
          } else {
            this.saveLoading = false;
            this.$nextTick(() => {
              this.saveLoading = true;
            });
          }
        });
      } catch (e) {
        //TODO zhuoda sentry
        console.error(e);
      } finally {
        this.saveLoading = false;
      }
    },
    // 保存任务
    async saveTask() {
      try {
        let result = await taskApi.addOrUpdateTask(this.saveItem);
        this.saveLoading = false;
        this.$Message.success('添加成功');
        this.getTaskList();
        this.cancelSave();
      } catch (e) {
        //TODO zhuoda sentry
        console.error(e);
        this.saveLoading = false;
      }
    },
    // 取消保存
    cancelSave() {
      this.saveItem = {};
      // 清空form规则检查
      this.$refs['saveRef'].resetFields();
      this.addModal = false;
    },
    // 取消修改
    cancelUpdate() {
      this.updateItem = {};
      // 清空form规则检查
      this.$refs['updateRef'].resetFields();
      this.editModal = false;
    },
    // 关闭日志
    closeLog() {
      this.logData = [];
      this.logPageNum = 1;
      this.hasLog = false;
      this.logModal = false;
    },
    // 刷新
    refresh() {
      this.pageNum = 1;
      this.getTaskList();
    }
  }
};
</script>

<style lang="less" scoped>
.log-modal-header {
  height: 25px;
  line-height: 25px;
  font-size: 14px;
  color: #17233d;
  font-weight: bold;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.log-modal-header-btn-group {
  text-align: right;
  button {
    margin: 0 5px;
    span {
      margin: 0;
    }
  }
}
</style>
