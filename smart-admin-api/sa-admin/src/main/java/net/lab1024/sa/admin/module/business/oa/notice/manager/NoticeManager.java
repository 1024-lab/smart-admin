package net.lab1024.sa.admin.module.business.oa.notice.manager;

import net.lab1024.sa.admin.module.business.oa.notice.dao.NoticeDao;
import net.lab1024.sa.admin.module.business.oa.notice.domain.entity.NoticeEntity;
import net.lab1024.sa.admin.module.business.oa.notice.domain.form.NoticeVisibleRangeForm;
import net.lab1024.sa.base.module.support.datatracer.constant.DataTracerTypeEnum;
import net.lab1024.sa.base.module.support.datatracer.service.DataTracerService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 通知、公告 manager
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-08-12 21:40:39
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Service
public class NoticeManager {

    @Resource
    private NoticeDao noticeDao;

    @Resource
    private DataTracerService dataTracerService;

    /**
     * 保存
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
