package net.lab1024.sa.admin.module.business.ai.relay.service.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.ai.relay.constant.QuotaActionTypeEnum;
import net.lab1024.sa.admin.module.business.ai.relay.constant.RelayErrorCode;
import net.lab1024.sa.admin.module.business.ai.relay.dao.RelayQuotaLedgerDao;
import net.lab1024.sa.admin.module.business.ai.relay.dao.RelayUserAccountDao;
import net.lab1024.sa.admin.module.business.ai.relay.domain.entity.RelayQuotaLedgerEntity;
import net.lab1024.sa.admin.module.business.ai.relay.domain.entity.RelayUserAccountEntity;
import net.lab1024.sa.admin.module.business.ai.relay.domain.form.RelayUserAccountAddForm;
import net.lab1024.sa.admin.module.business.ai.relay.domain.form.RelayUserAccountQueryForm;
import net.lab1024.sa.admin.module.business.ai.relay.domain.form.RelayUserAccountRechargeForm;
import net.lab1024.sa.admin.module.business.ai.relay.domain.form.RelayUserAccountUpdateForm;
import net.lab1024.sa.admin.module.business.ai.relay.domain.vo.RelayQuotaLedgerVO;
import net.lab1024.sa.admin.module.business.ai.relay.domain.vo.RelayUserAccountVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service
public class RelayUserAccountAdminService {

    @Resource
    private RelayUserAccountDao relayUserAccountDao;

    @Resource
    private RelayQuotaLedgerDao relayQuotaLedgerDao;

    public PageResult<RelayUserAccountVO> queryPage(RelayUserAccountQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<RelayUserAccountVO> list = relayUserAccountDao.queryPage(page, queryForm);
        return SmartPageUtil.convert2PageResult(page, list);
    }

    public ResponseDTO<String> add(RelayUserAccountAddForm form) {
        RelayUserAccountEntity exist = relayUserAccountDao.selectById(form.getEmployeeId());
        if (exist != null) {
            return ResponseDTO.userErrorParam("该员工账户已存在");
        }
        RelayUserAccountEntity account = new RelayUserAccountEntity();
        account.setEmployeeId(form.getEmployeeId());
        account.setQuotaRemain(form.getQuotaRemain());
        account.setQuotaUsed(BigDecimal.ZERO);
        account.setDisabledFlag(0);
        relayUserAccountDao.insert(account);
        return ResponseDTO.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> recharge(RelayUserAccountRechargeForm form) {
        RelayUserAccountEntity account = relayUserAccountDao.selectById(form.getEmployeeId());
        if (account == null) {
            // 自动创建账户
            account = new RelayUserAccountEntity();
            account.setEmployeeId(form.getEmployeeId());
            account.setQuotaRemain(form.getAmount());
            account.setQuotaUsed(BigDecimal.ZERO);
            account.setDisabledFlag(0);
            relayUserAccountDao.insert(account);
        } else {
            account.setQuotaRemain(account.getQuotaRemain().add(form.getAmount()));
            relayUserAccountDao.updateById(account);
        }
        // 记录流水
        RelayQuotaLedgerEntity ledger = new RelayQuotaLedgerEntity();
        ledger.setEmployeeId(form.getEmployeeId());
        ledger.setActionType(QuotaActionTypeEnum.RECHARGE.getValue());
        ledger.setDeltaQuota(form.getAmount());
        ledger.setBalanceAfter(account.getQuotaRemain());
        relayQuotaLedgerDao.insert(ledger);
        return ResponseDTO.ok();
    }

    public ResponseDTO<String> updateDisabledFlag(Long employeeId, Integer disabledFlag) {
        RelayUserAccountEntity account = relayUserAccountDao.selectById(employeeId);
        if (account == null) {
            return ResponseDTO.error(RelayErrorCode.ACCOUNT_NOT_FOUND);
        }
        account.setDisabledFlag(disabledFlag);
        relayUserAccountDao.updateById(account);
        return ResponseDTO.ok();
    }

    public PageResult<RelayQuotaLedgerVO> queryLedger(Long employeeId, Integer pageNum, Integer pageSize) {
        Page<?> page = new Page<>(pageNum, pageSize);
        List<RelayQuotaLedgerVO> list = relayQuotaLedgerDao.queryPageByEmployeeId(page, employeeId);
        return SmartPageUtil.convert2PageResult(page, list);
    }

    public ResponseDTO<String> update(RelayUserAccountUpdateForm form) {
        RelayUserAccountEntity account = relayUserAccountDao.selectById(form.getEmployeeId());
        if (account == null) {
            return ResponseDTO.error(RelayErrorCode.ACCOUNT_NOT_FOUND);
        }
        account.setQuotaRemain(form.getQuotaRemain());
        if (form.getDisabledFlag() != null) {
            account.setDisabledFlag(form.getDisabledFlag());
        }
        relayUserAccountDao.updateById(account);
        return ResponseDTO.ok();
    }

    public ResponseDTO<String> delete(Long employeeId) {
        RelayUserAccountEntity account = relayUserAccountDao.selectById(employeeId);
        if (account == null) {
            return ResponseDTO.error(RelayErrorCode.ACCOUNT_NOT_FOUND);
        }
        relayUserAccountDao.deleteById(employeeId);
        return ResponseDTO.ok();
    }
}
