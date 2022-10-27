package net.lab1024.sa.admin.module.business.oa.notice.manager;

import net.lab1024.sa.admin.module.business.oa.notice.dao.NoticeDao;
import net.lab1024.sa.admin.module.business.oa.notice.domain.entity.NoticeEntity;
import net.lab1024.sa.admin.module.business.oa.notice.domain.form.NoticeVisibleRangeForm;
import net.lab1024.sa.common.module.support.datatracer.constant.DataTracerTypeEnum;
import net.lab1024.sa.common.module.support.datatracer.service.DataTracerService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 通知、公告 manager
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-08-12 21:40:39
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ），2012-2022
 */
@Service
public class NoticeManager {

    @Autowired
    private NoticeDao noticeDao;

    @Autowired
    private DataTracerService dataTracerService;

    /**
     * 保存
     *
     * @param noticeEntity
     * @param visibleRangeFormList
     */
    @Transactional(rollbackFor = Throwable.class)
    public void save(NoticeEntity noticeEntity, List<NoticeVisibleRangeForm> visibleRangeFormList) {
        noticeDao.insert(noticeEntity);
        Long noticeId = noticeEntity.getNoticeId();
        // 保存可见范围
        if (CollectionUtils.isNotEmpty(visibleRangeFormList)) {
            noticeDao.insertVisibleRange(noticeId, visibleRangeFormList);
        }
        dataTracerService.insert(noticeId, DataTracerTypeEnum.OA_NOTICE);
    }

    /**
     * 更新
     *
     * @param noticeEntity
     * @param visibleRangeList
     */
    @Transactional(rollbackFor = Throwable.class)
    public void update(NoticeEntity old, NoticeEntity noticeEntity, List<NoticeVisibleRangeForm> visibleRangeList) {
        noticeDao.updateById(noticeEntity);
        Long noticeId = noticeEntity.getNoticeId();
        // 保存可见范围
        if (CollectionUtils.isNotEmpty(visibleRangeList)) {
            noticeDao.deleteVisibleRange(noticeId);
            noticeDao.insertVisibleRange(noticeId, visibleRangeList);
        }
        dataTracerService.update(noticeId, DataTracerTypeEnum.OA_NOTICE, old, noticeEntity);
    }
}
