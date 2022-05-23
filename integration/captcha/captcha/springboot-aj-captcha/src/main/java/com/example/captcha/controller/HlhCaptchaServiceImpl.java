package com.example.captcha.controller;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;


/**
 * 验证码接口
 *
 * @author ShiJianming
 * @date 2022-02-24 22:41:08
 */
@Service
public class HlhCaptchaServiceImpl implements HlhCaptchaService {

    @Autowired
    private CaptchaService captchaService;

    /**
     * 获取验证码接口
     *
     * @return
     */
    @Override
    public BaseResult<ResponseModel> getCaptcha(String uid, long requestTime) {
        CaptchaVO vo = new CaptchaVO();
        //验证码类型:(clickWord,blockPuzzle)
        vo.setCaptchaType(HlhWebConstant.IMAGE_CODE_TYPE);
        vo.setClientUid(uid);
        vo.setTs(requestTime);
        //获取验证码
        ResponseModel responseModel = captchaService.get(vo);
        //返回获取结果
        return BaseResult.setResult(responseModel.getRepMsg(), responseModel.getRepCode(), responseModel.getRepData());
    }

    /**
     * 前端验证滑动验证码
     *
     * @param dto
     * @return
     */
    @Override
    public BaseResult checkCaptcha(CaptchaParams dto) {
        CaptchaVO vo = new CaptchaVO();
        //点坐标(base64加密传输)
        vo.setPointJson(dto.getPointJson());
        //UUID(每次请求的验证码唯一标识)
        vo.setToken(dto.getToken());
        //验证码类型:(clickWord,blockPuzzle)
        vo.setCaptchaType(HlhWebConstant.IMAGE_CODE_TYPE);
        //核对验证码(前端)
        ResponseModel responseModel = captchaService.check(vo);
        //返回校验结果
        return BaseResult.setResult(responseModel.getRepMsg(), responseModel.getRepCode(), responseModel.getRepData());
    }

    /**
     * 后端滑动验证码二次效验
     *
     * @param captchaVerification 二次验证参数
     * @return
     */
    @Override
    public BaseResult<Boolean> isCheckCaptcha(String captchaVerification) {
        CaptchaVO dto = new CaptchaVO();
        //后台二次校验参数
        dto.setCaptchaVerification(captchaVerification);
        //二次校验验证码(后端)
        ResponseModel responseModel = captchaService.verification(dto);
        if (Objects.nonNull(responseModel) && Objects.nonNull(responseModel.getRepCode()) && HintEnum.SUCCESS.getCode().equals(responseModel.getRepCode())) {
            return BaseResult.success(true);
        }
        return BaseResult.setResult(responseModel.getRepMsg(), responseModel.getRepCode(), false);
    }

}
