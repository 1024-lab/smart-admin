<!--
  * 已办/代办
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-09-12 22:34:00 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
  *
-->
<template>
  <default-home-card icon="StarTwoTone" title="已办待办">
    <div style="height: 280px;">
      <div class="center column">
        <a-space direction="vertical" style="width: 100%">
          <div v-for="(item, index) in toDoList" :key="index" :class="['to-do', { done: item.doneFlag }]">
            <a-checkbox v-model:checked="item.doneFlag">
              <span class="task">{{ item.title }}</span>
            </a-checkbox>
            <div class="star-icon" @click="itemStar(item)">
              <StarFilled v-if="item.starFlag" style="color: #ff8c00" />
              <StarOutlined v-else style="color: #c0c0c0" />
            </div>
          </div>
          <div v-for="(item, index) in doneList" :key="index" :class="['to-do', { done: item.doneFlag }]">
            <a-checkbox v-model:checked="item.doneFlag">
              <span class="task">{{ item.title }}</span>
            </a-checkbox>
            <div class="star-icon" @click="itemStar(item)">
              <StarFilled v-if="item.starFlag" style="color: #ff8c00" />
              <StarOutlined v-else style="color: #c0c0c0" />
            </div>
          </div>
        </a-space>
      </div>
    </div>
  </default-home-card>
</template>
<script setup lang="ts">
  import DefaultHomeCard from '/@/views/system/home/components/default-home-card.vue';
  import { computed, ref } from 'vue';
  import dayjs from 'dayjs';
  import { message } from 'ant-design-vue';

  let taskList = ref([
    {
      title: '周五下班前需要提交周报',
      doneFlag: true,
      starFlag: true,
      starTime: 0,
    },
    {
      title: '为SmartAdmin前端小组分配任务',
      doneFlag: false,
      starFlag: false,
      starTime: 0,
    },
    {
      title: '跟进团建内容事宜',
      doneFlag: false,
      starFlag: true,
      starTime: 0,
    },
    {
      title: '跟进客户定制一个软件平台',
      doneFlag: false,
      starFlag: false,
      starTime: 0,
    },
    {
      title: '下个版本的需求确认',
      doneFlag: false,
      starFlag: false,
      starTime: 0,
    },
    {
      title: '线上版本发布',
      doneFlag: true,
      starFlag: true,
      starTime: dayjs().unix(),
    },
    {
      title: '周一财务报销',
      doneFlag: true,
      starFlag: false,
      starTime: 0,
    },
  ]);

  let toDoList = computed(() => {
    return taskList.value.filter((e) => !e.doneFlag).sort((a, b) => b.starTime - a.starTime);
  });

  let doneList = computed(() => {
    return taskList.value.filter((e) => e.doneFlag);
  });

  function itemStar(item) {
    item.starFlag = !item.starFlag;
    if (item.starFlag) {
      item.starTime = dayjs().unix();
    }
  }

  //-------------------------任务新建-----------------------
  let taskTitle = ref('');
  function addTask() {
    if (!taskTitle.value) {
      message.warn('请输入任务标题');
      return;
    }
    let data = {
      title: taskTitle.value,
      doneFlag: false,
      starFlag: false,
      starTime: 0,
    };
    taskList.value.unshift(data);
    taskTitle.value = '';
  }
</script>
<style lang="less" scoped>
  .center {
    display: flex;
    justify-content: center;
    height: 100%;

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
</style>
