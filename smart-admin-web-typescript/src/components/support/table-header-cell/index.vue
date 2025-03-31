<template>
  <div class="container">
    <div class="table-title">
      <slot name="title">
        {{ column.title }}
      </slot>
    </div>
    <div class="filter" style="max-width: 300px; min-height: 40px">
      <slot>
        <template v-if="column.filterOptions">
          <template v-if="column.filterOptions.type === 'input'">
            <a-input-search
              @change="change('no-search')"
              v-model:value="modelValue"
              allowClear
              :placeholder="column.placeholder || column.title"
              @search="change"
            />
          </template>
          <template v-else-if="column.filterOptions.type === 'date-range'">
            <a-range-picker
              v-model:value="createDateRange"
              :ranges="column.filterOptions.ranges ? defaultTimeRanges : []"
              style="width: 100%"
              @change="changeCreateDate"
            />
          </template>
          <template v-else-if="column.filterOptions.type === 'datetime-range'">
            <a-range-picker
              show-time
              v-model:value="createDateRange"
              :ranges="column.filterOptions.ranges ? defaultTimeRanges : []"
              style="width: 100%"
              @change="changeCreateDate"
            />
          </template>
          <template v-else-if="column.filterOptions.type === 'month'">
            <a-range-picker v-model:value="createDateRange" picker="month" @change="changeCreateDate" />
          </template>
          <template v-else-if="column.filterOptions.type === 'submit'">
            <div class="smart-table-operate"><a-button :type="column.filterOptions.btnType" @click="submit">查询</a-button></div>
          </template>
          <template v-else>
            <component
              v-if="componentsKey.includes(column.filterOptions.type)"
              :is="getComponent(column.filterOptions.type)"
              width="100%"
              v-model:value="modelValue"
              :multiple="column.filterOptions.multiple"
              :dictCode="column.filterOptions.dictCode"
              :enumName="column.filterOptions.enumName"
              :systemConfigKey="column.filterOptions.systemConfigKey"
              :placeholder="column.placeholder"
              :leaveFlag="column.filterOptions.leaveFlag"
              :categoryType="column.filterOptions.categoryType"
              @change="change"
            />
            <div v-else class="error-component">未定义的组件类型</div>
          </template>
        </template>
      </slot>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { ref, useSlots, watch, onMounted, defineAsyncComponent } from 'vue';
  import dayjs from 'dayjs';
  import _ from 'lodash';
  import { defaultTimeRanges } from '/@/lib/default-time-ranges';
  const props = defineProps({
    value: {
      type: [Number, String, Array, Object],
    },
    column: {
      type: Object,
      default: () => {},
    },
  });

  const components = {
    'enum-select': defineAsyncComponent(() => import('/src/components/framework/smart-enum-select/index.vue')),
    'dict-select': defineAsyncComponent(() => import('/src/components/support/dict-select/index.vue')),
    'employee-select': defineAsyncComponent(() => import('/src/components/system/employee-select/index.vue')),
    'enterprise-select': defineAsyncComponent(() => import('/src/components/business/oa/enterprise-select/index.vue')),
    'boolean-select': defineAsyncComponent(() => import('/src/components/framework/boolean-select/index.vue')),
    'category-tree': defineAsyncComponent(() => import('/src/components/business/category-tree-select/index.vue')),
  };

  const componentsKey = Object.keys(components);

  function getComponent(key) {
    return components[key];
  }

  watch(
    () => props.value,
    () => {
      if (props.column.filterOptions && (props.column.filterOptions.type === 'date-range' || props.column.filterOptions.type === 'datetime-range')) {
        if (!_.isEmpty(props.value) && props.value.length === 2 && !_.isEmpty(props.value[0]) && !_.isEmpty(props.value[1])) {
          createDateRange.value = [dayjs(props.value[0]), dayjs(props.value[1])];
        } else {
          createDateRange.value = [];
        }
      } else {
        modelValue.value = props.value;
      }
    }
  );

  const slots = useSlots();

  const emits = defineEmits(['change', 'update:value']);
  const createDateRange = ref([]);
  const modelValue = ref(undefined);

  function changeCreateDate(dates, dateStrings) {
    // emits('update:value',dates)
    emits('change', {
      type: props.column.filterOptions.type,
      key: props.column.filterOptions.key || props.column.dataIndex,
      value: [dateStrings[0], dateStrings[1]],
      search: true,
    });
  }

  //是否立即查询
  function change(search) {
    let isSearch = true;
    if (search === 'no-search') {
      isSearch = false;
    }
    emits('update:value', modelValue.value);
    emits('change', {
      type: props.column.filterOptions.type,
      key: props.column.filterOptions.key || props.column.dataIndex,
      value: modelValue.value,
      search: isSearch,
    });
  }

  function submit() {
    emits('change', {
      search: true,
    });
  }

  onMounted(() => {
    if (props.column.filterOptions && (props.column.filterOptions.type === 'date-range' || props.column.filterOptions.type === 'datetime-range')) {
      if (!_.isEmpty(props.value) && props.value.length === 2 && !_.isEmpty(props.value[0]) && !_.isEmpty(props.value[1])) {
        createDateRange.value = [dayjs(props.value[0]), dayjs(props.value[1])];
      } else {
        createDateRange.value = [];
      }
    } else {
      modelValue.value = props.value;
    }
  });
</script>
<style scoped lang="less">
  .container {
    overflow: hidden;
  }

  .table-title {
    border-bottom: 1px solid #f0f0f0;
  }

  .filter {
    padding-top: 8px;
  }

  .error-component {
    color: red;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
</style>
