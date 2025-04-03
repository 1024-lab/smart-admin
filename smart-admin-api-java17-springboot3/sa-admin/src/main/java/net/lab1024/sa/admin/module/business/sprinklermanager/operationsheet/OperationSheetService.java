package net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.form.Impl.StockInOperationSheetCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.domain.form.OperationSheetCreateForm;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.handler.SprinklerOperationHandler;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.registry.OperationHandlerRegistry;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.dao.SprinklerDao;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerCreateForm;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.LocalDateParseUtil;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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


    /**
     * 新建记录表
     *
     */
    public ResponseDTO<String> batchCreateStockInOperationSheet(@Valid MultipartFile file, RequestUser requestUser){

        try(InputStream stream = file.getInputStream()) {
            // 使用POI解析为导入DTO
            Workbook workbook = new XSSFWorkbook(stream);
            List<StockInOperationSheetCreateForm> createVOs = new ArrayList<>();
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(i);
                for (int j =0; j < sheet.getLastRowNum(); j++) {
                    Row row = sheet.getRow(j);
                    if (row == null||row.getRowNum() == 0) continue; // 跳过标题行
                    if (row.getCell(2) == null) continue;
                    String headSerial = row.getCell(2).getStringCellValue();
                    StockInOperationSheetCreateForm createVO = new StockInOperationSheetCreateForm();
                    createVO.setSprinklerSerial(headSerial);
                    if (row.getCell(0).getCellType() == CellType.NUMERIC) {
                        row.getCell(0).setCellType(CellType.STRING);
                    }
                    String purchaseContract = row.getCell(0).getStringCellValue();
                    Matcher matcher = Pattern.compile("[（(]").matcher(purchaseContract);
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

                    if (headSerial != "") {
                        createVO.setCreateUserId(requestUser.getUserId());
                        createVO.setCreateUserName(requestUser.getUserName());
                        createVOs.add(createVO);
                    }

                }
            }
            SprinklerOperationHandler handler = operationHandlerRegistry.getHandler("stock_in");
            List<StockInOperationSheetCreateForm> oscreateVOs = createVOs.stream().map(createVO->{
                StockInOperationSheetCreateForm oscreateVO = new StockInOperationSheetCreateForm();
                oscreateVO.setSprinklerId(sprinklerDao.findIdBySprinklerSerial(createVO.getSprinklerSerial()));
                oscreateVO.setDisabledFlag(Boolean.FALSE);
                oscreateVO.setCreateUserId(requestUser.getUserId());
                oscreateVO.setCreateUserName(requestUser.getUserName());
                return oscreateVO;
            }).collect(Collectors.toList());
            oscreateVOs.forEach(handler::createOperationSheet);
//            createVOs.forEach(handler::handle);
            return ResponseDTO.ok();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
