package net.lab1024.smartadmin.module.system.systemconfig.constant;

import net.lab1024.smartadmin.util.SmartVerificationUtil;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/9/4 0004 上午 11:43
 * @since JDK1.8
 */
public enum SystemConfigDataType {
    /**
     * 整数
     */
    INTEGER(SmartVerificationUtil.INTEGER),
    /**
     * 文本
     */
    TEXT(null),
    /**
     * url地址
     */
    URL(SmartVerificationUtil.URL),
    /**
     *  邮箱
     */
    EMAIL(SmartVerificationUtil.EMAIL),
    /**
     * JSON 字符串
     */
    JSON(null),
    /**
     * 2019-08
     */
    YEAR_MONTH(SmartVerificationUtil.YEAR_MONTH),
    /**
     * 2019-08-01
     */
    DATE(SmartVerificationUtil.DATE),
    /**
     * 2019-08-01 10:23
     */
    DATE_TIME(SmartVerificationUtil.DATE_TIME),
    /**
     * 10:23-10:56
     */
    TIME_SECTION(SmartVerificationUtil.TIME_SECTION),
    /**
     * 10:23
     */
    TIME(SmartVerificationUtil.TIME);

    private String valid;


    SystemConfigDataType(String valid){
        this.valid = valid;
    }

    public String getValid() {
        return valid;
    }
}
