<template>
  <div class="account-container">
    <!--菜单列-->
    <div class="account-menu-list">
      <a-menu v-model:selectedKeys="selectedKeys" mode="inline" @click="selectMenu($event.key)">
        <a-menu-item v-for="item in menuList" :key="item.menuId">
          <span v-if="item.menuId === 'message'">
            {{ item.menuName }}
            <a-badge :count="unreadMessageCount" style="margin-left: 10px" />
          </span>
          <span v-if="item.menuId !== 'message'">{{ item.menuName }} </span>
        </a-menu-item>
      </a-menu>
    </div>
    <!--内容区域-->
    <div class="account-content">
      <component :is="selectedMenu.components" />
    </div>
  </div>
</template>
<script setup>
  import { computed, onMounted, ref, watch } from 'vue';
  import _ from 'lodash';
  import { ACCOUNT_MENU } from '/@/views/system/account/account-menu.js';
  import { useRoute } from 'vue-router';
  import { useUserStore } from '/@/store/modules/system/user.js';

  // 菜单展示
  let menuList = computed(() => {
    return _.values(ACCOUNT_MENU);
  });
  // 选中的菜单
  let selectedMenu = ref({ menuId: 0 });
  let selectedKeys = computed(() => {
    return _.isEmpty(selectedMenu.value) ? [] : [selectedMenu.value.menuId];
  });

  function selectMenu(menuId) {
    selectedMenu.value = menuList.value.find((e) => e.menuId === menuId);
  }

  // ------------------------- 未读消息数量  -------------------------
  const unreadMessageCount = computed(() => {
    return useUserStore().unreadMessageCount;
  });

  // ------------------------- 绑定路由参数  -------------------------
  const route = useRoute();
  onMounted(() => {
    if (_.isEmpty(menuList.value)) {
      return;
    }
    let menuId;
    if (route.query.menuId) {
      menuId = route.query.menuId;
    } else {
      let firstMenu = menuList.value[0];
      menuId = firstMenu.menuId;
    }
    selectMenu(menuId);
  });

  watch(
    () => route.query,
    (newQuery, oldQuery) => {
      let menuId;
      if (route.query.menuId) {
        menuId = route.query.menuId;
      } else {
        let firstMenu = menuList.value[0];
        menuId = firstMenu.menuId;
      }
      selectMenu(menuId);
    }
  );
</script>
<style lang="less" scoped>
  .account-container {
    display: flex;
    height: 100%;
    background-color: white;
    padding: 20px 0;

    .account-menu-list {
      width: 180px;
      height: calc(100% - 100);
      border-right: solid 1px #efefef;
    }

    .account-content {
      flex: 1;
      margin-left: 10px;
      background: #ffffff;
      padding: 20px;
      border-radius: 8px;
    }
  }
</style>
