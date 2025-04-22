package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.factory;

import net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.processor.DataProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DataProcessorFactory {
    @Autowired
    private Map<String, DataProcessor> processorMap; // key为场景类型

    public DataProcessor getProcessor(String sceneType) {
        return processorMap.get(sceneType);
    }
}
