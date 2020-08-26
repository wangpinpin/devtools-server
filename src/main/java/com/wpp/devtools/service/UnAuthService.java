package com.wpp.devtools.service;

import com.alibaba.fastjson.JSONObject;
import com.wpp.devtools.config.BaiduConfig;
import com.wpp.devtools.config.CommonConfig;
import com.wpp.devtools.config.RedisKeyConfig;
import com.wpp.devtools.config.UrlConfig;
import com.wpp.devtools.domain.entity.DogText;
import com.wpp.devtools.domain.entity.EveryDayText;
import com.wpp.devtools.domain.entity.TextBoard;
import com.wpp.devtools.domain.entity.TextBoardPraise;
import com.wpp.devtools.domain.enums.TypeEnum;
import com.wpp.devtools.enums.ExceptionCodeEnums;
import com.wpp.devtools.exception.CustomException;
import com.wpp.devtools.repository.DogTextRepository;
import com.wpp.devtools.repository.EveryDayTextRepository;
import com.wpp.devtools.repository.TextBoardPraiseRepository;
import com.wpp.devtools.repository.TextBoardRepository;
import com.wpp.devtools.repository.TypeRepository;
import com.wpp.devtools.util.CommonUtils;
import com.wpp.devtools.util.HttpUtil;
import com.wpp.devtools.util.RedistUtil;
import java.text.MessageFormat;
import java.util.List;
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

    @Autowired
    private EveryDayTextRepository everyDayTextRepository;

    @Autowired
    private TextBoardPraiseRepository textBoardPraiseRepository;

    @Autowired
    private TypeRepository typeRepository;


    /**
     * 舔狗日记
     *
     * @param typeId
     * @return
     */
    public String getDoglickingDiary(String typeId) {
        return dogTextRepository.findContentByTypeIdAndRandom(typeId);

    }

    /**
     * 每日一文
     *
     * @return
     */
    public Object getEveryDayText() {
        return everyDayTextRepository.findOneTextByRandom();
    }

    /**
     * 同步舔狗日记
     *
     * @throws InterruptedException
     */
    public void getDoglickingDiaryListInsert() throws InterruptedException {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("token", CommonConfig.ALAPI_TOKEN);
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();

        for (int i = 0; i < 500; i++) {
            try {
                String result = HttpUtil
                        .get("https://v1.alapi.cn/api/hitokoto?format=text&type=a", param, headers);
//                JSONObject js = JSONObject.parseObject(result);
//                JSONObject textObject = JSONObject.parseObject(js.getString("data"));

                DogText e = dogTextRepository.findByContent(result);
                if (null == e) {
                    DogText d = DogText.builder()
                            .content(result)
                            .typeId("0760fbcd-e5b8-11ea-9d4b-00163e1e93a5")
                            .build();
                    dogTextRepository.save(d);
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
    public Object findMsgBoard(int pageNo, int pageSize, HttpServletRequest request) {
        String ip = CommonUtils.getIpAddr(request);
        return textBoardRepository.findAllByPage(pageNo * pageSize, pageSize, ip);
    }

    /**
     * 留言点赞
     *
     * @param msgId
     * @param request
     */
    @Transactional
    public void msgBoardPraise(String msgId, HttpServletRequest request) {
        String ip = CommonUtils.getIpAddr(request);

        //查询是否已经点赞
        int count = textBoardPraiseRepository.findParaiseRecordCount(ip, msgId);
        if (count > 0) {
            throw new CustomException(ExceptionCodeEnums.TEXT_BOARD_PRAISE_EXISTS);
        }
        //保存点赞记录
        TextBoardPraise textBoardPraise = TextBoardPraise.builder()
                .textBoardId(msgId)
                .ip(ip)
                .build();
        textBoardPraiseRepository.save(textBoardPraise);

        textBoardRepository.addPraiseCount(msgId);
    }

    /**
     * 添加舔狗日记
     *
     * @param texts
     * @return
     */
    public Object addGogText(List<String> texts) {
        final int[] count = {0};
        if (null != texts && texts.size() > 0) {
            texts.forEach(e -> {
                DogText dogTextContent = dogTextRepository.findByContent(e);
                if (null == dogTextContent) {
                    DogText dogText = DogText.builder()
                            .content(e)
                            .build();
                    dogTextRepository.save(dogText);
                    count[0]++;
                }
            });
        }
        return count[0];
    }


    /**
     * 类型查询
     *
     * @param t
     * @return
     */
    public Object findTypeList(TypeEnum t) {
        return typeRepository.findByTypeOrderBySort(t);
    }

    /**
     * 跨域接口
     * @param url
     * @return
     */
    public Object crossDomain(String url) {
        String result = HttpUtil.post(url, null);
        JSONObject js = JSONObject.parseObject(result);
        return js;
    }
}
