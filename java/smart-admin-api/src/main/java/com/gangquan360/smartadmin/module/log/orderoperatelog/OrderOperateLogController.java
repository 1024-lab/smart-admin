package com.gangquan360.smartadmin.module.log.orderoperatelog;

import com.gangquan360.smartadmin.common.anno.OperateLog;
import com.gangquan360.smartadmin.common.domain.ResponseDTO;
import com.gangquan360.smartadmin.constant.SwaggerTagConst;
import com.gangquan360.smartadmin.module.log.orderoperatelog.constant.OrderOperateLogOrderTypeEnum;
import com.gangquan360.smartadmin.module.log.orderoperatelog.domain.vo.OrderOperateLogVO;
import com.gangquan360.smartadmin.util.SmartStringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 单据操作记录Controller
 *
 * @author lidoudou
 * @date: 2018/1/31 16:56
 */

@Api(tags = {SwaggerTagConst.Admin.MANAGER_ORDER_OPERATE_LOG})
@OperateLog
@RestController
public class OrderOperateLogController {

    @Autowired
    private OrderOperateLogService orderOperateLogService;

    @ApiOperation(value = "查询单据操作日志", notes = "查询单据操作日志")
    @GetMapping("/orderOperateLog/list/{orderId}")
    @ApiImplicitParams({@ApiImplicitParam(name = "orderId", value = "业务id", paramType = "path"), @ApiImplicitParam(name = "orderType", value = "业务类型" + OrderOperateLogOrderTypeEnum.INFO, paramType
        = "query")})
    public ResponseDTO<List<OrderOperateLogVO>> list(@PathVariable Long orderId, String orderType) {
        List<Integer> orderTypeList = SmartStringUtil.splitConverToIntSet(orderType, ",").stream().collect(Collectors.toList());
        return orderOperateLogService.listOrderOperateLogsByOrderTypeAndOrderId(orderId, orderTypeList);
    }
}
