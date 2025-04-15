package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.listener;

import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.read.listener.ReadListener;
import lombok.Getter;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.bo.SprinklerStockInCreateBO;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SprinklerStockInCreateBOParseListener implements ReadListener<SprinklerStockInCreateBO> {

    private List<SprinklerStockInCreateBO> SprinklerStockInCreateBOList = new ArrayList<>();

    @Override
    public void invoke(SprinklerStockInCreateBO data, AnalysisContext context) {
        if(data.getStatus()==0)
            SprinklerStockInCreateBOList.add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }
}
