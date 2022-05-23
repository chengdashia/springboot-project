package com.manke.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.manke.pojo.User;
import com.manke.service.UserService;
import com.manke.utils.globalResult.CodeType;
import com.manke.utils.globalResult.MsgType;
import com.manke.utils.globalResult.R;
import com.manke.utils.redis.RedisUtil;
import com.manke.utils.safe.JWTUtil;
import com.manke.utils.validate.phone.Phone;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户
 * @author 成大事
 * @since 2022-03-31
 */
@Api(tags = "用户表")
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final RedisUtil redisUtil;

    @Autowired
    public UserController(UserService userService,RedisUtil redisUtil){
        this.userService = userService;
        this.redisUtil = redisUtil;
    }

    /**
     * 普通登录,使用手机号+密码
     * @param phone  手机号
     * @param pwd    密码
     * @return  R
     */
    @ApiOperation("使用手机号+密码登录")
    @PostMapping("/login")
    public R publicLogin(
            @ApiParam(value = "手机号",required = true) @RequestParam("phone") @NotBlank(message = "手机号不能为空") @Phone @Size(max = 11,min = 11) String phone,
            @ApiParam(value = "密码",required = true) @RequestParam("pwd") @NotBlank(message = "密码不能为空") String pwd){
        try{
            User user = userService.getOne(new QueryWrapper<User>()
                            .select("u_id","pwd")
                    .eq("phone", phone));
            //如果数据库有这个个人信息
            if(user != null){
                //如果密码正确
                if(user.getPwd().equals(pwd)){
                    Map<String, String> map = new HashMap<>();
                    map.put("uid",user.getUId());
                    String token = JWTUtil.getToken(map);
                    //sa-token 登录
                    StpUtil.login(token);
                    user.setLastTime(new Date());
                    //更新用户最后登录时间
                    userService.updateById(user);
                    return R.success(token);
                }else {
                    //密码错误
                    return R.failure(CodeType.PWD_ERROR,MsgType.PWD_ERROR);
                }
            }else {
                //用户不存在
                return R.notExists();
            }
        }catch (Exception e){
            //数据库错误
            e.printStackTrace();
            return R.sqlError();
        }
    }

    @ApiOperation("注册")
    @PostMapping("/register")
    public R register(
            @ApiParam(value = "用户名",required = true) @RequestParam("nickName") @NotBlank(message = "用户名不能为空") String nickName,
            @ApiParam(value = "手机号",required = true) @RequestParam("phone") @NotBlank(message = "手机号不能为空") @Phone @Size(max = 11,min = 11) String phone,
            @ApiParam(value = "密码",required = true) @RequestParam("pwd") @NotBlank(message = "密码不能为空") String pwd,
            @ApiParam(value = "验证码",required = true) @RequestParam("smsCode") @NotBlank(message = "短信验证码不能为空") String smsCode
    ){
        //如果redis有这个验证码
        if(redisUtil.hasKey(phone)){
            String redisSmsCode = (String) redisUtil.get(phone);
            if(redisSmsCode.equals(smsCode)){
                try {
                    //用户不存在，注册
                    User user = userService.getOne(new QueryWrapper<User>().eq("phone", phone));
                    if(user == null){
                        user = new User();
                        user.setUId(IdUtil.simpleUUID());
                        user.setPhone(phone);
                        user.setPwd(pwd);
                        user.setNickName(nickName);
                        user.setCreateTime(new Date());
                        user.setLastTime(new Date());
                        userService.save(user);
                        return R.success();
                    }else {
                        //用户已存在，请勿重复注册
                        return R.failure(CodeType.ID_EXISTS,MsgType.ID_EXISTS);
                    }
                }catch (Exception e){
                    //数据库错误
                    e.printStackTrace();
                    return R.sqlError();
                }
                //验证码错误
            }else {
                return R.failure(CodeType.CAPTCHA_ERROR,MsgType.CAPTCHA_ERROR);
            }
            //如果redis没有这个验证码   过期了。重新发送
        }else {
            return R.failure(CodeType.CAPTCHA_EXPIRE,MsgType.CAPTCHA_EXPIRE);
        }
    }



    @ApiOperation("修改密码")
    @PostMapping("/updatePwd")
    public R updatePwd(
            @ApiParam(value = "手机号",required = true) @RequestParam("phone") @NotBlank(message = "手机号不能为空") @Phone @Size(max = 11,min = 11) String phone,
            @ApiParam(value = "密码",required = true) @RequestParam("pwd") @NotBlank(message = "密码不能为空") String pwd,
            @ApiParam(value = "重复密码",required = true) @RequestParam("rePwd") @NotBlank(message = "密码不能为空") String rePwd,
            @ApiParam(value = "验证码",required = true) @RequestParam("smsCode") @NotBlank(message = "短信验证码不能为空") String smsCode
    ){
        //如果redis有这个验证码
        if(redisUtil.hasKey(phone)){
            String redisSmsCode = (String) redisUtil.get(phone);
            if(redisSmsCode.equals(smsCode)) {
                if(pwd.equals(rePwd)){
                    try{
                        boolean update = userService.update(new LambdaUpdateWrapper<User>()
                                .set(User::getPwd, pwd)
                                .eq(User::getPhone, phone));
                        if(update){
                            return R.success();
                        }else {
                            return R.failure();
                        }
                    }catch (Exception e){
                        //数据库错误
                        e.printStackTrace();
                        return R.sqlError();
                    }
                    //两次密码不一致！
                }else {
                    return R.failure(CodeType.TWO_PWD_ERROR,MsgType.TWO_PWD_ERROR);
                }
                //验证码错误
            }else {
                return R.failure(CodeType.CAPTCHA_ERROR,MsgType.CAPTCHA_ERROR);
            }
            //验证码过期
        }else {
            return R.failure(CodeType.CAPTCHA_EXPIRE,MsgType.CAPTCHA_EXPIRE);
        }
    }




    /**
     * 生成图像验证码
     * @param response response请求对象
     */
    @ApiOperation("生成图像验证码")
    @GetMapping("/register/generateValidateCode")
    public void generateValidateCode(HttpServletResponse response) throws IOException {
        //设置response响应
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        //定义图形验证码的长和宽
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100,4,100);
//        ShearCaptcha shearCaptcha = CaptchaUtil.createShearCaptcha(200, 100, 4, 10);
//        CircleCaptcha circleCaptcha = CaptchaUtil.createCircleCaptcha(200, 100, 4, 100);
//        int time=60;
//
        System.out.println("lineCaptcha:   "+lineCaptcha);
        System.out.println("lineCaptchaGetBase64:   "+lineCaptcha.getImageBase64());
        //把凭证对应的验证码信息保存到reids（可从redis中获取）
//        redisUtil.set(lineCaptcha.getCode(), lineCaptcha.getCode(),60);

        //输出浏览器
        OutputStream out = response.getOutputStream();

//        System.out.println("lineCaptcha:  "+lineCaptcha.getCode());
//        System.out.println("shearCaptcha:  "+shearCaptcha.getCode());
//        System.out.println("circleCaptcha:  "+circleCaptcha.getCode());

        lineCaptcha.write(out);

        out.flush();
        out.close();
    }


    @SaCheckLogin
    @ApiOperation("测试sa-token")
    @SaCheckRole(value = {"0"})
    @PostMapping("/testSa")
    public R testSa(HttpServletRequest request){
        System.out.println(StpUtil.hasRole("0"));
        return R.success();

    }

//    @ApiOperation("验证码校验")
//    @PostMapping("verityValidateCode")
//    public R verityValidateCode(
//            @ApiParam(value = "手机号",required = true) @RequestParam("phone") @NotBlank(message = "手机号不能为空") @Phone @Size(max = 11,min = 11) String phone,
//            @ApiParam(value = "图片验证码",required = true) @RequestParam("validateCode") @NotBlank(message = "验证码不能为空") String validateCode
//    ){
//
//    }


}

