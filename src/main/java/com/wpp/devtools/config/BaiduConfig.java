package com.wpp.devtools.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class BaiduConfig {

    @Value("${BAIDU.APP_ID}")
    private String appId;

    @Value("${BAIDU.API_KEY}")
    private String apiKey;

    @Value("${BAIDU.SECRET_KEY}")
    private String secretKey;
}
