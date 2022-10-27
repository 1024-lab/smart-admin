package net.lab1024.sa.common.module.support.serialnumber.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.common.common.domain.PageResult;
import net.lab1024.sa.common.common.domain.ResponseDTO;
import net.lab1024.sa.common.common.util.SmartPageUtil;
import net.lab1024.sa.common.module.support.operatelog.domain.OperateLogEntity;
import net.lab1024.sa.common.module.support.operatelog.domain.OperateLogVO;
import net.lab1024.sa.common.module.support.serialnumber.constant.SerialNumberIdEnum;
import net.lab1024.sa.common.module.support.serialnumber.dao.SerialNumberDao;
import net.lab1024.sa.common.module.support.serialnumber.dao.SerialNumberRecordDao;
import net.lab1024.sa.common.module.support.serialnumber.domain.SerialNumberRecordEntity;
import net.lab1024.sa.common.module.support.serialnumber.domain.SerialNumberRecordQueryForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 单据序列号 记录
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-03-25 21:46:07
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Service
public class SerialNumberRecordService {

    @Autowired
    private SerialNumberRecordDao serialNumberRecordDao;

    public PageResult<SerialNumberRecordEntity> query(SerialNumberRecordQueryForm queryForm) {
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<SerialNumberRecordEntity> recordList = serialNumberRecordDao.query(page, queryForm);
        return SmartPageUtil.convert2PageResult(page, recordList);
    }
}
