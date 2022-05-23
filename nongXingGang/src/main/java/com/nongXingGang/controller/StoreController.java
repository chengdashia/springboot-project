package com.nongXingGang.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nongXingGang.pojo.Store;
import com.nongXingGang.service.StoreService;
import com.nongXingGang.utils.result.R;
import com.nongXingGang.utils.result.StatusType;
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
 * @since 2022-03-18
 */
@Api(tags = "收藏")
@RestController
@RequestMapping("/store")
@Validated
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class StoreController {

    private final StoreService storeService;

    /**
     * 添加收藏记录
     * @param thingUUId 在售、预售、需求的uuid
     * @return R
     */
    @ApiOperation("添加收藏记录")
    @PostMapping("/addStore")
    public R add(
            @ApiParam(value = "在售、预售、需求的uuid",required = true) @NotBlank @NotNull @RequestParam("thingUUId") String thingUUId,
            @ApiParam(value = "在售、预售、需求类型",required = true)  @RequestParam("thingType") int thingType
    ){
        String id = (String) StpUtil.getLoginId();
        int result = storeService.addStore(id, thingUUId, thingType);
        if(result == StatusType.SUCCESS){
            return R.ok();
        }
        return R.error();
    }

    /**
     * 查询收藏记录
     * @return R
     */
    @ApiOperation("查询收藏记录")
    @PostMapping("/findMyStoreList")
    public R findMyStoreList(
            @Min(0) @NotNull(message = "页数不能为空")@ApiParam(value = "页码",required = true) @RequestParam("pageNum") int pageNum,
            @Min(0) @Max(30)@NotNull(message = "数量不能为空")@ApiParam(value = "数量",required = true) @RequestParam("pageSize") int pageSize
    ){
        String id = (String) StpUtil.getLoginId();
        Map<String, Object> map = storeService.getMyStoreList(id,pageNum,pageSize);
        Object status = map.get("status");
        if (status.equals(StatusType.SUCCESS)){
            return R.ok(map);
        }else if (status.equals(StatusType.NOT_EXISTS)){
            return R.notExists();
        }else {
            return R.error();
        }
    }


    /**
     * 查询商品的收藏记录
     * @return R
     */
    @ApiOperation("查询商品的收藏记录")
    @PostMapping("/findMyStoreGoodsList")
    public R findMyStoreGoodsList(
            @Min(0) @NotNull(message = "页数不能为空")@ApiParam(value = "页码",required = true) @RequestParam("pageNum") int pageNum,
            @Min(0) @Max(30)@NotNull(message = "数量不能为空")@ApiParam(value = "数量",required = true) @RequestParam("pageSize") int pageSize
    ){
        String id = (String) StpUtil.getLoginId();
        Map<String, Object> map = storeService.findMyStoreGoodsList(id,pageNum,pageSize);
        Object status = map.get("status");
        if (status.equals(StatusType.SUCCESS)){
            return R.ok(map);
        }else if (status.equals(StatusType.NOT_EXISTS)){
            return R.notExists();
        }else {
            return R.error();
        }
    }


    /**
     * 查询需求的收藏记录
     * @return R
     */
    @ApiOperation("查询需求的收藏记录")
    @PostMapping("/findMyStoreDemandList")
    public R findMyStoreDemandList(
            @Min(0) @NotNull(message = "页数不能为空")@ApiParam(value = "页码",required = true) @RequestParam("pageNum") int pageNum,
            @Min(0) @Max(30)@NotNull(message = "数量不能为空")@ApiParam(value = "数量",required = true) @RequestParam("pageSize") int pageSize
    ){
        String id = (String) StpUtil.getLoginId();
        Map<String, Object> map = storeService.findMyStoreDemandList(id,pageNum,pageSize);
        Object status = map.get("status");
        if (status.equals(StatusType.SUCCESS)){
            return R.ok(map);
        }else if (status.equals(StatusType.NOT_EXISTS)){
            return R.notExists();
        }else {
            return R.error();
        }
    }

    /**
     * 删除收藏记录
     * @param colUUId 收藏的id
     * @return R
     */
    @ApiOperation("删除收藏记录")
    @PostMapping("/deleteStore")
    public R deleteStore(
            @ApiParam("收藏的id")@NotBlank @NotNull @RequestParam("colUUId") String colUUId
    ){
        String id = (String) StpUtil.getLoginId();
        try {
            boolean remove = storeService.remove(new QueryWrapper<Store>().eq("user_openid", id).eq("col_uuid", colUUId));
            if(remove){
                return R.ok();
            }else {
                return R.error();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.sqlError();
        }
    }


    /**
     * 删除收藏记录
     * @param thingUUId  在售、预售和需求的uuid
     * @return R
     */
    @ApiOperation("取消收藏")
    @PostMapping("/cancelStore")
    public R cancelStore(@ApiParam("thingUUId")@NotBlank @NotNull @RequestParam("thingUUId") String thingUUId){
        String id = (String) StpUtil.getLoginId();
        try {
            boolean remove = storeService.remove(new QueryWrapper<Store>()
                    .eq("user_openid", id)
                    .eq("thing_uuid", thingUUId));
            if (remove){
                return R.ok();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.sqlError();
        }
        return R.error();
    }
}

