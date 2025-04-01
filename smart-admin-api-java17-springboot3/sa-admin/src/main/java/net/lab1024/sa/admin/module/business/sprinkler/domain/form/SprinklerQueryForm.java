package net.lab1024.sa.admin.module.business.sprinkler.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;

@Data
public class SprinklerQueryForm extends PageParam {

    @Schema(description = "删除状态", hidden = true)
    private Boolean deletedFlag;
}
