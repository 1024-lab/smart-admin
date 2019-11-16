<template>
  <div class="bar-main"
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
  mounted() {
    this.initChart();
  },
  methods: {
    resize() {
      this.dom.resize();
    },
    initChart() {
      this.$nextTick(() => {
        let xAxisData = Object.keys(this.value);
        let seriesData = Object.values(this.value);
        let option = {
          grid: {
            left: '1%',
            right: '1%',
            top: '2%',
            bottom: '1%',
            containLabel: true
          },
          title: {
            text: this.text,
            subtext: this.subtext,
            x: 'center'
          },
          tooltip: {
            trigger: 'item',
            formatter: '{c}人',
            // position: ['30%', '90%'],
            position: 'top',
            backgroundColor: '#FAFBFE',
            textStyle: {
              fontSize: 14,
              color: '#6d6d6d'
            }
          },
          xAxis: {
            // show: false,
            type: 'category',
            data: xAxisData,
            splitLine: {
              show: false
            }
          },
          yAxis: [
            {
              // show: false,
              type: 'value',
              splitLine: {
                show: true,
                lineStyle: {
                  // 设置刻度线粗度(粗的宽度)
                  width: 1,
                  // 颜色数组，数组数量要比刻度线数量大才能不循环使用
                  color: [
                    'rgba(0, 0, 0, 0)',
                    '#eee',
                    '#eee',
                    '#eee',
                    '#eee',
                    '#eee',
                    '#eee',
                    '#eee',
                    '#eee'
                  ]
                }
              }
            }
          ],
          series: [
            {
              data: seriesData,
              type: 'bar',
              barWidth: 36,
              areaStyle: {
                normal: {
                  color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    { offset: 0, color: '#f2f5ff' },
                    { offset: 1, color: '#fff' }
                  ])
                }
              },
              itemStyle: {
                normal: {
                  barBorderRadius: [50],
                  color: new echarts.graphic.LinearGradient(
                    0,
                    1,
                    0,
                    0,
                    [
                      {
                        offset: 0,
                        color: '#3AA1FF' // 0% 处的颜色
                      },
                      {
                        offset: 1,
                        color: '#36CBCB' // 100% 处的颜色
                      }
                    ],
                    false
                  )
                }
              }
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
.bar-main {
  width: 100%;
  height: 360px;
  padding: 28px;
  background: #fff;
}
</style>
