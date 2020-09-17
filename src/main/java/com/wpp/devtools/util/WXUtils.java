package com.wpp.devtools.util;

import com.wpp.devtools.config.WXconfig;
import com.wpp.devtools.enums.ExceptionCodeEnums;
import com.wpp.devtools.exception.CustomException;
import java.security.MessageDigest;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;

/**
 * @program: devtools-server
 * @description:
 * @author: wpp
 * @create: 2020-08-17
 **/
public class WXUtils {

    //验证服务器地址
    public static String checkUrl(HttpServletRequest request) {
        //参数配置
        String signature = request.getParameter("signature");
        //时间戳
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        //echostr 字符
        String echostr = request.getParameter("echostr");
        //token 此token跟需跟微信公众号的token一致；
        String token = WXconfig.WX_URL_TOKEN;
        String str = "";
        try {
            //判断是否为空
            if (null != signature) {
                //声明一个存储数据字符数组
                String[] ArrTmp = {token, timestamp, nonce};
                Arrays.sort(ArrTmp);
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < ArrTmp.length; i++) {
                    sb.append(ArrTmp[i]);
                }
                //获取消息对象
                MessageDigest md = MessageDigest.getInstance("SHA-1");
                //声明一个字节流数组；
                byte[] bytes = md.digest(new String(sb).getBytes());
                //声明一个字符流
                StringBuffer buf = new StringBuffer();
                for (int i = 0; i < bytes.length; i++) {
                    if (((int) bytes[i] & 0xff) < 0x10) {
                        buf.append("0");
                    }
                    buf.append(Long.toString((int) bytes[i] & 0xff, 16));
                }
                if (signature.equals(buf.toString())) {
                    str = echostr;
                }
            }
        } catch (Exception e) {
           throw new CustomException(ExceptionCodeEnums.OTHER_ERROR);
        }
        //返回消息
        return str;
    }
}
