package net.lab1024.sa.base.common.domain;

import lombok.Data;

import java.util.List;

/**
 * sa-token 所需的权限信息
 *
 * @Author 1024创新实验室-主任:卓大
 * @Date 2023/8/26 15:23:10
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>，Since 2012
 */

@Data
public class UserPermission {

    /**
     * 权限列表
     */
    private List<String> permissionList;

    /**
     * 角色列表
     */
    private List<String> roleList;


}
