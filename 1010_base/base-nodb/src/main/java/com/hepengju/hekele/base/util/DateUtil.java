package com.hepengju.hekele.base.util;

import com.alibaba.fastjson.JSON;
import com.hepengju.hekele.base.constant.HeConst;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

/**
 * 日期工具类
 */
public class DateUtil {

    public static final String DATE_TIME_FORMAT = HeConst.DATE_TIME_FORMAT;
    public static final String DATE_FORMAT = HeConst.DATE_FORMAT;

    public static final DateTimeFormatter yyyy_MM_ddPattern = DateTimeFormatter.ofPattern(HeConst.DATE_FORMAT);
    public static final DateTimeFormatter yyyy_MM_dd_HH_mm_ssPattern = DateTimeFormatter.ofPattern(HeConst.DATE_TIME_FORMAT);

    public static final DateTimeFormatter yyyyMMPattern = DateTimeFormatter.ofPattern("yyyyMM");
    public static final DateTimeFormatter yyyyMMddPattern = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final DateTimeFormatter yyyyMMddHHmmssPattern = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * 日期格式化为字符串
     */
    public static String dateToString(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    public static String dateToString(Date date) {
        return new SimpleDateFormat(DATE_TIME_FORMAT).format(date);
    }

    public static String dateToString(LocalDate date) {
        return yyyy_MM_ddPattern.format(date);
    }

    public static String dateToString(LocalDateTime date) {
        return yyyy_MM_dd_HH_mm_ssPattern.format(date);
    }

    /**
     * 解析常用的日期字符串
     *
     * @see com.hepengju.hekele.base.config.springmvc.DateConverter
     */
    public static Date stringToDate(String source) {
        String dateStr = JSON.toJSONString(source);
        Date date = JSON.parseObject(dateStr, Date.class);
        return date;
    }

    public static final String yyyMMddHHmmss() {
        return yyyyMMddHHmmssPattern.format(LocalDateTime.now());
    }

    public static final String yyyyMMdd() {
        return yyyyMMddPattern.format(LocalDate.now());
    }

    public static String displayDuration(long durationMillis) {
        String words = DurationFormatUtils.formatDurationWords(durationMillis, true, true);
        String newWords = words
                .replace("days", "d")
                .replace("day", "d")
                .replace("hours", "h")
                .replace("hour", "h")
                .replace("minutes", "m")
                .replace("minute", "m")
                .replace("seconds", "s")
                .replace("second", "s")
                .replace(" ", "");
        return newWords;
    }

    /**
     * 两个日期之间的天数: Truncate时分秒
     */
    public static long days(Date beginDate, Date endDate) {
        Date begin = DateUtils.truncate(beginDate, Calendar.DAY_OF_MONTH);
        Date end = DateUtils.truncate(endDate, Calendar.DAY_OF_MONTH);
        return Duration.between(begin.toInstant(), end.toInstant()).toDays();
    }

    /**
     * 计算年龄,工龄等
     */
    @SuppressWarnings("deprecation")
    public static int getAge(Date birthdayDate) {
        return new Date().getYear() - birthdayDate.getYear();
    }

    /**
     * 开始月份和结束月份之间的所有月份字符串(格式为yyyymm),包含startMonth和endMonth
     */
    public static Set<String> betweenMonths(String startMonth, String endMonth) {
        LocalDate start = LocalDate.parse(startMonth + "01", yyyyMMddPattern);
        LocalDate end = LocalDate.parse(endMonth + "01", yyyyMMddPattern);

        Set<String> set = new TreeSet<>();
        while (!start.isAfter(end)) {
            set.add(yyyyMMPattern.format(start));
            start = start.plusMonths(1);
        }
        return set;
    }

    /**
     * 取得目标月份
     *
     * @param ldt 当前日期
     * @param num 返回的总月份个数,num为负数,则返回之前; num为正数,则返回之后
     */
    public static Set<String> targetMonths(LocalDate ldt, int num) {
        return targetMonths(yyyyMMPattern.format(ldt), num);
    }

    public static Set<String> targetMonths(String nowMonth, int num) {
        Set<String> set = new TreeSet<>();
        LocalDate now = LocalDate.parse(nowMonth + "01", yyyyMMddPattern);
        set.add(nowMonth);

        if (num == 0) return set;

        int plus = num > 0 ? 1 : -1;
        num = Math.abs(num) - 1;
        while (num > 0) {
            now = now.plusMonths(plus);
            set.add(yyyyMMPattern.format(now));
            num--;
        }

        return set;
    }


}
