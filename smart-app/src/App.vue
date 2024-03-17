<script>
  import { useUserStore } from '/@/store/modules/system/user';
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
</style>
