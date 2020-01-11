import { postAxios, getAxios } from '@/lib/http';
import config from '@/config';
const baseUrl = config.baseUrl.apiUrl;
export const fileApi = {
  // 系统文件查询
  queryFileList: data => {
    return postAxios('/api/file/query', data);
  },
  // 系统文件下载通过接口
  downLoadFile: id => {
    return getAxios('/api/file/downLoad?id=' + id);
  },
  // 文件上传
  fileUpload: (type, data) => {
    // return postAxios('/api/file/localUpload/' + type, data);
    return this.fileUploadUrl;
  },
  // 文件保存
  addFile: data => {
    return postAxios('/api/file/save', data);
  },
  // 上传路径：本地
  fileUploadLocalUrl: baseUrl + '/api/file/localUpload/',
  // 上传路径：阿里OSS
  fileUploadAliUrl: baseUrl + '/api/file/aliYunUpload/',
  // 上传路径：七牛
  fileUploadQiNiuUrl: baseUrl + '/api/file/qiNiuUpload/'
};
