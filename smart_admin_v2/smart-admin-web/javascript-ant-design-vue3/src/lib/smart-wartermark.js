/*
 * 水印
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-09-06 20:50:10
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */

import dayjs from 'dayjs';

/**
 *  水印DOM id
 */
const WATER_MARK_DOM_ID = 'smart_admin_water_mark';
let smartAdminWaterMarkIntervalId = null;

/**
 *
 * 因为modal的z-index为1000，所以为了modal的黑色背景隐藏掉，z-index为 999
 *
 * @param id
 * @param str
 * @param watermarkId
 * @returns
 */

function setWatermark(id, str) {
  //删掉之前的水印
  if (document.getElementById(WATER_MARK_DOM_ID) !== null) {
    document.getElementById(WATER_MARK_DOM_ID).remove();
  }

  str = str + ' ' + dayjs().format('YYYY-MM-DD HH:mm');

  //创建一个画布
  const can = document.createElement('canvas');
  //设置画布的长宽
  can.width = 400;
  can.height = 200;

  const cans = can.getContext('2d');
  //旋转角度
  cans.rotate((-15 * Math.PI) / 150);
  cans.font = '16px Microsoft JhengHei';
  //设置填充绘画的颜色、渐变或者模式
  cans.fillStyle = 'rgba(190, 190, 190, 0.30)';
  //设置文本内容的当前对齐方式
  cans.textAlign = 'left';
  //设置在绘制文本时使用的当前文本基线
  cans.textBaseline = 'middle';
  //在画布上绘制填色的文本（输出的文本，开始绘制文本的X坐标位置，开始绘制文本的Y坐标位置）
  cans.fillText(str, can.width / 8, can.height / 2);
  const div = document.createElement('div');
  div.id = WATER_MARK_DOM_ID;
  div.style.pointerEvents = 'none';
  div.style.top = '0px';
  div.style.left = '0px';
  div.style.position = 'absolute';
  div.style.zIndex = '999';
  div.style.width = '100%';
  div.style.height = '100%';
  div.style.background = 'url(' + can.toDataURL('image/png') + ') left top repeat';
  document.getElementById(id).appendChild(div);
}

const watermark = {
  show: function () {
    document.getElementById(WATER_MARK_DOM_ID).style.display = 'block';
  },
  hide: function () {
    document.getElementById(WATER_MARK_DOM_ID).style.display = 'hide';
  },
  // 该方法只允许调用一次
  set: function (id, str) {
    // 如果存在水印，则不允许再调用了
    if (document.getElementById(WATER_MARK_DOM_ID) !== null) {
      alert('已经添加过全局水印了，请不要再重复添加!');
      return;
    }

    setWatermark(id, str);

    //每隔1分钟检查一次水印
    smartAdminWaterMarkIntervalId = setInterval(() => {
      setWatermark(id, str);
    }, 60000);

    window.onresize = () => {
      setWatermark(id, str);
    };
  },
  // 清空水印
  clear: function () {
    document.getElementById(WATER_MARK_DOM_ID).remove();
    window.removeEventListener('resize', setWatermark);
    if (smartAdminWaterMarkIntervalId) {
      clearInterval(smartAdminWaterMarkIntervalId);
    }
  },
};
export default watermark;
