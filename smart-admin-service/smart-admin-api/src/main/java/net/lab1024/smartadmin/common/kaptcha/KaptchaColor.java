package net.lab1024.smartadmin.common.kaptcha;

import com.google.common.collect.Lists;

import java.awt.*;
import java.util.List;
import java.util.Random;

/**
 * [ 验证码颜色 ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/7/6 0006 上午 10:51
 * @since JDK1.8
 */
public class KaptchaColor {

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
