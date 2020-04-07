<template>
  <div class="custom-bread-crumb">
    <Breadcrumb :style="{fontSize: `${fontSize}px`}">
      <BreadcrumbItem v-for="item in list" :to="item.to" :key="`bread-crumb-${item.name}`">
        {{ showTitle(item) }}
      </BreadcrumbItem>
    </Breadcrumb>
  </div>
</template>
<script>
import { showTitle } from '@/lib/menu-func';
import './custom-bread-crumb.less';
export default {
  name: 'CustomBreadCrumb',
  props: {
    // 面包屑集合
    list: {
      type: Array,
      default: () => []
    },
    // 面包屑显示字体大小
    fontSize: {
      type: Number,
      default: 12
    },
    // 是否显示图标
    showIcon: {
      type: Boolean,
      default: false
    }
  },
  methods: {
    showTitle (item) {
      return showTitle(item, this);
    },
    isCustomIcon (iconName) {
      return iconName.indexOf('_') === 0;
    },
    getCustomIconName (iconName) {
      return iconName.slice(1);
    }
  }
};
</script>
