let loader = null;

const SmartPlugin = {};

SmartPlugin.install = function(Vue, options) {
  Vue.prototype.$smart = {
    loading: (message) => {
      if (loader) {
        loader.hide();
      }

      loader = Vue.$loading.show({
        // Optional parameters
        lockScroll: true,
        color: '#1989fa'
      });
    },
    loadingClear: () => {
      loader.hide();
    }
  };
};

export default SmartPlugin;
