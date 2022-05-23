package com.oneKeyRecycling.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oneKeyRecycling.pojo.TOrder;
import com.oneKeyRecycling.service.TOrderService;
import com.oneKeyRecycling.utils.globalResult.R;
import com.oneKeyRecycling.utils.globalResult.StatusType;
import com.oneKeyRecycling.utils.safe.JWTUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 成大事
 * @since 2022-03-13
 */
@Api(tags = "订单")
@RestController
@RequestMapping("/order")
@Validated
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TOrderController {

    private final TOrderService orderService;


    @ApiOperation("提交订单")
    @PostMapping("/submitOrder")
    public R submitOrder(
            HttpServletRequest request,
            @ApiParam("商家id")                          @RequestParam(value = "bId",required = false) String bId,
            @ApiParam(value = "物品名称",required = true) @NotNull @NotBlank(message = "不能为空") @RequestParam("sName") String sName,
            @ApiParam(value = "物品数量",required = true) @NotNull @RequestParam("sQuantity") int sQuantity,
            @ApiParam(value = "物品重量",required = true) @NotNull @RequestParam("sWeight") Float sWeight,
            @ApiParam("图片1")                           @RequestParam(value = "img1",required = false) String img1,
            @ApiParam("图片2")                           @RequestParam(value = "img2",required = false) String img2,
            @ApiParam("图片3")                           @RequestParam(value = "img3",required = false) String img3,
            @ApiParam(value = "用户地址",required = true) @NotNull @NotBlank(message = "不能为空") @RequestParam("address") String address,
            @ApiParam("备注,描述")                        @RequestParam(value = "remark",required = false) String remark,
            @ApiParam(value = "总价",required = true)    @NotNull @DecimalMin("0") @RequestParam("totalPrice") BigDecimal totalPrice
    ){
//        System.out.println("bid:   "+bId);
        String uuId = JWTUtil.getUUId(request);
        int order = orderService.createOrder(uuId, bId, sName, sQuantity, sWeight, img1, img2, img3, address, remark, totalPrice);
        if(order == StatusType.SUCCESS){
            return R.success();
        }else if(order == StatusType.SQL_ERROR){
            return R.sqlError();
        }else {
            return R.failure();
        }
    }



    @ApiOperation("查看订单  分页")
    @PostMapping("/getOrderPage")
    public R getOrderPage(
            @ApiParam("状态") @Range(min = 0,max = 1) @RequestParam(value = "status",required = false,defaultValue = "0") int status,
            @ApiParam(value = "页码",required = true) @Min(1) @RequestParam("pageNum") int pageNum,
            @ApiParam(value = "数量",required = true) @Max(10) @RequestParam("pageSize") int pageSize
    ){
        Object id = StpUtil.getLoginId();
        Map<String, Object> orderPage = orderService.getOrderPage(status, pageNum, pageSize);
        int result = (int) orderPage.get("status");
        if(result == StatusType.SUCCESS){
            System.out.println(orderPage.get("data"));
            return R.success(orderPage.get("data"));
        }else if(result == StatusType.SQL_ERROR){
            return R.sqlError();
        }else {
            return R.failure();
        }
    }


    @ApiOperation("根据订单id查看订单详情")
    @PostMapping("/getOrderDetails")
    public R getOrderDetails(
            @ApiParam(value = "订单id",required = true) @RequestParam("orderId") @NotNull @NotBlank(message = "不能为空") String orderId
    ){
        try {
            TOrder order = orderService.getOne(new QueryWrapper<TOrder>().eq("order_id", orderId));
            if(order != null){
                return R.success(order);
            }else {
                return R.failure();
            }
        } catch (Exception e){
            e.printStackTrace();
            return R.sqlError();
        }
    }


    @ApiOperation("根据订单id 删除订单")
    @PostMapping("/delOrder")
    public R delOrder(
            @ApiParam(value = "订单id",required = true) @RequestParam("orderId") @NotNull @NotBlank(message = "不能为空") String orderId
    ){
        try {
            int del = orderService.getBaseMapper().delete(new QueryWrapper<TOrder>().eq("order_id", orderId));
            if(del == 1){
                return R.success();
            }else {
                return R.failure();
            }
        } catch (Exception e){
            e.printStackTrace();
            return R.sqlError();
        }
    }

}

