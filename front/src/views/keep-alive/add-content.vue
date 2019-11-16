<template>
  <Card class="warp-card" dis-hover>
    <Alert>
      <h3>让我们测试一下</h3>
      <pre>
1 Query保存 之后，刷新KeepAlive页面，即router query传入noKeepAlive=true参数
2 Param保存 之后，刷新KeepAlive页面，即router param传入noKeepAlive=true参数
3 返回 之后，不刷新KeepAlive页面，不用传入任何参数
      </pre>
    </Alert>
    <Form class="tools" inline>
      <FormItem>
        <Input placeholder="请输入" v-model="addData" />
      </FormItem>
      <FormItem>
        <Button @click="addContent4Query" type="primary">Query保存</Button>
        <Button @click="addContent4Param" style="margin-left:10px" type="primary">Param保存</Button>
      </FormItem>
      <FormItem>
        <Button @click="backPage" type="default">返回</Button>
      </FormItem>
    </Form>
  </Card>
</template>

<script>
export default {
  name: 'KeepAliveAddContent',
  components: {},
  props: {},
  data() {
    return {
      addData: ''
    };
  },
  computed: {},
  watch: {},
  filters: {},
  methods: {
    addContent4Query() {
      if (this.addData === '') {
        this.$Message.error('请输入内容');
        return;
      }

      this.$Notice.success({
        title: 'noKeepAlive跳转',
        render: h => {
          return h('pre', [
            'this.$router.closeCurrentPageAndPush({\r\n' +
              '    name: "KeepAliveContentList",\r\n' +
              '    query: { noKeepAlive: true }\r\n' +
              '});'
          ]);
        }
      });

      this.$router.closeCurrentPageAndPush({
        name: 'KeepAliveContentList',
        query: { noKeepAlive: true }
      });
    },
    addContent4Param() {
      if (this.addData === '') {
        this.$Message.error('请输入内容');
        return;
      }

      this.$Notice.success({
        title: 'noKeepAlive跳转',
        render: h => {
          return h('pre', [
            'this.$router.closeCurrentPageAndPush({\r\n' +
              '    name: "KeepAliveContentList",\r\n' +
              '    params: { noKeepAlive: true }\r\n' +
              '});'
          ]);
        }
      });

      this.$router.closeCurrentPageAndPush({
        name: 'KeepAliveContentList',
        params: { noKeepAlive: true }
      });
    },
    backPage() {
      // 返回
      this.$router.closeCurrentPage();
    }
  }
};
</script>
<style lang="less" scoped>
</style>
