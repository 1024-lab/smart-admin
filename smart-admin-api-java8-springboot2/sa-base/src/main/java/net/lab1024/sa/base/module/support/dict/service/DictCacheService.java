package net.lab1024.sa.base.module.support.dict.service;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.module.support.dict.dao.DictKeyDao;
import net.lab1024.sa.base.module.support.dict.dao.DictValueDao;
import net.lab1024.sa.base.module.support.dict.domain.entity.DictKeyEntity;
import net.lab1024.sa.base.module.support.dict.domain.entity.DictValueEntity;
import net.lab1024.sa.base.module.support.dict.domain.vo.DictValueVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 字典缓存 服务
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2022/5/26 19:40:55
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@Service
public class DictCacheService {

    @Resource
    private DictKeyDao dictKeyDao;
    @Resource
    private DictValueDao dictValueDao;

    private ConcurrentHashMap<String, List<DictValueVO>> DICT_CACHE = new ConcurrentHashMap<>();


    @PostConstruct
    public void dictCache() {
        this.cacheInit();
    }

    public void cacheInit() {
        List<DictKeyEntity> dictKeyEntityList = dictKeyDao.selectByDeletedFlag(false);
        if (CollectionUtils.isEmpty(dictKeyEntityList)) {
            return;
        }
        List<DictValueEntity> dictKeyValueList = dictValueDao.selectByDeletedFlag(false);
        List<DictValueVO> dictValueVOList = SmartBeanUtil.copyList(dictKeyValueList, DictValueVO.class);
        Map<Long, List<DictValueVO>> valueListMap = dictValueVOList.stream().collect(Collectors.groupingBy(DictValueVO::getDictKeyId));
        //字典键值对缓存
        for (DictKeyEntity dictKeyEntity : dictKeyEntityList) {
            String keyCode = dictKeyEntity.getKeyCode();
            Long dictKeyId = dictKeyEntity.getDictKeyId();
            DICT_CACHE.put(keyCode, valueListMap.getOrDefault(dictKeyId, Lists.newArrayList()));
        }
        log.info("################# 数据字典缓存初始化完毕 ###################");
    }

    /**
     * 刷新缓存
     */
    public ResponseDTO<String> cacheRefresh() {
        DICT_CACHE.clear();
        this.cacheInit();
        return ResponseDTO.ok();
    }

    /**
     * 查询某个key对应的字典值列表
     *
     * @param keyCode
     * @return
     */
    public List<DictValueVO> selectByKeyCode(String keyCode) {
        return DICT_CACHE.getOrDefault(keyCode, Lists.newArrayList());
    }

    /**
     * 查询值code名称
     * @param keyCode
     * @param valueCode
     * @return
     */
    public String selectValueNameByValueCode(String keyCode, String valueCode) {
        DictValueVO dictValueVO = this.selectValueByValueCode(keyCode, valueCode);
        if (dictValueVO == null){
            return "";
        }
        return dictValueVO.getValueName()
        ;
    }

    public DictValueVO selectValueByValueCode(String keyCode, String valueCode) {
        if (StrUtil.isEmpty(valueCode)) {
            return null;
        }
        if (StrUtil.isEmpty(keyCode)) {
            return null;
        }

        List<DictValueVO> dictValueVOList = DICT_CACHE.get(valueCode);
        if (CollectionUtils.isEmpty(dictValueVOList)) {
            return null;
        }
        Optional<DictValueVO> option = dictValueVOList.stream().filter(e->e.getValueCode().equals(valueCode)).findFirst();
        if(option.isPresent()){
            return option.get();
        }
        return null;
    }
    public String selectValueNameByValueCodeSplit(String keyCode, String valueCodes) {
        if (StrUtil.isEmpty(valueCodes)) {
            return "";
        }
        List<String> valueNameList = Lists.newArrayList();
        String[] valueCodeArray = valueCodes.split(",");
        for (String valueCode : valueCodeArray) {
            DictValueVO dictValueVO = this.selectValueByValueCode(keyCode, valueCode);
            if (dictValueVO != null) {
                valueNameList.add(dictValueVO.getValueName());
            }
        }
        return StringUtils.join(valueNameList, ",");
    }

}
