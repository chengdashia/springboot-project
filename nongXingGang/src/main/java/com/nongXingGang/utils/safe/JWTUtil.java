package com.nongXingGang.utils.safe;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Map;

/**
 * @author 成大事
 * @date 2021-12-15
 */
@Component
public class JWTUtil {

    private static final String SIGN = "bigDataStudio666##@@&#*#";
    /**
     * 生成token  header.payload.sign
     */
    public static String getToken(Map<String,String> map){

        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE,7);   //默认十五天过期

        //创建jwt  builder
        JWTCreator.Builder builder = JWT.create();

        //payload
        map.forEach((k,v)->{
            builder.withClaim(k,v);
        });

        String token = builder.withExpiresAt(instance.getTime())
                .sign(Algorithm.HMAC256(SIGN));
        return token;
    }

    /**
     * 验证token 合法性
     * @return
     */
    public static DecodedJWT verify(String token){
        return JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);
    }

    /**
     * 从token 中获取添加的数据
     * @return
     */
    public static String getValue(String token,String key){
        DecodedJWT verify = verify(token);
        return verify.getClaim(key).asString();
    }


    /**
     * 通过token  获取uuid
     * @return  openid
     */
    public static String getOpenid(HttpServletRequest request){
        try {
            String token = request.getHeader("token");
            return getValue(token,"openid");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 通过token  获取uuid
     * @return  openid
     */
    public static String getOpenid(String token){
        try {
            return getValue(token,"openid");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }






}
