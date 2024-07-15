<template>
  <div class="account-container">
    <!--菜单列-->
    <div class="account-menu-list">
      <a-menu
          v-model:selectedKeys="selectedKeys"
          mode="inline"
          @click="selectMenu($event.key)"
      >
        <a-menu-item v-for="item in menuList" :key="item.menuId">
          {{ item.menuName }}
        </a-menu-item>
      </a-menu>
    </div>
    <!--内容区域-->
    <div class="account-content">
      <component :is="selectedMenu.components"/>
    </div>
  </div>
</template>
<script setup>
import {computed, defineAsyncComponent, markRaw, onMounted, ref} from "vue";
import _ from 'lodash';

/**
 * 菜单展示
 * defineAsyncComponent 异步组件
 * markRaw  将一个Vue组件对象转换为响应式对象时，可能会导致不必要的性能开销。使用markRaw方法将组件对象标记为非响应式对象
 */
let menuList = ref([
  {
    menuId: 1,
    menuName: '个人中心',
    components: markRaw(defineAsyncComponent(() => import('./components/center/index.vue')))
  },
  {
    menuId: 2,
    menuName: '我的消息',
    components: markRaw(defineAsyncComponent(() => import('./components/message/index.vue')))
  },
  {
    menuId: 3,
    menuName: '修改密码',
    components: markRaw(defineAsyncComponent(() => import('./components/password/index.vue')))
  },
  {
    menuId: 4,
    menuName: '安全设置',
    components: markRaw(defineAsyncComponent(() => import('./components/safe/index.vue')))
  }
])
// 选中的菜单
let selectedMenu = ref({menuId: 0});
let selectedKeys = computed(() => {
  return _.isEmpty(selectedMenu.value) ? [] : [selectedMenu.value.menuId];
})

function selectMenu(menuId) {
  selectedMenu.value = menuList.value.find(e => e.menuId === menuId);
}

onMounted(() => {
  if (_.isEmpty(menuList.value)) {
    return;
  }
  let firstMenu = menuList.value[0];
  selectMenu(firstMenu.menuId)
})
</script>
<style lang="less" scoped>
.account-container {
  display: flex;
  height: 100%;

  .account-menu-list {
    width: 224px;
    height: 100%;

    :deep(.ant-menu) {
      border-radius: 8px;
    }

    :deep(.ant-menu-item) {
      height: 60px !important;
    }
  }

  .account-content {
    flex: 1;
    margin-left: 10px;
    background: #FFFFFF;
    padding: 8px 40px;
    border-radius: 8px;
  }
}
</style>