<!--
  * 更新日志
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-09-12 22:34:00 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
  *
-->
<template>
  <default-home-card extra="更多" icon="FireTwoTone" title="更新日志" @extraClick="onMore">
    <a-empty v-if="$lodash.isEmpty(data)" />
    <ul v-else>
      <template v-for="(item, index) in data" :key="index">
        <li class="un-read">
          <a class="content" @click="goDetail(item)">
            <a-badge status="geekblue" />
            {{ $smartEnumPlugin.getDescByValue('CHANGE_LOG_TYPE_ENUM', item.type) }}：{{ item.version }} 版本
          </a>
          <span class="time"> {{ item.publicDate }}</span>
        </li>
      </template>
    </ul>
  </default-home-card>

  <ChangeLogForm ref="modalRef" />
</template>
<script setup>
  import { onMounted, ref } from 'vue';
  import { useRouter } from 'vue-router';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { changeLogApi } from '/@/api/support/change-log/change-log-api';
  import DefaultHomeCard from '/@/views/system/home/components/default-home-card.vue';
  import ChangeLogForm from '/@/views/support/change-log/change-log-modal.vue';

  const router = useRouter();

  const queryForm = {
    pageNum: 1,
    pageSize: 8,
    searchCount: false,
  };

  let data = ref([]);

  const loading = ref(false);
  // 查询列表
  async function queryChangeLog() {
    loading.value = true;
    try {
      let queryResult = await changeLogApi.queryPage(queryForm);
      data.value = queryResult.data.list;
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      loading.value = false;
    }
  }

  onMounted(queryChangeLog);

  // 查看更多
  function onMore() {
    router.push({
      path: '/support/change-log/change-log-list',
    });
  }

  // 进入详情
  const modalRef = ref();
  function goDetail(data) {
    modalRef.value.show(data);
  }
</script>
<style lang="less" scoped>
  ul li {
    margin-bottom: 8px;
    display: flex;
    justify-content: space-between;

    .content {
      white-space: nowrap;
      text-overflow: ellipsis;
      overflow: hidden;
      word-break: break-all;
    }

    .time {
      flex-shrink: 0;
      color: @text-color-secondary;
      min-width: 75px;
    }
  }

  ul li :hover {
    color: @primary-color;
  }

  .un-read a {
    color: @text-color;
  }

  .read a {
    color: @text-color-secondary;
  }
</style>
