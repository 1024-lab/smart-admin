package net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.registry;

import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.handler.Impl.BaseSprinklerHandler;
import net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.handler.SprinklerOperationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.OperationType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class OperationHandlerRegistry {

    private final Map<String, SprinklerOperationHandler> handlerMap = new HashMap<>();

    // 自动注入所有处理器
    @Autowired
    public OperationHandlerRegistry(List<SprinklerOperationHandler> handlers) {
        for (SprinklerOperationHandler handler : handlers) {
            String type = handler.getOperationType();
            handlerMap.put(type, handler);
        }
    }

    public SprinklerOperationHandler getHandler(String type) {
        SprinklerOperationHandler handler = handlerMap.get(type);
        if (handler == null) {
            throw new IllegalArgumentException("Unsupported operation type: " + type);
        }
        return handler;
    }
}
