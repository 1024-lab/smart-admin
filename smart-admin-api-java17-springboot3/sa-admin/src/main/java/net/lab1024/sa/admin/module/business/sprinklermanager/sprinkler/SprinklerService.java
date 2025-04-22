package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler;

import cn.idev.excel.util.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.constant.RepositorySprinklerTypeEnum;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.dao.SprinklerStockInDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerStockInEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.BaseCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerStockInCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerStockInQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.SprinklerStockInExcelVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.SprinklerStockInVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.factory.DataProcessorFactory;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.factory.RepositorySprinklerCreateFormFactory;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.processor.DataProcessor;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.SprinklerRepository;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.ExcelUtil;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.module.support.datatracer.service.DataTracerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SprinklerService {

    @Resource
    private SprinklerStockInDao sprinklerStockInDao;


    @Resource
    private DataTracerService dataTracerService;

    @Autowired
    private SprinklerRepository sprinklerRepository;

    /**
     * 分页查询喷头模块
     *
     */
    public ResponseDTO<PageResult<SprinklerStockInVO>> queryByPage(SprinklerStockInQueryForm queryForm) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<SprinklerStockInVO> sprinklerList = sprinklerStockInDao.queryPage(page, queryForm);
        PageResult<SprinklerStockInVO> pageResult = SmartPageUtil.convert2PageResult(page, sprinklerList);
        return ResponseDTO.ok(pageResult);
    }



    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> createSprinklerStockIn(SprinklerStockInCreateForm createVO) {
        //验证喷头序列号是否重复
        SprinklerStockInEntity validateSprinkler = sprinklerStockInDao.queryBySprinklerSerial(createVO.getSprinklerSerial(), null, Boolean.FALSE);
        if(Objects.nonNull(validateSprinkler)) {
            return ResponseDTO.userErrorParam("喷头序列号重复");
        }
        //数据插入
        SprinklerStockInEntity insertSprinkler = SmartBeanUtil.copy(createVO, SprinklerStockInEntity.class);
        sprinklerStockInDao.insert(insertSprinkler);

        return ResponseDTO.ok();
    }

    /**
     * 查询喷头详情
     *
     */
    public SprinklerStockInVO getDetail(Long sprinklerId) {
        return sprinklerStockInDao.getDetail(sprinklerId, Boolean.FALSE);
    }


    /**
     * 获取导出数据
     */
    public List<SprinklerStockInExcelVO> getExcelExportData(@Valid SprinklerStockInQueryForm queryForm) {
        queryForm.setDeletedFlag(false);
        return sprinklerStockInDao.selectExcelExportData(queryForm);
    }

    @Resource
    private RepositorySprinklerCreateFormFactory createFormFactory;

    @Resource
    private DataProcessorFactory processorFactory;

    public ResponseDTO<String> batchRepositorySprinklerCreate(@Valid MultipartFile file, RequestUser requestUser, @Valid Integer type) {
        Class<? extends BaseCreateForm> createVOClazz = createFormFactory.getSprinklerClass(type);
        List<? extends BaseCreateForm> list = ExcelUtil.importExcelByClass(file, createVOClazz).stream().peek(vo->initCreateVO(vo, requestUser)).toList();
        DataProcessor dataProcessor = processorFactory.getProcessor(RepositorySprinklerTypeEnum.values()[type].getDesc());
        dataProcessor.process(list);
        return ResponseDTO.ok();
    }


    public ResponseDTO<String> batchSprinklerCreate(@Valid MultipartFile file, RequestUser requestUser) {
        //使用收集器一次性完成字段设置，避免冗余操作
        List<SprinklerCreateForm> createVOs = ExcelUtil.importExcelByClass(file, SprinklerCreateForm.class)
                .stream()
                .peek(vo -> initCreateVO(vo, requestUser)) // 提取字段设置为独立方法
                .toList();

        //提前返回空值情况
        if (createVOs.isEmpty()) {
            return ResponseDTO.ok("导入数据为空");
        }

        // 校验1：收集无效数据（空值或空字符串）
        Set<String> invalidSerials = createVOs.stream()
                .filter(vo -> StringUtils.isBlank(vo.getSprinklerSerial()))
                .map(SprinklerCreateForm::getSprinklerSerial) // 实际会得到null或空字符串
                .collect(Collectors.toSet());

        //使用提取方法优化可读性
        // 校验2：收集已存在数据
        Set<String> existingSerials = getExistingSprinklerSerials(createVOs);

        //合并校验结果
        Map<Boolean, List<SprinklerCreateForm>> partitionedData = createVOs.stream()
                .collect(Collectors.partitioningBy(
                        vo -> StringUtils.isNotBlank(vo.getSprinklerSerial())
                                && !existingSerials.contains(vo.getSprinklerSerial())
                ));

        List<SprinklerEntity> validData = partitionedData.get(true).stream()
                .map(this::convertToEntity)
                .toList();

        // 错误数据合并（空值+重复值）
        Set<String> errorData = new HashSet<>();
        errorData.addAll(invalidSerials);
        errorData.addAll(partitionedData.get(false).stream()
                .map(SprinklerCreateForm::getSprinklerSerial)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toSet()));

        // 执行插入并返回详细信息
        if (!validData.isEmpty()) {
            sprinklerRepository.saveBatch(validData);
            return buildResponse(validData.size(), errorData);
        }
        return ResponseDTO.userErrorParam("无有效数据可插入，错误数据：" + String.join(",", errorData));

    }

    private ResponseDTO<String> buildResponse(int successCount, Set<String> errorData) {
        String msg = String.format(
                "成功插入%d条，错误数据（空值/重复）:%s",
                successCount,
                errorData.isEmpty() ? "无" : String.join(",", errorData)
        );
        return ResponseDTO.okMsg(msg);
    }

    // 辅助方法：对象转换
    private SprinklerEntity convertToEntity(SprinklerCreateForm form) {
        return SmartBeanUtil.copy(form, SprinklerEntity.class);
    }

    // 辅助方法：获取已存在序列号
    private Set<String> getExistingSprinklerSerials(List<SprinklerCreateForm> createVOs) {
        List<String> serials = createVOs.stream()
                .map(SprinklerCreateForm::getSprinklerSerial)
                .toList();

        return sprinklerRepository.getListBySprinklerSerials(serials)
                .stream()
                .map(SprinklerEntity::getSprinklerSerial)
                .collect(Collectors.toCollection(LinkedHashSet::new)); // 保持查询顺序
    }

    // 辅助方法：初始化创建对象
    private <T extends BaseCreateForm> void initCreateVO(T vo, RequestUser user) {
        vo.setDisabledFlag(Boolean.FALSE);
        vo.setCreateUserId(user.getUserId());
        vo.setCreateUserName(user.getUserName());
    }

}
