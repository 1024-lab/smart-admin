package net.lab1024.sa.admin.module.business.goods.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品 实体类
 *
 * @Author 1024创新实验室: 胡克
 * @Date 2021-10-25 20:26:54
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@TableName("t_goods")
public class GoodsEntity {

    @TableId(type = IdType.AUTO)
    private Long goodsId;

    /**
     *  商品状态:[1:预约中,2:售卖中,3:售罄]
     */
    private Integer goodsStatus;

    /**
     * 商品分类
     */
    private Long categoryId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 产地
     */
    private String place;

    /**
     * 商品价格
     */
    private BigDecimal price;


    /**
     * 上架状态
     */
    private Boolean shelvesFlag;

    /**
     * 删除状态
     */
    private Boolean deletedFlag;

    /**
     * 备注
     */
    private String remark;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
