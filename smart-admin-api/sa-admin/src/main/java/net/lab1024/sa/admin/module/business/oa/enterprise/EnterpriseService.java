package net.lab1024.sa.admin.module.business.oa.enterprise;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.oa.enterprise.dao.EnterpriseDao;
import net.lab1024.sa.admin.module.business.oa.enterprise.dao.EnterpriseEmployeeDao;
import net.lab1024.sa.admin.module.business.oa.enterprise.domain.entity.EnterpriseEmployeeEntity;
import net.lab1024.sa.admin.module.business.oa.enterprise.domain.entity.EnterpriseEntity;
import net.lab1024.sa.admin.module.business.oa.enterprise.domain.form.*;
import net.lab1024.sa.admin.module.business.oa.enterprise.domain.vo.EnterpriseEmployeeVO;
import net.lab1024.sa.admin.module.business.oa.enterprise.domain.vo.EnterpriseListVO;
import net.lab1024.sa.admin.module.business.oa.enterprise.domain.vo.EnterpriseVO;
import net.lab1024.sa.admin.module.system.department.service.DepartmentService;
import net.lab1024.sa.common.common.code.UserErrorCode;
import net.lab1024.sa.common.common.domain.PageResult;
import net.lab1024.sa.common.common.domain.ResponseDTO;
import net.lab1024.sa.common.common.util.SmartBeanUtil;
import net.lab1024.sa.common.common.util.SmartPageUtil;
import net.lab1024.sa.common.module.support.datatracer.constant.DataTracerTypeEnum;
import net.lab1024.sa.common.module.support.datatracer.domain.form.DataTracerForm;
import net.lab1024.sa.common.module.support.datatracer.service.DataTracerService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 企业
 *
 * @Author 1024创新实验室: 开云
 * @Date 2022/7/28 20:37:15
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ），2012-2022
 */
@Service
@Slf4j
public class EnterpriseService {

    @Autowired
    private EnterpriseDao enterpriseDao;

    @Autowired
    private EnterpriseEmployeeDao enterpriseEmployeeDao;

    @Autowired
    private EnterpriseEmployeeManager enterpriseEmployeeManager;

    @Autowired
    private DataTracerService dataTracerService;

    @Autowired
    private DepartmentService departmentService;

    /**
     * 分页查询企业模块
     *
     * @param queryDTO
     * @return
     */
    public ResponseDTO<PageResult<EnterpriseVO>> queryByPage(EnterpriseQueryForm queryDTO) {
        queryDTO.setDeletedFlag(Boolean.FALSE);
        Page<?> page = SmartPageUtil.convert2PageQuery(queryDTO);
        List<EnterpriseVO> enterpriseVOS = enterpriseDao.queryPage(page, queryDTO);
        PageResult<EnterpriseVO> pageResult = SmartPageUtil.convert2PageResult(page, enterpriseVOS);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 查询企业详情
     *
     * @param enterpriseId
     * @return
     */
    public EnterpriseVO getDetail(Long enterpriseId) {
        EnterpriseVO enterpriseDetail = enterpriseDao.getDetail(enterpriseId, Boolean.FALSE);
        return enterpriseDetail;
    }

    /**
     * 新建企业
     *
     * @param createVO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> createEnterprise(EnterpriseCreateForm createVO) {
        // 验证企业名称是否重复
        EnterpriseEntity validateEnterprise = enterpriseDao.queryByEnterpriseName(createVO.getEnterpriseName(), null, Boolean.FALSE);
        if (Objects.nonNull(validateEnterprise)) {
            return ResponseDTO.userErrorParam("企业名称重复");
        }
        // 数据插入
        EnterpriseEntity insertEnterprise = SmartBeanUtil.copy(createVO, EnterpriseEntity.class);
        enterpriseDao.insert(insertEnterprise);
        dataTracerService.insert(insertEnterprise.getEnterpriseId(), DataTracerTypeEnum.OA_ENTERPRISE);
        return ResponseDTO.ok();
    }

    /**
     * 编辑企业
     *
     * @param updateVO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> updateEnterprise(EnterpriseUpdateForm updateVO) {
        Long enterpriseId = updateVO.getEnterpriseId();
        // 校验企业是否存在
        EnterpriseEntity enterpriseDetail = enterpriseDao.selectById(enterpriseId);
        if (Objects.isNull(enterpriseDetail) || enterpriseDetail.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("企业不存在");
        }
        // 验证企业名称是否重复
        EnterpriseEntity validateEnterprise = enterpriseDao.queryByEnterpriseName(updateVO.getEnterpriseName(), enterpriseId, Boolean.FALSE);
        if (Objects.nonNull(validateEnterprise)) {
            return ResponseDTO.userErrorParam("企业名称重复");
        }
        // 数据编辑
        EnterpriseEntity updateEntity = SmartBeanUtil.copy(enterpriseDetail, EnterpriseEntity.class);
        SmartBeanUtil.copyProperties(updateVO, updateEntity);
        enterpriseDao.updateById(updateEntity);

        //变更记录
        DataTracerForm dataTracerForm = DataTracerForm.builder()
                .dataId(updateVO.getEnterpriseId())
                .type(DataTracerTypeEnum.OA_ENTERPRISE)
                .content("修改企业信息")
                .diffOld(dataTracerService.getChangeContent(enterpriseDetail))
                .diffNew(dataTracerService.getChangeContent(updateEntity))
                .build();

        dataTracerService.addTrace(dataTracerForm);
        return ResponseDTO.ok();
    }


    /**
     * 删除企业
     *
     * @param enterpriseId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> deleteEnterprise(Long enterpriseId) {
        // 校验企业是否存在
        EnterpriseEntity enterpriseDetail = enterpriseDao.selectById(enterpriseId);
        if (Objects.isNull(enterpriseDetail) || enterpriseDetail.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("企业不存在");
        }
        enterpriseDao.deleteEnterprise(enterpriseId, Boolean.TRUE);
        dataTracerService.delete(enterpriseId, DataTracerTypeEnum.OA_ENTERPRISE);
        return ResponseDTO.ok();
    }

    /**
     * 企业列表查询
     *
     * @return
     */
    public ResponseDTO<List<EnterpriseListVO>> queryList(Integer type) {
        List<EnterpriseListVO> enterpriseListVOS = enterpriseDao.queryList(type, Boolean.FALSE, Boolean.FALSE);
        return ResponseDTO.ok(enterpriseListVOS);
    }

    //----------------------------------------- 以下为员工相关--------------------------------------------

    /**
     * 企业添加员工
     *
     * @param enterpriseEmployeeForm
     * @return
     */
    public synchronized ResponseDTO<String> addEmployee(EnterpriseEmployeeForm enterpriseEmployeeForm) {
        Long enterpriseId = enterpriseEmployeeForm.getEnterpriseId();
        EnterpriseEntity enterpriseEntity = enterpriseDao.selectById(enterpriseId);
        if (enterpriseEntity == null || enterpriseEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        //过滤掉已存在的员工
        List<Long> waitAddEmployeeIdList = enterpriseEmployeeForm.getEmployeeIdList();
        List<EnterpriseEmployeeEntity> enterpriseEmployeeEntityList = enterpriseEmployeeDao.selectByEnterpriseAndEmployeeIdList(enterpriseId, waitAddEmployeeIdList);
        if (CollectionUtils.isNotEmpty(enterpriseEmployeeEntityList)) {
            List<Long> existEmployeeIdList = enterpriseEmployeeEntityList.stream().map(EnterpriseEmployeeEntity::getEmployeeId).collect(Collectors.toList());
            waitAddEmployeeIdList = waitAddEmployeeIdList.stream().filter(e -> !existEmployeeIdList.contains(e)).collect(Collectors.toList());
        }
        if (CollectionUtils.isEmpty(waitAddEmployeeIdList)) {
            return ResponseDTO.ok();
        }
        List<EnterpriseEmployeeEntity> batchAddList = Lists.newArrayList();
        for (Long employeeId : waitAddEmployeeIdList) {
            EnterpriseEmployeeEntity enterpriseEmployeeEntity = new EnterpriseEmployeeEntity();
            enterpriseEmployeeEntity.setEnterpriseId(enterpriseId);
            enterpriseEmployeeEntity.setEmployeeId(employeeId);
            batchAddList.add(enterpriseEmployeeEntity);
        }
        enterpriseEmployeeManager.saveBatch(batchAddList);
        return ResponseDTO.ok();
    }

    /**
     * 企业删除员工
     *
     * @param enterpriseEmployeeForm
     * @return
     */
    public synchronized ResponseDTO<String> deleteEmployee(EnterpriseEmployeeForm enterpriseEmployeeForm) {
        Long enterpriseId = enterpriseEmployeeForm.getEnterpriseId();
        EnterpriseEntity enterpriseEntity = enterpriseDao.selectById(enterpriseId);
        if (enterpriseEntity == null || enterpriseEntity.getDeletedFlag()) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        List<Long> waitDeleteEmployeeIdList = enterpriseEmployeeForm.getEmployeeIdList();
        enterpriseEmployeeDao.deleteByEnterpriseAndEmployeeIdList(enterpriseId, waitDeleteEmployeeIdList);
        return ResponseDTO.ok();
    }

    /**
     * 企业下员工列表
     *
     * @param enterpriseIdList
     * @return
     */
    public List<EnterpriseEmployeeVO> employeeList(List<Long> enterpriseIdList) {
        if (CollectionUtils.isEmpty(enterpriseIdList)) {
            return Lists.newArrayList();
        }
        List<EnterpriseEmployeeVO> enterpriseEmployeeVOList = enterpriseEmployeeDao.selectByEnterpriseIdList(enterpriseIdList);
        return enterpriseEmployeeVOList;
    }

    /**
     * 分页查询企业员工
     *
     * @param queryForm
     * @return
     */
    public PageResult<EnterpriseEmployeeVO> queryPageEmployeeList(EnterpriseEmployeeQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<EnterpriseEmployeeVO> enterpriseEmployeeVOList = enterpriseEmployeeDao.queryPageEmployeeList(page, queryForm);
        for (EnterpriseEmployeeVO enterpriseEmployeeVO : enterpriseEmployeeVOList) {
            enterpriseEmployeeVO.setDepartmentName(departmentService.getDepartmentPath(enterpriseEmployeeVO.getDepartmentId()));
        }
        return SmartPageUtil.convert2PageResult(page, enterpriseEmployeeVOList);
    }
}
