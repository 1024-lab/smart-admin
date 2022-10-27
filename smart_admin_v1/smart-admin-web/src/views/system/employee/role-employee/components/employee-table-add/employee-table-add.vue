<template>
  <div>
    <!--Modal 添加编辑成员 start-->
    <Modal
      :style="{top: '20px'}"
      :title="formValidate.id?'编辑成员':'添加成员'"
      @on-visible-change="onChangeVisibleModal"
      v-model="isShowAddOrUpdateEmployeeModal"
      width="740"
    >
      <div>
        <!--Form 添加编辑成员表单 start-->
        <Form :label-width="100" :model="formValidate" :rules="ruleValidate" ref="formValidate">
          <Row>
            <Col span="12">
              <FormItem label="登录名" prop="loginName">
                <Input
                  :disabled="formValidate.id!=null"
                  :maxlength="30"
                  @on-keydown="formValidate.loginName=formValidate.loginName.replace(/^ +| +$/g,'')"
                  @on-keyup="formValidate.loginName=formValidate.loginName.replace(/^ +| +$/g,'')"
                  class="form-width"
                  placeholder="请输入登录名"
                  v-model="formValidate.loginName"
                />
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="姓名" prop="actualName">
                <Input
                  :maxlength="30"
                  @on-keydown="formValidate.actualName=formValidate.actualName.replace(/^ +| +$/g,'')"
                  @on-keyup="formValidate.actualName=formValidate.actualName.replace(/^ +| +$/g,'')"
                  class="form-width"
                  placeholder="请输入姓名"
                  v-model="formValidate.actualName"
                />
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="别名">
                <Input
                  :maxlength="30"
                  @on-keyup="formValidate.nickName=formValidate.nickName.replace(/^ +| +$/g,'')"
                  class="form-width"
                  placeholder="请输入别名"
                  v-model="formValidate.nickName"
                />
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="身份证">
                <Input
                  @on-blur="changeIdCard(formValidate.idCard)"
                  @on-keyup="formValidate.idCard=formValidate.idCard.replace(/^ +| +$/g,'')"
                  class="form-width"
                  placeholder="请输入身份证信息"
                  v-model="formValidate.idCard"
                ></Input>
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="手机" prop="phone">
                <Input
                  @on-keyup="formValidate.phone=formValidate.phone.replace(/^ +| +$/g,'')"
                  class="form-width"
                  placeholder="请输入手机号"
                  v-model="formValidate.phone"
                ></Input>
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="E-mail" prop="email">
                <Input
                  class="form-width"
                  placeholder="请输入邮箱地址"
                  type="email"
                  v-model="formValidate.email"
                ></Input>
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem
                class="select-class"
                label="部门"
                prop="departmentName"
                v-click-outside="clickOutside"
              >
                <Input
                  @click.native="isShowTree = !isShowTree"
                  class="form-width"
                  placeholder="请选择部门"
                  readonly
                  v-model="formValidate.departmentName"
                >
                  <Icon slot="suffix" type="ios-arrow-down" v-if="!isShowTree" />
                  <Icon slot="suffix" type="ios-arrow-up" v-else />
                </Input>
                <div class="department-wrap" v-if="isShowTree">
                  <DepartmentEmployeeTree
                    :isDepartment="true"
                    @on-select="selectDepartmentOrEmployee"
                    ref="departmentEmployeeTree"
                  ></DepartmentEmployeeTree>
                </div>
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="状态" required>
                <RadioGroup v-model="formValidate.isDisabled">
                  <Radio :label="0">启用</Radio>
                  <Radio :label="1">禁用</Radio>
                </RadioGroup>
              </FormItem>
            </Col>
            <Col span="24">
              <FormItem class="select-class" label="岗位" prop="positionIdList">
                <Select class="form-width" multiple v-model="formValidate.positionIdList">
                  <Option
                    :key="item.value"
                    :value="item.value"
                    v-for="item in positionListData"
                  >{{ item.label }}</Option>
                </Select>
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="密码" prop="loginPwd" v-if="!formValidate.id">
                <Input
                  @on-keydown="formValidate.loginPwd=formValidate.loginPwd.replace(/^ +| +$/g,'')"
                  @on-keyup="formValidate.loginPwd=formValidate.loginPwd.replace(/^ +| +$/g,'')"
                  class="form-width"
                  placeholder="请输入密码"
                  type="password"
                  v-model="formValidate.loginPwd"
                />
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="密码确认" prop="passwordAgain" v-if="!formValidate.id">
                <Input
                  @on-keydown="formValidate.passwordAgain=formValidate.passwordAgain.replace(/^ +| +$/g,'')"
                  @on-keyup="formValidate.passwordAgain=formValidate.passwordAgain.replace(/^ +| +$/g,'')"
                  class="form-width"
                  placeholder="请输入确认密码"
                  type="password"
                  v-model="formValidate.passwordAgain"
                />
              </FormItem>
            </Col>
          </Row>
        </Form>
        <!--Form 添加编辑成员表单 end-->
      </div>
      <div slot="footer">
        <Button @click="isShowAddOrUpdateEmployeeModal=false" type="text">取消</Button>
        <Button :loading="isShowLoading" @click="submitFormData" class="newBtn" type="primary">提交</Button>
      </div>
    </Modal>
    <!--Modal 添加编辑成员 end-->
  </div>
</template>
<script>
import $ from 'jquery';
import { departmentApi } from '@/api/department';
import { employeeApi } from '@/api/employee';
import { roleApi } from '@/api/role';
import { positionApi } from '@/api/position';
import { dateFormat, utils } from '@/lib/util';
import DepartmentEmployeeTree from '../../../components/department-employee-tree/department-employee-tree';
export default {
  name: 'EmployeeTableAdd',
  components: {
    DepartmentEmployeeTree
  },
  props: {
    // 选中的部门
    selectDepartment: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      isShowAddOrUpdateEmployeeModal: false,
      isShowLoading: false,
      roleList: [],
      positionListData: [],
      isShowTree: false,
      formValidateBackup: {},
      formValidate: {
        actualName: '',
        loginName: '',
        nickName: '',
        departmentName: '',
        departmentId: '',
        isDisabled: 0,
        phone: '',
        idCard: '',
        birthday: '',
        loginPwd: '',
        passwordAgain: '',
        email: '',
        positionIdList: []
      },
      ruleValidate: {
        loginName: [
          { required: true, message: '登录名不能为空', trigger: 'blur' }
        ],
        actualName: [
          { required: true, message: '姓名不能为空', trigger: 'blur' }
        ],
        idCard: [
          { required: false, message: '身份证不能为空', trigger: 'blur' },
          {
            pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/,
            message: '身份证格式错误',
            trigger: 'blur'
          }
        ],
        email: [
          { required: false, message: '邮箱不能为空', trigger: 'blur' },
          { type: 'email', message: '请输入正确邮箱格式', trigger: 'blur' }
        ],
        departmentName: [
          { required: true, message: '部门不能为空', trigger: 'blur' },
          { required: true, message: '部门不能为空', trigger: 'change' }
        ],
        positionIdList: [
          {
            required: true,
            type: 'array',
            min: 1,
            message: '岗位不能为空',
            trigger: 'change'
          }
        ],
        birthday: [
          { required: false, message: '请选择出生日期', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '手机号不能为空', trigger: 'blur' },
          {
            pattern: /^(13|14|15|16|17|18)\d{9}$/,
            message: '手机号格式错误',
            trigger: 'blur'
          }
        ],
        loginPwd: [
          { required: true, message: '密码不能为空', trigger: 'blur' }
        ],
        passwordAgain: [
          { required: true, message: '确认密码不能为空', trigger: 'blur' }
        ]
      },
      birthDate: {
        shortcuts: [
          {
            text: '今天',
            value() {
              return new Date();
            },
            onClick: picker => {
              this.$Message.info('你选择的是今天');
            }
          },
          {
            text: '昨天',
            value() {
              const date = new Date();
              date.setTime(date.getTime() - 3600 * 1000 * 24);
              return date;
            },
            onClick: picker => {
              this.$Message.info('你选择的是昨天');
            }
          },
          {
            text: '一周前',
            value() {
              const date = new Date();
              date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
              return date;
            },
            onClick: picker => {
              this.$Message.info('你选择的是一周前');
            }
          }
        ]
      }
    };
  },
  computed: {},
  watch: {
    isShowAddOrUpdateEmployeeModal(val) {
      if (val) {
        this.getPositionListPage();
      }
    }
  },
  filters: {},
  created() {},
  mounted() {
    let dateStr = utils.getDateStr(0, dateFormat.YMDHM);
    this.formValidate.createDate = dateStr;
    Object.assign(this.formValidateBackup, this.formValidate);
    this.getAllRole();
  },
  beforeCreate() {},
  beforeMount() {},
  beforeUpdate() {},
  updated() {},
  beforeDestroy() {},
  destroyed() {},
  activated() {},
  methods: {
    // 选择部门点击外部关闭
    clickOutside() {
      if (this.isShowTree) {
        this.isShowTree = false;
      }
    },
    // 选择部门或者成员
    selectDepartmentOrEmployee(department) {
      this.$set(this.formValidate, 'departmentId', department.id);
      this.$set(this.formValidate, 'departmentName', department.name);
      this.isShowTree = false;
      $('.department-wrap').hide();
    },
    // 弹窗显示隐藏监听
    onChangeVisibleModal(value) {
      if (!value) {
        this.formValidate = Object.assign({}, this.formValidateBackup);
      }
    },
    // 打开Modal
    showModal(row) {
      if (typeof row === 'undefined') {
        this.ruleValidate.loginPwd = [
          {
            required: true,
            message: '密码长度至少为6位，不允许输入空格',
            trigger: 'blur',
            min: 6
          },
          { pattern: /^[0-9]{6,10}$/, message: '密码格式错误', trigger: 'blur' }
        ];
        this.ruleValidate.passwordAgain = [
          { required: true, message: '确认密码不能为空', trigger: 'blur' },
          {
            validator: this.loginPwd,
            trigger: 'blur'
          },
          {
            pattern: /^[0-9]{6,10}$/,
            message: '确认密码格式错误',
            trigger: 'blur'
          }
        ];
        this.ruleValidate.loginName = [
          { required: true, message: '登录名不能为空', trigger: 'blur' },
          {
            pattern: /^[A-Za-z]+[A-Za-z0-9]{5,17}$/,
            message:
              '请输入6位或6位字符以上(英文+数字)，第一个字符必须为英文字母',
            trigger: 'blur'
          }
        ];
      } else {
        delete this.ruleValidate.loginPwd;
        delete this.ruleValidate.passwordAgain;
        this.ruleValidate.loginName = [
          { required: true, message: '登录名不能为空', trigger: 'blur' }
        ];
      }
      this.$refs['formValidate'].resetFields();
      if (row && Object.keys(row).length > 0) {
        let positionIdList = [];
        row.positionRelationList = row.positionRelationList || [];
        row.positionRelationList.map(item => {
          positionIdList.push(item.positionId);
        });
        row.positionIdList = positionIdList;
        this.formValidate = Object.assign({}, row);
      } else {
        if (Object.keys(this.selectDepartment).length > 0) {
          if (this.selectDepartment.type === 1) {
            this.formValidate.departmentId = null;
            this.formValidate.departmentName = null;
            this.formValidate.organizationId = this.selectDepartment.id;
            this.formValidate.organizationName = this.selectDepartment.name;
          } else {
            this.formValidate.departmentId = this.selectDepartment.id;
            this.formValidate.departmentName = this.selectDepartment.name;
            this.formValidate.organizationId = this.selectDepartment.organizationId;
            this.formValidate.organizationName = this.selectDepartment.organizationName;
          }
        }
      }
      this.isShowAddOrUpdateEmployeeModal = true;
    },
    // 点击提交
    submitFormData() {
      this.$refs['formValidate'].validate(valid => {
        if (
          !/^[A-Za-z]+[A-Za-z0-9]{5,17}$/.test(this.formValidate.loginName) &&
          !this.formValidate.id
        ) {
          this.$Message.error('登录名格式不正确！');
          return;
        }
        if (this.formValidate.idCard) {
          if (
            !/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/.test(
              this.formValidate.idCard
            )
          ) {
            this.$Message.error('身份证号码格式不正确！');
            return;
          }
        }
        const reg = new RegExp(
          "[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？]"
        );
        if (reg.test(this.formValidate.actualName)) {
          this.$Message.error('姓名中不能含有特殊字符！');
          return false;
        }
        if (this.formValidate.passwordAgain !== this.formValidate.loginPwd) {
          this.$Message.error('两次输入密码不一致，请重新输入！！');
          return;
        }
        if (valid) {
          if (this.formValidate.id) {
            this.updateEmployee(this.formValidate);
          } else {
            this.addEmployee(this.formValidate);
          }
        }
      });
    },
    // 修改成员
    async updateEmployee(data) {
      try {
        this.isShowLoading = true;
        let result = await employeeApi.updateEmployee(data);
        this.isShowAddOrUpdateEmployeeModal = false;
        this.$Message.success('修改成员成功');
        this.$emit('addSuccess');
        this.$refs['formValidate'].resetFields();
      } catch (e) {
        //TODO zhuoda sentry
        console.error(e);
      } finally {
        this.isShowLoading = false;
      }
    },
    // 添加新用户接口
    async addEmployee(param) {
      try {
        this.isShowLoading = true;
        let result = await employeeApi.addEmployee(param);
        this.isShowAddOrUpdateEmployeeModal = false;
        this.$Message.success('添加成员成功');
        this.$emit('addSuccess');
        this.$refs['formValidate'].resetFields();
      } catch (e) {
        //TODO zhuoda sentry
        console.error(e);
      } finally {
        this.isShowLoading = false;
      }
    },
    // 加载角色列表
    async getAllRole() {
      let result = await roleApi.getAllRole();
      this.roleList = result.data;
    },
    // 分页查询所有岗位
    async getPositionListPage() {
      let result = await positionApi.getPositionListPage({
        pageNum: 1,
        pageSize: 200,
        sort: false
      });
      let datas = result.data;
      let list = [];
      datas.list.map(item => {
        list.push({
          value: item.id,
          label: item.positionName
        });
      });
      this.positionListData = list;
    },
    // 身份证输入失焦校验
    changeIdCard(value) {
      let reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
      if (reg.test(value)) {
        let data = value.slice(6, 14);
        let y = data.slice(0, 4);
        let m = data.slice(4, 6);
        let d = data.slice(6, 8);
        this.formValidate.birthday = y + '-' + m + '-' + d;
      }
    }
  }
};
</script>
<style lang="less" scoped>
.form-width {
  width: 100%;
}
.department-wrap {
  position: absolute;
  background-color: #ffffff;
  padding: 5px;
  border: 1px solid #dedede;
  width: 300px;
  z-index: 9;
  height: 250px;
  overflow-y: scroll;
}
.select-class {
  position: relative;
}
</style>
