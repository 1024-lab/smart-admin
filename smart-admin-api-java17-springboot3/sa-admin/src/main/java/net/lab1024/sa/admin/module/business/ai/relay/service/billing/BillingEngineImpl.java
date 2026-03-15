package net.lab1024.sa.admin.module.business.ai.relay.service.billing;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.ai.relay.constant.BillingStateEnum;
import net.lab1024.sa.admin.module.business.ai.relay.constant.EndpointTypeEnum;
import net.lab1024.sa.admin.module.business.ai.relay.constant.QuotaActionTypeEnum;
import net.lab1024.sa.admin.module.business.ai.relay.constant.RelayErrorCode;
import net.lab1024.sa.admin.module.business.ai.relay.dao.RelayBillingSessionDao;
import net.lab1024.sa.admin.module.business.ai.relay.dao.RelayUserAccountDao;
import net.lab1024.sa.admin.module.business.ai.relay.domain.entity.RelayBillingSessionEntity;
import net.lab1024.sa.admin.module.business.ai.relay.domain.entity.RelayModelEntity;
import net.lab1024.sa.admin.module.business.ai.relay.domain.entity.RelayQuotaLedgerEntity;
import net.lab1024.sa.admin.module.business.ai.relay.domain.entity.RelayUsageLogEntity;
import net.lab1024.sa.admin.module.business.ai.relay.domain.entity.RelayUserAccountEntity;
import net.lab1024.sa.admin.module.business.ai.relay.manager.RelayBillingManager;
import net.lab1024.sa.admin.module.business.ai.relay.provider.UsageResult;
import net.lab1024.sa.base.common.exception.BusinessException;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Slf4j
@Service
public class BillingEngineImpl implements BillingEngine {

    @Resource
    private RelayUserAccountDao relayUserAccountDao;

    @Resource
    private RelayBillingSessionDao relayBillingSessionDao;

    @Resource
    private RelayBillingManager relayBillingManager;

    @Override
    public Long preconsume(Long employeeId, Long tokenId, Long channelId,
                           Long modelId, EndpointTypeEnum endpointType,
                           String requestId, long estimatedQuota) {
        // 原子扣减
        int rows = relayUserAccountDao.preconsumeQuota(employeeId, estimatedQuota);
        if (rows == 0) {
            throw new BusinessException(RelayErrorCode.INSUFFICIENT_QUOTA);
        }

        // 查询扣减后余额
        RelayUserAccountEntity account = relayUserAccountDao.selectById(employeeId);
        long balanceAfter = account.getQuotaRemain();

        // 创建计费会话
        RelayBillingSessionEntity session = new RelayBillingSessionEntity();
        session.setRequestId(requestId);
        session.setEmployeeId(employeeId);
        session.setTokenId(tokenId);
        session.setChannelId(channelId);
        session.setModelId(modelId);
        session.setEndpointType(endpointType.getValue());
        session.setPreconsumeQuota(estimatedQuota);
        session.setBillingState(BillingStateEnum.PRECONSUMED.getValue());

        // 创建流水
        RelayQuotaLedgerEntity ledger = new RelayQuotaLedgerEntity();
        ledger.setEmployeeId(employeeId);
        ledger.setTokenId(tokenId);
        ledger.setRequestId(requestId);
        ledger.setActionType(QuotaActionTypeEnum.PRECONSUME.getValue());
        ledger.setDeltaQuota(-estimatedQuota);
        ledger.setBalanceAfter(balanceAfter);

        relayBillingManager.saveBillingSessionAndLedger(session, ledger);

        // 回填 billingSessionId 到 ledger
        ledger.setBillingSessionId(session.getBillingSessionId());

        return session.getBillingSessionId();
    }

    @Override
    public void settle(Long billingSessionId, UsageResult usage, RelayModelEntity model,
                       int upstreamStatus, int gatewayStatus, long latencyMs,
                       boolean streamFlag, Long channelId) {
        // 计算实际费用
        BigDecimal inputCost = model.getInputPricePerK()
                .multiply(BigDecimal.valueOf(usage.getPromptTokens()))
                .divide(BigDecimal.valueOf(1000), 6, RoundingMode.HALF_UP);
        BigDecimal outputCost = model.getOutputPricePerK()
                .multiply(BigDecimal.valueOf(usage.getCompletionTokens()))
                .divide(BigDecimal.valueOf(1000), 6, RoundingMode.HALF_UP);
        BigDecimal totalCost = inputCost.add(outputCost);

        // 实际额度 = totalCost 转 long（乘以倍率，这里假设 1:1 映射，单位为最小额度单位）
        long settledQuota = totalCost.setScale(0, RoundingMode.CEILING).longValue();

        // 查询原始会话
        RelayBillingSessionEntity original = relayBillingSessionDao.selectById(billingSessionId);
        long preconsumeQuota = original.getPreconsumeQuota();
        long delta = preconsumeQuota - settledQuota;

        // 更新会话
        original.setSettledQuota(settledQuota);
        original.setDeltaQuota(delta);
        original.setBillingState(BillingStateEnum.SETTLED.getValue());
        original.setUpstreamStatus(upstreamStatus);
        original.setGatewayStatus(gatewayStatus);
        original.setLatencyMs(latencyMs);
        original.setChannelId(channelId);
        original.setSettledTime(LocalDateTime.now());

        // 差额处理
        RelayQuotaLedgerEntity ledger = null;
        if (delta > 0) {
            // 预扣多了，退还差额
            relayUserAccountDao.restoreQuota(original.getEmployeeId(), delta);
            RelayUserAccountEntity account = relayUserAccountDao.selectById(original.getEmployeeId());
            ledger = new RelayQuotaLedgerEntity();
            ledger.setEmployeeId(original.getEmployeeId());
            ledger.setTokenId(original.getTokenId());
            ledger.setBillingSessionId(billingSessionId);
            ledger.setRequestId(original.getRequestId());
            ledger.setActionType(QuotaActionTypeEnum.SETTLE_REFUND.getValue());
            ledger.setDeltaQuota(delta);
            ledger.setBalanceAfter(account.getQuotaRemain());
        } else if (delta < 0) {
            // 预扣少了，补扣
            long extraAmount = -delta;
            relayUserAccountDao.preconsumeQuota(original.getEmployeeId(), extraAmount);
            RelayUserAccountEntity account = relayUserAccountDao.selectById(original.getEmployeeId());
            ledger = new RelayQuotaLedgerEntity();
            ledger.setEmployeeId(original.getEmployeeId());
            ledger.setTokenId(original.getTokenId());
            ledger.setBillingSessionId(billingSessionId);
            ledger.setRequestId(original.getRequestId());
            ledger.setActionType(QuotaActionTypeEnum.SETTLE_EXTRA.getValue());
            ledger.setDeltaQuota(-extraAmount);
            ledger.setBalanceAfter(account.getQuotaRemain());
        }

        // 用量日志
        RelayUsageLogEntity usageLog = buildUsageLog(original, usage, inputCost, outputCost, totalCost,
                settledQuota, upstreamStatus, gatewayStatus, latencyMs, streamFlag, channelId);

        relayBillingManager.settleAndLog(original, ledger, usageLog);
    }

    @Override
    public void rollback(Long billingSessionId, int upstreamStatus,
                         int gatewayStatus, long latencyMs, String errorMsg) {
        RelayBillingSessionEntity original = relayBillingSessionDao.selectById(billingSessionId);
        if (original == null) {
            log.error("回滚失败，计费会话不存在: {}", billingSessionId);
            return;
        }

        long preconsumeQuota = original.getPreconsumeQuota();

        // 全额退还
        relayUserAccountDao.restoreQuota(original.getEmployeeId(), preconsumeQuota);
        RelayUserAccountEntity account = relayUserAccountDao.selectById(original.getEmployeeId());

        // 更新会话
        original.setSettledQuota(0L);
        original.setDeltaQuota(preconsumeQuota);
        original.setBillingState(BillingStateEnum.REFUNDED.getValue());
        original.setUpstreamStatus(upstreamStatus);
        original.setGatewayStatus(gatewayStatus);
        original.setLatencyMs(latencyMs);
        original.setErrorMsg(errorMsg);
        original.setSettledTime(LocalDateTime.now());

        // 流水
        RelayQuotaLedgerEntity ledger = new RelayQuotaLedgerEntity();
        ledger.setEmployeeId(original.getEmployeeId());
        ledger.setTokenId(original.getTokenId());
        ledger.setBillingSessionId(billingSessionId);
        ledger.setRequestId(original.getRequestId());
        ledger.setActionType(QuotaActionTypeEnum.ROLLBACK.getValue());
        ledger.setDeltaQuota(preconsumeQuota);
        ledger.setBalanceAfter(account.getQuotaRemain());

        // 错误日志
        RelayUsageLogEntity usageLog = new RelayUsageLogEntity();
        usageLog.setRequestId(original.getRequestId());
        usageLog.setBillingSessionId(billingSessionId);
        usageLog.setEmployeeId(original.getEmployeeId());
        usageLog.setTokenId(original.getTokenId());
        usageLog.setChannelId(original.getChannelId());
        usageLog.setModelId(original.getModelId());
        usageLog.setEndpointType(original.getEndpointType());
        usageLog.setPreconsumeQuota(preconsumeQuota);
        usageLog.setSettledQuota(0L);
        usageLog.setUpstreamStatus(upstreamStatus);
        usageLog.setGatewayStatus(gatewayStatus);
        usageLog.setLatencyMs(latencyMs);
        usageLog.setErrorMsg(errorMsg);

        relayBillingManager.rollbackAndLog(original, ledger, usageLog);
    }

    private RelayUsageLogEntity buildUsageLog(RelayBillingSessionEntity session, UsageResult usage,
                                               BigDecimal inputCost, BigDecimal outputCost, BigDecimal totalCost,
                                               long settledQuota, int upstreamStatus, int gatewayStatus,
                                               long latencyMs, boolean streamFlag, Long channelId) {
        RelayUsageLogEntity log = new RelayUsageLogEntity();
        log.setRequestId(session.getRequestId());
        log.setBillingSessionId(session.getBillingSessionId());
        log.setEmployeeId(session.getEmployeeId());
        log.setTokenId(session.getTokenId());
        log.setChannelId(channelId);
        log.setModelId(session.getModelId());
        log.setEndpointType(session.getEndpointType());
        log.setStreamFlag(streamFlag ? 1 : 0);
        log.setPromptTokens(usage.getPromptTokens());
        log.setCompletionTokens(usage.getCompletionTokens());
        log.setTotalTokens(usage.getTotalTokens());
        log.setInputCost(inputCost);
        log.setOutputCost(outputCost);
        log.setTotalCost(totalCost);
        log.setPreconsumeQuota(session.getPreconsumeQuota());
        log.setSettledQuota(settledQuota);
        log.setUpstreamStatus(upstreamStatus);
        log.setGatewayStatus(gatewayStatus);
        log.setLatencyMs(latencyMs);
        return log;
    }
}
