<template>
  <div>
    <!--Row 员工管理 start-->
    <Row :gutter="10">
      <!--Col 左侧公司组织列表 start-->
      <Col :lg="5" :md="8">
        <Card class="warp-card" dis-hover>
          <p slot="title">公司组织</p>
          <Row>
            <Input
              @on-change="getListEmployeeByDepartmentName"
              placeholder="输入部门名称"
              v-model="departmentName"
            />
          </Row>
          <Tree
            :data="departmentGroup"
            :render="renderDepartmentTreeButton"
            style="height: 485px;overflow-x: scroll"
          ></Tree>
        </Card>
      </Col>
      <!--Col 左侧公司组织列表 end-->
      <!--EmployeeTable 表单列表模块 start-->
      <EmployeeTable :departments="departmentGroup" :selectDepartment="selectDepartment"></EmployeeTable>
      <!--EmployeeTable 表单列表模块 end-->
      <!--Modal 添加部门弹窗 start-->
      <Modal
        @on-visible-change="changeVisibleOperationModal"
        title="添加部门"
        v-model="isShowOperationModal"
      >
        <Form :label-width="80">
          <FormItem label="名称" required>
            <Input
              @on-keydown="departmentParam.name=departmentParam.name.replace(/^ +| +$/g,'')"
              @on-keyup="departmentParam.name=departmentParam.name.replace(/^ +| +$/g,'')"
              v-model="departmentParam.name"
            ></Input>
          </FormItem>
          <FormItem class="selectClass" label="负责人">
            <Input
              @click.native="showTree"
              placeholder="请选择负责人"
              readonly
              v-model="departmentParam.managerName"
            >
              <Icon slot="suffix" type="ios-arrow-down" v-if="!isShowTree" />
              <Icon slot="suffix" type="ios-arrow-up" v-else />
            </Input>
            <div class="departmentWrap">
              <DepartmentEmployeeTree
                :selectEmployeeId="selectEmployeeId"
                @on-select="selectDepartmentEmployee"
                ref="departmentEmployeeTree"
              ></DepartmentEmployeeTree>
            </div>
          </FormItem>
          <FormItem label="上级部门" required>
            <Input disabled v-model="departmentParam.parentName" />
          </FormItem>
        </Form>
        <div slot="footer">
          <Button @click="getOperationDepartmentData()" size="large" type="primary">确定</Button>
          <Button @click="hideDeleteAndOperationModal" size="large" type="error">取消</Button>
        </div>
      </Modal>
      <!--Modal 添加部门弹窗 end-->
      <!--Modal 删除部门弹窗 start-->
      <Modal title="删除确认" v-model="isShowDeleteModal">
        <p style="font-size: 16px;">
          确定要将
          <span style="color: red;font-size: 16px;">{{departmentParam.name}}</span>
          部门删除吗？
        </p>
        <div slot="footer">
          <Button @click="deleteDepartment" size="large" type="primary">确定</Button>
          <Button @click="hideDeleteAndOperationModal" size="large" type="error">取消</Button>
        </div>
      </Modal>
      <!--Modal 删除部门弹窗 end-->
      <!--Modal 公司设置弹窗 start-->
      <Modal title="公司设置" v-model="isShowCompanySettingModal">
        <Form :label-width="80">
          <FormItem label="公司名称" required>
            <Input
              :disabled=" departmentParam.id!=null"
              @on-keydown="departmentParam.name=departmentParam.name.replace(/^ +| +$/g,'')"
              @on-keyup="departmentParam.name=departmentParam.name.replace(/^ +| +$/g,'')"
              v-model="departmentParam.name"
            />
          </FormItem>
        </Form>
        <div slot="footer">
          <Button
            @click="getOperationDepartmentData()"
            size="large"
            type="primary"
            v-if=" !departmentParam.id"
          >确定</Button>
          <Button @click="hideCompanySettingModal" size="large" type="error">取消</Button>
        </div>
      </Modal>
      <!--Modal 公司设置弹窗 end-->
    </Row>
    <!--Row 员工管理 end-->
  </div>
</template>
<script>
import $ from 'jquery';
import { departmentApi } from '@/api/department';
import EmployeeTable from './components/employee-table/employee-table';
import DepartmentEmployeeTree from '../components/department-employee-tree/department-employee-tree';
export default {
  name: 'RoleEmployeeManage',
  components: {
    EmployeeTable,
    DepartmentEmployeeTree
  },
  props: {},
  data() {
    return {
      departmentName: '',
      isShowTree: false,
      // 选中的部门
      selectDepartment: {},
      isShowOperationModal: false,
      isShowDeleteModal: false,
      // 部门参数
      departmentParam: {
        id: '',
        name: '',
        managerId: null,
        managerName: '',
        parentName: '',
        parentId: '',
        type: 1
      },
      selectEmployeeId: null,
      // 备用的部门参数
      departmentParamBackup: {},
      isShowCompanySettingModal: false,
      // 员工table 表格样式功能
      departmentGroup: [
        {
          name: '',
          expand: true,
          render: (h, { root, node, data }) => {
            let newName = data.name;
            if (newName.length > 8) {
              newName = data.name.substring(0, 8) + '...';
            }
            if (!data || Object.keys(data).length === 0 || !data.id) {
              return h('span', '');
            }
            return h(
              'Tooltip',
              {
                props: {
                  placement: 'right'
                },
                style: { fontSize: '12px' }
              },
              [
                h(
                  'span',
                  {
                    style: {
                      display: 'inline-block'
                    }
                  },
                  [
                    h('span', [
                      h(
                        'div',
                        {
                          props: {
                            // content:'123',
                            // placement: 'top'
                          },
                          style: { fontSize: '12px' }
                        },
                        [
                          h('Icon', {
                            props: {
                              type: 'md-cube'
                            },
                            style: {
                              marginRight: '8px'
                            }
                          }),
                          h(
                            'Button',
                            {
                              props: Object.assign({}),
                              class: ['departmentSelect'],
                              style: {
                                border: 'none',
                                background: '#ffffff',
                                padding: '4px 5px'
                              },
                              on: {
                                click: event => {
                                  this.loadEmployeeTable(
                                    event,
                                    root,
                                    node,
                                    data
                                  );
                                }
                              }
                            },
                            newName
                          )
                        ]
                      )
                    ])
                  ]
                ),
                h(
                  'div',
                  {
                    slot: 'content'
                  },
                  [
                    h(
                      'a',
                      {
                        props: {},
                        style: {
                          color: '#fff',
                          margin: '0 5px 5px'
                        },
                        directives: [
                          {
                            name: 'privilege',
                            value: 'addDepartment'
                          }
                        ],
                        on: {
                          click: () => {
                            this.addOrUpdataDepartment(data, false);
                          }
                        }
                      },
                      '添加'
                    )
                  ]
                ),
                h(
                  'div',
                  {
                    slot: 'content'
                  },
                  [
                    h(
                      'a',
                      {
                        props: {},
                        style: {
                          color: '#fff',
                          margin: '0 5px 5px'
                        },
                        directives: [
                          {
                            name: 'privilege',
                            value: 'update-department'
                          }
                        ],
                        on: {
                          click: () => {
                            data.parentId = 0;
                            this.addOrUpdataDepartment(data, true);
                          }
                        }
                      },
                      '编辑'
                    )
                  ]
                )
              ]
            );
          },
          children: []
        }
      ]
    };
  },
  computed: {},
  watch: {},
  filters: {},
  created() {},
  mounted() {
    Object.assign(this.departmentParamBackup, this.departmentParam);
    this.getListEmployeeByDepartmentName();
    let self = this;
    $('.selectClass').click(event => {
      event = event || window.event;
      event.stopPropagation();
    });

    // 点击层外，隐藏这个层。由于层内的事件停止了冒泡，所以不会触发这个事件
    $(document).click(function(e) {
      self.isShowTree = false;
      $('.departmentWrap').hide();
    });
  },
  beforeCreate() {},
  beforeMount() {},
  beforeUpdate() {},
  updated() {},
  beforeDestroy() {},
  destroyed() {},
  activated() {},
  methods: {
    // 初始化加载数据
    async getListEmployeeByDepartmentName() {
      this.$Spin.show();
      let result = await departmentApi.getListEmployeeByDepartmentName(
        this.departmentName
      );
      this.$Spin.hide();
      let data = result.data;
      this.departmentGroup[0].id = null;
      this.departmentGroup[0].name = '';
      this.departmentGroup[0].children = [];
      this.departmentGroup[0].organizationId = [];
      this.departmentGroup[0].organizationName = [];
      this.departmentGroup[0].type = 1;
      if (data && data[0]) {
        let dateFirst = data[0];
        this.departmentGroup[0].id = dateFirst.id;
        this.departmentGroup[0].name = dateFirst.name;
        this.departmentGroup[0].children = dateFirst.children;
        this.departmentGroup[0].organizationId = dateFirst.organizationId;
        this.departmentGroup[0].organizationName = dateFirst.organizationName;
        this.departmentGroup[0].type = dateFirst.type;
      }
    },
    // 渲染部门树形图功能按钮
    renderDepartmentTreeButton(h, { root, node, data }) {
      let newName = data.name;
      if (newName.length > 8) {
        newName = data.name.substring(0, 8) + '...';
      }
      let icon = '';
      if (data.type === 1) {
        icon = 'md-cube';
      } else {
        icon = 'md-menu';
      }
      let buttonList = [
        h(
          'div',
          {
            style: {
              margin: '0 5px 5px 5px'
            }
          },
          [
            h(
              'a',
              {
                props: {},
                style: {
                  color: '#fff',
                  margin: '0 5px 5px'
                },
                directives: [
                  {
                    name: 'privilege',
                    value: 'update-department'
                  }
                ],
                on: {
                  click: () => {
                    this.addOrUpdataDepartment(data, true);
                  }
                }
              },
              '编辑'
            )
          ]
        ),
        h(
          'div',
          {
            style: {
              margin: '0 5px 5px 5px'
            }
          },
          [
            h(
              'a',
              {
                props: {},
                style: {
                  color: '#fff',
                  margin: '0 5px 5px'
                },
                directives: [
                  {
                    name: 'privilege',
                    value: 'add-department'
                  }
                ],
                on: {
                  click: () => {
                    this.addOrUpdataDepartment(data, false);
                  }
                }
              },
              '添加'
            )
          ]
        ),
        h(
          'div',
          {
            style: {
              margin: '0 5px 5px 5px'
            }
          },
          [
            h(
              'a',
              {
                props: {},
                style: {
                  color: '#fff',
                  margin: '0 5px 5px'
                },
                directives: [
                  {
                    name: 'privilege',
                    value: 'delete-department'
                  }
                ],
                on: {
                  click: () => {
                    this.showDeleteModal(data);
                  }
                }
              },
              '删除'
            )
          ]
        )
      ];

      if (data.preId) {
        buttonList.push(
          h(
            'div',
            {
              style: {
                margin: '0 5px 5px 5px'
              }
            },
            [
              h(
                'a',
                {
                  props: {},
                  style: {
                    color: '#fff',
                    margin: '0 5px 5px'
                  },
                  directives: [
                    {
                      name: 'privilege',
                      value: 'delete-department'
                    }
                  ],
                  on: {
                    click: () => {
                      this.upOrDownDepartment(data.id, data.preId);
                    }
                  }
                },
                '上移'
              )
            ]
          )
        );
      }
      if (data.nextId) {
        buttonList.push(
          h(
            'div',
            {
              style: {
                margin: '0 5px 5px 5px'
              }
            },
            [
              h(
                'a',
                {
                  props: {},
                  style: {
                    color: '#fff',
                    margin: '0 5px 5px'
                  },
                  directives: [
                    {
                      name: 'privilege',
                      value: 'delete-department'
                    }
                  ],
                  on: {
                    click: () => {
                      this.upOrDownDepartment(data.id, data.nextId);
                    }
                  }
                },
                '下移'
              )
            ]
          )
        );
      }
      if (data.parentId && data.parentId !== 1) {
        buttonList.push(
          h(
            'div',
            {
              style: {
                margin: '0 5px 5px 5px'
              }
            },
            [
              h(
                'a',
                {
                  props: {},
                  style: {
                    color: '#fff',
                    margin: '0 5px 5px'
                  },
                  directives: [
                    {
                      name: 'privilege',
                      value: 'delete-department'
                    }
                  ],
                  on: {
                    click: () => {
                      this.upDepartmentGrade(data.id);
                    }
                  }
                },
                '升级'
              )
            ]
          )
        );
      }

      if (data.preId) {
        buttonList.push(
          h(
            'div',
            {
              style: {
                margin: '0 5px 5px 5px'
              }
            },
            [
              h(
                'a',
                {
                  props: {},
                  style: {
                    color: '#fff',
                    margin: '0 5px 5px'
                  },
                  directives: [
                    {
                      name: 'privilege',
                      value: 'delete-department'
                    }
                  ],
                  on: {
                    click: () => {
                      this.downDepartmentGrade(data.id, data.preId);
                    }
                  }
                },
                '降级'
              )
            ]
          )
        );
      }
      return h(
        'Tooltip',
        {
          props: {
            placement: 'right'
          },
          style: { fontSize: '12px' }
        },
        [
          h(
            'span',
            {
              style: {
                display: 'inline-block'
              }
            },
            [
              h('span', [
                h(
                  'div',
                  {
                    props: {
                      // content:'123',
                      // placement: 'top'
                    },
                    style: { fontSize: '12px' }
                  },
                  [
                    h('Icon', {
                      props: {
                        type: icon
                      },
                      style: {
                        marginRight: '8px'
                      }
                    }),
                    h(
                      'Button',
                      {
                        props: Object.assign({}),
                        class: ['departmentSelect'],
                        style: {
                          border: 'none',
                          background: '#ffffff',
                          padding: '4px 5px'
                        },
                        on: {
                          click: event => {
                            console.log('11');
                            this.loadEmployeeTable(event, root, node, data);
                          }
                        }
                      },
                      newName
                    )
                  ]
                )
              ])
            ]
          ),
          h(
            'div',
            {
              slot: 'content'
            },
            buttonList
          )
        ]
      );
    },
    // 选中部门 更新员工table
    loadEmployeeTable(event, root, node, data) {
      $('.departmentSelect').css({ background: '#ffffff', color: 'black' });
      let target = event.target;
      let tagName = target.tagName;
      if (tagName !== 'BUTTON') {
        target.parentNode.style.backgroundColor = '#5cadff';
        target.parentNode.style.color = '#ffffff';
      } else {
        target.style.backgroundColor = '#5cadff';
        target.style.color = '#ffffff';
      }
      this.selectDepartment = data;
    },
    // 关闭模态框
    hideDeleteAndOperationModal() {
      this.isShowDeleteModal = false;
      this.isShowOperationModal = false;
    },
    // 隐藏公司设置弹窗
    hideCompanySettingModal() {
      this.isShowCompanySettingModal = false;
    },
    // 获取部门数据
    async getOperationDepartmentData() {
      if (!this.departmentParam.name) {
        this.$Message.error('名称不能为空');
        return;
      }
      this.$Spin.show();
      let result;
      if (this.departmentParam.id) {
        result = await departmentApi.updateDepartment(this.departmentParam);
      } else {
        result = await departmentApi.addDepartment(this.departmentParam);
      }
      this.$Spin.hide();
      this.$Message.success('操作成功');
      this.getListEmployeeByDepartmentName();
      this.hideDeleteAndOperationModal();
      this.hideCompanySettingModal();
    },
    // 删除部门
    async deleteDepartment() {
      this.$Spin.show();
      let result = await departmentApi.deleteDepartment(
        this.departmentParam.id
      );
      this.$Message.success('删除成功');
      this.$Spin.hide();
      this.getListEmployeeByDepartmentName();
      this.hideDeleteModal();
    },
    // 添加或编辑部门 并显示弹窗
    addOrUpdataDepartment(data, update) {
      this.selectEmployeeId = null;
      if (update) {
        this.departmentParam = Object.assign({}, data);
        this.selectEmployeeId = data.managerId;
      } else {
        this.departmentParam = {
          parentName: data.name,
          parentId: data.id,
          type: 1
        };
      }
      this.isShowOperationModal = true;
    },
    // 显示删除弹窗
    showDeleteModal(val) {
      let data;
      if (!val) {
        data = Object.assign({}, this.departmentGroup[0]);
      } else {
        data = Object.assign({}, val);
      }
      this.departmentParam = {
        id: data.id,
        name: data.name
      };
      this.isShowDeleteModal = true;
    },
    // 关闭删除弹窗
    hideDeleteModal() {
      this.departmentParam = Object.assign({}, this.departmentParamBackup);
      this.isShowDeleteModal = false;
    },
    // 部门树形图的显示与隐藏
    showTree() {
      if (this.isShowTree) {
        this.isShowTree = false;
        $('.departmentWrap').hide();
      } else {
        this.isShowTree = true;
        $('.departmentWrap').show();
        this.$refs.departmentEmployeeTree.resetSearch();
      }
    },
    // 选中树形一项
    selectDepartmentEmployee(emp) {
      this.$set(this.departmentParam, 'managerId', emp.id);
      this.$set(this.departmentParam, 'managerName', emp.name);
      this.isShowTree = false;
      $('.departmentWrap').hide();
    },
    // 添加部门弹窗 显示隐藏监听
    changeVisibleOperationModal(showStatus) {
      if (!showStatus) {
        this.isShowTree = false;
        $('.departmentWrap').hide();
        this.$refs.departmentEmployeeTree.generateTreeData();
      }
    },
    // 部门上移或者下移
    async upOrDownDepartment(departmentId, swapId) {
      this.$Spin.show();
      let result = await departmentApi.upOrDown(departmentId, swapId);
      this.$Spin.hide();
      this.$Message.success('操作成功');
      this.getListEmployeeByDepartmentName();
    },
    // 升级
    async upDepartmentGrade(departmentId) {
      this.$Spin.show();
      let result = await departmentApi.upGrade(departmentId);
      this.$Message.success('升级成功');
      this.$Spin.hide();
      this.getListEmployeeByDepartmentName();
    },
    // 降级
    async downDepartmentGrade(departmentId, preId) {
      this.$Spin.show();
      let result = await departmentApi.downGrade(departmentId, preId);
      this.$Message.success('降级成功');
      this.$Spin.hide();
      this.getListEmployeeByDepartmentName();
    }
  }
};
</script>

<style lang="less" scoped>
.ivu-tree-children {
  cursor: pointer;
  width: 100%;
}
.option-department {
  font-size: 14px;
  padding: 5px;
  cursor: pointer;
}
.option-department:hover {
  background-color: rgba(5, 170, 250, 0.2);
}
.departmentWrap {
  position: absolute;
  background-color: #ffffff;
  padding: 5px;
  border: 1px solid #dedede;
  width: 100%;
  z-index: 9;
  display: none;
  height: 250px;
  overflow-y: scroll;
}
</style>
