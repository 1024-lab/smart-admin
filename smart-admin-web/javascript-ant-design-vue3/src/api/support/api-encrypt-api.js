/**
 * 接口：加密、解密
 *
 * @Author:    1024创新实验室-主任-卓大
 * @Date:      2023-10-17 20:02:37
 * @Copyright  1024创新实验室
 */
import { postRequest, postEncryptRequest } from '/src/lib/axios';

export const encryptApi = {

  /**
   * 测试 请求加密  @author  1024创新实验室-主任-卓大
   */
  testRequestEncrypt: (param) => {
    return postEncryptRequest('/support/apiEncrypt/testRequestEncrypt', param);
  },

  /**
   * 测试 返回加密  @author  1024创新实验室-主任-卓大
   */
  testResponseEncrypt: (param) => {
    return postRequest('/support/apiEncrypt/testResponseEncrypt', param);
  },

  /**
   * 测试 请求参数加密和解密、返回数据加密和解密  @author  1024创新实验室-主任-卓大
   */
  testDecryptAndEncrypt: (param) => {
    return postEncryptRequest('/support/apiEncrypt/testDecryptAndEncrypt', param);
  },

  /**
   * 测试 数组加解密  @author  1024创新实验室-主任-卓大
   */
  testArray : (param) => {
    return postEncryptRequest('/support/apiEncrypt/testArray', param);
  },
  
};
