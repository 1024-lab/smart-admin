<!--
  * Relay 模型管理 - 列表
  *
  * @Author:    Mission
  * @Date:      2026-03-15
  * @Copyright  Mission
-->
<template>
    <!---------- 查询表单form begin ----------->
    <a-form class="smart-query-form">
        <a-row class="smart-query-form-row">
            <a-form-item label="模型名称" class="smart-query-form-item">
                <a-input style="width: 200px" v-model:value="queryForm.modelName" placeholder="模型名称" />
            </a-form-item>
            <a-form-item label="端点类型" class="smart-query-form-item">
                <SmartEnumSelect v-model:value="queryForm.endpointType" enumName="ENDPOINT_TYPE_ENUM" placeholder="请选择" width="150px" />
            </a-form-item>
            <a-form-item label="状态" class="smart-query-form-item">
                <SmartEnumSelect v-model:value="queryForm.disabledFlag" enumName="FLAG_NUMBER_ENUM" placeholder="请选择" width="120px" />
            </a-form-item>
            <a-form-item class="smart-query-form-item">
                <a-button type="primary" @click="onSearch">
                    <template #icon><SearchOutlined /></template>
                    查询
                </a-button>
                <a-button @click="resetQuery" class="smart-margin-left10">
                    <template #icon><ReloadOutlined /></template>
                    重置
                </a-button>
            </a-form-item>
        </a-row>
    </a-form>
    <!---------- 查询表单form end ----------->

    <a-card size="small" :bordered="false" :hoverable="true">
        <!---------- 表格操作行 begin ----------->
        <a-row class="smart-table-btn-block">
            <div class="smart-table-operate-block">
                <a-button @click="showForm" type="primary" size="small">
                    <template #icon><PlusOutlined /></template>
                    新建
                </a-button>
            </div>
            <div class="smart-table-setting-block">
                <TableOperator v-model="columns" :tableId="null" :refresh="queryData" />
            </div>
        </a-row>
        <!---------- 表格操作行 end ----------->

        <!---------- 表格 begin ----------->
        <a-table
            size="small"
            :scroll="{ x: 1200 }"
            :dataSource="tableData"
            :columns="columns"
            rowKey="modelId"
            bordered
            :loading="tableLoading"
            :pagination="false"
        >
            <template #bodyCell="{ text, record, column }">
                <template v-if="column.dataIndex === 'endpointType'">
                    {{ $smartEnumPlugin.getDescByValue('ENDPOINT_TYPE_ENUM', text) }}
                </template>
                <template v-if="column.dataIndex === 'disabledFlag'">
                    <a-switch
                        :checked="text === 0"
                        checked-children="启用"
                        un-checked-children="禁用"
                        @change="(checked) => updateDisabled(record, checked)"
                    />
                </template>
                <template v-if="column.dataIndex === 'action'">
                    <div class="smart-table-operate">
                        <a-button @click="showForm(record)" type="link">编辑</a-button>
                        <a-button @click="onDelete(record)" danger type="link">删除</a-button>
                    </div>
                </template>
            </template>
        </a-table>
        <!---------- 表格 end ----------->

        <div class="smart-query-table-page">
            <a-pagination
                showSizeChanger
                showQuickJumper
                show-less-items
                :pageSizeOptions="PAGE_SIZE_OPTIONS"
                :defaultPageSize="queryForm.pageSize"
                v-model:current="queryForm.pageNum"
                v-model:pageSize="queryForm.pageSize"
                :total="total"
                @change="queryData"
                @showSizeChange="queryData"
                :show-total="(total) => `共${total}条`"
            />
        </div>

        <RelayModelForm ref="formRef" @reloadList="queryData" />
    </a-card>
</template>
<script setup>
    import { reactive, ref, onMounted } from 'vue';
    import { message, Modal } from 'ant-design-vue';
    import { SmartLoading } from '/@/components/framework/smart-loading';
    import { relayModelApi } from '/@/api/business/ai/relay/relay-model-api';
    import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
    import { smartSentry } from '/@/lib/smart-sentry';
    import TableOperator from '/@/components/support/table-operator/index.vue';
    import SmartEnumSelect from '/@/components/framework/smart-enum-select/index.vue';
    import RelayModelForm from './relay-model-form.vue';

    // ---------------------------- 表格列 ----------------------------
    const columns = ref([
        { title: 'ID', dataIndex: 'modelId', width: 80 },
        { title: '模型名称', dataIndex: 'modelName', ellipsis: true },
        { title: '端点类型', dataIndex: 'endpointType', width: 120 },
        { title: '输入价格(每千token)', dataIndex: 'inputPricePerK', width: 160 },
        { title: '输出价格(每千token)', dataIndex: 'outputPricePerK', width: 160 },
        { title: '状态', dataIndex: 'disabledFlag', width: 100 },
        { title: '备注', dataIndex: 'remark', ellipsis: true },
        { title: '更新时间', dataIndex: 'updateTime', width: 170, ellipsis: true },
        { title: '创建时间', dataIndex: 'createTime', width: 170, ellipsis: true },
        { title: '操作', dataIndex: 'action', fixed: 'right', width: 120 },
    ]);

    // ---------------------------- 查询 ----------------------------
    const queryFormState = {
        modelName: undefined,
        endpointType: undefined,
        disabledFlag: undefined,
        pageNum: 1,
        pageSize: 10,
    };
    const queryForm = reactive({ ...queryFormState });
    const tableLoading = ref(false);
    const tableData = ref([]);
    const total = ref(0);

    function resetQuery() {
        let pageSize = queryForm.pageSize;
        Object.assign(queryForm, queryFormState);
        queryForm.pageSize = pageSize;
        queryData();
    }

    function onSearch() {
        queryForm.pageNum = 1;
        queryData();
    }

    async function queryData() {
        tableLoading.value = true;
        try {
            let queryResult = await relayModelApi.queryPage(queryForm);
            tableData.value = queryResult.data.list;
            total.value = queryResult.data.total;
        } catch (e) {
            smartSentry.captureError(e);
        } finally {
            tableLoading.value = false;
        }
    }

    onMounted(queryData);

    // ---------------------------- 添加/修改 ----------------------------
    const formRef = ref();
    function showForm(data) {
        formRef.value.show(data);
    }

    // ---------------------------- 删除 ----------------------------
    function onDelete(data) {
        Modal.confirm({
            title: '提示',
            content: '确定要删除该模型吗?',
            okText: '删除',
            okType: 'danger',
            onOk() { requestDelete(data); },
            cancelText: '取消',
        });
    }

    async function requestDelete(data) {
        SmartLoading.show();
        try {
            await relayModelApi.delete(data.modelId);
            message.success('删除成功');
            queryData();
        } catch (e) {
            smartSentry.captureError(e);
        } finally {
            SmartLoading.hide();
        }
    }

    // ---------------------------- 启用/禁用 ----------------------------
    async function updateDisabled(record, checked) {
        SmartLoading.show();
        try {
            let disabledFlag = checked ? 0 : 1;
            await relayModelApi.updateDisabled(record.modelId, disabledFlag);
            message.success('操作成功');
            queryData();
        } catch (e) {
            smartSentry.captureError(e);
        } finally {
            SmartLoading.hide();
        }
    }
</script>
