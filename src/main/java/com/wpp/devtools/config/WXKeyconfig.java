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
public class WXKeyconfig {
    @Value("${WX.APPID}")
    public String appId;

    @Value("${WX.SECRET}")
    public String appSecret;

}
