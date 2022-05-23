package com.example.springbootjwt.controller;


import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springbootjwt.pojo.Jwtuser;
import com.example.springbootjwt.service.JwtuserService;
import com.example.springbootjwt.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author chengdashi
 * @since 2021-11-26
 */
@RestController
@RequestMapping("/jwtuser")
@Slf4j
public class JwtuserController {

    @Autowired
    private JwtuserService jwtuserService;

    private JWTUtils jwtUtils;


    @GetMapping("/login")
    public Map<String, String> login(String name, String password) {
//        System.out.println("username:"+name+" password:"+password);
//        QueryWrapper<Jwtuser> wrapper = new QueryWrapper<>();
//        wrapper.eq("usernam",name)
//                        .eq("password",password);
//        Jwtuser juser = jwtuserService.getOne(wrapper);
//        if(juser != null){
//            Map<String,String> map = new HashMap<>();
//            map.put("name", name);
//            map.put("password",password);
//            String token = jwtUtils.getToken(map);
//            return token;
//        }
//        return "false";
//    }
        log.info("name:" + name);
        log.info("password:" + password);
        Map<String, String> map = new HashMap<>();
        try {
            QueryWrapper<Jwtuser> wrapper = new QueryWrapper<>();
            wrapper.eq("name", "chengguo")
                    .eq("password", "chengdashi");
            Jwtuser one = jwtuserService.getOne(wrapper);
            Map<String, String> payload = new HashMap<>();
            payload.put("name", name);
            payload.put("password", password);
            //生成Jwt的令牌
            String token = JWTUtils.getToken(payload);
            System.out.println(one);
            map.put("status", "true");
            map.put("msg", "认证成功");
            map.put("token",token);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", "false");
            map.put("msg", "认证失败");
        }
        return map;
    }

    @GetMapping("/test")
    public Map<String, String> login(HttpServletRequest request) {
        String token = request.getHeader("token");
        DecodedJWT verify = JWTUtils.verify(token);
        Claim name = verify.getClaim("name");
        Claim password = verify.getClaim("password");
        System.out.println("name"+name);
        System.out.println("password"+password);
        Map<String, String> map = new HashMap<>();
        map.put("status", "true");
        map.put("msg", "认证成功");
        return map;
    }

}