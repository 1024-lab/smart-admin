package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.listener;

import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.read.listener.ReadListener;
import lombok.Getter;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.bo.UsableSprinklerCreateBO;
import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.form.UsableSprinklerCreateForm;

import java.util.ArrayList;
import java.util.List;


@Getter
public class UsableSprinklerCreateBOParseListener implements ReadListener<UsableSprinklerCreateBO> {

    private List<UsableSprinklerCreateForm> UsableSprinklerCreateVOList = new ArrayList<>();

    @Override
    public void invoke(UsableSprinklerCreateBO createBO, AnalysisContext context) {
        UsableSprinklerCreateForm createVO = new UsableSprinklerCreateForm();
        createVO.setSprinklerSerial(createBO.getSprinklerSerial());
        createVO.setReturnUsableRepoDate(createBO.getReturnUsableRepoDate());
        createVO.setHistory(createBO.getHistory());
        createVO.setIsLimitedAllocation(createBO.getIsLimitedAllocation());
        createVO.setAllocationNote1(createBO.getAllocationNote1());
        createVO.setStatus(createBO.getStatus());
        UsableSprinklerCreateVOList.add(createVO);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }
}
