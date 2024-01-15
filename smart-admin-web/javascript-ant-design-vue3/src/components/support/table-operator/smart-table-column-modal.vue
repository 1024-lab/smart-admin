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
  <a-modal :width="700" :open="visible" title="设置列" :destroyOnClose="true" :closable="false">
    <a-alert type="info" show-icon class="smart-margin-bottom10">
      <template #icon><smile-outlined /></template>
      <template #message> 可以通过拖拽行直接修改顺序哦；（ <pushpin-outlined />为固定列，不可拖拽 ）</template>
    </a-alert>
    <a-table
      id="smartTableColumnModalTable"
      rowKey="columnKey"
      row-class-name="column-row"
      :columns="tableColumns"
      :dataSource="tableData"
      :rowSelection="{ checkStrictly: false, selectedRowKeys: selectedRowKeyList, onChange: onSelectChange }"
      :pagination="false"
      size="small"
      bordered
    >
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'title'">
          <a-button type="text" :class="record.fixed ? '' : 'handle'" size="small" style="width: 100%; text-align: left">
            <template #icon v-if="!record.fixed"> <drag-outlined /> </template>
            <template #icon v-if="record.fixed"> <pushpin-outlined /> </template>
            {{ text }}
          </a-button>
        </template>
        <template v-if="column.dataIndex === 'width'">
          <a-input-number v-model:value="record.width" style="width: 90px; margin-left: 10px; margin-right: 3px" size="small" />px
        </template>
        <template v-if="column.dataIndex === 'operate'">
          <div class="smart-table-operate" v-if="!record.fixed">
            <a-button @click="up(index)" v-show="index > 0" type="link" class="handle" size="small" style="margin-right: 12px"> 上移 </a-button>
            <a-button @click="down(index)" type="link" class="handle" size="small" v-show="index !== tableData.length - 1"> 下移</a-button>
          </div>
        </template>
      </template>
    </a-table>

    <template #footer>
      <a-button key="back" @click="hide">取消</a-button>
      <a-button key="submit" type="primary" :loading="submitLoading" @click="save">保存</a-button>
      <a-button key="back" :loading="submitLoading" @click="reset" danger style="margin-left: 20px">恢复默认</a-button>
    </template>
  </a-modal>
</template>
<script setup>
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { tableColumnApi } from '/src/api/support/table-column-api';
  import { nextTick, ref } from 'vue';
  import _ from 'lodash';
  import Sortable from 'sortablejs';
  import { message, Modal } from 'ant-design-vue';
  import { mergeColumn } from './smart-table-column-merge';
  import { smartSentry } from '/@/lib/smart-sentry';

  const emit = defineEmits(['change']);

  defineExpose({ show });

  // ---------------- 显示 / 隐藏 --------------------
  let tableId = 1;
  const visible = ref(false);
  //显示
  function show(columns, showTableId) {
    tableId = showTableId;
    visible.value = true;
    getUserTableColumns(tableId, _.cloneDeep(columns));
  }

  //隐藏
  function hide() {
    visible.value = false;
  }

  //获取用户的列数据
  async function getUserTableColumns(tableId, columns) {
    SmartLoading.show();
    let userTableColumnArray = [];
    try {
      let res = await tableColumnApi.getColumns(tableId);
      if (res.data) {
        try {
          userTableColumnArray = JSON.parse(res.data);
        } catch (e1) {
          smartSentry.captureError(e1);
        }
      }
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      SmartLoading.hide();
    }

    //根据前端列和后端列构建新的列数据
    tableData.value = mergeColumn(columns, userTableColumnArray);

    //将已经显示的展示出来
    for (const item of tableData.value) {
      if (item.showFlag) {
        selectedRowKeyList.value.push(item.columnKey);
      }
    }

    nextTick(() => {
      initDrag();
    });
  }

  // --------------------- 表格渲染 --------------------------------
  const tableData = ref([]);

  const tableColumns = [
    {
      title: '列',
      dataIndex: 'title',
    },
    {
      title: '宽度(像素)',
      dataIndex: 'width',
      width: 150,
    },
    {
      title: '操作',
      dataIndex: 'operate',
      width: 150,
      rowDrag: true,
    },
  ];

  // --------------------- 表格移动【拖拽移动、上移、下移】 --------------------------------
  //初始化拖拽
  function initDrag() {
    let tbody = document.querySelector('#smartTableColumnModalTable tbody');
    Sortable.create(tbody, {
      animation: 300,
      dragClass: 'smart-ghost-class', //设置拖拽样式类名
      ghostClass: 'smart-ghost-class', //设置拖拽停靠样式类名
      chosenClass: 'smart-ghost-class', //设置选中样式类名
      handle: '.handle',
      onEnd({ newIndex, oldIndex }) {
        if (newIndex === oldIndex) {
          return;
        }
        moveTableData(oldIndex, newIndex);
      },
    });
  }
  //上移
  function up(oldIndex) {
    let newIndex = oldIndex - 1;
    if (newIndex < 0) {
      return;
    }
    //如果下一个是固定列，则也不可移动
    if (tableData.value[newIndex].fixed) {
      return;
    }
    moveTableData(oldIndex, newIndex);
  }

  //下移
  function down(oldIndex) {
    let newIndex = oldIndex + 1;
    if (newIndex >= tableData.value.length) {
      return;
    }

    //如果下一个是固定列，则也不可移动
    if (tableData.value[newIndex].fixed) {
      return;
    }

    moveTableData(oldIndex, newIndex);
  }

  //移动表格数据
  function moveTableData(oldIndex, newIndex) {
    const currRow = tableData.value.splice(oldIndex, 1)[0];
    tableData.value.splice(newIndex, 0, currRow);
  }

  // ----------- table 批量操作 start -----------
  const selectedRowKeyList = ref([]);

  function onSelectChange(keyArray) {
    selectedRowKeyList.value = keyArray;
  }

  // -------------------------提交表单【恢复默认、保存、取消】 ------------------------
  const submitLoading = ref(false);

  //重置
  function reset() {
    Modal.confirm({
      title: '确定要恢复默认吗？',
      content: '确定恢复默认后，该信息将不可恢复',
      okText: '确定恢复',
      okType: 'danger',
      onOk() {
        (async () => {
          submitLoading.value = true;
          try {
            await tableColumnApi.deleteColumns(tableId);
            message.success('恢复默认成功');
            emit('change', []);
            hide();
          } catch (e) {
            smartSentry.captureError(e);
          } finally {
            submitLoading.value = false;
          }
        })();
      },
      cancelText: '取消',
      onCancel() {},
    });
  }

  //保存
  async function save() {
    submitLoading.value = true;
    try {
      let columnList = [];
      for (let index = 0; index < tableData.value.length; index++) {
        let item = tableData.value[index];
        let column = {
          columnKey: item.columnKey,
          sort: index + 1,
        };
        if (item.width) {
          column.width = item.width;
        }
        column.showFlag = selectedRowKeyList.value.indexOf(item.columnKey) > -1;
        columnList.push(column);
      }

      columnList = _.sortBy(columnList, (e) => e.sort);

      await tableColumnApi.updateTableColumn({
        tableId,
        columnList,
      });

      message.success('保存成功');
      emit('change', columnList);
      hide();
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      submitLoading.value = false;
    }
  }
</script>
<style scoped lang="less">
  .column-row:hover {
    background-color: red !important;
  }
  .column-row {
    cursor: pointer;
  }
  .blue-background-class {
    background-color: red !important;
  }

  :deep(.ant-table-tbody) {
    .ant-table-row-selected > td {
      background-color: #ffffff;
    }
  }
</style>
