<script>
  import { useUserStore } from '@/store/modules/system/user';
  export default {
    onLaunch: function () {
      useUserStore().getLoginInfo();
    },
    onShow: function () {
      console.log('App Show');
    },
    onHide: function () {
      console.log('App Hide');
    },
  };
</script>

<style lang="scss">
  @import '@/uni_modules/uni-scss/index.scss';
  /* 设置基准字体大小为16px */
  body {
    font-size: 16px;
  }
</style>
