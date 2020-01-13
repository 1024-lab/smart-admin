package com.gangquan360.smartadmin.module.login.domain;

import com.gangquan360.smartadmin.module.employee.domain.dto.EmployeeDTO;
import lombok.Data;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/8/9 0009 下午 17:32
 * @since JDK1.8
 */
@Data
public class LoginCacheDTO {

    /**
     * 基本信息
     */
    private EmployeeDTO employeeDTO;

    /**
     * 过期时间
     */
    private Long expireTime;
}
