package net.lab1024.smartadmin.module.business.email;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.smartadmin.common.constant.ResponseCodeConst;
import net.lab1024.smartadmin.common.domain.PageResultDTO;
import net.lab1024.smartadmin.common.domain.ResponseDTO;
import net.lab1024.smartadmin.module.business.email.domain.dto.EmailConfigDTO;
import net.lab1024.smartadmin.module.business.email.domain.dto.EmailDTO;
import net.lab1024.smartadmin.module.business.email.domain.dto.EmailQueryDTO;
import net.lab1024.smartadmin.module.business.email.domain.dto.EmailVO;
import net.lab1024.smartadmin.module.business.email.domain.entity.EmailEntity;
import net.lab1024.smartadmin.module.system.systemconfig.SystemConfigService;
import net.lab1024.smartadmin.module.system.systemconfig.constant.SystemConfigEnum;
import net.lab1024.smartadmin.util.SmartBeanUtil;
import net.lab1024.smartadmin.util.SmartPageUtil;
import net.lab1024.smartadmin.util.SmartSendMailUtil;
import org.apache.commons.lang3.StringUtils;
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
 * @date 2019-05-13 17:10:16
 * @since JDK1.8
 */
@Service
public class EmailService {

    @Autowired
    private EmailDao emailDao;

    @Autowired
    private SystemConfigService systemConfigService;

    /**
     * @author yandanyang
     * @description 分页查询
     * @date 2019-05-13 17:10:16
     */
    public ResponseDTO<PageResultDTO<EmailVO>> queryByPage(EmailQueryDTO queryDTO) {
        Page page = SmartPageUtil.convert2QueryPage(queryDTO);
        List<EmailEntity> entities = emailDao.queryByPage(page, queryDTO);
        List<EmailVO> dtoList = SmartBeanUtil.copyList(entities, EmailVO.class);
        page.setRecords(dtoList);
        PageResultDTO<EmailVO> pageResultDTO = SmartPageUtil.convert2PageResult(page);
        return ResponseDTO.succData(pageResultDTO);
    }

    /**
     * @author yandanyang
     * @description 添加
     * @date 2019-05-13 17:10:16
     */
    public ResponseDTO<Long> add(EmailDTO addDTO) {
        EmailEntity entity = SmartBeanUtil.copy(addDTO, EmailEntity.class);
        emailDao.insert(entity);
        return ResponseDTO.succData(entity.getId());
    }

    /**
     * @author yandanyang
     * @description 编辑
     * @date 2019-05-13 17:10:16
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<Long> update(EmailDTO updateDTO) {
        EmailEntity entity = SmartBeanUtil.copy(updateDTO, EmailEntity.class);
        emailDao.updateById(entity);
        return ResponseDTO.succData(entity.getId());
    }

    /**
     * @author yandanyang
     * @description 删除
     * @date 2019-05-13 17:10:16
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> delete(Long id) {
        emailDao.deleteById(id);
        return ResponseDTO.succ();
    }

    /**
     * @author yandanyang
     * @description 根据ID查询
     * @date 2019-05-13 17:10:16
     */
    public ResponseDTO<EmailVO> detail(Long id) {
        EmailEntity entity = emailDao.selectById(id);
        EmailVO dto = SmartBeanUtil.copy(entity, EmailVO.class);
        return ResponseDTO.succData(dto);
    }

    /**
     * 发送某个已创建的邮件
     *
     * @param id
     * @return
     */
    public ResponseDTO<String> send(Long id) {
        EmailEntity entity = emailDao.selectById(id);
        EmailConfigDTO emailConfig = systemConfigService.selectByKey2Obj(SystemConfigEnum.Key.EMAIL_CONFIG.name(), EmailConfigDTO.class);
        String toEmails = entity.getToEmails();
        if (StringUtils.isEmpty(toEmails)) {
            return ResponseDTO.wrap(ResponseCodeConst.ERROR_PARAM, "收件人信息为空");
        }
        String[] emails = toEmails.split(";");
        SmartSendMailUtil.sendMail(emailConfig.getUsername(), emailConfig.getPassword(), emailConfig.getUsername(), emails, "", emailConfig.getSmtpHost(), entity.getTitle(), entity.getContent());
        entity.setSendStatus(EmailSendStatusEnum.SEND.getType());
        emailDao.updateById(entity);
        return ResponseDTO.succ();
    }

}
