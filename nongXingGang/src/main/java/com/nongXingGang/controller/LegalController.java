package com.nongXingGang.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.nongXingGang.service.LegalService;
import com.nongXingGang.utils.result.R;
import com.nongXingGang.utils.result.StatusType;
import com.nongXingGang.utils.safe.JWTUtil;
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
@Api(tags = "法人")
@RestController
@RequestMapping("/legal")
@Validated
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LegalController {


    private final LegalService legalService;



    @ApiOperation("法人认证")
    @PostMapping("/legalCertification")
    public R LegalCertification(
            @ApiParam(value = "法人号码",required = true)@NotBlank(message = "不能为空")@NotNull @RequestParam("legalNum") String legalNum,
            @ApiParam(value = "imgPath",required = true)@NotBlank(message = "不能为空")@NotNull @RequestParam("imgPath") String imgPath
    ){
        String openid = (String) StpUtil.getLoginId();
        int certification = legalService.certification(openid, legalNum, imgPath);
        if(certification == StatusType.SUCCESS){
            return R.ok();
        }else if(certification == StatusType.FIRST_CERTIFICATION_ID){
            return R.error("请先认证身份");
        }else if(certification == StatusType.SQL_ERROR){
            return R.sqlError();
        }else {
            return R.error();
        }

    }

}

