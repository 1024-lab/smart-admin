package net.lab1024.sa.base.module.support.operatelog;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.base.common.code.UserErrorCode;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.module.support.operatelog.domain.OperateLogEntity;
import net.lab1024.sa.base.module.support.operatelog.domain.OperateLogQueryForm;
import net.lab1024.sa.base.module.support.operatelog.domain.OperateLogVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *  操作日志
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2021-12-08 20:48:52
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Service
public class OperateLogService {

    @Resource
    private OperateLogDao operateLogDao;

    /**
     * @author 罗伊
     * @description 分页查询
     */
    public ResponseDTO<PageResult<OperateLogVO>> queryByPage(OperateLogQueryForm queryForm) {
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<OperateLogEntity> logEntityList = operateLogDao.queryByPage(page, queryForm);
        PageResult<OperateLogVO> pageResult = SmartPageUtil.convert2PageResult(page, logEntityList, OperateLogVO.class);
        return ResponseDTO.ok(pageResult);
    }


    /**
     * 查询详情
     * @param operateLogId
     * @return
     */
    public ResponseDTO<OperateLogVO> detail(Long operateLogId) {
        OperateLogEntity operateLogEntity = operateLogDao.selectById(operateLogId);
        if(operateLogEntity == null){
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        OperateLogVO operateLogVO = SmartBeanUtil.copy(operateLogEntity, OperateLogVO.class);
        return ResponseDTO.ok(operateLogVO);
    }
}
