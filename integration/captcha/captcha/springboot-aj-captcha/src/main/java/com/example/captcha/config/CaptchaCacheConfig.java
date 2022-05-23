package com.example.captcha.config;

import com.anji.captcha.service.CaptchaCacheService;
import com.example.captcha.utils.redis.RedisUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 验证码
 */
@Component
public class CaptchaCacheConfig implements CaptchaCacheService {

    private static final String REDIS = "redis";

    @Resource
    private RedisUtil redisUtil;

    @Override
    public void set(String key, String value, long expiresInSeconds) {
        redisUtil.set(key, value, expiresInSeconds);
    }

    @Override
    public boolean exists(String key) {
        return redisUtil.hasKey(key);
    }

    @Override
    public void delete(String key) {
        redisUtil.del(key);
    }

    @Override
    public String get(String key) {
        return (String) redisUtil.get(key);
    }

    @Override
    public String type() {
        return REDIS;
    }

}
