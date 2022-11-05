<template>
  <default-home-card
      :extra="`${editFlag ? '完成' : '编辑'}`"
      icon="ThunderboltTwoTone"
      title="快捷入口"
      @extraClick="editFlag = !editFlag"
  >
    <div class="quick-entry-list">
      <a-row>
        <a-col v-for="(item,index) in quickEntry" :key="index" span="4">
          <div class="quick-entry" @click="turnToPage(item.path)">
            <div class="icon">
              <component :is='$antIcons[item.icon]' :style="{ fontSize:'30px'}"/>
              <close-circle-outlined v-if="editFlag" class="delete-icon" @click="deleteQuickEntry(index)"/>
            </div>
            <span class="entry-title">{{ item.title }}</span>
          </div>
        </a-col>
        <a-col v-if="editFlag && quickEntry.length < maxCount" span="4">
          <div class="add-quick-entry" @click="addHomeQuickEntry">
            <div class="add-icon">
              <plus-outlined :style="{ fontSize:'30px'}"/>
            </div>
          </div>
        </a-col>
      </a-row>
    </div>
  </default-home-card>
  <HomeQuickEntryModal ref="homeQuickEntryModal" @addQuickEntry="addQuickEntry"/>
</template>
<script setup>
import {onMounted, ref} from "vue";
import {router} from "/@/router";
import HomeQuickEntryModal from './home-quick-entry-modal.vue'
import localKey from '/@/constants/local-storage-key-const';
import {localRead, localSave} from '/@/utils/local-util';
import _ from "lodash";
import InitQuickEntryList from './init-quick-entry-list';
import DefaultHomeCard from "/@/views/system/home/components/default-home-card.vue";

//---------------- 初始化展示 --------------------
onMounted(() => {
  initQuickEntry();
})
let quickEntry = ref([])

function initQuickEntry() {
  let quickEntryJson = localRead(localKey.HOME_QUICK_ENTRY);
  if (!quickEntryJson) {
    quickEntry.value = _.cloneDeep(InitQuickEntryList);
    return;
  }
  let quickEntryList = JSON.parse(quickEntryJson);
  if (_.isEmpty(quickEntryList)) {
    quickEntry.value = _.cloneDeep(InitQuickEntryList);
    return;
  }
  quickEntry.value = quickEntryList;
}

// 页面跳转
function turnToPage(path) {
  if (editFlag.value) {
    return;
  }
  router.push({path});
}

//----------------  编辑快捷入口 --------------------
let editFlag = ref(false);
let maxCount = ref(6);

// 快捷入口删除
function deleteQuickEntry(index) {
  quickEntry.value.splice(index, 1)
  localSave(localKey.HOME_QUICK_ENTRY, JSON.stringify(quickEntry.value));
}

// 添加快捷入口
let homeQuickEntryModal = ref();

function addHomeQuickEntry() {
  homeQuickEntryModal.value.showModal();
}

function addQuickEntry(row) {
  quickEntry.value.push(row);
  localSave(localKey.HOME_QUICK_ENTRY, JSON.stringify(quickEntry.value));
}
</script>
<style lang='less' scoped>
.quick-entry-list {
  height: 100%;

  .quick-entry {
    padding: 10px 0;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    border-radius: 4px;

    .entry-title {
      margin-top: 5px;
    }

    .icon {
      position: relative;
    }

    &:hover {
      background-color: #F0FFFF;
    }

    .delete-icon {
      position: absolute;
      color: #F08080;
      top: -5px;
      right: -5px;
    }
  }

  .add-quick-entry {
    display: flex;
    align-items: center;
    justify-content: center;

    .add-icon {
      width: 70px;
      height: 70px;
      background-color: #fafafa;
      border: 1px dashed #d9d9d9;
      border-radius: 2px;
      cursor: pointer;
      transition: border-color .3s;
      display: flex;
      align-items: center;
      justify-content: center;
      color: #A9A9A9;

      &:hover {
        border-color: @primary-color;
        color: @primary-color;
      }
    }
  }
}
</style>
