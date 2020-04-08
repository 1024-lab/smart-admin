package net.lab1024.smartadmin.module.system.privilege.constant;

import net.lab1024.smartadmin.common.constant.ResponseCodeConst;


/**
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2019 1024lab.netInc. All rights reserved.
 * @date
 * @since JDK1.8
 */
public class PrivilegeResponseCodeConst extends ResponseCodeConst {

    public static final PrivilegeResponseCodeConst PRIVILEGE_NOT_EXISTS = new PrivilegeResponseCodeConst(7001, "当前数据不存在，请联系你的管理员！");

    public static final PrivilegeResponseCodeConst ROUTER_KEY_NO_REPEAT = new PrivilegeResponseCodeConst(7002, "模块和页面的“功能Key”值不能重复！");

    public static final PrivilegeResponseCodeConst MENU_NOT_EXIST = new PrivilegeResponseCodeConst(7003, "菜单不存在，清先保存菜单！");

    public static final PrivilegeResponseCodeConst POINT_NOT_EXIST = new PrivilegeResponseCodeConst(7004, "功能点不存在，清先保存功能点！");

    public PrivilegeResponseCodeConst(int code, String msg) {
        super(code, msg);
    }
}

