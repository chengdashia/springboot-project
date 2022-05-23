package com.example.demo1.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo1.common.GlobalResult;
import com.example.demo1.common.WeChatUtil;
import com.example.demo1.pojo.User;
import com.example.demo1.service.UserService;
import com.example.demo1.utils.JWTUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * 微信用户信息 前端控制器
 * </p>
 *
 * @author chengdashi
 * @since 2021-11-27
 */
@RestController
public class UserController {


    @Autowired
    private UserService userService;

    @GetMapping("/test")
    public Map<String,String> testGetUser(HttpServletRequest request){
        String token = request.getHeader("token");
        DecodedJWT verify = JWTUtils.verify(token);
        String name = String.valueOf(verify.getClaim("name"));
        Map<String, String> map = new HashMap<>();
        map.put("status", "true");
        map.put("msg", "认证成功");
        map.put("name",name);
        return map;
    }

    /**
     * 微信用户登录详情
     */
    @PostMapping("/wx/login")
    @ResponseBody
    public GlobalResult user_login(@RequestParam(value = "code", required = false) String code,
                                   @RequestParam(value = "rawData", required = false) String rawData,
                                   @RequestParam(value = "signature", required = false) String signature) {
        // 用户非敏感信息：rawData
        // 签名：signature
        JSONObject rawDataJson = JSON.parseObject(rawData);
        // 1.接收小程序发送的code
        // 2.开发者服务器 登录凭证校验接口 appi + appsecret + code
        JSONObject SessionKeyOpenId = WeChatUtil.getSessionKeyOrOpenId(code);
        System.out.println(code);
        System.out.println(SessionKeyOpenId);
        // 3.接收微信接口服务 获取返回的参数
        String openid = SessionKeyOpenId.getString("openid");

        String sessionKey = SessionKeyOpenId.getString("session_key");

        // 4.校验签名 小程序发送的签名signature与服务器端生成的签名signature2 = sha1(rawData + sessionKey)
        String signature2 = DigestUtils.sha1Hex(rawData + sessionKey);
        if (!signature.equals(signature2)) {
            return GlobalResult.build(500, "签名校验失败", null);
        }
        // 5.根据返回的User实体类，判断用户是否是新用户，是的话，将用户信息存到数据库；不是的话，更新最新登录时间
        User user = userService.getById(openid);
        
        String skey = null;
        String nickName = null;

        if (user == null) {
            // 用户信息入库
            nickName = rawDataJson.getString("nickName");
            String avatarUrl = rawDataJson.getString("avatarUrl");
            String city = rawDataJson.getString("city");
            String country = rawDataJson.getString("country");
            String province = rawDataJson.getString("province");

            Map<String,String> map = new HashMap<>();
            map.put("name", nickName);
            skey = JWTUtils.getToken(map);

            user = new User();
            user.setOpenId(openid);
            user.setSkey(skey);
            user.setCreateTime(new Date());
            user.setLastVisitTime(new Date());
            user.setSessionKey(sessionKey);
            user.setCity(city);
            user.setProvince(province);
            user.setCountry(country);
            user.setAvatarUrl(avatarUrl);
            user.setNickName(nickName);

            userService.getBaseMapper().insert(user);
        } else {
            Map<String,String> map = new HashMap<>();
            map.put("name", nickName);
            skey = JWTUtils.getToken(map);
            // 已存在，更新用户登录时间
            user.setLastVisitTime(new Date());
            // 重新设置会话skey
            user.setSkey(skey);
            userService.getBaseMapper().updateById(user);
        }
        //encrypteData比rowData多了appid和openid
        //JSONObject userInfo = WechatUtil.getUserInfo(encrypteData, sessionKey, iv);
        //6. 把新的skey返回给小程序
        GlobalResult result = GlobalResult.build(200, null, skey);
        return result;
    }

}

