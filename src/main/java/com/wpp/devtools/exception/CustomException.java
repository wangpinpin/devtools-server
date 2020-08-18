package com.wpp.devtools.exception;


import com.wpp.devtools.enums.ExceptionCodeEnums;
import com.wpp.devtools.enums.UserCodeEnums;
import lombok.Data;

/**
 * @program: volvo
 * @description: 自定义异常
 * @author: wpp
 * @create: 2020-06-01
 **/
@Data
public class CustomException extends RuntimeException {

    /**
     * 错误状态码.
     */
    private Integer code;

    public CustomException(ExceptionCodeEnums exceptionCodeEnums) {
        super(exceptionCodeEnums.getMsg());
        this.code = exceptionCodeEnums.getCode();
    }

    public CustomException(ExceptionCodeEnums exceptionCodeEnums, String msg) {
        super(msg);
        this.code = exceptionCodeEnums.getCode();
    }

    public CustomException(UserCodeEnums userCodeEnums) {
        super(userCodeEnums.getMsg());
        this.code = userCodeEnums.getCode();
    }

    public CustomException(UserCodeEnums userCodeEnums, String msg) {
        super(msg);
        this.code = userCodeEnums.getCode();
    }
}
