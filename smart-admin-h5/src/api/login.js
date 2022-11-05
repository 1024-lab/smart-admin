import { getAxios, postAxios } from '@/lib/http';

export const loginApi = {
  login: (data) => {
    return postAxios('/session/login', data);
  },
  logout: (token) => {
    return getAxios(`/session/logOut?x-access-token=${token}`);
  }
};
