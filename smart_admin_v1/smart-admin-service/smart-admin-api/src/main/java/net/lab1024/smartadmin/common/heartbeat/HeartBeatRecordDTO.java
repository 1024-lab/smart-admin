package net.lab1024.smartadmin.common.heartbeat;

import lombok.Data;

import java.util.Date;

/**
* @Description: 心跳记录日志
* @Author: simajinqiang
* @Date: 2018/7/9 11:11
*/
@Data
public class HeartBeatRecordDTO {

    /**
     * 项目名字
     */
    private String projectPath;
    /**
     * 服务器ip
     */
    private String serverIp;
    /**
     * 进程号
     */
    private Integer processNo;
    /**
     * 进程开启时间
     */
    private Date processStartTime;
    /**
     * 心跳当前时间
     */
    private Date heartBeatTime;


}
