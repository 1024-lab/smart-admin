package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler;

import cn.idev.excel.FastExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.dao.SprinklerStockInDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.bo.SprinklerStockInCreateBO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerStockInEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerStockInCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerStockInQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.SprinklerStockInExcelVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.SprinklerStockInVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.listener.SprinklerStockInCreateBOParseListener;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.module.support.datatracer.service.DataTracerService;
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
public class SprinklerService {

    @Resource
    private SprinklerStockInDao sprinklerStockInDao;


    @Resource
    private DataTracerService dataTracerService;

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

    public ResponseDTO<String> batchImport(@Valid MultipartFile file, RequestUser requestUser) {

        try(InputStream stream = file.getInputStream()) {

            SprinklerStockInCreateBOParseListener listener = new SprinklerStockInCreateBOParseListener();

            FastExcel.read(stream, SprinklerStockInCreateBO.class, listener).sheet().doRead();
            List<SprinklerStockInCreateBO> createBOs = listener.getSprinklerStockInCreateBOList();

            List<SprinklerStockInCreateForm> createVOs = createBOs.stream().map(
                    createBO->{
                        SprinklerStockInCreateForm createVO = new SprinklerStockInCreateForm();
                        createVO.setPurchaseDateContractNumber(createBO.getPurchaseDateContractNumber());
                        createVO.setSprinklerModel(createBO.getSprinklerModel());
                        createVO.setSprinklerSerial(createBO.getSprinklerSerial());
                        createVO.setShippingDate(createBO.getShippingDate());
                        createVO.setWarehouseDate(createBO.getWarehouseDate());
                        createVO.setVoltage(createBO.getVoltage());
                        createVO.setJetsout(createBO.getJetsout());
                        createVO.setHistory(createBO.getHistory());
                        createVO.setStatus(createBO.getStatus());
                        createVO.setDisabledFlag(Boolean.FALSE);
                        createVO.setCreateUserId(requestUser.getUserId());
                        createVO.setCreateUserName(requestUser.getUserName());
                        return createVO;
                    }
            ).collect(Collectors.toList());
            createVOs.forEach(this::createSprinklerStockIn);
            return ResponseDTO.ok();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

}
