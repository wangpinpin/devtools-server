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
public class PlayGameBo {

    @NotBlank(message = "游戏编号不能为空")
    @ApiModelProperty("游戏编号")
    private String gameCode;

    @NotBlank(message = "游戏等级不能为空")
    @ApiModelProperty("游戏等级")
    private String gameLevel;

    @NotBlank(message = "名字不能为空")
    @ApiModelProperty("名字")
    private String name;

}
