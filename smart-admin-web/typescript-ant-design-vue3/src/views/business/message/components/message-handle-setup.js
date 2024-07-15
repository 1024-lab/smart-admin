import { useRouter } from 'vue-router';
import { MESSAGE_TYPE_ENUM } from '/src/constants/business/message/message-const';
import { messageApi } from '/@/api/business/message/message-api';

export function messageSetup() {
  // 去处理
  const router = useRouter();
  
  function toHandle(record) {
    //标记已读
    readHandle(record.messageId);
    // 订单
    if (record.messageType == MESSAGE_TYPE_ENUM.ORDER.value) {
      router.push({
        path: '/order/detail',
        query: {
          receiveOrderId: record.dataId
        }
      });
      return;
    }
  }
  
  // 标记已读
  async function readHandle(msgId) {
    try {
      await messageApi.updateReadFlag(msgId);
    } catch (e) {
      console.log(e);
    } finally {
    }
  }
  
  return {
    toHandle
  };
}
