/*
 *  ajax请求
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-09-06 20:46:03
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */

import axios, { AxiosRequestConfig } from 'axios';
import { getTokenFromCookie } from '/@/utils/cookie-util';
import { message, Modal } from 'ant-design-vue';
import { clearAllCoolies } from '/@/utils/cookie-util';
import { smartSentry } from '/@/lib/smart-sentry';
import { localClear } from '/@/utils/local-util';
import { decryptData, encryptData } from './encrypt';
import { DATA_TYPE_ENUM } from '../constants/common-const';
import _ from 'lodash';

// token的消息头
const TOKEN_HEADER: string = 'x-access-token';

// 创建axios对象
const smartAxios = axios.create({
  baseURL: import.meta.env.VITE_APP_API_URL,
});

// 退出系统
function logout() {
  localClear();
  location.href = '/';
}

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
    // 根据content-type ，判断是否为 json 数据
    let contentType = response.headers['content-type'] ? response.headers['content-type'] : response.headers['Content-Type'];
    if (contentType.indexOf('application/json') === -1) {
      return Promise.resolve(response);
    }
    
    // 如果是json数据
    if (response.data && response.data instanceof Blob) {
      return Promise.reject(response.data);
    }
    
    // 如果是加密数据
    if (response.data.dataType === DATA_TYPE_ENUM.ENCRYPT.value) {
      response.data.encryptData = response.data.data;
      let decryptStr = decryptData(response.data.data);
      if (decryptStr) {
        response.data.data = JSON.parse(decryptStr);
      }
    }
    
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
      
      // 等保安全的登录提醒
      if (res.code === 30010 || res.code === 30011) {
        Modal.error({
          title: '重要提醒',
          content: res.msg,
        });
        return Promise.reject(response);
      }
      
      // 长时间未操作系统，需要重新登录
      if (res.code === 30012) {
        Modal.error({
          title: '重要提醒',
          content: res.msg,
          onOk: logout,
        });
        setTimeout(logout, 3000);
        return Promise.reject(response);
      }
      message.destroy();
      
      message.error(res.msg);
      return Promise.reject(response);
    } else {
      return Promise.resolve(res);
    }
  },
  (error) => {
    // 对响应错误做点什么
    if (error.message.indexOf('timeout') != -1) {
      message.destroy();
      message.error('网络超时');
    } else if (error.message == 'Network Error') {
      message.destroy();
      message.error('网络连接错误');
    }else if (error.message.indexOf('Request') != -1) {
      message.destroy();
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
export const request = <T = any>(config: AxiosRequestConfig): Promise<T> => {
  return smartAxios.request(config);
};

/**
 * post请求
 */
export const postRequest = <T = any>(url: string, data: any): Promise<T> => {
  return request({ data, url, method: 'post' });
};

/**
 * get请求
 */
export const getRequest = <T = any>(url: string, params?: any): Promise<T> => {
  return request({ url, method: 'get', params });
};


// ================================= 加密 =================================

/**
 * 加密请求参数的post请求
 */
export const postEncryptRequest = <T = any>(url: string, data: any): Promise<T> => {
  return request({
    data: { encryptData: encryptData(data) },
    url,
    method: 'post',
  });
};

// ================================= 下载 =================================

export const postDownload = function (url: string, data: any) {
  request({
    method: 'post',
    url,
    data,
    responseType: 'blob',
  })
    .then((data) => {
      handleDownloadData(data);
    })
    .catch((error) => {
      handleDownloadError(error);
    });
};

/**
 * 文件下载
 */
export const getDownload = function (url: string, params: any) {
  request({
    method: 'get',
    url,
    params,
    responseType: 'blob',
  })
    .then((data) => {
      handleDownloadData(data);
    })
    .catch((error) => {
      handleDownloadError(error);
    });
};

function handleDownloadError(error: any) {
  if (error instanceof Blob) {
    const fileReader = new FileReader();
    fileReader.readAsText(error);
    fileReader.onload = () => {
      const msg: any = fileReader.result;
      const jsonMsg = JSON.parse(msg);
      message.destroy();
      message.error(jsonMsg.msg);
    };
  } else {
    message.destroy();
    message.error('网络发生错误', error);
  }
}

function handleDownloadData(response: any) {
  if (!response) {
    return;
  }
  
  // 获取返回类型
  let contentType = _.isUndefined(response.headers['content-type']) ? response.headers['Content-Type'] : response.headers['content-type'];
  
  // 构建下载数据
  let url = window.URL.createObjectURL(new Blob([response.data], { type: contentType }));
  let link = document.createElement('a');
  link.style.display = 'none';
  link.href = url;
  
  // 从消息头获取文件名
  let str = _.isUndefined(response.headers['content-disposition'])
    ? response.headers['Content-Disposition'].split(';')[1]
    : response.headers['content-disposition'].split(';')[1];
  
  let filename = _.isUndefined(str.split('fileName=')[1]) ? str.split('filename=')[1] : str.split('fileName=')[1];
  link.setAttribute('download', decodeURIComponent(filename));
  
  // 触发点击下载
  document.body.appendChild(link);
  link.click();
  
  // 下载完释放
  document.body.removeChild(link); // 下载完成移除元素
  window.URL.revokeObjectURL(url); // 释放掉blob对象
}

/**
 * 下载
 */
export const download = function (fileName: string, url: string, params?: any): void {
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
      smartSentry.captureError(error);
    });
};
