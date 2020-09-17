package com.wpp.devtools.service;

import com.alibaba.fastjson.JSONObject;
import com.wpp.devtools.config.WXKeyconfig;
import com.wpp.devtools.config.WXconfig;
import com.wpp.devtools.enums.ExceptionCodeEnums;
import com.wpp.devtools.exception.CustomException;
import com.wpp.devtools.repository.UserRepository;
import com.wpp.devtools.util.HttpUtil;
import com.wpp.devtools.util.RedistUtil;
import com.wpp.devtools.util.WXMessageUtil;
import com.wpp.devtools.util.XmlUtil;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WXService {

    @Autowired
    private RedistUtil redistUtil;

    @Autowired
    private WXKeyconfig wxKeyconfig;

    @Autowired
    private UserRepository userRepository;

    /**
     * 获取access_token
     *
     * @return
     */
    public String getAccessToken() {

        String accessToken = redistUtil.getString(WXconfig.WX_ACCESS_TOKEN_KEY);
        //如果缓存不存在，去api获取access_token
        if (StringUtils.isBlank(accessToken)) {
            String result = HttpUtil.get(MessageFormat
                    .format(WXconfig.WX_GET_ACCESS_TOKEN_URL, wxKeyconfig.getAppId(),
                            wxKeyconfig.getAppSecret()), null);
            isSucessWXApiByResult(result);
            JSONObject jo = JSONObject.parseObject(result);
            accessToken = jo.getString("access_token");
            //微信失效2小时, 这里保存1小时
            redistUtil.setString(WXconfig.WX_ACCESS_TOKEN_KEY, accessToken, 3600L);
            log.info("获取微信获取access_token: " + accessToken);
        }

        return accessToken;
    }

    public void wxUrlTokenValid(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String message = "success";
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        try {
            //把微信返回的xml信息转义成map
            Map<String, String> map;
            map = XmlUtil.xmlToMap(request);
            String fromUserName = map.get("FromUserName");//消息来源用户标识
            String toUserName = map.get("ToUserName");//消息目的用户标识
            String msgType = map.get("MsgType");//消息类型
            String content = map.get("Content");//消息内容

            String eventType = map.get("Event");
            if (WXMessageUtil.MSGTYPE_EVENT.equals(msgType)) {//如果为事件类型
                if (WXMessageUtil.MESSAGE_SUBSCIBE.equals(eventType)) {
                    //关注事件
                    message = WXMessageUtil.subscribeForText(toUserName, fromUserName);

                    //保存用户信息
/*                    String result = HttpUtil.get(MessageFormat
                                    .format(WXconfig.WX_GET_USER_INFO, getAccessToken(), fromUserName),
                            null);
                    log.info("result: "+ result);
                    JSONObject jo = JSON.parseObject(result);
                    jo.put("subscribe_time",
                            Long.parseLong(jo.get("subscribe_time").toString()) * 1000);
                    User olduser = userRepository.findByOpenid(fromUserName);
                    User user = JSON.parseObject(String.valueOf(jo), User.class);
                    if (null == olduser) {
                        userRepository.save(user);
                    } else {
                        olduser.setSubscribeTime(new Timestamp(new Date().getTime()));
                        olduser.setSubscribe(1L);
                        userRepository.save(olduser);
                    }*/


                } else if (WXMessageUtil.MESSAGE_UNSUBSCIBE.equals(eventType)) {
                    //取消订阅事件
                    message = "";
/*                    User user = userRepository.findByOpenid(fromUserName);
                    if (null != user) {
                        user.setSubscribe(0L);
                        userRepository.save(user);
                    }*/
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.println(message);
            if (out != null) {
                out.close();
            }
        }
    }



    public void isSucessWXApiByResult(String result) {
        JSONObject jo = JSONObject.parseObject(result);
        if(null != jo.get("errcode") && Integer.parseInt(jo.get("errcode").toString()) != 0) {
            throw new CustomException(ExceptionCodeEnums.WX_HTTP_REQUEST_ERROR, jo.get("errmsg").toString());
        }

    }
}
