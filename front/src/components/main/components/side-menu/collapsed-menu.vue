<template>
  <Dropdown ref="dropdown"
            @on-click="handleClick"
            :class="hideTitle ? '' : 'collased-menu-dropdown'"
            :transfer="hideTitle"
            :placement="placement"
            transfer-class-name="menu-dropdown">
    <a class="drop-menu-a"
       type="text"
       @mouseover="handleMousemove($event, children)"
       :style="{textAlign: !hideTitle ? 'left' : ''}">
      <CommonIcon :size="rootIconSize"
                   :color="textColor"
                   :type="parentItem.icon" />
      <span class="menu-title"
            v-if="!hideTitle">{{ showTitle(parentItem) }}</span>
      <Icon style="float: right;"
            v-if="!hideTitle"
            type="ios-arrow-forward"
            :size="16" />
    </a>
    <DropdownMenu ref="dropdown"
                  slot="list">
      <template v-for="child in children">
        <CollapsedMenu v-if="showChildren(child)"
                        :icon-size="iconSize"
                        :parent-item="child"
                        :key="`drop-${child.name}`"/>
        <DropdownItem v-else
                      :key="`drop-${child.name}`"
                      :name="child.name">
          <CommonIcon :size="iconSize"
                       :type="child.icon" />
          <span class="menu-title">{{ showTitle(child) }}</span>
        </DropdownItem>
      </template>
    </DropdownMenu>
  </Dropdown>
</template>
<script>
import mixin from './mixin';
import itemMixin from './item-mixin';
import { findNodeUpperByClasses } from '@/lib/menu-func';

export default {
  name: 'CollapsedMenu',
  mixins: [ mixin, itemMixin ],
  props: {
    // 是否隐藏标题
    hideTitle: {
      type: Boolean,
      default: false
    },
    // 图标尺寸
    rootIconSize: {
      type: Number,
      default: 16
    }
  },
  data () {
    return {
      // 下拉菜单出现的位置，
      // 可选值为top,top-start,top-end,bottom,bottom-start,bottom-end,left,left-start,left-end,
      // right,right-start,right-end, 2.12.0 版本开始支持自动识别
      placement: 'right-end'
    };
  },
  mounted () {
    let dropdown = findNodeUpperByClasses(this.$refs.dropdown.$el, ['ivu-select-dropdown', 'ivu-dropdown-transfer']);
    if (dropdown) dropdown.style.overflow = 'visible';
  },
  methods: {
    handleClick (name) {
      this.$emit('on-click', name);
    },
    handleMousemove (event, children) {
      const { pageY } = event;
      const height = children.length * 38;
      const isOverflow = pageY + height < window.innerHeight;
      this.placement = isOverflow ? 'right-start' : 'right-end';
    }
  }
};
</script>
