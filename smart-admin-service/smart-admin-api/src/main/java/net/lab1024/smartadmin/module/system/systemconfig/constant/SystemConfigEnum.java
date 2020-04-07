package net.lab1024.smartadmin.module.system.systemconfig.constant;


import java.util.Arrays;
import java.util.Optional;

/**
 * [ 系统配置常量类 ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2019 1024lab.netInc. All rights reserved.
 * @date
 * @since JDK1.8
 */
public class SystemConfigEnum {

    public enum Group {
        BACK,
        GIT_LOG
    }

    public enum Key {
        /**
         * 超管id
         */
        EMPLOYEE_SUPERMAN(SystemConfigDataType.TEXT),
        /**
         * 阿里云OSS配置项
         */
        ALI_OSS(SystemConfigDataType.JSON),
        /**
         * 七牛云OSS配置项
         */
        QI_NIU_OSS(SystemConfigDataType.JSON),
        /**
         * 本地文件上传url前缀
         */
        LOCAL_UPLOAD_URL_PREFIX(SystemConfigDataType.URL),
        /**
         * 邮件配置
         */
        EMAIL_CONFIG(SystemConfigDataType.JSON),
        /**
         * git-log 插件
         */
        GIT_LOG_PLUGIN(SystemConfigDataType.JSON);

        private SystemConfigDataType dataType;

        Key(SystemConfigDataType dataType) {
            this.dataType = dataType;
        }


        public SystemConfigDataType getDataType() {
            return dataType;
        }

        public static Key selectByKey(String key) {
            Key[] values = Key.values();
            Optional<Key> first = Arrays.stream(values).filter(e -> e.name().equalsIgnoreCase(key)).findFirst();
            return !first.isPresent() ? null : first.get();
        }
    }

}
