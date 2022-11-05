package net.lab1024.sa.common.common.security;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 校验权限
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2022/5/12 21:50
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
public abstract class SecurityPermissionCheckService {


    /**
     * 校验是否有权限
     *
     * @param permission
     * @return
     */
    public boolean checkPermission(String permission) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        return checkPermission(authentication, permission);
    }

    /**
     * 校验是否有权限
     *
     * @param authentication
     * @param permission
     * @return
     */
    public abstract boolean checkPermission(Authentication authentication, String permission);

    /**
     * 判断
     *
     * @param userDetails
     * @param permissionStr
     * @return
     */
    protected boolean permissionJudge(UserDetails userDetails, String permissionStr) {
        if (CollectionUtils.isEmpty(userDetails.getAuthorities())) {
            return false;
        }

        if (StringUtils.isBlank(permissionStr)) {
            return false;
        }

        String[] permissionArray = permissionStr.split(",");
        for (String permission : permissionArray) {
            if(userDetails.getAuthorities().contains(new SimpleGrantedAuthority(permission))){
                return true;
            }
        }
        return false;
    }
}