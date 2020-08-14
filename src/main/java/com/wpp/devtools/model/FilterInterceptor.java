package com.wpp.devtools.model;

import com.wpp.devtools.domain.annotation.AccessLimit;
import com.wpp.devtools.enums.ExceptionCodeEnums;
import com.wpp.devtools.exception.CustomException;
import com.wpp.devtools.util.RedistUtil;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @program: volvo-sever
 * @description:
 * @author: wpp
 * @create: 2020-07-06
 **/
@Component
@Slf4j
public class FilterInterceptor extends HandlerInterceptorAdapter {

    private static final String[] PROXYS = {"x-forwarded-for", "Proxy-Client-IP", "WL-Proxy-Client-IP", "X-Real-IP", "HTTP_CLIENT_IP"};


    @Autowired
    private RedistUtil redistUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler){

        //判断请求是否属于方法的请求
        if (handler instanceof HandlerMethod) {

            HandlerMethod hm = (HandlerMethod) handler;

            //获取方法中的注解,看是否有该注解
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if (accessLimit == null) {
                return true;
            }
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            String ip = getIpAddr(request);
            String key = request.getRequestURI() + ip;

            //统计每个IP访问方法次数
            redistUtil.incr("count" + key + ip);
            //从redis中获取用户访问的次数(redis中保存的key保存(seconds)秒，redisUtils使用的单位是秒，意思是5秒内重复请求接口限制次数)
            String countString = redistUtil.getString(key);
            if (countString == null) {
                //第一次访问
                redistUtil.incr(key, Long.valueOf(seconds));
            } else if (Integer.valueOf(countString) < maxCount) {
                //加1
                redistUtil.incr(key);
            } else {
                //超出访问次数
                log.info("ip: " + ip + ", 超出访问次数");
                throw new CustomException(ExceptionCodeEnums.HTTP_REQUEST_FREQUENTLY);
            }
        }
        return true;
    }


    /**
     * 获取客户端ip
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;

        try {
            for (String proxy: PROXYS) {
                ipAddress = request.getHeader(proxy);
                if (StringUtils.isNotBlank(ipAddress) && !"unknown".equalsIgnoreCase(ipAddress)) {
                    return ipAddress;
                }
            }
            if (StringUtils.isBlank(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals("127.0.0.1")) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        throw new CustomException(ExceptionCodeEnums.GET_IP_ERROR);
                    }
                    ipAddress = inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            // "***.***.***.***".length() = 15
            if (ipAddress != null && ipAddress.length() > 15) {
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress = "";
        }
        return ipAddress;
    }
}
