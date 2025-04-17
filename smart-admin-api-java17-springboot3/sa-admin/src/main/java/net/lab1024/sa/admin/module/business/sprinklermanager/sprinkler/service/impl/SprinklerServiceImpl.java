package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.service.impl;

import cn.idev.excel.FastExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.oa.bank.domain.BankEntity;
import net.lab1024.sa.admin.module.business.oa.enterprise.domain.entity.EnterpriseEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.dao.SprinklerDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.dao.UsableSprinklerDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.bo.SprinklerCreateBO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.bo.UsableSprinklerCreateBO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.UsableSprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.UsableSprinklerCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.SprinklerExcelVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.SprinklerVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.listener.SprinklerCreateBOParseListener;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.listener.UsableSprinklerCreateBOParseListener;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.repository.SprinklerRepository;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.service.SprinklerService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.module.support.datatracer.constant.DataTracerConst;
import net.lab1024.sa.base.module.support.datatracer.constant.DataTracerTypeEnum;
import net.lab1024.sa.base.module.support.datatracer.service.DataTracerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SprinklerServiceImpl implements SprinklerService {

    @Resource
    private SprinklerRepository sprinklerRepository;

    @Resource
    private DataTracerService dataTracerService;

    /**
     * 分页查询喷头总表模块
     *
     */
    public ResponseDTO<PageResult<SprinklerVO>> queryByPage(SprinklerQueryForm queryForm, Byte type) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<SprinklerVO> sprinklerList = sprinklerRepository.queryPage(page, queryForm);
        PageResult<SprinklerVO> pageResult = SmartPageUtil.convert2PageResult(page, sprinklerList);
        return ResponseDTO.ok(pageResult);
    }

    public ResponseDTO<String> batchImport(@Valid MultipartFile file, RequestUser requestUser) {
        try(InputStream stream = file.getInputStream()) {

            SprinklerCreateBOParseListener listener = new SprinklerCreateBOParseListener();

            FastExcel.read(stream, SprinklerCreateBO.class, listener).sheet().doRead();
            List<SprinklerCreateForm> createVOs = listener.getSprinklerCreateVOList();
            this.createBatchSprinkler(createVOs);
            List<ResponseDTO> result = createVOs.stream().map(createVO->{
                createVO.setDisabledFlag(Boolean.FALSE);
                createVO.setCreateUserId(requestUser.getUserId());
                createVO.setCreateUserName(requestUser.getUserName());
                return this.createSprinkler(createVO);
            }).collect(Collectors.toList());
            return ResponseDTO.ok();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createBatchSprinkler(List<SprinklerCreateForm> createVOs) {
        List<SprinklerEntity> validateSprinklers = sprinklerRepository.getListBySprinklerSerial();
    }


    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> createSprinkler(SprinklerCreateForm createVO) {
        //验证喷头序列号是否重复
        SprinklerEntity validateSprinkler = sprinklerRepository.queryBySprinklerSerial(createVO.getSprinklerSerial(), null, Boolean.FALSE);
        if(Objects.nonNull(validateSprinkler)) {
            return ResponseDTO.userErrorParam("喷头序列号重复");
        }
        //数据插入
        SprinklerEntity insertSprinkler = SmartBeanUtil.copy(createVO, SprinklerEntity.class);
        sprinklerDao.insert(insertSprinkler);

        return ResponseDTO.ok();
    }

    /**
     * 查询喷头详情
     *
     */
    public SprinklerVO getDetail(Long sprinklerId) {
        return sprinklerDao.getDetail(sprinklerId, Boolean.FALSE);
    }


    /**
     * 获取导出数据
     */
    public List<SprinklerExcelVO> getExcelExportData(@Valid SprinklerQueryForm queryForm) {
        queryForm.setDeletedFlag(false);
        return sprinklerDao.selectExcelExportData(queryForm);
    }

    public ResponseDTO<String> batchCreateUsableSprinkler(@Valid MultipartFile file, RequestUser requestUser) {
        try(InputStream stream = file.getInputStream()) {

            UsableSprinklerCreateBOParseListener listener = new UsableSprinklerCreateBOParseListener();

            FastExcel.read(stream, UsableSprinklerCreateBO.class, listener).sheet().doRead();
            List<UsableSprinklerCreateForm> createVOs = listener.getUsableSprinklerCreateVOList();
            List<ResponseDTO> result = createVOs.stream().map(createVO->{
                createVO.setDisabledFlag(Boolean.FALSE);
                createVO.setCreateUserId(requestUser.getUserId());
                createVO.setCreateUserName(requestUser.getUserName());
                return this.createUsableSprinkler(createVO);
            }).collect(Collectors.toList());
            return ResponseDTO.ok();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Resource
    private UsableSprinklerDao usableSprinklerDao;

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> createUsableSprinkler(UsableSprinklerCreateForm createVO) {
        Long enterpriseId = createVO.getEnterpriseId();
        // 校验企业是否存在
        EnterpriseEntity enterpriseDetail = enterpriseDao.selectById(enterpriseId);
        if (Objects.isNull(enterpriseDetail) || enterpriseDetail.getDeletedFlag()) {
            return ResponseDTO.userErrorParam("企业不存在");
        }
        // 验证银行信息账号是否重复
        BankEntity validateBank = bankDao.queryByAccountNumber(enterpriseId, createVO.getAccountNumber(), null, Boolean.FALSE);
        if (Objects.nonNull(validateBank)) {
            return ResponseDTO.userErrorParam("银行信息账号重复");
        }
        // 数据插入
        BankEntity insertBank = SmartBeanUtil.copy(createVO, BankEntity.class);
        bankDao.insert(insertBank);
        dataTracerService.addTrace(enterpriseId, DataTracerTypeEnum.OA_ENTERPRISE, "新增银行:" + DataTracerConst.HTML_BR + dataTracerService.getChangeContent(insertBank));
        return ResponseDTO.ok();

        //验证喷头是否存在
        UsableSprinklerEntity validateSprinkler = usableSprinklerDao.queryBySprinklerSerial(createVO.getSprinklerSerial(), null, Boolean.FALSE);
        if(Objects.nonNull(validateSprinkler)) {
            return ResponseDTO.userErrorParam("喷头序列号重复");
        }
        //数据插入
        UsableSprinklerEntity insertUsableSprinkler = SmartBeanUtil.copy(createVO, UsableSprinklerEntity.class);
        usableSprinklerDao.insert(insertUsableSprinkler);

        return ResponseDTO.ok();
    }
}
