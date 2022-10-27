package net.lab1024.sa.common.module.support.heartbeat;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.common.common.domain.PageParam;
import net.lab1024.sa.common.common.domain.PageResult;
import net.lab1024.sa.common.common.domain.ResponseDTO;
import net.lab1024.sa.common.common.util.SmartPageUtil;
import net.lab1024.sa.common.module.support.heartbeat.domain.HeartBeatRecordQueryForm;
import net.lab1024.sa.common.module.support.heartbeat.domain.HeartBeatRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 心跳记录
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-01-09 20:57:24
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Slf4j
@Service
public class HeartBeatService {

    @Autowired
    private HeartBeatRecordDao heartBeatRecordDao;

    public ResponseDTO<PageResult<HeartBeatRecordVO>> pageQuery(HeartBeatRecordQueryForm pageParam) {
        Page pageQueryInfo = SmartPageUtil.convert2PageQuery(pageParam);
        List<HeartBeatRecordVO> recordVOList = heartBeatRecordDao.pageQuery(pageQueryInfo,pageParam);
        PageResult<HeartBeatRecordVO> pageResult = SmartPageUtil.convert2PageResult(pageQueryInfo, recordVOList);
        return ResponseDTO.ok(pageResult);
    }
}
