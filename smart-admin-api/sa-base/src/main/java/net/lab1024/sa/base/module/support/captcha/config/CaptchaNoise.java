package net.lab1024.sa.base.module.support.captcha.config;

import com.google.code.kaptcha.NoiseProducer;
import com.google.code.kaptcha.util.Configurable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 验证码加噪处理
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2021-09-02 20:21:10
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
public class CaptchaNoise extends Configurable implements NoiseProducer {

    public CaptchaNoise() {
    }

    @Override
    public void makeNoise(BufferedImage image, float factorOne, float factorTwo, float factorThree, float factorFour) {

        int width = image.getWidth();
        int height = image.getHeight();
        Graphics2D graph = (Graphics2D) image.getGraphics();
        graph.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
        graph.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
        Random random = new Random();
        int noiseLineNum = random.nextInt(3);
        if (noiseLineNum == 0) {
            noiseLineNum = 1;
        }
        for (int i = 0; i < noiseLineNum; i++) {
            graph.setColor(CaptchaColor.getColor());
            graph.drawLine(random.nextInt(width), random.nextInt(height), 10 + random.nextInt(20), 10 + random.nextInt(20));
        }

        graph.dispose();
    }
}
