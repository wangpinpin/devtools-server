package com.wpp.devtools.controller;

import com.wpp.devtools.config.JWTConfig;
import com.wpp.devtools.domain.bo.GameEndBo;
import com.wpp.devtools.domain.bo.PlayGameBo;
import com.wpp.devtools.domain.pojo.Result;
import com.wpp.devtools.domain.vo.ResultVo;
import com.wpp.devtools.service.GameService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/game/")
@Api(tags = {"游戏接口服务"})
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private HttpServletRequest request;


    @ApiOperation("游戏开始接口")
    @PostMapping("playGame")
    public Result playGame(@Validated @RequestBody PlayGameBo playGameBo) {
        String userId = request.getAttribute(JWTConfig.JWT_USER_ID_KEY).toString();
        gameService.playGame(playGameBo, userId);
        return ResultVo.success();
    }

    @ApiOperation("游戏结束接口")
    @PostMapping("gameEnd")
    public Result gameEnd() {
        String userId = request.getAttribute(JWTConfig.JWT_USER_ID_KEY).toString();
        gameService.gameEnd(userId);
        return ResultVo.success();
    }

    @ApiOperation("游戏排行榜查询")
    @PostMapping("gameRank/{gameCode}")
    public Result gameRank(@PathVariable String gameCode) {
        return ResultVo.success(gameService.gameRank(gameCode));
    }

}
