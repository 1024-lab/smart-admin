<template>
  <!--div tab切换成员列表部分 start-->
  <div>
    <Row>
      <Col span="16" style="margin:20px 0;font-size: 15px;color: #95a5a6;">管理拥有当前角色权限的人员列表</Col>
      <Col span="8" style="margin:20px 0; text-align: right;">
        <Button
          @click="addEmployee"
          type="primary"
          v-privilege="'add-employee-role'"
        >添加成员</Button>

             <Button
          @click="deleteEmployees()"
          style="margin-left:10px"
          type="error"
          v-privilege="'delete-employee-role-batch'"
        >批量移除</Button>
      </Col>
    </Row>
    <!--Table 表格列表 start-->
    <Table
      :columns="columns"
      :data="tableData"
      :loading="isShowTablesLoading"
      @on-selection-change="selectChange"
      border
      ref="selection"
    ></Table>
    <!--Table 表格列表 end-->
    <Row class="page" justify="end" type="flex">
      <Col order="2" span="24" style="text-align: right;margin-top:20px;">
        <Page
          :current="currentPage"
          :page-size="pageSize"
          :total="total"
          @on-change="changePage"
          show-elevator
        ></Page>
      </Col>
    </Row>
    <!--modal 添加成员 start-->
    <Modal
      @on-ok="confirmPrepAddEmployees()"
      style="min-width:1800px"
      title="添加成员"
      v-model="isShowEmployeeModal"
      width="700"
    >
      <Row class="shuttle-box">
        <!--Col 左侧员工列表 start-->
        <Col class="box" span="11">
          <Row>
            <Col class="title" span="24">员工列表</Col>
          </Row>
          <Row>
            <Col
              @dblclick.native="getCharge"
              class="no-scrollbar"
              id="goRight"
              span="24"
              style="height: 290px;overflow-y:scroll"
            >
              <DepartmentEmployeeTree ref="departmentEmployeeTree"></DepartmentEmployeeTree>
            </Col>
          </Row>
        </Col>
        <!--Col 左侧员工列表 end-->
        <!--Col 转移按钮 start-->
        <Col span="2" style="text-align: center;">
          <Icon
            @click.native="addPrepEmployees"
            size="30"
            style="line-height: 350px"
            type="md-arrow-round-forward"
          ></Icon>
        </Col>
        <!--Col 转移按钮 end-->
        <!--Col 右侧预添加列表 start-->
        <Col class="box" span="11">
          <Row>
            <Col class="title" span="24">成员列表</Col>
            <Col
              class="no-scrollbar"
              span="24"
              style="overflow-y:scroll;height: 290px;text-align: center"
            >
              <Row :key="index" v-for="(item,index) in prepAddEmployees">
                <Col span="24" style="font-size: 15px;text-align: center;">
                  <icon type="ios-people"></icon>
                  {{item.manageName}}
                  <Button @click.native="deletePrepEmployee(index)" icon="md-close" type="text"></Button>
                </Col>
              </Row>
            </Col>
          </Row>
        </Col>
        <!--Col 右侧预添加列表 end-->
      </Row>
    </Modal>
    <!--modal 添加成员 end-->
  </div>
  <!--div tab切换成员列表部分 end-->
</template>
<script>
import DepartmentEmployeeTree from '../../../components/department-employee-tree/department-employee-tree';
import { roleApi } from '@/api/role';

export default {
  name: 'RoleList',
  components: {
    DepartmentEmployeeTree
  },
  props: {
    // 角色id
    roleId: {
      type: Number,
      required: true,
      validator: value => {
        return value >= 0;
      }
    }
  },
  // 数据
  data() {
    return {
      // 是否显示添加成员弹窗
      isShowEmployeeModal: false,
      currentPage: 1,
      isShowTablesLoading: false,
      columns: [
        {
          type: 'selection',
          width: 60,
          align: 'center'
        },
        {
          title: '登录名',
          key: 'loginName'
        },
        {
          title: '姓名',
          key: 'actualName'
        },
        {
          title: '手机号',
          key: 'phone'
        },
        {
          title: '邮箱',
          key: 'email'
        },
        {
          title: '部门',
          key: 'departmentName'
        },
        {
          title: '操作',
          key: 'operation ',
          width: 100,
          align: 'center',
          className: 'action-hide',
          render: (h, params) => {
            return this.$tableAction(h, [
              {
                title: '移除',
                directives: [
                  {
                    name: 'privilege',
                    value: 'delete-employee-role'
                  }
                ],
                action: () => {
                  this.deleteEmployee(params.index);
                }
              }
            ]);
          }
        }
      ],
      employeeData: {
        // orderby:'',
        roleId: '',
        pageNum: 1,
        pageSize: 10,
        sort: ''
      },
      // 表格数据
      tableData: [],
      // 待添加的成员列表
      prepAddEmployees: [],
      // 提交添加成员数据
      submitPrepAddEmployeesData: {
        // 提交成员id列表
        employeeIds: [],
        // 当前roleId
        roleId: 0
      },
      // 批量删除的id列表
      deleteIds: [],
      total: 0,
      pageSize: 0
    };
  },
  computed: {},
  watch: {
    roleId(val) {
      if (val) {
        this.employeeData.roleId = this.roleId;
        this.getListEmployee(this.employeeData);
      }
    }
  },
  filters: {},
  created() {},
  mounted() {
    this.employeeData.roleId = this.roleId;
    this.getListEmployee(this.employeeData);
  },
  beforeCreate() {},
  beforeMount() {},
  beforeUpdate() {},
  updated() {},
  beforeDestroy() {},
  destroyed() {},
  activated() {},
  methods: {
    // 批量删除
    deleteEmployees() {
      let object = {};
      object.employeeIds = this.deleteIds.slice(0, this.deleteIds.length);
      object.roleId = this.roleId;
      if (object.employeeIds.length <= 0) {
        this.$Message.error('请先选择要移除的成员');
      } else {
        this.deleteEmployeeList(object); // 删除
      }
    },
    // 删除待添加列表中 人员
    deletePrepEmployee(index) {
      this.prepAddEmployees.splice(index, 1);
    },
    // 批量删除方法
    async deleteEmployeeList(param) {
      this.isShowTablesLoading = true;
      try {
        await roleApi.deleteEmployeeList(param);
        await this.getListEmployee(this.employeeData); // 刷新数据
      } catch (e) {
        //TODO zhuoda sentry
        console.error(e);
      } finally {
        this.isShowTablesLoading = false;
      }
    },
    // 选项框多选移除
    selectChange(selection) {
      this.deleteIds = [];
      for (let i = 0; i < selection.length; i++) {
        this.deleteIds.push(selection[i].id);
      }
      console.log(this.deleteIds);
    },
    // 移除当前项
    deleteEmployee(index) {
      let object = {};
      object.employeeId = this.tableData[index].id;
      object.roleId = this.roleId;
      this.deleteEmployeeRole(object);
    },
    // 删除角色成员方法
    async deleteEmployeeRole(param) {
      this.isShowTablesLoading = true;
      try {
        await roleApi.deleteEmployeeRole(param);
        this.$Message.success('移除成功');
        await this.getListEmployee(this.employeeData);
      } catch (e) {
        //TODO zhuoda sentry
        console.error(e);
      } finally {
        this.isShowTablesLoading = false;
      }
    },
    // 分页改变获取数据方法
    // 分页器
    changePage(number) {
      console.log(number);
      let object = {};
      object.roleId = this.roleId;
      this.currentPage = number;
      object.pageNum = number;
      object.pageSize = this.pageSize;
      object.sort = '';
      this.getListEmployee(object);
    },
    // 确定添加角色成员
    confirmPrepAddEmployees() {
      this.submitPrepAddEmployeesData.employeeIds = [];
      this.prepAddEmployees.forEach(e => {
        console.log(e);
        this.submitPrepAddEmployeesData.employeeIds.push(e.manageId);
      });
      console.log(this.prepAddEmployees);
      this.submitPrepAddEmployeesData.roleId = this.roleId;
      this.addEmployeeListRole(this.submitPrepAddEmployeesData);
      this.getListEmployee(this.employeeData); // 刷新表格
    },
    // 添加角色成员方法
    async addEmployeeListRole(param) {
      this.isShowTablesLoading = true;
      try {
        await roleApi.addEmployeeListRole(param);
        this.$Message.success('添加成功');
        this.employeeData.roleId = this.roleId;
        await this.getListEmployee(this.employeeData);
      } catch (e) {
        //TODO zhuoda sentry
        console.error(e);
      } finally {
        this.isShowTablesLoading = false;
      }
    },
    // 穿梭框穿梭方法
    addPrepEmployees() {
      let obj = this.$refs.departmentEmployeeTree.getSelect();
      let Obj = {};
      let notHave = true;
      Obj.manageName = obj.name;
      Obj.manageId = obj.id;
      for (let i = 0; i < this.prepAddEmployees.length; i++) {
        if (this.prepAddEmployees[i].manageId === Obj.manageId) {
          notHave = false;
          break;
        }
      }
      if (notHave === true) {
        notHave = false;
        this.submitPrepAddEmployeesData.employeeIds.push(Obj.manageId);
        this.prepAddEmployees.push(Obj);
      }
    },
    // 获取角色id对应的成员列表方法
    async getListEmployee(param) {
      this.isShowTablesLoading = true;
      try {
        let response = await roleApi.getListEmployee(param);
        this.roleList = response.data;
        this.total = response.data.total;
        this.pageSize = response.data.pageSize;
        this.tableData = this.roleList.list;
        await this.getListAllEmployee();
      } catch (e) {
        //TODO zhuoda sentry
        console.error(e);
      } finally {
        this.isShowTablesLoading = false;
      }
    },
    // 获取角色id对应的全部成员列表方法
    async getListAllEmployee() {
      this.$isShowTablesLoading = true;
      try {
        let response = await roleApi.getAllListEmployee(this.roleId);
        let list = response.data;
        this.prepAddEmployees = [];
        for (let i = 0; i < list.length; i++) {
          let object = {};
          object.manageName = list[i].actualName;
          object.manageId = list[i].id;
          this.prepAddEmployees.push(object);
        }
      } catch (e) {
        //TODO zhuoda sentry
        console.error(e);
      } finally {
        this.isShowTablesLoading = false;
      }
    },
    // 添加成员方法，
    addEmployee() {
      this.isShowEmployeeModal = true;
      this.getListAllEmployee();
      this.submitPrepAddEmployeesData.employeeIds = [];
    }
  }
};
</script>

<style lang="less" scoped >
.shuttle-box {
  position: relative;
  .box {
    border: 1px solid #f0f0f0;
    border-radius: 10px;
    height: 330px;
  }

  .title {
    padding: 10px 0;
    background: #426783;
    font-size: 14px;
    color: #fff;
    text-align: center;
  }
}
</style>
