package com.wpp.devtools.util;

import com.wpp.devtools.domain.pojo.WXTextMessage;
import java.util.Date;

/**
 * @program: devtools-server
 * @description:
 * @author: wpp
 * @create: 2020-08-17
 **/
public class WXMessageUtil {

    public static final String MSGTYPE_EVENT = "event";//消息类型--事件
    public static final String MESSAGE_SUBSCIBE = "subscribe";//消息事件类型--订阅事件
    public static final String MESSAGE_UNSUBSCIBE = "unsubscribe";//消息事件类型--取消订阅事件
    public static final String MESSAGE_TEXT = "text";//消息类型--文本消息

    /*
     * 组装文本消息
     */
    public static String textMsg(String toUserName, String fromUserName, String content) {
        WXTextMessage text = new WXTextMessage();
        text.setFromUserName(toUserName);
        text.setToUserName(fromUserName);
        text.setMsgType(MESSAGE_TEXT);
        text.setCreateTime(new Date().getTime());
        text.setContent(content);
        return XmlUtil.textMsgToxml(text);
    }

    /*
     * 响应订阅事件--回复文本消息
     */
    public static String subscribeForText(String toUserName, String fromUserName) {
        return textMsg(toUserName, fromUserName, "欢迎关注舔狗小胖, 让小胖每天给您带来不同快乐。");
    }

}
