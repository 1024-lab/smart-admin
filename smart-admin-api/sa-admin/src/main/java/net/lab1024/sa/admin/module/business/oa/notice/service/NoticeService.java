package net.lab1024.sa.admin.module.business.oa.notice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import net.lab1024.sa.admin.module.business.oa.notice.constant.NoticeVisibleRangeDataTypeEnum;
import net.lab1024.sa.admin.module.business.oa.notice.dao.NoticeDao;
import net.lab1024.sa.admin.module.business.oa.notice.domain.entity.NoticeEntity;
import net.lab1024.sa.admin.module.business.oa.notice.domain.form.NoticeAddForm;
import net.lab1024.sa.admin.module.business.oa.notice.domain.form.NoticeQueryForm;
import net.lab1024.sa.admin.module.business.oa.notice.domain.form.NoticeUpdateForm;
import net.lab1024.sa.admin.module.business.oa.notice.domain.form.NoticeVisibleRangeForm;
import net.lab1024.sa.admin.module.business.oa.notice.domain.vo.NoticeTypeVO;
import net.lab1024.sa.admin.module.business.oa.notice.domain.vo.NoticeUpdateFormVO;
import net.lab1024.sa.admin.module.business.oa.notice.domain.vo.NoticeVO;
import net.lab1024.sa.admin.module.business.oa.notice.domain.vo.NoticeVisibleRangeVO;
import net.lab1024.sa.admin.module.business.oa.notice.manager.NoticeManager;
import net.lab1024.sa.admin.module.system.department.dao.DepartmentDao;
import net.lab1024.sa.admin.module.system.department.domain.entity.DepartmentEntity;
import net.lab1024.sa.admin.module.system.department.domain.vo.DepartmentVO;
import net.lab1024.sa.admin.module.system.department.service.DepartmentService;
import net.lab1024.sa.admin.module.system.employee.dao.EmployeeDao;
import net.lab1024.sa.admin.module.system.employee.domain.entity.EmployeeEntity;
import net.lab1024.sa.base.common.constant.StringConst;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.module.support.datatracer.constant.DataTracerTypeEnum;
import net.lab1024.sa.base.module.support.datatracer.service.DataTracerService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 通知。公告 后台管理业务
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-08-12 21:40:39
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Service
public class NoticeService {

    @Resource
    private NoticeDao noticeDao;

    @Resource
    private NoticeManager noticeManager;

    @Resource
    private EmployeeDao employeeDao;

    @Resource
    private DepartmentDao departmentDao;

    @Resource
    private DepartmentService departmentService;

    @Resource
    private NoticeTypeService noticeTypeService;

    @Resource
    private DataTracerService dataTracerService;

    /**
     * 查询 通知、公告
     *
     */
    public PageResult<NoticeVO> query(NoticeQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<NoticeVO> list = noticeDao.query(page, queryForm);
        LocalDateTime now = LocalDateTime.now();
        list.forEach(e -> e.setPublishFlag(e.getPublishTime().isBefore(now)));
        return SmartPageUtil.convert2PageResult(page, list);
    }

    /**
     * 添加
     */
    public ResponseDTO<String> add(NoticeAddForm addForm) {
        // 校验并获取可见范围
        ResponseDTO<String> validate = this.checkAndBuildVisibleRange(addForm);
        if (!validate.getOk()) {
            return ResponseDTO.error(validate);
        }

        // build 资讯
        NoticeEntity noticeEntity = SmartBeanUtil.copy(addForm, NoticeEntity.class);
        // 发布时间：不是定时发布时 默认为 当前
        if (!addForm.getScheduledPublishFlag()) {
            noticeEntity.setPublishTime(LocalDateTime.now());
        }
        // 保存数据
        noticeManager.save(noticeEntity, addForm.getVisibleRangeList());
        return ResponseDTO.ok();
    }

    /**
     * 校验并返回可见范围
     *
     */
    private ResponseDTO<String> checkAndBuildVisibleRange(NoticeAddForm form) {
        // 校验资讯分类
        NoticeTypeVO noticeType = noticeTypeService.getByNoticeTypeId(form.getNoticeTypeId());
        if (noticeType == null) {
            return ResponseDTO.userErrorParam("分类不存在");
        }

        if (form.getAllVisibleFlag()) {
            return ResponseDTO.ok();
        }

        /*
         * 校验可见范围
         * 非全部可见时 校验选择的员工|部门
         */
        List<NoticeVisibleRangeForm> visibleRangeUpdateList = form.getVisibleRangeList();
        if (CollectionUtils.isEmpty(visibleRangeUpdateList)) {
            return ResponseDTO.userErrorParam("未设置可见范围");
        }

        // 校验可见范围-> 员工
        List<Long> employeeIdList = visibleRangeUpdateList.stream()
                .filter(e -> NoticeVisibleRangeDataTypeEnum.EMPLOYEE.equalsValue(e.getDataType()))
                .map(NoticeVisibleRangeForm::getDataId)
                .distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(employeeIdList)) {
            employeeIdList = employeeIdList.stream().distinct().collect(Collectors.toList());
            List<Long> dbEmployeeIdList = employeeDao.selectBatchIds(employeeIdList).stream().map(EmployeeEntity::getEmployeeId).collect(Collectors.toList());
            Collection<Long> subtract = CollectionUtils.subtract(employeeIdList, dbEmployeeIdList);
            if (!subtract.isEmpty()) {
                return ResponseDTO.userErrorParam("员工id不存在：" + subtract);
            }
        }

        // 校验可见范围-> 部门
        List<Long> deptIdList = visibleRangeUpdateList.stream()
                .filter(e -> NoticeVisibleRangeDataTypeEnum.DEPARTMENT.equalsValue(e.getDataType()))
                .map(NoticeVisibleRangeForm::getDataId)
                .distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(deptIdList)) {
            deptIdList = deptIdList.stream().distinct().collect(Collectors.toList());
            List<Long> dbDeptIdList = departmentDao.selectBatchIds(deptIdList).stream().map(DepartmentEntity::getDepartmentId).collect(Collectors.toList());
            Collection<Long> subtract = CollectionUtils.subtract(deptIdList, dbDeptIdList);
            if (!subtract.isEmpty()) {
                return ResponseDTO.userErrorParam("部门id不存在：" + subtract);
            }
        }
        return ResponseDTO.ok();
    }


    /**
     * 更新
     *
     */
    public ResponseDTO<String> update(NoticeUpdateForm updateForm) {

        NoticeEntity oldNoticeEntity = noticeDao.selectById(updateForm.getNoticeId());
        if (oldNoticeEntity == null) {
            return ResponseDTO.userErrorParam("通知不存在");
        }

        // 校验并获取可见范围
        ResponseDTO<String> res = this.checkAndBuildVisibleRange(updateForm);
        if (!res.getOk()) {
            return ResponseDTO.error(res);
        }

        // 更新
        NoticeEntity noticeEntity = SmartBeanUtil.copy(updateForm, NoticeEntity.class);
        noticeManager.update(oldNoticeEntity, noticeEntity, updateForm.getVisibleRangeList());
        return ResponseDTO.ok();
    }


    /**
     * 删除
     *
     */
    public ResponseDTO<String> delete(Long noticeId) {
        NoticeEntity noticeEntity = noticeDao.selectById(noticeId);
        if (null == noticeEntity || noticeEntity.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("通知公告不存在");
        }
        // 更新删除状态
        noticeDao.updateDeletedFlag(noticeId);
        dataTracerService.delete(noticeId, DataTracerTypeEnum.OA_NOTICE);
        return ResponseDTO.ok();
    }

    /**
     * 获取更新表单用的详情
     */
    public NoticeUpdateFormVO getUpdateFormVO(Long noticeId) {
        NoticeEntity noticeEntity = noticeDao.selectById(noticeId);
        if (null == noticeEntity) {
            return null;
        }

        NoticeUpdateFormVO updateFormVO = SmartBeanUtil.copy(noticeEntity, NoticeUpdateFormVO.class);
        if (!updateFormVO.getAllVisibleFlag()) {
            List<NoticeVisibleRangeVO> noticeVisibleRangeList = noticeDao.queryVisibleRange(noticeId);
            List<Long> employeeIdList = noticeVisibleRangeList.stream().filter(e -> NoticeVisibleRangeDataTypeEnum.EMPLOYEE.getValue().equals(e.getDataType()))
                    .map(NoticeVisibleRangeVO::getDataId)
                    .collect(Collectors.toList());

            Map<Long, EmployeeEntity> employeeMap = null;
            if (CollectionUtils.isNotEmpty(employeeIdList)) {
                employeeMap = employeeDao.selectBatchIds(employeeIdList).stream().collect(Collectors.toMap(EmployeeEntity::getEmployeeId, Function.identity()));
            } else {
                employeeMap = Maps.newHashMap();
            }
            for (NoticeVisibleRangeVO noticeVisibleRange : noticeVisibleRangeList) {
                if (noticeVisibleRange.getDataType().equals(NoticeVisibleRangeDataTypeEnum.EMPLOYEE.getValue())) {
                    EmployeeEntity employeeEntity = employeeMap.get(noticeVisibleRange.getDataId());
                    noticeVisibleRange.setDataName(employeeEntity == null ? StringConst.EMPTY : employeeEntity.getActualName());
                } else {
                    DepartmentVO departmentVO = departmentService.getDepartmentById(noticeVisibleRange.getDataId());
                    noticeVisibleRange.setDataName(departmentVO == null ? StringConst.EMPTY : departmentVO.getName());
                }
            }
            updateFormVO.setVisibleRangeList(noticeVisibleRangeList);
        }
        return updateFormVO;
    }
}
