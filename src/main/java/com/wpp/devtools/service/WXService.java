package com.wpp.devtools.service;

import com.alibaba.fastjson.JSONObject;
import com.wpp.devtools.config.WXKeyconfig;
import com.wpp.devtools.config.WXconfig;
import com.wpp.devtools.util.HttpUtil;
import com.wpp.devtools.util.RedistUtil;
import java.text.MessageFormat;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WXService {

    @Autowired
    private RedistUtil redistUtil;

    @Autowired
    private WXKeyconfig wxKeyconfig;







    /**
     * 获取access_token
     *
     * @return
     */
    public String getAccessToken() {

        String accessToken = redistUtil.getString(WXconfig.ACCESS_TOKEN_KEY);
        //如果缓存不存在，去api获取access_token
        if (StringUtils.isBlank(accessToken)) {
            String result = HttpUtil.get(MessageFormat
                    .format(WXconfig.WX_GET_ACCESS_TOKEN_URL, wxKeyconfig.getAppId(),
                            wxKeyconfig.getAppSecret()), null);
            JSONObject jo = JSONObject.parseObject(result);
            accessToken = jo.getString("access_token");
            //微信失效2小时, 这里保存1小时
            redistUtil.setString(WXconfig.ACCESS_TOKEN_KEY, accessToken, 3600L);
        }

        return accessToken;
    }
}
