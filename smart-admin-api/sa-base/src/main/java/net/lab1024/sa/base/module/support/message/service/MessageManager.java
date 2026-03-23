package net.lab1024.sa.base.module.support.message.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.sa.base.module.support.message.dao.MessageDao;
import net.lab1024.sa.base.module.support.message.domain.MessageEntity;
import org.springframework.stereotype.Service;

/**
 * 消息manager
 *
 * @author luoyi
 * @date 2024/06/22 20:20
 */
@Service
public class MessageManager extends ServiceImpl<MessageDao, MessageEntity> {


}
