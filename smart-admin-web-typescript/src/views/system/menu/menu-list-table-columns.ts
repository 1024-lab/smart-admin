/*
 *  菜单表格列
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-05-12 19:46:11
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */
import { ref } from 'vue';
export const columns = ref([
  {
    title: '名称',
    dataIndex: 'menuName',
    key: 'ID',
    width: 200,
  },
  {
    title: '类型',
    dataIndex: 'menuType',
    width: 80,
  },
  {
    title: '图标',
    dataIndex: 'icon',
    width: 50,
  },
  {
    title: '路径',
    dataIndex: 'path',
    ellipsis: true,
  },
  {
    title: '组件',
    dataIndex: 'component',
    ellipsis: true,
  },
  {
    title: '后端权限',
    dataIndex: 'apiPerms',
    ellipsis: true,
  },
  {
    title: '前端权限',
    dataIndex: 'webPerms',
    ellipsis: true,
  },
  {
    title: '顺序',
    dataIndex: 'sort',
    width: 80,
  },
  {
    title: '操作',
    dataIndex: 'operate',
    width: 100,
  },
]);
