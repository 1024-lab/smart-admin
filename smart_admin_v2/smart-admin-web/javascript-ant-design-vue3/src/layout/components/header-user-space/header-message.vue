<!--
  * 消息通知
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-09-06 20:17:18 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
-->

<template>
  <a-dropdown trigger="click" v-model:visible="show">
    <div @click="fetchMessage">
      <a-badge count="12">
        <div style="width: 26px; height: 26px">
          <BellOutlined :style="{ fontSize: '18px' }" />
        </div>
      </a-badge>
    </div>

    <template #overlay>
      <div>
        <a-spin :spinning="loading">
          <a-tabs class="dropdown-tabs" centered :tabBarStyle="{ textAlign: 'center' }" style="width: 300px">
            <a-tab-pane tab="通知" key="1">
              <a-list class="tab-pane" size="small">
                <a-list-item>
                  <a-list-item-meta description="7天前">
                    <template #title>
                      <a href="#">今天洛阳天气39°c，洛阳变各阳...</a>
                    </template>
                    <template #avatar>
                      <a-avatar src="https://zhuoda.vip/images/blog/profile-128-128.jpg" />
                    </template>
                  </a-list-item-meta>
                </a-list-item>
                <a-list-item>
                  <a-list-item-meta description="7天前">
                    <template #title>
                      <a href="#">六月的雨　就是无情的你...</a>
                    </template>
                    <template #avatar>
                      <a-avatar src="https://zhuoda.vip/images/blog/profile-128-128.jpg" />
                    </template>
                  </a-list-item-meta>
                </a-list-item>
                <a-list-item>
                  <a-list-item-meta description="7天前">
                    <template #title>
                      <a href="#">今年河南天气炎热，河南变可南...</a>
                    </template>
                    <template #avatar>
                      <a-avatar src="https://zhuoda.vip/images/blog/profile-128-128.jpg" />
                    </template>
                  </a-list-item-meta>
                </a-list-item>
              </a-list>
            </a-tab-pane>
            <a-tab-pane tab="消息" key="2">
              <a-list class="tab-pane" />
            </a-tab-pane>
            <a-tab-pane tab="待办" key="3">
              <a-list class="tab-pane" />
            </a-tab-pane>
          </a-tabs>
        </a-spin>
      </div>
    </template>
  </a-dropdown>
</template>

<script setup>
  import { ref } from 'vue';
  import { BellOutlined } from '@ant-design/icons-vue';

  defineExpose({ showMessage });

  function showMessage() {
    show.value = true;
  }

  const loading = ref(false);
  const show = ref(false);

  const fetchMessage = () => {
    if (loading.value) {
      loading.value = false;
      return;
    }
    loading.value = true;
    setTimeout(() => {
      loading.value = false;
    }, 700);
  };
</script>

<style lang="less">
  .header-notice {
    display: inline-block;
    transition: all 0.3s;

    span {
      vertical-align: initial;
    }

    .notice-badge {
      color: inherit;
    }
  }

  .dropdown-tabs {
    background-color: @base-bg-color;
    box-shadow: 0 2px 8px @shadow-color;
    border-radius: 4px;
  }
</style>
