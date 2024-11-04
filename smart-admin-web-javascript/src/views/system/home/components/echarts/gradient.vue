<template>
  <default-home-card icon="BarChartOutlined" title="代码提交量">
    <div class="echarts-box">
      <div class="gradient-main" id="gradient-main"></div>
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
      color: ['#80FFA5', '#00DDFF', '#37A2FF', '#FF0087', '#FFBF00'],
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'cross',
          label: {
            backgroundColor: '#6a7985',
          },
        },
      },
      legend: {
        data: ['罗伊', '佩弦', '开云', '清野', '飞叶'],
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true,
      },
      xAxis: [
        {
          type: 'category',
          boundaryGap: false,
          data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
        },
      ],
      yAxis: [
        {
          type: 'value',
        },
      ],
      series: [
        {
          name: '罗伊',
          type: 'line',
          stack: 'Total',
          smooth: true,
          lineStyle: {
            width: 0,
          },
          showSymbol: false,
          areaStyle: {
            opacity: 0.8,
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              {
                offset: 0,
                color: 'rgb(128, 255, 165)',
              },
              {
                offset: 1,
                color: 'rgb(1, 191, 236)',
              },
            ]),
          },
          emphasis: {
            focus: 'series',
          },
          data: [140, 232, 101, 264, 90, 340, 250],
        },
        {
          name: '佩弦',
          type: 'line',
          stack: 'Total',
          smooth: true,
          lineStyle: {
            width: 0,
          },
          showSymbol: false,
          areaStyle: {
            opacity: 0.8,
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              {
                offset: 0,
                color: 'rgb(0, 221, 255)',
              },
              {
                offset: 1,
                color: 'rgb(77, 119, 255)',
              },
            ]),
          },
          emphasis: {
            focus: 'series',
          },
          data: [120, 282, 111, 234, 220, 340, 310],
        },
        {
          name: '开云',
          type: 'line',
          stack: 'Total',
          smooth: true,
          lineStyle: {
            width: 0,
          },
          showSymbol: false,
          areaStyle: {
            opacity: 0.8,
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              {
                offset: 0,
                color: 'rgb(55, 162, 255)',
              },
              {
                offset: 1,
                color: 'rgb(116, 21, 219)',
              },
            ]),
          },
          emphasis: {
            focus: 'series',
          },
          data: [320, 132, 201, 334, 190, 130, 220],
        },
        {
          name: '清野',
          type: 'line',
          stack: 'Total',
          smooth: true,
          lineStyle: {
            width: 0,
          },
          showSymbol: false,
          areaStyle: {
            opacity: 0.8,
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              {
                offset: 0,
                color: 'rgb(255, 0, 135)',
              },
              {
                offset: 1,
                color: 'rgb(135, 0, 157)',
              },
            ]),
          },
          emphasis: {
            focus: 'series',
          },
          data: [220, 402, 231, 134, 190, 230, 120],
        },
        {
          name: '飞叶',
          type: 'line',
          stack: 'Total',
          smooth: true,
          lineStyle: {
            width: 0,
          },
          showSymbol: false,
          label: {
            show: true,
            position: 'top',
          },
          areaStyle: {
            opacity: 0.8,
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              {
                offset: 0,
                color: 'rgb(255, 191, 0)',
              },
              {
                offset: 1,
                color: 'rgb(224, 62, 76)',
              },
            ]),
          },
          emphasis: {
            focus: 'series',
          },
          data: [220, 302, 181, 234, 210, 290, 150],
        },
      ],
    };
    let chartDom = document.getElementById('gradient-main');
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
    .gradient-main {
      width: 1200px;
      height: 300px;
      background: #fff;
    }
  }
</style>
