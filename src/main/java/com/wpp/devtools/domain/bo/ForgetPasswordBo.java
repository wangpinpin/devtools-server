package com.wpp.devtools.domain.bo;

import com.wpp.devtools.config.CommonConfig;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;

/**
 * @program: volvo-sever
 * @description:
 * @author: wpp
 * @create: 2020-09-17
 **/
@Data
public class ForgetPasswordBo {

    @ApiModelProperty("验证码")
    @NotBlank(message = "验证码不能为空")
    private String code;

    @NotBlank(message = "邮箱不能为空")
    @ApiModelProperty("邮箱")
    private String email;

    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = CommonConfig.PASSWORD_VALIDATE, message = "密码必须同时包含字母数字的6-18位组合")
    @ApiModelProperty("密码")
    private String password;

}
