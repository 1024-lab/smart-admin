package net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.dao.OperationSheetDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.entity.OperationSheetEntity;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.form.Impl.SprinklerAllocateOperationSheetCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.form.Impl.SprinklerMaintainOperationSheetCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.form.Impl.SprinklerRmaOperationSheetCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.form.Impl.SprinklerStockInOperationSheetCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.form.Impl.SprinklerUsableOperationSheetCreateForm;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
     */
    public ResponseDTO<String> batchCreateStockInOperationSheet(@Valid MultipartFile file, RequestUser requestUser) {

        try (InputStream stream = file.getInputStream()) {
            // 使用POI解析为导入DTO
            Workbook workbook = new XSSFWorkbook(stream);
            List<SprinklerStockInOperationSheetCreateForm> createVOs = new ArrayList<>();
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(i);
                for (int j = 0; j < sheet.getLastRowNum(); j++) {
                    Row row = sheet.getRow(j);
                    if (row == null || row.getRowNum() == 0) continue; // 跳过标题行
                    if (row.getCell(2) == null) continue;
                    String headSerial = row.getCell(2).getStringCellValue();
                    SprinklerStockInOperationSheetCreateForm createVO = new SprinklerStockInOperationSheetCreateForm();
                    createVO.setSprinklerSerial(headSerial);
                    if (row.getCell(0) == null) {
                        createVO.setPurchaseDate(null);
                        createVO.setContractNumber(null);
                    } else {
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
                    if (headSerial == "") continue;
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

    public ResponseDTO<String> batchCreateMaintainOperationSheet(@Valid MultipartFile file, RequestUser requestUser) {
        try (InputStream stream = file.getInputStream()) {
            // 使用POI解析为导入DTO
            Workbook workbook = new XSSFWorkbook(stream);
            List<SprinklerMaintainOperationSheetCreateForm> createVOs = new ArrayList<>();
            XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);
            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || row.getRowNum() == 0) continue; // 跳过标题行
                if (row.getCell(2) == null) continue;
                String headSerial = row.getCell(2).getStringCellValue();
                SprinklerMaintainOperationSheetCreateForm createVO = new SprinklerMaintainOperationSheetCreateForm();
                createVO.setSprinklerSerial(headSerial);
                if (row.getCell(1) == null) {
                    createVO.setMaintainDate(null);
                } else {
                    if (row.getCell(1).getCellType() == CellType.NUMERIC) {
                        row.getCell(1).setCellType(CellType.STRING);
                    }
                    createVO.setMaintainDate(LocalDateParseUtil.parseDate(row.getCell(1).getStringCellValue()));
                }
                if (row.getCell(3) == null) {
                    createVO.setMaintainReason(null);
                } else {
                    if (row.getCell(3).getCellType() == CellType.NUMERIC) {
                        row.getCell(3).setCellType(CellType.STRING);
                    }
                    createVO.setMaintainReason(row.getCell(3).getStringCellValue());
                }
                if (row.getCell(4) == null) {
                    createVO.setRealReason(null);
                } else {
                    if (row.getCell(4).getCellType() == CellType.NUMERIC) {
                        row.getCell(4).setCellType(CellType.STRING);
                    }
                    createVO.setRealReason(row.getCell(4).getStringCellValue());
                }
                if (row.getCell(5) == null) {
                    createVO.setMaintainMachine(null);
                } else {
                    if (row.getCell(5).getCellType() == CellType.NUMERIC) {
                        row.getCell(5).setCellType(CellType.STRING);
                    }
                    createVO.setMaintainMachine(row.getCell(5).getStringCellValue());
                }
                if (row.getCell(6) == null) {
                    createVO.setHistory(null);
                } else {
                    if (row.getCell(6).getCellType() == CellType.NUMERIC) {
                        row.getCell(6).setCellType(CellType.STRING);
                    }
                    createVO.setHistory(row.getCell(6).getStringCellValue());
                }
                if (headSerial == "") continue;
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
            SprinklerOperationHandler handler = operationHandlerRegistry.getHandler("maintain");
            createVOs.forEach(handler::createOperationSheet);
            return ResponseDTO.ok();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseDTO<String> batchCreateRmaOperationSheet(@Valid MultipartFile file, RequestUser requestUser) {
        try (InputStream stream = file.getInputStream()) {
            // 使用POI解析为导入DTO
            Workbook workbook = new XSSFWorkbook(stream);
            List<SprinklerRmaOperationSheetCreateForm> createVOs = new ArrayList<>();
            Map<String, SprinklerRmaOperationSheetCreateForm> createVOMap = new HashMap<>();

            XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(2);
            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || row.getRowNum() == 0) continue; // 跳过标题行
                if (row.getCell(0) == null || row.getCell(0).getStringCellValue()=="" || createVOMap.containsKey(row.getCell(0).getStringCellValue())) continue;
                String headSerial = row.getCell(0).getStringCellValue();

                SprinklerRmaOperationSheetCreateForm createVO = new SprinklerRmaOperationSheetCreateForm();
                createVO.setSprinklerSerial(headSerial);
                createVOMap.put(headSerial, createVO);
                if (row.getCell(1) == null) {
                    createVO.setMaintainReason(null);
                } else {
                    if (row.getCell(1).getCellType() == CellType.NUMERIC) {
                        row.getCell(1).setCellType(CellType.STRING);
                    }
                    createVO.setMaintainReason(row.getCell(1).getStringCellValue());
                }
                if (row.getCell(2) == null) {
                    createVO.setMaintainDate(null);
                } else {
                    if (row.getCell(2).getCellType() == CellType.NUMERIC) {
                        row.getCell(2).setCellType(CellType.STRING);
                    }
                    createVO.setMaintainDate(LocalDateParseUtil.parseDate(row.getCell(2).getStringCellValue()));
                }
                if (row.getCell(4) == null) {
                    createVO.setMaintainUser(null);
                } else {
                    if (row.getCell(4).getCellType() == CellType.NUMERIC) {
                        row.getCell(4).setCellType(CellType.STRING);
                    }
                    createVO.setMaintainUser(row.getCell(4).getStringCellValue());
                }
                if (row.getCell(5) == null) {
                    createVO.setHistory(null);
                } else {
                    if (row.getCell(5).getCellType() == CellType.NUMERIC) {
                        row.getCell(5).setCellType(CellType.STRING);
                    }
                    createVO.setHistory(row.getCell(5).getStringCellValue());
                }
                if (row.getCell(7) == null) {
                    createVO.setInMaintainWarehouse(null);
                } else {
                    if (row.getCell(7).getCellType() == CellType.NUMERIC) {
                        row.getCell(7).setCellType(CellType.STRING);
                    }
                    createVO.setInMaintainWarehouse(row.getCell(7).getStringCellValue());
                }
            }

            sheet = (XSSFSheet) workbook.getSheetAt(0);
            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || row.getRowNum() == 0) continue; // 跳过标题行
                if (row.getCell(2) == null || row.getCell(2).getStringCellValue()=="") continue;
                String headSerial = row.getCell(2).getStringCellValue();
                SprinklerRmaOperationSheetCreateForm createVO = null;
                if(createVOMap.containsKey(headSerial)){
                    createVO = createVOMap.get(headSerial);
                }else {
                    createVO = new SprinklerRmaOperationSheetCreateForm();
                    createVO.setSprinklerSerial(headSerial);
                }
                if (row.getCell(0) == null) {
                    createVO.setRmaDate(null);
                } else {
                    if (row.getCell(0).getCellType() == CellType.NUMERIC) {
                        row.getCell(0).setCellType(CellType.STRING);
                    }
                    createVO.setMaintainDate(LocalDateParseUtil.parseDate(row.getCell(0).getStringCellValue()));
                }
                if (row.getCell(1) == null) {
                    createVO.setRmaNumber(null);
                } else {
                    if (row.getCell(1).getCellType() == CellType.NUMERIC) {
                        row.getCell(1).setCellType(CellType.STRING);
                    }
                    createVO.setRmaNumber(row.getCell(1).getStringCellValue());
                }
                if (row.getCell(3) == null) {
                    createVO.setRmaPosition(null);
                } else {
                    if (row.getCell(3).getCellType() == CellType.NUMERIC) {
                        row.getCell(3).setCellType(CellType.STRING);
                    }
                    createVO.setRmaPosition(row.getCell(3).getStringCellValue());
                }
                if (row.getCell(4) == null) {
                    createVO.setRmaReason(null);
                } else {
                    if (row.getCell(4).getCellType() == CellType.NUMERIC) {
                        row.getCell(4).setCellType(CellType.STRING);
                    }
                    createVO.setRmaReason(row.getCell(4).getStringCellValue());
                }
                if (row.getCell(5) == null) {
                    createVO.setPostNumber(null);
                } else {
                    if (row.getCell(5).getCellType() == CellType.NUMERIC) {
                        row.getCell(5).setCellType(CellType.STRING);
                    }
                    createVO.setPostNumber(row.getCell(5).getStringCellValue());
                }

                if (row.getCell(6) == null) {
                    createVO.setProcessResult(null);
                } else {
                    if (row.getCell(6).getCellType() == CellType.NUMERIC) {
                        row.getCell(6).setCellType(CellType.STRING);
                    }
                    createVO.setProcessResult(row.getCell(6).getStringCellValue());
                }

                if (row.getCell(7) == null) {
                    createVO.setAgreeReplacement(null);
                } else {
                    if (row.getCell(7).getCellType() == CellType.NUMERIC) {
                        row.getCell(7).setCellType(CellType.STRING);
                    }
                    createVO.setAgreeReplacement(row.getCell(7).getStringCellValue());
                }

                if (row.getCell(8) == null) {
                    createVO.setReplacementResult(null);
                } else {
                    if (row.getCell(8).getCellType() == CellType.NUMERIC) {
                        row.getCell(8).setCellType(CellType.STRING);
                    }
                    createVO.setReplacementResult(row.getCell(8).getStringCellValue());
                }
                createVOs.add(createVO);
            }

            sheet = (XSSFSheet) workbook.getSheetAt(1);
            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || row.getRowNum() == 0) continue; // 跳过标题行
                if (row.getCell(2) == null || row.getCell(2).getStringCellValue()=="") continue;
                String headSerial = row.getCell(2).getStringCellValue();
                SprinklerRmaOperationSheetCreateForm createVO = null;
                if(createVOMap.containsKey(headSerial)){
                    createVO = createVOMap.get(headSerial);
                }else {
                    createVO = new SprinklerRmaOperationSheetCreateForm();
                    createVO.setSprinklerSerial(headSerial);
                }
                if (row.getCell(0) == null) {
                    createVO.setRmaDate(null);
                } else {
                    if (row.getCell(0).getCellType() == CellType.NUMERIC) {
                        row.getCell(0).setCellType(CellType.STRING);
                    }
                    createVO.setMaintainDate(LocalDateParseUtil.parseDate(row.getCell(0).getStringCellValue()));
                }
                if (row.getCell(1) == null) {
                    createVO.setRmaNumber(null);
                } else {
                    if (row.getCell(1).getCellType() == CellType.NUMERIC) {
                        row.getCell(1).setCellType(CellType.STRING);
                    }
                    createVO.setRmaNumber(row.getCell(1).getStringCellValue());
                }
                if (row.getCell(3) == null) {
                    createVO.setRmaPosition(null);
                } else {
                    if (row.getCell(3).getCellType() == CellType.NUMERIC) {
                        row.getCell(3).setCellType(CellType.STRING);
                    }
                    createVO.setRmaPosition(row.getCell(3).getStringCellValue());
                }
                if (row.getCell(4) == null) {
                    createVO.setRmaReason(null);
                } else {
                    if (row.getCell(4).getCellType() == CellType.NUMERIC) {
                        row.getCell(4).setCellType(CellType.STRING);
                    }
                    createVO.setRmaReason(row.getCell(4).getStringCellValue());
                }
                if (row.getCell(5) == null) {
                    createVO.setPostNumber(null);
                } else {
                    if (row.getCell(5).getCellType() == CellType.NUMERIC) {
                        row.getCell(5).setCellType(CellType.STRING);
                    }
                    createVO.setPostNumber(row.getCell(5).getStringCellValue());
                }

                if (row.getCell(6) == null) {
                    createVO.setProcessResult(null);
                } else {
                    if (row.getCell(6).getCellType() == CellType.NUMERIC) {
                        row.getCell(6).setCellType(CellType.STRING);
                    }
                    createVO.setProcessResult(row.getCell(6).getStringCellValue());
                }

                if (row.getCell(7) == null) {
                    createVO.setAgreeReplacement(null);
                } else {
                    if (row.getCell(7).getCellType() == CellType.NUMERIC) {
                        row.getCell(7).setCellType(CellType.STRING);
                    }
                    createVO.setAgreeReplacement(row.getCell(7).getStringCellValue());
                }

                if (row.getCell(8) == null) {
                    createVO.setReplacementResult(null);
                } else {
                    if (row.getCell(8).getCellType() == CellType.NUMERIC) {
                        row.getCell(8).setCellType(CellType.STRING);
                    }
                    createVO.setReplacementResult(row.getCell(8).getStringCellValue());
                }
                createVOs.add(createVO);
            }
            createVOs = createVOs.stream().map((createVO)->{
                Long sprinklerId = sprinklerDao.findIdBySprinklerSerial(createVO.getSprinklerSerial());
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
                return createVO;
            }).collect(Collectors.toList());

            SprinklerOperationHandler handler = operationHandlerRegistry.getHandler("rma");
            createVOs.forEach(handler::createOperationSheet);
            return ResponseDTO.ok();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public ResponseDTO<String> batchCreateUsableOperationSheet(@Valid MultipartFile file, RequestUser requestUser) {
        try (InputStream stream = file.getInputStream()) {
            // 使用POI解析为导入DTO
            Workbook workbook = new XSSFWorkbook(stream);
            List<SprinklerUsableOperationSheetCreateForm> createVOs = new ArrayList<>();
            XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);
            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || row.getRowNum() == 0) continue; // 跳过标题行
                if (row.getCell(0) == null) continue;
                if (row.getCell(0).getCellType() == CellType.NUMERIC) {
                    row.getCell(0).setCellType(CellType.STRING);
                }
                String headSerial = row.getCell(0).getStringCellValue();
                SprinklerUsableOperationSheetCreateForm createVO = new SprinklerUsableOperationSheetCreateForm();
                createVO.setSprinklerSerial(headSerial);
                if (row.getCell(1) == null) {
                    createVO.setHistory(null);
                } else {
                    if (row.getCell(1).getCellType() == CellType.NUMERIC) {
                        row.getCell(1).setCellType(CellType.STRING);
                    }
                    createVO.setHistory(row.getCell(1).getStringCellValue());
                }
                if (row.getCell(2) == null) {
                    createVO.setNote1(null);
                } else {
                    if (row.getCell(2).getCellType() == CellType.NUMERIC) {
                        row.getCell(2).setCellType(CellType.STRING);
                    }
                    createVO.setNote1(row.getCell(2).getStringCellValue());
                }
                if (row.getCell(3) == null) {
                    createVO.setNote2(null);
                } else {
                    if (row.getCell(3).getCellType() == CellType.NUMERIC) {
                        row.getCell(3).setCellType(CellType.STRING);
                    }
                    createVO.setNote2(row.getCell(3).getStringCellValue());
                }
                if (headSerial == "") continue;
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
            SprinklerOperationHandler handler = operationHandlerRegistry.getHandler("usable");
            createVOs.forEach(handler::createOperationSheet);
            return ResponseDTO.ok();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
