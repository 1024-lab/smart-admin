/*
 * 登录用户
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-09-06 20:55:09
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */
import _ from 'lodash';
import { defineStore } from 'pinia';
import { USER_TOKEN } from '@/constants/local-storage-key-const';
import { loginApi } from '@/api/system/login-api';

const defaultUserInfo = {
  //员工id
  employeeId: '',
  //登录名
  loginName: '',
  //姓名
  actualName: '',
  //手机号
  phone: '',
  //部门id
  departmentId: '',
  //部门名词
  departmentName: '',
  //是否为超级管理员
  administratorFlag: true,
  //上次登录ip
  lastLoginIp: '',
  //上次登录ip地区
  lastLoginIpRegion: '',
  //上次登录 设备
  lastLoginUserAgent: '',
  //上次登录时间
  lastLoginTime: '',
};

export const useUserStore = defineStore({
  id: 'userStore',
  state: () => ({
    ...defaultUserInfo,
  }),
  getters: {
    getToken(state) {
      return uni.getStorageSync(USER_TOKEN);
    },
  },

  actions: {
    logout() {
      this.token = null;
      this.setUserLoginInfo(defaultUserInfo);
      uni.removeStorage(USER_TOKEN);
    },
    clearUserLoginInfo() {
      this.setUserLoginInfo(defaultUserInfo);
      uni.removeStorage(USER_TOKEN);
    },
    async getLoginInfo() {
      let res = await loginApi.getLoginInfo();
      this.setUserLoginInfo(res.data);
    },
    //设置登录信息
    setUserLoginInfo(data) {
      // 用户基本信息
      this.token = data.token;
      this.employeeId = data.employeeId;
      this.loginName = data.loginName;
      this.actualName = data.actualName;
      this.phone = data.phone;
      this.departmentId = data.departmentId;
      this.departmentName = data.departmentName;
      this.administratorFlag = data.administratorFlag;
      this.lastLoginIp = data.lastLoginIp;
      this.lastLoginIpRegion = data.lastLoginIpRegion;
      this.lastLoginUserAgent = data.lastLoginUserAgent;
      this.lastLoginTime = data.lastLoginTime;

      uni.setStorageSync(USER_TOKEN, data.token);
    },
  },
});
