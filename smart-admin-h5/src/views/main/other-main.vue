<template>
  <div>

    <!---------------- 頁面内容 begin ---------------->
    <div class="main-content">
      <keep-alive>
        <router-view v-if="$route.meta.keepAlive" />
      </keep-alive>
      <router-view v-if="!$route.meta.keepAlive" />
    </div>

  </div>
</template>

<script type="text/javascript">
import mainMixin from './main-mixin';

export default {
  name: 'OtherMain',
  mixins: [mainMixin],
  data() {
    return {};
  },
  created() {
  },
  computed: {},
  methods: {}
};
</script>
<style lang="less" scoped>
</style>
