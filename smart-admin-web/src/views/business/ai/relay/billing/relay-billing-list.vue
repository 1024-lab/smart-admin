<!--
  * Relay 计费会话 - 列表（只读）
  *
  * @Author:    Mission
  * @Date:      2026-03-15
  * @Copyright  Mission
-->
<template>
    <!---------- 查询表单form begin ----------->
    <a-form class="smart-query-form">
        <a-row class="smart-query-form-row">
            <a-form-item label="员工ID" class="smart-query-form-item">
                <a-input-number style="width: 150px" v-model:value="queryForm.employeeId" placeholder="员工ID" />
            </a-form-item>
            <a-form-item label="请求ID" class="smart-query-form-item">
                <a-input style="width: 220px" v-model:value="queryForm.requestId" placeholder="请求ID" />
            </a-form-item>
            <a-form-item label="计费状态" class="smart-query-form-item">
                <SmartEnumSelect v-model:value="queryForm.billingState" enumName="BILLING_STATE_ENUM" placeholder="请选择" width="140px" />
            </a-form-item>
            <a-form-item label="时间范围" class="smart-query-form-item">
                <a-range-picker
                    v-model:value="queryDateRange"
                    style="width: 280px"
                    show-time
                    format="YYYY-MM-DD HH:mm:ss"
                    value-format="YYYY-MM-DD HH:mm:ss"
                    @change="onDateRangeChange"
                />
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
            <div class="smart-table-operate-block"></div>
            <div class="smart-table-setting-block">
                <TableOperator v-model="columns" :tableId="null" :refresh="queryData" />
            </div>
        </a-row>

        <a-table
            size="small"
            :scroll="{ x: 2000 }"
            :dataSource="tableData"
            :columns="columns"
            rowKey="billingSessionId"
            bordered
            :loading="tableLoading"
            :pagination="false"
        >
            <template #bodyCell="{ text, column }">
                <template v-if="column.dataIndex === 'billingState'">
                    <a-tag :color="billingStateColor(text)">
                        {{ $smartEnumPlugin.getDescByValue('BILLING_STATE_ENUM', text) }}
                    </a-tag>
                </template>
                <template v-if="column.dataIndex === 'endpointType'">
                    {{ $smartEnumPlugin.getDescByValue('ENDPOINT_TYPE_ENUM', text) }}
                </template>
                <template v-if="column.dataIndex === 'errorMsg'">
                    <a-tooltip :title="text" v-if="text">
                        <span>{{ text }}</span>
                    </a-tooltip>
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
    </a-card>
</template>
<script setup>
    import { reactive, ref, onMounted } from 'vue';
    import { relayBillingApi } from '/@/api/business/ai/relay/relay-billing-api';
    import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
    import { smartSentry } from '/@/lib/smart-sentry';
    import TableOperator from '/@/components/support/table-operator/index.vue';
    import SmartEnumSelect from '/@/components/framework/smart-enum-select/index.vue';

    const columns = ref([
        { title: 'ID', dataIndex: 'billingSessionId', width: 80 },
        { title: '请求ID', dataIndex: 'requestId', width: 200, ellipsis: true },
        { title: '员工ID', dataIndex: 'employeeId', width: 90 },
        { title: '渠道', dataIndex: 'channelName', width: 120 },
        { title: '模型', dataIndex: 'modelName', width: 150 },
        { title: '端点', dataIndex: 'endpointType', width: 100 },
        { title: '预扣额度', dataIndex: 'preconsumeQuota', width: 120, customRender: ({ text }) => text != null ? Number(text).toFixed(6) : '-' },
        { title: '结算额度', dataIndex: 'settledQuota', width: 120, customRender: ({ text }) => text != null ? Number(text).toFixed(6) : '-' },
        { title: '差额', dataIndex: 'deltaQuota', width: 100, customRender: ({ text }) => text != null ? Number(text).toFixed(6) : '-' },
        { title: '计费状态', dataIndex: 'billingState', width: 100 },
        { title: '上游状态', dataIndex: 'upstreamStatus', width: 90 },
        { title: '网关状态', dataIndex: 'gatewayStatus', width: 90 },
        { title: '耗时(s)', dataIndex: 'latencyMs', width: 90, customRender: ({ text }) => text != null ? (text / 1000).toFixed(3) : '-' },
        { title: '错误信息', dataIndex: 'errorMsg', width: 200, ellipsis: true },
        { title: '结算时间', dataIndex: 'settledTime', width: 170, ellipsis: true },
        { title: '创建时间', dataIndex: 'createTime', width: 170, ellipsis: true },
    ]);

    const queryFormState = {
        employeeId: undefined,
        requestId: undefined,
        billingState: undefined,
        startTime: undefined,
        endTime: undefined,
        pageNum: 1,
        pageSize: 10,
    };
    const queryForm = reactive({ ...queryFormState });
    const queryDateRange = ref(null);
    const tableLoading = ref(false);
    const tableData = ref([]);
    const total = ref(0);

    function onDateRangeChange(dates) {
        if (dates && dates.length === 2) {
            queryForm.startTime = dates[0];
            queryForm.endTime = dates[1];
        } else {
            queryForm.startTime = undefined;
            queryForm.endTime = undefined;
        }
    }

    function resetQuery() {
        let pageSize = queryForm.pageSize;
        Object.assign(queryForm, queryFormState);
        queryForm.pageSize = pageSize;
        queryDateRange.value = null;
        queryData();
    }

    function onSearch() {
        queryForm.pageNum = 1;
        queryData();
    }

    async function queryData() {
        tableLoading.value = true;
        try {
            let queryResult = await relayBillingApi.queryPage(queryForm);
            tableData.value = queryResult.data.list;
            total.value = queryResult.data.total;
        } catch (e) {
            smartSentry.captureError(e);
        } finally {
            tableLoading.value = false;
        }
    }

    function billingStateColor(state) {
        const map = { 0: 'processing', 1: 'success', 2: 'warning', 3: 'error' };
        return map[state] || 'default';
    }

    onMounted(queryData);
</script>
