package net.lab1024.sa.common.module.support.feedback.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.common.common.domain.PageResult;
import net.lab1024.sa.common.common.domain.RequestUser;
import net.lab1024.sa.common.common.domain.ResponseDTO;
import net.lab1024.sa.common.common.util.SmartBeanUtil;
import net.lab1024.sa.common.common.util.SmartPageUtil;
import net.lab1024.sa.common.module.support.feedback.dao.FeedbackDao;
import net.lab1024.sa.common.module.support.feedback.domain.FeedbackAddForm;
import net.lab1024.sa.common.module.support.feedback.domain.FeedbackEntity;
import net.lab1024.sa.common.module.support.feedback.domain.FeedbackQueryForm;
import net.lab1024.sa.common.module.support.feedback.domain.FeedbackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 意见反馈
 *
 * @Author 1024创新实验室: 开云
 * @Date 2022-08-11 20:48:09
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Service
public class FeedbackService {

    @Autowired
    private FeedbackDao feedbackDao;

    /**
     * 分页查询
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<FeedbackVO>> query(FeedbackQueryForm queryForm) {
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<FeedbackVO> list = feedbackDao.queryPage(page, queryForm);
        PageResult<FeedbackVO> pageResultDTO = SmartPageUtil.convert2PageResult(page, list);
        if (pageResultDTO.getEmptyFlag()) {
            return ResponseDTO.ok(pageResultDTO);
        }
        return ResponseDTO.ok(pageResultDTO);
    }

    /**
     * 新建
     */
    public ResponseDTO<String> add(FeedbackAddForm addForm, RequestUser requestUser) {
        FeedbackEntity feedback = SmartBeanUtil.copy(addForm, FeedbackEntity.class);
        feedback.setUserType(requestUser.getUserType().getValue());
        feedback.setUserId(requestUser.getUserId());
        feedback.setUserName(requestUser.getUserName());
        feedbackDao.insert(feedback);
        return ResponseDTO.ok();
    }
}
