package com.nongXingGang.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.nongXingGang.service.ContractOrderService;
import com.nongXingGang.utils.pdf.AddAutograph;
import com.nongXingGang.utils.pdf.Base64Util;
import com.nongXingGang.utils.result.R;
import com.nongXingGang.utils.result.StatusType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.*;
import java.io.IOException;
import java.util.Map;

/**
 * @author 成大事
 * @since 2022-03-18
 */
@Slf4j
@Api(tags = "订单")
@RestController
@RequestMapping("/contractOrder")
@Validated
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ContractOrderController {

    private final ContractOrderService orderService;


    @ApiOperation("下订单+买家签名")
    @PostMapping("/placeOrder")
    public R legalPersonCertification(
            @RequestBody Map<String,String> map
    ){
        String base64 = map.get("base64");
        String signA;
        try {
            signA = Base64Util.GenerateImage(base64);
            if(signA != null){
                log.info("signA :{}" + signA);
                String openid = (String) StpUtil.getLoginId();
                String goodsUUId = map.get("goodsUUId");
                String addressUUId = map.get("addressUUId");
                String kilogram = map.get("kilogram");
                String remark = map.get("remark");
                int order = orderService.placeOrder(openid,signA,goodsUUId,addressUUId,kilogram,remark);
                if (order == StatusType.SUCCESS){
                    return R.ok();
                }else {
                    return R.error();
                }
            }else {
                return R.error();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return R.error();
        }
    }


    @ApiOperation("卖家签名")
    @PostMapping("/signB")
    public boolean addSignB(@RequestBody Map<String,String> map){
        String base64 = map.get("base64");
        System.out.println("base64str  :" +base64);
        try {
            String imagePath = Base64Util.GenerateImage(base64);
            if(imagePath != null){
                String path = map.get("path");
                AddAutograph.AddSignA(imagePath,path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }


    @ApiOperation("查看订单  不分页")
    @PostMapping("/getOrderList")
    public R buyerGetOrderList(
            @ApiParam(value = "用户状态 0:买家  1 卖家",required = true) @NotBlank(message = "用户状态") @NotNull(message = "姓名不能为空") @RequestParam(value = "uStatus",defaultValue = "0") int uStatus
    ){
        String openid = (String) StpUtil.getLoginId();
        Map<String, Object> orderList = orderService.getOrderList(openid, uStatus);
        int status = (int) orderList.get("status");
        if(status == StatusType.SUCCESS){
            return R.ok(orderList.get("data"));
        }else {
            return R.error();
        }

    }



    @ApiOperation("查看订单  分页")
    @PostMapping("/getOrderListPage")
    public R buyerGetOrderList(
            @ApiParam(value = "用户状态 0:买家  1 卖家",required = true)  @RequestParam(value = "uStatus",defaultValue = "0") int uStatus,
            @Min (0)@ApiParam(value = "页码",required = true) @RequestParam(value = "pageNum",defaultValue = "0") int pageNum,
            @Max(10) @ApiParam(value = "数量",required = true) @RequestParam("pageSize") int pageSize
    ){
        String openid = (String) StpUtil.getLoginId();
        Map<String, Object> orderList = orderService.getOrderListPage(openid, uStatus,pageNum,pageSize);
        int status = (int) orderList.get("status");
        if(status == StatusType.SUCCESS){
            return R.ok(orderList.get("data"));
        }else {
            return R.error();
        }

    }

    @ApiOperation("取消订单")
    @PostMapping("buyerCancelOrder")
    public R buyerCancelOrder(
         @NotNull @NotBlank @ApiParam("orderUUId") @RequestParam("orderUUId") String orderUUId
    ){
        String id = (String) StpUtil.getLoginId();
        int delete = orderService.buyerCancelOrder(id, orderUUId);
        if (delete == StatusType.SUCCESS){
            return R.ok();
        }else if(delete == StatusType.SQL_ERROR){
            return R.sqlError();
        }else {
            return R.error();
        }
    }

    @ApiOperation("取消订单")
    @PostMapping("sellerCancelOrder")
    public R sellerCancelOrder(
            @NotNull @NotBlank @ApiParam("orderUUId") @RequestParam("orderUUId") String orderUUId
    ){
        String id = (String) StpUtil.getLoginId();
        int delete = orderService.sellerCancelOrder(id, orderUUId);
        if (delete == StatusType.SUCCESS){
            return R.ok();
        }else if(delete == StatusType.SQL_ERROR){
            return R.sqlError();
        }else {
            return R.error();
        }
    }



}

