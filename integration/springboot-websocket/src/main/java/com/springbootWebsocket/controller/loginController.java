package com.springbootWebsocket.controller;

import com.alibaba.fastjson.support.spring.annotation.ResponseJSONP;
import com.springbootWebsocket.utils.JWTUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 成大事
 * @date 2022/3/5 21:46
 */
@Controller
public class loginController {

    @ResponseJSONP
    @GetMapping("/login")
    public String getToken(@RequestParam("userId") String userId,
                           @RequestParam("pwd") String pwd){
        Map<String, String> map = new HashMap<>();
        map.put("userId",userId);
        map.put("pwd",pwd);
        String token = JWTUtils.getToken(map);
        return token;
    }
}
