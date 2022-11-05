<template>
  <Card class="warp-card" dis-hover>
    <Alert>
      <h3>三级路由，第四级页面</h3>
      <pre>
        当前页面为第四级页面，页面中有两个按钮，进行权限测试
      </pre>
    </Alert>
    <Form class="tools" inline>
      <FormItem>
        <Input placeholder="请输入" v-model="searchString">
          <Button  icon="ios-search" slot="append"></Button>
        </Input>
      </FormItem>
      <FormItem>
        <Button @click="addContent" v-privilege="'three-level-router-view-add'" icon="md-add" type="primary">添加</Button>
      </FormItem>
    </Form>
    <Table :columns="columns" :data="tableData" border></Table>
  </Card>
</template>

<script>
export default {
  name: 'ThreeLevelRouterView',
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
  },
  methods: {
    addContent() {
      this.$Message.success('哇哦，你点击了添加操作哦~');
    }
  }
};
</script>
<style lang="less" scoped>
</style>
