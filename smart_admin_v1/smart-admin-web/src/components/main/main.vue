<template>
  <Layout class="main" style="height: 100%">
    <Sider
      :collapsed-width="60"
      :style="{overflow: 'hidden'}"
      :width="185"
      class="left-sider"
      collapsible
      hide-trigger
      v-model="collapsed"
    >
      <SideMenu
        :active-name="$route.name"
        :collapsed="collapsed"
        :menu-list="menuList"
        :menuNameMatchedMap="menuNameMatchedMap"
        @on-select="turnToPage"
        accordion
        ref="sideMenu"
      >
        <!-- 需要放在菜单上面的内容，如Logo，写在side-menu标签内部，如下 -->
        <div :class="{'collapsed':collapsed}" class="logo-con">
          <img :src="maxLogo" key="max-logo" v-show="!collapsed" />
          <img :src="minLogo" key="min-logo" v-show="collapsed" />
        </div>
        <div :class="{'collapsed':collapsed}" class="search-bar">
          <div class="search-box">
            <Input placeholder="搜索" v-model="searchKeyWord">
              <Icon @click="collapsed=false" slot="prefix" type="ios-search" />
            </Input>
            <div class="searchMenu" v-if="searchKeyWord">
              <ul>
                <li
                  :key="index"
                  @click="toRoute(item.name)"
                  v-for="(item, index) in searchListResult"
                >{{item.title}}</li>
                <li
                  @click="searchKeyWord = ''"
                  class="noData"
                  v-if="searchListResult.length == 0"
                >未检索到数据</li>
              </ul>
            </div>
          </div>
        </div>
      </SideMenu>
    </Sider>
    <Layout>
      <Header class="header-con">
        <HeaderBar
          :collapsed="collapsed"
          :currentTopMenuTitle="currentTopMenuTitle"
          :topMenuArray="userTopMenuArray"
          @on-change-top-menu="handleChangeTopMenu"
          @on-coll-change="handleCollapsedChange"
        >
          <User :message-unread-count="unreadCount" />
          <language
            :lang="local"
            @on-lang-change="setLocal"
            style="margin-right: 10px;"
            v-if="$config.useI18n"
          />
          <Notice />
          <Fullscreen style="margin-right: 10px;" v-model="isFullscreen" />
        </HeaderBar>
      </Header>
      <Content class="main-content-con" v-if="isLoadedPrvileges">
        <Layout class="main-layout-con">
          <div class="tag-nav-wrapper">
            <TagsNav
              :list="tagNavList"
              :value="$route"
              @input="handleClick"
              @on-close="handleCloseTag"
            />
          </div>
          <Content class="content-wrapper">
            <transition mode="out-in" name="fade-transform">
              <keep-alive :include="keepAliveIncludes">
                <router-view :key="key" />
              </keep-alive>
            </transition>
            <ABackTop :bottom="80" :height="100" :right="50" container=".content-wrapper"></ABackTop>
          </Content>
        </Layout>
      </Content>
    </Layout>
  </Layout>
</template>
<script>
import ABackTop from './components/a-back-top';
import SideMenu from './components/side-menu';
import HeaderBar from './components/header-bar';
import TagsNav from './components/tags-nav';
import Notice from './components/notice/notice';
import User from './components/user';
import { topMenuArray } from '@/router';
import Fullscreen from './components/fullscreen';
import Language from './components/language';
import { mapMutations, mapActions, mapGetters } from 'vuex';
import { getNewTagList, routeEqual, getShowMenu } from '@/lib/menu-func';
import { routers } from '@/router/routers';
import minLogo from '@/assets/images/logo-min.png';
import maxLogo from '@/assets/images/logo.png';
import { loginApi } from '@/api/login';
import './main.less';
export default {
  name: 'Main',
  components: {
    SideMenu,
    HeaderBar,
    Notice,
    Language,
    TagsNav,
    Fullscreen,
    User,
    ABackTop
  },
  data() {
    return {
      //是否加载完了权限
      isLoadedPrvileges:false,
      //用户所拥有的顶级菜单数组
      userTopMenuArray: [],
      //当前顶级菜单名字
      currentTopMenuName: '',
      currentTopMenuTitle: '',
      // 是否折叠
      collapsed: false,
      minLogo,
      maxLogo,
      // 是否全屏
      isFullscreen: false,
      // 缓存的路由：
      includes: [],
      searchKeyWord: '',
      searchList: [],
      searchListResult: [],
      menuList: [],
      menuNameMatchedMap: new Map()
    };
  },
  computed: {
    ...mapGetters(['errorCount', 'userMenuPrivilege']),
    tagNavList() {
      return this.$store.state.app.tagNavList;
    },
    tagRouter() {
      return this.$store.state.app.tagRouter;
    },
    keepAliveIncludes() {
      return this.$store.state.app.keepAliveIncludes;
    },
    cacheList() {
      const list = [
        'ParentView',
        ...(this.tagNavList.length
          ? this.tagNavList
              .filter(item => !(item.meta && item.meta.noKeepAlive))
              .map(item => item.name)
          : [])
      ];
      return list;
    },
    local() {
      return this.$store.state.app.local;
    },
    hasReadErrorPage() {
      return this.$store.state.app.hasReadErrorPage;
    },
    unreadCount() {
      return this.$store.state.user.unreadCount;
    },
    key() {
      return this.$route.name;
    }
  },
  watch: {
    searchKeyWord(val) {
      if (val) {
        this.searchListResult = this.searchList.filter(
          item => item.title.indexOf(val) >= 0
        );
      }
    },
    $route(newRoute) {
      const { name, query, params, meta } = newRoute;
      this.addTag({
        route: {
          name,
          query,
          params,
          meta
        },
        type: 'push'
      });
      this.setBreadCrumb(newRoute);
      // this.pushKeepAliveIncludes(newRoute);
      this.setTagNavList(getNewTagList(this.tagNavList, newRoute));
      //更新左侧目录打开
      this.$refs.sideMenu.updateOpenName(newRoute.name);
      // 如果param参数 存在 noKeepAlive切位true
      let isParamNoKeepAlive = params && params.noKeepAlive === true;
      // 如果query参数 存在 noKeepAlive切位true
      let isQueryNoKeepAlive = query && query.noKeepAlive === true;
      // 如果router meta 存在 noKeepAlive
      let isMetaNoKeepAlive = meta && meta.noKeepAlive === true;
      //如果存在noKeepAlive且已经缓存了，需要去掉
      if (isParamNoKeepAlive || isMetaNoKeepAlive || isQueryNoKeepAlive) {
        // 去掉keep-alive
        this.deleteKeepAliveIncludes(name);
        return;
      }
      //默认缓存住所有
      this.pushKeepAliveIncludes(name);
    }
  },
  mounted() {
    /**
     * 初始化设置面包屑导航和标签导航
     */
    this.setTagNavList();
    this.setHomeRoute(routers);
    this.setCollapsed();
    this.setBreadCrumb(this.$route);
    this.buildTopMenu();
    // 设置初始语言
    this.setLocal(this.$i18n.locale);
    //初始化左侧菜单
    this.initSideMenu();
    this.jumpRouter();
  },
  methods: {
    ...mapMutations([
      'setBreadCrumb',
      'setTagNavList',
      'pushKeepAliveIncludes',
      'clearKeepAliveIncludes',
      'deleteKeepAliveIncludes',
      'deleteOtherKeepAliveIncludes',
      'addTag',
      'setLocal',
      'setHomeRoute',
      'closeTag'
    ]),
    ...mapActions(['handleLogin']),

    jumpRouter() {
      const { name, params, query, meta } = this.$route;
      this.addTag({
        route: {
          name,
          params,
          query,
          meta
        }
      });
      // 如果当前打开页面不在标签栏中，跳到homeName页
      if (!this.tagNavList.find(item => item.name === this.$route.name)) {
        this.$router.push({
          name: this.$config.homeName
        });
      }
    },

    initSideMenu() {
      //如果是登录跳转过来
      if (this.$store.state.user.isUpdatePrivilege) {
        this.isLoadedPrvileges = true;
        this.$Spin.show();
        this.buildMenuTree();
        this.$refs.sideMenu.updateActiveName(this.$route.name);
        this.jumpRouter();
        this.$Spin.hide();
        
      } else {
        //如果页面刷新，需要重新获取权限
        (async () => {
          this.$Spin.show();
          let sessionResult = await loginApi.getSession();
          //设置权限
          this.$store.commit(
            'setUserPrivilege',
            sessionResult.data.privilegeList
          );
          this.isLoadedPrvileges = true;
          this.buildTopMenu();
          this.buildMenuTree();
          //刷新以后手动更新左侧菜单打开和选中
          this.$refs.sideMenu.updateActiveName(this.$route.name);
          this.jumpRouter();
          this.$Spin.hide();
        })();
      }
    },
    buildTopMenu() {
      let arr = [];
      for (let topMenu of topMenuArray) {
        if (
          this.$store.state.user.userLoginInfo.isSuperMan ||
          this.$store.state.user.privilegeMenuKeyList.indexOf(topMenu.name) !==
            -1
        ) {
          arr.push(topMenu);
        }
      }
      if (arr.length > 0) {
        this.currentTopMenuName = arr[0].name;
        this.currentTopMenuTitle = arr[0].meta.title;
      } else {
        this.currentTopMenuName = '';
        this.currentTopMenuTitle = '';
      }
      this.userTopMenuArray = arr;
    },
    handleChangeTopMenu(name) {
      this.currentTopMenuName = name;
      for (let topMenu of this.userTopMenuArray) {
        if (topMenu.name === name) {
          this.currentTopMenuTitle = topMenu.meta.title;
        }
      }
      this.initSideMenu();
    },
    getCurrentTopMenuChild() {
      if (this.userTopMenuArray.length === 0) {
        return [];
      } else {
        for (const router of this.userTopMenuArray) {
          if (router.name === this.currentTopMenuName) {
            return router.children;
          }
        }
        return [];
      }
    },
    buildMenuTree() {
      let privilegeTree = [];
      let routerArray = this.getCurrentTopMenuChild();
      for (const router of routerArray) {
        //过滤非菜单
        if (!router.meta.hideInMenu) {
          //判断是否有权限
          if (
            this.$store.state.user.userLoginInfo.isSuperMan ||
            this.$store.state.user.privilegeMenuKeyList.indexOf(router.name) !==
              -1
          ) {
            let menu = {
              name: router.name,
              meta: router.meta,
              icon: _.isUndefined(router.meta.icon) ? '' : router.meta.icon,
              children: []
            };
            this.menuNameMatchedMap.set(menu.name, [menu.name]);
            privilegeTree.push(menu);
            //存在孩子节点，开始递归
            if (router.children && router.children.length > 0) {
              this.recursion(router.children, menu);
            }
          }
        }
      }
      console.log('privilegeTree',privilegeTree)
      this.menuList = privilegeTree;
    },

    recursion(children, parentMenu) {
      for (const router of children) {
          //验证权限
        if (this.$store.state.user.privilegeMenuKeyList.indexOf(router.name) ===-1) {
          continue;
        }  

        //过滤非菜单
        if (!router.meta.hideInMenu) {
          //验证权限
          if (!this.$store.state.user.userLoginInfo.isSuperMan) {
            if (
              this.$store.state.user.privilegeMenuKeyList.indexOf(
                router.name
              ) === -1
            ) {
              continue;
            }
          }
          let menu = {
            name: router.name,
            meta: router.meta,
            icon: _.isUndefined(router.meta.icon) ? '' : router.meta.icon,
            children: []
          };
          this.searchList.push({
            name: router.name,
            title: router.meta.title
          });

          let menuNameArray = this.menuNameMatchedMap.get(parentMenu.name);
          this.menuNameMatchedMap.set(menu.name, [...menuNameArray, menu.name]);
          parentMenu.children.push(menu);
          //存在孩子节点，开始递归
          if (router.children && router.children.length > 0) {
            this.recursion(router.children, menu);
          }
        }
      }
    },
    // 自适应左侧导航宽度
    setCollapsed() {
      let setWidth = () => {
        let width = document.body.offsetWidth;
        if (width < 1340) {
          this.collapsed = true;
        } else {
          this.collapsed = false;
        }
      };
      setWidth();
      window.onresize = () => setWidth();
    },
    turnToPage(route) {
      let { name, params, query } = {};
      if (typeof route === 'string') name = route;
      else {
        name = route.name;
        params = route.params;
        query = route.query;
      }
      if (name.indexOf('isTurnByHref_') > -1) {
        window.open(name.split('_')[1]);
        return;
      }
      this.$router.push({
        name,
        params,
        query
      });
    },
    handleCollapsedChange(state) {
      this.collapsed = state;
    },
    handleCloseTag(res, type, route) {
      let keepRouter = route ? route.name : null;
      if (type !== 'others') {
        if (type === 'all') {
          this.turnToPage(this.$config.homeName);
          this.clearKeepAliveIncludes(this.$config.homeName);
        } else {
          //如果是关闭单个tag，则从keepAliveIncludes将关闭的那个移除掉
          this.deleteKeepAliveIncludes(route.name);
          if (routeEqual(this.$route, route)) {
            this.closeTag(route);
          }
        }
      } else {
        // 如果是关闭其他（others），则只会保留本页面和home页面
        this.deleteOtherKeepAliveIncludes(this.$route.name);
      }
      this.setTagNavList(res);
    },
    handleClick(item) {
      this.turnToPage(item);
    },
    // 跳转路由
    toRoute(name) {
      this.$router.push({ name });
      this.searchKeyWord = '';
    }
  }
};
</script>
<style lang="less" scoped>
/*fade*/
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.28s;
}

.fade-enter,
.fade-leave-active {
  opacity: 0;
}

/*fade-transform*/
.fade-transform-leave-active,
.fade-transform-enter-active {
  transition: all 0.5s;
}
.fade-transform-enter {
  opacity: 0;
  transform: translateX(-30px);
}
.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(30px);
}

.search-box {
  position: relative;
  z-index: 999;
  .searchMenu {
    position: absolute;
    z-index: 3;
    width: 100%;
    background: rgba(255, 255, 255, 0.95);
    border-radius: 3px;
    padding-left: 20px;
    padding-top: 4px;
    padding-bottom: 4px;
    font-size: 12px;
    left: 0;
    margin-top: 1px;
    ul,
    li {
      padding: 0;
      margin: 0;
      list-style: none;
    }
    li {
      line-height: 28px;
      cursor: pointer;
      &:hover {
        color: #3399ff;
      }
      &.noData {
        color: #999;
      }
    }
  }
}
</style>
