/*
 * @Description:
 * @Author: zhuoda
 * @Date: 2021-08-03
 * @LastEditTime: 2021-08-26
 * @LastEditors: zhuoda
 */
/**
 * 不同环境下的配置信息
 */
interface ImportMetaEnv {
  // 后端接口地址
  VITE_APP_API_URL: string;
  // 项目标题
  VITE_APP_PROJECT_TITLE: string;
  // 项目环境：dev, sit, pre, prod
  VITE_APP_PROFILE: string;
}
