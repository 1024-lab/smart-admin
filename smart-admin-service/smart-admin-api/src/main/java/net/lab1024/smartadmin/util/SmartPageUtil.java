package net.lab1024.smartadmin.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.smartadmin.common.domain.OrderItemDTO;
import net.lab1024.smartadmin.common.domain.PageParamDTO;
import net.lab1024.smartadmin.common.domain.PageResultDTO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 分页工具类
 *
 * @author GHQ
 * @date 2017-12-23 16:40
 */

public class SmartPageUtil {

    public static <T> PageResultDTO<T> convert2PageResult(IPage<T> page) {
        PageResultDTO<T> result = new PageResultDTO<>();
        result.setPageNum(page.getCurrent());
        result.setPageSize(page.getSize());
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        result.setList(page.getRecords());
        return result;
    }

    public static <T> Page<T> convert2QueryPage(PageParamDTO baseDTO) {
        Page<T> page = new Page<>();

        List<OrderItemDTO> orders = baseDTO.getOrders();
        if (orders != null && !orders.isEmpty()) {
            List<com.baomidou.mybatisplus.core.metadata.OrderItem> orderItemList = orders.stream().map(SmartPageUtil::convertOrderItem).collect(Collectors.toList());
            page.setOrders(orderItemList);
        }
        page.setCurrent(baseDTO.getPageNum());
        page.setSize(baseDTO.getPageSize());
        if (null != baseDTO.getSearchCount()) {
            page.setSearchCount(baseDTO.getSearchCount());
        }
        return page;
    }

    private static com.baomidou.mybatisplus.core.metadata.OrderItem convertOrderItem(OrderItemDTO orderItemDTO) {
        if (orderItemDTO.isAsc()) {
            return com.baomidou.mybatisplus.core.metadata.OrderItem.asc(orderItemDTO.getColumn());
        } else {
            return com.baomidou.mybatisplus.core.metadata.OrderItem.desc(orderItemDTO.getColumn());
        }
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
    public static <T, E> PageResultDTO<T> convert2PageResult(IPage page, List<E> sourceList, Class<T> targetClazz) {
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
    public static <T, E> PageResultDTO<T> convert2PageResult(IPage page, List<E> sourceList) {
        PageResultDTO pageResultDTO = setPage(page);
        page.setRecords(sourceList);
        pageResultDTO.setList(sourceList);
        return pageResultDTO;
    }

    private static PageResultDTO setPage(IPage page) {
        PageResultDTO pageResultDTO = new PageResultDTO();
        pageResultDTO.setPageNum(page.getCurrent());
        pageResultDTO.setPageSize(page.getSize());
        pageResultDTO.setTotal(page.getTotal());
        pageResultDTO.setPages(page.getPages());
        return pageResultDTO;
    }
}
