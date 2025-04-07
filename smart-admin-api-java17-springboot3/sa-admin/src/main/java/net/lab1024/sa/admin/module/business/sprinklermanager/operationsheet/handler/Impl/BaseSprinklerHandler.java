package net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.handler.Impl;

import jakarta.annotation.Resource;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.dao.BaseDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.entity.BaseEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.form.BaseForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.handler.SprinklerOperationHandler;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.dao.SprinklerDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerEntity;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.module.support.datatracer.constant.DataTracerConst;
import net.lab1024.sa.base.module.support.datatracer.constant.DataTracerTypeEnum;
import net.lab1024.sa.base.module.support.datatracer.service.DataTracerService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Component
public abstract class BaseSprinklerHandler<F extends BaseForm, E extends BaseEntity> implements SprinklerOperationHandler<F> {

    @Resource
    protected DataTracerService dataTracerService;

    @Resource
    private SprinklerDao sprinklerDao;

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> createOperationSheet(F form) {
        // 基础校验
        Boolean sprinklerCheck = validateSprinkler(form.getId());
        if (sprinklerCheck) {
            return ResponseDTO.userErrorParam("喷头不存在");
        }

        // 业务校验
        Boolean bizCheck = validateBusiness(form);
        if (bizCheck) {
            return ResponseDTO.userErrorParam("信息Id重复");
        }

        // 数据转换
        E entity = convertToEntity(form);

        // 数据持久化
        getDao().insert(entity);

        // 添加数据追踪
        addDataTrace(form.getId(), entity);

        return ResponseDTO.ok();
    }


    protected abstract Boolean validateBusiness(F form);

    /**
     * 获取对应DAO（抽象方法）
     */
    protected abstract BaseDao<E> getDao();

    /**
     * 实体转换（通用实现）
     */
    protected E convertToEntity(F form) {
        return SmartBeanUtil.copy(form, getEntityClass());
    }

    /**
     * 获取实体类型（用于Bean拷贝）
     */
    protected abstract Class<E> getEntityClass();
    /**
     * 添加数据追踪
     */
    protected void addDataTrace(Long sprinklerId, E entity) {
        String traceContent = "新增" + getBizType() + ":" + DataTracerConst.HTML_BR
                + dataTracerService.getChangeContent(entity);
        dataTracerService.addTrace(sprinklerId, DataTracerTypeEnum.SPRINKLER, traceContent);
    }

    /**
     * 获取业务类型（用于追踪记录）
     */
    protected abstract String getBizType();
    private Boolean validateSprinkler(Long sprinklerId) {
        SprinklerEntity sprinklerDetail = sprinklerDao.selectById(sprinklerId);
        return Objects.isNull(sprinklerDetail) || sprinklerDetail.getDeletedFlag();
    }






}
