package net.lab1024.sa.admin.module.business.oa.notice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.oa.notice.domain.entity.NoticeEntity;
import net.lab1024.sa.admin.module.business.oa.notice.domain.form.NoticeEmployeeQueryForm;
import net.lab1024.sa.admin.module.business.oa.notice.domain.form.NoticeQueryForm;
import net.lab1024.sa.admin.module.business.oa.notice.domain.form.NoticeViewRecordQueryForm;
import net.lab1024.sa.admin.module.business.oa.notice.domain.form.NoticeVisibleRangeForm;
import net.lab1024.sa.admin.module.business.oa.notice.domain.vo.NoticeEmployeeVO;
import net.lab1024.sa.admin.module.business.oa.notice.domain.vo.NoticeVO;
import net.lab1024.sa.admin.module.business.oa.notice.domain.vo.NoticeViewRecordVO;
import net.lab1024.sa.admin.module.business.oa.notice.domain.vo.NoticeVisibleRangeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 公告、通知、新闻等等
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-08-12 21:40:39
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Mapper
@Component
public interface NoticeDao extends BaseMapper<NoticeEntity> {

    // ================================= 数据范围相关 【子表】 =================================

    /**
     * 保存可见范围
     *
     */
    void insertVisibleRange(@Param("noticeId") Long noticeId, @Param("visibleRangeFormList") List<NoticeVisibleRangeForm> visibleRangeFormList);

    /**
     * 删除可见范围
     *
     */
    void deleteVisibleRange(@Param("noticeId") Long noticeId);

    /**
     * 相关可见范围
     *
     */
    List<NoticeVisibleRangeVO> queryVisibleRange(@Param("noticeId") Long noticeId);

    // ================================= 通知公告【主表】 相关  =================================

    /**
     * 后管分页查询资讯
     *
     */
    List<NoticeVO> query(Page<?> page, @Param("query") NoticeQueryForm queryForm);


    /**
     * 更新删除状态
     *
     */
    void updateDeletedFlag(@Param("noticeId") Long noticeId);

    // ================================= 通知公告【员工查看】 相关  =================================

    /**
     * 查询 员工 查看到的通知公告
     *
     */
    List<NoticeEmployeeVO> queryEmployeeNotice(Page<?> page,
                                               @Param("requestEmployeeId") Long requestEmployeeId,
                                               @Param("query") NoticeEmployeeQueryForm noticeEmployeeQueryForm,
                                               @Param("requestEmployeeDepartmentIdList") List<Long> requestEmployeeDepartmentIdList,
                                               @Param("deletedFlag") boolean deletedFlag,
                                               @Param("administratorFlag") boolean administratorFlag,
                                               @Param("departmentDataType") Integer departmentDataType,
                                               @Param("employeeDataType") Integer employeeDataType

    );

    /**
     * 查询 员工 未读的通知公告
     *
     */
    List<NoticeEmployeeVO> queryEmployeeNotViewNotice(Page<?> page,
                                               @Param("requestEmployeeId") Long requestEmployeeId,
                                               @Param("query") NoticeEmployeeQueryForm noticeEmployeeQueryForm,
                                               @Param("requestEmployeeDepartmentIdList") List<Long> requestEmployeeDepartmentIdList,
                                               @Param("deletedFlag") boolean deletedFlag,
                                               @Param("administratorFlag") boolean administratorFlag,
                                               @Param("departmentDataType") Integer departmentDataType,
                                               @Param("employeeDataType") Integer employeeDataType

    );

    long  viewRecordCount(@Param("noticeId")Long noticeId, @Param("employeeId")Long employeeId);

    /**
     * 查询通知、公告的 查看记录
     */
    List<NoticeViewRecordVO> queryNoticeViewRecordList(Page page,@Param("queryForm") NoticeViewRecordQueryForm noticeViewRecordQueryForm);

    /**
     * 保存查看记录
     */
    void insertViewRecord(@Param("noticeId") Long noticeId, @Param("employeeId") Long employeeId, @Param("ip") String ip, @Param("userAgent") String userAgent,@Param("pageViewCount") Integer pageViewCount);

    /**
     * 更新查看记录
     */
    void updateViewRecord(@Param("noticeId")Long noticeId, @Param("employeeId")Long requestEmployeeId,@Param("ip") String ip, @Param("userAgent")String userAgent);

    /**
     * 更新 浏览量
     *
     * @param noticeId 通知 id
     * @param pageViewCountIncrement 页面浏览量的增量
     * @param userViewCountIncrement 用户浏览量的增量
     */
    void updateViewCount(@Param("noticeId")Long noticeId,@Param("pageViewCountIncrement") Integer pageViewCountIncrement, @Param("userViewCountIncrement")Integer userViewCountIncrement);


}
