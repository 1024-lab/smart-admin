<template>
  <div ref="dom" class="charts chart-bar"></div>
</template>

<script>
import echarts from 'echarts';
import tdTheme from './theme.json';
import { on, off } from '@/lib/util';
echarts.registerTheme('tdTheme', tdTheme);
export default {
  // 柱状图
  name: 'chartBar',
  props: {
    // map类型 keys 为图表x轴 values 为图表y轴数据
    value: {
      type: Object,
      require: true
    },
    // 标题
    text: {
      type: String,
      require: false,
      default: ''
    },
    // 子标题
    subtext: {
      type: String,
      require: false,
      default: ''
    }
  },
  data () {
    return {
      dom: null
    };
  },
  methods: {
    resize () {
      this.dom.resize();
    }
  },
  mounted () {
    this.$nextTick(() => {
      let xAxisData = Object.keys(this.value);
      let seriesData = Object.values(this.value);
      let option = {
        title: {
          text: this.text,
          subtext: this.subtext,
          x: 'center'
        },
        xAxis: {
          type: 'category',
          data: xAxisData
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          data: seriesData,
          type: 'bar'
        }]
      };
      this.dom = echarts.init(this.$refs.dom, 'tdTheme');
      this.dom.setOption(option);
      on(window, 'resize', this.resize);
    });
  },
  beforeDestroy () {
    off(window, 'resize', this.resize);
  }
};
</script>
