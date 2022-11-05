<!--
 * @Author: zhuoda
 * @Date: 2021-08-24 16:35:45
 * @LastEditTime: 2022-06-11
 * @LastEditors: zhuoda
 * @Description:
 * @FilePath: /smart-admin/src/views/system/home/components/gauge.vue
-->
<template>
  <default-home-card icon="RocketTwoTone" title="业绩完成度">
    <div class="echarts-box">
      <div id="gauge-main" class="gauge-main"></div>
    </div>
  </default-home-card>
</template>
<script setup>
import DefaultHomeCard from "/@/views/system/home/components/default-home-card.vue";
import * as echarts from "echarts";
import {onMounted, watch} from "vue";
import {reactive} from "vue";

const props = defineProps({
  percent: {
    type: Number,
    default: 0
  },
});

let option = reactive({});
watch(
    () => props.percent,
    () => {
      init();
    }
);
onMounted(() => {
  init();
});

function init() {
  option = {
    series: [
      {
        type: "gauge",
        startAngle: 90,
        endAngle: -270,
        pointer: {
          show: false,
        },
        progress: {
          show: true,
          overlap: false,
          roundCap: true,
          clip: false,
          itemStyle: {
            borderWidth: 1,
            borderColor: "#464646",
          },
        },
        axisLine: {
          lineStyle: {
            width: 20,
          },
        },
        splitLine: {
          show: false,
          distance: 0,
          length: 10,
        },
        axisTick: {
          show: false,
        },
        axisLabel: {
          show: false,
          distance: 50,
        },
        data: [
          {
            value: props.percent,
            name: "完成度",
            title: {
              offsetCenter: ["0%", "-10%"],
            },
            detail: {
              offsetCenter: ["0%", "20%"],
            },
          },
        ],
        title: {
          fontSize: 18,
        },
        detail: {
          fontSize: 16,
          color: "auto",
          formatter: "{value}%",
        },
      },
    ],
  };
  let chartDom = document.getElementById("gauge-main");
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
  .gauge-main {
    width: 260px;
    height: 260px;
    background: #fff;
  }
}
</style>
