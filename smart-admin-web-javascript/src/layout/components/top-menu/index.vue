<!--
  * 顶部菜单
  *
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2022-09-06 20:29:12
  * @Wechat:    zhuda1024
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
-->
<template>
  <!--总共3部分：1、logo区域，包含 logo和名称; 2、菜单区域  ;3、用户操作区域-->
  <div class="header-main">
    <!-- 1、logo区域 -->
    <div class="logo" @click="onGoHome">
      <img class="logo-img" :src="logoImg" />
      <div class="title smart-logo title-light" v-if="sideMenuTheme === 'light'">{{ websiteName }}</div>
      <div class="title smart-logo title-dark" v-if="sideMenuTheme === 'dark'">{{ websiteName }}</div>
    </div>
    <!-- 2、菜单区域 -->
    <RecursionMenu ref="menuRef" />

    <!-- 3、用户操作区域 -->
    <div class="user-space">
      <div class="setting">
        <!---消息通知--->
        <HeaderMessage ref="headerMessage" />
        <!---设置--->
        <a-button type="text" @click="showSetting" class="operate-icon">
          <template #icon><setting-outlined /></template>
        </a-button>
      </div>
      <!---头像信息--->
      <div class="user-space-item">
        <HeaderAvatar />
      </div>
      <HeaderSetting ref="headerSetting" />
    </div>
  </div>
</template>

<script setup>
  import { computed, ref, watch } from 'vue';
  import { useRouter } from 'vue-router';
  import RecursionMenu from './recursion-menu.vue';
  import logoImg from '/@/assets/images/logo/smart-admin-logo.png';
  import { HOME_PAGE_NAME } from '/@/constants/system/home-const';
  import { useAppConfigStore } from '/@/store/modules/system/app-config';
  import HeaderAvatar from '../header-user-space/header-avatar.vue';
  import HeaderSetting from '../header-user-space/header-setting.vue';
  import HeaderMessage from '../header-user-space/header-message.vue';

  // 设置
  const headerSetting = ref();
  function showSetting() {
    headerSetting.value.show();
  }

  //消息通知
  const headerMessage = ref();
  function showMessage() {
    headerMessage.value.showMessage();
  }

  const websiteName = computed(() => useAppConfigStore().websiteName);
  const sideMenuTheme = computed(() => useAppConfigStore().sideMenuTheme);

  const props = defineProps({
    collapsed: {
      type: Boolean,
      required: false,
      default: false,
    },
  });

  const menuRef = ref();

  watch(
    () => props.collapsed,
    (newValue, oldValue) => {
      // 如果是展开菜单的话，重新获取更新菜单的展开项: openkeys和selectKeys
      if (!newValue) {
        menuRef.value.updateSelectKeys();
      }
    }
  );

  const color = computed(() => {
    let isLight = useAppConfigStore().$state.sideMenuTheme === 'light';
    return {
      color: isLight ? '#001529' : '#FFFFFF',
      background: isLight ? '#FFFFFF' : '#001529',
    };
  });

  const router = useRouter();
  function onGoHome() {
    router.push({ name: HOME_PAGE_NAME });
  }
</script>

<style lang="less" scoped>
  .header-main {
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    width: 100%;
    padding-left: 16px;
    height: 48px;
    z-index: 21;
    border-bottom: 1px solid rgb(238, 238, 238);

    .logo {
      min-width: 192px;
      display: flex;
      flex-direction: row;
      justify-content: center;
      align-items: center;

      .logo-img {
        display: inline-block;
        height: 30px;
        vertical-align: middle;
      }
      .title {
        font-size: 16px;
        font-weight: 600;
        margin-left: 8px;
      }
      .title-light {
        color: #001529;
      }
      .title-dark {
        color: #ffffff;
      }
    }

    .user-space {
      min-width: 208px;
      margin-left: auto;
      padding-right: 10px;
      color: v-bind('color.color');
      display: flex;
      flex-direction: row;
      vertical-align: middle;
      align-items: center;
      justify-content: flex-end;

      .setting {
        height: @header-user-height;
        line-height: @header-user-height;
        vertical-align: middle;
        display: flex;
        align-items: center;

        :deep(.ant-badge) {
          color: v-bind('color.color');
        }
      }
      .operate-icon {
        margin-left: 20px;
        color: v-bind('color.color');
      }

      .user-space-item {
        margin-left: 10px;
      }
    }
  }

  :deep(.ant-menu-horizontal) {
    border-bottom: 0;
  }
</style>
