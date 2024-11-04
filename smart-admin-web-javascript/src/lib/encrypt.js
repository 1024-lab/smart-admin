import CryptoJS from 'crypto-js';
import CryptoSM from 'sm-crypto';

function object2string(data) {
  if (typeof data === 'object') {
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

/**
 * 字符串转为数字
 */
function stringToHex(str) {
  let hex = '';
  for(let i = 0; i < str.length; i++) {
    hex += str.charCodeAt(i).toString(16).padStart(2, '0');
  }
  return hex;
}


/*
 * -------------------- ※ AES 加密、解密 begin ※ --------------------
 *
 * 1、AES加密算法支持三种密钥长度：128位、192位和256位，这里选择128位
 * 2、AES 要求秘钥为 128bit，转化字节为 16个字节；
 * 3、js前端使用 UCS-2 或者 UTF-16 编码，字母、数字、特殊符号等 占用1个字节；
 * 4、所以：秘钥Key 组成为：字母、数字、特殊符号 一共16个即可
 *
 * -------------------- ※ AES 加密、解密 end ※ --------------------
 */
const AES_KEY = '1024lab__1024lab';

const AES = {
  encryptData: function (data) {
    // AES 加密 并转为 base64
    let utf8Data = CryptoJS.enc.Utf8.parse(object2string(data));
    const key = CryptoJS.enc.Utf8.parse(AES_KEY);
    const encrypted = CryptoJS.AES.encrypt(utf8Data, key, {
      mode: CryptoJS.mode.ECB,
      padding: CryptoJS.pad.Pkcs7,
    });
    return CryptoJS.enc.Base64.stringify(encrypted.ciphertext);
  },

  decryptData: function (data) {
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

/*
 * -------------------- ※ 国密SM4算法 加密、解密 begin ※ --------------------
 *
 * 1、国密SM4 要求秘钥为 128bit，转化字节为 16个字节；
 * 2、js前端使用 UCS-2 或者 UTF-16 编码，字母、数字、特殊符号等 占用1个字节；
 * 3、java中 每个 字母数字 也是占用1个字节；
 * 4、所以：前端和后端的 秘钥Key 组成为：字母、数字、特殊符号 一共16个即可
 *
 * -------------------- ※ 国密SM4算法 加密、解密 end ※ --------------------
 */

// 秘钥Key 组成为：字母、数字、特殊符号 一共16个即可
const SM4_KEY = '1024lab__1024lab';

const SM4 = {
  encryptData: function (data) {
    // 第一步：SM4 加密
    let encryptData = CryptoSM.sm4.encrypt(object2string(data), stringToHex(SM4_KEY));
    // 第二步： Base64 编码
    return CryptoJS.enc.Base64.stringify(CryptoJS.enc.Utf8.parse(encryptData));
  },

  decryptData: function (data) {
    // 第一步：Base64 解码
    let words = CryptoJS.enc.Base64.parse(data);
    let decode64Str = CryptoJS.enc.Utf8.stringify(words);

    // 第二步：SM4 解密
    return CryptoSM.sm4.decrypt(decode64Str, stringToHex(SM4_KEY));
  },
};



// -----------------------  对外暴露： 加密、解密 -----------------------

// 默认使用SM4算法
const EncryptObject = SM4;
// const EncryptObject = AES;

/**
 * 加密
 */
export const encryptData = function (data) {
  return !data ? null : EncryptObject.encryptData(data);
};

/**
 * 解密
 */
export const decryptData = function (data) {
  return !data ? null : EncryptObject.decryptData(data);
};
