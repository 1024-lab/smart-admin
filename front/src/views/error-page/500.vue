<template>
  <error-content code="500" desc="Oh~~鬼知道服务器经历了什么~" :src="src"/>
</template>

<script>
import error404 from '@/assets/images/error-page/error-500.svg';
import ErrorContent from './error-content.vue';
export default {
  name: 'error500',
  components: {
    ErrorContent
  },
  data () {
    return {
      src: error404
    };
  }
};
</script>
