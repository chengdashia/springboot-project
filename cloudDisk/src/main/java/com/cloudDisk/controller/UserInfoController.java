package com.cloudDisk.controller;


import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloudDisk.pojo.UserInfo;
import com.cloudDisk.service.RootDirectoryInfoService;
import com.cloudDisk.service.UserInfoService;
import com.cloudDisk.utils.globalResult.CodeType;
import com.cloudDisk.utils.globalResult.MsgType;
import com.cloudDisk.utils.globalResult.R;
import com.cloudDisk.utils.globalResult.StatusType;
import com.cloudDisk.utils.redis.RedisUtil;
import com.cloudDisk.utils.validate.phone.Phone;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.xmlbeans.impl.xb.ltgfmt.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Map;

/**
 * @author 成大事
 * @since 2022-04-10
 */
@Slf4j
@Api(tags = "用户信息")
@RestController
@RequestMapping("/userInfo")
@Validated
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserInfoController {

    private final UserInfoService userInfoService;

    private final RedisUtil redisUtil;

    private final RootDirectoryInfoService rootDirectoryInfoService;

    /**
     * 普通登录,使用手机号+密码
     * @param phone  手机号
     * @param pwd    密码
     * @return  R
     */
    @ApiOperation("使用手机号+密码登录")
    @PostMapping("/loginByPassword")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name="user_tel",dataType="String",required=true,value="用户的手机号"),
            @ApiImplicitParam(paramType="query",name="user_pwd",dataType="String",required=true,value="用户的密码")
    })
    @ApiResponses({
            @ApiResponse(code = CodeType.SUCCESS,message = "登录成功"),
            @ApiResponse(code = CodeType.PWD_ERROR,message = "密码错误"),
            @ApiResponse(code = CodeType.ID_NOT_EXISTS,message = "用户不存在"),
            @ApiResponse(code = CodeType.SQL_ERROR,message = "服务器错误")
    })
    public R loginByPassword( @RequestParam("user_tel") @NotBlank(message = "手机号不能为空") @Phone @Size(max = 11,min = 11)String phone,
                              @RequestParam("user_pwd") @NotBlank(message = "密码不能为空") String pwd){
        try {
            UserInfo userInfo = userInfoService.getOne(new QueryWrapper<UserInfo>().select("user_id", "user_pwd", "user_initialize").eq("user_tel", phone));
            if(userInfo != null){
                if(userInfo.getUserPwd().equals(pwd)){
                    StpUtil.login(userInfo.getUserId(), SaLoginConfig.setExtra("uInit", userInfo.getUserInitialize()));
                    log.info("用户{}登录成功",userInfo.getUserId());
                    return R.ok(StpUtil.getTokenInfo());
                }else {
                    // 密码错误
                    return R.error(MsgType.PWD_ERROR);
                }
            }else {
                // 用户不存在 请先注册
                return R.notExists();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.sqlError();
        }
    }


    /**
     * 手机号登录,使用手机号+验证码
     * @param phone  手机号
     * @param code    手机验证码
     * @return  R
     */
    @ApiOperation("使用手机号+验证码登录")
    @PostMapping("/loginByVerificationCode")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name="user_tel",dataType="String",required=true,value="用户的手机号"),
            @ApiImplicitParam(paramType="query",name="verification_code",dataType="String",required=true,value="手机验证码")
    })
    @ApiResponses({
            @ApiResponse(code = CodeType.SUCCESS,message = "登录成功"),
            @ApiResponse(code = CodeType.CAPTCHA_ERROR,message = "验证码错误"),
            @ApiResponse(code = CodeType.CAPTCHA_EXPIRE,message = "验证码过期"),
            @ApiResponse(code = CodeType.SQL_ERROR,message = "服务器错误")
    })
    public R loginByVerificationCode(
            @RequestParam("user_tel") @NotBlank(message = "手机号不能为空") @Phone @Size(max = 11,min = 11)String phone,
            @RequestParam("verification_code") @NotBlank(message = "验证码不能为空") String code){
        if (redisUtil.hasKey(phone)) {
            if (redisUtil.get(phone).equals(code)) {
                try {
                    UserInfo userInfo = userInfoService.getOne(new QueryWrapper<UserInfo>()
                            .select("user_id", "user_initialize")
                            .eq("user_tel", phone));
                    if (userInfo != null){
                        StpUtil.login(userInfo.getUserId(), SaLoginConfig
                                .setExtra("uInit", userInfo.getUserInitialize()));
                        return R.ok(StpUtil.getTokenInfo());
                    }else {
                        // 用户不存在 请先注册
                        return R.notExists();
                    }
                } catch (Exception e) {
                    // 服务器错误
                    e.printStackTrace();
                    return R.sqlError();
                }
            }else {
                //验证码错误
                return R.error(CodeType.CAPTCHA_ERROR,MsgType.CAPTCHA_ERROR);
            }
        }else {
            //验证码过期
            return R.error(CodeType.CAPTCHA_EXPIRE,MsgType.CAPTCHA_EXPIRE);
        }
    }

    /**
     * 注册
     * @param phone       手机号
     * @param pwd         密码
     * @param smsCode     验证码
     * @return R
     */
    @ApiOperation("注册")
    @PostMapping("/registered")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name="phone",dataType="String",required=true,value="用户的手机号"),
            @ApiImplicitParam(paramType="query",name="pwd",dataType="String",required=true,value="用户的密码"),
            @ApiImplicitParam(paramType="query",name="smsCode",dataType="String",required=true,value="手机验证码")
    })
    @ApiResponses({
            @ApiResponse(code = CodeType.SUCCESS,message = "登录成功"),
            @ApiResponse(code = CodeType.ID_EXISTS,message = "用户已存在"),
            @ApiResponse(code = CodeType.CAPTCHA_EXPIRE,message = "验证码过期"),
            @ApiResponse(code = CodeType.FAILED,message = "服务器错误")
    })
    public R registered(
            @RequestParam("phone") @NotBlank(message = "手机号不能为空") @Phone @Size(max = 11,min = 11) String phone,
            @RequestParam("pwd") @NotBlank(message = "密码不能为空") String pwd,
            @RequestParam("smsCode") @NotBlank(message = "短信验证码不能为空") String smsCode
    ){
        //如果redis有这个验证码
        if(redisUtil.hasKey(phone)){
            String redisSmsCode = (String) redisUtil.get(phone);
            if(redisSmsCode.equals(smsCode)){
                int register = userInfoService.register(phone, pwd);
                if(register == StatusType.SUCCESS){
                    return R.ok();
                }else if(register == StatusType.EXISTS){
                    //手机号已存在
                    return R.error(CodeType.ID_EXISTS,MsgType.ID_EXISTS);
                }else {
                    //注册失败
                    return R.error();
                }
            }else {
                //验证码错误
                return R.error(CodeType.CAPTCHA_ERROR,MsgType.CAPTCHA_ERROR);
            }
        }else {
            //如果redis没有这个验证码   过期了。重新发送
            return R.error(CodeType.CAPTCHA_EXPIRE,MsgType.CAPTCHA_EXPIRE);
        }
    }



    /**
     * 用户获取个人信息
     * @return  R
     */
    @ApiOperation("用户获取个人信息 New")
    @PostMapping("/getUserInfoNew")
    @ApiResponses({
            @ApiResponse(code = CodeType.SUCCESS,message = "成功"),
            @ApiResponse(code = CodeType.ID_NOT_EXISTS,message = "用户不存在"),
            @ApiResponse(code = CodeType.FAILED,message = "服务器错误")
    })
    public R getUserInfoNew(){
        int uInit = (int) StpUtil.getExtra("uInit");
        //1.通过token获取个人信息
        //先查user_label 查insterest_lable_id 再去label info里面查label_name
        String uuId = (String) StpUtil.getLoginId();
        Map<String,Object> map = userInfoService.getUserInfo(uuId,uInit);
        Object status = map.get("status");
        if (status.equals(StatusType.SUCCESS)){
            map.remove("status");
            //成功
            return R.ok(map);
        }else if(status.equals(StatusType.NOT_EXISTS)){
            //用户不存在
            return R.notExists();
        }else {
            //失败
            return R.error();
        }

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

