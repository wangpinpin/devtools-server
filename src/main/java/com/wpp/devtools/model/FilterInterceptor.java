package com.wpp.devtools.model;

import com.wpp.devtools.config.RedisKeyConfig;
import com.wpp.devtools.domain.annotation.AccessLimit;
import com.wpp.devtools.enums.ExceptionCodeEnums;
import com.wpp.devtools.exception.CustomException;
import com.wpp.devtools.util.CommonUtils;
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




    @Autowired
    private RedistUtil redistUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) {

        String ip = CommonUtils.getIpAddr(request);
        log.info("IP进入: " + ip);
        //IP黑名单
        if (redistUtil.getStringToHash(RedisKeyConfig.BLACK, ip)) {
            throw new CustomException(ExceptionCodeEnums.ERROR);
        }

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
            String uri = request.getRequestURI();
            String key = uri + ip;

            //统计每个IP访问方法次数
            redistUtil.incr(RedisKeyConfig.COUNT + key, 3600 * 24);

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
                log.info("ip: " + ip + ", 请求太频繁");
                redistUtil.delKey(key);
                Long warningCount = redistUtil.incr(RedisKeyConfig.WARNING + ip);
                //警告次数超过10次，ip上黑名单
                if (warningCount > 10) {
                    redistUtil.setStringToHash(RedisKeyConfig.BLACK, ip, "");
                }
                throw new CustomException(ExceptionCodeEnums.HTTP_REQUEST_FREQUENTLY);
            }
        }
        return true;
    }



}
