<!--
  * 主应用页面
  *
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2022-09-12 23:46:47
  * @Wechat:    zhuda1024
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
-->

<template>
  <a-config-provider
    :locale="antdLocale"
    :theme="{
      algorithm: themeAlgorithm,
      token: {
        colorPrimary: themeColors[colorIndex].primaryColor,
        colorLink: themeColors[colorIndex].primaryColor,
        colorLinkActive: themeColors[colorIndex].activeColor,
        colorLinkHover: themeColors[colorIndex].hoverColor,
        colorIcon: themeColors[colorIndex].primaryColor,
        borderRadius: borderRadius,
      },
      components: {
        Button: {
          colorLink: themeColors[colorIndex].primaryColor,
          colorLinkActive: themeColors[colorIndex].activeColor,
          colorLinkHover: themeColors[colorIndex].hoverColor,
        },
        Icon: {
          colorIcon: themeColors[colorIndex].primaryColor,
        },
      },
    }"
    :transformCellText="transformCellText"
  >
    <!---全局loading--->
    <a-spin :spinning="spinning" tip="稍等片刻，我在拼命加载中..." size="large">
      <!--- 路由 -->
      <RouterView />
    </a-spin>
  </a-config-provider>
</template>

<script setup>
  import dayjs from 'dayjs';
  import { computed, h, useSlots } from 'vue';
  import { messages } from '/@/i18n';
  import { useAppConfigStore } from '/@/store/modules/system/app-config';
  import { useSpinStore } from '/@/store/modules/system/spin';
  import { Popover, theme } from 'ant-design-vue';
  import { themeColors } from '/@/theme/color.js';
  import SmartCopyIcon from '/@/components/framework/smart-copy-icon/index.vue';

  const antdLocale = computed(() => messages[useAppConfigStore().language].antdLocale);
  const dayjsLocale = computed(() => messages[useAppConfigStore().language].dayjsLocale);
  dayjs.locale(dayjsLocale);

  // 全局loading
  let spinStore = useSpinStore();
  const spinning = computed(() => spinStore.loading);
  // 主题颜色
  const colorIndex = computed(() => {
    return useAppConfigStore().colorIndex;
  });
  // 主题
  const themeAlgorithm = computed(() => {
    let themeArray = [];
    themeArray.push(useAppConfigStore().darkModeFlag ? theme.darkAlgorithm : theme.defaultAlgorithm);
    if (useAppConfigStore().compactFlag) {
      themeArray.push(theme.compactAlgorithm);
    }
    return themeArray;
  });
  // 圆角
  const borderRadius = computed(() => {
    return useAppConfigStore().borderRadius;
  });

  function transformCellText({ text, column, record, index }) {
    if (column && column.textEllipsisFlag === true) {
      return h(
        Popover,
        { placement: 'bottom' },
        {
          default: () =>
            h(
              'div',
              {
                style: { whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' },
                id: `${column.dataIndex}${index}`,
              },
              text
            ),
          content: () =>
            h('div', { style: { display: 'flex' } }, [
              h('div', text),
              h(SmartCopyIcon, { value: document.getElementById(`${column.dataIndex}${index}`).innerText }),
            ]),
        }
      );
    } else {
      return text;
    }
  }

  const { useToken } = theme;
  const { token } = useToken();
</script>
<style lang="less">
  @color-bg-container: v-bind('token.colorBgContainer');

  :deep(.ant-table-column-sorters) {
    align-items: flex-start !important;
  }

  .smart-query-form {
    background-color: @color-bg-container;
    padding: 5px 10px;
    margin-bottom: 10px;
  }

  .smart-detail-header {
    background-color: @color-bg-container;
    padding: 10px;
  }
</style>
