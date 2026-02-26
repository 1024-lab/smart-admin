package net.lab1024.sa.base.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
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

    public static final String CREATE_BY = "createBy";

    public static final String CREATE_BY_NAME = "createByName";

    public static final String UPDATE_BY = "updateBy";

    public static final String UPDATE_BY_NAME = "updateByName";

    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        this.fillDate(metaObject, CREATE_TIME, now);
        this.fillUserInfo(metaObject, true);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        this.fillDate(metaObject, UPDATE_TIME, now);
        this.fillUserInfo(metaObject, false);
    }

    private void fillDate(MetaObject metaObject, String fieldName, LocalDateTime value) {
        if (metaObject.hasSetter(fieldName)) {
            this.fillStrategy(metaObject, fieldName, value);
        }
    }

    private void fillUserInfo(MetaObject metaObject, boolean includeCreateField) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        if (requestUser == null) {
            return;
        }

        String userId = requestUser.getUserId() == null ? null : String.valueOf(requestUser.getUserId());
        String userName = requestUser.getUserName();

        if (includeCreateField) {
            this.fillField(metaObject, CREATE_BY, userId);
            this.fillField(metaObject, CREATE_BY_NAME, userName);
        }
        this.fillField(metaObject, UPDATE_BY, userId);
        this.fillField(metaObject, UPDATE_BY_NAME, userName);
    }

    private void fillField(MetaObject metaObject, String fieldName, String value) {
        if (value == null || !metaObject.hasSetter(fieldName)) {
            return;
        }
        this.fillStrategy(metaObject, fieldName, value);
    }
}
