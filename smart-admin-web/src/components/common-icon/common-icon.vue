<template>
  <Icons :is="iconType" :type="iconName" :color="iconColor" :size="iconSize"/>
</template>

<script>
import Icons from '_c/icons';
export default {
  name: 'CommonIcon',
  components: { Icons },
  props: {
    // 图标名 eg: icon-name
    type: {
      type: String,
      required: true
    },
    // 图标颜色
    color: {
      type: String,
      required: false
    },
    // 图标尺寸
    size: {
      type: Number,
      required: false
    }
  },
  computed: {
    iconType () {
      return this.type.indexOf('_') === 0 ? 'Icons' : 'Icon';
    },
    iconName () {
      return this.iconType === 'Icons' ? this.getCustomIconName(this.type) : this.type;
    },
    iconSize () {
      return this.size || (this.iconType === 'Icons' ? 12 : undefined);
    },
    iconColor () {
      return this.color || '';
    }
  },
  methods: {
    // 返回图标名
    getCustomIconName (iconName) {
      return iconName.slice(1);
    }
  }
};
</script>

<style>

</style>
