import { postAxios, getAxios } from '@/lib/http';

export const login = ({ userName, password }) => {
  const data = {
    userName,
    password
  };
  return postAxios('login', data);
};

export const getUserInfo = (token) => {
  let params = {
    token
  };
  return getAxios('get_info', params);
};

export const logout = (token) => {
  return postAxios('logout', {});
};
