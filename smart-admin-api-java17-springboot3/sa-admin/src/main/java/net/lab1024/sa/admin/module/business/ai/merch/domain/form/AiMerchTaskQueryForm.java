package net.lab1024.sa.admin.module.business.ai.merch.domain.form;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.lab1024.sa.base.common.domain.PageParam;

@Data
@EqualsAndHashCode(callSuper = false)
public class AiMerchTaskQueryForm extends PageParam {

    private String promptStatus;

    private String imageStatus;

    private String videoStatus;
}
