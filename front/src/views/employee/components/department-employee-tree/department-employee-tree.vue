<template>
  <div style="height: 330px;">
    <Row>
      <Col span="24">
        <Input
          @on-change="filterEmployee"
          placeholder="请输入员工名称"
          style="max-width: 300px"
          v-if="!isDepartment"
          v-model="searchKeywords"
        />
        <Tree :data="treeData"></Tree>
      </Col>
    </Row>
  </div>
</template>

<script>
import { departmentApi } from '@/api/department';
import DepartmentEmployeeTreeItem from '../department-employee-tree-item/department-employee-tree-item.vue';
import Vue from 'vue';
export default {
  name: 'DepartmentEmployeeTree',
  components: {
    DepartmentEmployeeTreeItem
  },
  props: {
    // true 查部门
    isDepartment: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      // 查询部门及员工列表
      originalData: [],
      // 搜索内容
      searchKeywords: '',
      eventbus: new Vue(),
      // 当前树形选中 部门或人员
      currentSelect: null,
      // 生成适配的树形数据
      treeData: []
    };
  },
  computed: {},
  watch: {},
  filters: {},
  created() {},
  mounted() {
    this.getDepartmentEmployeeList();
  },
  beforeCreate() {},
  beforeMount() {},
  beforeUpdate() {},
  updated() {},
  beforeDestroy() {},
  destroyed() {},
  activated() {},
  methods: {
    // 重置搜索内容
    resetSearch() {
      this.searchKeywords = '';
      this.getDepartmentEmployeeList();
    },
    // 获取当前选择项
    getSelect() {
      return this.currentSelect;
    },
    // 输入框输入后 搜索
    filterEmployee() {
      this.originalData.forEach(department => {
        this.recursionFilterEmployee(department);
      });
      this.generateTreeData();
    },
    // 根据searchKeywords搜索成员和部门
    recursionFilterEmployee(department) {
      if (department.employees) {
        department.employees.forEach(e => {
          if (
            this.searchKeywords === '' ||
            e.actualName.indexOf(this.searchKeywords) > -1
          ) {
            e.show = true;
          } else {
            e.show = false;
          }
        });
      }

      if (department.children) {
        department.children.forEach(item => {
          this.recursionFilterEmployee(item);
        });
      }
    },
    // 让所有部门展开所有子项
    filterDepartment() {
      this.originalData.forEach(department => {
        this.recursionFilterDepartment(department);
      });
      this.generateTreeData();
    },
    // 展开部门所有子项
    recursionFilterDepartment(department) {
      if (department.children) {
        department.children.forEach(e => {
          e.show = true;
          this.recursionFilterDepartment(e);
        });
      }
    },
    // 查询部门及员工列表
    async getDepartmentEmployeeList() {
      this.$Spin.show();
      let res = await departmentApi.getDepartmentEmployeeList();
      this.$Spin.hide();
      this.originalData = res.data;
      if (!this.isDepartment) {
        this.filterEmployee();
      } else {
        this.filterDepartment();
      }
    },
    // 生成树形图数据
    generateTreeData() {
      let tree = [];
      this.originalData.forEach(department => {
        let icon = department.type == 1 ? 'md-cube' : 'md-menu';
        let obj = Object.assign(
          {},
          {
            title: department.name,
            expand: true,
            children: this.constractTree(department),
            render: (h, { root, node, data }) => {
              return h(DepartmentEmployeeTreeItem, {
                props: {
                  isDepartment: this.isDepartment,
                  itemData: {
                    title: department.name,
                    icon: icon,
                    isEmployee: false,
                    id: department.id,
                    selectFunction: obj => {
                      if (this.isDepartment) {
                        this.currentSelect = obj;
                        this.eventbus.$emit('select', obj);
                        this.$emit('on-select', obj);
                      }
                    }
                  }
                },
                style: {
                  cursor: 'pointer'
                }
              });
            }
          }
        );
        tree.push(obj);
      });
      this.treeData = tree;
    },
    // 根据部门构建树形
    constractTree(department) {
      let children = [];
      if (department.children) {
        department.children.forEach(departmentChild => {
          if (this.isDepartment && !departmentChild.show) {
            return;
          }
          let icon = departmentChild.type == 1 ? 'md-cube' : 'md-menu';
          let obj = Object.assign(
            {},
            {
              title: departmentChild.name,
              expand: true,
              disabled: false,
              children: this.constractTree(departmentChild),
              render: (h, { root, node, data }) => {
                return h(DepartmentEmployeeTreeItem, {
                  props: {
                    isDepartment: this.isDepartment,
                    itemData: {
                      title: departmentChild.name,
                      icon: icon,
                      isEmployee: false,
                      id: departmentChild.id,
                      selectFunction: obj => {
                        this.currentSelect = obj;
                        this.eventbus.$emit('select', obj);
                        this.$emit('on-select', obj);
                      }
                    }
                  },
                  style: {
                    cursor: 'pointer'
                  }
                });
              }
            }
          );
          children.push(obj);
        });
      }
      if (!this.isDepartment && department.employees) {
        department.employees.forEach(employeeItem => {
          if (employeeItem.show) {
            let obj = Object.assign(
              {},
              {
                title: employeeItem.actualName,
                render: (h, { root, node, data }) => {
                  return h(DepartmentEmployeeTreeItem, {
                    props: {
                      isDepartment: this.isDepartment,
                      itemData: {
                        title: employeeItem.actualName,
                        icon: 'md-person',
                        isEmployee: true,
                        selected: false,
                        id: employeeItem.id,
                        eventbus: this.eventbus,
                        selectFunction: obj => {
                          this.currentSelect = obj;
                          this.eventbus.$emit('select', obj);
                          this.$emit('on-select', obj);
                        }
                      }
                    },
                    style: {
                      cursor: 'pointer'
                    }
                  });
                }
              }
            );
            children.push(obj);
          }
        });
      }
      return children;
    }
  }
};
</script>
<style lang="less" scoped>
</style>
