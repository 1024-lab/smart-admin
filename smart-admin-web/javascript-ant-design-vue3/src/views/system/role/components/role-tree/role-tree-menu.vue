<!--
  * 角色 菜单
  *
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2022-09-12 22:34:00
  * @Wechat:    zhuda1024
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
  *
-->
<template>
  <li v-for="module in props.tree" :key="module.menuId">
    <div class="menu" :style="{ marginLeft: `${props.index * 4}%` }">
      <a-checkbox @change="selectCheckbox(module)" class="checked-box-label" :value="module.menuId">{{ module.menuName }} </a-checkbox>
      <div v-if="module.children && module.children.some((e) => e.menuType == MENU_TYPE_ENUM.POINTS.value)">
        <RoleTreePoint :tree="module.children" @selectCheckbox="selectCheckbox" />
      </div>
    </div>
    <template v-if="module.children && !module.children.some((e) => e.menuType == MENU_TYPE_ENUM.POINTS.value)">
      <RoleTreeMenu :tree="module.children" :index="props.index + 1" />
    </template>
  </li>
</template>
<script setup>
  import { MENU_TYPE_ENUM } from '/src/constants/system/menu-const';
  import { useRoleStore } from '/src/store/modules/system/role';
  import RoleTreePoint from './role-tree-point.vue';
  import RoleTreeMenu from './role-tree-menu.vue';

  const props = defineProps({
    tree: {
      type: Array,
      default: [],
    },
    index: {
      type: Number,
      default: 0,
    },
  });
  defineEmits(['update:value']);
  let roleStore = useRoleStore();
  function selectCheckbox(module) {
    if (!module.menuId) {
      return;
    }
    // 是否勾选
    let checkedData = roleStore.checkedData;
    let findIndex = checkedData.indexOf(module.menuId);
    // 选中
    if (findIndex == -1) {
      // 选中本级以及子级
      roleStore.addCheckedDataAndChildren(module);
      // 选中上级
      roleStore.selectUpperLevel(module);
      // 是否有关联菜单 有则选中
      if (module.contextMenuId) {
        roleStore.addCheckedData(module.contextMenuId);
      }
    } else {
      // 取消选中本级以及子级
      roleStore.deleteCheckedDataAndChildren(module);
    }
  }
</script>
