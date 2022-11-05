<template>
    <div>
        <Card class="smart-query-card">
            <!------ 查询条件第一行 begin------->
            <Row class="smart-query-form-row">
                <span>
                    ID :
                    <Input placeholder="请输入ID" style="width: 180px" v-model="queryForm.id" />
                </span>
                <span>
                    品种 :
                    <Input placeholder="请输入品种" style="width: 180px" v-model="queryForm.kind" />
                </span>
                <span>
                    名字 :
                    <Input placeholder="请输入名字" style="width: 180px" v-model="queryForm.name" />
                </span>
                <span>
                    颜色 :
                    <Input placeholder="请输入颜色" style="width: 180px" v-model="queryForm.color" />
                </span>
                <ButtonGroup>
                    <Button
                            @click="queryList"
                            icon="ios-search"
                            type="primary"
                            v-privilege="'peony-list-query'"
                    >查询</Button>
                    <Button
                            @click="resetQueryList"
                            icon="md-refresh"
                            type="default"
                            v-privilege="'peony-list-query'"
                    >重置</Button>
                </ButtonGroup>

                <Button
                        @click="showMoreQueryConditionFlag = !showMoreQueryConditionFlag"
                        icon="md-more"
                        style="margin-left: 20px"
                        type="primary"
                        v-privilege="'peony-list-query'"
                >{{showMoreQueryConditionFlag?'隐藏':'展开'}}</Button>
            </Row>
            <!------ 查询条件第一行 begin------->

            <!------ 查询条件第二行 begin------->
            <Row class="smart-query-form-row" v-show="showMoreQueryConditionFlag">
                <span>
                  xxx：
                  <Input placeholder="请输入xxx" style="width: 250px" />
                </span>
                <span>
                    创建时间:
                    <DatePicker
                            placeholder="选择创建日期范围"
                            split-panels
                            style="width: 200px"
                            type="daterange"
                            v-model="queryForm.createTimeRange"
                    ></DatePicker>
                </span>
                <span>
                    更新时间:
                    <DatePicker
                            placeholder="选择更新日期范围"
                            split-panels
                            style="width: 200px"
                            type="daterange"
                            v-model="queryForm.updateTimeRange"
                    ></DatePicker>
                </span>
            </Row>
            <!------ 查询条件第二行 end------->
        </Card>

        <Card class="warp-card">
            <!-------操作按钮行 begin------->
            <Row class="marginBottom10">
                <Button
                        @click="showAddPeonyForm"
                        icon="md-add"
                        size="small"
                        type="primary"
                        v-privilege="'peony-list-add'"
                >新建数据</Button>
                <Button
                        @click="showBatchDeleteModal"
                        class="marginLeft10"
                        icon="ios-trash-outline"
                        size="small"
                        type="error"
                        v-privilege="'peony-list-batch-delete'"
                >批量删除</Button>

                <Button
                        :loading="allExportBtnLoading"
                        @click="exportAll"
                        class="marginLeft10 float-right"
                        icon="ios-cloud-download-outline"
                        size="small"
                        type="warning"
                        v-privilege="'peony-list-export-all'"
                >导出全部</Button>

                <Button
                        :loading="batchExportBtnLoading"
                        @click="batchExport"
                        class="marginLeft10 float-right"
                        icon="ios-download-outline"
                        size="small"
                        type="warning"
                        v-privilege="'peony-list-batch-export'"
                >批量导出</Button>

            </Row>
            <!-------操作按钮行 end------->

            <!-------表格列表 begin------->
            <Table
                    :columns="mainTable.columnArray"
                    :data="mainTable.data"
                    :loading="mainTable.loading"
                    @on-sort-change="handleSortChange"
                    border
                    highlight-row
                    ref="mainTable"
            >
            
                 <template slot-scope="{ row, index }" slot="imageUrl">
                     <img width="40" height="40" v-if="index % 2 === 0" src="http://q8deiydpv.bkt.clouddn.com/image/peony1.jpg"/>
                       <img v-if="index % 2 === 1"  width="40" height="40" src="http://q8deiydpv.bkt.clouddn.com/image/peony2.jpg"/>
                </template>
            </Table>

            <Page
                    :current="queryForm.pageNum"
                    :page-size="queryForm.pageSize"
                    :page-size-opts="mainTablePage.pageSizeOps"
                    :total="mainTablePage.total"
                    @on-change="changeMainTablePagePageNum"
                    @on-page-size-change="changeMainTablePagePageSize"
                    class="smart-query-table-page"
                    show-elevator
                    show-sizer
                    show-total
            />
        </Card>
        <!-------表格列表 end------->

        <!-------批量删除Modal begin------->
        <Modal title="批量删除" v-model="batchDeleteModal.show" width="450">
            <Form :label-width="80">
                <FormItem>
                    <h3 class="error-color">确定要删除以下数据吗？</h3>
                </FormItem>
                <FormItem label="删除数据">
                    <Card style="width:350px;height:250px;overflow-y:scroll;">
                        <ul>
                            <li v-for="item in mainTableSelectArray">
                                <a href="#">{{ item.id }}</a>
                            </li>
                        </ul>
                    </Card>
                </FormItem>
            </Form>
            <div slot="footer">
                <Button @click="batchDeleteModal.show = false" size="small" type="default">取消</Button>
                <Button @click="batchDelete" size="small" type="primary">确定删除</Button>
            </div>
        </Modal>
        <!-------批量删除Modal end------->

        <!-------添加、更新 Form表单 begin------->
        <Modal
                :footer-hide="true"
                :title="saveModal.isUpdate?'更新':'新建'"
                v-model="saveModal.show"
                @on-cancel="saveFormClose"
                width="500"
        >
            <PeonyListForm
                    :isUpdate="saveModal.isUpdate"
                    :updateData="saveModal.updateData"
                    @on-form-close="saveFormClose"
            />
        </Modal>
        <!-------添加、更新 Form表单 end------->
    </div>
</template>

<script>
    import {dateTimeRangeConvert} from '@/lib/util'
    import { PAGE_SIZE_OPTIONS } from '@/constants/table-page';
    import { peonyApi } from '@/api/peony';
    import PeonyListForm from './components/peony-list-form';
    const PAGE_SIZE_INIT = 20;
    export default {
        name: 'PeonyList',
        components: {
                PeonyListForm
        },
        props: {},
        data() {
            return {
                /* -------------------------添加、更新表单 ------------------------- */
                saveModal: {
                    show: false,
                    isUpdate: false,
                    updateData: null
                },
                /* -------------------------批量操作------------------------- */
                //批量刪除彈出框
                batchDeleteModal: {
                    show: false
                },
                //表格多选选中的元素数组
                mainTableSelectArray: [],
                /* -------------------------导出操作------------------------- */
                //批量导出loading按钮
                batchExportBtnLoading:false,
                //导出全部loading按钮
                allExportBtnLoading:false,
                /* -------------------------查询条件相关数据-------------------- */
                //搜索表单
                queryForm: {
                   //ID
                   id:null,
                   //品种
                   kind:null,
                   //名字
                   name:null,
                   //颜色
                   color:null,
                    createTimeRange: ["",""],
                    updateTimeRange: ["",""],
                    pageNum: 1,
                    pageSize: PAGE_SIZE_INIT,
                    orders: []
                },
                //是否展示更多搜索条件
                showMoreQueryConditionFlag: false,
                /*  -------------------------表格相关数据------------------------- */
                //表格分页
                mainTablePage: {
                    total: 0,
                    pageSizeOps: PAGE_SIZE_OPTIONS
                },
                //表格
                mainTable: {
                    //加载中
                    loading: false,
                    //表格数据
                    data: [],
                    //表格列
                    columnArray: [
                        {
                            type: 'selection',
                            width: 30,
                            align: 'center'
                        },
                                                {
                            title: 'ID',
                            key: 'id',
                            tableColumn: 't_peony.id',
                            sortable: 'custom'
                        },
                                                {
                            title: '品种',
                            key: 'kind',
                            tableColumn: 't_peony.kind',
                            sortable: 'custom'
                        },
                                                {
                            title: '名字',
                            key: 'name',
                            tableColumn: 't_peony.name',
                            sortable: 'custom'
                        },
                                                {
                            title: '颜色',
                            key: 'color',
                            tableColumn: 't_peony.color',
                            sortable: 'custom'
                        },
                                                {
                            title: '图片链接',
                            key: 'imageUrl',
                            tableColumn: 't_peony.image_url',
                            sortable: 'custom',
                            slot:'imageUrl'
                        },
                                                {
                            title: '创建时间',
                            key: 'createTime',
                            tableColumn: 't_peony.create_time',
                            sortable: 'custom'
                        },
                                                {
                            title: '更新时间',
                            key: 'updateTime',
                            tableColumn: 't_peony.update_time',
                            sortable: 'custom'
                        },
                                                {
                            title: '操作',
                            key: 'action',
                            align: 'right',
                            width: 130,
                            className: 'action-hide',
                            render: (h, params) => {
                                let actions = [
                                    {
                                        title: '编辑',
                                        directives: [
                                            {
                                                name: 'privilege',
                                                value: 'peony-list-update'
                                            }
                                        ],
                                        action: () => {
                                            this.showEditPeonyForm(params.row);
                                        }
                                    }
                                ];
                                return this.$tableAction(h, actions);
                            }
                        }
                    ]
                }
            };
        },
        computed: {},
        watch: {},
        filters: {},
        created() {},
        mounted() {
            this.queryList();
        },
        beforeCreate() {},
        beforeMount() {},
        beforeUpdate() {},
        updated() {},
        beforeDestroy() {},
        destroyed() {},
        activated() {},
        methods: {
            /* -------------------------查询相关 begin------------------------- */
            convertQueryParam(){
                let createTimeArray = dateTimeRangeConvert(this.queryForm.createTimeRange);
                let updateTimeArray = dateTimeRangeConvert(this.queryForm.updateTimeRange);
                return {...this.queryForm,
                    createTimeBegin:createTimeArray[0],
                    createTimeEnd:createTimeArray[1],
                    updateTimeBegin:updateTimeArray[0],
                    updateTimeEnd:updateTimeArray[1]
                };
            },
            //查询
            async queryList() {
                this.mainTable.loading = true;
                try {
                    let params = this.convertQueryParam();
                    let result = await peonyApi.queryPeony(params);
                    this.mainTable.data = result.data.list;
                    this.mainTablePage.total = result.data.total;
                } finally {
                    this.mainTable.loading = false;
                }
            },
            //重置查询
            resetQueryList() {
                let pageSize = this.queryForm.pageSize;
                this.queryForm = {
                    id:null,
                    kind:null,
                    name:null,
                    color:null,
                    createTimeRange: ["",""],
                    updateTimeRange: ["",""],
                    pageNum: 1,
                    pageSize: pageSize,
                    orders: []
                };
                this.queryList();
            },
            //修改页码
            changeMainTablePagePageNum(pageNum) {
                this.queryForm.pageNum = pageNum;
                this.queryList();
            },
            //修改页大小
            changeMainTablePagePageSize(pageSize) {
                this.queryForm.pageNum = 1;
                this.queryForm.pageSize = pageSize;
                this.queryList();
            },
            //处理排序
            handleSortChange(column) {
                if (column.order === 'normal') {
                    this.queryForm.orders = [];
                } else {
                    this.queryForm.orders = [
                        {
                            column: column.column.tableColumn,
                            asc: 'asc' === column.order
                        }
                    ];
                }
                this.queryList();
            },
            /*-------------------------查询相关 end------------------------- */

            /*-------------------------批量操作 begin------------------------- */
            //显示批量删除弹窗
            showBatchDeleteModal() {
                if (!this.validateMainTableSelection()) {
                    return;
                }
                this.batchDeleteModal.show = true;
            },
            //批量删除
            async batchDelete() {
                this.$Spin.show();
                await peonyApi.batchDeletePeony(
                        this.mainTableSelectArray.map(e => e.id)
                );
                this.batchDeleteModal.show = false;
                this.$Message.success('刪除成功');
                this.$Spin.hide();
                this.queryList();
            },
            //清空选中
            clearMainTableSelection() {
                this.mainTableSelectArray = [];
            },
            //校验是否有选中
            validateMainTableSelection() {
                this.mainTableSelectArray = this.$refs.mainTable.getSelection();
                if (this.mainTableSelectArray.length < 1) {
                    this.$Message.error('请选择至少一条数据');
                    return false;
                }
                return true;
            },
            /*-------------------------批量操作 end------------------------- */

            /*-------------------------导入导出 begin------------------------- */
            //导出全部
            async exportAll(){
                try{
                    this.allExportBtnLoading = true;
                    let params = this.convertQueryParam();
                    await peonyApi.exportAll(params);
                }  catch(e){
                    console.log(e);
                }finally{
                    this.allExportBtnLoading = false;
                }
            },
            //批量导出
            async batchExport(){
                if (!this.validateMainTableSelection()) {
                    return;
                }
                try{
                    this.batchExportBtnLoading = true;
                    await peonyApi.batchExport(this.mainTableSelectArray.map(e => e.id));
                }  catch(e){
                    console.log(e);
                }finally{
                    this.batchExportBtnLoading = false;
                }
            },
            /*-------------------------导入导出 end------------------------- */

            /*-------------------------添加，修改 表单 begin------------------------- */
            //显示添加表单
            showAddPeonyForm() {
                this.saveModal.isUpdate = false;
                this.saveModal.show = true;
            },
            showEditPeonyForm(updateObject){
                this.saveModal.isUpdate =true;
                this.saveModal.updateData = updateObject;
                this.saveModal.show = true;
            },
            saveFormClose(){
                this.saveModal.show = false;
                this.queryList();
            }
            /*-------------------------添加，修改 表单 end------------------------- */
        }
    };
</script>
