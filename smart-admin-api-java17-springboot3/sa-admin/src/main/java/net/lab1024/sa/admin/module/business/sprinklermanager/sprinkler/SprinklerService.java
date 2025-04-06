package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.dao.Impl.SprinklerStockInOperationSheetDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.dao.OperationSheetDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.entity.OperationSheetEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.form.Impl.SprinklerStockInOperationSheetQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.vo.SprinklerStockInOperationSheetVO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.dao.SprinklerDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.entity.SprinklerEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerQueryForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo.SprinklerVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.LocalDateParseUtil;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.module.support.datatracer.constant.DataTracerTypeEnum;
import net.lab1024.sa.base.module.support.datatracer.service.DataTracerService;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class SprinklerService {

    @Resource
    private SprinklerDao sprinklerDao;

    @Resource
    private SprinklerStockInOperationSheetDao sprinklerStockInOperationSheetDao;

    @Resource
    private OperationSheetDao operationSheetDao;

    @Resource
    private DataTracerService dataTracerService;

    /**
     * 分页查询喷头模块
     *
     */
    public ResponseDTO<PageResult<SprinklerVO>> queryByPage(SprinklerQueryForm queryForm) {
        queryForm.setDeletedFlag(Boolean.FALSE);
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<SprinklerVO> sprinklerList = sprinklerDao.queryPage(page, queryForm);
        PageResult<SprinklerVO> pageResult = SmartPageUtil.convert2PageResult(page, sprinklerList);
        return ResponseDTO.ok(pageResult);
    }

    public ResponseDTO<String> batchImport(@Valid MultipartFile file, RequestUser requestUser) {

        try(InputStream stream = file.getInputStream()) {
            // 使用POI解析为导入DTO
            Workbook workbook = new XSSFWorkbook(stream);
            List<SprinklerCreateForm> createVOs = new ArrayList<>();
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(i);
                for (int j =0; j < sheet.getLastRowNum(); j++) {
                    Row row = sheet.getRow(j);
                    if (row == null||row.getRowNum() == 0) continue; // 跳过标题行
                    if (row.getCell(2) == null) continue;
                    String headSerial = row.getCell(2).getStringCellValue();
                    SprinklerCreateForm createVO = new SprinklerCreateForm();
                    createVO.setSprinklerSerial(headSerial);

                    if(row.getCell(3) == null) {
                        createVO.setWarehouseDate(null);
                    }else {
                        if (row.getCell(3).getCellType() == CellType.NUMERIC) {
                            row.getCell(3).setCellType(CellType.STRING);
                        }
                        createVO.setWarehouseDate(LocalDateParseUtil.parseDate(row.getCell(3).getStringCellValue()));
                    }

                    if (headSerial != "") {
                        createVO.setCreateUserId(requestUser.getUserId());
                        createVO.setCreateUserName(requestUser.getUserName());
                        createVOs.add(createVO);
                    }
                }
            }
            createVOs.forEach(this::createSprinkler);
            return ResponseDTO.ok();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> createSprinkler(SprinklerCreateForm createVO) {
        //验证喷头序列号是否重复
        SprinklerEntity validateSprinkler = sprinklerDao.queryBySprinklerSerial(createVO.getSprinklerSerial(), null, Boolean.FALSE);
        if(Objects.nonNull(validateSprinkler)) {
            return ResponseDTO.userErrorParam("喷头序列号重复");
        }
        //数据插入
        SprinklerEntity insertSprinkler = SmartBeanUtil.copy(createVO, SprinklerEntity.class);
        sprinklerDao.insert(insertSprinkler);
        dataTracerService.insert(insertSprinkler.getSprinklerId(), DataTracerTypeEnum.SPRINKLER);
        Long sprinklerId = insertSprinkler.getSprinklerId();
        OperationSheetEntity operationSheetEntity = new OperationSheetEntity();
        operationSheetEntity.setSprinklerId(sprinklerId);
        operationSheetEntity.setDisabledFlag(Boolean.FALSE);
        operationSheetEntity.setDeletedFlag(Boolean.FALSE);
        operationSheetDao.insert(operationSheetEntity);

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
     * 分页查询喷头入库记录
     *
     */
    public PageResult<SprinklerStockInOperationSheetVO> queryPageStockInOperationSheetList(SprinklerStockInOperationSheetQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<SprinklerStockInOperationSheetVO> sprinklerStockInOperationSheetVOList = sprinklerStockInOperationSheetDao.queryPageStockInOperationSheetList(page, queryForm);
        return SmartPageUtil.convert2PageResult(page, sprinklerStockInOperationSheetVOList);
    }
}
