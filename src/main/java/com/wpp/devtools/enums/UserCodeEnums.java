package com.wpp.devtools.enums;

import lombok.Getter;

/**
 * @program: volvo
 * @description: 异常状态码
 * @author: wpp
 * @create: 2020-06-01
 **/
@Getter
public enum UserCodeEnums {

    USER_PSW_ERROR(-10001, "账号或密码有误"),
    EMAIL_EXIST(-10002, "邮箱已存在"),
    EMAIL_NOT_EXIST(-10003, "邮箱不存在"),
    DAY_MAX_SEND_EMAIL_SEND_COUNT(-10004, "超出24小时最大发送数量"),
    MINUTE_MAX_SEND_EMAIL_SEND_COUNT(-10005, "超出每分钟最大发送数量"),
    VERIFICATION_CODE_ERROR(-10006, "验证码错误"),
    VERIFICATION_CODE_INVALID(-10007, "验证码过期"),

    ;

    private Integer code;

    private String msg;

    UserCodeEnums(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
