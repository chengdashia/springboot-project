package com.nongXingGang.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nongXingGang.pojo.Goods;
import com.nongXingGang.pojo.es.GoodEs;
import com.nongXingGang.pojo.request.GoodsBody;
import com.nongXingGang.service.GoodsService;
import com.nongXingGang.utils.result.R;
import com.nongXingGang.utils.result.StatusType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilterBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 成大事
 * @since 2022-03-18
 */
@Slf4j
@Api(tags = "商品信息")
@RestController
@RequestMapping("/goods")
@Validated
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GoodsController {

    private final GoodsService goodsService;

    private final ElasticsearchRestTemplate restTemplate;

    /**
     * 显示首页商品数据
     * @param status              商品状态  预售还是在售
     * @param pageNum             页码
     * @param pageSize           数量
     * @return                 R
     */
    @ApiOperation("显示首页商品数据")
    @PostMapping("/getIndexGoods")
    public R getIndexGoods(
            @Range(min = 0,max = 1) @NotNull(message = "状态不能为空")@ApiParam(value = "状态",required = true) @RequestParam("status") int status,
            @Min(0) @NotNull(message = "页数不能为空")@ApiParam(value = "页码",required = true) @RequestParam("pageNum") int pageNum,
            @Min(0) @Max(30)@NotNull(message = "数量不能为空")@ApiParam(value = "数量",required = true) @RequestParam("pageSize") int pageSize
    ){
        log.info("goodsStatus  {}",status);
        String id = (String) StpUtil.getLoginId();
//        Map<String, Object> map = goodsService.getIndexGoods(id,status, pageNum, pageSize);
//        int resStatus = (int) map.get("status");
//        if(resStatus == StatusType.SUCCESS){
//            Object data = map.get("data");
//            return R.ok(data);
//        }else {
//            return R.error();
//        }
        //构建查询条件构造器
        NativeSearchQuery build = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.boolQuery()
                        .mustNot(QueryBuilders.termQuery("userOpenid",id))
                        .must(QueryBuilders.termQuery("goodsStatus",status)))
                .withPageable(PageRequest.of(pageNum, pageSize))
                .withSorts(SortBuilders.fieldSort("goodsCreateTime").order(SortOrder.DESC))
                .withSourceFilter(new FetchSourceFilterBuilder().withIncludes("goodsUuid", "goodsStatus", "goodsVarieties", "goodsPrice", "goodsProductionArea", "goodsMainImgUrl","goodsCreateTime").build())
                .build();

        List<GoodEs> temp = new ArrayList<>();
        //搜索
        SearchHits<GoodEs> search = restTemplate.search(build, GoodEs.class);
        List<Map<String, Object>> result = new ArrayList<>();
        for (SearchHit<GoodEs> searchHit : search.getSearchHits()) {
            GoodEs goodEs = searchHit.getContent();
            Map<String, Object> resultMap = JSON.parseObject(JSON.toJSONString(goodEs), new TypeReference<Map<String, Object>>() {});
            resultMap.remove("userOpenid");
            resultMap.remove("goodsType");
            resultMap.remove("goodsKilogram");
            resultMap.remove("remark");
            resultMap.remove("goodsNewTime");
            result.add(resultMap);
        }

        return R.ok(result);

    }


    /**
     * 显示商品的详细数据
     * @param goodsUUId         商品的uuid
     * @return                 R
     */
    @ApiOperation(value = "显示商品的详细数据")
    @PostMapping("/getGoodsDetails")
    public R getGoodsDetails(
           @NotNull @NotBlank(message = "状态不能为空") @ApiParam(value = "商品的uuid",required = true) @RequestParam("goodsUUId") String goodsUUId){
        String id = (String) StpUtil.getLoginId();
        return goodsService.getGoodsDetails(id,goodsUUId);
    }


    /**
     * 添加商品
     * @param goods         商品的信息
     * @return                 R
     */
    @ApiOperation("添加商品")
    @PostMapping("/addGoods")
    public R getGoodsDetails(@Valid @RequestBody GoodsBody goods){
        log.info("商品的信息：{}",goods);
        String openid = (String) StpUtil.getLoginId();
        return goodsService.addGoods(openid, goods);

    }


    /**
     * 修改商品信息
     * @param goods         商品的信息
     * @return                 R
     */
    @ApiOperation("修改商品信息")
    @PostMapping("/updateGoods")
    public R updateGoodsDetails( @RequestBody GoodsBody goods){
        log.info("商品的信息：{}",goods);
        String openid = (String) StpUtil.getLoginId();
        return goodsService.updateGoods(openid, goods);
    }


    /**
     * 删除商品信息
     * @param goodsUUId         商品的信息
     * @return                 R
     */
    @ApiOperation("删除商品信息")
    @PostMapping("/delGoods")
    public R delGoods(
            @NotNull @NotBlank(message = "状态不能为空") @ApiParam(value = "商品的uuid",required = true) @RequestParam("goodsUUId") String goodsUUId
    ){
        String id = (String) StpUtil.getLoginId();
        return goodsService.delGoods(id,goodsUUId);
    }

    /**
     * 我的发布商品
     * @return                 R
     * @param status        商品状态
     */
    @ApiOperation("我的发布商品")
    @PostMapping("/getMyPublishGoods")
    public R getMyPublishGoods(
            @NotNull @Min(0) @Max(1) @ApiParam(value = "商品的状态",required = true) @RequestParam(value = "status",defaultValue = "0") int status
    ){
       String id = (String) StpUtil.getLoginId();
        try {
            List<Goods> goodsList = goodsService.getBaseMapper()
                    .selectList(new QueryWrapper<Goods>()
                            .eq("user_openid", id)
                            .eq("goods_status",status)
                            .orderByDesc("goods_create_time"));
            if(goodsList != null){
                return R.ok(goodsList);
            }else {
                return R.error();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.sqlError();
        }
    }



}

