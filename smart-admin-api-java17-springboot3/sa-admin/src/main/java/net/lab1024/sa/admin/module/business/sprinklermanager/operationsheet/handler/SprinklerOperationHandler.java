package net.lab1024.sa.admin.module.business.sprinklermanager.operationsheet.handler;

import net.lab1024.sa.base.common.domain.ResponseDTO;

public interface SprinklerOperationHandler<F> {

    ResponseDTO<String> createOperationSheet(F form);

    String getOperationType();

}
