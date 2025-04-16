package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.listener;

import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.read.listener.ReadListener;
import lombok.Getter;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.bo.SprinklerStockInCreateBO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerStockInCreateForm;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SprinklerStockInCreateBOParseListener implements ReadListener<SprinklerStockInCreateBO> {

    private List<SprinklerStockInCreateForm> SprinklerStockInCreateVOList = new ArrayList<>();

    @Override
    public void invoke(SprinklerStockInCreateBO createBO, AnalysisContext context) {
        if(createBO.getStatus()==0){
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
            SprinklerStockInCreateVOList.add(createVO);
        }

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }
}
