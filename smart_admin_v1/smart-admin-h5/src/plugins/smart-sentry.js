import * as Sentry from '@sentry/browser';

const SmartSentry = {};

SmartSentry.install = function(Vue, options) {
  Vue.prototype.$smartSentry = {
    captureException: (error) => {
      console.error(error);
      if (error.config && error.data && error && error.headers && error.request && error.status) {
        return;
      }
      Sentry.captureException(error);
    }
  };
};

export default SmartSentry;
