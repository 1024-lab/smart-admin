import { getAxios } from '@/lib/http';

export const userApi = {
  getSession: () => {
    return getAxios('/session/get');
  }
};
