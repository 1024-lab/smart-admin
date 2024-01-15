package net.lab1024.sa.admin.module.system.role.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.sa.admin.module.system.role.dao.RoleMenuDao;
import net.lab1024.sa.admin.module.system.role.domain.entity.RoleMenuEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色-菜单 manager
 *
 * @Author 1024创新实验室: 善逸
 * @Date 2022-04-09 19:05:49
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Service
public class RoleMenuManager extends ServiceImpl<RoleMenuDao, RoleMenuEntity> {

    @Resource
    private RoleMenuDao roleMenuDao;

    /**
     * 更新角色权限
     *
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleMenu(Long roleId, List<RoleMenuEntity> roleMenuEntityList) {
        // 根据角色ID删除菜单权限
        roleMenuDao.deleteByRoleId(roleId);
        // 批量添加菜单权限
        saveBatch(roleMenuEntityList);
    }
}
