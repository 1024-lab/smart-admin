package net.lab1024.sa.base.module.support.heartbeat.core;

/**
 * 心跳处理接口
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-01-09 20:57:24
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
public interface IHeartBeatRecordHandler {

    /**
     * 心跳日志处理方法
     *
     * @param heartBeatRecord
     */
    void handler(HeartBeatRecord heartBeatRecord);
}
