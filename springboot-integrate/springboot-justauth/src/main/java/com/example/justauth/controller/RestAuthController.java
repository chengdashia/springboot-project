package com.example.justauth.controller;

import cn.hutool.json.JSONUtil;
import me.zhyd.oauth.model.AuthResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.request.AuthGiteeRequest;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 成大事
 * @since 2022/4/4 20:34
 */
@RestController
@RequestMapping("/oauth")
public class RestAuthController {

    @RequestMapping("/render")
    public void renderAuth(HttpServletResponse response) throws IOException {
        AuthRequest authRequest = getAuthRequest();
        response.sendRedirect(authRequest.authorize(AuthStateUtils.createState()));
    }

    @RequestMapping("/callback")
    public Object login(AuthCallback callback) {
        AuthRequest authRequest = getAuthRequest();

        AuthResponse login = authRequest.login(callback);
        String s = JSONUtil.toJsonStr(login);
        System.out.println(s);

        return login;
    }

    private AuthRequest getAuthRequest() {
        return new AuthGiteeRequest(AuthConfig.builder()
                .clientId("204300a2252aab19c2c6fe0a29b6d82faa65439effe3a3fe964548e2ddcace7a")
                .clientSecret("403bb6895bb426b23a9164d9f684c21812121bd6eb5d913be65604f1ba54705a")
                .redirectUri("http://127.0.0.1:9999/oauth/callback")
                .build());
    }
}

