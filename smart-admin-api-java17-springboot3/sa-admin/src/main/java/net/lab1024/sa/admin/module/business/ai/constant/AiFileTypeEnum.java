package net.lab1024.sa.admin.module.business.ai.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * AI文件类型枚举
 *
 * @Author Mission
 * @Date 2025-12-21
 * @Copyright Mission
 */
@Getter
@AllArgsConstructor
public enum AiFileTypeEnum {

    DOCUMENT("document", "文档"),
    IMAGE("image", "图片"),
    AUDIO("audio", "音频"),
    VIDEO("video", "视频"),
    CUSTOM("custom", "自定义");

    private final String value;
    private final String desc;
}
