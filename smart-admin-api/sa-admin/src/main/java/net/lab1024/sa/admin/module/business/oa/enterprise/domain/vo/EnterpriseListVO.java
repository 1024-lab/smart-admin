package net.lab1024.sa.admin.module.business.oa.enterprise.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * OA企业模块列表
 *
 * @author lihaifan
 * @date 2022/6/23 14:31
 */
@Data
public class EnterpriseListVO {

    @Schema(description = "企业ID")
    private Long enterpriseId;

    @Schema(description = "企业名称")
    private String enterpriseName;
}
