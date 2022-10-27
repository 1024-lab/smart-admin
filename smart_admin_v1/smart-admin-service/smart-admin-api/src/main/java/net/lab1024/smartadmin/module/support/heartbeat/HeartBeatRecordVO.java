package net.lab1024.smartadmin.module.support.heartbeat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 心跳记录日志
 * User: simajinqiang
 * Date: 2018/7/9
 * Time: 11:11
 */
@Data
public class HeartBeatRecordVO implements Serializable {

    private Integer id;

    @ApiModelProperty("项目名字")
    private String projectPath;

    @ApiModelProperty("服务器ip")
    private String serverIp;

    @ApiModelProperty("进程号")
    private Integer processNo;

    @ApiModelProperty("进程开启时间")
    private Date processStartTime;

    @ApiModelProperty("心跳当前时间")
    private Date heartBeatTime;


}
