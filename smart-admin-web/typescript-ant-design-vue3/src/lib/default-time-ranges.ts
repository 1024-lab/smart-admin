/*
 * 时间选择框快捷选择
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-09-06 20:49:28
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */

import dayjs from 'dayjs';
import { ref } from "vue";

export const defaultTimeRanges = ref([
  {
    label:'今日',
    value:[dayjs().startOf('date'), dayjs().endOf('days')],
  },
  {
    label:'昨日',
    value:[dayjs().subtract(1, 'days').startOf('days'), dayjs().subtract(1, 'days').endOf('days')],
  },
  {
    label:'本月',
    value:[dayjs().startOf('month'), dayjs().endOf('month')],
  },
  {
    label:'上个月',
    value: [dayjs().subtract(1, 'months').startOf('month'), dayjs().subtract(1, 'months').endOf('month')],
  },
  {
    label:'本年度',
    value: [dayjs().startOf('year'), dayjs().endOf('year')],
  },
  {
    label:'上年度',
    value: [dayjs().subtract(1, 'years').startOf('year'), dayjs().subtract(1, 'years').endOf('year')],
  },
]);

// 不可跨月
export const defaultLimitMonth =ref([
  {
    label:'今日',
    value:[dayjs(), dayjs()],
  },
  {
    label:'昨日',
    value:[dayjs().subtract(1, 'days'), dayjs().subtract(1, 'days')],
  },
  {
    label:'本月',
    value:[dayjs().startOf('month'), dayjs().endOf('month')],
  },
  {
    label:'上个月',
    value:[dayjs().subtract(1, 'months').startOf('month'), dayjs().subtract(1, 'months').endOf('month')],
  },
  {
    label:'下个月',
    value:[dayjs().subtract(-1, 'months').startOf('month'), dayjs().subtract(-1, 'months').endOf('month')],
  },
]);

// 线索：今日，昨日，本月，上个月
export const defaultDaysLastMonth = ref([
  {
    label:'今日',
    value:[dayjs(), dayjs()],
  },
  {
    label:'昨日',
    value:[dayjs().subtract(1, 'days'), dayjs().subtract(1, 'days')],
  },
  {
    label:'本月',
    value:[dayjs().startOf('month'), dayjs().endOf('month')],
  },
  {
    label:'上个月',
    value:[dayjs().subtract(1, 'months').startOf('month'), dayjs().subtract(1, 'months').endOf('month')],
  },
])
