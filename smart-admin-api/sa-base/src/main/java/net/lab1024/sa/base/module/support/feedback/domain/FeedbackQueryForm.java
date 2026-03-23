package net.lab1024.sa.base.module.support.feedback.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.base.common.domain.PageParam;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

/**
 * 意见反馈 查询
 *
 * @Author 1024创新实验室: 开云
 * @Date 2022-08-11 20:48:09
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class FeedbackQueryForm extends PageParam {

    @Schema(description = "搜索词")
    @Length(max = 25, message = "搜索词最多25字符")
    private String searchWord;

    @Schema(description = "开始时间", example = "2021-02-14")
    private LocalDate startDate;

    @Schema(description = "截止时间", example = "2022-10-15")
    private LocalDate endDate;
}
