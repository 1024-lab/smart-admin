package net.lab1024.smartadmin.common.kaptcha;

import com.google.code.kaptcha.util.Configurable;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * [ 验证码字体生成 ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/7/6 0006 上午 9:43
 * @since JDK1.8
 */
public class KaptchaWordRenderer extends Configurable implements com.google.code.kaptcha.text.WordRenderer {

    public KaptchaWordRenderer() {
    }

    @Override
    public BufferedImage renderWord(String word, int width, int height) {
        int fontSize = this.getConfig().getTextProducerFontSize();
        Font[] fonts = this.getConfig().getTextProducerFonts(fontSize);
        int charSpace = this.getConfig().getTextProducerCharSpace();
        BufferedImage image = new BufferedImage(width, height, 2);

        Graphics2D g2D = image.createGraphics();
        g2D.setColor(Color.WHITE);
        RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        hints.add(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        g2D.setRenderingHints(hints);
        g2D.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));

        FontRenderContext frc = g2D.getFontRenderContext();
        Random random = new Random();
        int startPosY = (height - fontSize) / 5 + fontSize;
        char[] wordChars = word.toCharArray();
        Font[] chosenFonts = new Font[wordChars.length];
        int[] charWidths = new int[wordChars.length];
        int widthNeeded = 0;

        int startPosX;
        for(startPosX = 0; startPosX < wordChars.length; ++startPosX) {
            chosenFonts[startPosX] = fonts[random.nextInt(fonts.length)];
            char[] charToDraw = new char[]{wordChars[startPosX]};
            GlyphVector gv = chosenFonts[startPosX].createGlyphVector(frc, charToDraw);
            charWidths[startPosX] = (int)gv.getVisualBounds().getWidth();
            if (startPosX > 0) {
                widthNeeded += 2;
            }

            widthNeeded += charWidths[startPosX];
        }

        startPosX = (width - widthNeeded) / 2;

        for(int i = 0; i < wordChars.length; ++i) {
            g2D.setColor(KaptchaColor.getColor());
            g2D.setFont(chosenFonts[i].deriveFont(Font.PLAIN));
            char[] charToDraw = new char[]{wordChars[i]};
            g2D.drawChars(charToDraw, 0, charToDraw.length, startPosX, startPosY);
            startPosX = startPosX + charWidths[i] + charSpace;
        }

        return image;
    }


}
