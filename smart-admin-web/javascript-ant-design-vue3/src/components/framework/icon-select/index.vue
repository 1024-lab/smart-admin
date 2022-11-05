<!--
  * 图标 选择 
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-09-01 23:14:49 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
-->
<template>
  <div>
    <a-popover v-model:visible="visible" placement="bottomLeft" trigger="click">
      <template #title>
        <a-form-item-rest>
          <a-radio-group @change="updateSelectIconArray" v-model:value="iconStyle" style="margin: 8px">
            <a-radio-button value="outlined">线框风格</a-radio-button>
            <a-radio-button value="filled">实底风格</a-radio-button>
            <a-radio-button value="twoTone">双色风格</a-radio-button>
          </a-radio-group>
        </a-form-item-rest>
        <a-form-item-rest>
          <a-input-search v-model:value="searchValue" placeholder="输入英文关键词进行搜索" @change="updateSelectIconArray" />
        </a-form-item-rest>
      </template>

      <template #content>
        <div class="icon-box">
          <div v-for="item in iconLoopArray" :key="item" @click="handleClick(item)" class="icon-content">
            <component :is="$antIcons[item]" />
          </div>
          <div v-show="showMoreIndex > 0">
            <a-button type="link" @click="showMore">点击展开更多图标（因图标较多，可能会卡一小会）</a-button>
          </div>
        </div>
      </template>
      <slot name="iconSelect"></slot>
    </a-popover>
  </div>
</template>

<script setup>
  import * as VueIcon from '@ant-design/icons-vue';
  import { computed, ref, watch } from 'vue';
  import _ from 'lodash';

  //线框风格图标数组
  const outlinedIconArray = Object.keys(VueIcon).filter((e) => _.endsWith(e.toLowerCase(), 'outlined'));
  //实底风格图标数组
  const filledIconArray = Object.keys(VueIcon).filter((e) => _.endsWith(e.toLowerCase(), 'filled'));
  //双色风格图标数组
  const twoToneIconArray = Object.keys(VueIcon).filter((e) => _.endsWith(e.toLowerCase(), 'twotone'));

  // ------------ 显示/隐藏 ------------
  const visible = ref(false);

  // ------------ 展开更多 ------------
  const SHOW_MORE_LENGTH = 35;
  const showMoreIndex = ref(SHOW_MORE_LENGTH);
  function showMore() {
    showMoreIndex.value = -1;
  }

  // ------------ 图标展示与搜索 ------------

  const iconStyle = ref('outlined');
  const selectIconArray = ref([...outlinedIconArray]);

  const iconLoopArray = computed(() => {
    return _.slice(selectIconArray.value, 0, showMoreIndex.value);
  });

  watch(iconStyle, (newValue, oldValue) => {
    updateSelectIconArray();
  });

  let searchValue = ref('');
  function updateSelectIconArray() {
    let tempArray = null;
    if (iconStyle.value === 'outlined') {
      tempArray = outlinedIconArray;
    } else if (iconStyle.value === 'filled') {
      tempArray = filledIconArray;
    } else {
      tempArray = twoToneIconArray;
    }
    if (!searchValue.value) {
      selectIconArray.value = tempArray;
    } else {
      selectIconArray.value = tempArray.filter((e) => e.toLowerCase().includes(searchValue.value.toLowerCase()));
    }

    if (selectIconArray.value.length > SHOW_MORE_LENGTH) {
      showMoreIndex.value = SHOW_MORE_LENGTH;
    }
  }

  // ------------ 对外抛出选择图标事件 ------------
  const emit = defineEmits(['updateIcon']);
  function handleClick(icon) {
    visible.value = false;
    emit('updateIcon', icon);
  }
</script>

<style scoped lang="less">
  .icon-box {
    overflow: auto;
    font-size: 20px;
    width: 410px;
    height: 300px;
    display: flex;
    flex-wrap: wrap;
    flex-direction: row;
    align-content: flex-start;
  }

  .icon-content {
    width: 45px;
    height: 40px;
    margin: 5px;
    cursor: pointer;
    text-align: center;
    border-radius: 6px;
    border: 1px solid #ccc;
    .more-icon {
      font-size: 14px;
      margin: 5px;
    }
  }
  .icon-content:hover {
    background: #1890ff;
  }
</style>
