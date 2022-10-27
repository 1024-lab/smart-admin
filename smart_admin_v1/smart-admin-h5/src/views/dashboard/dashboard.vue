<template>
  <div>

    <!---------------- 页面内容 begin ---------------->
    <div class="main-content">
      <keep-alive :include="keepAliveIncludes">
        <router-view/>
      </keep-alive>
    </div>
    <!---------------- 页面内容 end ---------------->

    <!---------------- 底部 tabbar begin ---------------->

    <!---------------- 对于一些需要展示tabbar的页面，则进行展示 ---------------->
    <div class="main-footer" v-show="$route.meta && $route.meta.showTabbar">
      <van-tabbar
        fixed
        route
        v-model="active"
      >
        <van-tabbar-item
          v-for="(item, index) in tabbar"
          :to="item.to"
          :icon="item.icon"
          :name="item.name"
          :key="index">
          {{ item.title }}
        </van-tabbar-item>
      </van-tabbar>
    </div>
    <!---------------- 底部 tabbar end ---------------->

  </div>
</template>

<script type="text/javascript">
import SessionMixin from 'components/mixin/session-mixin';
import { mapMutations, mapState } from 'vuex';

export default {
  name: 'DashBoard',
  mixins: [SessionMixin],
  data() {
    return {
      active: 'Mine',
      tabbar: [
        // {
        //   name: 'ContactCompany',
        //   title: '首页',
        //   icon: 'user-o',
        //   badge: 0,
        //   to: '/contact-company'
        // },
        {
          name: 'Mine',
          title: '我的',
          icon: 'user-o',
          to: '/user'
        }
      ]
    };
  },
  computed: {
    ...mapState({
      keepAliveIncludes: state => state.app.keepAliveIncludes
    })
  },
  created() {
    // 通过路由跳转绑定Tabbar的选中
    this.updateTabbarSelected(this.$route.name);
    this.checkRouterKeepAlive(this.$route);
  },
  watch: {
    $route(newRoute) {
      this.checkRouterKeepAlive(newRoute);
    }
  },
  methods: {
    ...mapMutations('app', [
      'pushKeepAliveIncludes',
      'deleteKeepAliveIncludes'
    ]),
    checkRouterKeepAlive(newRoute) {
      const { name, meta } = newRoute;
      if (meta && meta.keepAlive) {
        this.pushKeepAliveIncludes(name);
      } else {
        this.deleteKeepAliveIncludes(name);
      }
    },
    updateTabbarSelected(item) {
      console.log(item, 12222);
      this.active = item;
    }
  }
};
</script>
<style lang="scss" scoped>
</style>
