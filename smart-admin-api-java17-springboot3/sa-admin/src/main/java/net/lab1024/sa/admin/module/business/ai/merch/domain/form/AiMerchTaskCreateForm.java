package net.lab1024.sa.admin.module.business.ai.merch.domain.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AiMerchTaskCreateForm {

    @NotBlank(message = "服装图片不能为空")
    private String sourceFileKey;

    @NotBlank(message = "提示词1不能为空")
    private String promptTemplate;

    @NotNull(message = "提示词模型不能为空")
    private Long promptModelId;

    @NotNull(message = "图片模型不能为空")
    private Long imageModelId;

    @NotNull(message = "视频模型不能为空")
    private Long videoModelId;
}
