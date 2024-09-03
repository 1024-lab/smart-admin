package net.lab1024.sa.base.module.support.mail;


import cn.hutool.core.lang.UUID;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.SystemEnvironment;
import net.lab1024.sa.base.module.support.mail.constant.MailTemplateCodeEnum;
import net.lab1024.sa.base.module.support.mail.constant.MailTemplateTypeEnum;
import net.lab1024.sa.base.module.support.mail.domain.MailTemplateEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;

/**
 *
 * 发生邮件：<br/>
 * 1、支持直接发送 <br/>
 * 2、支持使用邮件模板发送
 *
 * @Author 1024创新实验室-创始人兼主任:卓大
 * @Date 2024/8/5
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a> ，Since 2012
 */
@Slf4j
@Component
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Resource
    private MailTemplateDao mailTemplateDao;

    @Resource
    private SystemEnvironment systemEnvironment;

    @Value("${spring.mail.username}")
    private String clientMail;


    /**
     * 使用模板发送邮件
     */
    public ResponseDTO<String> sendMail(MailTemplateCodeEnum templateCode, Map<String, Object> templateParamsMap, List<String> receiverUserList, List<File> fileList) {

        MailTemplateEntity mailTemplateEntity = mailTemplateDao.selectById(templateCode.name().toLowerCase());
        if (mailTemplateEntity == null) {
            return ResponseDTO.userErrorParam("模版不存在");
        }

        if (mailTemplateEntity.getDisableFlag()) {
            return ResponseDTO.userErrorParam("模版已禁用，无法发送");
        }

        String content = null;
        if (MailTemplateTypeEnum.FREEMARKER.name().equalsIgnoreCase(mailTemplateEntity.getTemplateType().trim())) {
            content = freemarkerResolverContent(mailTemplateEntity.getTemplateContent(), templateParamsMap);
        } else if (MailTemplateTypeEnum.STRING.name().equalsIgnoreCase(mailTemplateEntity.getTemplateType().trim())) {
            content = stringResolverContent(mailTemplateEntity.getTemplateContent(), templateParamsMap);
        } else {
            return ResponseDTO.userErrorParam("模版类型不存在");
        }

        try {

            this.sendMail(mailTemplateEntity.getTemplateSubject(), content, fileList, receiverUserList, true);

        } catch (Throwable e) {
            log.error("邮件发送失败", e);
            return ResponseDTO.userErrorParam("邮件发送失败");
        }
        return ResponseDTO.ok();
    }

    /**
     * 使用模板发送邮件
     */
    public ResponseDTO<String> sendMail(MailTemplateCodeEnum templateCode, Map<String, Object> templateParamsMap, List<String> receiverUserList) {
        return this.sendMail(templateCode, templateParamsMap, receiverUserList, null);
    }


    /**
     * 发送邮件
     *
     * @param subject          主题
     * @param content          内容
     * @param fileList         文件
     * @param receiverUserList 接收方
     * @throws MessagingException
     */
    public void sendMail(String subject, String content, List<File> fileList, List<String> receiverUserList, boolean isHtml) throws MessagingException {

        if (CollectionUtils.isEmpty(receiverUserList)) {
            throw new RuntimeException("接收方不能为空");
        }

        if (StringUtils.isBlank(content)) {
            throw new RuntimeException("邮件内容不能为空");
        }

        if (!systemEnvironment.isProd()) {
            subject = "(测试)" + subject;
        }

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        //是否为多文件上传
        boolean multiparty = !CollectionUtils.isEmpty(fileList);
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, multiparty);
        helper.setFrom(clientMail);
        helper.setTo(receiverUserList.toArray(new String[0]));
        helper.setSubject(subject);
        //发送html格式
        helper.setText(content, isHtml);

        //附件
        if (multiparty) {
            for (File file : fileList) {
                helper.addAttachment(file.getName(), file);
            }
        }
        javaMailSender.send(mimeMessage);
    }

    /**
     * 使用字符串生成最终内容
     */
    private String stringResolverContent(String stringTemplate, Map<String, Object> templateParamsMap) {
        StringSubstitutor stringSubstitutor = new StringSubstitutor(templateParamsMap);
        String contractHtml = stringSubstitutor.replace(stringTemplate);
        Document doc = Jsoup.parse(contractHtml);
        doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        return doc.outerHtml();
    }


    /**
     * 使用 freemarker 生成最终内容
     */
    private String freemarkerResolverContent(String htmlTemplate, Map<String, Object> templateParamsMap) {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
        StringTemplateLoader stringLoader = new StringTemplateLoader();
        String templateName = UUID.fastUUID().toString(true);
        stringLoader.putTemplate(templateName, htmlTemplate);
        configuration.setTemplateLoader(stringLoader);
        try {
            Template template = configuration.getTemplate(templateName, "utf-8");
            Writer out = new StringWriter(2048);
            template.process(templateParamsMap, out);
            return out.toString();
        } catch (Throwable e) {
            log.error("freemarkerResolverContent error: ", e);
        }
        return "";
    }
}
