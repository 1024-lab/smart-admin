<!--
  * 角色 数据范围
  *
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2022-09-12 22:34:00
  * @Wechat:    zhuda1024
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
  *
-->
<template>
  <div>
    <div class="btn-group">
      <a-button class="button-style" type="primary" @click="updateDataScope" v-privilege="'system:role:dataScope:update'"> 保存 </a-button>
      <a-button class="button-style" @click="getDataScope" v-privilege="'role:query'"> 刷新 </a-button>
    </div>
    <a-row class="header">
      <a-col class="tab-margin" :span="4">业务单据</a-col>
      <a-col class="tab-data" :span="8">查看数据范围</a-col>
      <a-col class="tab-margin" :span="12" />
    </a-row>
    <div class="data-container">
      <a-row class="data" align="middle" justify="center" v-for="(item, index) in dataScopeList" :key="item.dataScopeType">
        <a-col class="tab-margin" :span="4">
          {{ item.dataScopeTypeName }}
        </a-col>
        <a-col class="tab-data" :span="8">
          <a-radio-group v-model:value="selectedDataScopeList[index].viewType">
            <a-radio
              v-for="scope in item.viewTypeList"
              :key="`${item.dataScopeType}-${scope.viewType}`"
              class="radio-style"
              :value="scope.viewType"
              >{{ scope.viewTypeName }}</a-radio
            >
          </a-radio-group>
        </a-col>
        <a-col class="tab-margin tab-desc" :span="12">
          <p>{{ item.dataScopeTypeDesc }}</p>
        </a-col>
      </a-row>
    </div>
  </div>
</template>
<script setup>
  import { message } from 'ant-design-vue';
  import _ from 'lodash';
  import { inject, onMounted, ref, watch } from 'vue';
  import { roleApi } from '/@/api/system/role/role-api';
  import { smartSentry } from '/@/lib/smart-sentry';

  const props = defineProps({
    value: Number,
  });

  defineEmits(['update:value']);

  // ----------------------- 显示 ---------------------------------

  let selectRoleId = inject('selectRoleId');
  let dataScopeList = ref([]);
  let selectedDataScopeList = ref([]);

  watch(
    () => selectRoleId.value,
    () => getRoleDataScope()
  );

  onMounted(getDataScope);

  // 获取系统支持的所有种类的数据范围
  async function getDataScope() {
    let result = await roleApi.getDataScopeList();
    dataScopeList.value = result.data;

    selectedDataScopeList.value = [];

    dataScopeList.value.forEach((item) => {
      selectedDataScopeList.value.push({
        viewType: undefined,
        dataScopeType: item.dataScopeType,
      });
    });
    getRoleDataScope();
  }

  // 获取数据范围根据角色id，并赋予选中状态
  async function getRoleDataScope() {
    let result = await roleApi.getDataScopeByRoleId(selectRoleId.value);
    let data = result.data;
    selectedDataScopeList.value = [];

    dataScopeList.value.forEach((item) => {
      let find = data.find((e) => e.dataScopeType == item.dataScopeType);
      selectedDataScopeList.value.push({
        viewType: find ? find.viewType : undefined,
        dataScopeType: item.dataScopeType,
      });
    });
  }

  // ----------------------- 数据范围更新 ---------------------------------
  // 更新
  async function updateDataScope() {
    try {
      let data = {
        roleId: selectRoleId.value,
        dataScopeItemList: selectedDataScopeList.value.filter((e) => !_.isUndefined(e.viewType)),
      };
      await roleApi.updateRoleDataScopeList(data);
      message.success('保存成功');
      getDataScope();
    } catch (e) {
      smartSentry.captureError(e);
    }
  }
</script>
<style scoped lang="less">
  .btn-group {
    text-align: right;
  }

  .button-style {
    margin: 0 10px;
  }
  .header {
    border-bottom: 1px solid #f2f2f2;
    font-weight: 600;
    margin: 10px 0px;
  }
  .tab-data {
    margin: 10px 0px;
  }

  .data-container {
    height: 680px;
    overflow-y: scroll;
  }
  .data {
    border-bottom: 1px solid #f2f2f2;
    margin: 10px 0px;
  }
  .radio-style {
    display: block;
    height: 30px;
    line-height: 30px;
  }
  .tab-margin {
    text-align: center;
    margin: 10px 0px;
  }
  .tab-desc {
    line-height: 30px;
    font-size: 16px;
    text-align: left;
  }
</style>
