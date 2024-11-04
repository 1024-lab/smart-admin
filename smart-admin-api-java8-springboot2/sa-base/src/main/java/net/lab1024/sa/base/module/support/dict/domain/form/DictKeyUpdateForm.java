package net.lab1024.sa.base.module.support.dict.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

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
public class DictKeyUpdateForm extends DictKeyAddForm {

    @Schema(description = "keyId")
    @NotNull(message = "keyId不能为空")
    private Long dictKeyId;
}