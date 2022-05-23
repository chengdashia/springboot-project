package com.oneKeyRecycling.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oneKeyRecycling.pojo.TShop;
import com.oneKeyRecycling.service.TShopService;
import com.oneKeyRecycling.utils.globalResult.R;
import com.oneKeyRecycling.utils.globalResult.StatusType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 成大事
 * @since 2022-03-13
 */
@Api(tags = "shop的")
@RestController
@RequestMapping("/shop")
@Validated
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TShopController {

    private final TShopService shopService;



    @ApiOperation("商家列表")
    @PostMapping("/getSellerList")
    public R getSellerList(
            @ApiParam(value = "页码",required = true) @NotNull @Min (0)  @RequestParam("pageNum") int pageNum,
            @ApiParam(value = "数量",required = true) @NotNull @Max (10) @RequestParam("pageSize") int pageSize
    ){
        Map<String, Object> map = shopService.getSellerList(pageNum, pageSize);
        int status = (int) map.get("status");
        if(status == StatusType.SUCCESS){
            return R.success(map.get("data"));
        }else if(status == StatusType.SQL_ERROR){
            return R.sqlError();
        }else {
            return R.failure();
        }
    }

    @ApiOperation("根据商家的id 得到商家的全部信息")
    @PostMapping("getSellerDetails")
    public R getSellerDetails(
            @ApiParam(value = "页码", required = true) @RequestParam("pageNum") @NotNull @NotBlank(message = "不能为空") String uid
    ){
        try {
            TShop one = shopService.getOne(new QueryWrapper<TShop>().eq("u_id", uid));
            if(one != null){
                return R.success(one);
            }else {
                return R.notExists();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.sqlError();
        }
    }

    @ApiOperation("添加商家")
    @PostMapping("addSeller")
    public R addSeller(@RequestBody TShop tShop){
        try {
            shopService.save(tShop);
            return R.success();
        } catch (Exception e) {
            e.printStackTrace();
            return R.sqlError();
        }
    }


    @ApiOperation("修改商家信息")
    @PostMapping("updateSeller")
    public R updateSeller(@RequestBody TShop tShop){
        try {
            shopService.updateById(tShop);
            return R.success();
        } catch (Exception e) {
            e.printStackTrace();
            return R.sqlError();
        }
    }

    @ApiOperation("删除商家信息")
    @PostMapping("delSeller")
    public R delSeller(
            @ApiParam(value = "商家id",required = true) @RequestParam("shopId") @NotBlank(message = "商家id不能为空") String shopId
    ){
        try {
            boolean remove = shopService.remove(new QueryWrapper<TShop>().eq("shop_id", shopId));
            if (remove){
                return R.success();
            }else {
                return R.failure();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.sqlError();
        }

    }


}

