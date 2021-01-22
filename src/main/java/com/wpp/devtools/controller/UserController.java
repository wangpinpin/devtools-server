package com.wpp.devtools.controller;

import com.wpp.devtools.config.JWTConfig;
import com.wpp.devtools.domain.bo.AddSubscribeBo;
import com.wpp.devtools.domain.bo.NotebookBo;
import com.wpp.devtools.domain.bo.SaveUserInfoBo;
import com.wpp.devtools.domain.pojo.Result;
import com.wpp.devtools.domain.vo.ResultVo;
import com.wpp.devtools.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/")
@Api(tags = {"用户接口服务"})
public class UserController {


    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserService userService;

    @ApiOperation("保存用户信息")
    @PostMapping("saveUserInfo")
    public Result saveUserInfo(@RequestBody SaveUserInfoBo u) {
        String userId = request.getAttribute(JWTConfig.JWT_USER_ID_KEY).toString();
        userService.saveUserInfo(u, userId);
        return ResultVo.success();
    }

    @ApiOperation("获取用户信息")
    @PostMapping("findUserInfo")
    public Result findUserInfo() {
        String userId = request.getAttribute(JWTConfig.JWT_USER_ID_KEY).toString();
        return ResultVo.success(userService.findUserInfo(userId));
    }

    @ApiOperation("查询活动列表")
    @GetMapping("findActivityList")
    public Result findActivityList() {
        return ResultVo.success(userService.findActivityList());
    }

    @ApiOperation("添加订阅")
    @PostMapping("addSubscribe")
    public Result addSubscribe(@Valid @RequestBody AddSubscribeBo a) {
        String userId = request.getAttribute(JWTConfig.JWT_USER_ID_KEY).toString();
        userService.addSubscribe(a, userId);
        return ResultVo.success();
    }

    @ApiOperation("查询订阅")
    @GetMapping("findSubscribe")
    public Result findSubscribe() {
        String userId = request.getAttribute(JWTConfig.JWT_USER_ID_KEY).toString();
        return ResultVo.success(userService.findSubscribe(userId));
    }

    @ApiOperation("发送订阅")
    @PostMapping("sendSubscribe")
    public Result sendSubscribe() {
        userService.sendSubscribe();
        return ResultVo.success();
    }

    @ApiOperation("启用禁用订阅")
    @PostMapping("enabledSubscribe")
    public Result enabledSubscribe(@RequestParam String id, @RequestParam Boolean enabled) {
        userService.enabledSubscribe(id, enabled);
        return ResultVo.success();
    }

    @ApiOperation("删除订阅")
    @PostMapping("delSubscribe")
    public Result delSubscribe(@RequestParam String id) {
        userService.delSubscribe(id);
        return ResultVo.success();
    }

    @ApiOperation("保存笔记")
    @PostMapping("saveNotebook")
    public Result saveNotebook(@RequestBody NotebookBo n) {
        String userId = request.getAttribute(JWTConfig.JWT_USER_ID_KEY).toString();
        userService.saveNotebook(userId, n);
        return ResultVo.success();
    }

    @ApiOperation("删除笔记")
    @PostMapping("delNotebook")
    public Result saveNotebook(@RequestParam String id) {
        userService.delNotebook(id);
        return ResultVo.success();
    }

    @ApiOperation("查询笔记列表")
    @GetMapping("findNotebookList")
    public Result findNotebookList() {
        String userId = request.getAttribute(JWTConfig.JWT_USER_ID_KEY).toString();
        return ResultVo.success(userService.findNotebookList(userId));
    }

    @ApiOperation("修改笔记排序")
    @PostMapping("updateNotebookIndex")
    public Result updateNotebookIndex(@RequestParam String id, @RequestParam long oldIndex, @RequestParam long newIndex) {
        String userId = request.getAttribute(JWTConfig.JWT_USER_ID_KEY).toString();
        userService.updateNotebookIndex(id, userId, oldIndex, newIndex);
        return ResultVo.success();
    }
}
