package com.wpp.devtools.domain.vo;

import com.wpp.devtools.domain.pojo.Result;
import com.wpp.devtools.enums.ExceptionCodeEnums;

/**
 * @program: language
 * @description: 统一返回
 * @author: wpp
 * @create: 2020-06-01
 **/
public class ResultVo {


    public static Result success(Object object) {
        return Result.builder()
                .code(ExceptionCodeEnums.OK.getCode())
                .msg(ExceptionCodeEnums.OK.getMsg())
                .data(object).build();
    }

    public static Result success() {
        return success("");
    }

    public static Result error(Integer code, String msg) {
        return Result.builder()
                .code(code)
                .msg(msg)
                .build();
    }

    public static Result error() {
        return Result.builder()
                .code(ExceptionCodeEnums.ERROR.getCode())
                .msg(ExceptionCodeEnums.ERROR.getMsg())
                .build();
    }

    public static Result error(Object object) {
        return Result.builder()
                .code(ExceptionCodeEnums.ERROR.getCode())
                .msg(ExceptionCodeEnums.ERROR.getMsg())
                .data(object)
                .build();
    }
}
