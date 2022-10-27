/*
 * 所有路由入口
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-09-06 20:52:26
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */
import { homeRouters } from './system/home';
import { loginRouters } from './system/login';
import { helpDocRouters } from './support/help-doc';

export const routerArray = [...loginRouters, ...homeRouters, ...helpDocRouters];
