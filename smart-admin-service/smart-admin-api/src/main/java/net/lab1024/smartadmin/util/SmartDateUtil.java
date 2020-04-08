package net.lab1024.smartadmin.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 线程安全的date工具类
 *
 * @author jiaozi
 */
public class SmartDateUtil extends DateUtils {

    private static final ThreadLocal<DateFormats> dateFormats = new ThreadLocal<DateFormats>() {
        @Override
        protected DateFormats initialValue() {
            return new DateFormats();
        }
    };

    public static final int HOUR_MIN = 60;

    public static final int DAY_MI_SECOND = 24 * 60 * 60 * 1000;

    public static String formatYMD(Date date) {
        return dateFormats.get().ymd.format(date);
    }

    public static String formatYMDDigital(Date date) {
        return dateFormats.get().ymdDigital.format(date);
    }

    public static String formatYMDHMSDigital(Date date) {
        return dateFormats.get().ymdhmsDigital.format(date);
    }

    public static String formatYM(Date date) {
        return dateFormats.get().ym.format(date);
    }

    public static String formatHMS(Date date) {
        return dateFormats.get().hms.format(date);
    }

    public static String formatHM(Date date) {
        return dateFormats.get().hm.format(date);
    }

    public static String formatYMDHM(Date date) {
        return dateFormats.get().ymdhm.format(date);
    }

    public static String formatYMDHMS(Date date) {
        return dateFormats.get().ymdhms.format(date);
    }

    public static String formatYMDChinese(Date date) {
        return dateFormats.get().ymdChinese.format(date);
    }

    public static String formatYMDSlash(Date date) {
        return dateFormats.get().ymdSlash.format(date);
    }

    public static Date parseYMD(String dateStr) {
        return parse(dateFormats.get().ymd, dateStr);
    }

    public static Date parseYMDDigital(String dateStr) {
        return parse(dateFormats.get().ymdDigital, dateStr);
    }

    public static Date parseYMDHMSDigital(String dateStr) {
        return parse(dateFormats.get().ymdhmsDigital, dateStr);
    }

    public static Date parseformatYMDChinese(String dateStr) {
        return parse(dateFormats.get().ymdChinese, dateStr);
    }

    public static Date parseYM(String dateStr) {
        return parse(dateFormats.get().ym, dateStr);
    }

    public static Date parseYMDHMS(String dateStr) {

        return parse(dateFormats.get().ymdhms, dateStr);
    }

    public static Date parseYMDHM(String dateStr) {
        return parse(dateFormats.get().ymdhm, dateStr);
    }

    public static Date parseTodayHMS(String dateStr) {
        String today = formatYMD(new Date());
        String todayDateStr = String.format("%s %s", today, dateStr);
        return parse(dateFormats.get().ymdhms, todayDateStr);
    }

    /**
     * 判断当前时间是否在某段时间内 参数不区分先后顺序
     */
    public static boolean isDuringTwoDate(Date date, Date another) {
        long dateTime = date.getTime();
        long anotherTime = another.getTime();
        long currentTime = System.currentTimeMillis();

        if (currentTime > dateTime && currentTime < anotherTime) {
            return true;
        } else if (currentTime > anotherTime && currentTime < dateTime) {
            return true;
        } else {
            return false;
        }
    }

    public static Date parse(SimpleDateFormat format, String dateStr) {
        try {
            Date d = format.parse(dateStr);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            int year = c.get(Calendar.YEAR);
            if (year >= 1000 && year <= 9999) {
                return d;
            } else {
                return null;
            }
        } catch (Exception ex) {
            return null;
        }
    }

    public static long daysOffset(Date date1, Date date2) {
        date1 = parseYMD(formatYMD(date1));
        date2 = parseYMD(formatYMD(date2));
        return (date1.getTime() - date2.getTime()) / DAY_MI_SECOND;
    }

    /**
     * 今天是星期几 , 7表示星期日
     *
     * @return
     */
    public static int getTodayDayOfWeek() {
        Calendar now = Calendar.getInstance();
        int dayOfweek = now.get(Calendar.DAY_OF_WEEK);
        dayOfweek--;
        if (dayOfweek == 0) {
            dayOfweek = 7;
        }
        return dayOfweek;
    }

    public static boolean isTodaytDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Calendar todayCalendar = Calendar.getInstance();
        if (calendar.get(Calendar.YEAR) != todayCalendar.get(Calendar.YEAR)) {
            return false;
        } else if (calendar.get(Calendar.MONTH) != todayCalendar.get(Calendar.MONTH)) {
            return false;
        } else if (calendar.get(Calendar.DAY_OF_MONTH) != todayCalendar.get(Calendar.DAY_OF_MONTH)) {
            return false;
        }
        return true;
    }

    /**
     * 设置Calendar的小时、分钟、秒、毫秒
     *
     * @param calendar 日历
     * @param hour 小时
     * @param minute 分钟
     * @param second 秒
     * @param milliSecond 毫秒
     */
    public static void setCalender(Calendar calendar, int hour, int minute, int second, int milliSecond) {
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, milliSecond);
    }

    /**
     * 获取指定天开始时间
     *
     * @param date 日期
     * @return 获得该日期的开始
     */
    public static Date getDayBegin(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        setCalender(calendar, 0, 0, 0, 0);
        return calendar.getTime();
    }

    /**
     * 获取指定天结束时间
     *
     * @param date 日期
     * @return 获得该日期的结束
     */
    public static Date getDayEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        setCalender(calendar, 23, 59, 59, 999);
        return calendar.getTime();
    }

    /**
     * 获取该日期当月第一天
     *
     * @param date
     * @return
     */
    public static Date getMonthBegin(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDayBegin(date));
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * 获取该日期当月最后一天getAgeByBirthday
     *
     * @param date
     * @return
     */
    public static Date getMonthEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDayEnd(date));
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, - 1);
        return calendar.getTime();
    }

    public static String timeDifference(Date endDate) {
        Date nowDate = new Date();
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // 获得两个时间的毫秒时间差异
        long diff = nowDate.getTime() - endDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        if (day > 0) {
            return day + "天前";
        }
        // 计算差多少小时
        long hour = diff % nd / nh;
        if (hour > 0) {
            return hour + "小时前";
        }
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        if (min > 0) {
            return "1小时内";
        }
        return "1小时内";
    }

    /**
     * 计算所用时长
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static BigDecimal timeDifferenceMin(Date startDate, Date endDate) {
        long nm = 1000 * 60;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - startDate.getTime();
        BigDecimal min = BigDecimal.valueOf(diff).divide(BigDecimal.valueOf(nm), RoundingMode.HALF_UP);
        return min;
    }

    /**
     * 功能描述: 是否为当天
     *
     * @param dateStr yyyy-mm-dd
     * @return
     * @auther yandanyang
     * @date 2018/10/16 0016 下午 17:43
     */
    public static boolean isCurrentDayYMD(String dateStr) {
        if (StringUtils.isEmpty(dateStr)) {
            return true;
        }
        String current = SmartDateUtil.formatYMD(new Date());
        if (current.equals(dateStr)) {
            return true;
        }
        return false;
    }

    /**
     * 功能描述: 是否为当月
     *
     * @param dateStr yyyy-mm-dd
     * @return
     * @auther yandanyang
     * @date 2018/10/16 0016 下午 17:43
     */
    public static boolean isCurrentMonthYMD(String dateStr) {
        if (StringUtils.isEmpty(dateStr)) {
            return true;
        }
        String queryDate = SmartDateUtil.formatYM(SmartDateUtil.parseYMD(dateStr));
        String current = SmartDateUtil.formatYM(new Date());
        if (current.equals(queryDate)) {
            return true;
        }
        return false;
    }

    public static boolean isCurrentMonthYM(String dateStr) {
        if (StringUtils.isEmpty(dateStr)) {
            return true;
        }
        String current = SmartDateUtil.formatYM(new Date());
        if (current.equals(dateStr)) {
            return true;
        }
        return false;
    }

    /**
     * 获取本周的开始时间
     *
     * @return
     */
    public static Date getBeginDayOfWeek() {
        Date date = new Date();
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == 1) {
            dayofweek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayofweek);
        return getDayBegin(cal.getTime());
    }

    /**
     * 获取本周的结束时间
     *
     * @return
     */
    public static Date getEndDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getBeginDayOfWeek());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date weekEndSta = cal.getTime();
        return getDayEnd(weekEndSta);
    }

    /**
     * 获取两个日期区间的日期（包括这两个日期）
     */
    public static List<String> getiIntervalDate(String dateBegin, String dateEnd) {
        List<String> dateList = new ArrayList<>();
        Date startDate = SmartDateUtil.parseYMD(dateBegin);
        Date endDate = SmartDateUtil.parseYMD(dateEnd);
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        dateList.add(dateBegin);
        while (cal.getTime().compareTo(endDate) < 0) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
            dateList.add(SmartDateUtil.formatYMD(cal.getTime()));
        }
        return dateList;
    }

    /**
     * 返回某个日期后几天的日期
     *
     * @param date
     * @param i
     * @return
     */
    public static Date getNextDay(Date date, int i) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) + i);
        return cal.getTime();
    }

    /**
     * 返回某个日期前几天的日期
     *
     * @param date
     * @param i
     * @return
     */
    public static Date getFrontDay(Date date, int i) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) - i);
        return cal.getTime();
    }

    /**
     * 获取昨天的开始时间
     *
     * @return
     */
    public static Date getBeginDayOfYesterday() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(getDayBegin(new Date()));
        cal.add(Calendar.DAY_OF_MONTH, - 1);
        return cal.getTime();
    }

    /**
     * 获取昨天的结束时间
     *
     * @return
     */
    public static Date getEndDayOfYesterDay() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(getDayEnd(new Date()));
        cal.add(Calendar.DAY_OF_MONTH, - 1);
        return cal.getTime();
    }

    public static Integer getDayNumOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        Integer num = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        return num;
    }

    /**
     * 转换日期（格式：年-月-日 时:分--分自定义）
     */
    public static String formatYMDH(Date date, String minute) {
        String ymdhm = dateFormats.get().ymdh + ":" + minute;
        SimpleDateFormat format = new SimpleDateFormat(ymdhm);
        return format.format(date);
    }

    /**
     * 获取几个月后的日期
     */
    public static Date getAfterMonth(Date inputDate, int number) {
        Calendar c = Calendar.getInstance();//获得一个日历的实例
        c.setTime(inputDate);//设置日历时间
        c.add(Calendar.MONTH, number);//在日历的月份上增加月
        return c.getTime();
    }

    /**
     * 计算当前月有多少天
     */
    public static int getDays(int year, int month) {
        int days = 0;
        if (month != 2) {
            switch (month) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    days = 31;
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    days = 30;

            }
        } else {
            // 闰年
            if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
                days = 29;
            } else {
                days = 28;
            }
        }
        System.out.println("当月有" + days + "天！");
        return days;
    }

}

class DateFormats {

    public final SimpleDateFormat hms = new SimpleDateFormat("HH:mm:ss");

    public final SimpleDateFormat hm = new SimpleDateFormat("HH:mm");

    public final SimpleDateFormat ymdhm = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public final SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");

    public final SimpleDateFormat ym = new SimpleDateFormat("yyyy-MM");

    public final SimpleDateFormat ymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public final SimpleDateFormat ymdChinese = new SimpleDateFormat("yyyy年MM月dd日");

    public final SimpleDateFormat ymdSlash = new SimpleDateFormat("yyyy/MM/dd");

    public final SimpleDateFormat ymdDigital = new SimpleDateFormat("yyyyMMdd");

    public final SimpleDateFormat ymdhmsDigital = new SimpleDateFormat("yyyyMMddHHmmss");

    public static final String ymdh = "yyyy-MM-dd HH";
}

