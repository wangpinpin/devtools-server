package com.wpp.devtools.domain.bo;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @program: devtools-server
 * @description:
 * @author: wpp
 * @create: 2020-09-17
 **/
@Data
public class LoginBo {

    @NotBlank(message = "邮箱不能为空")
    @ApiModelProperty("邮箱")
    private String email;

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty("密码")
    private String password;

}
