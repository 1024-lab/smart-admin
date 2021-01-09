/*
 * @Description:
 * @Author: zhuoda
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
