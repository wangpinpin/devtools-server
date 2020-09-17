package com.wpp.devtools.controller;

import com.wpp.devtools.domain.bo.ForgetPasswordBo;
import com.wpp.devtools.domain.pojo.Result;
import com.wpp.devtools.domain.vo.ResultVo;
import com.wpp.devtools.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/")
@Api(tags = {"用户接口服务"})
public class UserController {


    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserService userService;

    @ApiOperation("查询活动列表")
    @PostMapping("findActivityList")
    public Result findActivityList() {
        return ResultVo.success(userService.findActivityList());
    }
}
