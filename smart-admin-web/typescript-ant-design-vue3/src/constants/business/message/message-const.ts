/*
 * @Description: file content
 * @Author: yandy
 * @Date: 2022-07-24 21:43:43
 * @LastEditors:
 * @LastEditTime: 2022-07-24 21:43:43
 */
export const MESSAGE_TYPE_ENUM = {
    MAIL: {
        value: 1,
        desc: '站内信'
    },
    ORDER: {
        value: 2,
        desc: '订单'
    },
  };


export const MESSAGE_RECEIVE_TYPE_ENUM = {
    EMPLOYEE: {
        value: 1,
        desc: '员工'
    },
  };

export default {
    MESSAGE_TYPE_ENUM,
    MESSAGE_RECEIVE_TYPE_ENUM
};
