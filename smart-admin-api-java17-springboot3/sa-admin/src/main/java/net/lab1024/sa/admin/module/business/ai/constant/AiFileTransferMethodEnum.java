package net.lab1024.sa.admin.module.business.ai.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * AI文件传输方式枚举
 *
 * @Author Mission
 * @Date 2025-12-21
 * @Copyright Mission
 */
@Getter
@AllArgsConstructor
public enum AiFileTransferMethodEnum {

    REMOTE_URL("remote_url", "远程URL"),
    LOCAL_FILE("local_file", "本地文件");

    private final String value;
    private final String desc;
}
