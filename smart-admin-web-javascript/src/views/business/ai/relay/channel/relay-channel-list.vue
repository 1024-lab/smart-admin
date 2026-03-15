<!--
  * Relay 渠道管理 - 列表
  *
  * @Author:    Mission
  * @Date:      2026-03-15
  * @Copyright  Mission
-->
<template>
    <!---------- 查询表单form begin ----------->
    <a-form class="smart-query-form">
        <a-row class="smart-query-form-row">
            <a-form-item label="渠道名称" class="smart-query-form-item">
                <a-input style="width: 200px" v-model:value="queryForm.channelName" placeholder="渠道名称" />
            </a-form-item>
            <a-form-item label="供应商" class="smart-query-form-item">
                <SmartEnumSelect v-model:value="queryForm.providerType" enumName="PROVIDER_TYPE_ENUM" placeholder="请选择" width="150px" />
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
            :scroll="{ x: 1600 }"
            :dataSource="tableData"
            :columns="columns"
            rowKey="channelId"
            bordered
            :loading="tableLoading"
            :pagination="false"
        >
            <template #bodyCell="{ text, record, column }">
                <template v-if="column.dataIndex === 'providerType'">
                    {{ $smartEnumPlugin.getDescByValue('PROVIDER_TYPE_ENUM', text) }}
                </template>
                <template v-if="column.dataIndex === 'disabledFlag'">
                    <a-switch
                        :checked="text === 0"
                        checked-children="启用"
                        un-checked-children="禁用"
                        @change="(checked) => updateDisabled(record, checked)"
                    />
                </template>
                <template v-if="column.dataIndex === 'baseUrl'">
                    <a-tooltip :title="text">
                        <span>{{ text }}</span>
                    </a-tooltip>
                </template>
                <template v-if="column.dataIndex === 'action'">
                    <div class="smart-table-operate">
                        <a-button @click="showForm(record)" type="link">编辑</a-button>
                        <a-button @click="showModelBind(record)" type="link">绑定模型</a-button>
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

        <RelayChannelForm ref="formRef" @reloadList="queryData" />
        <RelayChannelModelBind ref="modelBindRef" @reloadList="queryData" />
    </a-card>
</template>
<script setup>
    import { reactive, ref, onMounted } from 'vue';
    import { message, Modal } from 'ant-design-vue';
    import { SmartLoading } from '/@/components/framework/smart-loading';
    import { relayChannelApi } from '/@/api/business/ai/relay/relay-channel-api';
    import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
    import { smartSentry } from '/@/lib/smart-sentry';
    import TableOperator from '/@/components/support/table-operator/index.vue';
    import SmartEnumSelect from '/@/components/framework/smart-enum-select/index.vue';
    import RelayChannelForm from './relay-channel-form.vue';
    import RelayChannelModelBind from './relay-channel-model-bind.vue';

    // ---------------------------- 表格列 ----------------------------
    const columns = ref([
        { title: 'ID', dataIndex: 'channelId', width: 80 },
        { title: '渠道名称', dataIndex: 'channelName', width: 150 },
        { title: '供应商', dataIndex: 'providerType', width: 130 },
        { title: '基础URL', dataIndex: 'baseUrl', width: 200, ellipsis: true },
        { title: 'API Key', dataIndex: 'apiKeyMask', width: 150, ellipsis: true },
        { title: '优先级', dataIndex: 'priority', width: 80 },
        { title: '权重', dataIndex: 'weight', width: 80 },
        { title: '超时(ms)', dataIndex: 'timeoutMs', width: 100 },
        { title: '最大重试', dataIndex: 'maxRetries', width: 90 },
        { title: '状态', dataIndex: 'disabledFlag', width: 100 },
        { title: '创建时间', dataIndex: 'createTime', width: 170, ellipsis: true },
        { title: '操作', dataIndex: 'action', fixed: 'right', width: 200 },
    ]);

    // ---------------------------- 查询 ----------------------------
    const queryFormState = {
        channelName: undefined,
        providerType: undefined,
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
            let queryResult = await relayChannelApi.queryPage(queryForm);
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

    // ---------------------------- 绑定模型 ----------------------------
    const modelBindRef = ref();
    function showModelBind(record) {
        modelBindRef.value.show(record);
    }

    // ---------------------------- 删除 ----------------------------
    function onDelete(data) {
        Modal.confirm({
            title: '提示',
            content: '确定要删除该渠道吗?',
            okText: '删除',
            okType: 'danger',
            onOk() { requestDelete(data); },
            cancelText: '取消',
        });
    }

    async function requestDelete(data) {
        SmartLoading.show();
        try {
            await relayChannelApi.delete(data.channelId);
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
            await relayChannelApi.updateDisabled(record.channelId, disabledFlag);
            message.success('操作成功');
            queryData();
        } catch (e) {
            smartSentry.captureError(e);
        } finally {
            SmartLoading.hide();
        }
    }
</script>
