<template>
  <Row :gutter="10">
    <!--菜单管理-->
    <Col :lg="6" :md="8">
      <Card class="warp-card" dis-hover>
        <div class="card-title" slot="title">
          <Icon type="ios-switch"></Icon>菜单管理
        </div>
        <div slot="extra">
          <Button
            @click="addBatchSaveMenu"
            icon="md-add"
            size="small"
            type="primary"
            v-if="menusChange"
          >批量保存</Button>
        </div>
        <Alert show-icon type="warning" v-if="menusChange">有 {{this.menusChangeNum}} 个更新，请立即批量保存！</Alert>
        <Menu
          :active-name="activeName"
          @on-select="loadPrivilegeTableData"
          ref="defineSelect"
          width="100%"
        >
          <!--遍历得到模块-->
          <template v-for="(item, i) in menuTree">
            <Submenu :key="i" :name="item.menuKey">
              <template slot="title">
                <span>{{item.menuName}}</span>
              </template>
              <!--遍历得到子模块-->
              <template v-for="(children, j) in item.children">
                <Submenu :key="j" :name="children.menuKey" v-if="children.children.length > 0">
                  <template slot="title">
                    <span>{{children.menuName}}</span>
                  </template>
                  <!--遍历得到子模块页面-->
                  <template v-for="(childrenPages, k) in children.children">
                    <MenuItem :key="k" :name="childrenPages.menuKey">{{childrenPages.menuName}}</MenuItem>
                  </template>
                </Submenu>
                <MenuItem :key="j" :name="children.menuKey" v-else>{{children.menuName}}</MenuItem>
              </template>
            </Submenu>
          </template>
        </Menu>
      </Card>
    </Col>
    <Col :lg="18" :md="16">
      <Card class="warp-card" dis-hover style="margin-bottom:100px">
        <div class="card-title" slot="title">
          <Icon type="ios-cog"></Icon>功能点
        </div>
        <Row>
          <Table :columns="privilegeTableColumn" :data="privilegeTableData" border></Table>
        </Row>
      </Card>
    </Col>
    <Col span="24">
      <privilege-form
        :privilege="formData.privilege"
        :show="formData.show"
        :title="formData.title"
        :typeDisabled="typeDisabled"
        @closeModal="closeModal"
        @updateMenuSuccess="getPrivilegeList"
      ></privilege-form>
    </Col>
  </Row>
</template>
<script>
import { privilegeApi } from '@/api/privilege';
import PrivilegeForm from './components/privilege-form';
import { routers } from '@/router/routers';
import { PRIVILEGE_TYPE_ENUM } from '@/constants/privilege';

export default {
  name: 'SystemPrivilege',
  components: {
    PrivilegeForm
  },
  data() {
    return {
      typeDisabled: true,
      activeName: 0,
      scope: 1,
      formData: {
        show: false,
        title: '功能点菜单',
        privilege: {
          functionKey: '',
          functionName: '',
          menuKey: '',
          url: 1
        }
      },
      menuTree: [],
      menusChange: false,
      menusChangeNum: 0,
      menuList: [],
      routerMap: new Map(),
      privilegeTableData: [],
      privilegeTableColumn: [
        {
          title: '名称',
          key: 'title'
        },
        {
          title: 'routerKey',
          key: 'name'
        },
        {
          title: 'url',
          key: 'url'
        },
        {
          title: '操作',
          width: 120,
          align: 'center',
          className: 'action-hide',
          render: (h, params) => {
            return this.$tableAction(h, [
              {
                title: '编辑',
                directives: [
                  {
                    name: 'privilege',
                    value: 'privilege-main-update'
                  }
                ],
                action: () => {
                  this.updatePrivilege(params.row, params.index);
                }
              }
            ]);
          }
        }
      ]
    };
  },
  mounted() {
    this.initRouters();
  },
  methods: {
    // 关闭模态框
    closeModal() {
      this.formData.show = false;
    },
    // 初始化菜单
    initRouters() {
      this.resetMenuChange();
      this.buildPrivilegeTree();
    },
    // 获取全部菜单列表
    async buildPrivilegeTree() {
      this.$Spin.show();
      let getMenuListResult = await privilegeApi.getMenuList();
      let serverMenuList = getMenuListResult.data;
      let serverMenuMap = new Map();
      for (let serverMenu of serverMenuList) {
        serverMenuMap.set(serverMenu.menuKey, serverMenu);
      }

      let privilegeList = [];
      let privilegeTree = [];

      for (const router of routers) {
        //过滤非菜单
        if (!router.meta.hideInMenu) {
          this.routerMap.set(router.name, router);
          let menu = {
            type: PRIVILEGE_TYPE_ENUM.MENU.value,
            menuName: router.meta.title,
            menuKey: router.name,
            parentKey: null,
            url: router.path,
            children: [],
            sort: 0
          };
          privilegeTree.push(menu);
          privilegeList.push(menu);
          //判断是否有更新菜单
          this.hasMenuChange(menu, serverMenuMap);
          //存在孩子节点，开始递归
          if (router.children && router.children.length > 0) {
            this.recursion(router.children, menu, privilegeList, serverMenuMap);
          }
        }
      }

      if (privilegeList.length !== serverMenuList.length) {
        this.menusChange = true;
        this.menusChangeNum =
          this.menusChangeNum +
          Math.abs(privilegeList.length - serverMenuList.length);
      }

      this.menuTree = privilegeTree;
      this.menuList = privilegeList;
      this.$Spin.hide();
    },

    recursion(children, parentMenu, menuList, serverMenuMap) {
      for (const router of children) {
        //过滤非菜单
        if (!router.meta.hideInMenu) {
          this.routerMap.set(router.name, router);
          let menu = {
            type: PRIVILEGE_TYPE_ENUM.MENU.value,
            menuName: router.meta.title,
            menuKey: router.name,
            parentKey: parentMenu.menuKey,
            url: router.path,
            children: [],
            sort: 0
          };

          parentMenu.children.push(menu);
          menuList.push(menu);
          //判断是否有更新菜单
          this.hasMenuChange(menu, serverMenuMap);
          //存在孩子节点，开始递归
          if (router.children && router.children.length > 0) {
            this.recursion(router.children, menu, menuList, serverMenuMap);
          }
        }
      }
    },

    // reset菜单有更新
    resetMenuChange() {
      this.menusChange = false;
      this.menusChangeNum = 0;
    },

    // 菜单有更新
    hasMenuChange(menu, serverMenuMap) {
      let isChange = false;
      let serverMenu = serverMenuMap.get(menu.menuKey);
      if (serverMenu) {
        isChange =
          serverMenu.menuName !== menu.menuName ||
          serverMenu.menuKey !== menu.menuKey ||
          serverMenu.parentKey !== menu.parentKey;
      } else {
        isChange = true;
      }

      if (isChange) {
        this.menusChange = true;
        this.menusChangeNum = this.menusChangeNum + 1;
      }
    },
    // 菜单批量保存
    async addBatchSaveMenu() {
      this.$Spin.show();
      let result = await privilegeApi.addBatchSaveMenu(this.menuList);
      this.$Message.success('批量保存成功');
      this.$Spin.hide();
      //重新获取数据
      this.initRouters();
    },
    // 编辑功能点
    updatePrivilege(item, sort) {
      this.formData.privilege = {
        functionKey: item.name,
        functionName: item.title,
        menuKey: item.parentKey,
        url: item.url,
        sort
      };
      this.formData.title = item.parentName;
      this.formData.show = true;
    },
    // 查询菜单对应的页面以及功能点
    async getPrivilegeList(menuKey) {
      this.$Spin.show();
      this.formData.show = false;
      let result = await privilegeApi.queryPrivilegeFunctionList(menuKey);
      this.$Spin.hide();
      let datas = result.data;
      let functionKey = new Map();
      datas.map(item => {
        functionKey.set(item.functionKey, item.url);
      });
      let privilegeTableData = [];
      this.privilegeTableData.map(item => {
        let url = functionKey.get(item.name) || '';
        item.url = url;
        privilegeTableData.push(item);
      });
      this.privilegeTableData = privilegeTableData;
    },
    // 点击菜单事件
    loadPrivilegeTableData(name) {
      let router = this.routerMap.get(name);
      if (!_.isUndefined(router) && router.meta && router.meta.childrenPoints) {
        this.privilegeTableData = router.meta.childrenPoints.map(e =>
          Object.assign({}, e, { parentKey: name })
        );
      }
      this.getPrivilegeList(name);
    }
  }
};
</script>
<style scoped>
.privilege-badge {
  background: rgba(45, 140, 240, 0.8);
  border-radius: 3px;
  font-size: 8px;
  color: #fff;
  line-height: 1;
  display: inline-block;
  padding: 0 3px;
  margin-left: 5px;
}
</style>
