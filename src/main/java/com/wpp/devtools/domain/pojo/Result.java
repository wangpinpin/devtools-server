package com.wpp.devtools.domain.pojo;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

/**
 * @program: volvo
 * @description: 统一返回封装
 * @author: wpp
 * @create: 2020-06-01
 **/
@Data
@Builder
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 3068837394742385883L;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 提示信息
     */
    private String msg;

    /**
     * 内容
     */
    private T data;
}
