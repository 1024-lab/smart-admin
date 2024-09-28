package net.lab1024.sa.base.module.support.loginlog.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;

/**
 * 登录查询日志
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022/07/22 19:46:23
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class LoginLogQueryForm extends PageParam {

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "用户类型")
    private Integer userType;

    @Schema(description = "开始日期")
    private String startDate;

    @Schema(description = "结束日期")
    private String endDate;

    @Schema(description = "用户名称")
    private String userName;

    @Schema(description = "ip")
    private String ip;

}
