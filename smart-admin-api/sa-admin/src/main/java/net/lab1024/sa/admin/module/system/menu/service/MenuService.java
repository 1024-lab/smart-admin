package net.lab1024.sa.admin.module.system.menu.service;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Lists;
import net.lab1024.sa.admin.module.system.menu.constant.MenuTypeEnum;
import net.lab1024.sa.admin.module.system.menu.dao.MenuDao;
import net.lab1024.sa.admin.module.system.menu.domain.entity.MenuEntity;
import net.lab1024.sa.admin.module.system.menu.domain.form.MenuAddForm;
import net.lab1024.sa.admin.module.system.menu.domain.form.MenuBaseForm;
import net.lab1024.sa.admin.module.system.menu.domain.form.MenuUpdateForm;
import net.lab1024.sa.admin.module.system.menu.domain.vo.MenuTreeVO;
import net.lab1024.sa.admin.module.system.menu.domain.vo.MenuVO;
import net.lab1024.sa.common.common.code.SystemErrorCode;
import net.lab1024.sa.common.common.domain.RequestUrlVO;
import net.lab1024.sa.common.common.domain.ResponseDTO;
import net.lab1024.sa.common.common.util.SmartBeanUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 菜单
 *
 * @Author 1024创新实验室: 善逸
 * @Date 2022-03-08 22:15:09
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Service
public class MenuService {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private List<RequestUrlVO> authUrl;

    /**
     * 添加菜单
     *
     * @param menuAddForm
     * @return
     */
    public synchronized ResponseDTO<String> addMenu(MenuAddForm menuAddForm) {
        // 校验菜单名称
        if (this.validateMenuName(menuAddForm)) {
            return ResponseDTO.userErrorParam("菜单名称已存在");
        }
        // 校验前端权限字符串
        if (this.validateWebPerms(menuAddForm)) {
            return ResponseDTO.userErrorParam("权限字符串已存在");
        }
        MenuEntity menuEntity = SmartBeanUtil.copy(menuAddForm, MenuEntity.class);
        // 处理接口权限
        List<String> permsList = menuAddForm.getApiPermsList();
        if (!CollectionUtils.isEmpty(permsList)) {
            String perms = StringUtils.join(permsList, ",");
            menuEntity.setApiPerms(perms);
        }
        menuDao.insert(menuEntity);
        return ResponseDTO.ok();
    }

    /**
     * 更新菜单
     *
     * @param menuUpdateForm
     * @return
     */
    public synchronized ResponseDTO<String> updateMenu(MenuUpdateForm menuUpdateForm) {
        //校验菜单是否存在
        MenuEntity selectMenu = menuDao.selectById(menuUpdateForm.getMenuId());
        if (selectMenu == null) {
            return ResponseDTO.userErrorParam("菜单不存在");
        }
        if (selectMenu.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("菜单已被删除");
        }
        //校验菜单名称
        if (this.validateMenuName(menuUpdateForm)) {
            return ResponseDTO.userErrorParam("菜单名称已存在");
        }
        // 校验前端权限字符串
        if (this.validateWebPerms(menuUpdateForm)) {
            return ResponseDTO.userErrorParam("权限字符串已存在");
        }
        if (menuUpdateForm.getMenuId().equals(menuUpdateForm.getParentId())) {
            return ResponseDTO.userErrorParam("上级菜单不能为自己");
        }
        MenuEntity menuEntity = SmartBeanUtil.copy(menuUpdateForm, MenuEntity.class);
        // 处理接口权限
        List<String> permsList = menuUpdateForm.getApiPermsList();
        if (!CollectionUtils.isEmpty(permsList)) {
            String perms = StringUtils.join(permsList, ",");
            menuEntity.setApiPerms(perms);
        }
        menuDao.updateById(menuEntity);
        return ResponseDTO.ok();
    }


    /**
     * 批量删除菜单
     *
     * @param menuIdList
     * @param employeeId
     * @return
     */
    public synchronized ResponseDTO<String> batchDeleteMenu(List<Long> menuIdList, Long employeeId) {
        if (CollectionUtils.isEmpty(menuIdList)) {
            return ResponseDTO.userErrorParam("所选菜单不能为空");
        }
        menuDao.deleteByMenuIdList(menuIdList, employeeId, Boolean.TRUE);
        //孩子节点也需要删除
        this.recursiveDeleteChildren(menuIdList, employeeId);
        return ResponseDTO.ok();
    }

    private void recursiveDeleteChildren(List<Long> menuIdList, Long employeeId) {
        List<Long> childrenMenuIdList = menuDao.selectMenuIdByParentIdList(menuIdList);
        if (CollectionUtil.isEmpty(childrenMenuIdList)) {
            return;
        }
        menuDao.deleteByMenuIdList(childrenMenuIdList, employeeId, Boolean.TRUE);
        recursiveDeleteChildren(childrenMenuIdList, employeeId);
    }

    /**
     * 校验菜单名称
     *
     * @param menuDTO
     * @param <T>
     * @return true 重复 false 未重复
     */
    public <T extends MenuBaseForm> Boolean validateMenuName(T menuDTO) {
        MenuEntity menu = menuDao.getByMenuName(menuDTO.getMenuName(), menuDTO.getParentId(), Boolean.FALSE);
        if (menuDTO instanceof MenuAddForm) {
            return menu != null;
        }
        if (menuDTO instanceof MenuUpdateForm) {
            Long menuId = ((MenuUpdateForm) menuDTO).getMenuId();
            return menu != null && menu.getMenuId().longValue() != menuId.longValue();
        }
        return true;
    }

    /**
     * 校验前端权限字符串
     *
     * @param menuDTO
     * @param <T>
     * @return true 重复 false 未重复
     */
    public <T extends MenuBaseForm> Boolean validateWebPerms(T menuDTO) {
        MenuEntity menu = menuDao.getByWebPerms(menuDTO.getWebPerms(), Boolean.FALSE);
        if (menuDTO instanceof MenuAddForm) {
            return menu != null;
        }
        if (menuDTO instanceof MenuUpdateForm) {
            Long menuId = ((MenuUpdateForm) menuDTO).getMenuId();
            return menu != null && menu.getMenuId().longValue() != menuId.longValue();
        }
        return true;
    }

    /**
     * 查询菜单列表
     *
     * @return
     */
    public List<MenuVO> queryMenuList(Boolean disabledFlag) {
        List<MenuVO> menuVOList = menuDao.queryMenuList(Boolean.FALSE, disabledFlag, null);
        //根据ParentId进行分组
        Map<Long, List<MenuVO>> parentMap = menuVOList.stream().collect(Collectors.groupingBy(MenuVO::getParentId, Collectors.toList()));
        List<MenuVO> filterMenuVOList = this.filterNoParentMenu(parentMap, NumberUtils.LONG_ZERO);
        return filterMenuVOList;
    }

    /**
     * 过滤没有上级菜单的菜单列表
     *
     * @param parentMap
     * @param parentId
     * @return
     */
    private List<MenuVO> filterNoParentMenu(Map<Long, List<MenuVO>> parentMap, Long parentId) {
        // 获取本级菜单树List
        List<MenuVO> res = parentMap.getOrDefault(parentId, Lists.newArrayList());
        List<MenuVO> childMenu = Lists.newArrayList();
        // 循环遍历下级菜单
        res.forEach(e -> {
            //处理接口权限
            String perms = e.getApiPerms();
            if (StringUtils.isBlank(perms)) {
                e.setApiPermsList(Lists.newArrayList());
            } else {
                List<String> permsList = Lists.newArrayList(StringUtils.split(perms, ","));
                e.setApiPermsList(permsList);
            }
            List<MenuVO> menuList = this.filterNoParentMenu(parentMap, e.getMenuId());
            childMenu.addAll(menuList);
        });
        res.addAll(childMenu);
        return res;
    }

    /**
     * 查询菜单树
     *
     * @param onlyMenu 不查询功能点
     * @return
     */
    public ResponseDTO<List<MenuTreeVO>> queryMenuTree(Boolean onlyMenu) {
        List<Integer> menuTypeList = Lists.newArrayList();
        if (onlyMenu) {
            menuTypeList = Lists.newArrayList(MenuTypeEnum.CATALOG.getValue(), MenuTypeEnum.MENU.getValue());
        }
        List<MenuVO> menuVOList = menuDao.queryMenuList(Boolean.FALSE, null, menuTypeList);
        //根据ParentId进行分组
        Map<Long, List<MenuVO>> parentMap = menuVOList.stream().collect(Collectors.groupingBy(MenuVO::getParentId, Collectors.toList()));
        List<MenuTreeVO> menuTreeVOList = this.buildMenuTree(parentMap, NumberUtils.LONG_ZERO);
        return ResponseDTO.ok(menuTreeVOList);
    }

    /**
     * 构建菜单树
     *
     * @return
     */
    List<MenuTreeVO> buildMenuTree(Map<Long, List<MenuVO>> parentMap, Long parentId) {
        // 获取本级菜单树List
        List<MenuTreeVO> res = parentMap.getOrDefault(parentId, Lists.newArrayList()).stream()
                .map(e -> SmartBeanUtil.copy(e, MenuTreeVO.class)).collect(Collectors.toList());
        // 循环遍历下级菜单
        res.forEach(e -> {
            //处理接口权限
            String perms = e.getApiPerms();
            if (StringUtils.isBlank(perms)) {
                e.setApiPermsList(Lists.newArrayList());
            } else {
                List<String> permsList = Lists.newArrayList(StringUtils.split(perms, ","));
                e.setApiPermsList(permsList);
            }
            e.setChildren(this.buildMenuTree(parentMap, e.getMenuId()));
        });
        return res;
    }

    /**
     * 查询菜单详情
     *
     * @param menuId
     * @return
     */
    public ResponseDTO<MenuVO> getMenuDetail(Long menuId) {
        //校验菜单是否存在
        MenuEntity selectMenu = menuDao.selectById(menuId);
        if (selectMenu == null) {
            return ResponseDTO.error(SystemErrorCode.SYSTEM_ERROR, "菜单不存在");
        }
        if (selectMenu.getDeletedFlag()) {
            return ResponseDTO.error(SystemErrorCode.SYSTEM_ERROR, "菜单已被删除");
        }
        MenuVO menuVO = SmartBeanUtil.copy(selectMenu, MenuVO.class);
        //处理接口权限
        String perms = menuVO.getApiPerms();
        if (!StringUtils.isBlank(perms)) {
            List<String> permsList = Lists.newArrayList(StringUtils.split(perms, ","));
            menuVO.setApiPermsList(permsList);
        }
        return ResponseDTO.ok(menuVO);
    }

    /**
     * 获取系统所有请求路径
     *
     * @return
     */
    public ResponseDTO<List<RequestUrlVO>> getAuthUrl() {
        return ResponseDTO.ok(authUrl);
    }

}
