<template>
  <!--div tab切换，数据范围部分 start-->
  <div>
    <Row>
      <Col span="16" style="margin:20px 0;font-size: 15px;color: #95a5a6;"></Col>
      <Col class="button-style" span="8">
        <Button
          @click.native="updateDataScope"
          style="margin-right: 20px;"
          type="primary"
          v-privilege="'update-data-scope'"
        >保存</Button>
        <Button @click.native="getDataScope()" type="warning" v-privilege="'query-data-scope'">刷新</Button>
      </Col>
    </Row>
    <Row style="border-bottom: 1px solid #f2f2f2;font-weight: 600; margin: 10px 0px;">
      <Col class="tab-margin" span="4">业务单据</Col>
      <Col class="tab-data" span="6">查看数据范围</Col>
      <Col class="tab-margin" span="14"></Col>
    </Row>
    <div class="no-scrollbar" style="height:680px;overflow-y: scroll">
      <Row
        :key="item.dataScopeType "
        style="border-bottom: 1px solid #f2f2f2; margin: 10px 0px;"
        v-for="item in this.dataScopeList"
      >
        <Col span="4" style="line-height:100px; text-align: center">{{item.dataScopeTypeName}}</Col>
        <!--Col 数据范围选中 start-->
        <Col class="tab-data" span="6">
          <RadioGroup v-model="item.viewType" vertical>
            <Radio
              :key="index+'viewType'"
              :label="scope.viewType"
              v-for="(scope,index) in item.viewTypeList"
            >{{scope.viewTypeName }}</Radio>
          </RadioGroup>
        </Col>
        <!--Col 数据范围选中 end-->
        <Col span="14" style="text-indent:2rem;line-height:30px;font-size:16px;color: #a3a3a3;">
          <p style="padding: 30px 0;">{{item.dataScopeTypeDesc}}</p>
        </Col>
      </Row>
    </div>
  </div>
  <!--div tab切换，数据范围部分 end-->
</template>

<script>
import { dataScopeApi } from '@/api/data-scope';
export default {
  name: 'RoleDataScope',
  components: {},
  props: {
    // 角色id
    roleId: {
      type: Number,
      required: true,
      validator: value => {
        return value >= 0;
      }
    }
  },
  data() {
    return {
      dataScopeList: {}
    };
  },
  computed: {},
  watch: {},
  filters: {},
  created() {},
  mounted() {
    this.getDataScope();
  },
  beforeCreate() {},
  beforeMount() {},
  beforeUpdate() {},
  updated() {},
  beforeDestroy() {},
  destroyed() {},
  activated() {},
  methods: {
    // 更新
    async updateDataScope() {
      try {
        let data = {
          roleId: this.roleId,
          batchSetList: this.dataScopeList
        };
        await dataScopeApi.updateDataScope(data);
        this.$Message.success('保存成功');
        this.getDataScope(this.employeeId);
      } catch (e) {
        //TODO zhuoda sentry
        console.error(e);
      }
    },
    // 获取数据
    async getDataScope() {
      try {
        let result = await dataScopeApi.getDataScopeList();
        this.dataScopeList = result.data;
        this.getRoleDataScope();
      } catch (e) {
        //TODO zhuoda sentry
        console.error(e);
      }
    },
    // 获取数据范围根据角色id
    async getRoleDataScope() {
      try {
        let result = await dataScopeApi.getDataScopeByRoleId(this.roleId);
        let data = result.data;
        this.dataScopeList.forEach((item, i) => {
          let find = data.find(e => e.dataScopeType == item.dataScopeType);
          if (find) {
            this.$set(item, 'viewType', find.viewType);
          } else {
            this.$set(item, 'viewType', '');
          }
        });
      } catch (e) {
        //TODO zhuoda sentry
        console.error(e);
      }
    }
  }
};
</script>
<style lang="less" scoped>
.button-style {
  margin: 20px 0 20px 0;
  padding-left: 20px;
  text-align: right;
}
.tab-data {
  padding-left: 30px;
  margin: 10px 0px;
}
.tab-margin {
  text-align: center;
  margin: 10px 0px;
}
</style>
