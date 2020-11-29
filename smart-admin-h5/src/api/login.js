import { getAxios, postAxios } from '@/lib/http';

export const loginApi = {
  getVerificationCode: () => {
    return getAxios('/verificationCode');
  },
  login: (data) => {
    return postAxios('/login', data);
  },
  logout: (token) => {
    return getAxios(`/logout?x-access-token=${token}`);
  }
};
