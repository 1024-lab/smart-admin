<template>
  <div>

    <!---------------- 頁面内容 begin ---------------->
    <div class="main-content">
      <keep-alive>
        <router-view v-if="$route.meta.keepAlive" />
      </keep-alive>
      <router-view v-if="!$route.meta.keepAlive" />
    </div>
    <!---------------- 頁面内容 end ---------------->

    <!---------------- 底部 tabbar begin ---------------->
    <div class="main-footer">
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
import mainMixin from 'views/main/main-mixin';

export default {
  name: 'TabbarMain',
  mixins: [mainMixin],
  data() {
    return {
      active: 'ContactCompany',
      tabbar: [
        // {
        //   name: 'Home',
        //   title: '首页',
        //   icon: 'wap-home-o',
        //   to: '/home'
        // },
        // {
        //   name: 'Business',
        //   title: '业务中心',
        //   icon: 'apps-o',
        //   to: '/business'
        // },
        {
          name: 'ContactCompany',
          title: '往来机构',
          icon: 'user-o',
          badge: 0,
          to: '/contact-company'
        },
        {
          name: 'Mine',
          title: '我的',
          icon: 'user-o',
          to: '/mine'
        }
      ]
    };
  },
  created() {
    // 通过路由跳转绑定Tabbar的选中
    this.updateTabbarSelected(this.$route.name);
  },
  watch: {
    // 监听路由变化,保证路由跳转Tabbar选中正常
    $route: {
      handler(val, oldval) {
        // this.updateTabbarSelected(val.name)
      }
    },
    deep: true
  },
  computed: {},
  methods: {
    updateTabbarSelected(item) {
      console.log(item, 12222);
      this.active = item;
    }
  }
};
</script>
<style lang="less" scoped>
</style>
