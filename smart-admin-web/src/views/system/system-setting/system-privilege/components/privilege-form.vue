<template>
  <Modal
    :closable="false"
    :mask-closable="false"
    :title="privilege.functionName"
    :width="680"
    @on-ok="submitForm()"
    v-model="show"
  >
    <Form :label-width="100" :model="privilege" ref="privilegeFormRef">
      <Form-item label="菜单名称：" required>
        <Input disabled placeholder="请输入菜单名称" v-model="title"></Input>
      </Form-item>
      <Form-item label="功能点名称：" prop="functionName" required>
        <Input disabled placeholder="请输入功能点名称" v-model="privilege.functionName"></Input>
      </Form-item>
      <Form-item label="功能Key：" prop="functionKey" required>
        <Input disabled placeholder="请输入功能Key" v-model="privilege.functionKey"></Input>
      </Form-item>
      <Form-item label="Url：">
        <Select filterable multiple v-model="urlArray">
          <OptionGroup :key="i" :label="items.label" v-for="(items, i) in urlList">
            <Option :key="j" :label="item.url" :value="item.name" v-for="(item, j) in items.data">
              <span>{{item.url}}</span>
              <span style="float:right;color:#ccc">{{item.comment}}</span>
            </Option>
          </OptionGroup>
        </Select>
      </Form-item>
    </Form>
    <div slot="footer">
      <Button @click="submitForm" type="primary">保存</Button>
      <Button @click="cancel" type="default">取消</Button>
    </div>
  </Modal>
</template>

<script>
import { privilegeApi } from '@/api/privilege';
export default {
  name: 'PrivilegeForm',
  components: {},
  // 类型禁用
  props: {
    typeDisabled: {
      type: Boolean,
      default: true,
      require: false
    },
    // 是否显示
    show: {
      type: Boolean,
      default: false,
      require: true
    },
    // 标题
    title: {
      type: String,
      require: true
    },
    // 权限
    privilege: {
      type: Object,
      require: true
    }
  },
  data() {
    return {
      scope: 1, // 权限划分 1管理端权限 2web端权限 ,
      urlList: [],
      privilegeNameTitle: '菜单名称',
      urlArray: []
    };
  },
  computed: {},
  watch: {
    privilege(val) {
      if (val) {
        this.urlArray = val.url.split(',');
      }
    }
  },
  filters: {},
  created() {},
  mounted() {
    this.getAllUrl();
  },
  beforeCreate() {},
  beforeMount() {},
  beforeUpdate() {},
  updated() {},
  beforeDestroy() {},
  destroyed() {},
  activated() {},
  methods: {
    // 获取所有请求路径
    async getAllUrl() {
      this.$Spin.show();
      let result = await privilegeApi.getAllUrl(this.scope);
      this.$Spin.hide();
      let key = 1;
      let datas = result.data;
      let list = [];
      let keys = [];
      datas.map(item => {
        let type = item.name.split('.')[0];
        let index = keys.indexOf(type);
        if (index < 0) {
          keys.push(type);
          list.push({
            label: type,
            data: [item]
          });
        } else {
          list[index].data.push(item);
        }
      });
      this.urlList = list;
    },
    //保存当前弹窗
    cancel() {
      this.$emit('closeModal');
    },
    // 提交数据
    submitForm() {
      let params = Object.assign({}, this.privilege);
      if (this.urlArray.length === 0) {
        this.$Message.error('请选择Url!');
        return;
      }
      params.url = this.urlArray.join(',');
      this.addOrUpdate(params);
    },
    // 保存更新功能点
    async addOrUpdate(prams) {
      this.$Spin.show();
      let result = await privilegeApi.addOrUpdate(prams);
      this.$Message.success('修改成功');
      this.$Spin.hide();
      this.$emit('updateMenuSuccess', prams.menuKey);
    }
  }
};
</script>
