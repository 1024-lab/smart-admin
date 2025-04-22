package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.processor.impl;

import cn.idev.excel.util.StringUtils;
import jakarta.annotation.Resource;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.UsableSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.BaseCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.UsableSprinklerCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.processor.DataProcessor;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.SprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.UsableSprinklerRepository;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component("usable")
public class UsableSprinklerDataProcessorImpl implements DataProcessor<UsableSprinklerCreateForm> {

    @Resource
    private SprinklerRepository sprinklerRepository;

    @Resource
    private UsableSprinklerRepository usableSprinklerRepository;

    @Override
    public ResponseDTO<String> process(List<UsableSprinklerCreateForm> createVOs) {

        //提前返回空值情况
        if (createVOs.isEmpty()) {
            return ResponseDTO.ok("导入数据为空");
        }

        // 校验1：收集无效数据（空值或空字符串或不在所在仓）
        Set<String> invalidSerials = createVOs.stream()
                .filter(vo -> StringUtils.isBlank(vo.getSprinklerSerial()) || vo.getStatus() != 0)
                .map(UsableSprinklerCreateForm::getSprinklerSerial) // 实际会得到null或空字符串
                .collect(Collectors.toSet());

        //使用提取方法优化可读性
        // 校验2：收集已存在数据
        Set<String> existingSerials = getExistingSprinklerSerials(createVOs);

        //合并校验结果
        Map<Boolean, List<UsableSprinklerCreateForm>> partitionedData = createVOs.stream()
                .collect(Collectors.partitioningBy(
                        vo -> StringUtils.isNotBlank(vo.getSprinklerSerial()) && vo.getStatus() == 0
                                && existingSerials.contains(vo.getSprinklerSerial())
                ));
        List<UsableSprinklerEntity> validData = partitionedData.get(true).stream()
                .map(this::convertToEntity)
                .toList();

        // 错误数据合并（空值+重复值）
        Set<String> errorData = new HashSet<>();
        errorData.addAll(invalidSerials);
        errorData.addAll(partitionedData.get(false).stream()
                .map(UsableSprinklerCreateForm::getSprinklerSerial)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toSet()));

        List<UsableSprinklerEntity> validIds = usableSprinklerRepository.getListBySprinklerSerials();

        // 执行插入并返回详细信息
        if (!validData.isEmpty()) {
            usableSprinklerRepository.saveBatch(validData);
            return buildResponse(validData.size(), errorData);
        }

        return ResponseDTO.userErrorParam("无有效数据可插入，错误数据：" + String.join(",", errorData));


    }

    // 辅助方法：获取已存在序列号
    private <T extends BaseCreateForm> Set<String> getExistingSprinklerSerials(List<T> createVOs) {
        List<String> serials = createVOs.stream()
                .map(BaseCreateForm::getSprinklerSerial)
                .toList();

        return sprinklerRepository.getListBySprinklerSerials(serials)
                .stream()
                .map(SprinklerEntity::getSprinklerSerial)
                .collect(Collectors.toCollection(LinkedHashSet::new)); // 保持查询顺序
    }

    // 辅助方法：对象转换
    private UsableSprinklerEntity convertToEntity(UsableSprinklerCreateForm form) {
        return SmartBeanUtil.copy(form, UsableSprinklerEntity.class);
    }


    private ResponseDTO<String> buildResponse(int successCount, Set<String> errorData) {
        String msg = String.format(
                "成功插入%d条，错误数据（空值/重复）:%s",
                successCount,
                errorData.isEmpty() ? "无" : String.join(",", errorData)
        );
        return ResponseDTO.okMsg(msg);
    }


}
