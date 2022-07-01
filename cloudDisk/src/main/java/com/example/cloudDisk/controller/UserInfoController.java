package com.example.cloudDisk.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.example.cloudDisk.common.result.R;
import com.example.cloudDisk.service.UserInfoService;
import com.example.cloudDisk.utils.validate.phone.Phone;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 成大事
 * @since 2022-07-01 11:54:54
 */
@Slf4j
@Api(tags = "用户信息")
@RestController
@RequestMapping("/userInfo")
@Validated
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserInfoController {

    private final UserInfoService userInfoService;

    /**
     * 普通登录,使用手机号+密码
     * @param tel  手机号
     * @param pwd    密码
     * @return  R
     */
    @ApiOperation("使用手机号+密码登录")
    @PostMapping("/loginByPassword")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name="user_tel",dataTypeClass = String.class,required=true,value="用户的手机号"),
            @ApiImplicitParam(paramType="query",name="user_pwd",dataTypeClass = String.class,required=true,value="用户的密码")
    })
    public R<Object> loginByPassword(
            @RequestParam("user_tel") @NotBlank(message = "手机号不能为空") @Phone @Size(max = 11,min = 11)String tel,
            @RequestParam("user_pwd") @NotBlank(message = "密码不能为空") String pwd
    ){
       return userInfoService.loginByPwd(tel,pwd);
    }


    /**
     * 手机号登录,使用手机号+验证码
     * @param tel  手机号
     * @param code    手机验证码
     * @return  R
     */
    @ApiOperation("使用手机号+验证码登录")
    @PostMapping("/loginByVerificationCode")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name="user_tel",dataTypeClass = String.class,required=true,value="用户的手机号"),
            @ApiImplicitParam(paramType="query",name="verification_code",dataTypeClass = String.class,required=true,value="手机验证码")
    })
    public R<Object> loginByVerificationCode(
            @RequestParam("user_tel") @NotBlank(message = "手机号不能为空") @Phone String tel,
            @RequestParam("verification_code") @NotBlank(message = "验证码不能为空") @Size(max = 6,min = 6) String code
    ){
        return userInfoService.loginByCaptcha(tel,code);
    }

    /**
     * 注册
     * @param tel       手机号
     * @param pwd         密码
     * @param code     验证码
     * @return R
     */
    @ApiOperation("注册")
    @PostMapping("/registered")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name="phone",dataTypeClass = String.class,required=true,value="用户的手机号"),
            @ApiImplicitParam(paramType="query",name="pwd",dataTypeClass = String.class,required=true,value="用户的密码"),
            @ApiImplicitParam(paramType="query",name="smsCode",dataTypeClass = String.class,required=true,value="手机验证码")
    })
    public R<Object> registered(
            @RequestParam("phone") @NotBlank(message = "手机号不能为空") @Phone @Size(max = 11,min = 11) String tel,
            @RequestParam("pwd") @NotBlank(message = "密码不能为空") String pwd,
            @RequestParam("smsCode") @NotBlank(message = "短信验证码不能为空") String code
    ){
        return userInfoService.register(tel, pwd, code);
    }



    /**
     * 用户获取个人信息
     * @return  R
     */
    @ApiOperation("用户获取个人信息 New")
    @PostMapping("/getUserInfo")
    public R<Object> getUserInfoNew(){
        int uInit = (int) StpUtil.getExtra("uInit");
        //1.通过token获取个人信息
        //先查user_label 查insterest_lable_id 再去label info里面查label_name
        String uId = (String) StpUtil.getLoginId();
        return userInfoService.getUserInfo(uId,uInit);
    }

    /**
     * 用户注销登录
     */
    @ApiOperation("用户注销登录")
    @PostMapping("/loginOut")
    public void loginOut(
    ){
        Object id = StpUtil.getLoginId();
        StpUtil.logout(id);
    }

}
