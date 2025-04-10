<!--
  * 字典数据 弹窗
  *
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2025-03-08 21:50:41
  * @Wechat:    zhuda1024
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
-->
<template>
  <a-drawer :width="1000" :open="visible" :body-style="{ paddingBottom: '80px' }" title="字典值" @close="onClose">
    <a-form class="smart-query-form">
      <a-row class="smart-query-form-row">
        <a-form-item label="关键字" class="smart-query-form-item">
          <a-input style="width: 300px" v-model:value="keywords" @change="search" placeholder="关键字" />
        </a-form-item>
        <a-form-item label="禁用" class="smart-query-form-item">
          <BooleanSelect v-model:value="disabledFlag" @change="search" style="width: 150px" />
        </a-form-item>

        <a-form-item class="smart-query-form-item smart-margin-left10">
          <a-button type="primary" @click="queryData">
            <template #icon> <SearchOutlined /> </template>
            查询
          </a-button>
          <a-button @click="resetQuery" class="smart-margin-left10">
            <template #icon> <ReloadOutlined /> </template>
            重置
          </a-button>
        </a-form-item>
      </a-row>
    </a-form>

    <a-row class="smart-table-btn-block">
      <div class="smart-table-operate-block">
        <a-button @click="addOrUpdateData" type="primary" v-privilege="'support:dictData:add'">
          <template #icon>
            <PlusOutlined />
          </template>
          新建
        </a-button>

        <a-button
          @click="confirmBatchDelete"
          type="primary"
          danger
          :disabled="selectedRowKeyList.length === 0"
          v-privilege="'support:dictData:delete'"
        >
          <template #icon>
            <DeleteOutlined />
          </template>
          批量删除
        </a-button>
      </div>
      <div class="smart-table-setting-block"></div>
    </a-row>

    <a-table
      size="small"
      :dataSource="tableData"
      :columns="columns"
      rowKey="dictDataId"
      :pagination="false"
      :row-selection="{ selectedRowKeys: selectedRowKeyList, onChange: onSelectChange }"
      bordered
    >
      <template #bodyCell="{ record, column }">
        <template v-if="column.dataIndex === 'disabledFlag'">
          <a-switch
            @change="(checked) => handleChangeDisabled(checked, record)"
            v-model:checked="record.enabled"
            checked-children="启用中"
            un-checked-children="已禁用"
          />
        </template>
        <template v-if="column.dataIndex === 'action'">
          <a-button @click="addOrUpdateData(record)" type="link" v-privilege="'support:dictData:update'">编辑</a-button>
        </template>
      </template>
    </a-table>

    <div class="smart-query-table-page">共计 {{ tableData.length }} 条</div>
    <DictDataFormModal ref="dictDataFormModalRef" @reloadList="queryData" />
  </a-drawer>
</template>
<script setup lang="ts">
  import { reactive, ref } from 'vue';
  import DictDataFormModal from './dict-data-form-modal.vue';
  import { dictApi } from '/@/api/support/dict-api';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { Modal } from 'ant-design-vue';
  import { message } from 'ant-design-vue';
  import { smartSentry } from '/@/lib/smart-sentry';
  import BooleanSelect from '/@/components/framework/boolean-select/index.vue';
  import _ from 'lodash';

  // 是否展示抽屉
  const visible = ref(false);
  const dictId = ref(undefined);
  const dictCode = ref(undefined);

  function showModal(id, code) {
    dictId.value = id;
    dictCode.value = code;
    visible.value = true;
    queryData();
  }

  function onClose() {
    visible.value = false;
    dictId.value = undefined;
    dictCode.value = undefined;
  }

  const columns = reactive([
    {
      title: '值',
      dataIndex: 'dataValue',
    },
    {
      title: '名称',
      dataIndex: 'dataLabel',
    },
    {
      title: '状态',
      width: 90,
      dataIndex: 'disabledFlag',
    },
    {
      title: '排序',
      width: 50,
      dataIndex: 'sortOrder',
    },
    {
      title: '备注',
      width: 200,
      ellipsis: true,
      dataIndex: 'remark',
    },
    {
      title: '更新时间',
      width: 150,
      dataIndex: 'updateTime',
    },
    {
      title: '操作',
      width: 70,
      dataIndex: 'action',
    },
  ]);

  // ----------------------- 表格 查询 ------------------------
  const keywords = ref(undefined);
  const disabledFlag = ref(null);

  const selectedRowKeyList = ref([]);
  const tableLoading = ref(false);
  const dictDataList = ref([]);
  const tableData = ref([]);

  function onSelectChange(selectedRowKeys) {
    selectedRowKeyList.value = selectedRowKeys;
  }

  function search() {
    tableData.value = dictDataList.value.filter((item) => {
      let keywordsFilterFlag = true;
      if (keywords.value) {
        keywordsFilterFlag =
          _.includes(item.dataValue.toLowerCase(), keywords.value.toLowerCase()) ||
          _.includes(item.dataLabel.toLowerCase(), keywords.value.toLowerCase()) ||
          _.includes(item.remark.toLowerCase(), keywords.value.toLowerCase());
      }
      let disabledFilterFlag = _.isNull(disabledFlag.value) ? true : item.disabledFlag === disabledFlag.value;
      return disabledFilterFlag && keywordsFilterFlag;
    });
  }

  function resetQuery() {
    keywords.value = null;
    disabledFlag.value = null;
    queryData();
  }

  async function queryData() {
    try {
      tableLoading.value = true;
      let responseData = await dictApi.queryDictData(dictId.value);
      responseData.data.map((e) => (e.enabled = !e.disabledFlag));
      dictDataList.value = responseData.data;
      search();
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      tableLoading.value = false;
    }
  }

  // ----------------------- 启用/禁用 ------------------------
  async function handleChangeDisabled(disabledFlag, dictData) {
    SmartLoading.show();
    try {
      await dictApi.updateDictDataDisabled(dictData.dictDataId);
      dictData.disabledFlag = !disabledFlag;
      message.success('操作成功');
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      SmartLoading.hide();
    }
  }

  // ----------------------- 批量 删除 ------------------------

  function confirmBatchDelete() {
    Modal.confirm({
      title: '提示',
      content: '确定要删除选中值吗?',
      okText: '删除',
      okType: 'danger',
      onOk() {
        batchDelete();
      },
      cancelText: '取消',
      onCancel() {},
    });
  }

  async function batchDelete() {
    try {
      SmartLoading.show();
      await dictApi.batchDeleteDictData(selectedRowKeyList.value);
      message.success('删除成功');
      await queryData();
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      SmartLoading.hide();
    }
  }

  // ----------------------- 弹窗表单操作 ------------------------

  const dictDataFormModalRef = ref();
  function addOrUpdateData(rowData) {
    dictDataFormModalRef.value.showModal(rowData, dictId.value, dictCode.value);
  }

  defineExpose({
    showModal,
  });
</script>
