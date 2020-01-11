package com.gangquan360.smartadmin.module.log.orderoperatelog.constant;

import com.gangquan360.smartadmin.common.constant.ResponseCodeConst;

/**
 * [ 8001 -8999 ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2019 1024lab.netInc. All rights reserved.
 * @date
 * @since JDK1.8
 */
public class OrderOperateLogOperateTypeConst extends ResponseCodeConst {


    public static final OrderOperateLogOperateTypeConst ADD = new OrderOperateLogOperateTypeConst(8001, "创建并提交");

    public static final OrderOperateLogOperateTypeConst UPDATE = new OrderOperateLogOperateTypeConst(8002, "修改并提交");

    public static final OrderOperateLogOperateTypeConst DELETE = new OrderOperateLogOperateTypeConst(8003, "删除");


    private OrderOperateLogOperateTypeConst(int code, String msg) {
        super(code, msg);
    }


}
