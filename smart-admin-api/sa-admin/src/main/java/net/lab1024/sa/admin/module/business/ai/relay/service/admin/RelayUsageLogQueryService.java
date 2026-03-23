package net.lab1024.sa.admin.module.business.ai.relay.service.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.ai.relay.dao.RelayUsageLogDao;
import net.lab1024.sa.admin.module.business.ai.relay.domain.form.RelayUsageLogQueryForm;
import net.lab1024.sa.admin.module.business.ai.relay.domain.vo.RelayUsageLogVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;

@Service
public class RelayUsageLogQueryService {

    @Resource
    private RelayUsageLogDao relayUsageLogDao;

    public PageResult<RelayUsageLogVO> queryPage(RelayUsageLogQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<RelayUsageLogVO> list = relayUsageLogDao.queryPage(page, queryForm);
        return SmartPageUtil.convert2PageResult(page, list);
    }
}
