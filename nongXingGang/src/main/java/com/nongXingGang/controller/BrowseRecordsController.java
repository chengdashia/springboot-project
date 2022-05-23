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
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    ) {
        String id = (String) StpUtil.getLoginId();
        Map<String, Object> map = browseRecordsService.getBrowseRecords(id);
        Object status = map.get("status");
        if(status.equals(StatusType.SUCCESS)){
            return R.ok(map);
        }else if(status.equals(StatusType.SQL_ERROR)){
            return R.sqlError();
        }else {
            return R.error();
        }
    }

    /**
     * 删除浏览记录
     * @return R
     */
    @ApiOperation("删除浏览记录")
    @RequestMapping(value = "/delBrowseRecords",method = RequestMethod.GET)
    public R delBrowseRecords(
            @NotNull @NotBlank(message = "浏览记录的uuid不能为空") @ApiParam(value = "浏览记录的uuid",required = true) @RequestParam("brUUId") String brUUId
    ) {
        String id = (String) StpUtil.getLoginId();
        try {
            boolean remove = browseRecordsService.remove(new QueryWrapper<BrowseRecords>()
                    .eq("br_uuid", brUUId)
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

