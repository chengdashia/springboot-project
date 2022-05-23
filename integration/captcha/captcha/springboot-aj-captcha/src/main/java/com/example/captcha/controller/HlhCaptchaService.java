package com.example.captcha.controller;

import com.anji.captcha.model.common.ResponseModel;
import com.example.captcha.pojo.CaptchaParams;


/**
 * Captcha验证码服务
 *
 * @author ShiJianming
 * @date 2022-02-24 22:33:54
 */
public interface HlhCaptchaService {

    /**
     * 获取验证码接口
     *
     * @return
     */
    BaseResult<ResponseModel> getCaptcha(String uid, long ts);

    /**
     * 前端验证滑动验证码
     *
     * @param dto
     * @return
     */
    BaseResult checkCaptcha(CaptchaParams dto);

    /**
     * 后端滑动验证码二次效验
     *
     * @param captchaVerification 二次验证参数
     * @return
     */
    BaseResult<Boolean> isCheckCaptcha(String captchaVerification);

}
