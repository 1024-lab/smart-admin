package net.lab1024.sa.common.common.excel;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.excel.entity.params.ExcelForEachParams;
import cn.afterturn.easypoi.excel.export.styler.IExcelExportStyler;
import org.apache.poi.ss.usermodel.*;

/**
 * excel样式
 *
 * @Author 1024创新实验室: 罗伊
 * @Date 2020/9/25 19:52
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright 1024创新实验室 （ https://1024lab.net ）
 */
public class ExcelStyle implements IExcelExportStyler {
    private static final short STRING_FORMAT = (short) BuiltinFormats.getBuiltinFormat("TEXT");
    private static final short FONT_SIZE_TEN = 10;
    private static final short FONT_SIZE_ELEVEN = 11;
    private static final short FONT_SIZE_TWELVE = 12;
    /**
     * 大标题样式
     */
    private CellStyle headerStyle;
    /**
     * 每列标题样式
     */
    private CellStyle titleStyle;
    /**
     * 数据行样式
     */
    private CellStyle styles;

    public ExcelStyle(Workbook workbook) {
        this.init(workbook);
    }

    /**
     * 初始化样式
     *
     * @param workbook
     */
    private void init(Workbook workbook) {
        this.headerStyle = initHeaderStyle(workbook);
        this.titleStyle = initTitleStyle(workbook);
        this.styles = initStyles(workbook);
    }

    /**
     * 大标题样式
     *
     * @param color
     * @return
     */
    @Override
    public CellStyle getHeaderStyle(short color) {
        return headerStyle;
    }

    /**
     * 每列标题样式
     *
     * @param color
     * @return
     */
    @Override
    public CellStyle getTitleStyle(short color) {
        return titleStyle;
    }

    /**
     * 数据行样式
     *
     * @param parity 可以用来表示奇偶行
     * @param entity 数据内容
     * @return 样式
     */
    public CellStyle getStyles(boolean parity, ExcelExportEntity entity) {
        return styles;
    }

    /**
     * 获取样式方法
     *
     * @param dataRow 数据行
     * @param obj     对象
     * @param data    数据
     */
    @Override
    public CellStyle getStyles(Cell cell, int dataRow, ExcelExportEntity entity, Object obj, Object data) {
        return getStyles(true, entity);
    }

    /**
     * 模板使用的样式设置
     */
    @Override
    public CellStyle getTemplateStyles(boolean isSingle, ExcelForEachParams excelForEachParams) {
        return null;
    }

    /**
     * 初始化--大标题样式
     *
     * @param workbook
     * @return
     */
    private CellStyle initHeaderStyle(Workbook workbook) {
        CellStyle style = getBaseCellStyle(workbook);
        style.setFont(getFont(workbook, FONT_SIZE_TWELVE, true));
        return style;
    }

    /**
     * 初始化--每列标题样式
     *
     * @param workbook
     * @return
     */
    private CellStyle initTitleStyle(Workbook workbook) {
        CellStyle style = getBaseCellStyle(workbook);
        style.setFont(getFont(workbook, FONT_SIZE_ELEVEN, false));
        //背景色
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    /**
     * 初始化--数据行样式
     *
     * @param workbook
     * @return
     */
    private CellStyle initStyles(Workbook workbook) {
        CellStyle style = getBaseCellStyle(workbook);
        style.setFont(getFont(workbook, FONT_SIZE_TEN, false));
        style.setDataFormat(STRING_FORMAT);
        style.setAlignment(HorizontalAlignment.LEFT);
        return style;
    }

    /**
     * 基础样式
     *
     * @return
     */
    private CellStyle getBaseCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        //下边框
        style.setBorderBottom(BorderStyle.THIN);
        //左边框
        style.setBorderLeft(BorderStyle.THIN);
        //上边框
        style.setBorderTop(BorderStyle.THIN);
        //右边框
        style.setBorderRight(BorderStyle.THIN);
        //水平居中
        style.setAlignment(HorizontalAlignment.CENTER);
        //上下居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置自动换行
        style.setWrapText(true);
        return style;
    }

    /**
     * 字体样式
     *
     * @param size   字体大小
     * @param isBold 是否加粗
     * @return
     */
    private Font getFont(Workbook workbook, short size, boolean isBold) {
        Font font = workbook.createFont();
        //字体样式
        font.setFontName("宋体");
        //是否加粗
        font.setBold(isBold);
        //字体大小
        font.setFontHeightInPoints(size);
        return font;
    }
}