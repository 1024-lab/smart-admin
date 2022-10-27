<!--
  * 部门 员工 弹窗
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-08-08 20:46:18
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
-->
<template>
  <a-modal v-model:visible="visible" title="调整部门" :footer="null" destroyOnClose>
    <DepartmentTree ref="departmentTree" :height="400" :showMenu="false" />
    <div class="footer">
      <a-button style="margin-right: 8px" @click="closeModal">取消</a-button>
      <a-button type="primary" @click="handleOk">提交</a-button>
    </div>
  </a-modal>
</template>
<script setup lang="ts">
  import { message } from 'ant-design-vue';
import _ from 'lodash';
import { ref } from 'vue';
import DepartmentTree from '../department-tree/index.vue';
import { employeeApi } from '/@/api/system/employee/employee-api';
import { smartSentry } from '/@/lib/smart-sentry';
import { SmartLoading } from '/@/components/framework/smart-loading';

  // ----------------------- 以下是字段定义 emits props ---------------------

  const emit = defineEmits(['refresh']);

  // ----------------------- 显示/隐藏 ------------------------

  const departmentTree = ref();
  const visible = ref(false);
  const employeeIdList = ref([]);

  //显示
  async function showModal(selectEmployeeId) {
    employeeIdList.value = selectEmployeeId;
    visible.value = true;
  }

  //隐藏
  function closeModal() {
    visible.value = false;
  }

  // ----------------------- form操作 ---------------------------------
  async function handleOk() {
    SmartLoading.show();
    try {
      if (_.isEmpty(employeeIdList.value)) {
        message.warning('请选择要调整的员工');
        return;
      }
      if (_.isEmpty(departmentTree.value.selectedKeys)) {
        message.warning('请选择要调整的部门');
        return;
      }
      let departmentId = departmentTree.value.selectedKeys[0];
      let params = {
        employeeIdList: employeeIdList.value,
        departmentId: departmentId,
      };
      await employeeApi.batchUpdateDepartmentEmployee(params);
      message.success('操作成功');
      emit('refresh');
      closeModal();
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      SmartLoading.hide();
    }
  }

  // ----------------------- 以下是暴露的方法内容 ----------------------------
  defineExpose({
    showModal,
  });
</script>
<style scoped lang="less">
  .footer {
    position: absolute;
    right: 0;
    bottom: 0;
    width: 100%;
    border-top: 1px solid #e9e9e9;
    padding: 10px 16px;
    background: #fff;
    text-align: right;
    z-index: 1;
  }
</style>
