package com.nongXingGang.controller;


import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 成大事
 * @since 2022-03-18
 */
@Api(tags = "合作社信息")
@RestController
@RequestMapping("/coop-info")
@Validated
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CoopInfoController {

}

