<template>
  <div class="side-menu-wrapper">
    <slot></slot>
    <Menu
      :accordion="accordion"
      :active-name="activeName"
      :open-names="openedNames"
      :theme="theme"
      @on-select="handleSelect"
      ref="menu"
      v-show="!collapsed"
      width="auto"
    >
      <template v-for="item in menuList">
        <template v-if="item.children && item.children.length === 1">
          <side-menu-item :key="`menu-${item.name}`" :parent-item="item" v-if="showChildren(item)"></side-menu-item>
          <menu-item
            :key="`menu-${item.children[0].name}`"
            :name="getNameOrHref(item, true)"
            v-else
          >
            <common-icon :type="item.children[0].icon || ''" />
            <span>{{ showTitle(item.children[0]) }}</span>
          </menu-item>
        </template>
        <template v-else>
          <side-menu-item :key="`menu-${item.name}`" :parent-item="item" v-if="showChildren(item)"></side-menu-item>
          <menu-item :key="`menu-${item.name}`" :name="getNameOrHref(item)" v-else>
            <common-icon :type="item.icon || ''" />
            <span>{{ showTitle(item) }}</span>
          </menu-item>
        </template>
      </template>
    </Menu>
    <div :list="menuList" class="menu-collapsed" v-show="collapsed">
      <template v-for="item in menuList">
        <CollapsedMenu
          :icon-size="iconSize"
          :key="`drop-menu-${item.name}`"
          :parent-item="item"
          :root-icon-size="rootIconSize"
          :theme="theme"
          @on-click="handleSelect"
          hide-title
          v-if="item.children && item.children.length > 1"
        ></CollapsedMenu>
        <Tooltip
          :content="showTitle(item.children && item.children[0] ? item.children[0] : item)"
          :key="`drop-menu-${item.name}`"
          placement="right"
          transfer
          v-else
        >
          <a
            :style="{textAlign: 'center'}"
            @click="handleSelect(getNameOrHref(item, true))"
            class="drop-menu-a"
          >
            <CommonIcon
              :color="textColor"
              :size="rootIconSize"
              :type="item.icon || (item.children && item.children[0].icon)"
            />
          </a>
        </Tooltip>
      </template>
    </div>
  </div>
</template>
<script>
import SideMenuItem from './side-menu-item.vue';
import CollapsedMenu from './collapsed-menu.vue';
import { getUnion } from '@/lib/util';
import mixin from './mixin';

export default {
  name: 'SideMenu',
  mixins: [mixin],
  components: {
    SideMenuItem,
    CollapsedMenu
  },
  props: {
    // 菜单集合
    menuList: {
      type: Array,
      default() {
        return [];
      }
    },
    // 显示状态 如果是折叠状态就不显示
    collapsed: {
      type: Boolean
    },
    // 主题
    theme: {
      type: String,
      default: 'dark'
    },
    // 父图标尺寸
    rootIconSize: {
      type: Number,
      default: 16
    },
    // 图标尺寸
    iconSize: {
      type: Number,
      default: 16
    },
    // 是否开启手风琴模式，开启后每次至多展开一个子菜单
    accordion: {
      type: Boolean,
      require: false
    },
    // 激活菜单的 name 值
    activeName: {
      type: String,
      default: ''
    },
    // 展开的 Submenu 的 name 集合
    openNames: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      openedNames: []
    };
  },
  computed: {
    textColor() {
      return this.theme === 'dark' ? '#fff' : '#495060';
    }
  },
  watch: {
    activeName(name) {
      if (this.accordion) {
        this.openedNames = this.getOpenedNamesByActiveName(name);
      } else {
        this.openedNames = getUnion(
          this.openedNames,
          this.getOpenedNamesByActiveName(name)
        );
      }
    },
    openNames(newNames) {
      this.openedNames = newNames;
    },
    openedNames() {
      this.$nextTick(() => {
        this.$refs.menu.updateOpened();
      });
    }
  },
  mounted() {
    this.openedNames = getUnion(
      this.openedNames,
      this.getOpenedNamesByActiveName(name)
    );
  },
  methods: {
    updateActiveName(name){
      this.$nextTick(() => {
        this.$refs.menu.updateOpened();
        this.$refs.menu.updateActiveName(name);
      });
    },
    handleSelect(name) {
      this.$emit('on-select', name);
    },
    // 从激活菜单的名称中获取打开的菜单
    getOpenedNamesByActiveName(name) {
      return this.$route.matched
        .map(item => item.name)
        .filter(item => item !== name);
    },
    updateOpenName(name) {
      if (name === this.$config.homeName) this.openedNames = [];
      else this.openedNames = this.getOpenedNamesByActiveName(name);
    }
  }
};
</script>
<style lang="less">
@import './side-menu.less';
</style>
