<!--
  * chrome样式 <a-tabs> 组件
  *
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2024-11-27 20:29:12
  * @Wechat:    zhuda1024
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
-->
<template>
  <!-- 标签页，共两部分：1、标签 ；2、标签操作区 -->
  <a-row style="border-bottom: 1px solid #eeeeee; position: relative" v-show="pageTagFlag">
    <a-dropdown :trigger="['contextmenu']">
      <div class="smart-page-tag">
        <a-tabs style="width: 100%" :tab-position="mode" v-model:activeKey="selectedKey" size="small" @tabClick="selectTab">
          <a-tab-pane v-for="item in tagNav" :key="item.menuName">
            <template #tab>
              <span class="smart-page-tag-content">
                <home-outlined style="font-size: 12px" v-if="item.menuName === HOME_PAGE_NAME" class="smart-page-tag-close" />
                <component class="smart-page-tag-icon" v-else :is="$antIcons[item.menuIcon]" />
                {{ item.menuTitle }}
                <close-outlined @click.stop="closeTag(item, false)" v-if="item.menuName !== HOME_PAGE_NAME" class="smart-page-tag-close" />
              </span>
            </template>
          </a-tab-pane>
        </a-tabs>
      </div>
      <template #overlay>
        <a-menu>
          <a-menu-item @click="closeByMenu(false)">关闭其他</a-menu-item>
          <a-menu-item @click="closeByMenu(true)">关闭所有</a-menu-item>
        </a-menu>
      </template>
    </a-dropdown>

    <a-dropdown>
      <!--标签页操作区-->
      <div class="smart-page-tag-operate">
        <div class="smart-page-tag-operate-icon">
          <AppstoreOutlined />
        </div>
      </div>
      <template #overlay>
        <a-menu>
          <a-menu-item @click="closeByMenu(false)">关闭其他</a-menu-item>
          <a-menu-item @click="closeByMenu(true)">关闭所有</a-menu-item>
        </a-menu>
      </template>
    </a-dropdown>
  </a-row>
</template>

<script setup lang="ts">
import { AppstoreOutlined, CloseOutlined } from '@ant-design/icons-vue';
import { computed, ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { HOME_PAGE_NAME } from '/@/constants/system/home-const';
import { useAppConfigStore } from '/@/store/modules/system/app-config';
import { useUserStore } from '/@/store/modules/system/user';
import { theme } from 'ant-design-vue';

//标签页 是否显示
const pageTagFlag = computed(() => useAppConfigStore().$state.pageTagFlag);

const router = useRouter();
const route = useRoute();
const mode = ref('top');
const tagNav = computed(() => useUserStore().getTagNav || []);
const selectedKey = ref(route.name);

watch(
  () => route.name,
  (newValue, oldValue) => {
    selectedKey.value = newValue;
  },
  { immediate: true }
);

//选择某个标签页
function selectTab(name) {
  if (selectedKey.value === name) {
    return;
  }
  // 寻找tag
  let tag = tagNav.value.find((e) => e.menuName === name);
  if (!tag) {
    router.push({ name: HOME_PAGE_NAME });
    return;
  }
  // router.push({ name, query: Object.assign({ _keepAlive: 1 }, tag.menuQuery) });
  router.push({ name, query: tag.menuQuery });
}

//通过菜单关闭
function closeByMenu(closeAll) {
  let find = tagNav.value.find((e) => e.menuName === selectedKey.value);
  if (!find || closeAll) {
    closeTag(null, true);
  } else {
    closeTag(find, true);
  }
}

//直接关闭
function closeTag(item, closeAll) {
  // 关闭单个tag
  if (item && !closeAll) {
    let goName = HOME_PAGE_NAME;
    let goQuery = undefined;
    if (item.fromMenuName && item.fromMenuName !== item.menuName && tagNav.value.some((e) => e.menuName === item.fromMenuName)) {
      goName = item.fromMenuName;
      goQuery = item.fromMenuQuery;
    } else {
      // 查询左侧tag
      let index = tagNav.value.findIndex((e) => e.menuName === item.menuName);
      if (index > 0) {
        // 查询左侧tag
        let leftTagNav = tagNav.value[index - 1];
        goName = leftTagNav.menuName;
        goQuery = leftTagNav.menuQuery;
      }
    }
    // router.push({ name: goName, query: Object.assign({ _keepAlive: 1 }, goQuery) });
    router.push({ name: goName, query: goQuery });
  } else if (!item && closeAll) {
    // 关闭所有tag
    router.push({ name: HOME_PAGE_NAME });
  }
  // 关闭其他tag不做处理 直接调用closeTagNav
  useUserStore().closeTagNav(item ? item.menuName : null, closeAll);
}

const { useToken } = theme;
const { token } = useToken();
const borderRadius = 8 + 'px';
</script>

<style scoped lang="less">
@smart-page-tag-operate-width: 40px;
@color-primary: v-bind('token.colorPrimary');

.smart-page-tag-operate {
  width: @smart-page-tag-operate-width;
  height: @smart-page-tag-operate-width;
  background-color: #ffffff;
  font-size: 17px;
  text-align: center;
  vertical-align: middle;
  line-height: @smart-page-tag-operate-width;
  padding-right: 10px;
  cursor: pointer;
  color: #606266;

  .smart-page-tag-operate-icon {
    width: 20px;
    height: 20px;
    transition: all 1s;
    transform-origin: 10px 20px;
  }

  .smart-page-tag-operate-icon:hover {
    width: 20px;
    height: 20px;
    transform: rotate(360deg);
  }
}

.smart-page-tag-operate:hover {
  color: @color-primary;
}

.smart-page-tag {
  position: relative;
  box-sizing: border-box;
  display: flex;
  align-content: center;
  align-items: flex-end;
  justify-content: space-between;
  min-height: @page-tag-height;
  padding-right: 20px;
  padding-left: 20px;
  user-select: none;
  background: #fff;
  width: calc(100% - @smart-page-tag-operate-width);

  .smart-page-tag-close {
    margin-left: 5px;
    font-size: 12px;
    color: #666666;
  }

  /**  覆盖 ant design vue的 tabs 样式，变小一点 **/

  :deep(.ant-tabs-nav) {
    margin: 0;
    //   padding: 0 0 2px 0;
    min-height: 35px;
    min-width: 120px;
    box-sizing: border-box;
  }

  :deep(.ant-tabs-nav::before) {
    border-bottom: 1px solid #ffffff;
  }

  :deep(.ant-tabs-small > .ant-tabs-nav .ant-tabs-tab) {
    padding: 5px 18px 3px 24px;
    border-radius: v-bind(borderRadius) v-bind(borderRadius) 0 0;
    margin: 0 -10px;
    &:nth-child(1) {
      margin-left: 0 !important;
    }
    &:nth-last-child(2) {
      margin-right: 0 !important;

    }
  }
  .smart-page-tag-content {
    display: inline-block;
    min-width: 100px;
    &::after {
      content: '';
      position: absolute;
      width: 1px;
      height: 16px;
      position: absolute;
      right: 9px;
      z-index: -2;
      top: 10px;
      background: #eeeeee;
    }
    .smart-page-tag-icon{
        margin-right:5px;
    }
  }
  :deep(.ant-tabs-tab-active) {
    position: relative;
    background-size: 60% 100%;
    & + .ant-tabs-tab {
      margin-left: -50px;
    }

    &::before {
      content: '';
      background: url(/@/assets/images/nav/active_bg2.svg) no-repeat left;
      width: 50%;
      height: 35px;
      //   background-size: 130%;
      z-index: -1;
      position: absolute;
      left: -4px;
      bottom: 0;
    }
    &::after {
      content: '';
      background: url(/@/assets/images/nav/active_bg2.svg) no-repeat left;
      width: 50%;
      height: 35px;
      transform: scaleX(-1);
      //   background-size: 130%;
      z-index: -1;
      position: absolute;
      right: -4px;
      bottom: 0;
    }
    .smart-page-tag-content {
      &::before {
        content: '';
        position: absolute;
        height: 35px;
        background: #e9efff;
        width: 60%;
        left: 0;
        right: 0;
        // top: 0;
        bottom: 0;
        margin: auto;
        z-index: -1;
      }
      &::after {
        display: none;
      }
    }
    .smart-page-tag-close {
      color: @color-primary;
    }
  }
  :deep(.ant-tabs-ink-bar) {
    display: none;
  }
  :deep(.ant-tabs-nav .ant-tabs-tab:hover) {
    &:not(.ant-tabs-tab-active) {
      &::before {
        content: '';
        background: url(/@/assets/images/nav/active_bg2_default.svg) no-repeat left;
        width: 50%;
        height: 35px;
        //   background-size: 130%;
        z-index: -2;
        position: absolute;
        left: -4px;
        bottom: 0;
      }
      &::after {
        content: '';
        background: url(/@/assets/images/nav/active_bg2_default.svg) no-repeat left;
        width: 50%;
        height: 35px;
        transform: scaleX(-1);
        //   background-size: 130%;
        z-index: -1;
        position: absolute;
        right: -4px;
        bottom: 0;
      }
      .smart-page-tag-content {
        color:rgba(0,0,0,.88);
        &::before {
          content: '';
          position: absolute;
          height: 35px;
          background: #dee1e6;
          width: 60%;
          left: 0;
          right: 0;
          // top: 0;
          bottom: 0;
          margin: auto;
          z-index: -1;
        }
        &::after {
          display: none;
        }
      }
    }

    .smart-page-tag-close {
      color: @color-primary;
    }
  }
}
</style>
