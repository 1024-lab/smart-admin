/*
 * 错误上报sentry
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-09-06 20:49:28
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */

export const smartSentry = {
  /**
   * sentry 主动上报
   */
  captureError: (error) => {
    if (error.config && error.data && error && error.headers && error.request && error.status) {
      return;
    }
    // Sentry.captureException(error);
    console.error(error);
  },
};
