package com.gangquan360.smartadmin.util;

import com.baomidou.mybatisplus.plugins.Page;
import com.gangquan360.smartadmin.common.domain.PageParamDTO;
import com.gangquan360.smartadmin.common.domain.PageResultDTO;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 * 分页工具类
 *
 * @author GHQ
 * @date 2017-12-23 16:40
 */

public class SmartPaginationUtil {

    public static <T> PageResultDTO<T> convert2PageResultDTO(Page<T> page) {
        PageResultDTO<T> result = new PageResultDTO<>();
        result.setPageNum(page.getCurrent());
        result.setPageSize(page.getSize());
        result.setTotal(Long.valueOf(page.getTotal()));
        result.setPages(page.getPages());
        result.setList(page.getRecords());
        return result;
    }

    public static Page<T> convert2PageQueryInfo(PageParamDTO baseDTO) {
        Page<T> page = new Page<>();
        Boolean sort = baseDTO.getSort();
        if (null != sort && SmartStringUtil.isNoneBlank(baseDTO.getOrderByField())) {
            page.setAsc(sort);
            page.setOrderByField(baseDTO.getOrderByField());
        }
        page.setCurrent(baseDTO.getPageNum());
        page.setSize(baseDTO.getPageSize());
        if (null != baseDTO.getSearchCount()) {
            page.setSearchCount(baseDTO.getSearchCount());
        }
        return page;
    }

    /**
     * 转换为 PageResultDTO 对象
     *
     * @param page
     * @param sourceList  原list
     * @param targetClazz 目标类
     * @return
     * @author yandanyang
     * @date 2018年5月16日 下午6:05:28
     */
    public static <T, E> PageResultDTO<T> convert2PageResultDTO(Page page, List<E> sourceList, Class<T> targetClazz) {
        PageResultDTO pageResultDTO = setPage(page);
        List<T> records = SmartBeanUtil.copyList(sourceList, targetClazz);
        page.setRecords(records);
        pageResultDTO.setList(records);
        return pageResultDTO;
    }

    /**
     * 转换为 PageResultDTO 对象
     *
     * @param page
     * @param sourceList list
     * @return
     * @author yandanyang
     * @date 2018年5月16日 下午6:05:28
     */
    public static <T, E> PageResultDTO<T> convert2PageResultDTO(Page page, List<E> sourceList) {
        PageResultDTO pageResultDTO = setPage(page);
        page.setRecords(sourceList);
        pageResultDTO.setList(sourceList);
        return pageResultDTO;
    }

    private static PageResultDTO setPage(Page page) {
        PageResultDTO pageResultDTO = new PageResultDTO();
        pageResultDTO.setPageNum(page.getCurrent());
        pageResultDTO.setPageSize(page.getSize());
        pageResultDTO.setTotal(Long.valueOf(page.getTotal()));
        pageResultDTO.setPages(page.getPages());
        return pageResultDTO;
    }
}
