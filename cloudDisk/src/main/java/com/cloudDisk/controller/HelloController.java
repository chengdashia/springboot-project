package com.cloudDisk.controller;



import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;

import io.swagger.annotations.Api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;


/**
 * @author 成大事
 * @since 2022/4/13 21:04
 */
@Api(tags = "HelloController")
@RestController
@RequestMapping("/hello")
public class HelloController {

    // 测试登录
    @RequestMapping(value = "login",method = RequestMethod.GET)
    public SaResult login(HttpServletResponse response) {


        return SaResult.ok("登录成功");
    }


    // 查询登录状态
    @RequestMapping(value = "isLogin",method = RequestMethod.GET)
    public SaResult isLogin() {
//        int init = (int) StpUtil.getExtra("init");
//        System.out.println(init);

        Object map = StpUtil.getExtra("map");
        System.out.println(map);


        return SaResult.ok("是否登录：" + StpUtil.isLogin());
    }

    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public void testGetValue(){
        Object name = StpUtil.getExtra("name");
        Object loginId = StpUtil.getLoginId();
        System.out.println(name);
        System.out.println(loginId);
    }

    // 测试注销
    @RequestMapping(value = "logout",method = RequestMethod.GET)
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok();
    }





}
