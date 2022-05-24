package com.nongXingGang.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.nongXingGang.service.CertificationService;
import com.nongXingGang.utils.result.CodeType;
import com.nongXingGang.utils.result.MsgType;
import com.nongXingGang.utils.result.R;
import com.nongXingGang.utils.result.StatusType;
import com.nongXingGang.utils.validate.phone.Phone;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 成大事
 * @since 2022-03-18
 */
@Api(tags = "身份认证")
@RestController
@RequestMapping("/certification")
@Validated
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CertificationController {


    private final CertificationService certificationService;


    @ApiOperation("认证")
    @PostMapping("/idAuthentication")
    public R idAuthentication(
          @ApiParam(value = "身份证号",required = true) @Phone @RequestParam("idNum") String idNum,
          @ApiParam(value = "真实姓名",required = true) @NotBlank(message = "姓名不能为空") @NotNull(message = "姓名不能为空") @RequestParam("realName") String realName,
          @ApiParam(value = "身份证前面图片",required = true)@NotBlank(message = "图片地址不能为空") @NotNull(message = "图片地址不能为空")@RequestParam("idImgFrontPath") String idImgFrontPath,
          @ApiParam(value = "身份证后面面图片",required = true)@NotBlank(message = "图片地址不能为空") @NotNull(message = "图片地址不能为空")@RequestParam("idImgBackPath") String idImgBackPath) throws Exception {
        String openid = (String) StpUtil.getLoginId();
        return certificationService.idAuthentication(openid, idNum, realName, idImgFrontPath, idImgBackPath);
    }

}

