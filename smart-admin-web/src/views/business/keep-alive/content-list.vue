<template>
  <Card class="warp-card" dis-hover>
    <Alert>
      <h3>keep-alive介绍：</h3>
      <pre>
简介：我们修改了原版iview-admin缓存策略,使之更灵活

用途：
· 用于关闭页面后跳转指定页面,可控制是否刷新跳转页面
· 点击左侧菜单栏打开已有页面,标签页切换依旧会缓存
      </pre>
    </Alert>
    <Form class="tools" inline>
      <FormItem>
        <Input placeholder="请输入" v-model="searchString">
          <Button @click="searchContent" icon="ios-search" slot="append"></Button>
        </Input>
      </FormItem>
      <FormItem>
        <Button @click="addContent" icon="md-add" type="primary">添加</Button>
      </FormItem>
    </Form>
    <Table :columns="columns" :data="tableData" border></Table>
  </Card>
</template>

<script>
export default {
  name: 'KeepAliveContentList',
  components: {},
  props: {},
  data() {
    return {
      sourceData: [
        { name: '张三' },
        { name: '李四' },
        { name: '王二' },
        { name: '唐三' },
        { name: '赵大' },
        { name: '李二' }
      ],
      columns: [
        {
          title: '名称',
          key: 'name'
        }
      ],
      tableData: [],
      searchString: ''
    };
  },
  mounted() {
    if (this.$route.query.data) {
      this.sourceData = [{ name: this.$route.query.data }, ...this.sourceData];
    }
    this.searchContent();
  },
  methods: {
    searchContent() {
      this.tableData = [];
      this.sourceData.forEach(val => {
        if (
          val.name.indexOf(this.searchString) !== -1 ||
          this.searchString == ''
        ) {
          this.tableData.push(val);
        }
      });
    },
    addContent() {
      this.$router.push({
        path: '/keep-alive/add-content',
        query: {}
      });
    }
  }
};
</script>
<style lang="less" scoped>
</style>
