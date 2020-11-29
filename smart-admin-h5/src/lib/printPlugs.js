// 打印类属性、方法定义
/* eslint-disable */
//第二个参数表明是否要关闭当前窗口
const Print = function(dom, close, options) {
  if (!(this instanceof Print)) return new Print(dom, close, options);

  this.options = this.extend(
    {
      noPrint: '.no-print'
    },
    options
  );

  if (typeof dom === 'string') {
    this.dom = document.querySelector(dom);
  } else {
    this.dom = dom;
  }

  this.init(close);
};
Print.prototype = {
  init: function(close) {
    var content = this.getStyle() + this.getHtml();
    this.writeIframe(content, close);
  },
  extend: function(obj, obj2) {
    for (var k in obj2) {
      obj[k] = obj2[k];
    }
    return obj;
  },

  getStyle: function() {
    var str = '',
      styles = document.querySelectorAll('style,link');
    for (var i = 0; i < styles.length; i++) {
      str += styles[i].outerHTML;
    }
    str +=
      '<style>body, html{height: auto; overflow: auto !important;}' +
      (this.options.noPrint ? this.options.noPrint : '.no-print') +
      '{display:none;}</style>';

    return str;
  },

  getHtml: function() {
    var inputs = document.querySelectorAll('input');
    var textareas = document.querySelectorAll('textarea');
    var selects = document.querySelectorAll('select');

    for (var k in inputs) {
      if (inputs[k].type == 'checkbox' || inputs[k].type == 'radio') {
        if (inputs[k].checked == true) {
          inputs[k].setAttribute('checked', 'checked');
        } else {
          inputs[k].removeAttribute('checked');
        }
      } else if (inputs[k].type == 'text') {
        inputs[k].setAttribute('value', inputs[k].value);
      }
    }

    for (var k2 in textareas) {
      if (textareas[k2].type == 'textarea') {
        textareas[k2].innerHTML = textareas[k2].value;
      }
    }

    for (var k3 in selects) {
      if (selects[k3].type == 'select-one') {
        var child = selects[k3].children;
        for (var i in child) {
          if (child[i].tagName == 'OPTION') {
            if (child[i].selected == true) {
              child[i].setAttribute('selected', 'selected');
            } else {
              child[i].removeAttribute('selected');
            }
          }
        }
      }
    }
    return this.dom.outerHTML;
  },

  writeIframe: function(content, close) {
    var w,
      doc,
      iframe = document.createElement('iframe'),
      f = document.body.appendChild(iframe);
    iframe.id = 'myIframe';
    iframe.style = 'position:absolute;';

    w = f.contentWindow || f.contentDocument;
    doc = f.contentDocument || f.contentWindow.document;
    doc.open();
    doc.write(content);
    doc.close();
    this.toPrint(w, close);
    setTimeout(function() {
      document.body.removeChild(iframe);
    }, 500);
  },

  toPrint: function(frameWindow, close) {
    try {
      setTimeout(function() {
        frameWindow.focus();
        try {
          if (!frameWindow.document.execCommand('print', false, null)) {
            frameWindow.print();
          }
        } catch (e) {
          frameWindow.print();
        }
        frameWindow.close();
        if (close) {
          window.close();
        }
      }, 500);
    } catch (err) {
      console.log('err', err);
    }
  }
};
const MyPlugin = {};
MyPlugin.install = function(Vue, options) {
  // 4. 添加实例方法
  Vue.prototype.$print = Print;
};
export default MyPlugin;
