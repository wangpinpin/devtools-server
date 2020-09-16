package com.wpp.devtools.util;

import com.alibaba.fastjson.JSON;
import com.google.common.base.CaseFormat;
import com.wpp.devtools.enums.ExceptionCodeEnums;
import com.wpp.devtools.exception.CustomException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

/**
 * @program: volvo-server
 * @description:
 * @author: wpp
 * @create: 2020-07-14
 **/
public class CommonUtils {

    private static final String[] PROXYS = {"x-forwarded-for", "Proxy-Client-IP",
            "WL-Proxy-Client-IP", "X-Real-IP", "HTTP_CLIENT_IP"};

    private final static SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final static SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * map key转成驼峰
     *
     * @param map
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T toCamelCase(Map<String, Object> map, Class<T> clazz) {
        Map var = new HashMap();
        map.forEach((k, v) -> {
            String s = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, k);
            var.put(s, v);
        });
        return JSON.parseObject(JSON.toJSONString(var), clazz);
    }


    /**
     * List map key转成驼峰
     *
     * @param list
     * @return
     */
    public static List<Map<String, Object>> toCamelCase(List<Map<String, Object>> list) {
        List<Map<String, Object>> listCopy = new ArrayList<>();
        list.stream().forEach(e -> {
            Map var = new HashMap();
            e.forEach((k, v) -> {
                String s = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, k);
                var.put(s, v);
            });
            listCopy.add(var);
        });
        return listCopy;
    }

    /**
     * 获取当月的的最初时间
     *
     * @return
     */
    public static Timestamp getThisMonthTimeFirstTimeByMonth() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, 1);
        return timeFormat(c.getTime(), "00:00:00");
    }

    /**
     * 获取当月的最后时间
     *
     * @return
     */
    public static Timestamp getThisMonthTimeLastTimeByMonth() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return timeFormat(c.getTime(), "23:59:59");
    }


    /**
     * 根据年月获取最初的时间
     *
     * @return
     */
    public static Timestamp getTimeByYearAndMonth(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.DATE, 1);
        return timeFormat(c.getTime(), "00:00:00");
    }

    /**
     * 根据年季度获取最初的时间
     *
     * @return
     */
    public static Timestamp getTimeByYearAndQuarter(int year, int quarter) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, (quarter - 1) * 3);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.DATE, 1);
        return timeFormat(c.getTime(), "00:00:00");
    }

    /**
     * 获取当前季度的最初时间
     *
     * @return
     */
    public static Timestamp getThisQuarterFirstTime() {
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH) + 1;
        c.set(Calendar.MONTH, monthToFirstMonth(month));
        c.set(Calendar.DATE, 1);
        return timeFormat(c.getTime(), "00:00:00");

    }

    /**
     * 获取下个季度的最初时间
     *
     * @return
     */
    public static Timestamp getNextQuarterFirstTime() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, +3);//把月份加三个月
        int month = c.get(Calendar.MONTH) + 1;
        c.set(Calendar.MONTH, monthToFirstMonth(month));
        c.set(Calendar.DATE, 1);
        return timeFormat(c.getTime(), "00:00:00");
    }

    /**
     * 获取当前季度的最后时间
     *
     * @return
     */
    public static Timestamp getThisQuarterLastTime() {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        if (currentMonth >= 1 && currentMonth <= 3) {
            c.set(Calendar.MONTH, 2);
            c.set(Calendar.DATE, 31);
        } else if (currentMonth >= 4 && currentMonth <= 6) {
            c.set(Calendar.MONTH, 5);
            c.set(Calendar.DATE, 30);
        } else if (currentMonth >= 7 && currentMonth <= 9) {
            c.set(Calendar.MONTH, 8);
            c.set(Calendar.DATE, 30);
        } else if (currentMonth >= 10 && currentMonth <= 12) {
            c.set(Calendar.MONTH, 11);
            c.set(Calendar.DATE, 31);
        }
        return timeFormat(c.getTime(), "23:59:59");
    }

    /**
     * 根据月份获取这个季度的第一个月
     *
     * @param month
     * @return
     */
    private static int monthToFirstMonth(int month) {
        int firstMonth = 0;
        if (month >= 1 && month <= 3) {
            firstMonth = 0;
        } else if (month >= 4 && month <= 6) {
            firstMonth = 3;
        } else if (month >= 7 && month <= 9) {
            firstMonth = 6;
        } else if (month >= 10 && month <= 12) {
            firstMonth = 9;
        }
        return firstMonth;
    }

    /**
     * 时间格式化
     *
     * @param time
     * @return
     */
    private static Timestamp timeFormat(Date time, String HMS) {
        try {
            time = longSdf.parse(shortSdf.format(time) + " " + HMS);
        } catch (ParseException e) {
            throw new CustomException(ExceptionCodeEnums.TIME_ERROR);
        }
        return new Timestamp(time.getTime());
    }


    /**
     * 图片转base64
     *
     * @param file
     * @return
     */
    public static String toBaseImg64(MultipartFile file) {
        BASE64Encoder encode = new BASE64Encoder();
        try {
            return encode.encode(file.getBytes()).replaceAll("[\\s*\t\n\r]", "");
        } catch (IOException e) {
            throw new CustomException(ExceptionCodeEnums.ERROR, e.getMessage());
        }
    }

    /**
     * 获取客户端ip
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个IP值，第一个为真实IP。
            int index = ip.indexOf(',');
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        } else {
            return request.getRemoteAddr();
        }
    }

    public static String getRandomCodeByCount(int count) {
        String[] beforeShuffle = new String[]{"1", "2", "3", "4", "5", "6", "7",
                "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
                "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
                "W", "X", "Y", "Z"};
        List list = Arrays.asList(beforeShuffle);
        Collections.shuffle(list);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(list.get(i));
        }
        return sb.toString();
    }

}
