package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.processor.impl;

import cn.idev.excel.util.StringUtils;
import jakarta.annotation.Resource;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.UsableSprinklerCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.processor.DataProcessor;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.SprinklerRepository;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UsableSprinklerDataProcessorImpl implements DataProcessor<UsableSprinklerCreateForm> {

    @Resource
    private UsableSprinklerRepository usableSprinklerRepository;
    @Autowired
    private RequestUser requestUser;

    @Override
    public ResponseDTO<String> process(List<UsableSprinklerCreateForm> createVOs) {

        //提前返回空值情况
        if (createVOs.isEmpty()) {
            return ResponseDTO.ok("导入数据为空");
        }

        // 校验1：收集无效数据（空值或空字符串）
        Set<String> invalidSerials = createVOs.stream()
                .filter(vo -> StringUtils.isBlank(vo.getSprinklerSerial()))
                .map(UsableSprinklerCreateForm::getSprinklerSerial) // 实际会得到null或空字符串
                .collect(Collectors.toSet());

        //使用提取方法优化可读性
        // 校验2：收集已存在数据
        Set<String> existingSerials = getExistingSprinklerSerials(createVOs);

        List<SprinklerEntity> validatedSprinklerId = sprinklerRepository.getListBySprinklerSerials(createVOs.stream().map(createVO->createVO.getSprinklerSerial()).toList());

//        EnterpriseEntity validateEnterprise = enterpriseDao.queryByEnterpriseName(createVO.getEnterpriseName(), null, Boolean.FALSE);
    }

    // 辅助方法：获取已存在序列号
    private Set<String> getExistingSprinklerSerials(List<UsableSprinklerCreateForm> createVOs) {
        List<String> serials = createVOs.stream()
                .map(UsableSprinklerCreateForm::getSprinklerSerial)
                .toList();

        return sprinklerRepository.getListBySprinklerSerials(serials)
                .stream()
                .map(SprinklerEntity::getSprinklerSerial)
                .collect(Collectors.toCollection(LinkedHashSet::new)); // 保持查询顺序
    }


}
