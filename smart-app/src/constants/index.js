/*
 * 所有常量入口
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-09-06 19:58:28
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */
import { FLAG_NUMBER_ENUM, GENDER_ENUM, USER_TYPE_ENUM } from './common-const';
import loginDevice from './system/login-device-const';
import enterpriseConst from './business/oa/enterprise-const';
import goodsConst from './business/erp/goods-const';
import changeLogConst from './support/change-log-const';
import fileConst from './support/file-const';

export default {
  FLAG_NUMBER_ENUM,
  GENDER_ENUM,
  USER_TYPE_ENUM,
  ...loginDevice,
  ...enterpriseConst,
  ...goodsConst,
  ...changeLogConst,
  ...fileConst,
};
