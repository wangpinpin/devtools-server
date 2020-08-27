package com.wpp.devtools.controller;

import com.wpp.devtools.domain.pojo.Result;
import com.wpp.devtools.domain.vo.ResultVo;
import com.wpp.devtools.service.DevtoolsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/devTools/")
@Api(tags = {"工具接口服务"})
public class DevtoolsController {


    @Autowired
    private HttpServletRequest request;

    @Autowired
    private DevtoolsService devtoolsService;

    @ApiOperation("图片转base64")
    @PostMapping("imgToBase64")
    public Result imgToBase64(@RequestParam MultipartFile file) {
        return ResultVo.success(devtoolsService.imgToBase64(file));
    }

    @ApiOperation("图片加水印")
    @PostMapping("imgAddWatermark")
    public Result imgAddWatermark(@RequestParam MultipartFile file) {
        return ResultVo.success(devtoolsService.imgAddWatermark(file));
    }

    @ApiOperation("图片去水印")
    @PostMapping("imgClearWatermark")
    public Result imgClearWatermark(@RequestParam MultipartFile file, @RequestParam String backgroundColor, @RequestParam String watermarkColor, @RequestParam int precision) {
        return ResultVo.success(devtoolsService.imgClearWatermark(file, backgroundColor, watermarkColor, precision));
    }

}
