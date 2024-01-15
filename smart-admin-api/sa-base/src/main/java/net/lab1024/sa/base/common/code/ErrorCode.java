package net.lab1024.sa.base.common.code;

/**
 * 错误码<br>
 * 一共分为三种： 1）系统错误、2）用户级别错误、3）未预期到的错误
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2021-09-02 20:21:10
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
public interface ErrorCode {

    /**
     * 系统等级
     */
    String LEVEL_SYSTEM = "system";

    /**
     * 用户等级
     */
    String LEVEL_USER = "user";

    /**
     * 未预期到的等级
     */
    String LEVEL_UNEXPECTED = "unexpected";

    /**
     * 错误码
     */
    int getCode();

    /**
     * 错误消息
     *
     */
    String getMsg();

    /**
     * 错误等级
     */
    String getLevel();


}
