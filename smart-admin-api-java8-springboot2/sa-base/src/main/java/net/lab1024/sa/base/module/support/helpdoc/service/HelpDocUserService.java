package net.lab1024.sa.base.module.support.helpdoc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.RequestUser;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.module.support.helpdoc.dao.HelpDocDao;
import net.lab1024.sa.base.module.support.helpdoc.domain.entity.HelpDocEntity;
import net.lab1024.sa.base.module.support.helpdoc.domain.form.HelpDocViewRecordQueryForm;
import net.lab1024.sa.base.module.support.helpdoc.domain.vo.HelpDocDetailVO;
import net.lab1024.sa.base.module.support.helpdoc.domain.vo.HelpDocVO;
import net.lab1024.sa.base.module.support.helpdoc.domain.vo.HelpDocViewRecordVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户查看  帮助文档
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-08-20 23:11:42
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Service
public class HelpDocUserService {

    @Resource
    private HelpDocDao helpDocDao;


    /**
     * 查询全部 帮助文档
     *
     * @return
     */
    public ResponseDTO<List<HelpDocVO>> queryAllHelpDocList() {
        return ResponseDTO.ok(helpDocDao.queryAllHelpDocList());
    }


    /**
     * 查询我的 待查看的 帮助文档清单
     *
     * @return
     */
    public ResponseDTO<HelpDocDetailVO> view(RequestUser requestUser, Long helpDocId) {
        HelpDocEntity helpDocEntity = helpDocDao.selectById(helpDocId);
        if (helpDocEntity == null) {
            return ResponseDTO.userErrorParam("帮助文档不存在");
        }

        HelpDocDetailVO helpDocDetailVO = SmartBeanUtil.copy(helpDocEntity, HelpDocDetailVO.class);
        long viewCount = helpDocDao.viewRecordCount(helpDocId, requestUser.getUserId());
        if (viewCount == 0) {
            helpDocDao.insertViewRecord(helpDocId, requestUser.getUserId(), requestUser.getUserName(), requestUser.getIp(), requestUser.getUserAgent(), 1);
            helpDocDao.updateViewCount(helpDocId, 1, 1);
            helpDocDetailVO.setPageViewCount(helpDocDetailVO.getPageViewCount() + 1);
            helpDocDetailVO.setUserViewCount(helpDocDetailVO.getUserViewCount() + 1);
        } else {
            helpDocDao.updateViewRecord(helpDocId, requestUser.getUserId(), requestUser.getIp(), requestUser.getUserAgent());
            helpDocDao.updateViewCount(helpDocId, 0, 1);
            helpDocDetailVO.setPageViewCount(helpDocDetailVO.getPageViewCount() + 1);
        }

        return ResponseDTO.ok(helpDocDetailVO);
    }


    /**
     * 分页查询  查看记录
     *
     * @param helpDocViewRecordQueryForm
     * @return
     */
    public PageResult<HelpDocViewRecordVO> queryViewRecord(HelpDocViewRecordQueryForm helpDocViewRecordQueryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(helpDocViewRecordQueryForm);
        List<HelpDocViewRecordVO> noticeViewRecordVOS = helpDocDao.queryViewRecordList(page, helpDocViewRecordQueryForm);
        return SmartPageUtil.convert2PageResult(page, noticeViewRecordVOS);
    }
}
