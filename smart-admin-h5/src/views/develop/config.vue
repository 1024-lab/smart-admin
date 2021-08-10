<!-- home -->
<template>
  <div>
    <router-nav-bar path="/dashboard/user" title="开发专用配置" left-text="返回" left-arrow />

    <van-cell center title="VConsole">
      <van-switch v-model="vconsoleFlag" @change="switchVConsole" />
    </van-cell>
  </div>
</template>

<script>
import RouterNavBar from 'components/van-bar/RouterNavBar';
const VCONSOLE_ID = '__vconsole';
export default {
  name: 'DevelopConfig',
  components: {
    RouterNavBar
  },
  data() {
    return {
      vconsole: null,
      vconsoleFlag: false
    };
  },
  mounted() {
    this.checkVConsoleExist();
  },
  methods: {
    // 检测vconsole是否存在
    checkVConsoleExist() {
      const vconsoleElement = document.getElementById(VCONSOLE_ID);
      this.vconsoleFlag = !!vconsoleElement;
    },
    // 提交表单
    switchVConsole(flag) {
      if (flag) {
        this.vconsole = new VConsole();
      } else {
        delete this.vconsole;
        document.getElementById(VCONSOLE_ID).remove();
        this.vconsole = null;
      }
    }
  }
};
</script>
