package com.nongXingGang.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nongXingGang.mapper.DemandMapper;
import com.nongXingGang.pojo.Demand;
import com.nongXingGang.pojo.es.DemandEs;
import com.nongXingGang.pojo.es.GoodEs;
import com.nongXingGang.service.DemandService;
import com.nongXingGang.utils.result.R;
import com.nongXingGang.utils.result.StatusType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
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
@Api(tags = "需求")
@RestController
@RequestMapping("/demand")
@Validated
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DemandController {

    private final DemandService demandService;

    private final DemandMapper demandMapper;

    private final ElasticsearchRestTemplate restTemplate;


    @ApiOperation("显示需求的列表数据")
    @PostMapping("/getNeedGoods")
    public R getNeedGoods( @NotNull(message = "页数不能为空") @RequestParam("pageNum") int pageNum,
                           @NotNull(message = "数量不能为空") @RequestParam("pageSize") int pageSize) {
        return demandService.getDemandGoods(pageNum,pageSize);
    }


    /**
     * 显示需求的详细数据
     * @param demandUUId         需求的uuid
     * @return                 R
     */
    @ApiOperation("显示需求的详细数据")
    @PostMapping("/getNeedDetails")
    public R getNeedDetails(
            @NotBlank(message = "需求的uuid不能为空") @ApiParam(value = "需求的uuid",required = true) @RequestParam("demandUUId") String demandUUId){
        return demandService.getNeedDetails(demandUUId);
    }


    /**
     * 添加需求
     * @param demand         商品的信息
     * @return                 R
     */
    @ApiOperation("添加需求")
    @PostMapping("/addDemand")
    public R addDemand(@Valid @RequestBody Demand demand){
        String openid = (String) StpUtil.getLoginId();
        return demandService.addDemand(openid, demand);
    }


    /**
     * 修改需求
     * @param demand         商品的信息
     * @return                 R
     */
    @ApiOperation("修改需求")
    @PostMapping("/updateDemand")
    public R updateDemand(@Valid @RequestBody Demand demand){
        String openid = (String) StpUtil.getLoginId();
        return  demandService.updateDemand(openid, demand);
    }


    /**
     * 删除需求信息
     * @param demandUUId         商品的信息
     * @return                 R
     */
    @ApiOperation("删除需求信息")
    @PostMapping("/delDemand")
    public R delDemand(
            @NotNull @NotBlank(message = "状态不能为空") @ApiParam(value = "商品的uuid",required = true) @RequestParam("demandUUId") String demandUUId
    ){
        String id = (String) StpUtil.getLoginId();
        return demandService.delDemand(id,demandUUId);
    }


    /**
     * 查看我发布的需求
     * @return                 R
     */
    @ApiOperation("查看我发布的需求")
    @PostMapping("/getMyPublishDemand")
    public R getMyPublishDemand(){
        String id = (String) StpUtil.getLoginId();
        try {
            List<Demand> demandList = demandService.getBaseMapper().selectList(new QueryWrapper<Demand>().eq("user_openid", id));
            if (demandList != null) {
                return R.ok(demandList);
            }
            return R.error();
        } catch (Exception e) {
            e.printStackTrace();
            return R.sqlError();
        }
    }



}

