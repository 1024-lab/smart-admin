package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 喷头信息编辑编辑
 *
 * @Author 海印: 芦苇
 */
@Data
public class SprinklerUpdateForm extends SprinklerCreateForm {

    @Schema(description = "喷头ID")
    @NotNull(message = "喷头ID不能为空")
    private Long sprinklerId;
}
