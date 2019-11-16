/**
 * @param {String} url
 * @description 从URL中解析参数
 */
export const getParams = url => {
  const keyValueArr = url.split('?')[1].split('&');
  let paramObj = {};
  keyValueArr.forEach(item => {
    const keyValue = item.split('=');
    paramObj[keyValue[0]] = keyValue[1];
  });
  return paramObj;
};

/**
 * @param {Any} obj
 * @description 获取数据类型
 */
export const getType = obj => {
  return {}.toString
    .call(obj)
    .match(/\s([a-zA-Z]+)/)[1]
    .toLowerCase();
};
// 日期格式
export const dateFormat = {
  YMD: 'YMD',
  YMDHM: 'YMDHM',
  YMDHMS: 'YMDHMS'
};
export const forEach = (arr, fn) => {
  if (!arr.length || !fn) return;
  let i = -1;
  let len = arr.length;
  while (++i < len) {
    let item = arr[i];
    fn(item, i, arr);
  }
};

/**
 * @param {Array} arr1
 * @param {Array} arr2
 * @description 得到两个数组的交集, 两个数组的元素为数值或字符串
 */
export const getIntersection = (arr1, arr2) => {
  let len = Math.min(arr1.length, arr2.length);
  let i = -1;
  let res = [];
  while (++i < len) {
    const item = arr2[i];
    if (arr1.indexOf(item) > -1) res.push(item);
  }
  return res;
};

/**
 * @param {Array} arr1
 * @param {Array} arr2
 * @description 得到两个数组的并集, 两个数组的元素为数值或字符串
 */
export const getUnion = (arr1, arr2) => {
  return Array.from(new Set([...arr1, ...arr2]));
};

/**
 * @param {Array} target 目标数组
 * @param {Array} arr 需要查询的数组
 * @description 判断要查询的数组是否至少有一个元素包含在目标数组中
 */
export const hasOneOf = (targetarr, arr) => {
  return targetarr.some(_ => arr.indexOf(_) > -1);
};

/**
 * @param {String|Number} value 要验证的字符串或数值
 * @param {*} validList 用来验证的列表
 */
export function oneOf (value, validList) {
  for (let i = 0; i < validList.length; i++) {
    if (value === validList[i]) {
      return true;
    }
  }
  return false;
}

/**
 * @param {Number} timeStamp 判断时间戳格式是否是毫秒
 * @returns {Boolean}
 */
const isMillisecond = timeStamp => {
  const timeStr = String(timeStamp);
  return timeStr.length > 10;
};

/**
 * @param {Number} timeStamp 传入的时间戳
 * @param {Number} currentTime 当前时间时间戳
 * @returns {Boolean} 传入的时间戳是否早于当前时间戳
 */
const isEarly = (timeStamp, currentTime) => {
  return timeStamp < currentTime;
};

/**
 * @param {Number} num 数值
 * @returns {String} 处理后的字符串
 * @description 如果传入的数值小于10，即位数只有1位，则在前面补充0
 */
const getHandledValue = num => {
  return num < 10 ? '0' + num : num;
};

/**
 * @param {Number} timeStamp 传入的时间戳
 * @param {Number} startType 要返回的时间字符串的格式类型，传入'year'则返回年开头的完整时间
 */
const getDate = (timeStamp, startType) => {
  const d = new Date(timeStamp * 1000);
  const year = d.getFullYear();
  const month = getHandledValue(d.getMonth() + 1);
  const date = getHandledValue(d.getDate());
  const hours = getHandledValue(d.getHours());
  const minutes = getHandledValue(d.getMinutes());
  const second = getHandledValue(d.getSeconds());
  let resStr = '';
  if (startType === 'year')
    {resStr =
      year +
      '-' +
      month +
      '-' +
      date +
      ' ' +
      hours +
      ':' +
      minutes +
      ':' +
      second;}
  else resStr = month + '-' + date + ' ' + hours + ':' + minutes;
  return resStr;
};

/**
 * @param {String|Number} timeStamp 时间戳
 * @returns {String} 相对时间字符串
 */
export const getRelativeTime = timeStamp => {
  // 判断当前传入的时间戳是秒格式还是毫秒
  const IS_MILLISECOND = isMillisecond(timeStamp);
  // 如果是毫秒格式则转为秒格式
  if (IS_MILLISECOND) Math.floor((timeStamp /= 1000));
  // 传入的时间戳可以是数值或字符串类型，这里统一转为数值类型
  timeStamp = Number(timeStamp);
  // 获取当前时间时间戳
  const currentTime = Math.floor(Date.parse(new Date()) / 1000);
  // 判断传入时间戳是否早于当前时间戳
  const IS_EARLY = isEarly(timeStamp, currentTime);
  // 获取两个时间戳差值
  let diff = currentTime - timeStamp;
  // 如果IS_EARLY为false则差值取反
  if (!IS_EARLY) diff = -diff;
  let resStr = '';
  const dirStr = IS_EARLY ? '前' : '后';
  // 少于等于59秒
  if (diff <= 59) resStr = diff + '秒' + dirStr;
  // 多于59秒，少于等于59分钟59秒
  else if (diff > 59 && diff <= 3599)
    {resStr = Math.floor(diff / 60) + '分钟' + dirStr;}
  // 多于59分钟59秒，少于等于23小时59分钟59秒
  else if (diff > 3599 && diff <= 86399)
    {resStr = Math.floor(diff / 3600) + '小时' + dirStr;}
  // 多于23小时59分钟59秒，少于等于29天59分钟59秒
  else if (diff > 86399 && diff <= 2623859)
    {resStr = Math.floor(diff / 86400) + '天' + dirStr;}
  // 多于29天59分钟59秒，少于364天23小时59分钟59秒，且传入的时间戳早于当前
  else if (diff > 2623859 && diff <= 31567859 && IS_EARLY)
    {resStr = getDate(timeStamp);}
  else resStr = getDate(timeStamp, 'year');
  return resStr;
};

/**
 * @returns {String} 当前浏览器名称
 */
export const getExplorer = () => {
  const ua = window.navigator.userAgent;
  const isExplorer = exp => {
    return ua.indexOf(exp) > -1;
  };
  if (isExplorer('MSIE')) return 'IE';
  else if (isExplorer('Firefox')) return 'Firefox';
  else if (isExplorer('Chrome')) return 'Chrome';
  else if (isExplorer('Opera')) return 'Opera';
  else if (isExplorer('Safari')) return 'Safari';
};

/**
 * @description 绑定事件 on(element, event, handler)
 */
export const on = (function () {
  if (document.addEventListener) {
    return function (element, event, handler) {
      if (element && event && handler) {
        element.addEventListener(event, handler, false);
      }
    };
  } else {
    return function (element, event, handler) {
      if (element && event && handler) {
        element.attachEvent('on' + event, handler);
      }
    };
  }
})();

/**
 * @description 解绑事件 off(element, event, handler)
 */
export const off = (function () {
  if (document.removeEventListener) {
    return function (element, event, handler) {
      if (element && event) {
        element.removeEventListener(event, handler, false);
      }
    };
  } else {
    return function (element, event, handler) {
      if (element && event) {
        element.detachEvent('on' + event, handler);
      }
    };
  }
})();

/**
 * 判断一个对象是否存在key，如果传入第二个参数key，则是判断这个obj对象是否存在key这个属性
 * 如果没有传入key这个参数，则判断obj对象是否有键值对
 */
export const hasKey = (obj, key) => {
  if (key) return key in obj;
  else {
    let keysArr = Object.keys(obj);
    return keysArr.length;
  }
};

/**
 * @param {*} obj1 对象
 * @param {*} obj2 对象
 * @description 判断两个对象是否相等，这两个对象的值只能是数字或字符串
 */
export const objEqual = (obj1, obj2) => {
  const keysArr1 = Object.keys(obj1);
  const keysArr2 = Object.keys(obj2);
  if (keysArr1.length !== keysArr2.length) return false;
  else if (keysArr1.length === 0 && keysArr2.length === 0) return true;
  /* eslint-disable-next-line */ else
    {return !keysArr1.some(key => obj1[key] != obj2[key]);}
};

// 相关工具类
export const utils = {
  /**
   * @description table实现反选
   * @param {Object} vm Vue实例
   * @param {Array} tableSelectDate 选中的数据
   * @param {Array} allData 所有数据
   * @param {Array} key 数据中的唯一值
   */
  reverseSelect (vm, tableSelectDate, allData, key) {
    let copyMess = JSON.parse(JSON.stringify(tableSelectDate));
    // 流程：先全部选中->再部分选中
    vm.handleSelectAll(false);
    // 选中的idList
    let idList = copyMess.map(item => item[key]);
    console.log(idList);
    for (let item of allData) {
      if (idList.every(id => id !== item.id)) {
        vm.$set(item, '_checked', true);
        tableSelectDate.push(item);
      } else {
        vm.$set(item, '_checked', false);
      }
    }
  },
  // 校验字符串是否相同 合同使用
  contrastString (originStr, changeStr) {
    let origin = originStr
      .replace(/\s*/g, '')
      .replace(/"/g, '\'')
      .replace(/&nbsp;/g, '')
      .replace(/disabled=\/'\/'/g, 'disabled');
    let change = changeStr
      .replace(/\s*/g, '')
      .replace(/"/g, '\'')
      .replace(/&nbsp;/g, '')
      .replace(/disabled=\/'\/'/g, 'disabled');
    return origin === change;
  },
  // 获取当前日期getDateStr(0)、前几天getDateStr(-10)、后几天getDateStr(20)
  getDateStr (AddDayCount, format) {
    let date = new Date();
    // 获取AddDayCount天后的日期
    date.setDate(date.getDate() + AddDayCount);
    return this.getDate(date, format);
  },
  getDate (date, format) {
    let year = date.getFullYear();
    // day获取当前几号，不足10补0
    let day = date.getDate() > 9 ? date.getDate() : '0' + date.getDate();
    // month获取当前月份的日期，不足10补0
    let month =
      date.getMonth() + 1 > 9
        ? date.getMonth() + 1
        : '0' + (date.getMonth() + 1);
    // h获取当前小时，不足10补0
    let h = date.getHours() > 9 ? date.getHours() : '0' + date.getHours();
    // s获取当前分钟，不足10补0
    let m = date.getMinutes() > 9 ? date.getMinutes() : '0' + date.getMinutes();
    // s获取当前秒数，不足10补0
    let s = date.getSeconds() > 9 ? date.getSeconds() : '0' + date.getSeconds();
    let resultDate = '';
    if (format === dateFormat.YMD) {
      resultDate = year + '-' + month + '-' + day;
    }
    if (format === dateFormat.YMDHM) {
      resultDate = year + '-' + month + '-' + day + ' ' + h + ':' + m;
    }
    if (format === dateFormat.YMDHMS) {
      resultDate = year + '-' + month + '-' + day + ' ' + h + ':' + m + ':' + s;
    }
    return resultDate;
  },
  // 获取周一和周日日期，返回两种格式时间
  getDateWeek () {
    let now = new Date();
    let nowTime = now.getTime();
    let day = now.getDay();
    let oneDayLong = 1000 * 60 * 60 * 24;
    let MondayTime = nowTime - (day - 1) * oneDayLong;
    let SundayTime = nowTime + (7 - day) * oneDayLong;
    let monday = new Date(MondayTime);
    let sunday = new Date(SundayTime);
    return {
      // first: this.getDateAll(monday),
      // last: this.getDateAll(sunday),
      firstDate: monday,
      lastDate: sunday
    };
  },
  // 获取月初与月末日期，返回两种时间格式
  getDateMonth () {
    let dateFirter = new Date();
    let dateLast = new Date();
    dateFirter.setDate(1);

    let currentMonth = dateLast.getMonth();
    let nextMonth = ++currentMonth;
    let nextMonthFirstDay = new Date(dateLast.getFullYear(), nextMonth, 1);
    let oneDay = 1000 * 60 * 60 * 24;
    dateLast = new Date(nextMonthFirstDay - oneDay);

    return {
      // first: this.getDateAll(dateFirter),
      // last: this.getDateAll(dateLast),
      firstDate: dateFirter,
      lastDate: dateLast
    };
  },
  // 计算天数
  getDayBetweenDate (date) {
    date = this.getDate(new Date(date), 'YMD');
    let startTime = Date.parse(new Date(date)); // IE支持“yyyy/MM/dd”格式
    let endTime = Date.parse(this.getDate(new Date(), 'YMD'));
    let day = parseInt((endTime - startTime) / (1000 * 60 * 60 * 24));
    return day;
  },
  getDateIntervalYear (firstDate, secondDate) {
    if (!firstDate || !secondDate) {
      return 0;
    }
    let first = new Date(firstDate);
    let second = new Date(secondDate);
    let firstYear = first.getFullYear();
    let secondYear = second.getFullYear();
    let intervalYear = secondYear - firstYear;
    return intervalYear < 0 ? 0 : intervalYear;
  },
  getDateIntervalYearFixed2 (firstDate, secondDate) {
    if (!firstDate || !secondDate) {
      return 0;
    }
    // 格式化时间
    let startDate = new Date(this.getDate(new Date(firstDate), 'YMD'));
    let endDate = new Date(this.getDate(new Date(secondDate), 'YMD'));
    // 得到毫秒值
    let startTime = Date.parse(startDate);
    let endTime = Date.parse(endDate);
    // 得到差了多少天
    let day = parseInt((endTime - startTime) / (1000 * 60 * 60 * 24));
    if (day <= 0) {
      return 0;
    }
    // 得到差的多少年 保留两位小数
    let resultYear = parseFloat((day / (30 * 12)).toFixed(2));
    return resultYear;
  },
  // 数字转化为中文大写
  // 代码如下所示：
  convertCurrency (money) {
    // 汉字的数字
    let cnNums = ['零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'];
    // 基本单位
    let cnIntRadice = ['', '拾', '佰', '仟'];
    // 对应整数部分扩展单位
    let cnIntUnits = ['', '万', '亿', '兆'];
    // 对应小数部分单位
    let cnDecUnits = ['角', '分', '毫', '厘'];
    // 整数金额时后面跟的字符
    let cnInteger = '整';
    // 整型完以后的单位
    let cnIntLast = '元';
    // 最大处理的数字
    let maxNum = 999999999999999.9999;
    // 金额整数部分
    let integerNum;
    // 金额小数部分
    let decimalNum;
    // 输出的中文金额字符串
    let chineseStr = '';
    // 分离金额后用的数组，预定义
    let parts;
    if (money === '') {
      return '';
    }
    money = parseFloat(money);
    if (money >= maxNum) {
      // 超出最大处理数字
      return '';
    }
    if (money === 0) {
      chineseStr = cnNums[0] + cnIntLast + cnInteger;
      return chineseStr;
    }
    // 转换为字符串
    money = money.toString();
    if (money.indexOf('.') === -1) {
      integerNum = money;
      decimalNum = '';
    } else {
      parts = money.split('.');
      integerNum = parts[0];
      decimalNum = parts[1].substr(0, 4);
    }
    // 获取整型部分转换
    if (parseInt(integerNum, 10) > 0) {
      let zeroCount = 0;
      let IntLen = integerNum.length;
      for (let i = 0; i < IntLen; i++) {
        let n = integerNum.substr(i, 1);
        let p = IntLen - i - 1;
        let q = p / 4;
        let m = p % 4;
        if (n === '0') {
          zeroCount++;
        } else {
          if (zeroCount > 0) {
            chineseStr += cnNums[0];
          }
          // 归零
          zeroCount = 0;
          chineseStr += cnNums[parseInt(n)] + cnIntRadice[m];
        }
        if (m === 0 && zeroCount < 4) {
          chineseStr += cnIntUnits[q];
        }
      }
      chineseStr += cnIntLast;
    }
    // 小数部分
    if (decimalNum !== '') {
      let decLen = decimalNum.length;
      for (let i = 0; i < decLen; i++) {
        let n = decimalNum.substr(i, 1);
        if (n !== '0') {
          chineseStr += cnNums[Number(n)] + cnDecUnits[i];
        }
      }
    }
    if (chineseStr === '') {
      chineseStr += cnNums[0] + cnIntLast + cnInteger;
    } else if (decimalNum === '') {
      chineseStr += cnInteger;
    }
    return chineseStr;
  }
};
