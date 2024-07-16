package net.lab1024.sa.admin.module.system.role.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.sa.admin.module.system.role.dao.RoleEmployeeDao;
import net.lab1024.sa.admin.module.system.role.domain.entity.RoleEmployeeEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色员工 manager
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-04-08 21:53:04
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Service
public class RoleEmployeeManager extends ServiceImpl<RoleEmployeeDao, RoleEmployeeEntity> {

    /**
     * 保存 角色员工
     *
     */
    @Transactional(rollbackFor = Throwable.class)
    public void saveRoleEmployee(Long roleId, List<RoleEmployeeEntity> roleEmployeeList) {
        this.getBaseMapper().deleteByRoleId(roleId);
        if (CollectionUtils.isNotEmpty(roleEmployeeList)) {
            this.saveBatch(roleEmployeeList);
        }
    }
}
