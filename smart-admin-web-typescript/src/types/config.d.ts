/*
 * 应用默认配置
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-09-03 22:07:01
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */
/**
 *  语言 i18n
 */
export type LanguageType = 'zh_CN' | 'en' | 'ru' | 'ja' | 'ko';

/**
 * 四种布局: 左侧、左侧展开、顶部、混合
 */
export type LayoutType = 'side' | 'side-expand';

/**
 * 主题： 亮色，暗色，夜色
 */
export type ThemeType = 'light' | 'dark';


/**
 * 应用信息配置
 */
export interface AppConfig {
  // i18n 语言选择
  language: LanguageType;
  // 布局
  layout: string;
  // 主题
  sideMenuTheme: ThemeType;
  // 侧边菜单宽度 ， 默认为256px
  sideMenuWidth: number;
  // 主题颜色索引
  colorIndex: number;
  // 顶部菜单页面宽度
  pageWidth: string;
  // 圆角
  borderRadius: number;
  // 标签页
  pageTagFlag: boolean;
  // 标签页样式: default、 antd
  pageTagStyle: string;
  // 面包屑
  breadCrumbFlag: boolean;
  // 页脚
  footerFlag: boolean;
  // 帮助文档
  helpDocFlag: boolean;
  // 水印
  watermarkFlag: boolean;
  // 网站名称
  websiteName: string;
  // 主题颜色
  primaryColor: string;
  // 紧凑
  compactFlag: boolean;
}
