package net.lab1024.smartadmin.module.support.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import net.lab1024.smartadmin.module.support.websocket.domain.MessageCommonDTO;
import net.lab1024.smartadmin.module.support.websocket.domain.MessageDTO;
import net.lab1024.smartadmin.module.support.websocket.domain.WebSocketHeartBeatDTO;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/7/10 0010 下午 16:09
 * @since JDK1.8
 */
@Slf4j
@ServerEndpoint("/webSocket/{employeeId}")
@Component
public class WebSocketServer {

    /**
     * 当前在线用户 employee,expireTime
     */
    private static ConcurrentHashMap<Long, Long> onLineUser = new ConcurrentHashMap<>();

    /**
     * 当前在线用户所对应的 socket session信息
     */
    private static ConcurrentHashMap<Long, Session> webSocketSession = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("employeeId") Long employeeId) {
        if (employeeId == null) {
            return;
        }
        webSocketSession.put(employeeId, session);
        log.info("连接打开");
    }

    /**
     * 不做处理如果 前台可以监听到浏览器关闭 此处处理在线人数也可
     *
     * @param session
     */
    @OnClose
    public void onClose(Session session) {

        log.info("连接关闭");
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("socket error,{}", error);
        error.printStackTrace();
    }

    /**
     * 此方法接收 前台信息
     *
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        if (StringUtils.isEmpty(message)) {
            return;
        }
        MessageCommonDTO messageCommonDTO = JSON.parseObject(message, new TypeReference<MessageCommonDTO>() {});
        if (MessageTypeEnum.HEART_BEAT.getValue().equals(messageCommonDTO.getMessageType())) {
            this.heartBeatHandle(messageCommonDTO.getJsonStr());
        }
    }

    /**
     * 更新用户过期时间
     *
     * @param json
     */
    private void heartBeatHandle(String json) {
        Long currentDate = System.currentTimeMillis();
        Long expireTime = currentDate + 5 * 1000;
        WebSocketHeartBeatDTO heartBeatDTO = JSON.parseObject(json, new TypeReference<WebSocketHeartBeatDTO>() {});
        Long employeeId = heartBeatDTO.getEmployeeId();
        onLineUser.put(employeeId, expireTime);
    }

    /**
     * 移除过期用户,如果用户超过5s未获取到心跳列表则清除在线用户信息
     */
    @Scheduled(cron = "0/5 * * * * ?")
    private void removeOnLineUser() {
        Long currentDate = System.currentTimeMillis();
        Iterator<Entry<Long, Long>> it = onLineUser.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Long, Long> entry = it.next();
            Long key = entry.getKey();
            Long value = entry.getValue();
            Long userExpireTime = value + 5 * 1000;
            if (currentDate > userExpireTime) {
                onLineUser.remove(key);
                webSocketSession.remove(key);
            }
        }
    }

    /**
     * 此方法用户后台发送消息
     *
     * @param messageDTO
     */
    public static void sendMessage(MessageDTO messageDTO) {
        //系统通知
        if (MessageTypeEnum.SYS_NOTICE.getValue().equals(messageDTO.getMessageType())) {
            sendAllOnLineUser(messageDTO.getMessage(), messageDTO.getFromUserId());
        }
        //站内信
        if (MessageTypeEnum.PRIVATE_LETTER.getValue().equals(messageDTO.getMessageType())) {
            sendOneOnLineUser(messageDTO.getMessage(), messageDTO.getToUserId());
        }
    }

    /**
     * 通知所有在线用户
     *
     * @param message
     */
    public static void sendAllOnLineUser(String message, Long fromUserId) {
        for (Entry<Long, Session> entry : webSocketSession.entrySet()) {
            Session session = entry.getValue();
            Long userId = entry.getKey();
            try {
                //不想消息创建人推送消息
                if (! userId.equals(fromUserId)) {
                    session.getBasicRemote().sendText(message);
                }
            } catch (IOException e) {
                log.error("推送消息到{},发送错误{}", userId, e);
                log.error("", e);
            }

        }
    }

    /**
     * 通知某人
     *
     * @param message
     * @param toUserId
     */
    public static void sendOneOnLineUser(String message, Long toUserId) {
        Session session = webSocketSession.get(toUserId);
        if (session == null) {
            log.error("推送消息到{},用户不在线", toUserId);
        }
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error("推送消息到{},发送错误{}", toUserId, e);
            log.error("", e);
        }
    }

    /**
     * 获取所有在线用户id
     *
     * @return
     */
    public static List<Long> getOnLineUserList() {
        return Lists.newArrayList(onLineUser.keySet());
    }

    /**
     * 获取当前在线用户数
     *
     * @return
     */
    public static Integer getOnLineUserCount() {
        return onLineUser.entrySet().size();
    }

}
