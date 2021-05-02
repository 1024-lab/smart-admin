package net.lab1024.smartadmin.module.support.idgenerator.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import net.lab1024.smartadmin.common.domain.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author sun
 * @Auther: anders
 * @Date: 2018/8/7 0007 13:33
 * @Description:
 */
@Data
@TableName(value = "t_id_generator")
public class IdGeneratorEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 5582354131134766548L;
    /**
     * 英文key
     */
    private String keyName;
    /**
     * 规则格式
     */
    private String ruleFormat;
    /**
     * 类型
     */
    private String ruleType;
    /**
     * 初始值
     */
    private Long initNumber;
    /**
     * 上次产生的id
     */
    private Long lastNumber;
    /**
     * 备注
     */
    private String remark;
}
