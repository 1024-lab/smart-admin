<template>
  <div class="header-bar">
    <SiderTrigger :collapsed="collapsed" icon="icon iconfont icondaohangzhedie" @on-change="handleCollpasedChange"/>
    <CustomBreadCrumb show-icon style="margin-left: 30px;" :list="breadCrumbList"/>
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
    }
  },
  computed: {
    // 面包屑集合
    breadCrumbList () {
      return this.$store.state.app.breadCrumbList;
    }
  },
  methods: {
    handleCollpasedChange (state) {
      this.$emit('on-coll-change', state);
    }
  }
};
</script>
