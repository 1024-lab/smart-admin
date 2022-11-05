package net.lab1024.sa.common.module.support.captcha;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.common.common.code.UserErrorCode;
import net.lab1024.sa.common.common.constant.StringConst;
import net.lab1024.sa.common.common.domain.ResponseDTO;
import net.lab1024.sa.common.common.domain.SystemEnvironment;
import net.lab1024.sa.common.common.exception.BusinessException;
import net.lab1024.sa.common.constant.RedisKeyConst;
import net.lab1024.sa.common.module.support.captcha.domain.CaptchaForm;
import net.lab1024.sa.common.module.support.captcha.domain.CaptchaVO;
import net.lab1024.sa.common.module.support.redis.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Objects;
import java.util.UUID;

/**
 * 图形验证码 服务
 *
 * @Author 1024创新实验室: 胡克
 * @Date 2021/8/31 20:52
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
@Slf4j
@Service
public class CaptchaService {

    /**
     * 过期时间：65秒
     */
    private static final long EXPIRE_SECOND = 65L;

    @Autowired
    private DefaultKaptcha defaultKaptcha;
    @Autowired
    private SystemEnvironment systemEnvironment;
    @Autowired
    private RedisService redisService;

    /**
     * 生成图形验证码
     * 默认 1 分钟有效期
     *
     * @return
     */
    public CaptchaVO generateCaptcha() {
        String captchaText = defaultKaptcha.createText();
        BufferedImage image = defaultKaptcha.createImage(captchaText);

        String base64Code;
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            ImageIO.write(image, "jpg", os);
            base64Code = Base64Utils.encodeToString(os.toByteArray());
        } catch (Exception e) {
            log.error("generateCaptcha error:", e);
            throw new BusinessException("生成验证码错误");
        }

        /**
         * 返回验证码对象
         * 图片 base64格式
         */
        // uuid 唯一标识
        String uuid = UUID.randomUUID().toString().replace("-", StringConst.EMPTY);

        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCaptchaUuid(uuid);
        captchaVO.setCaptchaBase64Image("data:image/png;base64," + base64Code);
        captchaVO.setExpireSeconds(EXPIRE_SECOND);
        if (!systemEnvironment.isProd()) {
            captchaVO.setCaptchaText(captchaText);
        }
        String redisCaptchaKey = redisService.generateRedisKey(RedisKeyConst.Support.CAPTCHA, uuid);
        redisService.set(redisCaptchaKey, captchaText, EXPIRE_SECOND);
        return captchaVO;
    }

    /**
     * 校验图形验证码
     *
     * @param captchaForm
     * @return
     */
    public ResponseDTO<String> checkCaptcha(CaptchaForm captchaForm) {
        if (StringUtils.isBlank(captchaForm.getCaptchaUuid()) || StringUtils.isBlank(captchaForm.getCaptchaCode())) {
            return ResponseDTO.userErrorParam("请输入正确验证码");
        }
        /**
         * 1、校验redis里的验证码
         * 2、校验成功后，删除redis
         */
        String redisCaptchaKey = redisService.generateRedisKey(RedisKeyConst.Support.CAPTCHA, captchaForm.getCaptchaUuid());
        String redisCaptchaCode = redisService.get(redisCaptchaKey);
        if (StringUtils.isBlank(redisCaptchaCode)) {
            return ResponseDTO.userErrorParam("验证码已过期，请刷新重试");
        }
        if (!Objects.equals(redisCaptchaCode, captchaForm.getCaptchaCode())) {
            return ResponseDTO.userErrorParam("验证码错误，请输入正确的验证码");
        }
        // 删除已使用的验证码
        redisService.delete(redisCaptchaKey);
        return ResponseDTO.ok();
    }

}
