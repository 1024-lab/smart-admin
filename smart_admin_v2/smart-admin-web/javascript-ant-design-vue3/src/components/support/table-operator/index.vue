<!--
  *  表格列设置
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-08-26 23:45:51 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
  *
-->

<template>
  <span>
    <a-tooltip title="全屏" v-if="!fullScreenFlag">
      <a-button type="text" @click="fullScreen" size="small">
        <template #icon><fullscreen-outlined /></template>
      </a-button>
    </a-tooltip>
    <a-tooltip title="取消全屏" v-if="fullScreenFlag">
      <a-button type="text" @click="fullScreen" size="small">
        <template #icon><fullscreen-exit-outlined /></template>
      </a-button>
    </a-tooltip>
    <a-tooltip title="刷新">
      <a-button type="text" @click="props.refresh" size="small">
        <template #icon><redo-outlined /></template>
      </a-button>
    </a-tooltip>
    <a-tooltip title="列设置">
      <a-button type="text" @click="showModal" size="small">
        <template #icon><setting-outlined /></template>
      </a-button>
    </a-tooltip>
    <SmartTableColumnModal ref="smartTableColumnModal" @change="updateColumn" />
  </span>
</template>

<script setup>
  import _ from 'lodash';
  import { tableColumnApi } from '/@/api/support/table/table-column-api';
  import { onMounted, reactive, ref, watch } from 'vue';
  import SmartTableColumnModal from './smart-table-column-modal.vue';
  import { message } from 'ant-design-vue';
  import { mergeColumn } from './smart-table-column-merge';
  import { smartSentry } from '/@/lib/smart-sentry';
  const props = defineProps({
    // 表格列数组
    modelValue: {
      type: Array,
      default: new Array(),
    },
    // 刷新表格函数
    refresh: {
      type: Function,
      required: true,
    },
    // 表格id
    tableId: {
      type: Number,
      require: true,
    },
  });

  const emit = defineEmits(['update:modelValue']);

  // 原始表格列数据（复制一份最原始的columns集合，以供后续各个地方使用）
  let originalColumn = _.cloneDeep(props.modelValue);

  onMounted(buildUserTableColumns);

  //构建用户的数据列
  async function buildUserTableColumns() {
    if (!props.tableId) {
      return;
    }

    let userTableColumnArray = [];
    try {
      let res = await tableColumnApi.getColumns(props.tableId);
      if (res.data) {
        try {
          userTableColumnArray = JSON.parse(res.data);
        } catch (e1) {
          smartSentry.captureError(e1);
        }
      }
    } catch (e) {
      smartSentry.captureError(e);
    }

    updateColumn(userTableColumnArray);
  }

  // ----------------- 全屏 -------------------
  const fullScreenFlag = ref(false);
  function fullScreen() {
    if (fullScreenFlag.value) {
      //取消全屏
      exitFullscreen(document.querySelector('#smartAdminLayoutContent'));
      fullScreenFlag.value = false;
    } else {
      //全屏
      launchFullScreen(document.querySelector('#smartAdminLayoutContent'));
      fullScreenFlag.value = true;
    }
  }

  //判断各种浏览器 -全屏
  function launchFullScreen(element) {
    if (element.requestFullscreen) {
      element.requestFullscreen();
    } else if (element.mozRequestFullScreen) {
      element.mozRequestFullScreen();
    } else if (element.webkitRequestFullScreen) {
      element.webkitRequestFullScreen();
    } else if (element.msRequestFullscreen) {
      element.msRequestFullscreen();
    } else {
      message.error('当前浏览器不支持部分全屏！');
    }
  }
  //判断各种浏览器 -退出全屏
  function exitFullscreen(element) {
    if (document.exitFullscreen) {
      document.exitFullscreen();
    } else if (document.mozCancelFullScreen) {
      document.mozCancelFullScreen();
    } else if (document.webkitExitFullscreen) {
      document.webkitExitFullscreen();
    } else if (document.msExitFullscreen) {
      document.msExitFullscreen();
    }
  }

  // ----------------- 弹窗 修改表格列 -------------------

  const smartTableColumnModal = ref();
  function showModal() {
    smartTableColumnModal.value.show(originalColumn, props.tableId);
  }

  // 将弹窗修改的列数据，赋值给原表格 列数组
  function updateColumn(changeColumnArray) {
    //合并列
    const newColumns = mergeColumn(_.cloneDeep(originalColumn), changeColumnArray);
    emit(
      'update:modelValue',
      newColumns.filter((e) => e.showFlag)
    );
  }

  // ========= 定义 watch 监听 ===============
  watch(
    () => props.tableId,
    (e) => {
      if (e) {
        originalColumn = _.cloneDeep(props.modelValue);
        buildUserTableColumns();
      }
    },
    { immediate: true }
  );
</script>
