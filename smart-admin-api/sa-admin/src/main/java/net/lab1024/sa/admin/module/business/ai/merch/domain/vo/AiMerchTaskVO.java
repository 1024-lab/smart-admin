package net.lab1024.sa.admin.module.business.ai.merch.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AiMerchTaskVO {

    private Long taskId;

    private Long sourceFileId;

    private String sourceFileKey;

    private String sourceFileName;

    private String sourceFileType;

    private String sourceFileUrl;

    private String promptTemplate;

    private Long promptModelId;

    private String promptModelName;

    private Long imageModelId;

    private String imageModelName;

    private Long videoModelId;

    private String videoModelName;

    private String generatedPrompt;

    private String promptStatus;

    private String imageStatus;

    private String videoStatus;

    private String imageTaskId;

    private String videoTaskId;

    private String imageResultUrl;

    private String videoResultUrl;

    private Long imageFileId;

    private String imageFileKey;

    private String imageFileName;

    private String imageFileType;

    private String imageFileUrl;

    private Long videoFileId;

    private String videoFileKey;

    private String videoFileName;

    private String videoFileType;

    private String videoFileUrl;

    private String promptErrorMsg;

    private String imageErrorMsg;

    private String videoErrorMsg;

    private String createByName;

    private LocalDateTime createTime;

    private String updateByName;

    private LocalDateTime updateTime;
}
