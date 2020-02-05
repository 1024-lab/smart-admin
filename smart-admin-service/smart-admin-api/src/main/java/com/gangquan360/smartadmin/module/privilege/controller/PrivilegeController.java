package com.gangquan360.smartadmin.module.privilege.controller;

import com.gangquan360.smartadmin.common.anno.OperateLog;
import com.gangquan360.smartadmin.common.domain.ResponseDTO;
import com.gangquan360.smartadmin.common.domain.ValidateList;
import com.gangquan360.smartadmin.constant.SwaggerTagConst;
import com.gangquan360.smartadmin.module.privilege.domain.dto.*;
import com.gangquan360.smartadmin.module.privilege.service.PrivilegeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * [ 与员工权限相关：角色权限关系、权限列表 ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2019 1024lab.netInc. All rights reserved.
 * @date
 * @since JDK1.8
 */
@OperateLog
@RestController
@Api(tags = {SwaggerTagConst.Admin.MANAGER_PRIVILEGE})
public class PrivilegeController {

    @Autowired
    private PrivilegeService privilegeService;

    @GetMapping("/privilege/getAllUrl")
    @ApiOperation(value = "获取所有请求路径", notes = "获取所有请求路径")
    public ResponseDTO<List<PrivilegeRequestUrlVO>> getAllUrl() {
        return privilegeService.getPrivilegeUrlDTOList();
    }

    @ApiOperation(value = "菜单批量保存")
    @PostMapping("/privilege/menu/batchSaveMenu")
    public ResponseDTO<String> menuBatchSave(@Valid @RequestBody ValidateList<PrivilegeMenuDTO> menuList) {
        return privilegeService.menuBatchSave(menuList);
//        return ResponseDTO.succ();
    }


    @ApiOperation(value = "查询所有菜单项")
    @PostMapping("/privilege/menu/queryAll")
    public ResponseDTO<List<PrivilegeMenuVO>> queryAll() {
        return privilegeService.menuQueryAll();
    }


    @ApiOperation(value = "保存更新功能点")
    @PostMapping("/privilege/function/saveOrUpdate")
    public ResponseDTO<String> functionSaveOrUpdate(@Valid @RequestBody PrivilegeFunctionDTO privilegeFunctionDTO) {
        return privilegeService.functionSaveOrUpdate(privilegeFunctionDTO);
//        return ResponseDTO.succ();
    }

    @ApiOperation(value = "查询菜单功能点", notes = "更新")
    @PostMapping("/privilege/function/query/{menuKey}")
    public ResponseDTO<List<PrivilegeFunctionVO>> functionQuery(@PathVariable String menuKey) {
        return privilegeService.functionQuery(menuKey);
    }


}
