package com.nongXingGang.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.nongXingGang.service.ReceivingAddressService;
import com.nongXingGang.utils.result.R;
import com.nongXingGang.utils.result.StatusType;
import com.nongXingGang.utils.safe.JWTUtil;
import com.nongXingGang.utils.validate.phone.Phone;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 成大事
 * @since 2022-03-18
 */
@Api(tags = "收货地址")
@RestController
@RequestMapping("/receivingAddress")
@Validated
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class ReceivingAddressController {

    private final ReceivingAddressService receivingAddressService;


    @ApiOperation("获取收货地址")
    @PostMapping("/getSelfAddress")
    public R getSelfAddress(

    ){
        String openid = (String) StpUtil.getLoginId();
        log.info("收货地址的：  openid:  "+StpUtil.getTokenInfo().getTokenValue());
        return receivingAddressService.getSelfAddress(openid);

    }


    @ApiOperation("添加收货地址")
    @PostMapping("/addAddress")
    public R addAddress(
                        @ApiParam(value = "用户电话号码",required = true) @Phone @Size(max = 11,min = 11) @RequestParam("userTel") String userTel,
                        @ApiParam(value = "用户姓名",required = true) @NotNull @NotBlank(message = "用户名不能为空") @RequestParam("userRealName") String userRealName,
                        @ApiParam(value = "用户地址",required = true) @NotNull @NotBlank(message = "地址不能为空") @RequestParam("userDetailedAddress") String userDetailedAddress
                        ){
        String openid = (String) StpUtil.getLoginId();
        return receivingAddressService.addAddress(openid, userRealName, userTel, userDetailedAddress);
    }




}

