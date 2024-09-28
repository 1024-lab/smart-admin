package net.lab1024.sa.admin.module.business.oa.enterprise.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 企业员工
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2022/7/28 20:37:15
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Data
@TableName("t_oa_enterprise_employee")
@NoArgsConstructor
public class EnterpriseEmployeeEntity {

    @TableId(type = IdType.AUTO)
    private Long enterpriseEmployeeId;

    /**
     * 企业ID
     */
    private Long enterpriseId;
    /**
     * 员工
     */
    private Long employeeId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    public EnterpriseEmployeeEntity(Long enterpriseId, Long employeeId) {
        this.enterpriseId = enterpriseId;
        this.employeeId = employeeId;
    }
}
