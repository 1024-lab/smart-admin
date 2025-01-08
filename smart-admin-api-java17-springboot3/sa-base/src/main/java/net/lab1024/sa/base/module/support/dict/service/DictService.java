package net.lab1024.sa.base.module.support.dict.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import jakarta.annotation.Resource;
import net.lab1024.sa.base.common.code.UserErrorCode;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.module.support.dict.dao.DictKeyDao;
import net.lab1024.sa.base.module.support.dict.dao.DictValueDao;
import net.lab1024.sa.base.module.support.dict.domain.entity.DictKeyEntity;
import net.lab1024.sa.base.module.support.dict.domain.entity.DictValueEntity;
import net.lab1024.sa.base.module.support.dict.domain.form.*;
import net.lab1024.sa.base.module.support.dict.domain.vo.DictKeyVO;
import net.lab1024.sa.base.module.support.dict.domain.vo.DictValueVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典 服务
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2022/5/26 19:40:55
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Service
public class DictService {

    @Resource
    private DictKeyDao dictKeyDao;
    @Resource
    private DictValueDao dictValueDao;
    @Resource
    private DictCacheService dictCacheService;


    /**
     * key添加
     *
     * @param keyAddForm
     * @return
     */
    public synchronized ResponseDTO<String> keyAdd(DictKeyAddForm keyAddForm) {
        DictKeyEntity dictKeyEntity = dictKeyDao.selectByCode(keyAddForm.getKeyCode(), false);
        if (dictKeyEntity != null) {
            return ResponseDTO.error(UserErrorCode.ALREADY_EXIST);
        }
        dictKeyEntity = SmartBeanUtil.copy(keyAddForm, DictKeyEntity.class);
        dictKeyDao.insert(dictKeyEntity);
        //刷新缓存
        dictCacheService.cacheRefresh();
        return ResponseDTO.ok();
    }

    /**
     * 值添加
     *
     * @param valueAddForm
     * @return
     */
    public synchronized ResponseDTO<String> valueAdd(DictValueAddForm valueAddForm) {
        DictValueEntity dictValueEntity = dictValueDao.selectByCode(valueAddForm.getValueCode(), false);
        if (dictValueEntity != null) {
            return ResponseDTO.error(UserErrorCode.ALREADY_EXIST);
        }
        dictValueEntity = SmartBeanUtil.copy(valueAddForm, DictValueEntity.class);
        dictValueDao.insert(dictValueEntity);
        //刷新缓存
        dictCacheService.cacheRefresh();
        return ResponseDTO.ok();
    }

    /**
     * key 编辑
     *
     * @param keyUpdateForm
     * @return
     */
    public synchronized ResponseDTO<String> keyEdit(DictKeyUpdateForm keyUpdateForm) {
        DictKeyEntity dictKeyEntity = dictKeyDao.selectByCode(keyUpdateForm.getKeyCode(), false);
        if (dictKeyEntity != null && !dictKeyEntity.getDictKeyId().equals(keyUpdateForm.getDictKeyId())) {
            return ResponseDTO.error(UserErrorCode.ALREADY_EXIST);
        }
        DictKeyEntity dictKeyUpdateEntity = SmartBeanUtil.copy(keyUpdateForm, DictKeyEntity.class);
        dictKeyDao.updateById(dictKeyUpdateEntity);
        //刷新缓存
        dictCacheService.cacheRefresh();
        return ResponseDTO.ok();
    }

    /**
     * 值编辑
     *
     * @param valueUpdateForm
     * @return
     */
    public synchronized ResponseDTO<String> valueEdit(DictValueUpdateForm valueUpdateForm) {
        DictKeyEntity dictKeyEntity = dictKeyDao.selectById(valueUpdateForm.getDictKeyId());
        if (dictKeyEntity == null || dictKeyEntity.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("key不能存在");
        }
        DictValueEntity dictValueEntity = dictValueDao.selectByCode(valueUpdateForm.getValueCode(), false);
        if (dictValueEntity != null && !dictValueEntity.getDictValueId().equals(valueUpdateForm.getDictValueId())) {
            return ResponseDTO.error(UserErrorCode.ALREADY_EXIST);
        }
        DictValueEntity dictValueUpdateEntity = SmartBeanUtil.copy(valueUpdateForm, DictValueEntity.class);
        dictValueDao.updateById(dictValueUpdateEntity);
        //刷新缓存
        dictCacheService.cacheRefresh();
        return ResponseDTO.ok();
    }

    /**
     * key删除
     *
     * @param keyIdList
     * @return
     */
    public synchronized ResponseDTO<String> keyDelete(List<Long> keyIdList) {
        if (CollectionUtils.isEmpty(keyIdList)) {
            return ResponseDTO.ok();
        }
        dictKeyDao.updateDeletedFlagByIdList(keyIdList, true);
        //刷新缓存
        dictCacheService.cacheRefresh();
        return ResponseDTO.ok();
    }

    /**
     * 值删除
     *
     * @param valueIdList
     * @return
     */
    public synchronized ResponseDTO<String> valueDelete(List<Long> valueIdList) {
        if (CollectionUtils.isEmpty(valueIdList)) {
            return ResponseDTO.ok();
        }
        dictValueDao.updateDeletedFlagByIdList(valueIdList, true);
        //刷新缓存
        dictCacheService.cacheRefresh();
        return ResponseDTO.ok();
    }

    /**
     * 分页查询key
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<DictKeyVO>> keyQuery(DictKeyQueryForm queryForm) {
        queryForm.setDeletedFlag(false);
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<DictKeyVO> list = dictKeyDao.query(page, queryForm);
        PageResult<DictKeyVO> pageResult = SmartPageUtil.convert2PageResult(page, list);
        if (pageResult.getEmptyFlag()) {
            return ResponseDTO.ok(pageResult);
        }
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 所有key
     *
     * @return
     */
    public List<DictKeyVO> queryAllKey() {
        return SmartBeanUtil.copyList(dictKeyDao.selectByDeletedFlag(false), DictKeyVO.class);
    }

    /**
     * 分页查询值
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<DictValueVO>> valueQuery(DictValueQueryForm queryForm) {
        queryForm.setDeletedFlag(false);
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<DictValueVO> list = dictValueDao.query(page, queryForm);
        PageResult<DictValueVO> pageResult = SmartPageUtil.convert2PageResult(page, list);
        if (pageResult.getEmptyFlag()) {
            return ResponseDTO.ok(pageResult);
        }
        return ResponseDTO.ok(pageResult);
    }


}