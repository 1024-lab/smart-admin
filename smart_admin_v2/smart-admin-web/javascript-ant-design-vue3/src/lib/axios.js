/*
 *  ajax请求
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-09-06 20:46:03
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */
import { message } from 'ant-design-vue';
import axios from 'axios';
import { clearAllCoolies, getTokenFromCookie } from '/@/utils/cookie-util';
import { localClear } from '/@/utils/local-util';

// token的消息头
const TOKEN_HEADER = 'x-access-token';

// 创建axios对象
const smartAxios = axios.create({
  baseURL: import.meta.env.VITE_APP_API_URL,
});

// ================================= 请求拦截器 =================================

smartAxios.interceptors.request.use(
  (config) => {
    // 在发送请求之前消息头加入token token
    const token = getTokenFromCookie();
    if (token) {
      config.headers[TOKEN_HEADER] = token;
    } else {
      delete config.headers[TOKEN_HEADER];
    }
    return config;
  },
  (error) => {
    // 对请求错误做些什么
    return Promise.reject(error);
  }
);

// ================================= 响应拦截器 =================================

// 添加响应拦截器
smartAxios.interceptors.response.use(
  (response) => {
    // 对响应数据做点什么
    const res = response.data;
    if (res.code && res.code !== 1) {
      // `token` 过期或者账号已在别处登录
      if (res.code === 30007 || res.code === 30008) {
        message.error('您没有登录，请重新登录');
        clearAllCoolies();
        localClear();
        //跳转到登录页面，直接使用页面刷新的策略
        setTimeout(() => {
          location.href = '/';
        }, 300);
        return Promise.reject(response);
      }
      message.error(res.msg);
      return Promise.reject(response);
    } else {
      return Promise.resolve(res);
    }
  },
  (error) => {
    // 对响应错误做点什么
    if (error.message.indexOf('timeout') != -1) {
      message.error('网络超时');
    } else if (error.message == 'Network Error') {
      message.error('网络连接错误');
    }else if (error.message.indexOf('Request') != -1) {
      message.error('网络发生错误');
    }
    return Promise.reject(error);
  }
);

// ================================= 对外提供请求方法：通用请求，get， post, 下载download等 =================================

/**
 * 通用请求封装
 * @param config
 */
export const request = (config) => {
  return smartAxios.request(config);
};

/**
 * post请求
 */
export const postRequest = (url, data) => {
  return request({ data, url, method: 'post' });
};

/**
 * get请求
 */
export const getRequest = (url, params) => {
  return request({ url, method: 'get', params });
};

/**
 * 文件下载
 */
export const download = function (fileName, url, params) {
  request({
    method: 'get',
    url: url,
    params: params,
    responseType: 'blob',
  })
    .then((data) => {
      if (!data) {
        return;
      }
      let url = window.URL.createObjectURL(new Blob([data]));
      let link = document.createElement('a');
      link.style.display = 'none';
      link.href = url;
      link.setAttribute('download', fileName);
      document.body.appendChild(link);
      link.click();
    })
    .catch((error) => {
      throw error;
    });
};
