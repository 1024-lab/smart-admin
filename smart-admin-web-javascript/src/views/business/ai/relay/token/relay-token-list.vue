<!--
  * Relay Token管理 - 列表
  *
  * @Author:    Mission
  * @Date:      2026-03-15
  * @Copyright  Mission
-->
<template>
    <!---------- 查询表单form begin ----------->
    <a-form class="smart-query-form">
        <a-row class="smart-query-form-row">
            <a-form-item label="员工" class="smart-query-form-item">
                <EmployeeSelect v-model:value="queryForm.employeeId" placeholder="请选择员工" width="200px" />
            </a-form-item>
            <a-form-item label="渠道组" class="smart-query-form-item">
                <RelayChannelGroupSelect v-model:value="queryForm.channelGroupId" placeholder="请选择渠道组" width="200px" />
            </a-form-item>
            <a-form-item label="Token名称" class="smart-query-form-item">
                <a-input style="width: 200px" v-model:value="queryForm.tokenName" placeholder="Token名称" />
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

        <a-table
            size="small"
            :scroll="{ x: 1400 }"
            :dataSource="tableData"
            :columns="columns"
            rowKey="tokenId"
            bordered
            :loading="tableLoading"
            :pagination="false"
        >
            <template #bodyCell="{ text, record, column }">
                <template v-if="column.dataIndex === 'disabledFlag'">
                    <a-tag :color="text === 0 ? 'green' : 'red'">{{ text === 0 ? '启用' : '禁用' }}</a-tag>
                </template>
                <template v-if="column.dataIndex === 'action'">
                    <div class="smart-table-operate">
                        <a-button @click="showForm(record)" type="link">编辑</a-button>
                        <a-button @click="onDelete(record)" danger type="link">删除</a-button>
                    </div>
                </template>
            </template>
        </a-table>

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

        <RelayTokenForm ref="formRef" @reloadList="queryData" />
        <RelayTokenResultModal ref="resultModalRef" />
    </a-card>
</template>
<script setup>
    import { reactive, ref, onMounted } from 'vue';
    import { message, Modal } from 'ant-design-vue';
    import { SmartLoading } from '/@/components/framework/smart-loading';
    import { relayTokenApi } from '/@/api/business/ai/relay/relay-token-api';
    import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
    import { smartSentry } from '/@/lib/smart-sentry';
    import TableOperator from '/@/components/support/table-operator/index.vue';
    import SmartEnumSelect from '/@/components/framework/smart-enum-select/index.vue';
    import EmployeeSelect from '/@/components/system/employee-select/index.vue';
    import RelayChannelGroupSelect from '/@/components/business/relay/relay-channel-group-select/index.vue';
    import RelayTokenForm from './relay-token-form.vue';
    import RelayTokenResultModal from './relay-token-result-modal.vue';

    const columns = ref([
        { title: 'ID', dataIndex: 'tokenId', width: 80 },
        { title: 'Token名称', dataIndex: 'tokenName', width: 150 },
        { title: 'Token前缀', dataIndex: 'tokenPrefix', width: 150 },
        { title: '员工ID', dataIndex: 'employeeId', width: 100 },
        { title: '渠道组', dataIndex: 'channelGroupName', width: 160 },
        { title: '过期时间', dataIndex: 'expiresTime', width: 170, ellipsis: true },
        { title: '最后使用', dataIndex: 'lastUsedTime', width: 170, ellipsis: true },
        { title: '请求次数', dataIndex: 'requestCount', width: 100 },
        { title: '状态', dataIndex: 'disabledFlag', width: 80 },
        { title: '备注', dataIndex: 'remark', ellipsis: true },
        { title: '创建时间', dataIndex: 'createTime', width: 170, ellipsis: true },
        { title: '操作', dataIndex: 'action', fixed: 'right', width: 120 },
    ]);

    const queryFormState = {
        employeeId: undefined,
        channelGroupId: undefined,
        tokenName: undefined,
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
            let queryResult = await relayTokenApi.queryPage(queryForm);
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
    const resultModalRef = ref();

    function showForm(data) {
        formRef.value.show(data, resultModalRef.value);
    }

    // ---------------------------- 删除 ----------------------------
    function onDelete(data) {
        Modal.confirm({
            title: '提示',
            content: '确定要删除该Token吗?',
            okText: '删除',
            okType: 'danger',
            onOk() { requestDelete(data); },
            cancelText: '取消',
        });
    }

    async function requestDelete(data) {
        SmartLoading.show();
        try {
            await relayTokenApi.delete(data.tokenId);
            message.success('删除成功');
            queryData();
        } catch (e) {
            smartSentry.captureError(e);
        } finally {
            SmartLoading.hide();
        }
    }
</script>
