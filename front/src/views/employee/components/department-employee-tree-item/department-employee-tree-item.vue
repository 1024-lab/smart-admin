<template>
  <span @click="selectItem" style="display: inline-block">
    <span
      :style="itemData.selected ? selectStyle : currentStyle "
      @mouseout="onMouseout"
      @mouseover="onMouseover"
    >
      <Icon :type="itemData.icon" style="margin-right: 8px" />
      {{itemData.title}}
    </span>
  </span>
</template>

<script>
export default {
  name: 'DepartmentEmployeeTreeItem',
  components: {},
  props: {
    // 数据样式 是否选中 图标
    // {
    //   title: department.name,
    //   icon: icon,
    //   isEmployee: false,
    //   id: department.id,
    //   selectFunction: obj => {
    //     if (this.isDepartment) {
    //     }
    //   }
    // }
    itemData: {
      type: Object,
      required: true
    },
    // true 查部门
    isDepartment: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      currentStyle: {
        backgroundColor: '#FFF',
        color: '#495060',
        padding: '3px 8px'
      },
      hoverStyle: {
        backgroundColor: '#2d8cf0',
        color: '#FFF',
        padding: '3px 8px'
      },
      selectStyle: {
        backgroundColor: '#2d8cf0',
        color: '#FFF',
        padding: '3px 8px'
      },
      style: {
        backgroundColor: '#FFF',
        color: '#495060',
        padding: '3px 8px'
      }
    };
  },
  computed: {},
  watch: {},
  filters: {},
  created() {
    if (this.itemData.eventbus) {
      this.itemData.eventbus.$on('select', this.listenSelect);
    }
  },
  mounted() {},
  beforeCreate() {},
  beforeMount() {},
  beforeUpdate() {},
  updated() {},
  beforeDestroy() {
    if (this.itemData.eventbus) {
      this.itemData.eventbus.$off('select', this.listenSelect);
    }
  },
  destroyed() {},
  activated() {},
  methods: {
    // 点击选中当前部门或成员
    selectItem() {
      if (
        (!this.isDepartment && this.itemData.isEmployee) ||
        this.isDepartment
      ) {
        if (this.itemData.selectFunction) {
          this.itemData.selectFunction({
            id: this.itemData.id,
            name: this.itemData.title
          });
        }
      }
    },
    //
    listenSelect(obj) {
      if (obj.id === this.itemData.id) {
        this.itemData.selected = true;
      } else {
        this.currentStyle = this.style;
        this.itemData.selected = false;
      }
    },
    // 鼠标移入效果
    onMouseover() {
      if (this.itemData.selected === null || !this.itemData.selected) {
        this.currentStyle = this.hoverStyle;
      }
    },
    // 鼠标移出效果
    onMouseout() {
      if (this.itemData.selected === null || !this.itemData.selected) {
        this.currentStyle = this.style;
      }
    }
  }
};
</script>
<style lang="less" scoped>
</style>
