import { defineStore } from 'pinia';

export const useDictStore = defineStore({
  id: 'dict',
  state: () => ({
    dict: new Array(),
  }),
  actions: {
    // 获取字典
    getDict(keyCode: any) {
      if (keyCode == null && keyCode == '') {
        return null;
      }
      try {
        for (let i = 0; i < this.dict.length; i++) {
          if (this.dict[i].keyCode == keyCode) {
            return this.dict[i].value;
          }
        }
      } catch (e) {
        return null;
      }
    },
    // 设置字典
    setDict(keyCode: any, value: any) {
      if (keyCode !== null && keyCode !== '') {
        this.dict.push({
          key: keyCode,
          value: value,
        });
      }
    },
    // 删除字典
    removeDict(keyCode: any) {
      let flag = false;
      try {
        for (let i = 0; i < this.dict.length; i++) {
          if (this.dict[i].keyCode == keyCode) {
            this.dict.splice(i, 1);
            return true;
          }
        }
      } catch (e) {
        flag = false;
      }
      return false;
    },
    // 清空字典
    cleanDict() {
      this.dict = new Array();
    },
  },
});
