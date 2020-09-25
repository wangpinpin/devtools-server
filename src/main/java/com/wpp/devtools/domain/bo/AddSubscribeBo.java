package com.wpp.devtools.domain.bo;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @program: volvo-sever
 * @description:
 * @author: wpp
 * @create: 2020-09-17
 **/
@Data
public class AddSubscribeBo {

    private String id;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("女神昵称")
    @NotBlank(message = "女神昵称不能为空")
    private String godNickName;

    @ApiModelProperty("邮箱")
    @NotBlank(message = "邮箱不能为空")
    private String email;

    @ApiModelProperty("发送时间 单位小时")
    @NotNull(message = "发送时间不能为空")
    private Integer hour;

    @ApiModelProperty("活动名称")
    @NotEmpty(message = "活动名称不能为空")
    private List<String> activityName;

    @ApiModelProperty("是否启用")
    private boolean enabled;


}
