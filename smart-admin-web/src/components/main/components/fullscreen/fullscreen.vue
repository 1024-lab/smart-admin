<template>
  <div v-if="showFullScreenBtn" class="full-screen-btn-con">
    <Tooltip :content="value ? '退出全屏' : '全屏'" placement="bottom">
      <Icon @click.native="handleChange"  :type="value ? 'md-contract' : 'md-expand'" :size="16"></Icon>
    </Tooltip>
  </div>
</template>

<script>
export default {
  name: 'Fullscreen',
  props: {
    // 当前全屏状态
    value: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    showFullScreenBtn () {
      return window.navigator.userAgent.indexOf('MSIE') < 0;
    }
  },
  mounted () {
    let isFullscreen = document.fullscreenElement || document.mozFullScreenElement || document.webkitFullscreenElement || document.fullScreen || document.mozFullScreen || document.webkitIsFullScreen;
    isFullscreen = !!isFullscreen;
    document.addEventListener('fullscreenchange', () => {
      this.$emit('input', !this.value);
      this.$emit('on-change', !this.value);
    });
    document.addEventListener('mozfullscreenchange', () => {
      this.$emit('input', !this.value);
      this.$emit('on-change', !this.value);
    });
    document.addEventListener('webkitfullscreenchange', () => {
      this.$emit('input', !this.value);
      this.$emit('on-change', !this.value);
    });
    document.addEventListener('msfullscreenchange', () => {
      this.$emit('input', !this.value);
      this.$emit('on-change', !this.value);
    });
    this.$emit('input', isFullscreen);
  },
  methods: {
    handleFullscreen () {
      let main = document.body;
      if (this.value) {
        if (document.exitFullscreen) {
          document.exitFullscreen();
        } else if (document.mozCancelFullScreen) {
          document.mozCancelFullScreen();
        } else if (document.webkitCancelFullScreen) {
          document.webkitCancelFullScreen();
        } else if (document.msExitFullscreen) {
          document.msExitFullscreen();
        }
      } else {
        if (main.requestFullscreen) {
          main.requestFullscreen();
        } else if (main.mozRequestFullScreen) {
          main.mozRequestFullScreen();
        } else if (main.webkitRequestFullScreen) {
          main.webkitRequestFullScreen();
        } else if (main.msRequestFullscreen) {
          main.msRequestFullscreen();
        }
      }
    },
    handleChange () {
      this.handleFullscreen();
    }
  }
};
</script>

<style lang="less">
.full-screen-btn-con .ivu-tooltip-rel {
  height: 52px;
  line-height: 52px;
  padding:0 12px;
    &:hover {
    background: #f7f7f8;
    cursor: pointer;
  }
  cursor: pointer;
  i {
    cursor: pointer;
    color:#909ca4;
  }
}
</style>
