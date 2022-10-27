package net.lab1024.smartadmin.module.system.datascope.service;

import net.lab1024.smartadmin.common.constant.ResponseCodeConst;
import net.lab1024.smartadmin.common.domain.ResponseDTO;
import net.lab1024.smartadmin.module.system.datascope.DataScopeRoleDao;
import net.lab1024.smartadmin.module.system.datascope.constant.DataScopeTypeEnum;
import net.lab1024.smartadmin.module.system.datascope.constant.DataScopeViewTypeEnum;
import net.lab1024.smartadmin.module.system.datascope.domain.entity.DataScopeRoleEntity;
import net.lab1024.smartadmin.util.SmartBeanUtil;
import com.google.common.collect.Lists;
import net.lab1024.smartadmin.module.system.datascope.domain.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/4/27 0027 下午 14:52
 * @since JDK1.8
 */
@Service
public class DataScopeService {

    @Autowired
    private DataScopeRoleDao dataScopeRoleDao;

    /**
     * 获取所有可以进行数据范围配置的信息
     *
     * @return
     */
    public ResponseDTO<List<DataScopeAndViewTypeVO>> dataScopeList() {
        List<DataScopeDTO> dataScopeList = this.getDataScopeType();
        List<DataScopeAndViewTypeVO> dataScopeAndTypeList = SmartBeanUtil.copyList(dataScopeList, DataScopeAndViewTypeVO.class);
        List<DataScopeViewTypeVO> typeList = this.getViewType();
        dataScopeAndTypeList.forEach(e -> {
            e.setViewTypeList(typeList);
        });
        return ResponseDTO.succData(dataScopeAndTypeList);
    }

    /**
     * 获取当前系统存在的数据可见范围
     *
     * @return
     */
    public List<DataScopeViewTypeVO> getViewType() {
        List<DataScopeViewTypeVO> viewTypeList = Lists.newArrayList();
        DataScopeViewTypeEnum[] enums = DataScopeViewTypeEnum.class.getEnumConstants();
        DataScopeViewTypeVO dataScopeViewTypeDTO;
        for (DataScopeViewTypeEnum viewTypeEnum : enums) {
            dataScopeViewTypeDTO = DataScopeViewTypeVO.builder().viewType(viewTypeEnum.getValue()).viewTypeLevel(viewTypeEnum.getLevel()).viewTypeName(viewTypeEnum.getDesc()).build();
            viewTypeList.add(dataScopeViewTypeDTO);
        }
        Comparator<DataScopeViewTypeVO> comparator = (h1, h2) -> h1.getViewTypeLevel().compareTo(h2.getViewTypeLevel());
        viewTypeList.sort(comparator);
        return viewTypeList;
    }

    public List<DataScopeDTO> getDataScopeType() {
        List<DataScopeDTO> dataScopeTypeList = Lists.newArrayList();
        DataScopeTypeEnum[] enums = DataScopeTypeEnum.class.getEnumConstants();
        DataScopeDTO dataScopeDTO;
        for (DataScopeTypeEnum typeEnum : enums) {
            dataScopeDTO =
                DataScopeDTO.builder().dataScopeType(typeEnum.getValue()).dataScopeTypeDesc(typeEnum.getDesc()).dataScopeTypeName(typeEnum.getName()).dataScopeTypeSort(typeEnum.getSort()).build();
            dataScopeTypeList.add(dataScopeDTO);
        }
        Comparator<DataScopeDTO> comparator = (h1, h2) -> h1.getDataScopeTypeSort().compareTo(h2.getDataScopeTypeSort());
        dataScopeTypeList.sort(comparator);
        return dataScopeTypeList;
    }

    /**
     * 获取某个角色的数据范围设置信息
     *
     * @param roleId
     * @return
     */
    public ResponseDTO<List<DataScopeSelectVO>> dataScopeListByRole(Long roleId) {

        List<DataScopeRoleEntity> dataScopeRoleEntityList = dataScopeRoleDao.listByRoleId(roleId);
        if (CollectionUtils.isEmpty(dataScopeRoleEntityList)) {
            return ResponseDTO.succData(Lists.newArrayList());
        }
        List<DataScopeSelectVO> dataScopeSelects = SmartBeanUtil.copyList(dataScopeRoleEntityList, DataScopeSelectVO.class);
        return ResponseDTO.succData(dataScopeSelects);
    }

    /**
     * 批量设置某个角色的数据范围设置信息
     *
     * @param batchSetRoleDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> dataScopeBatchSet(DataScopeBatchSetRoleDTO batchSetRoleDTO) {
        List<DataScopeBatchSetDTO> batchSetList = batchSetRoleDTO.getBatchSetList();
        if (CollectionUtils.isEmpty(batchSetList)) {
            return ResponseDTO.wrap(ResponseCodeConst.ERROR_PARAM, "缺少配置信息");
        }
        List<DataScopeRoleEntity> dataScopeRoleEntityList = SmartBeanUtil.copyList(batchSetList, DataScopeRoleEntity.class);
        dataScopeRoleEntityList.forEach(e -> e.setRoleId(batchSetRoleDTO.getRoleId()));
        dataScopeRoleDao.deleteByRoleId(batchSetRoleDTO.getRoleId());
        dataScopeRoleDao.batchInsert(dataScopeRoleEntityList);
        return ResponseDTO.succ();
    }

}
