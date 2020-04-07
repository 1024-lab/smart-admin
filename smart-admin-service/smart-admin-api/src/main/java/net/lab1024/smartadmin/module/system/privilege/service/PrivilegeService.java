package net.lab1024.smartadmin.module.system.privilege.service;

import net.lab1024.smartadmin.common.domain.ResponseDTO;
import net.lab1024.smartadmin.module.system.privilege.constant.PrivilegeTypeEnum;
import net.lab1024.smartadmin.module.system.privilege.dao.PrivilegeDao;
import net.lab1024.smartadmin.module.system.privilege.domain.entity.PrivilegeEntity;
import net.lab1024.smartadmin.module.system.role.roleprivilege.RolePrivilegeDao;
import com.google.common.collect.Lists;
import net.lab1024.smartadmin.module.system.privilege.domain.dto.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * [ 后台员工权限 ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2019 1024lab.netInc. All rights reserved.
 * @date
 * @since JDK1.8
 */
@Service
public class PrivilegeService {

    @Autowired
    private PrivilegeRequestUrlService privilegeRequestUrlService;

    @Autowired
    private PrivilegeDao privilegeDao;

    @Autowired
    private RolePrivilegeDao rolePrivilegeDao;

    /**
     * 获取系统所有请求路径
     *
     * @return
     */
    public ResponseDTO<List<PrivilegeRequestUrlVO>> getPrivilegeUrlDTOList() {
        List<PrivilegeRequestUrlVO> privilegeUrlList = privilegeRequestUrlService.getPrivilegeList();
        return ResponseDTO.succData(privilegeUrlList);
    }

    /**
     * 批量保存权限菜单项
     *
     * @param menuList
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public ResponseDTO<String> menuBatchSave(List<PrivilegeMenuDTO> menuList) {
        if (CollectionUtils.isEmpty(menuList)) {
            return ResponseDTO.succ();
        }
        //使用前端发送权限的排序
        for (int i = 0; i < menuList.size(); i++) {
            menuList.get(i).setSort(i);
        }

        List<PrivilegeEntity> privilegeList = privilegeDao.selectByExcludeType(PrivilegeTypeEnum.POINTS.getValue());
        //若数据库无数据 直接全部保存
        if (CollectionUtils.isEmpty(privilegeList)) {
            List<PrivilegeEntity> menuSaveEntity = this.buildPrivilegeMenuEntity(menuList);
            privilegeDao.batchInsert(menuSaveEntity);
            return ResponseDTO.succ();
        }
        //处理需更新的菜单项
        Map<String, PrivilegeMenuDTO> storageMap = menuList.stream().collect(Collectors.toMap(PrivilegeMenuDTO::getMenuKey, e -> e));
        Set<String> menuKeyList = storageMap.keySet();
        List<PrivilegeEntity> updatePrivilegeList = privilegeList.stream().filter(e -> menuKeyList.contains(e.getKey())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(updatePrivilegeList)) {
            this.rebuildPrivilegeMenuEntity(storageMap, updatePrivilegeList);
            privilegeDao.batchUpdate(updatePrivilegeList);
        }
        //处理需删除的菜单项
        List<String> delKeyList = privilegeList.stream().filter(e -> !menuKeyList.contains(e.getKey())).map(PrivilegeEntity::getKey).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(delKeyList)) {
            privilegeDao.delByKeyList(delKeyList);
            //处理需删除的功能点
            privilegeDao.delByParentKeyList(delKeyList);
            rolePrivilegeDao.deleteByPrivilegeKey(delKeyList);
        }

        //处理需新增的菜单项
        List<String> dbKeyList = privilegeList.stream().map(PrivilegeEntity::getKey).collect(Collectors.toList());
        List<PrivilegeMenuDTO> addPrivilegeList = menuList.stream().filter(e -> !dbKeyList.contains(e.getMenuKey())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(addPrivilegeList)) {
            List<PrivilegeEntity> menuAddEntity = this.buildPrivilegeMenuEntity(addPrivilegeList);
            privilegeDao.batchInsert(menuAddEntity);
        }
        return ResponseDTO.succ();
    }

    /**
     * 构建权限菜单项类别
     *
     * @param menuList
     * @return
     */
    private List<PrivilegeEntity> buildPrivilegeMenuEntity(List<PrivilegeMenuDTO> menuList) {
        List<PrivilegeEntity> privilegeList = Lists.newArrayList();
        PrivilegeEntity privilegeEntity;
        for (PrivilegeMenuDTO menuDTO : menuList) {
            privilegeEntity = new PrivilegeEntity();
            privilegeEntity.setKey(menuDTO.getMenuKey());
            privilegeEntity.setName(menuDTO.getMenuName());
            privilegeEntity.setParentKey(menuDTO.getParentKey());
            privilegeEntity.setType(menuDTO.getType());
            privilegeEntity.setSort(menuDTO.getSort());
            privilegeEntity.setUrl(menuDTO.getUrl());
            privilegeList.add(privilegeEntity);
        }
        return privilegeList;
    }

    /**
     * 更新权限菜单项
     *
     * @param menuMap
     * @param menuEntityList
     */
    private void rebuildPrivilegeMenuEntity(Map<String, PrivilegeMenuDTO> menuMap, List<PrivilegeEntity> menuEntityList) {
        for (PrivilegeEntity menuEntity : menuEntityList) {
            PrivilegeMenuDTO menuDTO = menuMap.get(menuEntity.getKey());
            menuEntity.setName(menuDTO.getMenuName());
            menuEntity.setParentKey(menuDTO.getParentKey());
            menuEntity.setType(menuDTO.getType());
            menuEntity.setSort(menuDTO.getSort());
        }

    }

    /**
     * 查询所有的权限菜单
     *
     * @return
     */
    public ResponseDTO<List<PrivilegeMenuVO>> menuQueryAll() {
        List<PrivilegeEntity> privilegeEntityList = privilegeDao.selectByType(PrivilegeTypeEnum.MENU.getValue());
        if (CollectionUtils.isEmpty(privilegeEntityList)) {
            return ResponseDTO.succData(Lists.newArrayList());
        }

        List<PrivilegeMenuVO> voList = privilegeEntityList.stream().map(e-> {
            PrivilegeMenuVO vo = new PrivilegeMenuVO();
            vo.setMenuKey(e.getKey());
            vo.setMenuName(e.getName());
            vo.setParentKey(e.getParentKey());
            vo.setSort(e.getSort());
            vo.setUrl(e.getUrl());
            return vo;
        }).collect(Collectors.toList());

        return ResponseDTO.succData(voList);
    }


    /**
     * 保存更新功能点
     *
     * @param privilegeFunctionDTO
     * @return
     */
    public ResponseDTO<String> functionSaveOrUpdate(PrivilegeFunctionDTO privilegeFunctionDTO) {
        String functionKey = privilegeFunctionDTO.getFunctionKey();
        PrivilegeEntity functionEntity = privilegeDao.selectByKey(functionKey);
        if (functionEntity == null) {
            functionEntity = new PrivilegeEntity();
            functionEntity.setName(privilegeFunctionDTO.getFunctionName());
            functionEntity.setParentKey(privilegeFunctionDTO.getMenuKey());
            functionEntity.setType(PrivilegeTypeEnum.POINTS.getValue());
            functionEntity.setUrl(privilegeFunctionDTO.getUrl());
            functionEntity.setKey(privilegeFunctionDTO.getFunctionKey());
            functionEntity.setSort(privilegeFunctionDTO.getSort());
            functionEntity.setCreateTime(new Date());
            functionEntity.setUpdateTime(new Date());
            privilegeDao.insert(functionEntity);
        } else {
            functionEntity.setUrl(privilegeFunctionDTO.getUrl());
            functionEntity.setName(privilegeFunctionDTO.getFunctionName());
            functionEntity.setParentKey(privilegeFunctionDTO.getMenuKey());
            functionEntity.setSort(privilegeFunctionDTO.getSort());
            privilegeDao.updateById(functionEntity);
        }
        return ResponseDTO.succ();
    }

    /**
     * 查询功能点
     *
     * @param menuKey
     * @return
     */
    public ResponseDTO<List<PrivilegeFunctionVO>> functionQuery(String menuKey) {
        List<PrivilegeEntity> functionPrivilegeList = privilegeDao.selectByParentKey(menuKey);
        if (CollectionUtils.isEmpty(functionPrivilegeList)) {
            return ResponseDTO.succData(Lists.newArrayList());
        }
        List<PrivilegeFunctionVO> functionList = Lists.newArrayList();
        PrivilegeFunctionVO functionDTO;
        for (PrivilegeEntity functionEntity : functionPrivilegeList) {
            functionDTO = new PrivilegeFunctionVO();
            functionDTO.setFunctionKey(functionEntity.getKey());
            functionDTO.setFunctionName(functionEntity.getName());
            functionDTO.setMenuKey(functionEntity.getParentKey());
            functionDTO.setUrl(functionEntity.getUrl());
            functionDTO.setSort(functionEntity.getSort());
            functionList.add(functionDTO);
        }
        return ResponseDTO.succData(functionList);
    }

}
