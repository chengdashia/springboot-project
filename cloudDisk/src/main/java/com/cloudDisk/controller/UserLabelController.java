package com.cloudDisk.controller;


import com.cloudDisk.service.UserLabelService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * @author 成大事
 * @since 2022-04-15
 */
@Api(tags = "用户信息")
@RestController
@RequestMapping("/userLabel")
@Validated
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserLabelController {


    private final UserLabelService userLabelService;



}

