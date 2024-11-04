package net.lab1024.sa.base.module.support.dict.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;

import javax.validation.constraints.NotNull;

/**
 * 字典
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2022/5/26 19:40:55
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class DictValueQueryForm extends PageParam {

    @Schema(description = "dictKeyId")
    @NotNull(message = "dictKeyId不能为空")
    private Long dictKeyId;

    @Schema(description = "搜索词")
    private String searchWord;

    @Schema(description = "删除标识",hidden = true)
    private Boolean deletedFlag;
}