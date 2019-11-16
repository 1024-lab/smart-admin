package com.gangquan360.smartadmin.module.log.orderoperatelog;

import com.gangquan360.smartadmin.common.domain.ResponseDTO;
import com.gangquan360.smartadmin.module.log.orderoperatelog.domain.dto.OrderOperateLogSaveDTO;
import com.gangquan360.smartadmin.module.log.orderoperatelog.domain.entity.OrderOperateLogEntity;
import com.gangquan360.smartadmin.module.log.orderoperatelog.domain.vo.OrderOperateLogVO;
import com.gangquan360.smartadmin.util.SmartBeanUtil;
import com.gangquan360.smartadmin.util.SmartStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 各种单据操作记录
 * 服务实现类
 * </p>
 *
 * @author anders
 * @since 2018-01-09
 */
@Service
public class OrderOperateLogService {

    @Autowired
    private OrderOperateLogDao orderOperateLogDao;

    public void batchSaveOrderOperateLog(List<OrderOperateLogSaveDTO> orderOperateLogSaveDTOList) {
        List<OrderOperateLogEntity> entityList = new ArrayList<>();
        orderOperateLogSaveDTOList.forEach(e -> {
            OrderOperateLogEntity orderOperateLogEntity = SmartBeanUtil.copy(e, OrderOperateLogEntity.class);
            orderOperateLogEntity.setOperateType(e.getOperateType().getCode());
            if (SmartStringUtil.isNotBlank(e.getOperateContent())) {
                orderOperateLogEntity.setOperateContent(e.getOperateContent());
            } else {
                orderOperateLogEntity.setOperateContent(e.getOperateType().getMsg());
            }
            orderOperateLogEntity.setOperateRemark(e.getOperateRemark());
            orderOperateLogEntity.setExtData(e.getExtData());
            orderOperateLogEntity.setCreateTime(new Date());
            orderOperateLogEntity.setOrderType(e.getOrderType().getType());
            entityList.add(orderOperateLogEntity);
        });
        //批量添加
        orderOperateLogDao.batchInsert(entityList);
    }

    public ResponseDTO<List<OrderOperateLogVO>> listOrderOperateLogsByOrderTypeAndOrderId(Long orderId, List<Integer> orderTypeList) {
        List<OrderOperateLogEntity> orderOperateLogEntities = orderOperateLogDao.listOrderOperateLogsByOrderTypeAndOrderId(orderId, orderTypeList);
        List<OrderOperateLogVO> dtoList = orderOperateLogEntities.stream().map(e -> SmartBeanUtil.copy(e, OrderOperateLogVO.class)).collect(Collectors.toList());
        return ResponseDTO.succData(dtoList);
    }

    public ResponseDTO<List<OrderOperateLogVO>> listOrderOperateLogsByOrderTypeAndOrderIds(List<Long> orderIds, List<Integer> orderTypeList) {
        List<OrderOperateLogEntity> orderOperateLogEntities = orderOperateLogDao.listOrderOperateLogsByOrderTypeAndOrderIds(orderIds, orderTypeList);
        List<OrderOperateLogVO> dtoList = orderOperateLogEntities.stream().map(e -> SmartBeanUtil.copy(e, OrderOperateLogVO.class)).collect(Collectors.toList());
        return ResponseDTO.succData(dtoList);
    }
}
