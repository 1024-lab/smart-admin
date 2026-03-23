package net.lab1024.sa.admin.module.business.ai.relay.manager;

import net.lab1024.sa.admin.module.business.ai.relay.dao.RelayBillingSessionDao;
import net.lab1024.sa.admin.module.business.ai.relay.dao.RelayQuotaLedgerDao;
import net.lab1024.sa.admin.module.business.ai.relay.dao.RelayUsageLogDao;
import net.lab1024.sa.admin.module.business.ai.relay.domain.entity.RelayBillingSessionEntity;
import net.lab1024.sa.admin.module.business.ai.relay.domain.entity.RelayQuotaLedgerEntity;
import net.lab1024.sa.admin.module.business.ai.relay.domain.entity.RelayUsageLogEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;

/**
 * 计费相关事务写入管理器
 */
@Service
public class RelayBillingManager {

    @Resource
    private RelayBillingSessionDao billingSessionDao;

    @Resource
    private RelayQuotaLedgerDao quotaLedgerDao;

    @Resource
    private RelayUsageLogDao usageLogDao;

    @Transactional(rollbackFor = Exception.class)
    public void saveBillingSessionAndLedger(RelayBillingSessionEntity session, RelayQuotaLedgerEntity ledger) {
        billingSessionDao.insert(session);
        quotaLedgerDao.insert(ledger);
    }

    @Transactional(rollbackFor = Exception.class)
    public void settleAndLog(RelayBillingSessionEntity session, RelayQuotaLedgerEntity ledger, RelayUsageLogEntity usageLog) {
        billingSessionDao.updateById(session);
        if (ledger != null) {
            quotaLedgerDao.insert(ledger);
        }
        usageLogDao.insert(usageLog);
    }

    @Transactional(rollbackFor = Exception.class)
    public void rollbackAndLog(RelayBillingSessionEntity session, RelayQuotaLedgerEntity ledger, RelayUsageLogEntity usageLog) {
        billingSessionDao.updateById(session);
        quotaLedgerDao.insert(ledger);
        if (usageLog != null) {
            usageLogDao.insert(usageLog);
        }
    }
}
