<template>
  <div>
    <van-field
      v-model="result"
      v-bind="$attrs"
      readonly
      is-link
      @click="show = !show"
    />
    <van-popup v-model="show" position="bottom">
      <van-picker
        value-key="desc"
        :columns="columns"
        show-toolbar
        :title="$attrs.label"
        @cancel="show = !show"
        @confirm="onConfirm"
      />
    </van-popup>
  </div>
</template>
<script>
export default {
  name: 'SmartEnumSelectPicker',
  model: {
    prop: 'selectValue'
  },
  props: {
    enumName: {
      type: String
    },
    selectValue: {
      type: String
    }
  },
  data() {
    return {
      show: false,
      result: this.selectValue,
      columns: this.$enum.getValueDescList(this.enumName)
    };
  },
  methods: {
    onConfirm(value) {
      this.result = value.desc;
      this.show = !this.show;
      this.$emit('change', value);
    }
  },
  watch: {
    selectValue: function(newVal) {
      this.result = newVal;
    },
    result(newVal) {
      this.$emit('input', newVal);
    }
  }
};
</script>

<style></style>
