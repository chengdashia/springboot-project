package com.example.springboot.controller;


import com.example.springboot.common.result.R;
import com.example.springboot.pojo.User;
import com.example.springboot.service.UserService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 成大事
 * @since 2022-06-04 12:36:15
 */
@Api(tags = "用户")
@Slf4j
@Validated
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserController {


    private final UserService userService;

    @ApiOperation(value = "测试",notes = "多个参数")
    @PostMapping("/queryUser")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "账号", dataTypeClass = Date.class, paramType = "query"),
            @ApiImplicitParam(name = "pwd", value = "密码", dataTypeClass = String.class, paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(code = 200,message = "登录成功"),
            @ApiResponse(code = 500,message = "验证码错误"),
    })
    public R queryUser(
            @RequestParam(value = "id",required = false) String id,
            @RequestParam("pwd") String pwd
    ){
        log.info("id: {}",id);
        log.info("pwd: {}",pwd);
        List<User> list = userService.list();
        return R.ok(list);
    }

    @ApiOperation(value = "测试2",notes = "多个参数")
    @PostMapping("/queryUser2")
    @ApiResponses({
            @ApiResponse(code = 200,message = "登录成功"),
            @ApiResponse(code = 500,message = "验证码错误"),
    })
    @ApiOperationSupport(ignoreParameters = {"user.userId,user.pwd"})
    public R queryUser2(@RequestBody User user){
        log.info("id: {}",user);
        List<User> list = userService.list();
        return R.ok(list);
    }



}

