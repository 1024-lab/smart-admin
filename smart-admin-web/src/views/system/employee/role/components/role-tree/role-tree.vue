<template>
  <!--div 功能权限部分 start-->
  <div id="tree">
    <Row>
      <Col class="col-desc" span="16">设置角色对应的功能操作、后台管理权限</Col>
      <Col class="button-style" span="8">
        <Button
          @click.native="saveChange()"
          style="margin-right: 20px;"
          type="primary"
          v-privilege="'update-role-privilege'"
        >保存</Button>
      </Col>
    </Row>
    <!--CheckboxGroup 功能权限勾选部分 start-->
    <CheckboxGroup v-model="checkedData">
      <div class="checked-box">
        <ul>
          <!--li 一级权限模块 start-->
          <li :key="module.key" v-for="(module, moduleIndex) in tree">
            <div class="level-one">
              <Checkbox
                :label="module.key"
                @click.prevent.native="selectCheckbox(module)"
              >{{module.name}} </Checkbox>
            </div>
            <!--div 二级权限模块 start-->
            <div
              :key="childrenModule.key"
              class="level-two"
              v-for="(childrenModule, childrenModuleIndex) in module.children"
            >
              <Checkbox
                :label="childrenModule.key"
                @click.prevent.native="selectCheckbox(childrenModule)"
                class="level-two-label"
              >{{childrenModule.name}}</Checkbox>
              <!--div 三级权限模块 start-->
              <div class="level-three">
                <template v-for="(pages,pagesIndex) in childrenModule.children">
                  <div
                    :key="pages.key"
                    class="isLevel-four"
                    v-if="pages.children && pages.children.length > 0"
                  >
                    <Checkbox
                      :key="pages.key"
                      :label="pages.key"
                      @click.prevent.native="selectCheckbox(pages)"
                      class="level-three-label"
                    >{{pages.name}}</Checkbox>
                    <!--div 四级权限模块 start-->
                    <div :key="pagesIndex" class="level-four" v-if="pages.children.length > 0">
                      <template v-for="(page, pageIndex) in pages.children">
                        <!---
                          如果是第四级菜单的话，会继续往下一层遍历
                        -->
                        <template v-if="page.children && page.children.length > 0">
                          <!--div 五级权限模块 start-->
                          <div :key="page.key" class="isLevel-four">
                            <Checkbox
                              :label="page.key"
                              @click.prevent.native="selectCheckbox(page)"
                              class="level-three-label"
                            >{{page.name}}</Checkbox>
                            <!--div 五级权限的功能点 start-->
                            <div
                              class="level-four"
                              v-if="page.children && page.children.length > 0"
                            >
                              <template
                                v-for="(fiveLevelFunction, fiveLevelIndex) in page.children"
                              >
                                <Checkbox
                                  :label="fiveLevelFunction.key"
                                  @click.prevent.native="selectCheckbox(fiveLevelFunction)"
                                >{{fiveLevelFunction.name}}</Checkbox>
                              </template>
                            </div>
                            <!--div 五级权限的功能点 start-->
                          </div>
                          <!--div 五级权限模块 start-->
                        </template>
                        <template v-else>
                          <Checkbox
                            :key="page.key"
                            :label="page.key"
                            @click.prevent.native="selectCheckbox(page)"
                          >{{page.name}}</Checkbox>
                        </template>
                      </template>
                    </div>
                    <!--div 四级权限模块 start-->
                  </div>
                  <Checkbox
                    :key="pages.key"
                    :label="pages.key"
                    @click.prevent.native="selectCheckbox(pages)"
                    v-else
                  >{{pages.name}}</Checkbox>
                </template>
              </div>
              <!--div 三级权限模块 end-->
            </div>
            <!--div 二级权限模块 end-->
          </li>
          <!--li 一级权限模块 end-->
        </ul>
      </div>
    </CheckboxGroup>
    <!--CheckboxGroup 功能权限勾选部分 end-->
  </div>
  <!--div 功能权限部分 end-->
</template>
<script>
import { roleApi } from '@/api/role';
import { privilegeApi } from '@/api/privilege';

export default {
  name: 'RoleTree',
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
  components: {},
  // 父级组件数据传递
  data() {
    return {
      // 权限数据
      tree: [],
      //权限铺平的map
      treeMap: null,
      loading: false,
      // 提交保存数据
      rolePower: {
        privilegeKeyList: [],
        roleId: ''
      },
      // 已选项
      checkedData: []
    };
  },
  computed: {},
  watch: {
    roleId(newVal) {
      if (newVal) {
        this.getListPrivilegeByRoleId(newVal);
      }
    }
  },
  filters: {},
  created() {},
  mounted() {
    this.getListPrivilegeByRoleId(this.roleId);
  },
  beforeCreate() {},
  beforeMount() {},
  beforeUpdate() {},
  updated() {},
  beforeDestroy() {},
  destroyed() {},
  activated() {},
  methods: {
    // 勾选权限
    selectCheckbox(module) {
      // 是否勾选
      let findIndex = this.checkedData.indexOf(module.key);
      if (findIndex !== -1) {
        //取消自己和孩子
        this.removeCheckAndChidlrenCheck(module);
        //判断父级及其以上是否有重复
        if (module.parentKey) {
          let parentPrivilege = this.treeMap.get(module.parentKey);
          if (parentPrivilege) {
            this.unCheckedParent(parentPrivilege);
          }
        }
      } else {
        // 选中子级所有checkBox
        this.addCheckAndChildrenCheck(module);
        // 父类集合选中
        let parentKey = module.parentKey;
        while (parentKey != null) {
          let parentPrivilege = this.treeMap.get(parentKey);
          if (parentPrivilege) {
            let findIndex = this.checkedData.findIndex(val => val == parentKey);
            if (findIndex == -1) {
              this.checkedData.push(parentKey);
            }
            parentKey = parentPrivilege.parentKey;
          } else {
            parentKey = null;
          }
        }
      }
    },
    // 取消父级check
    unCheckedParent(parentPrivilege) {
      if (
        parentPrivilege &&
        parentPrivilege.children &&
        parentPrivilege.children.length > 0
      ) {
        if (!this.judgeArrayExistCheck(parentPrivilege.children)) {
          let findIndex = this.checkedData.findIndex(
            val => val == parentPrivilege.key
          );
          if (findIndex != -1) {
            this.checkedData.splice(findIndex, 1);
          }

          parentPrivilege = this.treeMap.get(parentPrivilege.parentKey);
          this.unCheckedParent(parentPrivilege);
        }
      }
    },
    //判断权限数组是否有选中的，有返回true,没有任何选中返回false
    judgeArrayExistCheck(privilegeArray) {
      if (!privilegeArray) {
        return false;
      }

      for (let privilege of privilegeArray) {
        let findIndex = this.checkedData.findIndex(val => val == privilege.key);
        if (findIndex != -1) {
          return true;
        }
      }
      return false;
    },

    // 选中子级所有checkBox
    addCheckAndChildrenCheck(module) {
      let findIndex = this.checkedData.findIndex(val => val == module.key);
      if (findIndex == -1) {
        this.checkedData.push(module.key);
      }

      if (module.children) {
        module.children.forEach(item => {
          this.addCheckAndChildrenCheck(item);
        });
      }
    },
    // 取消自己和下级勾选
    removeCheckAndChidlrenCheck(module) {
      let findIndex = this.checkedData.findIndex(val => val == module.key);
      if (findIndex != -1) {
        this.checkedData.splice(findIndex, 1);
      }
      if (module.children) {
        module.children.forEach(item => {
          this.removeCheckAndChidlrenCheck(item);
        });
      }
    },
    // 保存改变权限
    saveChange() {
      if (this.checkedData.length == 0) {
        this.$Message.error('还未选择任何权限');
        return;
      }
      this.rolePower.roleId = this.roleId;
      this.rolePower.privilegeKeyList = this.checkedData.concat();
      this.getRolePower(this.rolePower);
    },
    // 更新角色功能权限方法
    async getRolePower(data) {
      this.$Spin.show();
      try {
        await privilegeApi.getRolePower(data);
        this.$Message.info('保存成功');
        this.rolePower.privilegeKeyList = [];
        await this.getListPrivilegeByRoleId(this.roleId);
      } catch (e) {
        console.error(e);
      } finally {
        this.$Spin.hide();
      }
    },
    //将权限tree 打平成map
    tree2map(tree) {
      if (tree) {
        for (const privilege of tree) {
          this.treeMap.set(privilege.key, privilege);
          this.tree2map(privilege.children);
        }
      }
    },
    // 获取角色可选的功能权限
    async getListPrivilegeByRoleId(id) {
      try {
        let response = await privilegeApi.getListPrivilegeByRoleId(id);
        let datas = response.data;
        this.tree = datas.privilege;
        this.treeMap = new Map();
        this.tree2map(this.tree);
        this.checkedData = datas.selectedKey || [];
      } catch (e) {
        console.error(e);
      }
    }
  }
};
</script>

<style lang="less" scoped>
#tree {
  border: 1px solid #dcdee2;
  border-top: none;
}
.col-desc {
  margin: 20px 0;
  font-size: 15px;
  color: #95a5a6;
  padding: 0 20px;
}
.button-style {
  margin: 20px 0 20px 0;
  padding-left: 20px;
  text-align: right;
}
.check-right {
  margin-right: 20px;
}
.row-border {
  border: 1px solid #f0f0f0;
}
.col-border {
  line-height: 50px;
  padding-left: 20px;
  border-right: 1px solid #f0f0f0;
}
.col-left {
  line-height: 50px;
  padding-left: 40px;
  border-right: 1px solid #f0f0f0;
}
.col-right {
  padding-left: 20px;
  border-right: 1px solid #f0f0f0;
}
.ivu-tree ul li {
  white-space: normal;
}
.ivu-tree {
  > ul {
    > li {
      > ul {
        > li {
          .ivu-tree-title {
            vertical-align: middle;
            font-weight: bold;
          }
          > ul {
            display: inline-block;
            .ivu-tree-title {
              font-weight: normal;
            }
          }
        }
      }
    }
  }
}
.checked-box {
  padding: 0 15px;
  ul {
    padding: 0;
    margin: 0;
    li {
      list-style: none;
      padding: 0;
      margin: 0;
      .level-one {
        border-bottom: 1px solid rgb(240, 240, 240);
        padding: 10px 0;
      }
      .level-two {
        display: flex;
        align-items: center;
        margin-left: 4%;
        position: relative;
        border-bottom: 1px solid rgb(240, 240, 240);
        line-height: 40px;
        .level-two-label {
          width: 12%;
          min-width: 120px;
        }
        // &:before{ content: '';  position: absolute; height: 100%; width: 1px; background: rgb(240, 240, 240); left: 12%; top: 0; }
        .level-three {
          padding-left: 4%;
          display: block;
          flex: 1;
          min-height: 40px;
          border-left: 1px rgb(240, 240, 240) solid;
          .isLevel-four {
            display: flex;
            align-items: center;
            border-bottom: 1px rgb(240, 240, 240) solid;
            .level-three-label {
              width: 12%;
              min-width: 120px;
            }
            .level-four {
              padding-left: 4%;
              flex: 1;
              min-height: 40px;
              border-left: 1px rgb(240, 240, 240) solid;
              .level-five {
                padding-left: 4%;
                min-height: 40px;
                border-left: 1px rgb(240, 240, 240) solid;
              }
            }
          }
        }
      }
      .ivu-checkbox-wrapper {
        margin-right: 15px;
      }
    }
  }
}
</style>
