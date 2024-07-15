import { defineAsyncComponent, markRaw } from 'vue';

/**
 * 菜单展示
 * defineAsyncComponent 异步组件
 * markRaw  将一个Vue组件对象转换为响应式对象时，可能会导致不必要的性能开销。使用markRaw方法将组件对象标记为非响应式对象
 */
export const ACCOUNT_MENU = {
  CENTER: {
    menuId: 'center',
    menuName: '个人中心',
    components: markRaw(defineAsyncComponent(() => import('./components/center/index.vue'))),
  },
  PASSWORD: {
    menuId: 'password',
    menuName: '修改密码',
    components: markRaw(defineAsyncComponent(() => import('./components/password/index.vue'))),
  },
  MESSAGE: {
    menuId: 'message',
    menuName: '我的消息',
    components: markRaw(defineAsyncComponent(() => import('./components/message/index.vue'))),
  },
  NOTICE: {
    menuId: 'notice',
    menuName: '通知公告',
    components: markRaw(defineAsyncComponent(() => import('./components/notice/index.vue'))),
  },
  LOGIN_LOG: {
    menuId: 'login-log',
    menuName: '登录日志',
    components: markRaw(defineAsyncComponent(() => import('./components/login-log/index.vue'))),
  },
  OPERATE_LOG: {
    menuId: 'operate-log',
    menuName: '操作日志',
    components: markRaw(defineAsyncComponent(() => import('./components/operate-log/index.vue'))),
  },
};
