<!--
  * 用户表
  *
  * @Author:    my
  * @Date:      2024-01-10 15:39:54
  * @Copyright  my
-->
<template>
    <!---------- 查询表单form begin ----------->
    <a-form class="smart-query-form">
        <a-row class="smart-query-form-row">
            <a-form-item label="关键字查询" class="smart-query-form-item">
                <a-input style="width: 150px" v-model:value="queryForm.keywords" placeholder="关键字查询" />
            </a-form-item>
            <a-form-item label="性别" class="smart-query-form-item">
                <SmartEnumSelect width="150px" v-model:value="queryForm.gender" enumName="" placeholder="性别"/>
            </a-form-item>
            <a-form-item label="国籍" class="smart-query-form-item">
                <a-input style="width: 150px" v-model:value="queryForm.country" placeholder="国籍" />
            </a-form-item>
            <a-form-item label="证件类型" class="smart-query-form-item">
                <a-input style="width: 150px" v-model:value="queryForm.cardType" placeholder="证件类型" />
            </a-form-item>
            <a-form-item label="删除标志" class="smart-query-form-item">
                <a-input style="width: 150px" v-model:value="queryForm.deletedFlag" placeholder="删除标志" />
            </a-form-item>
            <a-form-item label="创建时间" class="smart-query-form-item">
                <a-range-picker v-model:value="queryForm.createTime" :ranges="defaultTimeRanges" style="width: 150px" @change="onChangeCreateTime" />
            </a-form-item>
            <a-form-item class="smart-query-form-item">
                <a-button type="primary" @click="queryData">
                    <template #icon>
                        <SearchOutlined />
                    </template>
                    查询
                </a-button>
                <a-button @click="resetQuery" class="smart-margin-left10">
                    <template #icon>
                        <ReloadOutlined />
                    </template>
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
                <a-button @click="showForm()" type="primary" size="small">
                    <template #icon>
                        <PlusOutlined />
                    </template>
                    新建
                </a-button>
                <a-button @click="confirmBatchDelete" type="danger" size="small" :disabled="selectedRowKeyList.length == 0">
                    <template #icon>
                        <DeleteOutlined />
                    </template>
                    批量删除
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
                :dataSource="tableData"
                :columns="columns"
                rowKey="id"
                bordered
                :loading="tableLoading"
                :pagination="false"
                :row-selection="{ selectedRowKeys: selectedRowKeyList, onChange: onSelectChange }"
        >
            <template #bodyCell="{ text, record, column }">
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

        <UserinfoForm  ref="formRef" @reloadList="queryData"/>

    </a-card>
</template>
<script setup>
    import { reactive, ref, onMounted } from 'vue';
    import { message, Modal } from 'ant-design-vue';
    import { SmartLoading } from '/@/components/framework/smart-loading';
    import { userinfoApi } from '/@/api/business/userinfo/userinfo-api';
    import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
    import { smartSentry } from '/@/lib/smart-sentry';
    import TableOperator from '/@/components/support/table-operator/index.vue';
    import UserinfoForm from './userinfo-form.vue';
    import SmartEnumSelect from '/@/components/framework/smart-enum-select/index.vue';
    import { defaultTimeRanges } from '/@/lib/default-time-ranges';
    // ---------------------------- 表格列 ----------------------------

    const columns = ref([
        {
            title: 'ID',
            dataIndex: 'id',
            ellipsis: true,
        },
        {
            title: '姓名',
            dataIndex: 'name',
            ellipsis: true,
        },
        {
            title: '年龄',
            dataIndex: 'age',
            ellipsis: true,
        },
        {
            title: '性别',
            dataIndex: 'gender',
            ellipsis: true,
        },
        {
            title: '国籍',
            dataIndex: 'country',
            ellipsis: true,
        },
        {
            title: '身高',
            dataIndex: 'height',
            ellipsis: true,
        },
        {
            title: '体重',
            dataIndex: 'weight',
            ellipsis: true,
        },
        {
            title: '证件类型',
            dataIndex: 'cardType',
            ellipsis: true,
        },
        {
            title: '证件号码',
            dataIndex: 'cardNumber',
            ellipsis: true,
        },
        {
            title: '没有区号的手机号',
            dataIndex: 'purePhoneNumber',
            ellipsis: true,
        },
        {
            title: '用户绑定的手机号（国外手机号会有区号）',
            dataIndex: 'phoneNumber',
            ellipsis: true,
        },
        {
            title: '区号',
            dataIndex: 'countryCode',
            ellipsis: true,
        },
        {
            title: '邮箱',
            dataIndex: 'email',
            ellipsis: true,
        },
        {
            title: '所属队伍ID',
            dataIndex: 'teamId',
            ellipsis: true,
        },
        {
            title: '昵称',
            dataIndex: 'nickname',
            ellipsis: true,
        },
        {
            title: '用户头像，链接地址',
            dataIndex: 'avatarurl',
            ellipsis: true,
        },
        {
            title: '微信openid',
            dataIndex: 'openid',
            ellipsis: true,
        },
        {
            title: '微信unionid',
            dataIndex: 'unionid',
            ellipsis: true,
        },
        {
            title: '删除标志',
            dataIndex: 'deletedFlag',
            ellipsis: true,
        },
        {
            title: '创建时间',
            dataIndex: 'createTime',
            ellipsis: true,
        },
        {
            title: '更新时间',
            dataIndex: 'updateTime',
            ellipsis: true,
        },
        {
            title: '操作',
            dataIndex: 'action',
            fixed: 'right',
            width: 90,
        },
    ]);

    // ---------------------------- 查询数据表单和方法 ----------------------------

    const queryFormState = {
        keywords: undefined, //关键字查询
        gender: undefined, //性别
        country: undefined, //国籍
        cardType: undefined, //证件类型
        deletedFlag: undefined, //删除标志
        createTime: [], //创建时间
        createTimeBegin: undefined, //创建时间 开始
        createTimeEnd: undefined, //创建时间 结束
        pageNum: 1,
        pageSize: 10,
    };
    // 查询表单form
    const queryForm = reactive({ ...queryFormState });
    // 表格加载loading
    const tableLoading = ref(false);
    // 表格数据
    const tableData = ref([]);
    // 总数
    const total = ref(0);

    // 重置查询条件
    function resetQuery() {
        let pageSize = queryForm.pageSize;
        Object.assign(queryForm, queryFormState);
        queryForm.pageSize = pageSize;
        queryData();
    }

    // 查询数据
    async function queryData() {
        tableLoading.value = true;
        try {
            let queryResult = await userinfoApi.queryPage(queryForm);
            tableData.value = queryResult.data.list;
            total.value = queryResult.data.total;
        } catch (e) {
            smartSentry.captureError(e);
        } finally {
            tableLoading.value = false;
        }
    }

    function onChangeCreateTime(dates, dateStrings){
        queryForm.createTimeBegin = dateStrings[0];
        queryForm.createTimeEnd = dateStrings[1];
    }


    onMounted(queryData);

    // ---------------------------- 添加/修改 ----------------------------
    const formRef = ref();

    function showForm(data) {
        formRef.value.show(data);
    }

    // ---------------------------- 单个删除 ----------------------------
    //确认删除
    function onDelete(data){
        Modal.confirm({
            title: '提示',
            content: '确定要删除吗?',
            okText: '删除',
            okType: 'danger',
            onOk() {
                requestDelete(data);
            },
            cancelText: '取消',
            onCancel() {},
        });
    }

    //请求删除
    async function requestDelete(data){
        SmartLoading.show();
        try {
            await userinfoApi.delete(data.id);
            message.success('删除成功');
            queryData();
        } catch (e) {
            smartSentry.captureError(e);
        } finally {
            SmartLoading.hide();
        }
    }

    // ---------------------------- 批量删除 ----------------------------

    // 选择表格行
    const selectedRowKeyList = ref([]);

    function onSelectChange(selectedRowKeys) {
        selectedRowKeyList.value = selectedRowKeys;
    }

    // 批量删除
    function confirmBatchDelete() {
        Modal.confirm({
            title: '提示',
            content: '确定要批量删除这些数据吗?',
            okText: '删除',
            okType: 'danger',
            onOk() {
                requestBatchDelete();
            },
            cancelText: '取消',
            onCancel() {},
        });
    }

    //请求批量删除
    async function requestBatchDelete() {
        try {
            SmartLoading.show();
            await userinfoApi.batchDelete(selectedRowKeyList.value);
            message.success('删除成功');
            queryData();
        } catch (e) {
            smartSentry.captureError(e);
        } finally {
            SmartLoading.hide();
        }
    }
</script>
