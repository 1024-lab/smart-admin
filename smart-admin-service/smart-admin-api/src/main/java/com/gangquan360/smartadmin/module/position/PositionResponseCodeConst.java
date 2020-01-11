package com.gangquan360.smartadmin.module.position;

import com.gangquan360.smartadmin.common.constant.ResponseCodeConst;

/**
 * @author zzr
 */
public class PositionResponseCodeConst extends ResponseCodeConst {

    public static final PositionResponseCodeConst REMOVE_DEFINE = new PositionResponseCodeConst(13000, "还有人关联该岗位,不能删除");

    protected PositionResponseCodeConst(int code, String msg) {
        super(code, msg);
    }

}
