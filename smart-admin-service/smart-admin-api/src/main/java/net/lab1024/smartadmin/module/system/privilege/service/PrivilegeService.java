package net.lab1024.smartadmin.module.system.privilege.service;

import com.google.common.collect.Lists;
import net.lab1024.smartadmin.common.domain.ResponseDTO;
import net.lab1024.smartadmin.common.domain.ValidateList;
import net.lab1024.smartadmin.module.system.privilege.constant.PrivilegeResponseCodeConst;
import net.lab1024.smartadmin.module.system.privilege.constant.PrivilegeTypeEnum;
import net.lab1024.smartadmin.module.system.privilege.dao.PrivilegeDao;
import net.lab1024.smartadmin.module.system.privilege.domain.dto.*;
import net.lab1024.smartadmin.module.system.privilege.domain.entity.PrivilegeEntity;
import net.lab1024.smartadmin.module.system.role.roleprivilege.RolePrivilegeDao;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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

        List<PrivilegeMenuVO> voList = privilegeEntityList.stream().map(e -> {
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
            return ResponseDTO.wrap(PrivilegeResponseCodeConst.POINT_NOT_EXIST);
        }
        functionEntity.setUrl(privilegeFunctionDTO.getUrl());
        functionEntity.setName(privilegeFunctionDTO.getFunctionName());
        functionEntity.setParentKey(privilegeFunctionDTO.getMenuKey());
        functionEntity.setSort(privilegeFunctionDTO.getSort());
        privilegeDao.updateById(functionEntity);

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
        for (PrivilegeEntity functionEntity : functionPrivilegeList) {
            PrivilegeFunctionVO functionDTO = new PrivilegeFunctionVO();
            functionDTO.setFunctionKey(functionEntity.getKey());
            functionDTO.setFunctionName(functionEntity.getName());
            functionDTO.setMenuKey(functionEntity.getParentKey());
            functionDTO.setUrl(functionEntity.getUrl());
            functionDTO.setSort(functionEntity.getSort());
            functionList.add(functionDTO);
        }
        return ResponseDTO.succData(functionList);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> batchSaveFunctionList(ValidateList<PrivilegeFunctionDTO> functionList) {
        String menuKey = functionList.get(0).getMenuKey();
        PrivilegeEntity privilegeEntity = privilegeDao.selectByKey(menuKey);
        if (privilegeEntity == null) {
            return ResponseDTO.wrap(PrivilegeResponseCodeConst.MENU_NOT_EXIST);
        }

        List<String> functionKeyList = functionList.stream().map(PrivilegeFunctionDTO::getFunctionKey).collect(Collectors.toList());

        //数据库中存在的数据
        List<PrivilegeEntity> existFunctionList = privilegeDao.selectByKeyList(functionKeyList);
        //校验是否parent key重复
        boolean parentKeyExist = existFunctionList.stream().anyMatch(e -> !menuKey.equals(e.getParentKey()));
        if(parentKeyExist){
            return ResponseDTO.wrap(PrivilegeResponseCodeConst.ROUTER_KEY_NO_REPEAT);
        }

        existFunctionList = privilegeDao.selectByParentKey(menuKey);
        Map<String, PrivilegeEntity> privilegeEntityMap = existFunctionList.stream().collect(Collectors.toMap(PrivilegeEntity::getKey, e -> e));
        //如果没有，则保存全部
        if (existFunctionList.isEmpty()) {
            List<PrivilegeEntity> privilegeEntityList = functionList.stream().map(e -> function2Privilege(e)).collect(Collectors.toList());
            privilegeDao.batchInsert(privilegeEntityList);
            return ResponseDTO.succ();
        }

        Set<String> functionKeySet = functionList.stream().map(PrivilegeFunctionDTO::getFunctionKey).collect(Collectors.toSet());
        //删除的
        List<Long> deleteIdList = existFunctionList.stream().filter(e -> !functionKeySet.contains(e.getKey())).map(PrivilegeEntity::getId).collect(Collectors.toList());
        List<String> deleteKeyList = existFunctionList.stream().filter(e -> !functionKeySet.contains(e.getKey())).map(PrivilegeEntity::getKey).collect(Collectors.toList());
        if (!deleteIdList.isEmpty()) {
            privilegeDao.deleteBatchIds(deleteIdList);
            rolePrivilegeDao.deleteByPrivilegeKey(deleteKeyList);
        }

        //需要更新的
        ArrayList<PrivilegeEntity> batchUpdateList = Lists.newArrayList();
        for (PrivilegeFunctionDTO privilegeFunctionDTO : functionList) {
            PrivilegeEntity existPrivilege = privilegeEntityMap.get(privilegeFunctionDTO.getFunctionKey());
            if (existPrivilege != null) {
                existPrivilege.setSort(privilegeFunctionDTO.getSort());
                existPrivilege.setName(privilegeFunctionDTO.getFunctionName());
                batchUpdateList.add(existPrivilege);
            }
        }

        if (!batchUpdateList.isEmpty()) {
            privilegeDao.batchUpdate(batchUpdateList);
        }

        //新增的
        List<PrivilegeEntity> batchInsertList = functionList.stream()
                .filter(e -> !privilegeEntityMap.containsKey(e.getFunctionKey()))
                .map(e -> function2Privilege(e))
                .collect(Collectors.toList());

        if (!batchInsertList.isEmpty()) {
            privilegeDao.batchInsert(batchInsertList);
        }

        return ResponseDTO.succ();
    }

    private PrivilegeEntity function2Privilege(PrivilegeFunctionDTO privilegeFunction) {
        PrivilegeEntity privilegeEntity = new PrivilegeEntity();
        privilegeEntity.setKey(privilegeFunction.getFunctionKey());
        privilegeEntity.setName(privilegeFunction.getFunctionName());
        privilegeEntity.setParentKey(privilegeFunction.getMenuKey());
        privilegeEntity.setType(PrivilegeTypeEnum.POINTS.getValue());
        privilegeEntity.setSort(privilegeFunction.getSort());
        privilegeEntity.setUrl("");
        return privilegeEntity;
    }
}
