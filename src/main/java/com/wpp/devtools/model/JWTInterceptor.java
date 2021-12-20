package com.wpp.devtools.model;

import com.alibaba.fastjson.JSONObject;
import com.wpp.devtools.config.JWTConfig;
import com.wpp.devtools.enums.ExceptionCodeEnums;
import com.wpp.devtools.exception.CustomException;
import com.wpp.devtools.util.JWTUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @program: devtools-server
 * @description:
 * @author: wpp
 * @create: 2020-07-06
 **/
@Component
@Slf4j
public class JWTInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) {
        final String authHeader = request.getHeader(JWTConfig.JWT_HEADER);
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        } else {
            if (null == authHeader || !authHeader.startsWith(JWTConfig.JWT_BEARER)) {
                throw new CustomException(ExceptionCodeEnums.JWT_VALID_ERROR);
            }
        }
        return true;

    }
}
