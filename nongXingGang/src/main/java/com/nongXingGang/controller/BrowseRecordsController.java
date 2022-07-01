package com.nongXingGang.controller;


import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nongXingGang.pojo.BrowseRecords;
import com.nongXingGang.service.BrowseRecordsService;
import com.nongXingGang.utils.result.R;
import com.nongXingGang.utils.result.StatusType;
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
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 成大事
 * @since 2022-03-18
 */
@Api(tags = "浏览记录")
@RestController
@RequestMapping("/browse-records")
@Validated
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrowseRecordsController {


    private final BrowseRecordsService browseRecordsService;

    /**
     * 添加浏览记录
     * @param goodsUUId 商品uuid
     * @return R
     */
    @ApiOperation("添加浏览记录")
    @RequestMapping(value = "/addBrowseRecords",method = RequestMethod.POST)
    public R addBrowseRecords(
            @NotNull @NotBlank(message = "状态不能为空") @ApiParam(value = "商品的uuid",required = true) @RequestParam("goodsUUId") String goodsUUId
    ) {
        String id = (String) StpUtil.getLoginId();
        try {
            BrowseRecords one = browseRecordsService.getOne(new QueryWrapper<BrowseRecords>()
                    .eq("goods_uuid", goodsUUId)
                    .eq("user_openid", id));
            if(one == null){
                int browseNum = browseRecordsService.count(new QueryWrapper<BrowseRecords>().eq("user_openid", id));
                if(browseNum > 200){
                    String brUuid = browseRecordsService.page(new Page<>(199, 1), new QueryWrapper<BrowseRecords>()
                            .select("br_uuid")
                            .orderByDesc("create_time")).getRecords().get(0).getBrUuid();
                    boolean remove = browseRecordsService.remove(new QueryWrapper<BrowseRecords>().eq("br_uuid", brUuid));

                }
                BrowseRecords browseRecords = new BrowseRecords();
                browseRecords.setBrUuid(IdUtil.fastUUID());
                browseRecords.setUserOpenid(id);
                browseRecords.setThingUuid(goodsUUId);
                browseRecordsService.save(browseRecords);
                return R.ok();
            }else {
                return R.ok();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.sqlError();
        }
    }

    /**
     * 查询浏览记录
     * @return R
     */
    @ApiOperation("查询浏览记录")
    @RequestMapping(value = "/getBrowseRecords",method = RequestMethod.GET)
    public R getBrowseRecords(
            @Min(0) @NotNull(message = "页数不能为空")@ApiParam(value = "页码",required = true) @RequestParam("pageNum") int pageNum,
            @Min(0) @Max(30)@NotNull(message = "数量不能为空")@ApiParam(value = "数量",required = true) @RequestParam("pageSize") int pageSize
    ) {
        String id = (String) StpUtil.getLoginId();
        return browseRecordsService.getBrowseRecords(id,pageNum,pageSize);
    }



    /**
     * 查询商品的浏览记录
     * @param pageNum          页码
     * @param pageSize      数量
     * @return      R
     */
    @ApiOperation("查询商品的浏览记录")
    @PostMapping("/findMyBrowseGoodsList")
    public R findMyStoreGoodsList(
            @Min(0) @NotNull(message = "页数不能为空")@ApiParam(value = "页码",required = true) @RequestParam("pageNum") int pageNum,
            @Min(0) @Max(30)@NotNull(message = "数量不能为空")@ApiParam(value = "数量",required = true) @RequestParam("pageSize") int pageSize
    ){
        String id = (String) StpUtil.getLoginId();
        return browseRecordsService.findMyBrowseGoodsList(id,pageNum,pageSize);
    }



    /**
     * 查询需求的浏览记录
     * @param pageNum          页码
     * @param pageSize      数量
     * @return      R
     */
    @ApiOperation("查询需求的浏览记录")
    @PostMapping("/findMyBrowseDemandList")
    public R findMyStoreDemandList(
            @Min(0) @NotNull(message = "页数不能为空")@ApiParam(value = "页码",required = true) @RequestParam("pageNum") int pageNum,
            @Min(0) @Max(30)@NotNull(message = "数量不能为空")@ApiParam(value = "数量",required = true) @RequestParam("pageSize") int pageSize
    ){
        String id = (String) StpUtil.getLoginId();
        return browseRecordsService.findMyBrowseDemandList(id,pageNum,pageSize);

    }

    /**
     * 删除浏览记录
     * @param brUuId        浏览记录的uuid
     * @return R
     */
    @ApiOperation("删除浏览记录")
    @PostMapping(value = "/delBrowseRecords")
    public R delBrowseRecords(
            @NotNull @NotBlank(message = "浏览记录的uuid不能为空") @ApiParam(value = "浏览记录的uuid",required = true) @RequestParam("brUuId") String brUuId
    ) {
        String id = (String) StpUtil.getLoginId();
        try {
            boolean remove = browseRecordsService.remove(new QueryWrapper<BrowseRecords>()
                    .eq("br_uuid", brUuId)
                    .eq("user_openid", id));
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





}

