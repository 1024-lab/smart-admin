/*
 * 应用默认配置
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-09-03 22:07:01
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */
import { AppConfig } from '/@/types/config';

/**
 * 应用默认配置
 */

export const appDefaultConfig: AppConfig = {
  // i18n 语言选择
  language: 'zh_CN',
  // 布局: side 或者 side-expand 或者 top
  layout: 'side',
  // 侧边菜单宽度 ， 默认为200px
  sideMenuWidth: 200,
  // 菜单主题
  sideMenuTheme: 'dark',
  // 主题颜色索引
  colorIndex: 0,
  // 顶部菜单页面宽度
  pageWidth: '99%',
  // 圆角
  borderRadius: 6,
  // 标签页
  pageTagFlag: true,
  // 标签页样式: default、 antd
  pageTagStyle: 'default',
  // 面包屑
  breadCrumbFlag: true,
  // 页脚
  footerFlag: true,
  // 帮助文档
  helpDocFlag: true,
  // 水印
  watermarkFlag: true,
  // 网站名称
  websiteName: 'SmartAdmin 3.X',
  // 主题颜色
  primaryColor: '#1677ff',
  // 紧凑
  compactFlag: false,
};
