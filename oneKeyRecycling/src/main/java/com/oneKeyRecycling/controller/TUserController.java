package com.oneKeyRecycling.controller;


import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.extra.qrcode.QrCodeException;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oneKeyRecycling.pojo.TUser;
import com.oneKeyRecycling.service.TUserService;
import com.oneKeyRecycling.utils.safe.JWTUtil;
import com.oneKeyRecycling.utils.globalResult.CodeType;
import com.oneKeyRecycling.utils.globalResult.R;
import com.oneKeyRecycling.utils.globalResult.MsgType;
import com.oneKeyRecycling.utils.globalResult.StatusType;
import com.oneKeyRecycling.utils.redis.RedisUtil;
import com.oneKeyRecycling.utils.validate.id.Cid;
import com.oneKeyRecycling.utils.validate.phone.Phone;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 成大事
 * @since 2022-03-13
 */
@Slf4j
@Api(tags = "用户 API 接口")
@RestController
@RequestMapping("/TUser")
@Validated
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TUserController {

    private final TUserService tUserService;

    private final RedisUtil redisUtil;


    @ApiOperation("注册")
    @PostMapping("/register")
    public R userRegister(@ApiParam(value = "手机号",required = true) @RequestParam("phone") String phone){
        return new R(CodeType.UNKNOWN_ERROR, MsgType.UNKNOWN_ERROR,null);
    }

    /**
     * 普通登录,使用手机号+密码
     * @param phone  手机号
     * @param pwd    密码
     * @return  R
     */
    @ApiOperation("使用手机号+密码登录")
    @PostMapping("/publicLogin")
    public R publicLogin(@ApiParam(value = "手机号",required = true) @RequestParam("phone") @NotBlank(message = "手机号不能为空") @Phone @Size(max = 11,min = 11)String phone,
                         @ApiParam(value = "密码",required = true) @RequestParam("pwd") @NotBlank(message = "密码不能为空") String pwd){
        Map<String, Object> loginMap = tUserService.publicLogin(phone, pwd);
        int status = (int) loginMap.get("status");
        //成功
        if(status == StatusType.SUCCESS){
            StpUtil.login(loginMap.get("uuid"));
            return R.success(StpUtil.getTokenInfo().getTokenValue());
        }else if(status == StatusType.PWD_ERROR){      // 密码错误
            return new R(CodeType.PWD_ERROR, MsgType.PWD_ERROR,null);
        }else if(status == StatusType.NOT_EXISTS){    // 用户不存在
            return new R(CodeType.ID_NOT_EXISTS, MsgType.USER_NOT_EXISTS,null);
        }else if (status == StatusType.SQL_ERROR){       // 数据库错误
            return R.sqlError();
        }else {                                          // 未知错误
            return R.failure();
        }
    }

    /**
     * 手机号+验证码登录  顺带注册
     * @param phone  手机号
     * @param captcha 验证码
     * @return  R
     */
    @ApiOperation("使用手机号+验证码登录  顺带注册")
    @PostMapping("/SMSLogin")
    public R SMSLogin(@ApiParam("手机号") @RequestParam("phone")  @NotBlank(message = "手机号不能为空") @Phone @Size(max = 11,min = 11)String phone,
                      @ApiParam("验证码") @RequestParam("captcha") @NotBlank(message = "验证码不能为空") String captcha){

        //如果redis里面 存在
        if(redisUtil.hasKey(phone)){
            if(captcha.equals(redisUtil.get(phone))){                  //验证码对比一下
                Map<String, Object> SMSLoginMAP = tUserService.SMSLogin(phone);
                int status = (int) SMSLoginMAP.get("status");
                //成功
                Map<String, String> map = new HashMap<>();
                if(status == StatusType.SUCCESS){
                    StpUtil.login(String.valueOf(SMSLoginMAP.get("uuid")));
                    return R.success(StpUtil.getTokenInfo().getTokenValue());
                }else if(status == StatusType.NOT_EXISTS_REGISTER){    // 用户不存在,但是注册了
                    map.put("uuid", String.valueOf(SMSLoginMAP.get("uuid")));
                    String token = JWTUtil.getToken(map);
                    //使用sa-token ，对token进行登录
                    StpUtil.login(token);
                    return new R(CodeType.SUCCESS, MsgType.SUCCESS,token);
                }else if (status == StatusType.SQL_ERROR){       // 数据库错误
                    return new R(CodeType.SQL_ERROR, MsgType.SQL_ERROR,null);
                }else {                                          // 未知错误
                    return new R(CodeType.UNKNOWN_ERROR, MsgType.UNKNOWN_ERROR,null);
                }
            }else {
                //验证码不正确
                return new R(CodeType.CAPTCHA_ERROR, MsgType.CAPTCHA_ERROR,null);
            }
        }
        //验证码过期了
        return new R(CodeType.CAPTCHA_EXPIRE, MsgType.CAPTCHA_EXPIRE,null);

    }

    // 查询登录状态
    @ApiOperation("查询登录状态")
    @RequestMapping(value = "/isLogin",method = RequestMethod.POST)
    public String isLogin(HttpServletRequest request) {
        String token = request.getHeader("token");
        System.out.println(token);
        System.out.println("是否登录：" + StpUtil.isLogin());

        return "Success";
    }

    @ApiOperation("查看个人信息 包括：头像，账号，昵称，余额")
    @PostMapping("/getMyInfo")
    public R getMyInfo(){
        String uuId = String.valueOf(StpUtil.getLoginId());
        Map<String, Object> one;
        try {
            one = tUserService.getMap(new QueryWrapper<TUser>()
                    .eq("uid", uuId));
            if(one != null){
                // 成功
                return R.success(one);
            }
            // 错误
            return R.failure();
        } catch (Exception e) {
            e.printStackTrace();
            // 数据库错误
            return R.sqlError();
        }
    }


    @ApiOperation("修改个人信息 包括：头像，昵称，密码，地址")
    @PostMapping("/updateSelfInfo")
    public R updateSelfInfo(
                          @ApiParam("地址") @RequestParam(value = "address",required = false) String address,
                          @ApiParam("昵称") @RequestParam(value = "nickName",required = false) String nickName,
                          @ApiParam("头像") @RequestParam(value = "avatar",required = false) String avatar,
                          @ApiParam("密码") @RequestParam(value = "pwd",required = false) String pwd){
        String uuId = String.valueOf(StpUtil.getLoginId());
        int result = tUserService.updateMyInfo(uuId, address, nickName, avatar, pwd);
        if (result == StatusType.SUCCESS){
            return R.success();
        }else if(result == StatusType.SQL_ERROR){
            return R.sqlError();
        }else if(result == StatusType.NOT_CHANGE){
            return R.success(MsgType.NOT_CHANGE);
        }else {
            return R.failure();
        }
    }


    @ApiOperation("认证")
    @PostMapping("/idAuthentication")
    public R idAuthentication(
                              @ApiParam(value = "身份证号",required = true) @Cid @RequestParam("idNum") String idNum,
                              @ApiParam(value = "真实姓名",required = true) @NotBlank(message = "姓名不能为空") @NotNull(message = "姓名不能为空") @RequestParam("idNum") String realName,
                              @ApiParam(value = "身份证前面图片",required = true)@NotBlank(message = "图片地址不能为空") @NotNull(message = "图片地址不能为空")@RequestParam("idNum") String idImgFrontPath,
                              @ApiParam(value = "身份证后面面图片",required = true)@NotBlank(message = "图片地址不能为空") @NotNull(message = "图片地址不能为空")@RequestParam("idNum") String idImgBackPath){
        String uuId = String.valueOf(StpUtil.getLoginId());
        int result = tUserService.idAuthentication(uuId, idNum, realName, idImgFrontPath, idImgBackPath);
        if(result == StatusType.SUCCESS){
            // 成功
            return R.success();
        }else if(result == StatusType.SQL_ERROR){
            // 数据库错误
            return R.sqlError();
        }else if(result == StatusType.ID_INFO_ERROR){
            //身份证信息错误
            return R.failure(CodeType.ID_INFO_ERROR,MsgType.ID_INFO_ERROR);
        }else if(result == StatusType.ID_CARD_ERROR){
            //身份证图片错误
            return R.failure(CodeType.ID_CARD_ERROR,MsgType.ID_CARD_ERROR);
        } else {
            // 错误
            return R.failure();
        }
    }


    /**
     * 生成二维码
     * @param response  响应
     * @return 二维码
     */
    @GetMapping("/getQRCode")
    @ResponseBody
    public void getQRCode(HttpServletResponse response,
                          @ApiParam(value = "路径",required = true) @NotBlank(message = "路径不能为空") @NotNull @RequestParam("path") String path) throws IOException {
        try {
            QrConfig qrConfig = new QrConfig(300, 300);
            QrCodeUtil.generate(path, qrConfig, "png", response.getOutputStream());
        } catch (QrCodeException | IOException e) {
            e.printStackTrace();
        }
    }



}

