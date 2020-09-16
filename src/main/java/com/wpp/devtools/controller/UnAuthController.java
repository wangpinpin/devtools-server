package com.wpp.devtools.controller;

import com.wpp.devtools.domain.annotation.AccessLimit;
import com.wpp.devtools.domain.enums.TypeEnum;
import com.wpp.devtools.domain.pojo.Result;
import com.wpp.devtools.domain.vo.ResultVo;
import com.wpp.devtools.service.UnAuthService;
import com.wpp.devtools.util.EmailUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/unAuth/")
@Api(tags = {"无权限接口服务"})
public class UnAuthController {


    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UnAuthService unAuthService;


    @ApiOperation("舔狗日记")
    @GetMapping("getDoglickingDiary")
    @AccessLimit(seconds = 10, maxCount = 5)
    public Result getDoglickingDiary(@RequestParam String typeId) {
        return ResultVo.success(unAuthService.getDoglickingDiary(typeId));
    }

    @ApiOperation("每日一文")
    @GetMapping("getEveryDayText")
    @AccessLimit(seconds = 10, maxCount = 3)
    public Result getEveryDayText() {
        return ResultVo.success(unAuthService.getEveryDayText());
    }

    @ApiOperation("图片转文字")
    @PostMapping("imgToText")
    @AccessLimit(seconds = 10, maxCount = 3)
    public Result imgToText(MultipartFile file, String languageType) {
        return ResultVo.success(unAuthService.imgToText(file, languageType));
    }

    @ApiOperation("同步舔狗日记")
    @PostMapping("getDoglickingDiaryListInsert")
    public Result getDoglickingDiaryListInsert() {
        try {
            unAuthService.getDoglickingDiaryListInsert();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ResultVo.success();
    }

    @PostMapping("addGogText")
    public Result addGogText(@RequestBody List<String> texts) {
        return ResultVo.success(unAuthService.addGogText(texts));
    }

    @ApiOperation("添加留言")
    @PostMapping("addMsgBoard")
    @AccessLimit(seconds = 5, maxCount = 1)
    public Result addMsgBoard(@RequestParam String msg, String msgId) {
        unAuthService.addMsgBoard(msg, msgId, request);
        return ResultVo.success();
    }

    @ApiOperation("查询留言列表")
    @GetMapping("findMsgBoard")
    public Result findMsgBoard(int pageNo, int pageSize) {
        return ResultVo.success(unAuthService.findMsgBoard(pageNo, pageSize, request));
    }

    @ApiOperation("留言点赞")
    @PostMapping("msgBoardPraise")
    public Result msgBoardPraise(@RequestParam String msgId) {
        unAuthService.msgBoardPraise(msgId, request);
        return ResultVo.success();
    }

    @ApiOperation("类型查询")
    @GetMapping("findTypeList")
    public Result findTypeList(@RequestParam TypeEnum t) {
        return ResultVo.success(unAuthService.findTypeList(t));
    }

    @ApiOperation("跨域接口")
    @GetMapping("crossDomain")
    public Result crossDomain(@RequestParam String url) {
        return ResultVo.success(unAuthService.crossDomain(url));
    }

    @ApiOperation("发送验证码")
    @PostMapping("sendCode")
    public Result sendCode(@RequestParam String email) {
        unAuthService.sendCode(email);
        return ResultVo.success();
    }


}
