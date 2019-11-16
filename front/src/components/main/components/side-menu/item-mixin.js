export default {
  props: {
    // 父文件
    parentItem: {
      type: Object,
      default: () => { }
    },
    // 主题
    theme: {
      type: String,
      require: false
    },
    // 图标尺寸
    iconSize: {
      type: Number,
      require: false
    }
  },
  computed: {
    parentName () {
      return this.parentItem.name;
    },
    children () {
      return this.parentItem.children;
    },
    textColor () {
      return this.theme === 'dark' ? '#fff' : '#495060';
    }
  }
};
