/*
 * cookie相关操作
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-09-06 20:58:49
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */
import Cookies from 'js-cookie';

export const COOKIE_TOKEN_KEY = 'user_token';

export const clearAllCoolies = (): void => {
  Cookies.remove(COOKIE_TOKEN_KEY);
};

export const getTokenFromCookie = (): string | undefined => {
  return Cookies.get(COOKIE_TOKEN_KEY);
};

/**
 * 7 一年后cookie过期
 *
 * @param token
 */
export const saveTokenToCookie = (token: string): void => {
  Cookies.set(COOKIE_TOKEN_KEY, token, { expires: 365 });
};
