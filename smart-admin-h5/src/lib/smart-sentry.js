/*
 * @Description:
 * @Author: hanyu
 * @Date: 2020-05-28 12:46:06
 * @LastEditTime: 2020-07-08 09:16:15
 * @LastEditors: hy
 */
// smart sentry
import * as Sentry from '@sentry/browser';
export default {
  /**
 * sentry 主动上报
 * @param {error} error 错误信息
 */
  captureException: (error) => {
    if (error.config && error.data && error && error.headers && error.request && error.status) {
      return;
    }
    Sentry.captureException(error);
  }
};
