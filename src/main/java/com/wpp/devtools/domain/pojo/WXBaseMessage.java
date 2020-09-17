package com.wpp.devtools.domain.pojo;

import lombok.Data;

/**
 * @program: devtools-server
 * @description: 消息体基础类
 * @author: wpp
 * @create: 2020-08-17
 **/
@Data
public class WXBaseMessage {
    private String ToUserName;
    private String FromUserName;
    private long CreateTime;
    private String MsgType;
}
