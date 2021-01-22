package com.wpp.devtools.domain.bo;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @program: devtools-server
 * @description:
 * @author: wpp
 * @create: 2021-01-22
 **/
@Data
public class SaveUserInfoBo {

    private String id;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("性别： 0 未知  1 男  2 女")
    private Integer gender;

    @ApiModelProperty("头像")
    private String headImg;


}
