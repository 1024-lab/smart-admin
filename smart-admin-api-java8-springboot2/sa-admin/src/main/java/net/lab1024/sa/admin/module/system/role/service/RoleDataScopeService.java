package net.lab1024.sa.admin.module.system.role.service;

import com.google.common.collect.Lists;
import net.lab1024.sa.admin.module.system.role.domain.form.RoleDataScopeUpdateForm;
import net.lab1024.sa.admin.module.system.role.domain.vo.RoleDataScopeVO;
import net.lab1024.sa.base.common.code.UserErrorCode;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.admin.module.system.role.domain.entity.RoleDataScopeEntity;
import net.lab1024.sa.admin.module.system.role.manager.RoleDataScopeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色-数据范围
 *
 * @Author 1024创新实验室: 善逸
 * @Date 2021-10-22 23:17:47
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Service
public class RoleDataScopeService {

    @Resource
    private RoleDataScopeManager roleDataScopeManager;


    /**
     * 获取某个角色的数据范围设置信息
     *
     */
    public ResponseDTO<List<RoleDataScopeVO>> getRoleDataScopeList(Long roleId) {
        List<RoleDataScopeEntity> roleDataScopeEntityList = roleDataScopeManager.getBaseMapper().listByRoleId(roleId);
        if (CollectionUtils.isEmpty(roleDataScopeEntityList)) {
            return ResponseDTO.ok(Lists.newArrayList());
        }
        List<RoleDataScopeVO> roleDataScopeList = SmartBeanUtil.copyList(roleDataScopeEntityList, RoleDataScopeVO.class);
        return ResponseDTO.ok(roleDataScopeList);
    }

    /**
     * 批量设置某个角色的数据范围设置信息
     *
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> updateRoleDataScopeList(RoleDataScopeUpdateForm roleDataScopeUpdateForm) {
        List<RoleDataScopeUpdateForm.RoleUpdateDataScopeListFormItem> batchSetList = roleDataScopeUpdateForm.getDataScopeItemList();
        if (CollectionUtils.isEmpty(batchSetList)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "缺少配置信息");
        }
        List<RoleDataScopeEntity> roleDataScopeEntityList = SmartBeanUtil.copyList(batchSetList, RoleDataScopeEntity.class);
        roleDataScopeEntityList.forEach(e -> e.setRoleId(roleDataScopeUpdateForm.getRoleId()));
        roleDataScopeManager.getBaseMapper().deleteByRoleId(roleDataScopeUpdateForm.getRoleId());
        roleDataScopeManager.saveBatch(roleDataScopeEntityList);
        return ResponseDTO.ok();
    }
}
