package com.oneKeyRecycling.config.saToken;

import cn.dev33.satoken.jwt.StpLogicJwtForStyle;
import cn.dev33.satoken.stp.StpLogic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 成大事
 * @date 2022/3/13 21:18
 */
@Configuration
public class SaTokenConfigure {

    // Sa-Token 整合 jwt (Style模式)
    @Bean
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForStyle();
    }
}
