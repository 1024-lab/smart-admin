package net.lab1024.sa.base.common.enumeration;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统环境枚举类
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2020-10-15 22:45:04
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@AllArgsConstructor
@Getter
public enum SystemEnvironmentEnum implements BaseEnum {
    /**
     * dev
     */
    DEV(SystemEnvironmentNameConst.DEV, "开发环境"),

    /**
     * test
     */
    TEST(SystemEnvironmentNameConst.TEST, "测试环境"),

    /**
     * pre
     */
    PRE(SystemEnvironmentNameConst.PRE, "预发布环境"),

    /**
     * prod
     */
    PROD(SystemEnvironmentNameConst.PROD, "生产环境");

    private final String value;

    private final String desc;

    public static final class SystemEnvironmentNameConst {
        public static final String DEV = "dev";
        public static final String TEST = "test";
        public static final String PRE = "pre";
        public static final String PROD = "prod";
    }

}
