<template>
  <div>
    <Table
      ref="tablesMain"
      :data="insideTableData"
      :columns="insideColumns"
      :stripe="stripe"
      :border="border"
      :show-header="showHeader"
      :width="width"
      :height="height"
      :loading="loading"
      :disabled-hover="disabledHover"
      :highlight-row="highlightRow"
      :row-class-name="rowClassName"
      :size="size"
      :no-data-text="noDataText"
      :no-filtered-data-text="noFilteredDataText"
      @on-current-change="onCurrentChange"
      @on-select="onSelect"
      @on-select-cancel="onSelectCancel"
      @on-select-all="onSelectAll"
      @on-selection-change="onSelectionChange"
      @on-sort-change="onSortChange"
      @on-filter-change="onFilterChange"
      @on-row-click="onRowClick"
      @on-row-dblclick="onRowDblclick"
      @on-expand="onExpand"
    >
      <slot name="header" slot="header"></slot>
      <slot name="footer" slot="footer"></slot>
      <slot name="loading" slot="loading"></slot>
    </Table>
    <Page v-if="pageShow"
    :total="total"
    :current="pageNumber"
    :page-size="pageSize"
    :show-elevator="showElevator"
    :show-sizer="showSizer"
    :show-total="showTotal"
    :page-size-opts='pageOpts'
    style="margin:24px 0;text-align:right;"
    @on-change="pageChange" @on-page-size-change="pageSizeChange"></Page>

  </div>
</template>

<script>
import TablesEdit from './edit.vue';
import handleBtns from './handle-btns';
import './index.less';
export default {
  name: 'Tables',
  props: {
    // table 表格数据 集合类型
    value: {
      type: Array,
      default () {
        return [];
      }
    },
    // 是否显示页码
    pageShow: {
      type: Boolean,
      default: false
    },
    // 表头
    columns: {
      type: Array,
      default () {
        return [];
      }
    },
    // 每页数量
    pageSize: {
      type: Number,
      default: 10
    },
    // 显示电梯，可以快速切换到某一页
    showElevator: {
      type: Boolean,
      default: true
    },
    // table页码
    pageNumber: {
      type: Number,
      default: 1
    },
    // 显示分页，用来改变page-size
    showSizer: {
      type: Boolean,
      default: true
    },
    // 显示总数
    showTotal: {
      type: Boolean,
      default: true
    },
    // 总数
    total: {
      type: Number,
      default: 0
    },
    // 表格尺寸，可选值为 large、small、default 或者不填
    size: {
      type: String,
      default: null
    },
    // 表格宽度，单位 px
    width: {
      type: [Number, String]
    },
    // 表格高度，单位 px，设置后，如果表格内容大于此值，会固定表头
    height: {
      type: [Number, String]
    },
    // 是否显示间隔斑马纹
    stripe: {
      type: Boolean,
      default: false
    },
    // 是否显示纵向边框
    border: {
      type: Boolean,
      default: false
    },
    // 是否显示表头
    showHeader: {
      type: Boolean,
      default: true
    },
    // 是否支持高亮选中的行，即单选
    highlightRow: {
      type: Boolean,
      default: false
    },
    // 行的 className 的回调方法，传入参数：row：当前行数据 ,index：当前行的索引
    rowClassName: {
      type: Function,
      default () {
        return '';
      }
    },
    // 数据为空时显示的提示内容
    noDataText: {
      type: String,
      default: ''
    },
    // 筛选数据为空时显示的提示内容
    noFilteredDataText: {
      type: String,
      default: ''
    },
    // 禁用鼠标悬停时的高亮
    disabledHover: {
      type: Boolean
    },
    // 表格是否加载中
    loading: {
      type: Boolean,
      default: false
    },
    /**
     * @description 全局设置是否可编辑
     */
    editable: {
      type: Boolean,
      default: false
    },
    /**
     * @description 是否可搜索
     */
    searchable: {
      type: Boolean,
      default: false
    },
    /**
     * @description 搜索控件所在位置，'top' / 'bottom'
     */
    searchPlace: {
      type: String,
      default: 'top'
    }
  },
  /**
   * Events
   * @on-start-edit 返回值 {Object} ：同iview中render函数中的params对象 { row, index, column }
   * @on-cancel-edit 返回值 {Object} 同上
   * @on-save-edit 返回值 {Object} ：除上面三个参数外，还有一个value: 修改后的数据
   */
  data () {
    return {
      // 分页时每页数量选择
      pageOpts: [10, 20, 30, 50, 100],
      // 表格 每列的配置 如名称，宽度
      insideColumns: [],
      // 表格数据
      insideTableData: [],
      // 正在编辑的单元格id
      edittingCellId: '',
      // 编辑内容
      edittingText: '',
      // 搜索内容
      searchValue: '',
      // 搜索关键词
      searchKey: ''
    };
  },
  watch: {
    columns (columns) {
      this.handleColumns(columns);
      this.setDefaultSearchKey();
    },
    value (val) {
      this.handleTableData();
      if (this.searchable) this.handleSearch();
    }
  },
  mounted () {
    this.handleColumns(this.columns);
    this.setDefaultSearchKey();
    this.handleTableData();
  },
  methods: {
    // 页码改变
    pageChange (e) {
      this.$emit('on-change', e);
    },
    // 每页条数改变
    pageSizeChange (e) {
      this.$emit('on-page-size-change', e);
    },
    suportEdit (item, index) {
      item.render = (h, params) => {
        return h(TablesEdit, {
          props: {
            params: params,
            value: this.insideTableData[params.index][params.column.key],
            edittingCellId: this.edittingCellId,
            editable: this.editable
          },
          on: {
            'input': val => {
              this.edittingText = val;
            },
            'on-start-edit': (params) => {
              this.edittingCellId = `editting-${params.index}-${params.column.key}`;
              this.$emit('on-start-edit', params);
            },
            'on-cancel-edit': (params) => {
              this.edittingCellId = '';
              this.$emit('on-cancel-edit', params);
            },
            'on-save-edit': (params) => {
              this.value[params.row.initRowIndex][params.column.key] = this.edittingText;
              this.$emit('input', this.value);
              this.$emit('on-save-edit', Object.assign(params, { value: this.edittingText }));
              this.edittingCellId = '';
            }
          }
        });
      };
      return item;
    },
    surportHandle (item) {
      let options = item.options || [];
      let insideBtns = [];
      options.forEach(item => {
        if (handleBtns[item]) insideBtns.push(handleBtns[item]);
      });
      let btns = item.button ? [].concat(insideBtns, item.button) : insideBtns;
      item.render = (h, params) => {
        params.tableData = this.value;
        return h('div', btns.map(item => item(h, params, this)));
      };
      return item;
    },
    // 设置表格列头
    handleColumns (columns) {
      this.insideColumns = columns.map((item, index) => {
        let res = item;
        if (res.editable) { res = this.suportEdit(res, index); }
        if (res.key === 'handle') { res = this.surportHandle(res); }
        return res;
      });
    },
    // 设置默认搜索关键词
    setDefaultSearchKey () {
      this.searchKey = this.columns[0].key !== 'handle' ? this.columns[0].key : (this.columns.length > 1 ? this.columns[1].key : '');
    },
    handleClear (e) {
      if (e.target.value === '') this.insideTableData = this.value;
    },
    handleSearch () {
      this.insideTableData = this.value.filter(item => item[this.searchKey].indexOf(this.searchValue) > -1);
    },
    handleTableData () {
      this.insideTableData = this.value.map((item, index) => {
        let res = item;
        res.initRowIndex = index;
        return res;
      });
    },
    exportCsv (params) {
      this.$refs.tablesMain.exportCsv(params);
    },
    clearCurrentRow () {
      this.$refs.talbesMain.clearCurrentRow();
    },
    onCurrentChange (currentRow, oldCurrentRow) {
      this.$emit('on-current-change', currentRow, oldCurrentRow);
    },
    onSelect (selection, row) {
      this.$emit('on-select', selection, row);
    },
    onSelectCancel (selection, row) {
      this.$emit('on-select-cancel', selection, row);
    },
    onSelectAll (selection) {
      this.$emit('on-select-all', selection);
    },
    onSelectionChange (selection) {
      this.$emit('on-selection-change', selection);
    },
    onSortChange (column, key, order) {
      this.$emit('on-sort-change', column, key, order);
    },
    onFilterChange (row) {
      this.$emit('on-filter-change', row);
    },
    onRowClick (row, index) {
      this.$emit('on-row-click', row, index);
    },
    onRowDblclick (row, index) {
      this.$emit('on-row-dblclick', row, index);
    },
    onExpand (row, status) {
      this.$emit('on-expand', row, status);
    }
  }
};
</script>
