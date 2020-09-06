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
                <span><Icon type="md-menu" />{{item.menuName}}</span>
              </template>
              <!--遍历得到子模块-->
              <template v-for="(children, j) in item.children">
                <Submenu :key="j" :name="children.menuKey" v-if="children.children.length > 0">
                  <template slot="title">
                      <template v-if="children.hideInMenu">
                        <Icon type="md-open" /><i style="font-size:0.85rem"> {{children.menuName}}</i>
                      </template>
                      <template v-else>
                        <Icon type="md-menu" /> {{children.menuName}}
                      </template>
                  </template>
                  <!--遍历得到子模块页面-->
                  <template v-for="(childrenPages, k) in children.children">
                      <Submenu :key="k" :name="childrenPages.menuKey" v-if="childrenPages.children.length > 0">
                        <template slot="title">
                             <template v-if="childrenPages.hideInMenu">
                                <Icon type="md-open" /><i style="font-size:0.85rem"> {{childrenPages.menuName}}</i>
                              </template>
                              <template v-else>
                                <Icon type="md-menu" /> {{childrenPages.menuName}}
                              </template>
                        </template>
                        <template v-for="(fourLevelItem,m) in childrenPages.children">
                          <MenuItem :key="m" v-if="fourLevelItem.hideInMenu" :name="hideInMenu.menuKey">
                             <Icon type="md-open" /><i style="font-size:0.85rem"> {{fourLevelItem.menuName}}</i>
                          </MenuItem>    
                          <MenuItem  :key="m" v-else :name="fourLevelItem.menuKey">
                             <Icon type="md-menu" /> {{fourLevelItem.menuName}}
                          </MenuItem>    
                        </template>
                    </Submenu>

                    <MenuItem :key="k" :name="childrenPages.menuKey" v-else>
                      <template v-if="childrenPages.hideInMenu">
                        <Icon type="md-open" /><i style="font-size:0.85rem"> {{childrenPages.menuName}}</i>
                      </template>
                      <template v-else>
                        <Icon type="md-menu" /> {{childrenPages.menuName}}
                      </template>
                    </MenuItem>

                  </template>
                </Submenu>
                <MenuItem :key="j" :name="children.menuKey" v-else>
                  <template v-if="children.hideInMenu">
                    <Icon type="md-open" /><i style="font-size:0.85rem"> {{children.menuName}}</i>
                  </template>
                  <template v-else>
                   <Icon type="md-menu" /> {{children.menuName}}
                  </template>
                </MenuItem>
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
         <div slot="extra">
          <Button
            @click="addBatchSavePoints"
            icon="ios-albums-outline"
            type="primary"
            size="small"
            v-privilege="'privilege-batch-save-points'"
            v-if="pointsChangeNum > 0"
          >批量保存功能点</Button>
        </div>
         <Alert show-icon type="warning" v-if="pointsChangeNum > 0">有 {{this.pointsChangeNum}} 个功能点更新，请立即批量保存！</Alert>
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
        @updateMenuSuccess="loadPrivilegeTableData"
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
      //菜单差异数量
      menusChangeNum: 0,
      menuList: [],
      //功能点差异数量
      pointsChangeNum:0,
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
        if (!router.meta.noValidatePrivilege) {
          this.routerMap.set(router.name, router);
          let menu = this.convert2Menu(router,null);
          console.log('change menu : ', JSON.stringify(menu))
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

      if (privilegeList.length < serverMenuList.length) {
        this.menusChange = true;
        this.menusChangeNum =
          this.menusChangeNum +
          Math.abs(privilegeList.length - serverMenuList.length);
      }
console.error(privilegeTree)
      this.menuTree = privilegeTree;
      this.menuList = privilegeList;
      this.$Spin.hide();
    },

    convert2Menu(router, parent){
      return {
            type: PRIVILEGE_TYPE_ENUM.MENU.value,
            menuName: router.meta.title,
            menuKey: router.name,
            parentKey: parent,
            url: router.path,
            children: [],
            sort: 0,
            hideInMenu:router.meta.hideInMenu
          };

    },

    recursion(children, parentMenu, menuList, serverMenuMap) {
      for (const router of children) {
        //过滤非需要权限的
        if (!router.meta.noValidatePrivilege) {
          this.routerMap.set(router.name, router);
          let menu = this.convert2Menu(router,parentMenu.menuKey);
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
          serverMenu.parentKey !== menu.parentKey||
          serverMenu.url !== menu.url;
      } else {
        isChange = true;
      }

      if (isChange) {
        console.log('==============  change menu : ', menu, serverMenu,'   ===================')
        this.menusChange = true;
        this.menusChangeNum = this.menusChangeNum + 1;
      }
    },
    // 批量保存功能点
    async addBatchSavePoints(){
      this.$Spin.show();
      let result = await privilegeApi.addBatchSavePoints(
        this.privilegeTableData.map(e =>{
          return Object.assign({},{
            functionName:e.title,
            menuKey:e.parentKey,
            functionKey:e.name,
            sort:e.sort
          });
        })
      );
      this.$Message.success('批量保存成功');
      this.$Spin.hide();
      this.loadPrivilegeTableData(this.privilegeTableData[0].parentKey);
    },
    // 批量保存菜单
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
    async getPrivilegeList(menuKey,frontPrivilegeData) {
      this.$Spin.show();
      this.formData.show = false;
      let result = await privilegeApi.queryPrivilegeFunctionList(menuKey);
      this.$Spin.hide();
      //处理服务端存储的功能点
      let serverPointData = result.data;
      let serverFunctionKeyUrlMap = new Map();
      serverPointData.map(item => {
        serverFunctionKeyUrlMap.set(item.functionKey, {url:item.url?item.url:''});
      });
      
      let pointsChangeNum = 0;
      for (let frontPrivilege of frontPrivilegeData) {
        let serverUrlObject = serverFunctionKeyUrlMap.get(frontPrivilege.name);
        if(serverUrlObject){
            frontPrivilege.url = serverUrlObject.url;
        }else{
          frontPrivilege.url = '';
          //服务端没有此功能点
          pointsChangeNum++;
        }
      }

      //如果服务端比前端的要多，则说明需要删除
      if(serverPointData.length > frontPrivilegeData.length){
          for (const serverFunction of serverPointData) {
              if(frontPrivilegeData.findIndex(e => e.name === serverFunction.functionKey ) === -1){
                 //前端的功能点呗删除
                 pointsChangeNum++;
              }
          }
      }
      this.pointsChangeNum = pointsChangeNum;
      this.privilegeTableData = frontPrivilegeData;
    },
    // 点击菜单事件
    loadPrivilegeTableData(name) {
      let router = this.routerMap.get(name);
      let frontPrivilegeData = [];
      if (!_.isUndefined(router) && router.meta && router.meta.privilege) {
        let sort = 0;
        frontPrivilegeData = router.meta.privilege.map(e =>{
            sort++;
           return Object.assign({}, e, { parentKey: name },{sort});
          }
        );
      }
      this.getPrivilegeList(name,frontPrivilegeData);
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
