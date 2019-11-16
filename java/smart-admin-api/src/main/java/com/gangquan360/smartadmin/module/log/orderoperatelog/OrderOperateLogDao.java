package com.gangquan360.smartadmin.module.log.orderoperatelog;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gangquan360.smartadmin.module.log.orderoperatelog.domain.entity.OrderOperateLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 各种单据操作记录
 * Mapper 接口
 * </p>
 *
 * @author anders
 * @since 2018-01-09
 */
@Mapper
@Component
public interface OrderOperateLogDao extends BaseMapper<OrderOperateLogEntity> {

    List<OrderOperateLogEntity> listOrderOperateLogsByOrderTypeAndOrderId(@Param("orderId") Long orderId, @Param("orderTypeList") List<Integer> orderTypeList);

    List<OrderOperateLogEntity> listOrderOperateLogsByOrderTypeAndOrderIds(@Param("orderIds") List<Long> orderIds, @Param("orderTypeList") List<Integer> orderTypeList);

    void batchInsert(List<OrderOperateLogEntity> list);

}
