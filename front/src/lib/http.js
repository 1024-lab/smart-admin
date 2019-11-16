import Axios from 'axios';
import config from '@/config';
import { Message, Spin } from 'iview';
import cookie from '@/lib/cookie';
// 之所以封装这个axios，是因为在一些请求中，无法上传信息，很尴尬，估计原因是继承的有问题，无法携带headers
export const baseUrl = config.baseUrl.apiUrl;
export const socketBaseUrl = config.baseUrl.webSocketUrl;

let axios = Axios.create({
  baseURL: baseUrl,
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json; charset=utf-8'
  }
});
// 添加请求拦截器

// download url
const downloadUrl = url => {
  let iframe = document.createElement('iframe');
  iframe.style.display = 'none';
  iframe.src = url;
  iframe.onload = function () {
    document.body.removeChild(iframe);
  };
  document.body.appendChild(iframe);
};

axios.interceptors.request.use(
  function (config) {
    // 在发送请求之前做些什么
    if (cookie.getToken()) {
      config.headers['x-access-token'] = cookie.getToken();
    }
    return config;
  },
  function (error) {
    // 对请求错误做些什么
    Spin.hide();
    return Promise.reject(error);
  }
);
// 添加响应拦截器
axios.interceptors.response.use(
  res => {
    // 处理请求是下载的接口
    if (
      res.headers &&
      (res.headers['content-type'] === 'application/x-msdownload' ||
        res.headers['content-type'] ===
          'application/octet-stream;charset=utf-8')
    ) {
      downloadUrl(res.request.responseURL);
      res.data = '';
      res.headers['content-type'] = 'text/json';
      return res;
    }
    let { data } = res;
    if (data.code !== 1) {
      if (data.code === 1001) {
        cookie.clearToken();
        localStorage.clear();
        window.location.href = window.location.pathname + '#/login';
        Message.error('未登录，或登录失效，请登录');
      } else if (data.code === 502) {
        window.location.href = window.location.pathname + '#/500';
      } else {
        Message.error(data.msg);
      }
      Spin.hide();
      return Promise.reject(res);
    }
    return data;
  },
  error => {
    Spin.hide();
    Message.error('服务内部错误');
    console.log('1111', error);
    // 对响应错误做点什么
    return Promise.reject(error);
  }
);

export const postAxios = (url, data) => {
  return axios.post(url, data);
};
export const getAxios = (url, data) => {
  return axios.get(url, {
    params: data
  });
};
