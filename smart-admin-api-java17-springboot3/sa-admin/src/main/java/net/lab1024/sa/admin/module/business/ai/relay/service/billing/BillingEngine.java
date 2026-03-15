package net.lab1024.sa.admin.module.business.ai.relay.service.billing;

import net.lab1024.sa.admin.module.business.ai.relay.constant.EndpointTypeEnum;
import net.lab1024.sa.admin.module.business.ai.relay.domain.entity.RelayModelEntity;
import net.lab1024.sa.admin.module.business.ai.relay.provider.UsageResult;

/**
 * 计费引擎接口
 */
public interface BillingEngine {

    /**
     * 预扣额度
     *
     * @return billingSessionId
     */
    Long preconsume(Long employeeId, Long tokenId, Long channelId,
                    Long modelId, EndpointTypeEnum endpointType,
                    String requestId, long estimatedQuota);

    /**
     * 结算（成功场景）
     */
    void settle(Long billingSessionId, UsageResult usage, RelayModelEntity model,
                int upstreamStatus, int gatewayStatus, long latencyMs,
                boolean streamFlag, Long channelId);

    /**
     * 回滚（失败场景，全额退还预扣）
     */
    void rollback(Long billingSessionId, int upstreamStatus,
                  int gatewayStatus, long latencyMs, String errorMsg);
}
