package net.lab1024.sa.common.module.support.config;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.common.common.code.UserErrorCode;
import net.lab1024.sa.common.common.domain.PageResult;
import net.lab1024.sa.common.common.domain.ResponseDTO;
import net.lab1024.sa.common.common.util.SmartBeanUtil;
import net.lab1024.sa.common.common.util.SmartPageUtil;
import net.lab1024.sa.common.constant.ReloadConst;
import net.lab1024.sa.common.module.support.config.domain.*;
import net.lab1024.sa.common.module.support.reload.core.annoation.SmartReload;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 系统配置业务类
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-03-14 20:46:27
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Slf4j
@Service
public class ConfigService {

    /**
     * 一个简单的系统配置缓存
     */
    private final ConcurrentHashMap<String, ConfigEntity> configCache = new ConcurrentHashMap<>();

    @Autowired
    private ConfigDao configDao;

    @SmartReload(ReloadConst.CONFIG_RELOAD)
    public void configReload(String param) {
        this.loadConfigCache();
    }

    /**
     * 初始化系统设置缓存
     */
    @PostConstruct
    private void loadConfigCache() {
        configCache.clear();
        List<ConfigEntity> entityList = configDao.selectList(null);
        if (CollectionUtils.isEmpty(entityList)) {
            return;
        }
        entityList.forEach(entity -> this.configCache.put(entity.getConfigKey().toLowerCase(), entity));
        log.info("################# 系统配置缓存初始化完毕:{} ###################", configCache.size());
    }

    /**
     * 刷新系统设置缓存
     */
    private void refreshConfigCache(Long configId) {
        // 重新查询 加入缓存
        ConfigEntity configEntity = configDao.selectById(configId);
        if (null == configEntity) {
            return;
        }
        this.configCache.put(configEntity.getConfigKey().toLowerCase(), configEntity);
    }

    /**
     * 分页查询系统配置
     *
     * @param queryDTO
     * @return
     */
    public ResponseDTO<PageResult<ConfigVO>> queryConfigPage(ConfigQueryForm queryDTO) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryDTO);
        List<ConfigEntity> entityList = configDao.queryByPage(page, queryDTO);
        PageResult<ConfigVO> pageResult = SmartPageUtil.convert2PageResult(page, entityList, ConfigVO.class);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 查询配置缓存
     *
     * @param configKey
     * @return
     */
    public ConfigVO getConfig(ConfigKeyEnum configKey) {
        return this.getConfig(configKey.getValue());
    }

    /**
     * 查询配置缓存
     *
     * @param configKey
     * @return
     */
    public ConfigVO getConfig(String configKey) {
        if (StrUtil.isBlank(configKey)) {
            return null;
        }
        ConfigEntity entity = this.configCache.get(configKey.toLowerCase());
        return SmartBeanUtil.copy(entity, ConfigVO.class);
    }

    /**
     * 查询配置缓存参数
     *
     * @param configKey
     * @return
     */
    public String getConfigValue(ConfigKeyEnum configKey) {
        return this.getConfig(configKey).getConfigValue();
    }

    /**
     * 根据参数key查询 并转换为对象
     *
     * @param configKey
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getConfigValue2Obj(ConfigKeyEnum configKey, Class<T> clazz) {
        String configValue = this.getConfigValue(configKey);
        return JSON.parseObject(configValue, clazz);
    }

    /**
     * 添加系统配置
     *
     * @param configAddDTO
     * @return
     */
    public ResponseDTO<String> add(ConfigAddForm configAddDTO) {
        ConfigEntity entity = configDao.selectByKey(configAddDTO.getConfigKey());
        if (null != entity) {
            return ResponseDTO.error(UserErrorCode.ALREADY_EXIST);
        }
        entity = SmartBeanUtil.copy(configAddDTO, ConfigEntity.class);
        configDao.insert(entity);

        // 刷新缓存
        this.refreshConfigCache(entity.getConfigId());
        return ResponseDTO.ok();
    }

    /**
     * 更新系统配置
     *
     * @param updateDTO
     * @return
     */
    public ResponseDTO<String> updateSystemConfig(ConfigUpdateForm updateDTO) {
        Long configId = updateDTO.getConfigId();
        ConfigEntity entity = configDao.selectById(configId);
        if (null == entity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        ConfigEntity alreadyEntity = configDao.selectByKey(updateDTO.getConfigKey());
        if (null != alreadyEntity && !Objects.equals(configId, alreadyEntity.getConfigId())) {
            return ResponseDTO.error(UserErrorCode.ALREADY_EXIST, "config key 已存在");
        }

        // 更新数据
        entity = SmartBeanUtil.copy(updateDTO, ConfigEntity.class);
        configDao.updateById(entity);

        // 刷新缓存
        this.refreshConfigCache(configId);
        return ResponseDTO.ok();
    }

    /**
     * 更新系统配置
     *
     * @param key
     * @param value
     * @return
     */
    public ResponseDTO<String> updateValueByKey(ConfigKeyEnum key, String value) {
        ConfigVO config = this.getConfig(key);
        if (null == config) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }

        // 更新数据
        Long configId = config.getConfigId();
        ConfigEntity entity = new ConfigEntity();
        entity.setConfigId(configId);
        entity.setConfigValue(value);
        configDao.updateById(entity);

        // 刷新缓存
        this.refreshConfigCache(configId);
        return ResponseDTO.ok();
    }
}
