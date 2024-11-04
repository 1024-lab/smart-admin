package net.lab1024.sa.admin.module.business.goods.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * excel商品
 *
 * @Author 1024创新实验室: 胡克
 * @Date 2021-10-25 20:26:54
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsExcelVO {

    @ExcelProperty("商品分类")
    private String categoryName;

    @ExcelProperty("商品名称")
    private String goodsName;

    @ExcelProperty("商品状态错误")
    private String goodsStatus;

    @ExcelProperty("产地")
    private String place;

    @ExcelProperty("商品价格")
    private BigDecimal price;

    @ExcelProperty("备注")
    private String remark;
}
