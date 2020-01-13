<template>
  <div class="gauge-main"
       id="box"
       ref="dom"></div>
</template>

<script>
import echarts from 'echarts';
import tdTheme from './theme.json';
import { on, off } from '@/lib/util';
echarts.registerTheme('tdTheme', tdTheme);
export default {
  props: {
    value: Object,
    text: String,
    subtext: String
  },
  mounted () {
    this.initChart();
  },
  methods: {
    resize () {
      this.dom.resize();
    },
    initChart () {
      this.$nextTick(() => {
        let option = {
          grid: {
            left: 0,
            right: 0,
            top: 0,
            bottom: 0
            // containLabel: true
          },
          tooltip: {
            formatter: '{a} <br/>{b} : {c}%'
          },
          toolbox: {},
          series: [
            {
              name: '业务指标',
              startAngle: 195,
              endAngle: -15,
              axisLine: {
                show: true,
                lineStyle: {
                  color: [[0.6, '#4ECB73'], [0.8, '#FBD437'], [1, '#F47F92']],
                  width: 16
                }
              },
              pointer: {
                length: '80%',
                width: 3,
                color: 'auto'
              },
              axisTick: {
                show: false
              },
              splitLine: { show: false },
              type: 'gauge',
              detail: {
                formatter: '{value}%',
                textStyle: {
                  color: '#595959',
                  fontSize: 32
                }
              },
              data: [{ value: 10 }]
            }
          ]
        };
        this.dom = echarts.init(this.$refs.dom, 'tdTheme');
        this.dom.setOption(option);
        on(window, 'resize', this.resize);
      });
    }
  }
};
</script>

<style>
.gauge-main {
  width: 100%;
  height: 360px;
  background: #fff;
}
</style>
