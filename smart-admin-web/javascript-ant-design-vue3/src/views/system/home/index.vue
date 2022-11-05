<!--
  * 首页
  * 
  * @Author:    1024创新实验室-主任：卓大 
  * @Date:      2022-09-12 22:34:00 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
  *
-->
<template>
  <!--  顶部用户信息-->
  <a-row>
    <HomeHeader />
  </a-row>
  <!--下方左右布局-->
  <a-row :gutter="[10, 10]">
    <!--左侧-->
    <a-col :span="16">
      <a-row :gutter="[10, 10]">
        <!--公告信息-->
        <a-col :span="12">
          <HomeNotice title="公告" :noticeTypeId="1" />
        </a-col>
        <!--企业动态-->
        <a-col :span="12">
          <HomeNotice title="通知" :noticeTypeId="2" />
        </a-col>
        <!--各类报表-->
        <!-- <a-col :span="6">
          <Gauge :percent="saleTargetPercent" />
        </a-col> -->
        <a-col :span="12">
          <Pie />
        </a-col>
        <a-col :span="12">
          <Category />
        </a-col>
        <a-col :span="24">
          <Gradient />
        </a-col>
      </a-row>
    </a-col>
    <!--右侧-->
    <a-col :span="8">
      <a-row :gutter="[10, 10]">
        <!--快捷入口-->
        <!-- <a-col :span="24">
          <HomeQuickEntry />
        </a-col> -->
        <!--关注公众号-->
        <a-col :span="24">
          <OfficialAccountCard />
        </a-col>
        <!--待办、已办-->
        <a-col :span="24">
          <ToBeDoneCard />
        </a-col>
        <!--更新日志-->
        <a-col :span="24">
          <ChangelogCard />
        </a-col>
      </a-row>
    </a-col>
  </a-row>
</template>
<script setup>
  import { computed } from 'vue';
  import HomeHeader from './home-header.vue';
  import HomeNotice from './home-notice.vue';
  import HomeQuickEntry from './components/quick-entry/home-quick-entry.vue';
  import OfficialAccountCard from './components/official-account-card.vue';
  import ToBeDoneCard from './components/to-be-done-card.vue';
  import ChangelogCard from './components/changelog-card.vue';
  import Gauge from './components/echarts/gauge.vue';
  import Category from './components/echarts/category.vue';
  import Pie from './components/echarts/pie.vue';
  import Gradient from './components/echarts/gradient.vue';
  import { Modal } from 'ant-design-vue';

  // 业绩完成百分比
  const saleTargetPercent = computed(() => {
    return 75;
  });

</script>
<style lang="less" scoped>
  @import './index.less';
</style>
