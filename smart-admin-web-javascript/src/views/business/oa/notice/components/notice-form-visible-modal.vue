<!--
  * 通知  可见范围
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-08-21 19:52:43 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
-->
<template>
  <a-modal title="选择部门" v-model:open="visibleFlag" :maskClosable="false" :width="768" @ok="onSubmit" @cancel="onClose">
    <a-tabs v-model:activeKey="activeKey">
      <a-tab-pane :key="1" tab="选择员工">
        <NoticeFormVisibleTransferEmployee :employeeList="employeeList" @onChange="onChangeEmployee" />
      </a-tab-pane>
      <a-tab-pane :key="2" tab="选择部门">
        <NoticeFormVisibleTransferDepartment :departmentList="departmentList" @onChange="onChangeDepartment" />
      </a-tab-pane>
    </a-tabs>
  </a-modal>
</template>

<script setup>
import { reactive, ref, onMounted, watch } from 'vue';
import { NOTICE_VISIBLE_RANGE_DATA_TYPE_ENUM } from '/@/constants/business/oa/notice-const';
import NoticeFormVisibleTransferDepartment from './notice-form-visible-transfer-department.vue';
import NoticeFormVisibleTransferEmployee from './notice-form-visible-transfer-employee.vue';

const emits = defineEmits('selectedFinish');
const visibleFlag = ref(false);
function onClose() {
  visibleFlag.value = false;
}

const activeKey = ref(1);
// 已选的员工列表
const employeeList = ref([]);
// 已选的部门列表
const departmentList = ref([]);

// 显示弹窗
function showModal(visibleRangeList = []) {
  employeeList.value = visibleRangeList.filter((item) => item.dataType === NOTICE_VISIBLE_RANGE_DATA_TYPE_ENUM.EMPLOYEE.value);
  departmentList.value = visibleRangeList.filter((item) => item.dataType === NOTICE_VISIBLE_RANGE_DATA_TYPE_ENUM.DEPARTMENT.value);
  activeKey.value = 1;
  visibleFlag.value = true;
}

function onSubmit() {
  const selectedList = [...employeeList.value, ...departmentList.value];
  emits('selectedFinish', selectedList);
  onClose();
}

// 选择员工改变
function onChangeEmployee({ selectedList }) {
  employeeList.value = selectedList;
}

// 选择部门改变
function onChangeDepartment({ selectedList }) {
  departmentList.value = selectedList;
}

// ----------------------- 以下是暴露的方法内容 ------------------------
defineExpose({
  showModal,
});
</script>

<style lang="less" scoped></style>
