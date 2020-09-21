package com.wpp.devtools.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @program: devtools-server
 * @description:
 * @author: wpp
 * @create: 2020-07-06
 **/
@Component
@Data
public class JWTConfig {

    @Value("${JWT.JWT_CLIENTID}")
    private String jwtClientid;

    @Value("${JWT.JWT_BASE64SECRET}")
    private String jwtBase64secret;

    public final static String JWT_NAME = "xiaopozhan";

    public final static long JWT_EXPIRESSECOND = 9999999999L; //永久

    public final static String JWT_BEARER = "Bearer";

    public final static String JWT_HEADER = "Authorization";

    public final static String JWT_USER_ID_KEY = "USERID";

}