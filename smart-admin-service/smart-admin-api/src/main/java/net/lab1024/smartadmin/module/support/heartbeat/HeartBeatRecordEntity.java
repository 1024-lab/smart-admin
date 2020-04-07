package net.lab1024.smartadmin.module.support.heartbeat;

import com.baomidou.mybatisplus.annotation.TableName;
import net.lab1024.smartadmin.common.domain.BaseEntity;
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
@TableName(value = "t_heart_beat_record")
public class HeartBeatRecordEntity extends BaseEntity implements Serializable {

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
