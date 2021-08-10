import Axios from 'axios';
import config from '@/config';
import cookie from '@/lib/cookie';
import { Toast } from 'vant';

export const baseUrl = config.baseUrl.apiUrl;

const axios = Axios.create({
  baseURL: baseUrl,
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json; charset=utf-8'
  }
});

// 添加请求拦截器
axios.interceptors.request.use(
  function(config) {
    const token = cookie.getToken();
    if (token) {
      config.headers['x-access-token'] = token;
    }
    return config;
  },
  function(error) {
    return Promise.reject(error);
  }
);

// 添加响应拦截器
axios.interceptors.response.use(
  res => {
    const { data } = res;
    if (data && data.code && data.code !== 1) {
      if (data.code === 1001) {
        cookie.clearToken();
        localStorage.clear();
        window.location.href = window.location.pathname + '#/login';
        Toast.fail('未登录，或登录失效，请登录');
        return;
      } else if (data.code === 502) {
        window.location.href = window.location.pathname + '#/500';
        return;
      } else {
        Toast.fail(data.msg);
        return Promise.reject(res);
      }
    }
    return Promise.resolve(data);
  },
  error => {
    Toast.fail('服务内部错误');
    return Promise.reject(error);
  }
);

export const postAxios = (url, data, config) => {
  return axios.post(url, data, config);
};

export const getAxios = (url, data) => {
  return axios.get(url, {
    params: data
  });
};
