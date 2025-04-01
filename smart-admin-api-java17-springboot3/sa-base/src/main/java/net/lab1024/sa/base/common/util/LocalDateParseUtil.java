package net.lab1024.sa.base.common.util;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

public class LocalDateParseUtil {


    public static LocalDate parseDate(String dateStr) {
        // 预处理：去掉前缀 B
        if (dateStr.startsWith("B")) {
            dateStr = dateStr.substring(1); // 去掉 B
        }

        // 判断是否只包含年份
        if (dateStr.matches("\\d{4}")) {
            // 只有年份时，补充为1月1日
            return LocalDate.of(Integer.parseInt(dateStr), 1, 1);
        }
        // 预处理：去掉末尾的斜杠
        if (dateStr.endsWith("/")) {
            dateStr = dateStr.substring(0, dateStr.length() - 1);
        }

        // 定义支持的日期格式
        List<DateTimeFormatter> formatters = List.of(
                DateTimeFormatter.ofPattern("yyyy/MM/dd"),
                DateTimeFormatter.ofPattern("yyyyMMdd"),
                DateTimeFormatter.ofPattern("yyyy/MMdd"),
                DateTimeFormatter.ofPattern("yyyy.MM.dd"),
                DateTimeFormatter.ofPattern("yyyy.M.d"),
                DateTimeFormatter.ofPattern("yyyy/M/d"),
//                new DateTimeFormatterBuilder()
//                        .parseCaseInsensitive()  // 忽略大小写
//                        .appendPattern("dd-MMM-yy")
//                        .toFormatter(Locale.ENGLISH),
                new DateTimeFormatterBuilder()
                        .parseCaseInsensitive()  // 忽略大小写
                        .appendPattern("dd-MMM-yy")
                        .toFormatter(Locale.ENGLISH)
        );

        // 尝试使用每个格式进行解析
        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDate.parse(dateStr, formatter);
            } catch (DateTimeParseException ignored) {
                // 如果解析失败，继续尝试下一个格式
            }
        }
//        System.out.println(LocalDate.of(2000, 1, 1));
        return LocalDate.of(2000, 1, 1);
        // 如果所有格式都失败，抛出异常
//        throw new IllegalArgumentException("无法解析日期格式: " + dateStr);
    }

    public static void main(String[] args) {
        System.out.println(parseDate("2021/10/20")); // 输出: 2021-10-20
        System.out.println(parseDate("20220728"));   // 输出: 2022-07-28
        System.out.println(parseDate("2022/0728"));   // 输出: 2022-07-28
        System.out.println(parseDate("2022.07.28"));   // 输出: 2022-07-28
        System.out.println(parseDate("2020.4.9"));   // 输出: 2020-04-09
        System.out.println(parseDate("2020.04.09")); // 输出: 2020-04-09
        System.out.println(parseDate("2024.9.29"));  // 输出: 2024-09-29
        System.out.println(parseDate("2024.9.29/"));  // 输出: 2024-09-29
        System.out.println(parseDate("B2024.9.29"));  // 输出: 2024-09-29
        System.out.println(parseDate("B2017"));  // 输出: 2024-09-29
        System.out.println(parseDate("2019/2/25"));  // 输出: 2024-09-29
        System.out.println(parseDate(""));  // 输出: 2024-09-29

        System.out.println(parseDate("12-JUL-22"));  // 输出: 2024-09-29
        System.out.println(parseDate("12-DEC-22"));  // 输出: 2024-09-29

    }


}

