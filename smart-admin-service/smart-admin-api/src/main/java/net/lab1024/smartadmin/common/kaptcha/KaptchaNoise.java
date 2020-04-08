package net.lab1024.smartadmin.common.kaptcha;

import com.google.code.kaptcha.NoiseProducer;
import com.google.code.kaptcha.util.Configurable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * [ 验证码加噪处理 ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/7/6 0006 上午 10:47
 * @since JDK1.8
 */
public class KaptchaNoise extends Configurable implements NoiseProducer {
    public KaptchaNoise() {
    }

    @Override
    public void makeNoise(BufferedImage image, float factorOne, float factorTwo, float factorThree, float factorFour) {

        int width = image.getWidth();
        int height = image.getHeight();
        Graphics2D graph = (Graphics2D)image.getGraphics();
        graph.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
        graph.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
        Random random = new Random();
        int noiseLineNum = random.nextInt(3);
        if(noiseLineNum == 0){
            noiseLineNum = 1;
        }
        for (int i = 0; i < noiseLineNum; i++){
            graph.setColor(KaptchaColor.getColor());
            graph.drawLine(random.nextInt(width), random.nextInt(height), 10 + random.nextInt(20), 10 + random.nextInt(20));
        }

        graph.dispose();
    }
}
