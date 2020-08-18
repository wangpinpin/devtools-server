package com.wpp.devtools.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @program: volvo-sever
 * @description:
 * @author: wpp
 * @create: 2020-08-17
 **/
@Component
@Data
public class WXconfig {
    @Value("${WX.APPID}")
    public String appId;

    @Value("${WX.SECRET}")
    public String secret;

    public static final String WX_URL_TOKEN = "wangpinpin";
    public static final String WX_ACCESS_TOKEN_KEY = "WX_ACCESS_TOKEN_KEY";
    public static final String WX_GET_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={0}&secret={1}";
    public static final String WX_GET_USER_LIST_URL = "https://api.weixin.qq.com/cgi-bin/user/get?access_token={1}&next_openid={1}";
    public static final String WX_GET_USER_INFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token={0}&openid={1}&lang=zh_CN";
    public static final String WX_CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";


}
