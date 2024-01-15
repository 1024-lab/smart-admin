package net.lab1024.sa.admin.module.business.goods.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.lab1024.sa.admin.module.business.goods.constant.GoodsStatusEnum;
import net.lab1024.sa.base.common.json.serializer.DictValueVoSerializer;
import net.lab1024.sa.base.common.swagger.SchemaEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品
 *
 * @Author 1024创新实验室: 胡克
 * @Date 2021-10-25 20:26:54
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
public class GoodsVO  {

    @Schema(description = "商品分类")
    private Long categoryId;

    @Schema(description = "商品名称")
    private String goodsName;

    @SchemaEnum(GoodsStatusEnum.class)
    private Integer goodsStatus;

    @Schema(description = "产地")
    @JsonSerialize(using = DictValueVoSerializer.class)
    private String place;

    @Schema(description = "商品价格")
    private BigDecimal price;

    @Schema(description = "上架状态")
    private Boolean shelvesFlag;

    @Schema(description = "备注|可选")
    private String remark;

    @Schema(description = "商品id")
    private Long goodsId;

    @Schema(description = "商品分类")
    private String categoryName;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
