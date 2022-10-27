<template>
  <div class="user-avatar-dropdown">
    <Dropdown @on-click="handleClick">
      <label style="font-size: 14px;margin-left:6px;">{{loginInfo.actualName}}</label>
      <Icon :size="6" class="dropdown-arrows" type="icon iconfont iconxiangxiala"></Icon>
      <Avatar :src="loginInfo.header||defaultIcon" class="head" icon="ios-person" size="small" />
      <DropdownMenu slot="list">
        <DropdownItem name="updatePassword">修改密码</DropdownItem>
        <DropdownItem name="logout">退出登录</DropdownItem>
      </DropdownMenu>
    </Dropdown>
    <Modal
      :closable="false"
      :mask-closable="false"
      @on-cancel="editModal=false"
      @on-ok="editSure"
      title="修改密码"
      v-model="editModal"
    >
      <Form :label-width="100" :model="formValidate" :rules="ruleValidate" ref="formValidate">
        <FormItem label="密码：" prop="oldPwd">
          <Input placeholder="请输入原密码" type="password" v-model="formValidate.oldPwd" />
        </FormItem>
        <FormItem label="新密码：" prop="pwd">
          <Input placeholder="请输入新密码" type="password" v-model="formValidate.pwd" />
        </FormItem>
        <FormItem label="确认密码：" prop="pwdAgain">
          <Input placeholder="请再次输入新密码" type="password" v-model="formValidate.pwdAgain" />
        </FormItem>
      </Form>
    </Modal>
  </div>
</template>

<script>
import './user.less';
import { mapActions } from 'vuex';
import { employeeApi } from '@/api/employee';
import cookie from '@/lib/cookie';
import { loginApi } from '@/api/login';
export default {
  name: 'User',
  props: {
    // 未读消息数
    messageUnreadCount: {
      type: Number,
      default: 0
    }
  },
  data() {
    // 当前登录人信息
    let loginInfo = this.$store.state.user.userLoginInfo;
    return {
      loginInfo: loginInfo,
      defaultIcon: require('@/assets/images/default_icon.png'),
      editModal: false,
      formValidate: {},
      ruleValidate: {
        oldPwd: [
          {
            required: true,
            message: '请输入密码',
            trigger: 'blur'
          }
        ],
        pwd: [
          {
            required: true,
            message: '请输入新密码',
            trigger: 'blur'
          }
        ],
        pwdAgain: [
          {
            required: true,
            message: '请输入确认密码',
            trigger: 'blur'
          }
        ]
      }
    };
  },
  methods: {
    logout() {
      this.$Spin.show();
      let token = cookie.getToken();
      localStorage.clear();
      cookie.clearToken();

      loginApi.logout(token);
      location.reload();
    },
    updatePassword() {
      this.editModal = true;
    },
    handleClick(name) {
      switch (name) {
        case 'logout':
          this.logout();
          break;
        case 'updatePassword':
          this.updatePassword();
          break;
      }
    },
    async savePassword() {
      this.$Spin.show();
      let result = await employeeApi.updatePwd(this.formValidate);
      this.$Message.success('修改密码成功');
      this.$refs['formValidate'].resetFields();
      this.editModal = false;
      this.logout();
    },
    editSure() {
      this.$refs['formValidate'].validate(valid => {
        if (valid) {
          if (this.formValidate.passwordAgain !== this.formValidate.loginPwd) {
            return this.$Message.error('两次输入密码不一致，请重新输入！！');
          }
          this.savePassword();
        }
      });
    }
  }
};
</script>
<style>
.arrow-icon {
  margin: 0 5px;
}
</style>
