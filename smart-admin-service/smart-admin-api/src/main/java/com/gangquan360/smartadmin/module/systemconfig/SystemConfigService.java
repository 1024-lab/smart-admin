package com.gangquan360.smartadmin.module.systemconfig;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.gangquan360.smartadmin.common.constant.JudgeEnum;
import com.gangquan360.smartadmin.common.constant.ResponseCodeConst;
import com.gangquan360.smartadmin.common.domain.PageResultDTO;
import com.gangquan360.smartadmin.common.domain.ResponseDTO;
import com.gangquan360.smartadmin.common.exception.SmartBusinessException;
import com.gangquan360.smartadmin.common.reload.annotation.SmartReload;
import com.gangquan360.smartadmin.constant.SmartReloadTagConst;
import com.gangquan360.smartadmin.module.smartreload.SmartReloadService;
import com.gangquan360.smartadmin.module.systemconfig.constant.SystemConfigDataType;
import com.gangquan360.smartadmin.module.systemconfig.constant.SystemConfigEnum;
import com.gangquan360.smartadmin.module.systemconfig.constant.SystemConfigResponseCodeConst;
import com.gangquan360.smartadmin.module.systemconfig.domain.dto.*;
import com.gangquan360.smartadmin.module.systemconfig.domain.entity.SystemConfigEntity;
import com.gangquan360.smartadmin.util.SmartBeanUtil;
import com.gangquan360.smartadmin.util.SmartPaginationUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * 系统配置业务类
 *
 * @author GHQ
 * @date 2017-12-23 15:09
 */
@Slf4j
@Service
public class SystemConfigService {

    /**
     * 系统配置缓存
     */
    private ConcurrentHashMap<String, SystemConfigEntity> systemConfigMap = new ConcurrentHashMap<>();

    @Autowired
    private SystemConfigDao systemConfigDao;

    @Autowired
    private SmartReloadService smartReloadService;

    /**
     * 初始化系统设置缓存
     */
    @PostConstruct
    private void initSystemConfigCache() {
        List<SystemConfigEntity> entityList = systemConfigDao.selectSystemSettingList();
        if (CollectionUtils.isEmpty(entityList)) {
            return;
        }

        systemConfigMap.clear();
        entityList.forEach(entity -> this.systemConfigMap.put(entity.getConfigKey().toLowerCase(), entity));
        log.info("系统设置缓存初始化完毕:{}", systemConfigMap.size());

        smartReloadService.register(this);
    }

    @SmartReload(SmartReloadTagConst.SYSTEM_CONFIG)
    public boolean reload(String args) {
        this.initSystemConfigCache();
        log.warn("<<SmartReload>> <<{}>> , args {} reload success ", SmartReloadTagConst.SYSTEM_CONFIG, args);
        return true;
    }

    /**
     * 分页获取系统配置
     *
     * @param queryDTO
     * @return
     */
    public ResponseDTO<PageResultDTO<SystemConfigVO>> getSystemConfigPage(SystemConfigQueryDTO queryDTO) {
        Page page = SmartPaginationUtil.convert2PageQueryInfo(queryDTO);
        List<SystemConfigEntity> entityList = systemConfigDao.selectSystemSettingList(page, queryDTO);
        PageResultDTO<SystemConfigVO> pageResultDTO = SmartPaginationUtil.convert2PageResultDTO(page, entityList, SystemConfigVO.class);
        return ResponseDTO.succData(pageResultDTO);
    }

    /**
     * 根据参数key获得一条数据（数据库）
     *
     * @param configKey
     * @return
     */
    public ResponseDTO<SystemConfigVO> selectByKey(String configKey) {
        SystemConfigEntity entity = systemConfigDao.getByKey(configKey);
        if (entity == null) {
            return ResponseDTO.wrap(SystemConfigResponseCodeConst.NOT_EXIST);
        }
        SystemConfigVO configDTO = SmartBeanUtil.copy(entity, SystemConfigVO.class);
        return ResponseDTO.succData(configDTO);
    }

    /**
     * 根据参数key获得一条数据 并转换为 对象
     *
     * @param configKey
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T selectByKey2Obj(String configKey, Class<T> clazz) {
        SystemConfigEntity entity = systemConfigDao.getByKey(configKey);
        if (entity == null) {
            return null;
        }
        SystemConfigDTO configDTO = SmartBeanUtil.copy(entity, SystemConfigDTO.class);
        String configValue = configDTO.getConfigValue();
        if (StringUtils.isEmpty(configValue)) {
            return null;
        }
        T obj = JSON.parseObject(configValue, clazz);
        return obj;
    }

    public SystemConfigDTO getCacheByKey(SystemConfigEnum.Key key) {
        SystemConfigEntity entity = this.systemConfigMap.get(key.name().toLowerCase());
        if (entity == null) {
            throw new SmartBusinessException("缺少系统配置[" + key.name() + "]");
        }
        return SmartBeanUtil.copy(entity, SystemConfigDTO.class);
    }

    /**
     * 添加系统配置
     *
     * @param configAddDTO
     * @return
     */
    public ResponseDTO<String> addSystemConfig(SystemConfigAddDTO configAddDTO) {
        SystemConfigEntity entity = systemConfigDao.getByKey(configAddDTO.getConfigKey());
        if (entity != null) {
            return ResponseDTO.wrap(SystemConfigResponseCodeConst.ALREADY_EXIST);
        }
        ResponseDTO valueValid = this.configValueValid(configAddDTO.getConfigKey(),configAddDTO.getConfigValue());
        if(!valueValid.isSuccess()){
            return valueValid;
        }
        SystemConfigEntity addEntity = SmartBeanUtil.copy(configAddDTO, SystemConfigEntity.class);
        addEntity.setIsUsing(JudgeEnum.YES.getValue());
        systemConfigDao.insert(addEntity);
        //刷新缓存
        this.initSystemConfigCache();
        return ResponseDTO.succ();
    }

    /**
     * 更新系统配置
     *
     * @param updateDTO
     * @return
     */
    public ResponseDTO<String> updateSystemConfig(SystemConfigUpdateDTO updateDTO) {
        SystemConfigEntity entity = systemConfigDao.selectById(updateDTO.getId());
        //系统配置不存在
        if (entity == null) {
            return ResponseDTO.wrap(SystemConfigResponseCodeConst.NOT_EXIST);
        }
        SystemConfigEntity alreadyEntity = systemConfigDao.getByKeyExcludeId(updateDTO.getConfigKey(), updateDTO.getId());
        if (alreadyEntity != null) {
            return ResponseDTO.wrap(SystemConfigResponseCodeConst.ALREADY_EXIST);
        }
        ResponseDTO valueValid = this.configValueValid(updateDTO.getConfigKey(),updateDTO.getConfigValue());
        if(!valueValid.isSuccess()){
            return valueValid;
        }
        entity = SmartBeanUtil.copy(updateDTO, SystemConfigEntity.class);
        systemConfigDao.updateById(entity);

        //刷新缓存
        this.initSystemConfigCache();
        return ResponseDTO.succ();
    }


    private ResponseDTO<String> configValueValid(String configKey , String configValue){
        SystemConfigEnum.Key configKeyEnum = SystemConfigEnum.Key.selectByKey(configKey);
        if(configKeyEnum == null){
            return ResponseDTO.succ();
        }
        SystemConfigDataType dataType = configKeyEnum.getDataType();
        if(dataType == null){
            return ResponseDTO.succ();
        }
        if(dataType.name().equals(SystemConfigDataType.TEXT.name())){
            return ResponseDTO.succ();
        }
        if(dataType.name().equals(SystemConfigDataType.JSON.name())){
            try {
                JSONObject jsonStr = JSONObject.parseObject(configValue);
                return ResponseDTO.succ();
            } catch (Exception e) {
                return ResponseDTO.wrap(ResponseCodeConst.ERROR_PARAM,"数据格式不是JSON,请修改后提交。");
            }
        }
        if(StringUtils.isNotEmpty(dataType.getValid())){
            Boolean valid = Pattern.matches(dataType.getValid(), configValue);
            if(valid){
                return ResponseDTO.succ();
            }
            return ResponseDTO.wrap(ResponseCodeConst.ERROR_PARAM,"数据格式不是"+dataType.name().toLowerCase()+",请修改后提交。");
        }

        return ResponseDTO.succ();
    }

    /**
     * 根据分组名称 获取获取系统设置
     *
     * @param group
     * @return
     */
    public ResponseDTO<List<SystemConfigVO>> getListByGroup(String group) {

        List<SystemConfigEntity> entityList = systemConfigDao.getListByGroup(group);
        if (CollectionUtils.isEmpty(entityList)) {
            return ResponseDTO.succData(Lists.newArrayList());
        }
        List<SystemConfigVO> systemConfigList = SmartBeanUtil.copyList(entityList, SystemConfigVO.class);
        return ResponseDTO.succData(systemConfigList);
    }

    /**
     * 根据分组名称 获取获取系统设置
     *
     * @param group
     * @return
     */
    public List<SystemConfigDTO> getListByGroup(SystemConfigEnum.Group group) {
        List<SystemConfigEntity> entityList = systemConfigDao.getListByGroup(group.name());
        if (CollectionUtils.isEmpty(entityList)) {
            return Lists.newArrayList();
        }
        List<SystemConfigDTO> systemConfigList = SmartBeanUtil.copyList(entityList, SystemConfigDTO.class);
        return systemConfigList;
    }

}
