<template>
  <template v-if="props.showStyle">
    <template v-for="(item, index) in dataList" :key="item.dictDataId">
      <span v-if="item.color" :style="{ color: token[item.color] }"> {{ item.dataLabel }} </span>
      <span v-else>{{ item.dataLabel }}</span>
      {{ index < dataList.length - 1 ? ',' : '' }}
    </template>
  </template>
  <!--����ʾ���Ե���ɫ��ʾ-->
  <template v-else>
    <span>{{ dataList }}</span>
  </template>
</template>

<script setup lang="ts">
  import { computed } from 'vue';
  import { useDictStore } from '/@/store/modules/system/dict';
  import { theme } from 'ant-design-vue';
  import { DICT_DATA_STYLE_ENUM } from '/@/constants/support/dict-const';

  const props = defineProps({
    dictCode: String,
    dataValue: [String, Number],
    showStyle: { type: Boolean, default: false },
  });

  const dataList = computed(() => {
    if (!props.showStyle) {
      return useDictStore().getDataLabels(props.dictCode, props.dataValue);
    }

    let dictDataList = useDictStore().getDataList(props.dictCode, props.dataValue);
    let dataList = [];
    for (const item of dictDataList) {
      if (item.dataStyle) {
        dataList.push(Object.assign({}, item, { color: DICT_DATA_STYLE_ENUM[item.dataStyle.toUpperCase()].color }));
      } else {
        dataList.push(Object.assign({}, item));
      }
    }
    return dataList;
  });

  const { useToken } = theme;
  const { token } = useToken();
</script>
