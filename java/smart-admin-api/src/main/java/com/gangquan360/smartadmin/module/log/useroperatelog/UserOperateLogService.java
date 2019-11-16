package com.gangquan360.smartadmin.module.log.useroperatelog;

import com.baomidou.mybatisplus.plugins.Page;
import com.gangquan360.smartadmin.common.domain.PageResultDTO;
import com.gangquan360.smartadmin.common.domain.ResponseDTO;
import com.gangquan360.smartadmin.module.log.useroperatelog.domain.UserOperateLogDTO;
import com.gangquan360.smartadmin.module.log.useroperatelog.domain.UserOperateLogEntity;
import com.gangquan360.smartadmin.module.log.useroperatelog.domain.UserOperateLogQueryDTO;
import com.gangquan360.smartadmin.util.SmartBeanUtil;
import com.gangquan360.smartadmin.util.SmartPaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2019 1024lab.netInc. All rights reserved.
 * @date 2019-05-15 11:32:14
 * @since JDK1.8
 */
@Service
public class UserOperateLogService {

    @Autowired
    private UserOperateLogDao userOperateLogDao;

    /**
     * @author yandanyang
     * @description 分页查询
     * @date 2019-05-15 11:32:14
     */
    public ResponseDTO<PageResultDTO<UserOperateLogDTO>> queryByPage(UserOperateLogQueryDTO queryDTO) {
        Page page = SmartPaginationUtil.convert2PageQueryInfo(queryDTO);
        List<UserOperateLogEntity> entities = userOperateLogDao.queryByPage(page, queryDTO);
        List<UserOperateLogDTO> dtoList = SmartBeanUtil.copyList(entities, UserOperateLogDTO.class);
        page.setRecords(dtoList);
        PageResultDTO<UserOperateLogDTO> pageResultDTO = SmartPaginationUtil.convert2PageInfoDTO(page);
        return ResponseDTO.succData(pageResultDTO);
    }

    /**
     * @author yandanyang
     * @description 添加
     * @date 2019-05-15 11:32:14
     */
    public ResponseDTO<String> add(UserOperateLogDTO addDTO) {
        UserOperateLogEntity entity = SmartBeanUtil.copy(addDTO, UserOperateLogEntity.class);
        userOperateLogDao.insert(entity);
        return ResponseDTO.succ();
    }

    /**
     * @author yandanyang
     * @description 编辑
     * @date 2019-05-15 11:32:14
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> update(UserOperateLogDTO updateDTO) {
        UserOperateLogEntity entity = SmartBeanUtil.copy(updateDTO, UserOperateLogEntity.class);
        userOperateLogDao.updateById(entity);
        return ResponseDTO.succ();
    }

    /**
     * @author yandanyang
     * @description 删除
     * @date 2019-05-15 11:32:14
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> delete(Long id) {
        userOperateLogDao.deleteById(id);
        return ResponseDTO.succ();
    }

    /**
     * @author yandanyang
     * @description 根据ID查询
     * @date 2019-05-15 11:32:14
     */
    public ResponseDTO<UserOperateLogDTO> detail(Long id) {
        UserOperateLogEntity entity = userOperateLogDao.selectById(id);
        UserOperateLogDTO dto = SmartBeanUtil.copy(entity, UserOperateLogDTO.class);
        return ResponseDTO.succData(dto);
    }
}
