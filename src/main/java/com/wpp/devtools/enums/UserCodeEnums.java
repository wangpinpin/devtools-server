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
    USER_EXAMINE(-10002, "账号审核中"),
    USER_REFUSE(-10003, "账号审核被拒绝"),
    USER_NULL(-10004, "账号不存在"),
    DEALER_CODE_NULL(-10005, "经销商CODE不存在"),
    DEALER_CODE_EXISTS(-10006, "经销商CODE存在"),
    POSITION_NULL(-10007, "职位不存在"),
    ROLE_NULL(-10008, "角色不存在"),
    SMALL_AREA_MANAGER_NULL(-10009, "小区经理不存在"),
    DEALER_AREA_RUNNING(-10010, "改经销商正在小区管辖中, 请联系管理员"),
    USER_EXISTS(-10011, "用户已存在"),
    USER_ERROR(-10012, "用户异常"),
    PASSWORD_ERROR(-10013, "密码错误"),
    ;

    private Integer code;

    private String msg;

    UserCodeEnums(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
