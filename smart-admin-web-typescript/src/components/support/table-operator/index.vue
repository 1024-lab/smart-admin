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
      <a-button type="text" @click="onFullScreen" size="small">
        <template #icon><fullscreen-outlined /></template>
      </a-button>
    </a-tooltip>
    <a-tooltip title="取消全屏" v-if="fullScreenFlag">
      <a-button type="text" @click="onFullScreen" size="small">
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

<script setup lang="ts">
  import _ from 'lodash';
  import { tableColumnApi } from '/@/api/support/table-column-api';
  import { onMounted, ref, watch, reactive } from 'vue';
  import SmartTableColumnModal from './smart-table-column-modal.vue';
  import { message } from 'ant-design-vue';
  import { mergeColumn } from './smart-table-column-merge';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { useAppConfigStore } from '/@/store/modules/system/app-config.js';

  const props = defineProps({
    // 表格列数组
    modelValue: {
      type: Array,
      default: [],
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
  let originalColumn = reactive(_.cloneDeep(props.modelValue));
  // 存储最新的列数据
  let newColumn = reactive(_.cloneDeep(props.modelValue));

  // 用于监听表格拖拽后新的列数据
  watch(
    () => props.modelValue,
    (value) => {
      newColumn.forEach(item=>{
        value.forEach(itemNewColumns=>{
          if(item.dataIndex==itemNewColumns.dataIndex){
            Object.assign(item,itemNewColumns)
          }
        })
      })
    },
    {
      deep: true,
    }
  );
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

  function onFullScreen() {
    if (fullScreenFlag.value) {
      // 退出全屏
      handleExitFullScreen();
      exitElementFullscreen(document.body);
    } else {
      fullScreenFlag.value = true;
      useAppConfigStore().startFullScreen();
      launchElementFullScreen(document.body);
    }
  }

  // 处理退出全屏
  function handleExitFullScreen() {
    fullScreenFlag.value = false;
    useAppConfigStore().exitFullScreen();
    document.removeEventListener('fullscreenchange', handleFullscreenChange);
    document.removeEventListener('mozfullscreenchange', handleFullscreenChange); // Firefox
    document.removeEventListener('webkitfullscreenchange', handleFullscreenChange); // Chrome, Safari and Opera
    document.removeEventListener('MSFullscreenChange', handleFullscreenChange); // Internet Explorer and Edge
  }

  //判断各种浏览器 -全屏
  function launchElementFullScreen(element) {
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
    document.addEventListener('fullscreenchange', handleFullscreenChange);
    document.addEventListener('mozfullscreenchange', handleFullscreenChange); // Firefox
    document.addEventListener('webkitfullscreenchange', handleFullscreenChange); // Chrome, Safari and Opera
    document.addEventListener('MSFullscreenChange', handleFullscreenChange); // Internet Explorer and Edge
  }

  function handleFullscreenChange() {
    if (document.fullscreenElement || document.mozFullScreenElement || document.webkitFullscreenElement || document.msFullscreenElement) {
      console.log('进入全屏模式');
    } else {
      console.log('退出全屏模式');
      handleExitFullScreen();
    }
  }

  //判断各种浏览器 -退出全屏
  function exitElementFullscreen(element) {
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
    smartTableColumnModal.value.show(newColumn, props.tableId);
  }

  // 将弹窗修改的列数据，赋值给原表格 列数组
  function updateColumn(changeColumnArray) {
    let obj={}
    // 如果为空数组代表恢复默认，使用原始表格数据
    //合并列
    if(_.isEmpty(changeColumnArray)){
      obj = mergeColumn(_.cloneDeep(originalColumn), changeColumnArray);
    }else{
      obj = mergeColumn(_.cloneDeep(newColumn), changeColumnArray);
    }
    const newColumns = obj.newColumns;
    newColumn.forEach(item=>{
      obj.newColumns.forEach(itemNewColumns=>{
        if(item.dataIndex==itemNewColumns.dataIndex){
          Object.assign(item,itemNewColumns)
        }
      })
    })
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
        newColumn = _.cloneDeep(props.modelValue);
        buildUserTableColumns();
      }
    },
    { immediate: true }
  );
</script>
