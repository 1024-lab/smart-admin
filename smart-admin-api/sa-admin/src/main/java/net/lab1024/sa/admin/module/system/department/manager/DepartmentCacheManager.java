package net.lab1024.sa.admin.module.system.department.manager;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.constant.AdminCacheConst;
import net.lab1024.sa.admin.module.system.department.dao.DepartmentDao;
import net.lab1024.sa.admin.module.system.department.domain.vo.DepartmentTreeVO;
import net.lab1024.sa.admin.module.system.department.domain.vo.DepartmentVO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 部门 缓存相关
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-01-12 20:37:48
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@Service
public class DepartmentCacheManager {

    @Resource
    private DepartmentDao departmentDao;

    private void logClearInfo(String cache) {
        log.info("clear " + cache);
    }

    @CacheEvict(value = {AdminCacheConst.Department.DEPARTMENT_LIST_CACHE, AdminCacheConst.Department.DEPARTMENT_MAP_CACHE, AdminCacheConst.Department.DEPARTMENT_SELF_CHILDREN_CACHE, AdminCacheConst.Department.DEPARTMENT_TREE_CACHE, AdminCacheConst.Department.DEPARTMENT_PATH_CACHE,}, allEntries = true)
    public void clearCache() {
        logClearInfo(AdminCacheConst.Department.DEPARTMENT_LIST_CACHE);
    }


    /**
     * 部门列表
     */
    @Cacheable(AdminCacheConst.Department.DEPARTMENT_LIST_CACHE)
    public List<DepartmentVO> getDepartmentList() {
        return departmentDao.listAll();
    }

    /**
     * 部门map
     *
     */
    @Cacheable(AdminCacheConst.Department.DEPARTMENT_MAP_CACHE)
    public Map<Long, DepartmentVO> getDepartmentMap() {
        return departmentDao.listAll().stream().collect(Collectors.toMap(DepartmentVO::getDepartmentId, Function.identity()));
    }


    /**
     * 缓存部门树结构
     *
     */
    @Cacheable(AdminCacheConst.Department.DEPARTMENT_TREE_CACHE)
    public List<DepartmentTreeVO> getDepartmentTree() {
        List<DepartmentVO> departmentVOList = departmentDao.listAll();
        return this.buildTree(departmentVOList);
    }

    /**
     * 缓存某个部门的下级id列表
     *
     */
    @Cacheable(AdminCacheConst.Department.DEPARTMENT_SELF_CHILDREN_CACHE)
    public List<Long> getDepartmentSelfAndChildren(Long departmentId) {
        List<DepartmentVO> departmentVOList = departmentDao.listAll();
        return this.selfAndChildrenIdList(departmentId, departmentVOList);
    }


    /**
     * 部门的路径名称
     *
     */
    @Cacheable(AdminCacheConst.Department.DEPARTMENT_PATH_CACHE)
    public Map<Long, String> getDepartmentPathMap() {
        List<DepartmentVO> departmentVOList = departmentDao.listAll();
        Map<Long, DepartmentVO> departmentMap = departmentVOList.stream().collect(Collectors.toMap(DepartmentVO::getDepartmentId, Function.identity()));

        Map<Long, String> pathNameMap = Maps.newHashMap();
        for (DepartmentVO departmentVO : departmentVOList) {
            String pathName = this.buildDepartmentPath(departmentVO, departmentMap);
            pathNameMap.put(departmentVO.getDepartmentId(), pathName);
        }

        return pathNameMap;
    }

    /**
     * 构建父级考点路径
     */
    private String buildDepartmentPath(DepartmentVO departmentVO, Map<Long, DepartmentVO> departmentMap) {
        if (Objects.equals(departmentVO.getParentId(), NumberUtils.LONG_ZERO)) {
            return departmentVO.getName();
        }
        //父节点
        DepartmentVO parentDepartment = departmentMap.get(departmentVO.getParentId());
        if (parentDepartment == null) {
            return departmentVO.getName();
        }
        String pathName = buildDepartmentPath(parentDepartment, departmentMap);
        return pathName + "/" + departmentVO.getName();

    }
    // ---------------------- 构造树的一些方法 ------------------------------

    /**
     * 构建部门树结构
     *
     */
    public List<DepartmentTreeVO> buildTree(List<DepartmentVO> voList) {
        if (CollectionUtils.isEmpty(voList)) {
            return Lists.newArrayList();
        }
        List<DepartmentVO> rootList = voList.stream().filter(e -> e.getParentId() == null || Objects.equals(e.getParentId(), NumberUtils.LONG_ZERO)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(rootList)) {
            return Lists.newArrayList();
        }
        List<DepartmentTreeVO> treeVOList = SmartBeanUtil.copyList(rootList, DepartmentTreeVO.class);
        this.recursiveBuildTree(treeVOList, voList);
        return treeVOList;
    }

     /**
     * 构建所有根节点的下级树形结构
     * 返回值为层序遍历结果 
     * [由于departmentDao中listAll给出数据根据Sort降序 所以同一层中Sort值较大的优先遍历]
     */
    private List<Long> recursiveBuildTree(List<DepartmentTreeVO> nodeList, List<DepartmentVO> allDepartmentList) {
        int nodeSize = nodeList.size();
        List<Long> childIdList = new ArrayList<>();
        for(int i = 0; i < nodeSize; i++) {
            int preIndex = i - 1;
            int nextIndex = i + 1;
            DepartmentTreeVO node = nodeList.get(i);
            if (preIndex > -1) {
                node.setPreId(nodeList.get(preIndex).getDepartmentId());
            }
            if (nextIndex < nodeSize) {
                node.setNextId(nodeList.get(nextIndex).getDepartmentId());
            }

            List<DepartmentTreeVO> children = getChildren(node.getDepartmentId(), allDepartmentList);
            
            List<Long> tempChildIdList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(children)) {
                node.setChildren(children);
                tempChildIdList = this.recursiveBuildTree(children, allDepartmentList);
            }

            if(CollectionUtils.isEmpty(node.getSelfAndAllChildrenIdList())) {
                node.setSelfAndAllChildrenIdList(
                        new ArrayList<>()
                );
            }
            node.getSelfAndAllChildrenIdList().add(node.getDepartmentId());
            
            if(CollectionUtils.isNotEmpty(tempChildIdList)) {
                node.getSelfAndAllChildrenIdList().addAll(tempChildIdList);
                childIdList.addAll(tempChildIdList);
            }
            
        }
        
        // 保证本层遍历顺序
        for(int i = nodeSize - 1; i >= 0; i--) {
            childIdList.add(0, nodeList.get(i).getDepartmentId());
        }
        
        return childIdList;
    }


    /**
     * 获取子元素
     *
     */
    private List<DepartmentTreeVO> getChildren(Long departmentId, List<DepartmentVO> voList) {
        List<DepartmentVO> childrenEntityList = voList.stream().filter(e -> departmentId.equals(e.getParentId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(childrenEntityList)) {
            return Lists.newArrayList();
        }
        return SmartBeanUtil.copyList(childrenEntityList, DepartmentTreeVO.class);
    }


    /**
     * 通过部门id,获取当前以及下属部门
     *
     */
    public List<Long> selfAndChildrenIdList(Long departmentId, List<DepartmentVO> voList) {
        List<Long> selfAndChildrenIdList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(voList)) {
            return selfAndChildrenIdList;
        }
        selfAndChildrenIdList.add(departmentId);
        List<DepartmentTreeVO> children = this.getChildren(departmentId, voList);
        if (CollectionUtils.isEmpty(children)) {
            return selfAndChildrenIdList;
        }
        List<Long> childrenIdList = children.stream().map(DepartmentTreeVO::getDepartmentId).collect(Collectors.toList());
        selfAndChildrenIdList.addAll(childrenIdList);
        for (Long childId : childrenIdList) {
            this.selfAndChildrenRecursion(selfAndChildrenIdList, childId, voList);
        }
        return selfAndChildrenIdList;
    }

    /**
     * 递归查询
     */
    public void selfAndChildrenRecursion(List<Long> selfAndChildrenIdList, Long departmentId, List<DepartmentVO> voList) {
        List<DepartmentTreeVO> children = this.getChildren(departmentId, voList);
        if (CollectionUtils.isEmpty(children)) {
            return;
        }
        List<Long> childrenIdList = children.stream().map(DepartmentTreeVO::getDepartmentId).collect(Collectors.toList());
        selfAndChildrenIdList.addAll(childrenIdList);
        for (Long childId : childrenIdList) {
            this.selfAndChildrenRecursion(selfAndChildrenIdList, childId, voList);
        }
    }
}
