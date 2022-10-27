import { postAxios, getAxios } from '@/lib/http';
import config from '@/config';
import Cookies from '@/lib/cookie';
const baseUrl = config.baseUrl.apiUrl;

export const fileApi = {
  // 文件上传
  fileUpload: (folder, data) => {
    const url = baseUrl + '/common/file/upload/' + folder + '?x-access-token=' + Cookies.getToken();
    return postAxios(url, data, {
      headers: { 'Content-Type': 'multipart/form-data' }
    });
  }
};
