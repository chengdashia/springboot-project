package com.example.springbootjwt.test;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class testDemo01 {

    @Test
    void encryption(){
        Map<String, Object> map = new HashMap<>();

        Calendar instance = Calendar.getInstance();

        instance.add(Calendar.SECOND,120);

        String token = JWT.create()
                .withHeader(map)                        //header
                .withClaim("userid", 1616)   //payload
                .withClaim("username", "chengguo")
                .withExpiresAt(instance.getTime())                       //指定令牌过期时间
                .sign(Algorithm.HMAC256("!34%1@%#5!0"));//签名

        System.out.println(token);
    }

    @Test
    void decryption(){

        //创建验证对象
        JWTVerifier require = JWT.require(Algorithm.HMAC256("!34%1@%#5!0")).build();

        DecodedJWT verify = require.verify("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MzY5NTY0MjAsInVzZXJpZCI6MTYxNiwidXNlcm5hbWUiOiJjaGVuZ2d1byJ9.Lpeeyq12PSYyn-5I4G3INYOetcy_HpEPH3xXQ-7g37U");

        System.out.println(verify.getClaim("userid").asInt());
        System.out.println(verify.getClaim("username").asString());
    }


}
