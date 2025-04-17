package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.listener;

import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.read.listener.ReadListener;
import lombok.Getter;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.bo.SprinklerCreateBO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.SprinklerCreateForm;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SprinklerCreateBOParseListener implements ReadListener<SprinklerCreateBO> {

    private List<SprinklerCreateForm> SprinklerCreateVOList = new ArrayList<>();

    @Override
    public void invoke(SprinklerCreateBO createBO, AnalysisContext context) {
        SprinklerCreateForm createVO = new SprinklerCreateForm();
        createVO.setPurchaseDateContractNumber(createBO.getPurchaseDateContractNumber());
        createVO.setSprinklerModel(createBO.getSprinklerModel());
        createVO.setSprinklerSerial(createBO.getSprinklerSerial());
        createVO.setShippingDate(createBO.getShippingDate());
        createVO.setWarehouseDate(createBO.getWarehouseDate());
        createVO.setVoltage(createBO.getVoltage());
        createVO.setJetsout(createBO.getJetsout());
        createVO.setHistory(createBO.getHistory());
        createVO.setStatus(createBO.getStatus());
        SprinklerCreateVOList.add(createVO);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }
}
