package com.wpp.devtools.service;

import com.alibaba.fastjson.JSONObject;
import com.wpp.devtools.config.BaiduConfig;
import com.wpp.devtools.config.CommonConfig;
import com.wpp.devtools.config.RedisKeyConfig;
import com.wpp.devtools.config.UrlConfig;
import com.wpp.devtools.domain.entity.DogText;
import com.wpp.devtools.domain.entity.TextBoard;
import com.wpp.devtools.enums.ExceptionCodeEnums;
import com.wpp.devtools.exception.CustomException;
import com.wpp.devtools.repository.DogTextRepository;
import com.wpp.devtools.repository.TextBoardRepository;
import com.wpp.devtools.util.CommonUtils;
import com.wpp.devtools.util.HttpUtil;
import com.wpp.devtools.util.RedistUtil;
import java.text.MessageFormat;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException.TooManyRequests;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UnAuthService {

    @Autowired
    private BaiduConfig baiduConfig;

    @Autowired
    private RedistUtil redistUtil;

    @Autowired
    private DogTextRepository dogTextRepository;

    @Autowired
    private TextBoardRepository textBoardRepository;

    /**
     * 舔狗日记
     *
     * @return
     */
    public String getDoglickingDiary() {
        return dogTextRepository.findContentByRandom();

    }


    /**
     * 同步舔狗日记
     *
     * @throws InterruptedException
     */
    public void getDoglickingDiaryListInsert() throws InterruptedException {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("token", CommonConfig.ALAPI_TOKEN);
        for (int i = 0; i < 500; i++) {
            try {
                String result = HttpUtil.get(UrlConfig.DOG_LICKING_DIARY_URL, null, headers);
                JSONObject js = JSONObject.parseObject(result);
                String content = JSONObject.parseObject(js.getString("data")).getString("content");
                DogText dogTextContent = dogTextRepository.findByContent(content);
                if (null == dogTextContent) {
                    DogText dogText = DogText.builder()
                            .content(content)
                            .build();
                    dogTextRepository.save(dogText);
                }
//                Thread.sleep(1000);
            } catch (Exception e) {
                if (((TooManyRequests) e).getStatusText().equals("Too Many Requests")) {
                    Thread.sleep(5000);
                } else {
                    break;
                }
            }
        }

/*        try {
            SSLUtil.turnOffSslChecking();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }*/
/*        for (int i = 0; i < 500; i++) {
            String result = HttpUtil.get("http://www.tiangouriji.cc/api/", null);
//            String content = result.substring(result.indexOf("日晴") + 3);
            JSONObject jo = JSONObject.parseObject(result);
            String content = jo.getString("content");
            DogText dogTextContent = dogTextRepository.findByContent(content);
            if (null == dogTextContent) {
                DogText dogText = DogText.builder()
                        .content(content)
                        .build();
                dogTextRepository.save(dogText);
            }
                Thread.sleep(500);
        }*/

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
        if (!StringUtils.isEmpty(languageType)) {
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

    /**
     * 添加留言
     *
     * @param msg
     * @return
     */
    @Transactional
    public void addMsgBoard(String msg, HttpServletRequest request) {
        TextBoard textBoard = TextBoard.builder()
                .ip(CommonUtils.getIpAddr(request))
                .content(msg).build();
        textBoardRepository.save(textBoard);
    }

    /**
     * 查询留言列表
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Object findMsgBoard(int pageNo, int pageSize) {
        return textBoardRepository.findAllByPage(pageNo * pageSize, pageSize);
    }
}
