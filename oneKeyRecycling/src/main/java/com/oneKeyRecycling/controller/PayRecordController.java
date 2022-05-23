package com.oneKeyRecycling.controller;


import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oneKeyRecycling.pojo.PayRecord;
import com.oneKeyRecycling.pojo.TOrder;
import com.oneKeyRecycling.service.PayRecordService;
import com.oneKeyRecycling.service.TOrderService;
import com.oneKeyRecycling.utils.globalResult.R;
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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 成大事
 * @since 2022-04-25
 */
@Api(tags = "支付记录")
@RestController
@RequestMapping("/payRecord")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Validated
public class PayRecordController {

}

