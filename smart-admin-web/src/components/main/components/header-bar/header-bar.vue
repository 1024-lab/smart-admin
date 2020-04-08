<template>
  <div class="header-bar">
    <SiderTrigger
      :collapsed="collapsed"
      @on-change="handleCollpasedChange"
      icon="icon iconfont icondaohangzhedie"
    />

    <div class="custom-bread-crumb" style="margin-left: 30px;">
      <div class="ivu-breadcrumb" >
         <Dropdown @on-click="changeTopMenu">
          <a href="javascript:void(0)" style="font-size:15px;font-weight:600">
            {{currentTopMenuTitle}}
            <Icon type="ios-arrow-down" style="font-size:16px"></Icon>
          </a>
          <DropdownMenu slot="list">
            <DropdownItem :key="i" v-for="(item,i) in topMenuArray" :name="item.name">
              <Icon :type="item.meta.icon" style="font-size: 14px;"/>
             <span style="font-size: 14px;"> {{item.meta.title}} </span>
            </DropdownItem>
          </DropdownMenu>
        </Dropdown>
        <i>
         <CustomBreadCrumb :list="breadCrumbList" show-icon style="margin-left: 20px;" />
        </i>
         <!-- <Tabs value="name1">
            <TabPane style="font-size:16px;font-weight:700;padding:12px 16px" label="标签一" name="name1"/>
            <TabPane style="font-size:16px;font-weight:700;padding:12px 16px" label="标签二" name="name2"/>
            <TabPane style="font-size:16px;font-weight:700;padding:12px 16px" label="标签三" name="name3">标签三的内容</TabPane>
        </Tabs> -->

      </div>
    </div>

    <div class="custom-content-con">
      <slot></slot>
    </div>
  </div>
</template>
<script>
import SiderTrigger from './sider-trigger';
import CustomBreadCrumb from './custom-bread-crumb';
import './header-bar.less';
export default {
  name: 'HeaderBar',
  components: {
    SiderTrigger,
    CustomBreadCrumb
  },
  props: {
    // 折叠状态
    collapsed: {
      type: Boolean,
      require: false
    },
    //顶级菜单
    topMenuArray:{
      type:Array,
      required:true
    },
    //当前顶级菜单名字
    currentTopMenuTitle:{
      type:String,
      required:true
    }
  },
  computed: {
    // 面包屑集合
    breadCrumbList() {
      return this.$store.state.app.breadCrumbList;
    }
  },
  methods: {
    handleCollpasedChange(state) {
      this.$emit('on-coll-change', state);
    },
    changeTopMenu(name){
      this.$emit('on-change-top-menu', name);
    }
  }
};
</script>
