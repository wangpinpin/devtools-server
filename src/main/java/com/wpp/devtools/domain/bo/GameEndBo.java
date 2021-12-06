package com.wpp.devtools.domain.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @program: devtools-server
 * @description:
 * @author: wpp
 * @create: 2021-12-06
 **/
@Data
public class GameEndBo {

    @NotBlank(message = "用户临时ID不能为空")
    @ApiModelProperty("用户临时ID")
    private String userUUID;

}
