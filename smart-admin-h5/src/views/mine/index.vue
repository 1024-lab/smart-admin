<!-- home -->
<template>
  <div>
    <van-nav-bar title="个人中心"/>

    <van-grid :column-num="1">
      <van-grid-item icon="home-o" :text="actualName"/>
    </van-grid>

    <van-cell-group>
      <!--      <van-cell icon="user-o" title="订单" is-link />-->
      <!--      <van-cell icon="location-o" title="设置" is-link />-->
      <!--      <van-cell icon="location-o" to="/user/change-password" title="修改密码" is-link />-->
      <van-cell icon="bulb-o" title="更新权限" @click="updatePrivilege" is-link/>
      <van-cell icon="apps-o" title="开发专用" is-link to="/develop/config"/>
    </van-cell-group>

    <div style="margin: 16px;">
      <van-button block @click="logout" type="warning" size="small">
        退出登录
      </van-button>
    </div>

  </div>
</template>

<script>
import { loginApi } from 'api/login';
import cookie from '@/lib/cookie';
import { userApi } from 'api/user';

export default {
  data() {
    return {
      actualName: null
    };
  },

  computed: {},

  mounted() {
    this.actualName = '你好, ' + this.$store.getters['user/actualName'];
  },

  methods: {
    async updatePrivilege() {
      this.$smart.loading();
      try {
        const res = await userApi.getSession();
        const loginInfo = res.data;
        this.$store.commit('user/updateSession', loginInfo);
        this.$toast('已成功更新权限!');
      } catch (e) {
        this.$smartSentry.captureException(e);
      } finally {
        this.$smart.loadingClear();
      }
    },
    async logout() {
      this.$smart.loading();
      try {
        await loginApi.logout(cookie.getToken());
      } catch (e) {
        this.$smartSentry.captureException(e);
      } finally {
        cookie.clearToken();
        this.$smart.loadingClear();
        this.$router.replace('/login');
      }
    }
  }
};
</script>
