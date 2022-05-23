package com.example.captcha.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 验证码验证请求参数
 *
 * date: 2021/5/25 17:24
 *
 * @author ShiJianming
 */
@Data
public class CaptchaParams implements Serializable {

    /**
     * 点坐标(base64加密传输)
     */
    private String pointJson;

    /**
     * UUID(每次请求的验证码唯一标识)
     */
    private String token;
}
