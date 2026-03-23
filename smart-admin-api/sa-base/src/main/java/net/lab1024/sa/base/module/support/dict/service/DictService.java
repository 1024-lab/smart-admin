package net.lab1024.sa.base.module.support.dict.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.common.util.SmartStringUtil;
import net.lab1024.sa.base.constant.CacheKeyConst;
import net.lab1024.sa.base.module.support.dict.dao.DictDao;
import net.lab1024.sa.base.module.support.dict.dao.DictDataDao;
import net.lab1024.sa.base.module.support.dict.domain.entity.DictDataEntity;
import net.lab1024.sa.base.module.support.dict.domain.entity.DictEntity;
import net.lab1024.sa.base.module.support.dict.domain.form.*;
import net.lab1024.sa.base.module.support.dict.domain.vo.DictDataVO;
import net.lab1024.sa.base.module.support.dict.domain.vo.DictVO;
import net.lab1024.sa.base.module.support.dict.manager.DictManager;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据字典 Service
 *
 * @Author 1024创新实验室-主任-卓大
 * @Date 2025-03-25 22:25:04
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */

@Service
public class DictService {

    @Resource
    private DictDao dictDao;

    @Resource
    private DictDataDao dictDataDao;

    @Resource
    private CacheManager cacheManager;

    @Resource
    private DictManager dictManager;

    /**
     * 获取全部数据
     */
    public List<DictDataVO> getAll() {
        return dictDataDao.getAll();
    }

    /**
     * 获取所有字典
     */
    public List<DictVO> getAllDict() {
        List<DictEntity> dictEntityList = dictDao.selectList(null).stream().filter(e -> !e.getDisabledFlag()).collect(Collectors.toList());
        return SmartBeanUtil.copyList(dictEntityList, DictVO.class);
    }

    /**
     * 分页查询
     */
    public PageResult<DictVO> queryPage(DictQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<DictVO> list = dictDao.queryPage(page, queryForm);
        return SmartPageUtil.convert2PageResult(page, list);
    }

    /**
     * 添加
     */
    public synchronized ResponseDTO<String> add(DictAddForm addForm) {
        DictEntity existDictCode = dictDao.selectByCode(addForm.getDictCode());
        if (null != existDictCode) {
            return ResponseDTO.userErrorParam("数据字典编码已经存在！");
        }

        DictEntity dictEntity = SmartBeanUtil.copy(addForm, DictEntity.class);
        dictDao.insert(dictEntity);
        return ResponseDTO.ok();
    }

    /**
     * 禁用  启用
     */
    public ResponseDTO<String> updateDisabled(Long dictId) {
        DictEntity dictEntity = dictDao.selectById(dictId);
        if (dictEntity == null) {
            return ResponseDTO.userErrorParam("数据不存在");
        }

        dictEntity.setDisabledFlag(!dictEntity.getDisabledFlag());
        dictDao.updateById(dictEntity);
        return ResponseDTO.ok();
    }

    /**
     * 更新
     */
    @CacheEvict(CacheKeyConst.Dict.DICT_DATA)
    public synchronized ResponseDTO<String> update(DictUpdateForm updateForm) {
        DictEntity existDictCode = dictDao.selectByCode(updateForm.getDictCode());
        if (null != existDictCode && !existDictCode.getDictId().equals(updateForm.getDictId())) {
            return ResponseDTO.userErrorParam("数据字典编码已经存在！");
        }

        DictEntity dictEntity = SmartBeanUtil.copy(updateForm, DictEntity.class);
        dictDao.updateById(dictEntity);
        return ResponseDTO.ok();
    }

    /**
     * 批量删除
     */
    @CacheEvict(CacheKeyConst.Dict.DICT_DATA)
    public synchronized ResponseDTO<String> batchDelete(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return ResponseDTO.ok();
        }

        dictDao.deleteBatchIds(idList);
        return ResponseDTO.ok();
    }

    /**
     * 单个删除
     */
    @CacheEvict(CacheKeyConst.Dict.DICT_DATA)
    public synchronized ResponseDTO<String> delete(Long dictId) {
        if (null == dictId) {
            return ResponseDTO.ok();
        }

        dictDao.deleteById(dictId);
        return ResponseDTO.ok();
    }


    // -------------- 字典数据 --------------------

    /**
     * 分页查询
     */
    public List<DictDataVO> queryDictData(Long dictId) {
        return dictDataDao.queryByDictId(dictId);
    }

    /**
     * 获取字典
     */

    public DictDataVO getDictData(String dictCode, String dataValue) {
        return dictManager.getDictData(dictCode, dataValue);
    }

    /**
     * 获取字典Label
     */
    public String getDictDataLabel(String dictCode, String dataValue) {
        DictDataVO dictData = getDictData(dictCode, dataValue);
        return dictData == null ? "" : dictData.getDataLabel();
    }

    /**
     * 添加
     */
    public synchronized ResponseDTO<String> addDictData(DictDataAddForm addForm) {

        addForm.setDataValue(SmartStringUtil.trim(addForm.getDataValue()));

        DictEntity dictEntity = dictDao.selectById(addForm.getDictId());
        if (null == dictEntity) {
            return ResponseDTO.userErrorParam("数据字典不存在");
        }

        DictDataEntity existData = dictDataDao.selectByDictIdAndValue(addForm.getDictId(), addForm.getDataValue());
        if (null != existData) {
            return ResponseDTO.userErrorParam("已存在相同value的数据");
        }

        DictDataEntity dictDataEntity = SmartBeanUtil.copy(addForm, DictDataEntity.class);
        dictDataDao.insert(dictDataEntity);
        return ResponseDTO.ok();
    }

    /**
     * 更新
     */
    @CacheEvict(value = CacheKeyConst.Dict.DICT_DATA, key = "#updateForm.dictCode + '_' + #updateForm.dataValue")
    public synchronized ResponseDTO<String> updateDictData(DictDataUpdateForm updateForm) {

        updateForm.setDataValue(SmartStringUtil.trim(updateForm.getDataValue()));

        DictEntity dictEntity = dictDao.selectById(updateForm.getDictId());
        if (null == dictEntity) {
            return ResponseDTO.userErrorParam("数据字典不存在");
        }

        DictDataEntity existData = dictDataDao.selectByDictIdAndValue(updateForm.getDictId(), updateForm.getDataValue());
        if (null != existData && !existData.getDictDataId().equals(updateForm.getDictDataId())) {
            return ResponseDTO.userErrorParam("已存在相同value的数据");
        }

        DictDataEntity dictDataEntity = SmartBeanUtil.copy(updateForm, DictDataEntity.class);
        dictDataDao.updateById(dictDataEntity);
        return ResponseDTO.ok();
    }

    /**
     * 批量删除
     */
    public synchronized ResponseDTO<String> batchDeleteDictData(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return ResponseDTO.ok();
        }
        // 清除缓存
        clearDictDataCache(idList);
        // 删除
        dictDataDao.deleteBatchIds(idList);
        return ResponseDTO.ok();
    }

    /**
     * 单个删除
     */
    public synchronized ResponseDTO<String> deleteDictData(Long dictDataId) {
        if (null == dictDataId) {
            return ResponseDTO.ok();
        }
        // 清除缓存
        clearDictDataCache(Collections.singletonList(dictDataId));
        // 删除
        dictDataDao.deleteById(dictDataId);
        return ResponseDTO.ok();
    }


    /**
     * 清空字典数据缓存
     */
    private void clearDictDataCache(List<Long> idList) {
        List<DictDataVO> dictDataList = dictDataDao.selectByDictDataIds(idList);
        Cache cache = cacheManager.getCache(CacheKeyConst.Dict.DICT_DATA);
        if (cache == null) {
            return;
        }

        for (DictDataVO dictDataVO : dictDataList) {
            cache.evict(dictDataVO.getDictCode() + "_" + dictDataVO.getDataValue());
        }
    }


    /**
     * 更新启用/禁用
     */
    public synchronized ResponseDTO<String> updateDictDataDisabled(Long dictDataId) {
        DictDataEntity dictDataEntity = dictDataDao.selectById(dictDataId);
        if (dictDataEntity == null) {
            return ResponseDTO.userErrorParam("数据不存在");
        }

        dictDataEntity.setDisabledFlag(!dictDataEntity.getDisabledFlag());
        dictDataDao.updateById(dictDataEntity);
        return ResponseDTO.ok();
    }

}
