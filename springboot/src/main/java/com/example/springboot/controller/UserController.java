package com.example.springboot.controller;


import com.example.springboot.pojo.User;
import com.example.springboot.service.UserService;
import com.example.springboot.utils.redis.RedisUtil;
import com.example.springboot.utils.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 成大事
 * @since 2022-05-25 19:05:52
 */
@Api(tags = "测试")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserController {
    private final UserService userService;

    private final RedisUtil redisUtil;

    @GetMapping("/hello")
    @ApiOperation("hello")
    public R test(){
        User user = userService.getById("1");

        redisUtil.set("v1","1");
        return R.ok(user);
    }

}

