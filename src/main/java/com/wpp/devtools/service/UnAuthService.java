package com.wpp.devtools.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wpp.devtools.config.BaiduConfig;
import com.wpp.devtools.config.CommonConfig;
import com.wpp.devtools.config.RedisKeyConfig;
import com.wpp.devtools.config.UrlConfig;
import com.wpp.devtools.domain.bo.ForgetPasswordBo;
import com.wpp.devtools.domain.bo.LoginBo;
import com.wpp.devtools.domain.bo.RegisterBo;
import com.wpp.devtools.domain.entity.*;
import com.wpp.devtools.domain.enums.TypeEnum;
import com.wpp.devtools.domain.vo.UserVo;
import com.wpp.devtools.enums.ExceptionCodeEnums;
import com.wpp.devtools.enums.UserCodeEnums;
import com.wpp.devtools.exception.CustomException;
import com.wpp.devtools.repository.DogTextRepository;
import com.wpp.devtools.repository.EveryDayTextRepository;
import com.wpp.devtools.repository.TextBoardPraiseRepository;
import com.wpp.devtools.repository.TextBoardRepository;
import com.wpp.devtools.repository.UserRepository;
import com.wpp.devtools.repository.VerificationCodeRepository;
import com.wpp.devtools.util.CommonUtils;
import com.wpp.devtools.util.EmailUtil;
import com.wpp.devtools.util.HttpUtil;
import com.wpp.devtools.util.JWTUtil;
import com.wpp.devtools.util.MD5Util;
import com.wpp.devtools.util.RedistUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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
    private DogTextRepository dogTextRepository;

    @Autowired
    private TextBoardRepository textBoardRepository;

    @Autowired
    private EveryDayTextRepository everyDayTextRepository;

    @Autowired
    private TextBoardPraiseRepository textBoardPraiseRepository;

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTUtil jwtUtil;

    @Value("${HEFENG.KEY}")
    private String hefengKey;

    /**
     * 舔狗日记
     *
     * @return
     */
    public String getDoglickingDiary() {
        return dogTextRepository.findContentByRandom();

    }

    /**
     * 每日一文添加
     */
    public void addEveryDayText() {

        for (int i = 0; i < 10000; i++) {
            String result;
            try {
                URL url = new URL("https://meiriyiwen.com/");
                System.out.println(url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("GET");
                conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.6 (KHTML, like Gecko) Chrome/20.0.1096.1 Safari/536.6");
                conn.setRequestProperty("content-type","text/html;charset=UTF-8");
                conn.setDoOutput(true);


                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                StringBuffer sb = new StringBuffer();
                String s = "";
                while ((s = rd.readLine()) != null) {
                    sb.append(s);
                }
                if (sb.length() == 0) {
                    sb.append("[]");
                }
                result = sb.toString();
                System.out.println(result);
                Document document = Jsoup.parse(result);
                //Elements head = document.getElementsByTag("head");
                Element element = document.getElementById("article_show");
                String title = element.getElementsByTag("h1").html();
                String author = element.getElementsByTag("span").html().replace("\n", "");
                String content = element.getElementsByClass("article_text").html();

                EveryDayText everyDayText = everyDayTextRepository.findByTitle(title);
                if(null == everyDayText) {
                    EveryDayText e = EveryDayText.builder()
                            .title(title)
                            .author(author)
                            .content(content)
                            .build();
                    everyDayTextRepository.save(e);
                }
                rd.close();
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 每日一文
     *
     * @return
     */
    public EveryDayText getEveryDayText() {
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
        headers.add("format", "json");
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();

        for (int i = 0; i < 5000; i++) {
            try {
                String result = HttpUtil
                        .get("https://v2.alapi.cn/api/dog", param, headers);
                JSONObject js = JSONObject.parseObject(result);
                String content = JSONObject.parseObject(js.getString("data")).getString("content");

                DogText e = dogTextRepository.findByContent(content);
                if (null == e) {
                    DogText d = DogText.builder()
                            .content(content)
                            .build();
                    dogTextRepository.save(d);
                }
                Thread.sleep(1000);
            } catch (Exception e) {
                Thread.sleep(10000);
            }
        }
    }

    /**
     * 图片转文字
     *
     * @return
     */
    public Object imgToText(MultipartFile file) {

        if (file.isEmpty()) {
            throw new CustomException(ExceptionCodeEnums.PARAM_NULL);
        }

        String accessToken = getBaiduAccessToken();

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        String imgStr = CommonUtils.toBaseImg64(file);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("image", imgStr);
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
     * @param msgId
     * @param userId
     * @return
     */
    @Transactional
    public void addMsgBoard(String msg, String msgId, String userId) {
        if (StringUtils.isEmpty(msgId)) {
            msgId = "0";
        }
        TextBoard textBoard = TextBoard.builder()
                .userId(userId)
                .parentId(msgId)
                .content(msg).build();
        textBoardRepository.save(textBoard);
    }

    /**
     * 查询留言列表
     *
     * @param pageNo
     * @param pageSize
     * @param userId
     * @return
     */
    public Object findMsgBoard(int pageNo, int pageSize, String userId) {

        //查询留言
        List<Map<String, Object>> list = textBoardRepository
                .findAllByPage(pageNo * pageSize, pageSize, userId);
        list = CommonUtils.toCamelCase(list);
        List<String> ids = list.stream().map(e -> e.get("id").toString())
                .collect(Collectors.toList());
        //查询所有回复
        List<Map<String, Object>> replyList = textBoardRepository.findAllByParentIds(ids, userId);

        //分组
        Map<String, List<Map<String, Object>>> map = replyList.stream()
                .collect(Collectors.groupingBy(e -> e.get("parentId").toString()));

        List<Map<String, Object>> finalList = list;
        map.forEach((k, v) -> {
            finalList.forEach(e -> {
                if (e.get("id").toString().equals(k)) {
                    e.put("reply", v);
                }
            });
        });
        return finalList;
    }

    /**
     * 留言点赞
     *
     * @param msgId
     * @param userId
     */
    @Transactional
    public void msgBoardPraise(String msgId, String userId) {

        //查询是否已经点赞
        int count = textBoardPraiseRepository.findParaiseRecordCount(userId, msgId);
        if (count > 0) {
            throw new CustomException(ExceptionCodeEnums.TEXT_BOARD_PRAISE_EXISTS);
        }
        //保存点赞记录
        TextBoardPraise textBoardPraise = TextBoardPraise.builder()
                .textBoardId(msgId)
                .userId(userId)
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
                if (StringUtils.isEmpty(e)) {
                    return;
                }
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
     * 跨域接口
     *
     * @param url
     * @return
     */
    public Object crossDomain(String url) {
        String result = HttpUtil.get(url, null);
        return JSONObject.parseObject(result);
    }


    /**
     * 验证邮箱是否存在
     *
     * @param email
     */
    public boolean emailIsExist(String email) {
        User u = userRepository.findByEmail(email);
        if (null != u) {
            return true;
        }
        return false;
    }

    /**
     * 发送验证码
     *
     * @param email
     * @return
     */
    @Transactional
    public void sendCode(String email) {

        //限制24小时10次
        String dayCountString = redistUtil.getString("DAY" + email);
        int dayCount = null == dayCountString ? 0 : Integer.parseInt(dayCountString);
        if (dayCount >= 10) {
            throw new CustomException(UserCodeEnums.DAY_MAX_SEND_EMAIL_SEND_COUNT);
        }
        if (dayCount == 0) {
            redistUtil.incr("DAY" + email, 3600 * 24);
        } else {
            redistUtil.incr("DAY" + email);
        }

        //每分钟三次
        String minuteCountString = redistUtil.getString("MINUTE" + email);
        int minuteCount = null == minuteCountString ? 0 : Integer.parseInt(minuteCountString);
        if (minuteCount > 2) {
            throw new CustomException(UserCodeEnums.MINUTE_MAX_SEND_EMAIL_SEND_COUNT);
        } else {
            redistUtil.incr("MINUTE" + email, 58);
        }

        //开始发送
        //过期时间5分钟
        String code = CommonUtils.getRandomCodeByCount(4);
        VerificationCode vc = VerificationCode.builder().code(code).email(email)
                .deadline(new Timestamp(new Date().getTime() + 5 * 60 * 1000)).build();
        verificationCodeRepository.save(vc);

        //发送邮件
        emailUtil.sendMail(email, "小破站 | 验证码", emailUtil.sendCodeHtml(email, code));
    }

    /**
     * 注册
     *
     * @param r
     */
    @Transactional
    public void register(RegisterBo r) {

        if (emailIsExist(r.getEmail())) {
            throw new CustomException(UserCodeEnums.EMAIL_EXIST);
        }

        //验证邮件验证码
        validEmailAndCode(r.getEmail(), r.getCode().toUpperCase());

        //注册
        User user = User.builder().email(r.getEmail())
                .password(MD5Util.md5Encrypt32Upper(r.getPassword())).build();
        userRepository.save(user);
    }

    /**
     * 登录
     *
     * @param l
     * @return
     */
    public Object login(LoginBo l) {
        User user = userRepository.findByEmail(l.getEmail());

        if (null == user) {
            throw new CustomException(UserCodeEnums.USER_PSW_ERROR);
        }
        if (!emailIsExist(user.getEmail())) {
            throw new CustomException(UserCodeEnums.USER_PSW_ERROR);
        }
        if (!user.getPassword().equals(MD5Util.md5Encrypt32Upper(l.getPassword()))) {
            throw new CustomException(UserCodeEnums.USER_PSW_ERROR);
        }

        UserVo u = JSONObject.parseObject(JSON.toJSONString(user), UserVo.class);
        u.setToken(jwtUtil.createJWT(user.getId()));
        return u;
    }

    /**
     * 忘记密码
     *
     * @param f
     * @return
     */
    @Transactional
    public void forgetPassword(ForgetPasswordBo f) {

        //验证邮件验证码
        validEmailAndCode(f.getEmail(), f.getCode().toUpperCase());

        User user = userRepository.findByEmail(f.getEmail());
        user.setPassword(MD5Util.md5Encrypt32Upper(f.getPassword()));
        userRepository.save(user);
    }

    /**
     * 验证邮件验证码
     *
     * @param email
     * @param code
     */
    @Transactional
    public void validEmailAndCode(String email, String code) {
        VerificationCode vc = verificationCodeRepository
                .findByEmailAndCodeAndUsed(email, code, false);
        if (null == vc) {
            throw new CustomException(UserCodeEnums.VERIFICATION_CODE_ERROR);
        }
        if (vc.getDeadline().before(new Date())) {
            throw new CustomException(UserCodeEnums.VERIFICATION_CODE_INVALID);
        }
        //验证码被使用
        vc.setUsed(true);
        verificationCodeRepository.save(vc);
    }

    /**
     * 查询实况天气
     *
     * @param weatherId
     * @return
     */
    public Object findWeatherNow(String weatherId) {
        String result = HttpUtil
                .get(MessageFormat.format(UrlConfig.HEFENG_WEATHER_NOW_URL, weatherId, hefengKey),
                        null, null);
        return JSONObject.parseObject(result);
    }


}
