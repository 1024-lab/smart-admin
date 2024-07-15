import CryptoJS from 'crypto-js';
import CryptoSM from 'sm-crypto';

function object2string(data) {
  if (typeof data === 'Object') {
    return JSON.stringify(data);
  }

  let str = JSON.stringify(data);
  if (str.startsWith("'") || str.startsWith('"')) {
    str = str.substring(1);
  }
  if (str.endsWith("'") || str.endsWith('"')) {
    str = str.substring(0, str.length - 1);
  }
  return str;
}

// -----------------------  AES 加密、解密 -----------------------
const AES_KEY = '1024abcd1024abcd1024abcd1024abcd';

const AES = {
  encryptData: function (data: any) {
    // AES 加密 并转为 base64
    let utf8Data = CryptoJS.enc.Utf8.parse(object2string(data));
    const key = CryptoJS.enc.Utf8.parse(AES_KEY);
    const encrypted = CryptoJS.AES.encrypt(utf8Data, key, {
      mode: CryptoJS.mode.ECB,
      padding: CryptoJS.pad.Pkcs7,
    });

    return encrypted.toString();
  },

  decryptData: function (data: any) {
    //  第一步：Base64 解码
    let words = CryptoJS.enc.Base64.parse(data);

    // 第二步：AES 解密
    const key = CryptoJS.enc.Utf8.parse(AES_KEY);
    return CryptoJS.AES.decrypt({ ciphertext: words }, key, {
      mode: CryptoJS.mode.ECB,
      padding: CryptoJS.pad.Pkcs7,
    }).toString(CryptoJS.enc.Utf8);
  },
};

// -----------------------  国密SM4算法 加密、解密 -----------------------
const SM4_KEY = '1024abcd1024abcd1024abcd1024abcd';

const SM4 = {
  encryptData: function (data: any) {
    // 第一步：SM4 加密
    let encryptData = CryptoSM.sm4.encrypt(object2string(data), SM4_KEY);
    // 第二步： Base64 编码
    return CryptoJS.enc.Base64.stringify(CryptoJS.enc.Utf8.parse(encryptData));
  },

  decryptData: function (data: any) {
    // 第一步：Base64 解码
    let words = CryptoJS.enc.Base64.parse(data);
    let decode64Str = CryptoJS.enc.Utf8.stringify(words);

    // 第二步：SM4 解密
    return CryptoSM.sm4.decrypt(decode64Str, SM4_KEY);
  },
};

// -----------------------  对外暴露： 加密、解密 -----------------------

// 默认使用SM4算法
const EncryptObject = SM4;
// const EncryptObject = AES;

/**
 * 加密
 */
export const encryptData = function (data: any) {
  return !data ? null : EncryptObject.encryptData(data);
};

/**
 * 解密
 */
export const decryptData = function (data: any) {
  return !data ? null : EncryptObject.decryptData(data);
};
