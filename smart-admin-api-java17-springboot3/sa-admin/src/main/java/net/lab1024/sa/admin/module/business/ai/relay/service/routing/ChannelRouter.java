package net.lab1024.sa.admin.module.business.ai.relay.service.routing;

import net.lab1024.sa.admin.module.business.ai.relay.constant.EndpointTypeEnum;
import net.lab1024.sa.admin.module.business.ai.relay.domain.entity.RelayChannelEntity;

import java.util.Set;

/**
 * 渠道路由接口
 */
public interface ChannelRouter {

    /**
     * 选择一个可用渠道
     *
     * @param modelName       目标模型名称
     * @param endpointType    端点类型
     * @param excludeChannelIds 需排除的渠道ID（已失败的）
     * @return 选中的渠道，无可用渠道返回 null
     */
    RelayChannelEntity selectChannel(String modelName, EndpointTypeEnum endpointType, Set<Long> excludeChannelIds);
}
