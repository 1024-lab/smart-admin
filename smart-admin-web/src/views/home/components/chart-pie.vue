<template>
  <div class="pie-main"
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
    value: Array,
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
        let legend = this.value.map(_ => _.name);
        let option = {
          title: {
            text: this.text,
            subtext: this.subtext,
            x: 'center'
          },
          position: {
            top: 40
          },
          tooltip: {
            trigger: 'item',
            formatter: '{c} ({d}%)',
            // position: ['30%', '90%'],
            position: function (point, params, dom, rect, size) {
              console.log(size);
              let leftWidth = size.viewSize[0] / 2 - size.contentSize[0] / 2;
              console.log(leftWidth);
              return { left: leftWidth, bottom: 0 };
            },
            backgroundColor: 'transparent',
            textStyle: {
              fontSize: 24,
              color: '#666'
            }
          },
          legend: {
            // orient: 'vertical',
            top: 0,
            data: legend,
            backgroundColor: 'transparent',
            icon: 'circle'
          },
          series: [
            {
              name: '访问来源',
              type: 'pie',
              radius: ['45%', '60%'],
              center: ['50%', '52%'],
              avoidLabelOverlap: false,
              label: {
                normal: {
                  show: false,
                  position: 'center'
                },
                emphasis: {
                  show: true,
                  textStyle: {
                    fontSize: '24'
                  }
                }
              },
              labelLine: {
                normal: {
                  show: false
                }
              },
              data: [
                { value: 335, name: '直接访问' },
                { value: 310, name: '邮件营销' },
                { value: 234, name: '联盟广告' },
                { value: 135, name: '视频广告' },
                { value: 1548, name: '搜索引擎' }
              ]
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
.pie-main {
  width: 100%;
  height: 360px;
  padding: 28px;
  background: #fff;
}
</style>
