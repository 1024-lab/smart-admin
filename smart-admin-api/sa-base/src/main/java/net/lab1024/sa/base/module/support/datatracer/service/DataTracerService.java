package net.lab1024.sa.base.module.support.datatracer.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartIpUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import net.lab1024.sa.base.module.support.datatracer.constant.DataTracerConst;
import net.lab1024.sa.base.module.support.datatracer.constant.DataTracerTypeEnum;
import net.lab1024.sa.base.module.support.datatracer.dao.DataTracerDao;
import net.lab1024.sa.base.module.support.datatracer.domain.entity.DataTracerEntity;
import net.lab1024.sa.base.module.support.datatracer.domain.form.DataTracerForm;
import net.lab1024.sa.base.module.support.datatracer.domain.form.DataTracerQueryForm;
import net.lab1024.sa.base.module.support.datatracer.domain.vo.DataTracerVO;
import net.lab1024.sa.base.module.support.datatracer.manager.DataTracerManger;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据变动记录 Service
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-07-23 19:38:52
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@Service
public class DataTracerService {

    @Resource
    private DataTracerDao dataTracerDao;

    @Resource
    private DataTracerManger dataTracerManger;

    @Resource
    private DataTracerChangeContentService dataTracerChangeContentService;

    /**
     * 获取变更内容
     *
     * @param object
     * @return
     */
    public String getChangeContent(Object object) {
        return dataTracerChangeContentService.getChangeContent(object);
    }

    /**
     * 获取变更内容
     */
    public String getChangeContent(Object oldObject, Object newObject) {
        return dataTracerChangeContentService.getChangeContent(oldObject, newObject);
    }


    /**
     * 获取变更内容
     */
    public <T> String getChangeContent(List<T> oldObjectList, List<T> newObjectList) {
        return dataTracerChangeContentService.getChangeContent(oldObjectList, newObjectList);
    }


    /**
     * 保存【修改】数据变动记录
     *
     * @param dataId
     * @param type
     */
    public void update(Long dataId, DataTracerTypeEnum type, Object oldObject, Object newObject) {
        DataTracerForm form = DataTracerForm.builder()
                .dataId(dataId)
                .type(type)
                .content(dataTracerChangeContentService.getChangeContent(oldObject, newObject))
                .build();
        this.addTrace(form);
    }


    /**
     * 保存【新增】数据变动记录
     *
     * @param dataId
     * @param type
     */
    public void insert(Long dataId, DataTracerTypeEnum type) {
        DataTracerForm form = DataTracerForm.builder().dataId(dataId).type(type).content(DataTracerConst.INSERT).build();
        this.addTrace(form);
    }

    /**
     * 保存【删除】数据变动记录
     *
     * @param dataId
     * @param type
     */
    public void delete(Long dataId, DataTracerTypeEnum type) {
        DataTracerForm form = DataTracerForm.builder().dataId(dataId).type(type).content(DataTracerConst.DELETE).build();
        this.addTrace(form);
    }

    /**
     * 保存【删除】数据变动记录
     *
     * @param dataId
     * @param type
     */
    public void delete(Long dataId, DataTracerTypeEnum type, Object object) {
        DataTracerForm form = DataTracerForm.builder().dataId(dataId).type(type).content(DataTracerConst.DELETE).build();
        this.addTrace(form);
    }

    /**
     * 保存【批量删除】数据变动记录
     *
     * @param dataIdList
     * @param type
     */
    public void batchDelete(List<Long> dataIdList, DataTracerTypeEnum type) {
        if (CollectionUtils.isEmpty(dataIdList)) {
            return;
        }

        this.addTraceList(dataIdList.stream().map(e -> DataTracerForm.builder()
                        .dataId(e)
                        .type(type)
                        .content(DataTracerConst.DELETE)
                        .build())
                .collect(Collectors.toList())
        );
    }

    /**
     * 保存数据变动记录
     *
     * @param dataId
     * @param type
     * @param content
     */
    public void addTrace(Long dataId, DataTracerTypeEnum type, String content) {
        DataTracerForm form = DataTracerForm.builder().dataId(dataId).type(type).content(content).build();
        this.addTrace(form);
    }

    /**
     * 保存数据变动记录
     */
    public void addTrace(DataTracerForm tracerForm) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        this.addTrace(tracerForm, requestUser);
    }


    /**
     * 保存数据变动记录
     */
    public void addTrace(DataTracerForm tracerForm, RequestUser requestUser) {
        DataTracerEntity tracerEntity = SmartBeanUtil.copy(tracerForm, DataTracerEntity.class);
        tracerEntity.setType(tracerForm.getType().getValue());
        if (requestUser != null) {
            tracerEntity.setIp(requestUser.getIp());
            tracerEntity.setIpRegion(SmartIpUtil.getRegion(requestUser.getIp()));
            tracerEntity.setUserAgent(requestUser.getUserAgent());
            tracerEntity.setUserId(requestUser.getUserId());
            tracerEntity.setUserType(requestUser.getUserType().getValue());
            tracerEntity.setUserName(requestUser.getUserName());
        }
        dataTracerManger.save(tracerEntity);
    }

    /**
     * 批量保存数据变动记录
     */
    public void addTraceList(List<DataTracerForm> tracerFormList) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        this.addTraceList(tracerFormList, requestUser);
    }

    /**
     * 批量保存数据变动记录
     */
    public void addTraceList(List<DataTracerForm> tracerFormList, RequestUser requestUser) {
        if (CollectionUtils.isEmpty(tracerFormList)) {
            return;
        }

        List<DataTracerEntity> tracerEntityList = tracerFormList.stream().map(e -> {
            DataTracerEntity tracerEntity = SmartBeanUtil.copy(e, DataTracerEntity.class);
            tracerEntity.setType(e.getType().getValue());
            tracerEntity.setIp(requestUser.getIp());
            tracerEntity.setIpRegion(SmartIpUtil.getRegion(requestUser.getIp()));
            tracerEntity.setUserAgent(requestUser.getUserAgent());
            tracerEntity.setUserId(requestUser.getUserId());
            tracerEntity.setUserType(requestUser.getUserType().getValue());
            tracerEntity.setUserName(requestUser.getUserName());
            return tracerEntity;
        }).collect(Collectors.toList());
        dataTracerManger.saveBatch(tracerEntityList);
    }


    /**
     * 分页查询
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<DataTracerVO>> query(DataTracerQueryForm queryForm) {
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<DataTracerVO> list = dataTracerDao.query(page, queryForm);
        PageResult<DataTracerVO> pageResult = SmartPageUtil.convert2PageResult(page, list);
        return ResponseDTO.ok(pageResult);
    }

}
