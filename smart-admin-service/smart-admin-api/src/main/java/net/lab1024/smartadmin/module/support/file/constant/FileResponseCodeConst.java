package net.lab1024.smartadmin.module.support.file.constant;

import net.lab1024.smartadmin.common.constant.ResponseCodeConst;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2019 1024lab.netInc. All rights reserved.
 * @date
 * @since JDK1.8
 */
public class FileResponseCodeConst extends ResponseCodeConst {

    /**
     * 4001 -4999
     */
    public static final FileResponseCodeConst FILE_EMPTY = new FileResponseCodeConst(4001, "上传文件不存在！");

    public static final FileResponseCodeConst FILE_SIZE_ERROR = new FileResponseCodeConst(4002, "上传文件超过%s，请重新上传！");

    public static final FileResponseCodeConst UNKNOWN_FILE_TYPE = new FileResponseCodeConst(4003, "未知的文件类型！");

    public static final FileResponseCodeConst LOCAL_UPDATE_PREFIX_ERROR = new FileResponseCodeConst(4004, "文件本地上传缺少URL前缀配置[local_upload_url_prefix]");

    public static final FileResponseCodeConst UPLOAD_ERROR = new FileResponseCodeConst(4005, "上传失败");

    public static final FileResponseCodeConst URL_ERROR = new FileResponseCodeConst(4006, "获取URL失败");

    public static final FileResponseCodeConst FILE_MODULE_ERROR = new FileResponseCodeConst(4007, "文件目录类型错误");

    public FileResponseCodeConst(int code, String msg) {
        super(code, msg);
    }
}
