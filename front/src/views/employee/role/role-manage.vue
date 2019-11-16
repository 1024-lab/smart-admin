<template>
  <!-- Row 角色管理 start -->
  <Row :gutter="10">
    <!-- Col 左侧角色列表模块 start -->
    <Col :lg="5" :md="8">
      <Card class="warp-card" dis-hover style=" position: relative;">
        <p slot="title">角色列表</p>
        <div slot="extra">
          <Button
            @click="showAddRoleModal()"
            size="small"
            type="primary"
            v-privilege="'add-role'"
          >添加</Button>
        </div>
        <!-- Menu 角色列表 start -->
        <Menu
          :active-name="0"
          class="left role-list no-scrollbar"
          ref="sideMenu"
          style="height: 666px;overflow-y: scroll;width:100%;"
        >
          <!-- MenuItem 角色列表项 start -->
          <MenuItem
            :key="item.id"
            :name="index"
            @click.native="selectRole(item,index)"
            v-for="(item,index) in roleList"
          >
            <Row>
              <Col span="24">
                <span class="role-name">
                  <Tooltip placement="right">
                    <span>{{item.roleName}}</span>
                    <div class="suspension-box" slot="content">
                      <p>
                        <span @click="deleteSingleRole(item)" v-privilege="'delete-role'">删除</span>
                      </p>
                      <p>
                        <span @click="showUpdateRoleModal(item)" v-privilege="'update-role'">编辑</span>
                      </p>
                    </div>
                  </Tooltip>
                </span>
              </Col>
            </Row>
          </MenuItem>
          <!-- MenuItem 角色列表项 end -->
        </Menu>
        <!-- Menu 角色列表 end -->
        <!--Modal 添加角色 start-->
        <Modal title="添加角色" v-model="isShowAddRoleModal">
          <Form :label-width="100" :model="ruleDetail" label-position="left">
            <FormItem label="角色名称" required>
              <Input
                @on-keydown="ruleDetail.name=ruleDetail.name.replace(/^ +| +$/g,'')"
                @on-keyup="ruleDetail.name=ruleDetail.name.replace(/^ +| +$/g,'')"
                placeholder="请输入角色名称..."
                v-model="ruleDetail.name"
              ></Input>
            </FormItem>
            <FormItem label="备注">
              <Input placeholder="请输入备注" type="textarea" v-model="ruleDetail.detail"></Input>
            </FormItem>
          </Form>
          <div slot="footer">
            <Button @click="submitRole" type="primary">确认</Button>
            <Button @click="hideAddRoleModal()">返回</Button>
          </div>
        </Modal>
        <!--Modal 添加角色 end-->
        <!--Modal 修改角色 start-->
        <Modal title="修改角色" v-model="isShowUpdateRoleModal">
          <Form :label-width="100" :model="ruleDetail" label-position="left">
            <FormItem label="角色名称" required>
              <Input
                @on-keydown="ruleDetail.name=ruleDetail.name.replace(/^ +| +$/g,'')"
                @on-keyup="ruleDetail.name=ruleDetail.name.replace(/^ +| +$/g,'')"
                placeholder="请输入角色名称..."
                v-model="ruleDetail.name"
              ></Input>
            </FormItem>
            <FormItem label="备注">
              <Input placeholder="请输入备注" type="textarea" v-model="ruleDetail.detail"></Input>
            </FormItem>
          </Form>
          <div slot="footer">
            <Button @click="updateRole" type="primary">确认</Button>
            <Button @click="hideUpdateRoleModal()">返回</Button>
          </div>
        </Modal>
        <!--Modal 修改角色 end-->
        <!--Modal 删除角色 start-->
        <Modal
          @on-cancel="cancelDeleteRole()"
          @on-ok="confirmDeleteRole()"
          title="删除角色"
          v-model="isShowRemoveRoleModal"
        >确定删除"{{ruleDetail.name}}"这个角色吗？</Modal>
        <!--Modal 删除角色 end-->
      </Card>
    </Col>
    <!-- Col 左侧角色列表模块 end -->
    <!-- Col 功能列表 start -->
    <Col :lg="19" :md="16">
      <Card class="warp-card" dis-hover>
        <div class="card-title" slot="title">功能列表</div>
        <!-- Menu 切换功能 start -->
        <Menu :active-name="displayTab" @on-select="selectTab" mode="horizontal" style="z-index: 1">
          <MenuItem :name="1">
            <Icon type="ios-hammer" />功能权限
          </MenuItem>
          <MenuItem :name="2">
            <Icon type="ios-paper" />数据范围
          </MenuItem>
          <MenuItem :name="3">
            <Icon type="ios-people" />成员管理
          </MenuItem>
        </Menu>
        <!-- Menu 切换功能 end -->
        <!--功能权限-->
        <RoleTree :roleId="roleId" v-if="displayTab==1"></RoleTree>
        <!--数据范围-->
        <RoleDataScope :roleId="roleId" v-if="displayTab==2"></RoleDataScope>
        <!--成员管理-->
        <RoleList :roleId="roleId" v-if="displayTab==3"></RoleList>
      </Card>
    </Col>
    <!-- Col 功能列表 end -->
  </Row>
  <!-- Row 角色管理 end -->
</template>
<script>
import RoleTree from './components/role-tree/role-tree';
import RoleList from './components/role-list/role-list';
import RoleDataScope from './components/role-data-scope/role-data-scope';
import { roleApi } from '@/api/role';

export default {
  name: 'RoleManage',
  components: {
    RoleTree,
    RoleList,
    RoleDataScope
  },
  props: {},
  data() {
    return {
      roleList: {},
      roleId: 0,
      // 删除角色对话框隐藏
      isShowRemoveRoleModal: false,
      // 修改角色对话框隐藏
      isShowUpdateRoleModal: false,
      // 增加角色对话框隐藏
      isShowAddRoleModal: false,
      // 增加角色信息
      ruleDetail: {
        name: '',
        id: '',
        detail: ''
      },
      // 默认选中Menu标签为功能权限
      displayTab: 1,
      // 是否第一次请求数据
      isFirst: true
    };
  },
  computed: {},
  watch: {},
  filters: {},
  created() {},
  mounted() {
    // 初始化加载数据
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
    // 初始化加载数据方法
    async getAllRole() {
      try {
        let response = await roleApi.getAllRole();
        this.roleList = response.data;
        if (this.roleList && this.roleList.length > 0) {
          this.roleId = this.roleList[0].id;
          if (this.isFirst) {
            this.$nextTick(() => {
              this.$refs.sideMenu.updateOpened();
              this.$refs.sideMenu.updateActiveName();
            });
            this.isFirst = false;
          }
        }
      } catch (e) {
        //TODO zhuoda sentry
        console.error(e);
      }
    },
    // 增加角色方法
    async addRole(roleDesc, roleName) {
      this.$Spin.show();
      try {
        await roleApi.addRole(roleDesc, roleName);
        this.hideAddRoleModal();
        await this.getAllRole(); // 刷新
      } catch (e) {
        //TODO zhuoda sentry
        console.error(e);
      } finally {
        this.$Spin.hide();
      }
    },
    // 提交添加角色
    submitRole() {
      // 添加
      if (this.ruleDetail.name !== '' && this.ruleDetail.name.length <= 20) {
        this.addRole(this.ruleDetail.detail, this.ruleDetail.name);
      } else {
        this.$Message.warning('请先完善角色信息');
      }
    },
    // 编辑角色方法
    async updateRole() {
      this.$Spin.show();
      try {
        let response = await roleApi.updateRole(
          this.ruleDetail.id,
          this.ruleDetail.detail,
          this.ruleDetail.name
        );
        this.roleList = response.data;
        this.hideUpdateRoleModal();
        await this.getAllRole(); // 刷新
      } catch (e) {
        //TODO zhuoda sentry
        console.error(e);
      } finally {
        this.$Spin.hide();
      }
    },
    // 删除角色方法
    async deleteRole(id) {
      this.$Spin.show();
      try {
        await roleApi.deleteRole(id);
        this.$Message.success('删除成功');
        await this.getAllRole(); // 刷新
      } catch (e) {
        //TODO zhuoda sentry
        console.error(e);
      } finally {
        this.$Spin.hide();
      }
    },
    // 删除单个角色
    deleteSingleRole(item) {
      this.isShowRemoveRoleModal = true;
      this.ruleDetail.id = item.id;
      this.ruleDetail.name = item.roleName;
    },
    // 编辑角色页面
    showUpdateRoleModal(item) {
      console.log(item);
      this.isShowUpdateRoleModal = true;
      this.ruleDetail.id = item.id;
      this.ruleDetail.name = item.roleName;
      this.ruleDetail.detail = item.remark;
    },
    // 添加角色页面
    showAddRoleModal() {
      this.isShowAddRoleModal = true;
      this.ruleDetail.name = '';
      this.ruleDetail.detail = '';
    },
    // 关闭更新弹窗
    hideUpdateRoleModal() {
      this.isShowUpdateRoleModal = false;
    },
    // 关闭添加弹窗
    hideAddRoleModal() {
      this.isShowAddRoleModal = false;
    },
    // 功能选择
    selectTab(position) {
      this.displayTab = position;
    },
    // 角色选择
    selectRole(item, index) {
      this.roleId = item.id;
    },
    // 确定删除
    confirmDeleteRole() {
      this.deleteRole(this.ruleDetail.id);
      this.isShowRemoveRoleModal = false;
    },
    // 取消删除
    cancelDeleteRole() {
      this.isShowRemoveRoleModal = false;
    }
  }
};
</script>
<style lang="less" scoped>
.role-list {
  line-height: 30px;
  padding: 10px 0;

  .role-name {
    position: relative;
  }

  &::after {
    display: none;
  }

  button {
    margin-left: 3px;
  }
}

.ivu-menu-item-active:not(.ivu-menu-submenu) {
  z-index: 0 !important;
}

.suspension-box {
  z-index: 999;
  padding: 0 8px;

  p {
    padding: 3px 0;
  }
}
</style>
