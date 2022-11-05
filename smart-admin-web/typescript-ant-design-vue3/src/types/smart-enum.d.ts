/*
 * @Author: zhuoda
 * @Date: 2021-08-18 16:58:28
 * @LastEditTime: 2021-08-26
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/types/base.d.ts
 */
export interface SmartEnumWrapper<T> {
  [key: string]: SmartEnum<T>;
}

export interface SmartEnum<T> {
  [key: string]: SmartEnumItem<T>;
}

interface SmartEnumItem<T> {
  value: T;
  desc: string;
}

interface SmartEnumPlugin {
  getDescByValue(constantName: string, value: string | number | undefined): string;

  getValueDescList(constantName: string): SmartEnumItem[];

  getValueDesc(constantName: string): { [key: string]: string };
}
