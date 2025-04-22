package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.processor;

import net.lab1024.sa.base.common.domain.ResponseDTO;

import java.util.List;

public interface DataProcessor<T> {

    ResponseDTO<String> process(List<T> createVO);
}
