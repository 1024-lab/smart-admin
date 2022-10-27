<template>

  <van-nav-bar :title="title" left-text="返回"  left-arrow @click-left="onClickLeft" />

</template>
<script>
export default {
  name: 'RouterNavBar',
  props: {
    title: {
      type: String
    },
    path: {
      type: String
    }
  },
  data() {
    return {};
  },
  methods: {
    onClickLeft() {
      this.$router.push(this.path);
    }
  }
};
</script>

