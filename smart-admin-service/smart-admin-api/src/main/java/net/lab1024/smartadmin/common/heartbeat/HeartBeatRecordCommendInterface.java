package net.lab1024.smartadmin.common.heartbeat;



/**
* @Description:
* @Author: simajinqiang
* @Date: 2018/7/9 14:06
*/
public interface HeartBeatRecordCommendInterface {
    /**
     * 处理
     * @param heartBeatRecord
     */
    void handler(HeartBeatRecordDTO heartBeatRecord);

}
