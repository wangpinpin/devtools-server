package com.wpp.devtools.model;

import com.alibaba.fastjson.JSONObject;
import com.wpp.devtools.config.JWTConfig;
import com.wpp.devtools.config.RedisKeyConfig;
import com.wpp.devtools.domain.annotation.AccessLimit;
import com.wpp.devtools.enums.ExceptionCodeEnums;
import com.wpp.devtools.exception.CustomException;
import com.wpp.devtools.util.CommonUtils;
import com.wpp.devtools.util.JWTUtil;
import com.wpp.devtools.util.RedistUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @program: devtools-server
 * @description:
 * @author: wpp
 * @create: 2020-07-06
 **/
@Component
@Slf4j
public class FilterInterceptor extends HandlerInterceptorAdapter {


    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private RedistUtil redistUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) {

        String ip = CommonUtils.getIpAddr(request);
        log.info("IP进入: " + ip);
        //IP黑名单
        if (redistUtil.getStringToHash(RedisKeyConfig.BLACKLIST, ip)) {
            throw new CustomException(ExceptionCodeEnums.HTTP_REQUEST_FREQUENTLY);
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
                Long warningCount = redistUtil.incr(RedisKeyConfig.WARNING + ip, 3600 * 24);
                //警告次数超过10次，ip上黑名单
                if (warningCount > 10) {
                    redistUtil.setStringToHash(RedisKeyConfig.BLACKLIST, ip, "");
                }
                throw new CustomException(ExceptionCodeEnums.HTTP_REQUEST_FREQUENTLY);
            }
        }

        final String authHeader = request.getHeader(JWTConfig.JWT_HEADER);
        if(StringUtils.isNotBlank(authHeader)) {
            final JSONObject jsonObject = jwtUtil.parseJWT(authHeader);
            request.setAttribute(JWTConfig.JWT_USER_ID_KEY, jsonObject.get(JWTConfig.JWT_USER_ID_KEY));
        }


        return true;
    }



}
