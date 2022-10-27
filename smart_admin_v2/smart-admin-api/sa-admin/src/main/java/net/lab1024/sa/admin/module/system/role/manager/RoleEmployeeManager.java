package net.lab1024.sa.admin.module.system.role.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.sa.admin.module.system.role.dao.RoleEmployeeDao;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import net.lab1024.sa.admin.module.system.role.domain.entity.RoleEmployeeEntity;

import java.util.List;

/**
 * 角色员工 manager
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-04-08 21:53:04
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Service
public class RoleEmployeeManager extends ServiceImpl<RoleEmployeeDao, RoleEmployeeEntity> {

    /**
     * 保存 角色员工
     *
     * @param roleId
     * @param roleEmployeeList
     */
    @Transactional(rollbackFor = Throwable.class)
    public void saveRoleEmployee(Long roleId, List<RoleEmployeeEntity> roleEmployeeList) {
        this.getBaseMapper().deleteByRoleId(roleId);
        if (CollectionUtils.isNotEmpty(roleEmployeeList)) {
            this.saveBatch(roleEmployeeList);
        }
    }
}
