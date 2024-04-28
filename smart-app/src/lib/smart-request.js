/*
 *  ajax请求
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-09-06 20:46:03
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */
import { USER_TOKEN } from '@/constants/local-storage-key-const';
import { DATA_TYPE_ENUM } from '@/constants/common-const';
import { decryptData, encryptData } from './encrypt';
import { useUserStore } from '@/store/modules/system/user';

const baseUrl = import.meta.env.VITE_APP_API_URL;

function getUserToken() {
  let token = uni.getStorageSync(USER_TOKEN);
  if (token) {
    return token;
  }
  return '';
}

/**
 * 处理返回的消息
 */
function handleResponse(response, resolve, reject) {
  // 如果是加密数据
  if (response.data.dataType === DATA_TYPE_ENUM.ENCRYPT.value) {
    response.data.encryptData = response.data.data;
    let decryptStr = decryptData(response.data.data);
    if (decryptStr) {
      response.data.data = JSON.parse(decryptStr);
    }
  }

  const res = response.data;
  if (res.code && res.code !== 1) {
    // `token` 过期或者账号已在别处登录
    if (res.code === 30007 || res.code === 30008 || res.code === 30012) {
      uni.showToast({
        title: res.msg,
        icon: 'none',
      });
      useUserStore().clearUserLoginInfo();
      uni.navigateTo({ url: '/pages/login/login' });
    }

    uni.showToast({
      title: res.msg,
      icon: 'none',
    });
    reject(response);
  } else {
    resolve(res);
  }
}

/**
 * 通用请求封装
 */
export const request = function (url, method, data) {
  return new Promise((resolve, reject) => {
    uni.request({
      url: baseUrl + url, //拼接请求路径
      data: data,
      method: method,
      header: {
        'x-access-token': getUserToken(),
      },
      success: (response) => {
        handleResponse(response, resolve, reject);
      },
      fail: (error) => {
        reject(error);
      },
    });
  });
};

/**
 * get请求
 */
export const getRequest = (url) => {
  return request(url, 'GET');
};

/**
 * post请求
 */
export const postRequest = (url, data) => {
  return request(url, 'POST', data);
};

// ================================= 加密 =================================

/**
 * 加密请求参数的post请求
 */
export const postEncryptRequest = (url, data) => {
  return request(url, 'POST', { encryptData: encryptData(data) });
};

// ================================= 文件 =================================

export const uploadRequest = function (filePath, folder) {
  return new Promise((resolve, reject) => {
    uni.uploadFile({
      url: baseUrl + '/support/file/upload',
      filePath,
      header: {
        'x-access-token': getUserToken(),
      },
      name: 'file',
      formData: {
        folder,
      },
      success: (response) => {
        response.data = JSON.parse(response.data.replace('\uFEFF', ''));
        handleResponse(response, resolve, reject);
      },
      fail: (error) => {
        reject(error);
      },
    });
  });
};
