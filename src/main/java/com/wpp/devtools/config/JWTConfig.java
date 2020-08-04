package com.wpp.devtools.config;

/**
 * @program: volvo-sever
 * @description:
 * @author: wpp
 * @create: 2020-07-06
 **/
public class JWTConfig {

    public final static String JWT_CLIENTID = "098f6bcd4621d373cade4e832627b4f6";

    public final static String JWT_BASE64SECRET = "MDk4ZjZiY2Q0NjIxZDM3M2NhZGU0ZTgzMjYyN2I0ZjY=";

    public final static String JWT_NAME = "Volvo";

    //    public final static Integer JWT_EXPIRESSECOND = 604800; // 7天
    public final static long JWT_EXPIRESSECOND = 9999999999L; //永久

    public final static String JWT_BEARER = "Bearer ";

    public final static String JWT_HEADER = "Authorization";

    public final static String JWT_USER_ID_KEY = "USERID";

}