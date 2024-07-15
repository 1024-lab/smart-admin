import {getRequest, postRequest} from "/@/lib/axios";

export const messageApi = {
    // 通知消息-分页查询
    queryMessage: (param) => {
        return postRequest('/message/query', param);
    },
    // 通知消息-查询未读消息数
    queryUnreadCount: () => {
        return getRequest('/message/query/unread/count');
    },
    // 通知消息-标记已读
    updateReadFlag: (msgId) => {
        return getRequest(`/message/update/read/${msgId}`);
    },
    // 通知消息-以及消息类型更新已读
    updateReadFlagByMsgType: (msgType) => {
        return getRequest(`/message/update/read/msgType/${msgType}`);
    },


};
