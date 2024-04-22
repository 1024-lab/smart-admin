package net.lab1024.sa.base.common.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.opc.PackagePartName;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.poi.openxml4j.opc.TargetMode;
import org.apache.poi.xssf.usermodel.XSSFPictureData;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;

/**
 *
 * excel 工具类
 *
 * @Author 1024创新实验室-主任:卓大
 * @Date 2024/4/22 22:49:07
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ），2012-2024
 */
public final class SmartExcelUtil {

    /**
     * 通用单sheet导出
     */
    public static void exportExcel(HttpServletResponse response, String fileName, String sheetName, Class head,Collection<?> data) throws IOException {
        // 设置下载消息头
        SmartResponseUtil.setDownloadFileHeader(response, fileName, null);
        // 下载
        EasyExcel.write(response.getOutputStream(), head)
                .autoCloseStream(Boolean.FALSE)
                .sheet(sheetName)
                .doWrite(data);
    }

    /**
     * 通用单 sheet水印 导出
     */
    public static void exportExcelWithWatermark(HttpServletResponse response, String fileName, String sheetName, Class head,Collection<?> data, String watermarkString) throws IOException {
        // 设置下载消息头
        SmartResponseUtil.setDownloadFileHeader(response, fileName, null);
        // 水印
        Watermark watermark = new Watermark(watermarkString);
        // 一定要inMemory
        EasyExcel.write(response.getOutputStream(), head)
                .inMemory(true)
                .sheet(sheetName)
                .registerWriteHandler(new CustomWaterMarkHandler(watermark))
                .doWrite(data);
    }


    @Slf4j
    private static class CustomWaterMarkHandler implements SheetWriteHandler {

        private final Watermark watermark;

        public CustomWaterMarkHandler(Watermark watermark) {
            this.watermark = watermark;
        }

        @Override
        public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
            BufferedImage bufferedImage = createWatermarkImage();
            XSSFWorkbook workbook = (XSSFWorkbook) writeSheetHolder.getParentWriteWorkbookHolder().getWorkbook();
            try {
                // 添加水印的具体操作
                addWatermarkToSheet(workbook, bufferedImage);
            } catch (Exception e) {
                log.error("添加水印出错:", e);
            }

        }

        /**
         * 创建水印图片
         *
         * @return
         */
        private BufferedImage createWatermarkImage() {
            // 获取水印相关参数
            Font font = watermark.getFont();
            int width = watermark.getWidth();
            int height = watermark.getHeight();
            Color color = watermark.getColor();
            String text = watermark.getContent();

            // 创建带有透明背景的 BufferedImage
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = image.createGraphics();

            // 设置画笔字体、平滑、颜色
            g.setFont(font);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(color);

            // 计算水印位置和角度
            int y = watermark.getYAxis();
            int x = watermark.getXAxis();
            AffineTransform transform = AffineTransform.getRotateInstance(Math.toRadians(-watermark.getAngle()), 0, y);
            g.setTransform(transform);
            // 绘制水印文字
            g.drawString(text, x, y);

            // 释放资源
            g.dispose();

            return image;
        }

        private void addWatermarkToSheet(XSSFWorkbook workbook, BufferedImage watermarkImage) {
            try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                ImageIO.write(watermarkImage, "png", os);
                int pictureIdx = workbook.addPicture(os.toByteArray(), XSSFWorkbook.PICTURE_TYPE_PNG);
                XSSFPictureData pictureData = workbook.getAllPictures().get(pictureIdx);
                for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                    // 获取每个Sheet表
                    XSSFSheet sheet = workbook.getSheetAt(i);
                    PackagePartName ppn = pictureData.getPackagePart().getPartName();
                    String relType = XSSFRelation.IMAGES.getRelation();
                    PackageRelationship pr = sheet.getPackagePart().addRelationship(ppn, TargetMode.INTERNAL, relType, null);
                    sheet.getCTWorksheet().addNewPicture().setId(pr.getId());
                }
            } catch (Exception e) {
                // 处理ImageIO.write可能抛出的异常
                log.error("添加水印图片时发生错误", e);
            }
        }
    }

    @Data
    private static class Watermark {

        public Watermark(String content) {
            this.content = content;
            init();
        }

        public Watermark(String content, Color color, Font font, double angle) {
            this.content = content;
            this.color = color;
            this.font = font;
            this.angle = angle;
            init();
        }

        /**
         * 根据水印内容长度自适应水印图片大小，简单的三角函数
         */
        private void init() {
            FontMetrics fontMetrics = new JLabel().getFontMetrics(this.font);
            int stringWidth = fontMetrics.stringWidth(this.content);
            int charWidth = fontMetrics.charWidth('A');
            this.width = (int) Math.abs(stringWidth * Math.cos(Math.toRadians(this.angle))) + 5 * charWidth;
            this.height = (int) Math.abs(stringWidth * Math.sin(Math.toRadians(this.angle))) + 5 * charWidth;
            this.yAxis = this.height;
            this.xAxis = charWidth;
        }

        /**
         * 水印内容
         */
        private String content;

        /**
         * 画笔颜色
         */
        private Color color = new Color(239,239,239);

        /**
         * 字体样式
         */
        private Font font = new Font("Microsoft YaHei", Font.BOLD, 26);

        /**
         * 水印宽度
         */
        private int width;

        /**
         * 水印高度
         */
        private int height;

        /**
         * 倾斜角度，非弧度制
         */
        private double angle = 25;

        /**
         * 字体的y轴位置
         */
        private int yAxis = 50;

        /**
         * 字体的X轴位置
         */
        private int xAxis;

        /**
         * 水平倾斜度
         */
        private double shearX = 0.1;

        /**
         * 垂直倾斜度
         */
        private double shearY = -0.26;
    }
}
