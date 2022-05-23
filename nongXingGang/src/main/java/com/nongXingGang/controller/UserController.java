package com.nongXingGang.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nongXingGang.pojo.Goods;
import com.nongXingGang.service.GoodsService;
import com.nongXingGang.utils.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 成大事
 * @since 2022-03-18
 */
@Slf4j
@Api(tags = "用户表")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserController {

    private final GoodsService goodsService;


    @ApiOperation("我的总发布商品的数量")
    @GetMapping("/getMyPublishGoodsSum")
    public R getMyPublishGoodsSum(){
        String id = (String) StpUtil.getLoginId();
        int count = goodsService.count(new QueryWrapper<Goods>()
                .eq("user_openid", id));
        return R.ok(count);
    }

}

