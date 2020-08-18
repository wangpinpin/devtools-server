package com.wpp.devtools.exception;


import com.wpp.devtools.domain.pojo.Result;
import com.wpp.devtools.enums.ExceptionCodeEnums;
import java.util.HashSet;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: volvo
 * @description: 统一异常处理
 * @author: wpp
 * @create: 2020-06-01
 **/
@Slf4j
@ControllerAdvice
public class ExceptionAdvice {


    /**
     * 全局异常捕捉
     *
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Result errorHandler(Exception e) {
        log.error(e.getMessage());
        e.printStackTrace();
        return Result.builder()
                .code(ExceptionCodeEnums.ERROR.getCode())
                .msg(ExceptionCodeEnums.ERROR.getMsg())
                .data("")
                .build();
    }


    /**
     * 自定义异常捕获
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = CustomException.class)
    public Result customException(CustomException e) {
        log.info(e.getMessage());
        return Result.builder()
                .code(e.getCode())
                .msg(e.getMessage())
                .data("")
                .build();
    }


    /**
     * 入参校验异常捕获
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public Result customException(MissingServletRequestParameterException e) {
        return Result.builder()
                .code(ExceptionCodeEnums.PARAM_ERROR.getCode())
                .msg(e.getParameterName() + "不能为空")
                .data("")
                .build();
    }

    /**
     * valid入参异常处理
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result customException(MethodArgumentNotValidException e) {
        return customResult(e.getBindingResult());

    }


    /**
     * valid入参异常处理
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = BindException.class)
    public Result customException(BindException e) {
        return customResult(e.getBindingResult());
    }


    /**
     * List valid入参异常处理
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = ConstraintViolationException.class)
    public Result customException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        Set<String> set = new HashSet<>();
        for (ConstraintViolation violation : violations) {
            set.add(violation.getMessage());
        }
        return Result.builder()
                .code(ExceptionCodeEnums.PARAM_ERROR.getCode())
                .msg(StringUtils.join(set, ","))
                .data("")
                .build();
    }

    /**
     * 验证参数统一返回
     *
     * @param bindingResult
     * @return
     */
    public Result customResult(BindingResult bindingResult) {
        Set<String> set = new HashSet<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            set.add(fieldError.getDefaultMessage());
        }
        return Result.builder()
                .code(ExceptionCodeEnums.PARAM_ERROR.getCode())
                .msg(StringUtils.join(set, ","))
                .data("")
                .build();
    }

}
