<template>
  <default-home-card icon="Profile" title="销量统计">
    <div class="echarts-box">
      <div class="category-main" id="category-main"></div>
    </div>
  </default-home-card>
</template>
<script setup>
  import DefaultHomeCard from '/@/views/system/home/components/default-home-card.vue';
  import * as echarts from 'echarts';
  import { onMounted } from 'vue';

  onMounted(() => {
    init();
  });

  function init() {
    let option = {
      xAxis: {
        type: 'category',
        data: ['周一', '周二', '周三', '周四', '周五'],
      },
      yAxis: {
        type: 'value',
      },
      legend: {},
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true,
      },
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow',
        },
      },
      series: [
        {
          name: '善逸',
          data: [120, 200, 150, 80, 70, 110, 130],
          type: 'bar',
          backgroundStyle: {
            color: 'rgba(180, 180, 180, 0.2)',
          },
        },
        {
          name: '胡克',
          data: [100, 80, 120, 77, 52, 22, 190],
          type: 'bar',
          backgroundStyle: {
            color: 'rgba(180, 180, 180, 0.2)',
          },
        },
        {
          name: '开云',
          data: [200, 110, 85, 99, 120, 145, 180],
          type: 'bar',
          backgroundStyle: {
            color: 'rgba(180, 180, 180, 0.2)',
          },
        },
        {
          name: '初晓',
          data: [80, 70, 90, 110, 200, 44, 80],
          type: 'bar',
          backgroundStyle: {
            color: 'rgba(180, 180, 180, 0.2)',
          },
        },
      ],
    };
    let chartDom = document.getElementById('category-main');
    if (chartDom) {
      let myChart = echarts.init(chartDom);
      option && myChart.setOption(option);
    }
  }
</script>
<style lang="less" scoped>
  .echarts-box {
    display: flex;
    align-items: center;
    justify-content: center;
    .category-main {
      width: 800px;
      height: 280px;
      background: #fff;
    }
  }
</style>
