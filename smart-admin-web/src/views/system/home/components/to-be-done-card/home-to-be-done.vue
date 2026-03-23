<!--
  * 待办工作
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-09-12 22:34:00
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
  *
-->
<template>
  <default-home-card extra="添加" icon="StarTwoTone" title="待办工作" @extraClick="showAddToBeDone">
    <div style="height: 280px">
      <div class="center column">
        <a-space direction="vertical" style="width: 100%">
          <a-empty v-if="$lodash.isEmpty(toBeDoneList)" description="暂无待办工作" />
          <div v-for="(item, index) in toDoList" :key="index" :class="['to-do', { done: item.doneFlag }]">
            <a-checkbox v-model:checked="item.doneFlag" @change="handleCheckbox">
              <span class="task">{{ item.title }}</span>
            </a-checkbox>
            <div v-if="!item.doneFlag" class="star-icon" @click="itemStar(item)">
              <StarFilled v-if="item.starFlag" style="color: #ff8c00" />
              <StarOutlined v-else style="color: #c0c0c0" />
            </div>
            <close-circle-outlined class="delete-icon" @click="toDelete(item)" />
          </div>
          <div v-for="(item, index) in doneList" :key="index" :class="['to-do', { done: item.doneFlag }]">
            <a-checkbox v-model:checked="item.doneFlag" @change="handleCheckbox">
              <span class="task">{{ item.title }}</span>
            </a-checkbox>
            <div v-if="!item.doneFlag" class="star-icon" @click="itemStar(item)">
              <StarFilled v-if="item.starFlag" style="color: #ff8c00" />
              <StarOutlined v-else style="color: #c0c0c0" />
            </div>
            <close-circle-outlined class="delete-icon" @click="toDelete(item)" />
          </div>
        </a-space>
      </div>
    </div>
  </default-home-card>
  <ToBeDoneModal ref="toBeDoneModalRef" @addToBeDone="addToBeDone" />
</template>
<script setup>
  import DefaultHomeCard from '/@/views/system/home/components/default-home-card.vue';
  import ToBeDoneModal from './to-be-done-modal.vue';
  import localKey from '/@/constants/local-storage-key-const';
  import { localRead, localSave } from '/@/utils/local-util';
  import { useUserStore } from '/@/store/modules/system/user.js';
  import { computed, ref, onMounted } from 'vue';
  import { Modal } from 'ant-design-vue';

  let toBeDoneList = ref([]);

  onMounted(() => {
    initTaskList();
  });

  function initTaskList() {
    let localTaskList = localRead(localKey.TO_BE_DONE);
    if (localTaskList) {
      toBeDoneList.value = JSON.parse(localTaskList);
    }
  }

  let toDoList = computed(() => {
    return toBeDoneList.value.filter((e) => !e.doneFlag);
  });

  let doneList = computed(() => {
    return toBeDoneList.value.filter((e) => e.doneFlag);
  });

  function handleCheckbox(e) {
    localSave(localKey.TO_BE_DONE, JSON.stringify(toBeDoneList.value));
    useUserStore().toBeDoneCount = toDoList.value.length;
  }

  function itemStar(data) {
    data.starFlag = !data.starFlag;
    // 将取消 star 的删除掉
    const index = toBeDoneList.value.findIndex((item) => item.title === data.title);
    toBeDoneList.value.splice(index, 1);
    if (data.starFlag) {
      // 最新添加标记star的移动到第一位
      toBeDoneList.value.unshift(data);
    } else {
      // 取消标记star的移动到最后一个标记 star 的后面添加
      const lastStarIndex = toBeDoneList.value.findLastIndex((item) => item.starFlag);
      toBeDoneList.value.splice(lastStarIndex + 1, 0, data);
    }
    localSave(localKey.TO_BE_DONE, JSON.stringify(toBeDoneList.value));
  }

  //-------------------------任务新建-----------------------

  let toBeDoneModalRef = ref();

  function showAddToBeDone() {
    toBeDoneModalRef.value.showModal();
  }

  // 添加待办工作
  function addToBeDone(data) {
    toBeDoneList.value.push(data);
    localSave(localKey.TO_BE_DONE, JSON.stringify(toBeDoneList.value));
    useUserStore().toBeDoneCount = toDoList.value.length;
  }

  function toDelete(data) {
    if (!data.doneFlag) {
      Modal.confirm({
        title: '提示',
        content: '确定要删除吗?',
        okText: '删除',
        okType: 'danger',
        onOk() {
          deleteToBeDone(data);
        },
        cancelText: '取消',
        onCancel() {},
      });
    } else {
      deleteToBeDone(data);
    }
  }

  // 删除待办工作
  function deleteToBeDone(data) {
    const index = toBeDoneList.value.findIndex((item) => item.title === data.title);
    toBeDoneList.value.splice(index, 1);
    localSave(localKey.TO_BE_DONE, JSON.stringify(toBeDoneList.value));
    useUserStore().toBeDoneCount = toDoList.value.length;
  }
</script>
<style lang="less" scoped>
  .center {
    display: flex;
    justify-content: center;
    height: 100%;
    overflow-y: auto;

    &.column {
      flex-direction: column;
      width: 100%;
      padding: 0 10px;
      justify-content: flex-start;
    }
  }

  .to-do {
    width: 100%;
    border: 1px solid #d3d3d3;
    border-radius: 4px;
    padding: 4px;
    display: flex;
    align-items: center;
    .star-icon {
      margin-left: auto;
      cursor: pointer;
    }

    &.done {
      text-decoration: line-through;
      color: #8c8c8c;

      .task {
        color: #8c8c8c;
      }
    }
  }

  .delete-icon {
    color: #f08080;
    padding-left: 10px;
    top: -5px;
    right: -5px;
    float: right;
  }
</style>
