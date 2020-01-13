<template>
  <div class="tables-edit-outer">
    <div v-if="!isEditting" class="tables-edit-con">
      <span class="value-con">{{ value }}</span>
      <Button
        v-if="editable"
        @click="startEdit"
        class="tables-edit-btn"
        style="padding: 2px 4px;"
        type="text"
      >
        <Icon type="md-create"></Icon>
      </Button>
    </div>
    <div v-else class="tables-editting-con">
      <Input :value="value" @input="handleInput" class="tables-edit-input"/>
      <Button @click="saveEdit" style="padding: 6px 4px;" type="text">
        <Icon type="md-checkmark"></Icon>
      </Button>
      <Button @click="canceltEdit" style="padding: 6px 4px;" type="text">
        <Icon type="md-close"></Icon>
      </Button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'TablesEdit',
  props: {
    // 当前编辑的单元格值
    value: {
      type: [String, Number],
      require: true
    },
    // 当前编辑的单元格id
    edittingCellId: {
      type: String,
      require: true
    },
    // 表格数据
    params: {
      type: Object,
      require: true
    },
    // 表格里全局设置的是否可编辑
    editable: {
      type: Boolean,
      require: true
    }
  },
  computed: {
    // 判断是否处于编辑状态
    isEditting () {
      return this.edittingCellId === `editting-${this.params.index}-${this.params.column.key}`;
    }
  },
  methods: {
    handleInput (val) {
      this.$emit('input', val);
    },
    startEdit () {
      this.$emit('on-start-edit', this.params);
    },
    saveEdit () {
      this.$emit('on-save-edit', this.params);
    },
    canceltEdit () {
      this.$emit('on-cancel-edit', this.params);
    }
  }
};
</script>

<style lang="less">
.tables-edit-outer {
  height: 100%;
  .tables-edit-con {
    position: relative;
    height: 100%;
    .value-con {
      vertical-align: middle;
    }
    .tables-edit-btn {
      position: absolute;
      right: 10px;
      top: 0;
      display: none;
    }
    &:hover {
      .tables-edit-btn {
        display: inline-block;
      }
    }
  }
  .tables-editting-con {
    .tables-edit-input {
      width: ~"calc(100% - 60px)";
    }
  }
}
</style>
