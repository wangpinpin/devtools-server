package com.wpp.devtools.service;

import com.alibaba.fastjson.JSONObject;
import com.wpp.devtools.config.BaiduConfig;
import com.wpp.devtools.config.RedisKeyConfig;
import com.wpp.devtools.config.UrlConfig;
import com.wpp.devtools.domain.entity.Wb;
import com.wpp.devtools.enums.ExceptionCodeEnums;
import com.wpp.devtools.exception.CustomException;
import com.wpp.devtools.repository.WbRepository;
import com.wpp.devtools.util.CommonUtils;
import com.wpp.devtools.util.HttpUtil;
import com.wpp.devtools.util.RedistUtil;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UnAuthService {

    @Autowired
    private BaiduConfig baiduConfig;

    @Autowired
    private RedistUtil redistUtil;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public void readTxtFileByFileUtils() {
        File file = new File("C:\\Users\\pinpin.wang\\Desktop\\a.txt");
        List<Wb> wbs = new ArrayList<>(1000);

        try {
            LineIterator lineIterator = FileUtils.lineIterator(file);
            while (lineIterator.hasNext()) {
                String line = lineIterator.nextLine();

                // 行数据转换成数组
                String[] arr = line.split("      ");
                Wb wb = Wb.builder().phone(Long.parseLong(arr[0].trim())).wid(Long.parseLong(arr[1].trim())).build();
                wbs.add(wb);
                if (wbs.size() > 100) {
                    insertWbInfo(wbs);
                    wbs.clear();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void insertWbInfo(List<Wb> wbs) {
        jdbcTemplate.batchUpdate("INSERT INTO wb (phone, wid) VALUES (?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i)
                            throws SQLException {
                        Wb wb = wbs.get(i);
                        ps.setLong(1, wb.getPhone());
                        ps.setLong(2, wb.getWid());
                    }

                    @Override
                    public int getBatchSize() {
                        return wbs.size();
                    }
                });
    }

    /**
     * 舔狗日记
     *
     * @return
     */
    public Object getDoglickingDiary() {
        return HttpUtil.get(UrlConfig.DOG_LICKING_DIARY_URL, null);
    }


    /**
     * 图片转文字
     *
     * @return
     */
    public Object imgToText(MultipartFile file, String languageType) {
        if (file.isEmpty()) {
            throw new CustomException(ExceptionCodeEnums.PARAM_NULL);
        }

        String accessToken = getBaiduAccessToken();

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        String imgStr = CommonUtils.toBaseImg64(file);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("image", imgStr);
        if(!StringUtils.isEmpty(languageType)) {
            params.add("language_type", imgStr);
        }
        String result = HttpUtil
                .post(MessageFormat.format(UrlConfig.BAIDU_IMG_TO_TEXT_URL, accessToken), params,
                        headers);
        return JSONObject.parseObject(result);

    }


    /**
     * 获取百度AccessToken
     *
     * @return
     */
    private String getBaiduAccessToken() {
        String accessToken = redistUtil.getString(RedisKeyConfig.BAIDU_ACCESS_TOKEN_KEY);

        if (StringUtils.isEmpty(accessToken)) {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "client_credentials");
            params.add("client_id", baiduConfig.getApiKey());
            params.add("client_secret", baiduConfig.getSecretKey());
            try {
                String result = HttpUtil.post(UrlConfig.BAIDU_ACCESS_TOKEN_URL, params);
                JSONObject jsonObject = JSONObject.parseObject(result);
                accessToken = jsonObject.getString("access_token");
                //设置20天过期, 百度accessToken过期时间在一个月左右
                redistUtil.setString(RedisKeyConfig.BAIDU_ACCESS_TOKEN_KEY, accessToken, 1728000L);
            } catch (Exception e) {
                throw new CustomException(ExceptionCodeEnums.ERROR, e.getMessage());
            }
        }

        return accessToken;
    }
}
