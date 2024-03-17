export const SmartLoading = {
  show: function (msg) {
    uni.showLoading({ title: msg ? msg : '加载中' });
  },

  hide: function () {
    uni.hideLoading();
  },
};

export const SmartToast = {
  success: (message) => {
    uni.showToast({
      title: message,
      icon: 'success',
    });
  },
  error: (message) => {
    uni.showToast({
      title: message,
      icon: 'error',
    });
  },
  toast: (message) => {
    uni.showToast({
      title: message,
      icon: 'none',
    });
  },
};
