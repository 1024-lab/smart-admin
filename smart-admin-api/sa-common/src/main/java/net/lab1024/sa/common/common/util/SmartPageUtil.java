package net.lab1024.sa.common.common.util;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import net.lab1024.sa.common.common.domain.PageParam;
import net.lab1024.sa.common.common.domain.PageResult;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 分页工具类
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2020-04-23 20:51:40
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
public class SmartPageUtil {

    /**
     * 转换为查询参数
     *
     * @param baseDTO
     * @return
     */
    public static Page<?> convert2PageQuery(PageParam baseDTO) {
        Page<?> page = new Page<>(baseDTO.getPageNum(), baseDTO.getPageSize());
        // 设置排序字段
        List<PageParam.SortItem> sortItemList = baseDTO.getSortItemList();
        if (CollectionUtils.isNotEmpty(sortItemList)) {
            List<OrderItem> orderItemList = sortItemList.stream().map(e -> new OrderItem(e.getColumn(), e.getIsAsc())).collect(Collectors.toList());
            page.setOrders(orderItemList);
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
     */
    public static <T, E> PageResult<T> convert2PageResult(Page<?> page, List<E> sourceList, Class<T> targetClazz) {
        return convert2PageResult(page, SmartBeanUtil.copyList(sourceList, targetClazz));
    }

    /**
     * 转换为 PageResultDTO 对象
     *
     * @param page
     * @param sourceList list
     * @return
     */
    public static <E> PageResult<E> convert2PageResult(Page<?> page, List<E> sourceList) {
        PageResult<E> pageResult = new PageResult<>();
        pageResult.setPageNum(page.getCurrent());
        pageResult.setPageSize(page.getSize());
        pageResult.setTotal(page.getTotal());
        pageResult.setPages(page.getPages());
        pageResult.setList(sourceList);
        pageResult.setEmptyFlag(CollectionUtils.isEmpty(sourceList));
        return pageResult;
    }

    /**
     * 转换分页结果对象
     *
     * @param pageResult
     * @param targetClazz
     * @return
     */
    public static <E, T> PageResult<T> convert2PageResult(PageResult<E> pageResult, Class<T> targetClazz) {
        PageResult<T> newPageResult = new PageResult<>();
        newPageResult.setPageNum(pageResult.getPageNum());
        newPageResult.setPageSize(pageResult.getPageSize());
        newPageResult.setTotal(pageResult.getTotal());
        newPageResult.setPages(pageResult.getPages());
        newPageResult.setEmptyFlag(pageResult.getEmptyFlag());
        newPageResult.setList(SmartBeanUtil.copyList(pageResult.getList(), targetClazz));
        return newPageResult;
    }

    public static <T> PageResult subListPage(Integer pageNum, Integer pageSize, List<T> list) {
        PageResult<T> pageRet = new PageResult<T>();
        //总条数
        int count = list.size();
        int pages = count % pageSize == 0 ? count / pageSize : (count / pageSize + 1);
        int fromIndex = (pageNum - 1) * pageSize;
        int toIndex = pageNum * pageSize > count ? count : pageNum * pageSize;

        if (pageNum > pages) {
            pageRet.setList(Lists.newLinkedList());
            pageRet.setPageNum(pageNum.longValue());
            pageRet.setPages(Long.valueOf(pages));
            pageRet.setTotal(Long.valueOf(count));
            return pageRet;
        }
        List<T> pageList = list.subList(fromIndex, toIndex);
        pageRet.setList(pageList);
        pageRet.setPageNum(pageNum.longValue());
        pageRet.setPages(Long.valueOf(pages));
        pageRet.setTotal(Long.valueOf(count));
        return pageRet;
    }
}
