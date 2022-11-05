/*
 * @Author: zhuoda
 * @Date: 2021-08-03 10:27:11
 * @LastEditTime: 2022-05-16 20:59:27
 * @LastEditors: LiHaiFan
 * @Description:
 * @FilePath: /typescript-ant-design-vue/src/types/user.d.ts
 */
import { MenuVo } from '/@/api/system/menu/model/menu-vo';
import { MenuTreeVo } from '@/api/system/login/login-model.ts';
import { UserTagNav } from '/@/store/modules/model/UserTagNav';
import { LoginResultVo } from '/@/api/system/login/model/login-result-vo';

export interface UserState {
  /**
   * @description: token
   * @param {*}
   * @return {*}
   */
  token?: string;
  /**
   * @description: 用户信息
   * @param {*}
   * @return {*}
   */
  userInfo: LoginResultVo;
  /**
   * @description: 功能点权限列表
   * @param {*}
   * @return {*}
   */
  pointsList?: Array<string>;
  /**
   * @description: 菜单树
   * @param {*}
   * @return {*}
   */
  menuTree?: Array<MenuTreeVo>;
  /**
   * @description: 菜单列表 用于构建前端路由
   * @param {*}
   * @return {*}
   */
  menuList?: Array<MenuVo>;
  /**
   * @description: tag列表
   * @param {*}
   * @return {*}
   */
  tagNav?: Array<UserTagNav>;
  /**
   * @description: keep-alive缓存菜单
   * @param {*}
   * @return {*}
   */
  keepAliveIncludes?: string[];
}
