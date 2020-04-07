package net.lab1024.smartadmin.common.heartbeat;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/8/8 0008 下午 16:23
 * @since JDK1.8
 */
public interface HeartBeatLogger {

    void error(String string);

    void error(String string, Throwable e);

    void info(String string);
}
