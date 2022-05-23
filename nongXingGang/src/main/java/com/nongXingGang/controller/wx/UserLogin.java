package com.nongXingGang.controller.wx;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nongXingGang.pojo.User;
import com.nongXingGang.service.UserService;
import com.nongXingGang.utils.result.Constants;
import com.nongXingGang.utils.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 成大事
 * @date 2022/3/18 17:15
 */
@Api(tags = "微信登录")
@RestController
@RequestMapping("/wx")
@Validated
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserLogin {

    private final UserService userService;

    /**
     * 微信用户登录详情
     */
    @ApiOperation("微信登录")
    @PostMapping("/login")
    public R userLogin(@RequestParam(value = "code", required = false) String code,
                       @RequestParam(value = "rawData", required = false) String rawData,
                       @RequestParam(value = "signature", required = false) String signature) {
        // 用户非敏感信息：rawData
        // 签名：signature
        JSONObject rawDataJson = JSON.parseObject(rawData);
        // 1.接收小程序发送的code
        // 2.开发者服务器 登录凭证校验接口 appi + appsecret + code
        JSONObject SessionKeyOpenId = WeChatUtil.getSessionKeyOrOpenId(code);
//        System.out.println(code);
//        System.out.println(SessionKeyOpenId);
        // 3.接收微信接口服务 获取返回的参数
        String openid = SessionKeyOpenId.getString("openid");

        String sessionKey = SessionKeyOpenId.getString("session_key");

        // 4.校验签名 小程序发送的签名signature与服务器端生成的签名signature2 = sha1(rawData + sessionKey)
        String signature2 = DigestUtils.sha1Hex(rawData + sessionKey);
        if (!signature.equals(signature2)) {
            return R.error("签名校验失败");
        }
        // 5.根据返回的User实体类，判断用户是否是新用户，是的话，将用户信息存到数据库；不是的话，更新最新登录时间
        User user = userService.getById(openid);

        String nickName = null;

        String token;
        if (user == null) {
            // 用户信息入库
            nickName = rawDataJson.getString("nickName");
            String avatarUrl = rawDataJson.getString("avatarUrl");
//            String city = rawDataJson.getString("city");
//            String country = rawDataJson.getString("country");
//            String province = rawDataJson.getString("province");

//            Map<String,String> map = new HashMap<>();
//            map.put("name", nickName);
//            map.put("openid", openid);
//            token = JWTUtil.getToken(map);

            StpUtil.login(openid);

            user = new User();
            user.setUserOpenid(openid);
            user.setUserNickName(nickName);
            user.setUserGender(Constants.MAN);
            user.setSessionKey(sessionKey);
            user.setUserAvatarUrl(avatarUrl);
            user.setUserStatus(Constants.COOP);
            user.setCreateTime(new Date());
            user.setLastLoginTime(new Date());
            userService.getBaseMapper().insert(user);
        } else {
            Map<String,String> map = new HashMap<>();
//            map.put("name", nickName);
//            map.put("openid", openid);
//            token = JWTUtil.getToken(map);

            //放到sa-token中
            StpUtil.login(openid);

            // 已存在，更新用户登录时间
            user.setLastLoginTime(new Date());
            // 重新设置会话skey
            user.setSessionKey(sessionKey);
            userService.getBaseMapper().updateById(user);
        }
        //encrypteData比rowData多了appid和openid
        //JSONObject userInfo = WechatUtil.getUserInfo(encrypteData, sessionKey, iv);
        //6. 把新的skey返回给小程序
        return R.ok(StpUtil.getTokenInfo().getTokenValue());
    }
}
