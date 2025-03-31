import { defineStore } from 'pinia';
import { dictApi } from '/@/api/support/dict-api';
import { smartSentry } from '/@/lib/smart-sentry';
import { DICT_SPLIT } from '/@/constants/support/dict-const.js';
import _ from 'lodash';

export const useDictStore = defineStore({
  id: 'dict',
  state: () => ({
    // 字典集合
    dictMap: new Map(),
  }),

  actions: {
    // 获取字典数据
    getDictData(dictCode) {
      if (!dictCode) {
        return [];
      }
      let dictDataList = this.dictMap.get(dictCode);
      return dictDataList ? dictDataList : [];
    },

    // 获取字典的值名称
    getDataLabels(dictCode, dataValue) {
      if (!dataValue) {
        return '';
      }
      let dict = this.getDictData(dictCode);
      if (dict.length === 0) {
        return '';
      }

      let valueArray = dataValue.split(DICT_SPLIT);
      let result = [];
      for (let item of valueArray) {
        let target = _.find(dict, { dataValue: item });
        if (target) {
          result.push(target.dataLabel);
        }
      }
      return result.join(DICT_SPLIT);
    },
    // 初始化字典
    initData(dictDataList){
      this.dictMap.clear();
      for (let data of dictDataList) {
        let dataArray = this.dictMap.get(data.dictCode);
        if (!dataArray) {
          dataArray = [];
          this.dictMap.set(data.dictCode, dataArray);
        }
        dataArray.push(data);
      }
    },
  },
});
