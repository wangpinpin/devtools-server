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
     * 获取指定格式时间字符串
     * @param date
     * @param format
     * @return
     */
    public static String getTimeStr(Date date, String format){
        SimpleDateFormat sf = new SimpleDateFormat(format);
        return sf.format(date);
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

    /**
     * 获取随机验证码
     * @param count
     * @return
     */
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

    public static int getThisTimeHour() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(calendar.HOUR_OF_DAY);
    }
}
