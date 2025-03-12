import { useDictStore } from '/@/store/modules/system/dict';
import { dictApi } from '/@/api/support/dict-api';

/**
 * 获取字典数据
 */

export function useDict(...args) {
  let res = {};
  args.forEach(async (keyCode, index) => {
    res[keyCode] = [];
    const dicts = useDictStore().getDict(keyCode);
    if (dicts) {
      res[keyCode] = dicts;
    } else {
      let result = await dictApi.valueList(keyCode);
      res[keyCode] = result.data;
      useDictStore().setDict(keyCode, res[keyCode]);
    }
  });
  return res;
}
