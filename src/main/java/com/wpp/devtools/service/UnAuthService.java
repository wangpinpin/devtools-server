package com.wpp.devtools.service;

import com.wpp.devtools.config.UrlConfig;
import com.wpp.devtools.util.HttpUtil;
import org.springframework.stereotype.Service;

@Service
public class UnAuthService {

    /**
     * 舔狗日记
     * @return
     */
    public Object getDoglickingDiary() {
       return HttpUtil.get(UrlConfig.DOG_LICKING_DIARY_URL, null);
    }
}
