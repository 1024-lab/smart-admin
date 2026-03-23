<!--
  * 模型配置表
  *
  * @Author:    Mission
  * @Date:      2026-01-28 09:59:38
  * @Copyright  Mission
-->
<template>
    <!---------- 查询表单form begin ----------->
    <a-form class="smart-query-form">
        <a-row class="smart-query-form-row">
            <a-form-item label="模型名称" class="smart-query-form-item">
                <a-input style="width: 200px" v-model:value="queryForm.modelName" placeholder="模型名称" />
            </a-form-item>
            <a-form-item label="供应商" class="smart-query-form-item">
                <a-select style="width: 140px" v-model:value="queryForm.provider" placeholder="全部">
                    <a-select-option v-for="item in providerList" :key="item.value" :value="item.value">
                        {{ item.desc }}
                    </a-select-option>
                </a-select>
            </a-form-item>
            <a-form-item class="smart-query-form-item">
                <a-button type="primary" @click="onSearch">
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
                <a-button @click="showForm" type="primary" size="small">
                    <template #icon>
                        <PlusOutlined />
                    </template>
                    新建
                </a-button>
                <a-button @click="confirmBatchDelete" type="primary" danger size="small" :disabled="selectedRowKeyList.length == 0">
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
            :scroll="{ y: 800 }"
            :dataSource="tableData"
            :columns="columns"
            rowKey="id"
            bordered
            :loading="tableLoading"
            :pagination="false"
            :row-selection="{ selectedRowKeys: selectedRowKeyList, onChange: onSelectChange }"
        >
            <template #bodyCell="{ text, record, column }">
                <template v-if="column.dataIndex === 'provider'">
                    {{ getProviderDesc(text) }}
                </template>
                <template v-if="column.dataIndex === 'isActive'">
                    <DictLabel :dict-code="DICT_CODE_ENUM.YES_NO || 'YES_NO'" :data-value="text" />
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

        <AiModelConfigForm  ref="formRef" @reloadList="queryData"/>

    </a-card>
</template>
<script setup>
    import { reactive, ref, onMounted } from 'vue';
    import { message, Modal } from 'ant-design-vue';
    import { SmartLoading } from '/@/components/framework/smart-loading';
    import { aiModelConfigApi } from '/@/api/business/ai/model/ai-model-config-api';
    import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
    import { AI_MODEL_PROVIDER_ENUM } from '/@/constants/business/ai/model-const';
    import { smartSentry } from '/@/lib/smart-sentry';
    import TableOperator from '/@/components/support/table-operator/index.vue';
    import AiModelConfigForm from './ai-model-config-form.vue';
    import { DICT_CODE_ENUM } from '/@/constants/support/dict-const.js';
    import DictLabel from '/@/components/support/dict-label/index.vue';

    // ---------------------------- 表格列 ----------------------------

    const providerList = Object.values(AI_MODEL_PROVIDER_ENUM);

    const columns = ref([
        {
            title: 'id',
            dataIndex: 'id',
            ellipsis: true,
        },
        {
            title: '供应商',
            dataIndex: 'provider',
            ellipsis: true,
        },
        {
            title: 'Base URL',
            dataIndex: 'baseUrl',
            ellipsis: true,
        },
        {
            title: 'API密钥',
            dataIndex: 'apiKey',
            ellipsis: true,
        },
        {
            title: '配置名称',
            dataIndex: 'modelName',
            ellipsis: true,
        },
        {
            title: '是否激活',
            dataIndex: 'isActive',
            ellipsis: true,
        },
        {
            title: '模型编码',
            dataIndex: 'endPointModeName',
            ellipsis: true,
        },
        {
            title: '提交路径',
            dataIndex: 'path',
            ellipsis: true,
        },
        {
            title: '模型类型',
            dataIndex: 'modelType',
            ellipsis: true,
        },
        {
            title: '最大令牌',
            dataIndex: 'maxTokens',
            width: 100,
            ellipsis: true,
        },
        {
            title: '扩展JSON',
            dataIndex: 'extraConfigJson',
            ellipsis: true,
        },
        {
            title: '创建人姓名',
            dataIndex: 'createByName',
            ellipsis: true,
        },
        {
            title: '创建时间',
            dataIndex: 'createTime',
            ellipsis: true,
        },
        {
            title: '更新人姓名',
            dataIndex: 'updateByName',
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
        modelName: undefined, //模型名称
        provider: undefined,
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

    // 搜索
    function onSearch(){
      queryForm.pageNum = 1;
      queryData();
    }

    // 查询数据
    async function queryData() {
        tableLoading.value = true;
        try {
            let queryResult = await aiModelConfigApi.queryPage(queryForm);
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

    function getProviderDesc(value) {
        return AI_MODEL_PROVIDER_ENUM[value]?.desc || value || '-';
    }

    // ---------------------------- 单个删除 ----------------------------
    //确认删除
    function onDelete(data){
        Modal.confirm({
            title: '提示',
            content: '确定要删除选吗?',
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
            await aiModelConfigApi.delete(data.id);
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
            await aiModelConfigApi.batchDelete(selectedRowKeyList.value);
            message.success('删除成功');
            queryData();
        } catch (e) {
            smartSentry.captureError(e);
        } finally {
            SmartLoading.hide();
        }
    }
</script>
