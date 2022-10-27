package net.lab1024.smartadmin.module.business.notice;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.smartadmin.common.constant.JudgeEnum;
import net.lab1024.smartadmin.common.constant.ResponseCodeConst;
import net.lab1024.smartadmin.common.domain.PageParamDTO;
import net.lab1024.smartadmin.common.domain.PageResultDTO;
import net.lab1024.smartadmin.common.domain.ResponseDTO;
import net.lab1024.smartadmin.module.system.login.domain.RequestTokenBO;
import net.lab1024.smartadmin.module.business.notice.dao.NoticeDao;
import net.lab1024.smartadmin.module.business.notice.dao.NoticeReceiveRecordDao;
import net.lab1024.smartadmin.module.business.notice.domain.dto.*;
import net.lab1024.smartadmin.module.business.notice.domain.entity.NoticeEntity;
import net.lab1024.smartadmin.module.business.notice.domain.entity.NoticeReceiveRecordEntity;
import net.lab1024.smartadmin.module.support.websocket.WebSocketServer;
import net.lab1024.smartadmin.util.SmartBeanUtil;
import net.lab1024.smartadmin.util.SmartPageUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2019 1024lab.netInc. All rights reserved.
 * @date 2019-07-11 16:19:48
 * @since JDK1.8
 */
@Service
public class NoticeService {

    @Autowired
    private NoticeDao noticeDao;

    @Autowired
    private NoticeReceiveRecordDao noticeReceiveRecordDao;

    @Autowired
    private NoticeManage noticeManage;

    /**
     * @author yandanyang
     * @description 分页查询
     * @date 2019-07-11 16:19:48
     */
    public ResponseDTO<PageResultDTO<NoticeVO>> queryByPage(NoticeQueryDTO queryDTO) {
        queryDTO.setDeleted(JudgeEnum.NO.getValue());
        Page page = SmartPageUtil.convert2QueryPage(queryDTO);
        List<NoticeVO> dtoList = noticeDao.queryByPage(page, queryDTO);
        page.setRecords(dtoList);
        PageResultDTO<NoticeVO> pageResultDTO = SmartPageUtil.convert2PageResult(page);
        return ResponseDTO.succData(pageResultDTO);
    }

    /**
     * 获取当前登录人的消息列表
     *
     * @param queryDTO
     * @param requestToken
     * @return
     */
    public ResponseDTO<PageResultDTO<NoticeReceiveDTO>> queryReceiveByPage(NoticeReceiveQueryDTO queryDTO, RequestTokenBO requestToken) {
        queryDTO.setEmployeeId(requestToken.getRequestUserId());
        queryDTO.setSendStatus(JudgeEnum.YES.getValue());
        Page page = SmartPageUtil.convert2QueryPage(queryDTO);
        List<NoticeReceiveDTO> dtoList = noticeDao.queryReceiveByPage(page, queryDTO);
        dtoList.forEach(e -> {
            if (e.getReceiveTime() == null) {
                e.setReadStatus(JudgeEnum.NO.getValue());
            } else {
                e.setReadStatus(JudgeEnum.YES.getValue());
            }
        });
        page.setRecords(dtoList);
        PageResultDTO<NoticeReceiveDTO> pageResultDTO = SmartPageUtil.convert2PageResult(page);
        return ResponseDTO.succData(pageResultDTO);
    }

    /**
     * 获取我的未读消息
     *
     * @param queryDTO
     * @param requestToken
     * @return
     */
    public ResponseDTO<PageResultDTO<NoticeVO>> queryUnreadByPage(PageParamDTO queryDTO, RequestTokenBO requestToken) {
        Page page = SmartPageUtil.convert2QueryPage(queryDTO);
        List<NoticeVO> dtoList = noticeDao.queryUnreadByPage(page, requestToken.getRequestUserId(), JudgeEnum.YES.getValue());
        page.setRecords(dtoList);
        PageResultDTO<NoticeVO> pageResultDTO = SmartPageUtil.convert2PageResult(page);
        return ResponseDTO.succData(pageResultDTO);
    }

    /**
     * @author yandanyang
     * @description 添加
     * @date 2019-07-11 16:19:48
     */
    public ResponseDTO<String> add(NoticeAddDTO addDTO, RequestTokenBO requestToken) {
        NoticeEntity entity = SmartBeanUtil.copy(addDTO, NoticeEntity.class);
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        entity.setCreateUser(requestToken.getRequestUserId());
        entity.setSendStatus(JudgeEnum.NO.getValue());
        entity.setDeleted(JudgeEnum.NO.getValue());
        noticeDao.insert(entity);
        return ResponseDTO.succ();
    }

    /**
     * @author yandanyang
     * @description 编辑
     * @date 2019-07-11 16:19:48
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> update(NoticeUpdateDTO updateDTO) {
        NoticeEntity entity = noticeDao.selectById(updateDTO.getId());
        if (entity == null) {
            return ResponseDTO.wrap(ResponseCodeConst.ERROR_PARAM, "此系统通知不存在");
        }
        if (JudgeEnum.YES.getValue().equals(entity.getSendStatus())) {
            return ResponseDTO.wrap(ResponseCodeConst.ERROR_PARAM, "此系统通知已发送无法修改");
        }
        noticeManage.update(entity, updateDTO);
        return ResponseDTO.succ();
    }

    /**
     * @author yandanyang
     * @description 删除
     * @date 2019-07-11 16:19:48
     */
    public ResponseDTO<String> delete(Long id) {
        NoticeEntity entity = noticeDao.selectById(id);
        if (entity == null) {
            return ResponseDTO.wrap(ResponseCodeConst.ERROR_PARAM, "此系统通知不存在");
        }
        noticeManage.delete(entity);
        return ResponseDTO.succ();
    }

    /**
     * @author yandanyang
     * @description 根据ID查询
     * @date 2019-07-11 16:19:48
     */
    public ResponseDTO<NoticeDetailVO> detail(Long id) {
        NoticeDetailVO noticeDTO = noticeDao.detail(id);
        return ResponseDTO.succData(noticeDTO);
    }

    /**
     * 获取某人的未读消息数
     *
     * @param employeeId
     * @return
     */
    private Integer getUnreadCount(Long employeeId) {
        return noticeDao.noticeUnreadCount(employeeId, JudgeEnum.YES.getValue());
    }

    /**
     * 发送给所有在线用户未读消息数
     *
     * @param id
     * @param requestToken
     * @return
     */
    public ResponseDTO<NoticeDetailVO> send(Long id, RequestTokenBO requestToken) {
        NoticeEntity entity = noticeDao.selectById(id);
        if (entity == null) {
            return ResponseDTO.wrap(ResponseCodeConst.ERROR_PARAM, "此系统通知不存在");
        }
        noticeManage.send(entity, requestToken);
        this.sendMessage(requestToken);
        return ResponseDTO.succ();
    }

    /**
     * 发送系统通知 ，发送人不进行接收,需再事务外调用 以防止数据隔离级别不同造成未读消息数异常
     *
     * @param requestToken
     */
    private void sendMessage(RequestTokenBO requestToken) {
        List<Long> onLineEmployeeIds = WebSocketServer.getOnLineUserList();
        if (CollectionUtils.isEmpty(onLineEmployeeIds)) {
            return;
        }
        //在线用户已读消息数
        Map<Long, Integer> readCountMap = new HashMap<>();
        List<NoticeReadCountDTO> readCountList = noticeDao.readCount(onLineEmployeeIds);
        if (CollectionUtils.isNotEmpty(readCountList)) {
            readCountMap = readCountList.stream().collect(Collectors.toMap(NoticeReadCountDTO :: getEmployeeId, NoticeReadCountDTO :: getReadCount));
        }
        //已发送消息数
        Integer noticeCount = noticeDao.noticeCount(JudgeEnum.YES.getValue());
        for (Long employeeId : onLineEmployeeIds) {
            Integer readCount = readCountMap.get(employeeId) == null ? 0 : readCountMap.get(employeeId);
            Integer unReadCount = noticeCount - readCount;
            if (! requestToken.getRequestUserId().equals(employeeId)) {
                WebSocketServer.sendOneOnLineUser(unReadCount.toString(), employeeId);
            }
        }
    }

    /**
     * 读取消息
     *
     * @param id
     * @param requestToken
     * @return
     */
    public ResponseDTO<NoticeDetailVO> read(Long id, RequestTokenBO requestToken) {
        NoticeDetailVO noticeDTO = noticeDao.detail(id);

        NoticeReceiveRecordEntity recordEntity = noticeReceiveRecordDao.selectByEmployeeAndNotice(requestToken.getRequestUserId(), id);
        if (recordEntity != null) {
            return ResponseDTO.succData(noticeDTO);
        }
        noticeManage.saveReadRecord(id, requestToken);
        this.sendMessage(requestToken);
        return ResponseDTO.succData(noticeDTO);
    }
}
