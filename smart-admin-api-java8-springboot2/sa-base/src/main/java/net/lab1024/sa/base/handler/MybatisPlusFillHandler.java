package net.lab1024.sa.base.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Mybatis Plus 插入或者更新时指定字段设置值
 *
 * @author zhoumingfa
 */
@Component
@Slf4j
public class MybatisPlusFillHandler implements MetaObjectHandler {

    public static final String CREATE_TIME = "createTime";

    public static final String UPDATE_TIME = "updateTime";

    @Override
    public void insertFill(MetaObject metaObject) {
        if (metaObject.hasSetter(CREATE_TIME)) {
            this.fillStrategy(metaObject, CREATE_TIME, LocalDateTime.now());
        }
        if (metaObject.hasSetter(UPDATE_TIME)) {
            this.fillStrategy(metaObject, UPDATE_TIME, LocalDateTime.now());
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (metaObject.hasSetter(UPDATE_TIME)) {
            this.fillStrategy(metaObject, UPDATE_TIME, LocalDateTime.now());
        }
    }

}
