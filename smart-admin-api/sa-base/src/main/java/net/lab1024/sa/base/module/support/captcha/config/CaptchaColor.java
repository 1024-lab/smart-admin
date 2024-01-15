package net.lab1024.sa.base.module.support.captcha.config;

import com.google.common.collect.Lists;

import java.awt.*;
import java.util.List;
import java.util.Random;

/**
 * 验证码颜色
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2021-09-02 20:21:10
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
public class CaptchaColor {

    public static Color getColor() {

        List<Color> colors = Lists.newArrayList();
        colors.add(new Color(0, 135, 255));
        colors.add(new Color(51, 153, 51));
        colors.add(new Color(255, 102, 102));
        colors.add(new Color(255, 153, 0));
        colors.add(new Color(153, 102, 0));
        colors.add(new Color(153, 102, 153));
        colors.add(new Color(51, 153, 153));
        colors.add(new Color(102, 102, 255));
        colors.add(new Color(0, 102, 204));
        colors.add(new Color(204, 51, 51));
        colors.add(new Color(128, 153, 65));
        Random random = new Random();
        int colorIndex = random.nextInt(10);
        return colors.get(colorIndex);
    }
}
