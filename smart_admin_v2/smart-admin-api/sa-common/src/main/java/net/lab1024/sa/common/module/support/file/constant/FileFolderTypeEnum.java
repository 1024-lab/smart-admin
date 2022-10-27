package net.lab1024.sa.common.module.support.file.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.lab1024.sa.common.common.enumeration.BaseEnum;

/**
 * 文件服务 文件夹位置类型枚举类
 *
 * @Author 1024创新实验室: 胡克
 * @Date 2019年10月11日 15:34:47
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@AllArgsConstructor
@Getter
public enum FileFolderTypeEnum implements BaseEnum {

    COMMON(1, FileFolderTypeEnum.FOLDER_PUBLIC + "/common/", "通用"),

    NOTICE(2, FileFolderTypeEnum.FOLDER_PUBLIC + "/notice/", "公告"),

    HELP_DOC(3, FileFolderTypeEnum.FOLDER_PUBLIC + "help-doc", "帮助中心"),

    FEEDBACK(4, FileFolderTypeEnum.FOLDER_PUBLIC + "/feedback/", "意见反馈"),

    ;

    /**
     * 公用读取文件夹 public
     */
    public static final String FOLDER_PUBLIC = "public";

    /**
     * 私有读取文件夹 private
     */
    public static final String FOLDER_PRIVATE = "private";


    public static final String INFO = "1:通用";

    private final Integer value;

    private final String folder;

    private final String desc;
}

