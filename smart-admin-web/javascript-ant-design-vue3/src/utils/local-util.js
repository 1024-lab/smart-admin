/*
 * localStorage 相关操作
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-09-06 20:58:49
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */
export const localSave = (key, value) => {
  localStorage.setItem(key, value);
};

export const localRead = (key) => {
  return localStorage.getItem(key) || '';
};

export const localClear = () => {
  localStorage.clear();
};
