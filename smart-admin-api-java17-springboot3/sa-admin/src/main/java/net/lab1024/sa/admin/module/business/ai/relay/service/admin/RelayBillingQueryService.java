package net.lab1024.sa.admin.module.business.ai.relay.service.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.ai.relay.dao.RelayBillingSessionDao;
import net.lab1024.sa.admin.module.business.ai.relay.domain.form.RelayBillingSessionQueryForm;
import net.lab1024.sa.admin.module.business.ai.relay.domain.vo.RelayBillingSessionVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;

@Service
public class RelayBillingQueryService {

    @Resource
    private RelayBillingSessionDao relayBillingSessionDao;

    public PageResult<RelayBillingSessionVO> queryPage(RelayBillingSessionQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<RelayBillingSessionVO> list = relayBillingSessionDao.queryPage(page, queryForm);
        return SmartPageUtil.convert2PageResult(page, list);
    }
}
