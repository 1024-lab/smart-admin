import Cookies from 'js-cookie';
import config from '@/config';
const { cookieExpires } = config;
export const TOKEN_KEY = 'token';

export default {
  setToken: token => {
    Cookies.set(TOKEN_KEY, token, {
      // token在Cookie中存储的天数，默认1天
      expires: cookieExpires || 7
    });
  },
  getToken: () => {
    const token = Cookies.get(TOKEN_KEY);
    if (token) return token;
    else return null;
  },
  clearToken: () => {
    Cookies.remove(TOKEN_KEY);
  }
};
