<!--
  * Relay 用户账户 - 列表
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
                <a-button @click="showAdd" type="primary">
                    <template #icon><PlusOutlined /></template>
                    新增
                </a-button>
            </div>
            <div class="smart-table-setting-block">
                <TableOperator v-model="columns" :tableId="null" :refresh="queryData" />
            </div>
        </a-row>

        <a-table
            size="small"
            :scroll="{ x: 1200 }"
            :dataSource="tableData"
            :columns="columns"
            rowKey="employeeId"
            bordered
            :loading="tableLoading"
            :pagination="false"
        >
            <template #bodyCell="{ text, record, column }">
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
                        <a-button @click="showRecharge(record)" type="link">充值</a-button>
                        <a-button @click="showEdit(record)" type="link">编辑</a-button>
                        <a-button @click="showLedger(record)" type="link">流水</a-button>
                        <a-button @click="onDelete(record)" type="link" danger>删除</a-button>
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

        <RelayAccountAddForm ref="addRef" @reloadList="queryData" />
        <RelayAccountRechargeForm ref="rechargeRef" @reloadList="queryData" />
        <RelayAccountEditForm ref="editRef" @reloadList="queryData" />
        <RelayAccountLedgerModal ref="ledgerRef" />
    </a-card>
</template>
<script setup>
    import { SearchOutlined, ReloadOutlined, PlusOutlined } from '@ant-design/icons-vue';
    import { reactive, ref, onMounted } from 'vue';
    import { message } from 'ant-design-vue';
    import { SmartLoading } from '/@/components/framework/smart-loading';
    import { relayAccountApi } from '/@/api/business/ai/relay/relay-account-api';
    import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
    import { smartSentry } from '/@/lib/smart-sentry';
    import TableOperator from '/@/components/support/table-operator/index.vue';
    import SmartEnumSelect from '/@/components/framework/smart-enum-select/index.vue';
    import EmployeeSelect from '/@/components/system/employee-select/index.vue';
    import { Modal } from 'ant-design-vue';
    import RelayAccountAddForm from './relay-account-add-form.vue';
    import RelayAccountRechargeForm from './relay-account-recharge-form.vue';
    import RelayAccountEditForm from './relay-account-edit-form.vue';
    import RelayAccountLedgerModal from './relay-account-ledger-modal.vue';

    const columns = ref([
        { title: '员工ID', dataIndex: 'employeeId', width: 100 },
        { title: '员工姓名', dataIndex: 'employeeName', width: 120 },
        { title: '剩余额度', dataIndex: 'quotaRemain', width: 140, customRender: ({ text }) => text != null ? Number(text).toFixed(6) : '-' },
        { title: '已用额度', dataIndex: 'quotaUsed', width: 140, customRender: ({ text }) => text != null ? Number(text).toFixed(6) : '-' },
        { title: '状态', dataIndex: 'disabledFlag', width: 100 },
        { title: '更新时间', dataIndex: 'updateTime', width: 170, ellipsis: true },
        { title: '创建时间', dataIndex: 'createTime', width: 170, ellipsis: true },
        { title: '操作', dataIndex: 'action', fixed: 'right', width: 200 },
    ]);

    const queryFormState = {
        employeeId: undefined,
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
            let queryResult = await relayAccountApi.queryPage(queryForm);
            tableData.value = queryResult.data.list;
            total.value = queryResult.data.total;
        } catch (e) {
            smartSentry.captureError(e);
        } finally {
            tableLoading.value = false;
        }
    }

    onMounted(queryData);

    // ---------------------------- 新增 ----------------------------
    const addRef = ref();
    function showAdd() {
        addRef.value.show();
    }

    // ---------------------------- 充值 ----------------------------
    const rechargeRef = ref();
    function showRecharge(record) {
        rechargeRef.value.show(record);
    }

    // ---------------------------- 流水 ----------------------------
    const ledgerRef = ref();
    function showLedger(record) {
        ledgerRef.value.show(record);
    }

    // ---------------------------- 编辑 ----------------------------
    const editRef = ref();
    function showEdit(record) {
        editRef.value.show(record);
    }

    // ---------------------------- 删除 ----------------------------
    function onDelete(record) {
        Modal.confirm({
            title: '删除确认',
            content: `确定删除员工【${record.employeeName}】的账户吗？`,
            okText: '确定删除',
            okType: 'danger',
            onOk: async () => {
                SmartLoading.show();
                try {
                    await relayAccountApi.delete(record.employeeId);
                    message.success('删除成功');
                    queryData();
                } catch (e) {
                    smartSentry.captureError(e);
                } finally {
                    SmartLoading.hide();
                }
            },
        });
    }

    // ---------------------------- 启用/禁用 ----------------------------
    async function updateDisabled(record, checked) {
        SmartLoading.show();
        try {
            let disabledFlag = checked ? 0 : 1;
            await relayAccountApi.updateDisabled(record.employeeId, disabledFlag);
            message.success('操作成功');
            queryData();
        } catch (e) {
            smartSentry.captureError(e);
        } finally {
            SmartLoading.hide();
        }
    }
</script>
