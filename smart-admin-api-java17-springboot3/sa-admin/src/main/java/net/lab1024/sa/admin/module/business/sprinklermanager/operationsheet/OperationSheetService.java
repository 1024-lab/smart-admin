package net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.dao.OperationSheetDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.entity.OperationSheetEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.form.Impl.SprinklerAllocateOperationSheetCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.form.Impl.SprinklerStockInOperationSheetCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.handler.SprinklerOperationHandler;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.registry.OperationHandlerRegistry;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.dao.SprinklerDao;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.LocalDateParseUtil;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class OperationSheetService {

    @Autowired
    private OperationHandlerRegistry operationHandlerRegistry;

    @Resource
    private SprinklerDao sprinklerDao;

    @Resource
    private OperationSheetDao operationSheetDao;


    /**
     * 新建记录表
     *
     */
    public ResponseDTO<String> batchCreateStockInOperationSheet(@Valid MultipartFile file, RequestUser requestUser){

        try(InputStream stream = file.getInputStream()) {
            // 使用POI解析为导入DTO
            Workbook workbook = new XSSFWorkbook(stream);
            List<SprinklerStockInOperationSheetCreateForm> createVOs = new ArrayList<>();
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(i);
                for (int j =0; j < sheet.getLastRowNum(); j++) {
                    Row row = sheet.getRow(j);
                    if (row == null||row.getRowNum() == 0) continue; // 跳过标题行
                    if (row.getCell(2) == null) continue;
                    String headSerial = row.getCell(2).getStringCellValue();
                    SprinklerStockInOperationSheetCreateForm createVO = new SprinklerStockInOperationSheetCreateForm();
                    createVO.setSprinklerSerial(headSerial);
                    if(row.getCell(0) == null) {
                        createVO.setPurchaseDate(null);
                        createVO.setContractNumber(null);
                    }else {
                        if (row.getCell(0).getCellType() == CellType.NUMERIC) {
                            row.getCell(0).setCellType(CellType.STRING);
                        }
                        String purchaseContract = row.getCell(0).getStringCellValue();
                        Matcher matcher = Pattern.compile("(\\d{10})\\s*$(\\d+)$\n").matcher(purchaseContract);
//\s*（\s*\d+\s*）
                        int matcher_start = 0;
                        if (matcher.find(matcher_start)) {
                            LocalDate purchaseDate = LocalDateParseUtil.parseDate(matcher.group(0));

                            // 提取数字
                            createVO.setPurchaseDate(purchaseDate);
                            matcher_start = matcher.end();
                        }
                        if (matcher.find(matcher_start)) {
                            String contractNumber = matcher.group(0);
                            createVO.setContractNumber(contractNumber);
                        }
                    }
                    if(headSerial=="") continue;
                    Long sprinklerId = sprinklerDao.findIdBySprinklerSerial(headSerial);
                    createVO.setSprinklerId(sprinklerId);
                    createVO.setOperationSheetId(operationSheetDao.findBySprinklerId(sprinklerId));
                    createVO.setDisabledFlag(Boolean.FALSE);
                    createVO.setCreateUserId(requestUser.getUserId());
                    createVO.setCreateUserName(requestUser.getUserName());
                    createVOs.add(createVO);

                }
            }
            SprinklerOperationHandler handler = operationHandlerRegistry.getHandler("stock_in");
            createVOs.forEach(handler::createOperationSheet);
            return ResponseDTO.ok();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public ResponseDTO<String> batchCreateAllocateOperationSheet(@Valid MultipartFile file, RequestUser requestUser) {
        try(InputStream stream = file.getInputStream()) {
            // 使用POI解析为导入DTO
            Workbook workbook = new XSSFWorkbook(stream);
            List<SprinklerAllocateOperationSheetCreateForm> createVOs = new ArrayList<>();
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(i);
                for (int j =0; j < sheet.getLastRowNum(); j++) {
                    Row row = sheet.getRow(j);
                    if (row == null||row.getRowNum() == 0) continue; // 跳过标题行
                    if (row.getCell(2) == null) continue;
                    String headSerial = row.getCell(2).getStringCellValue();
                    SprinklerAllocateOperationSheetCreateForm createVO = new SprinklerAllocateOperationSheetCreateForm();
                    createVO.setSprinklerSerial(headSerial);
                    if(row.getCell(4) == null) {
                        createVO.setAllocateDate(null);
                    }else {
                        if (row.getCell(4).getCellType() == CellType.NUMERIC) {
                            row.getCell(4).setCellType(CellType.STRING);
                        }
                        createVO.setAllocateDate(LocalDateParseUtil.parseDate(row.getCell(4).getStringCellValue()));
                    }
                    if(row.getCell(5) == null) {
                        createVO.setAllocateUser(null);
                    }else {
                        if (row.getCell(5).getCellType() == CellType.NUMERIC) {
                            row.getCell(5).setCellType(CellType.STRING);
                        }
                        createVO.setAllocateUser(row.getCell(5).getStringCellValue());
                    }
                    if(row.getCell(6) == null) {
                        createVO.setAllocateMachine(null);
                    }else {
                        if (row.getCell(6).getCellType() == CellType.NUMERIC) {
                            row.getCell(6).setCellType(CellType.STRING);
                        }
                        createVO.setAllocateMachine(row.getCell(6).getStringCellValue());
                    }
                    if(row.getCell(7) == null) {
                        createVO.setColorPosition(null);
                    }else {
                        if (row.getCell(7).getCellType() == CellType.NUMERIC) {
                            row.getCell(7).setCellType(CellType.STRING);
                        }
                        createVO.setColorPosition(row.getCell(7).getStringCellValue());
                    }
                    if(row.getCell(8) == null) {
                        createVO.setHistory(null);
                    }else {
                        if (row.getCell(8).getCellType() == CellType.NUMERIC) {
                            row.getCell(8).setCellType(CellType.STRING);
                        }
                        createVO.setHistory(row.getCell(8).getStringCellValue());
                    }
                    if(headSerial=="") continue;
                    Long sprinklerId = sprinklerDao.findIdBySprinklerSerial(headSerial);
                    createVO.setSprinklerId(sprinklerId);
                    OperationSheetEntity operationSheetEntity = new OperationSheetEntity();
                    operationSheetEntity.setSprinklerId(sprinklerId);
                    operationSheetEntity.setDisabledFlag(Boolean.FALSE);
                    operationSheetEntity.setDeletedFlag(Boolean.FALSE);
                    operationSheetDao.insert(operationSheetEntity);
                    Long operationSheetId = operationSheetEntity.getOperationSheetId();
                    createVO.setOperationSheetId(operationSheetId);
                    createVO.setDisabledFlag(Boolean.FALSE);
                    createVO.setCreateUserId(requestUser.getUserId());
                    createVO.setCreateUserName(requestUser.getUserName());
                    createVOs.add(createVO);
                }
            }
            SprinklerOperationHandler handler = operationHandlerRegistry.getHandler("allocate");
            createVOs.forEach(handler::createOperationSheet);
            return ResponseDTO.ok();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
