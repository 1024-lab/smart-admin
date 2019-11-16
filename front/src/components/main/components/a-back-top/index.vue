<template>
  <div :class="classes" :style="styles" @click="back">
    <slot>
      <div :class="innerClasses">
        <i class="ivu-icon ivu-icon-ios-arrow-up"></i>
      </div>
    </slot>
  </div>
</template>
<script>
import { on, off } from '@/lib/util';
const prefixCls = 'ivu-back-top';

export default {
  name: 'ABackTop',
  props: {
    // 允许出现返回头部高度
    height: {
      type: Number,
      default: 400
    },
    // 返回头部按钮 距离底部距离
    bottom: {
      type: Number,
      default: 30
    },
    // 返回头部按钮 距离窗口右侧距离
    right: {
      type: Number,
      default: 30
    },
    // 滚动速度
    duration: {
      type: Number,
      default: 1000
    },
    // 可视区
    container: {
      type: null,
      default: window
    }
  },
  data () {
    return {
      // 是否显示返回顶部按钮
      backTop: false
    };
  },
  mounted () {
    // window.addEventListener('scroll', this.handleScroll, false)
    // window.addEventListener('resize', this.handleScroll, false)
    on(this.containerEle, 'scroll', this.handleScroll);
    on(this.containerEle, 'resize', this.handleScroll);
  },
  beforeDestroy () {
    // window.removeEventListener('scroll', this.handleScroll, false)
    // window.removeEventListener('resize', this.handleScroll, false)
    off(this.containerEle, 'scroll', this.handleScroll);
    off(this.containerEle, 'resize', this.handleScroll);
  },
  computed: {
    classes () {
      return [
        `${prefixCls}`,
        {
          [`${prefixCls}-show`]: this.backTop
        }
      ];
    },
    styles () {
      return {
        bottom: `${this.bottom}px`,
        right: `${this.right}px`
      };
    },
    innerClasses () {
      return `${prefixCls}-inner`;
    },
    containerEle () {
      return this.container === window ? window : document.querySelector(this.container);
    }
  },
  methods: {
    handleScroll () {
      this.backTop = this.containerEle.scrollTop >= this.height;
    },
    back () {
      let target = typeof this.container === 'string' ? this.containerEle : (document.documentElement || document.body);
      const sTop = target.scrollTop;
      this.scrollTop(this.containerEle, sTop, 0, this.duration);
      this.$emit('on-click');
    },
    // scrollTop animation
    scrollTop (el, from = 0, to, duration = 500, endCallback) {
      if (!window.requestAnimationFrame) {
        window.requestAnimationFrame = (
          window.webkitRequestAnimationFrame ||
      window.mozRequestAnimationFrame ||
      window.msRequestAnimationFrame ||
      function (callback) {
        return window.setTimeout(callback, 1000 / 60);
      }
        );
      }
      const difference = Math.abs(from - to);
      const step = Math.ceil(difference / duration * 50);

      const scroll = (start, end, step) => {
        if (start === end) {
          endCallback && endCallback();
          return;
        }

        let d = (start + step > end) ? end : start + step;
        if (start > end) {
          d = (start - step < end) ? end : start - step;
        }

        if (el === window) {
          window.scrollTo(d, d);
        } else {
          el.scrollTop = d;
        }
        window.requestAnimationFrame(() => scroll(d, end, step));
      };
      scroll(from, to, step);
    }
  }
};
</script>
