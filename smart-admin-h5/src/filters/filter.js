import Vue from 'vue';

function ellipsis(value, length) {
  if (!value) return '';
  if (value.length > length) {
    return value.slice(0, length) + '...';
  }
  return value;
}

/**
 * 去除空格 type 1-所有空格 2-前后空格 3-前空格 4-后空格
 */
function trim(value, trim) {
  switch (trim) {
    case 1:
      return value.replace(/\s+/g, '');
    case 2:
      return value.replace(/(^\s*)|(\s*$)/g, '');
    case 3:
      return value.replace(/(^\s*)/g, '');
    case 4:
      return value.replace(/(\s*$)/g, '');
    default:
      return value;
  }
}

/**
 * 任意格式日期处理
 使用格式：
 {{ '2018-09-14 01:05' | formatDate(yyyy-MM-dd hh:mm:ss) }}
 {{ '2018-09-14 01:05' | formatDate(yyyy-MM-dd) }}
 {{ '2018-09-14 01:05' | formatDate(MM/dd) }} 等

 * @param value
 * @param fmt
 * @returns {*}
 */
function formatDate(value, fmt) {
  var date = new Date(value);
  var o = {
    'M+': date.getMonth() + 1, // 月份
    'd+': date.getDate(), // 日
    'h+': date.getHours(), // 小时
    'm+': date.getMinutes(), // 分
    's+': date.getSeconds(), // 秒
    'w+': date.getDay(), // 星期
    'q+': Math.floor((date.getMonth() + 3) / 3), // 季度
    'S': date.getMilliseconds() // 毫秒
  };
  if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length));
  for (var k in o) {
    if (k === 'w+') {
      if (o[k] === 0) {
        fmt = fmt.replace('w', '周日');
      } else if (o[k] === 1) {
        fmt = fmt.replace('w', '周一');
      } else if (o[k] === 2) {
        fmt = fmt.replace('w', '周二');
      } else if (o[k] === 3) {
        fmt = fmt.replace('w', '周三');
      } else if (o[k] === 4) {
        fmt = fmt.replace('w', '周四');
      } else if (o[k] === 5) {
        fmt = fmt.replace('w', '周五');
      } else if (o[k] === 6) {
        fmt = fmt.replace('w', '周六');
      }
    } else if (new RegExp('(' + k + ')').test(fmt)) {
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length)));
    }
  }
  return fmt;
}

/**
 * 字母大小写切换
 type
 1:首字母大写
 2：首页母小写
 3：大小写转换
 4：全部大写
 5：全部小写
 * @param str
 * @param type
 * @returns {string|*}
 */
function changeCase(str, type) {
  function ToggleCase(str) {
    var itemText = '';
    str.split('').forEach(
      function(item) {
        if (/^([a-z]+)/.test(item)) {
          itemText += item.toUpperCase();
        } else if (/^([A-Z]+)/.test(item)) {
          itemText += item.toLowerCase();
        } else {
          itemText += item;
        }
      });
    return itemText;
  }

  switch (type) {
    case 1:
      return str.replace(/\b\w+\b/g, function(word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
      });
    case 2:
      return str.replace(/\b\w+\b/g, function(word) {
        return word.substring(0, 1).toLowerCase() + word.substring(1).toUpperCase();
      });
    case 3:
      return ToggleCase(str);
    case 4:
      return str.toUpperCase();
    case 5:
      return str.toLowerCase();
    default:
      return str;
  }
}

/**
 * 字符串循环复制,count->次数
 */
function repeatStr(str, count) {
  var text = '';
  for (var i = 0; i < count; i++) {
    text += str;
  }
  return text;
}

/**
 * 字符串替换
 */
function replaceAll(str, AFindText, ARepText) {
  const raRegExp = new RegExp(AFindText, 'g');
  return str.replace(raRegExp, ARepText);
}

/**
 *
 * 字符替换*，隐藏手机号或者身份证号等
 replaceStr(字符串,字符格式, 替换方式,替换的字符（默认*）)
 ecDo.replaceStr('18819322663',[3,5,3],0)
 result：188*****663
 ecDo.replaceStr('asdasdasdaa',[3,5,3],1)
 result：***asdas***
 ecDo.replaceStr('1asd88465asdwqe3',[5],0)
 result：*****8465asdwqe3
 ecDo.replaceStr('1asd88465asdwqe3',[5],1,'+')
 result："1asd88465as+++++"
 *
 * @param str
 * @param regArr
 * @param type
 * @param ARepText
 * @returns {*}
 */
function replaceStr(str, regArr, type, ARepText) {
  var regtext = '';
  var Reg = null;
  var replaceText = ARepText || '*';
  // repeatStr是在上面定义过的（字符串循环复制），大家注意哦
  if (regArr.length === 3 && type === 0) {
    regtext = '(\\w{' + regArr[0] + '})\\w{' + regArr[1] + '}(\\w{' + regArr[2] + '})';
    Reg = new RegExp(regtext);
    var replaceCount = this.repeatStr(replaceText, regArr[1]);
    return str.replace(Reg, '$1' + replaceCount + '$2');
  } else if (regArr.length === 3 && type === 1) {
    regtext = '\\w{' + regArr[0] + '}(\\w{' + regArr[1] + '})\\w{' + regArr[2] + '}';
    Reg = new RegExp(regtext);
    var replaceCount1 = this.repeatStr(replaceText, regArr[0]);
    var replaceCount2 = this.repeatStr(replaceText, regArr[2]);
    return str.replace(Reg, replaceCount1 + '$1' + replaceCount2);
  } else if (regArr.length === 1 && type === 0) {
    regtext = '(^\\w{' + regArr[0] + '})';
    Reg = new RegExp(regtext);
    var replaceCount = this.repeatStr(replaceText, regArr[0]);
    return str.replace(Reg, replaceCount);
  } else if (regArr.length === 1 && type === 1) {
    regtext = '(\\w{' + regArr[0] + '}$)';
    Reg = new RegExp(regtext);
    var replaceCount = this.repeatStr(replaceText, regArr[0]);
    return str.replace(Reg, replaceCount);
  }
}

/**
 * 格式化处理字符串
 ecDo.formatText('1234asda567asd890')
 result："12,34a,sda,567,asd,890"
 ecDo.formatText('1234asda567asd890',4,' ')
 result："1 234a sda5 67as d890"
 ecDo.formatText('1234asda567asd890',4,'-')
 result："1-234a-sda5-67as-d890"

 * @param str
 * @param size
 * @param delimiter
 * @returns {*}
 */
function formatText(str, size, delimiter) {
  var _size = size || 3;
  var _delimiter = delimiter || ',';
  var regText = '\\B(?=(\\w{' + _size + '})+(?!\\w))';
  var reg = new RegExp(regText, 'g');
  return str.replace(reg, _delimiter);
}

/**
 * 现金额大写转换函数
 ecDo.upDigit(168752632)
 result："人民币壹亿陆仟捌佰柒拾伍万贰仟陆佰叁拾贰元整"
 ecDo.upDigit(1682)
 result："人民币壹仟陆佰捌拾贰元整"
 ecDo.upDigit(-1693)
 result："欠人民币壹仟陆佰玖拾叁元整"
 * @param n
 * @returns {string}
 */
function upDigit(n) {
  var fraction = ['角', '分', '厘'];
  var digit = ['零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'];
  var unit = [
    ['元', '万', '亿'],
    ['', '拾', '佰', '仟']
  ];
  var head = n < 0 ? '欠人民币' : '人民币';
  n = Math.abs(n);
  var s = '';
  for (var i = 0; i < fraction.length; i++) {
    s += (digit[Math.floor(n * 10 * Math.pow(10, i)) % 10] + fraction[i]).replace(/零./, '');
  }
  s = s || '整';
  n = Math.floor(n);
  for (var i = 0; i < unit[0].length && n > 0; i++) {
    var p = '';
    for (var j = 0; j < unit[1].length && n > 0; j++) {
      p = digit[n % 10] + unit[1][j] + p;
      n = Math.floor(n / 10);
    }
    s = p.replace(/(零.)*零$/, '').replace(/^$/, '零') + unit[0][i] + s;
    // s = p + unit[0][i] + s;
  }
  return head + s.replace(/(零.)*零元/, '元').replace(/(零.)+/g, '零').replace(/^整$/, '零元整');
}

// 保留2位小数
function toFixed(val, acc) {
  let num = parseFloat(val);
  if (isNaN(num)) {
    num = 0;
  }
  let accuracy = parseInt(acc);
  if (isNaN(accuracy) || accuracy < 0 || accuracy > 10) {
    accuracy = 2;
  }
  return num.toFixed(accuracy);
}

// 转百分比
function toPercent(val, acc) {
  let num = parseFloat(val);
  if (isNaN(num)) {
    num = 0;
  }
  let accuracy = parseInt(acc);
  if (isNaN(accuracy) || accuracy < 0 || accuracy > 10) {
    accuracy = 2;
  }
  return (num * 100).toFixed(accuracy) + '%';
}

// ------------ enum begin ------------
function getEnumDescByValue(value, enumName) {
  return Vue.prototype.$enum.getDescByValue(enumName, value);
}

// ------------ enum end ------------

export {
  trim,
  changeCase,
  repeatStr,
  replaceAll,
  replaceStr,
  formatText,
  upDigit,
  toFixed,
  formatDate,
  toPercent,
  getEnumDescByValue,
  ellipsis
};
