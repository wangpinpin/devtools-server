package com.wpp.devtools.controller;

import com.wpp.devtools.domain.pojo.Result;
import com.wpp.devtools.domain.vo.ResultVo;
import com.wpp.devtools.service.UnAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/unAuth/")
@Api(tags = {"无权限接口服务"})
public class UnAuthController {

    @Autowired
    private UnAuthService unAuthService;


    @ApiOperation("舔狗日记")
    @GetMapping("getDoglickingDiary")
    public Result getDoglickingDiary(){
        return ResultVo.success(unAuthService.getDoglickingDiary());
    }

    @ApiOperation("图片转文字")
    @PostMapping("imgToText")
    public Result imgToText(MultipartFile file, String languageType){
        return ResultVo.success(unAuthService.imgToText(file, languageType));
    }
}
