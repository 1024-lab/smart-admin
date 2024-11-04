package net.lab1024.sa.base.module.support.captcha;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.lab1024.sa.base.common.controller.SupportBaseController;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.constant.SwaggerTagConst;
import net.lab1024.sa.base.module.support.captcha.domain.CaptchaVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 图形验证码业务
 *
 * @Author 1024创新实验室: 胡克
 * @Date 2021-09-02 20:21:10
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Tag(name = SwaggerTagConst.Support.CAPTCHA)
@RestController
public class CaptchaController extends SupportBaseController {

    @Resource
    private CaptchaService captchaService;

    @Operation(summary = "获取图形验证码 @author 胡克")
    @GetMapping("/captcha")
    public ResponseDTO<CaptchaVO> generateCaptcha() {
        return ResponseDTO.ok(captchaService.generateCaptcha());
    }

}
