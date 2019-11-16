<template>
  <div class="funnel-main"
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
  mounted() {
    this.initChart();
  },
  methods: {
    resize() {
      this.dom.resize();
    },
    initChart() {
      this.$nextTick(() => {
        let legend = this.value.map(_ => _.name);
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
            show: false,
            trigger: 'item',
            formatter: '{c} ({d}%)',
            // position: ['30%', '90%'],
            position: 'right',
            backgroundColor: 'transparent',
            textStyle: {
              fontSize: 14,
              color: '#666'
            }
          },
          legend: {
            orient: 'vertical',
            left: 'right',
            bottom: 0,

            // data: legend,
            backgroundColor: 'transparent',
            icon: 'circle'
          },
          series: [
            {
              name: '访问来源',
              type: 'funnel',
              radius: ['50%', '65%'],
              avoidLabelOverlap: false,
              label: {
                normal: {
                  show: false,
                  position: 'right',
                  formatter: '{c} ({d}%)'
                }
              },
              //   labelLine: {
              //     normal: {
              //       show: false
              //     }
              //   },
              data: [
                { value: 400, name: '交易完成' },
                { value: 300, name: '支付订单' },
                { value: 200, name: '生成订单' },
                { value: 100, name: '放入购物车' },
                { value: 100, name: '浏览网站' }
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
.funnel-main {
  width: 100%;
  height: 295px;
  padding: 28px;
  background: #fff;
}
</style>
