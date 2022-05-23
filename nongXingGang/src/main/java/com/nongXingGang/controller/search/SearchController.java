package com.nongXingGang.controller.search;

import com.nongXingGang.pojo.es.DemandEs;
import com.nongXingGang.pojo.es.GoodEs;
import com.nongXingGang.utils.es.EsUtil;
import com.nongXingGang.utils.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 成大事
 * @since 2022/5/21 19:41
 */
@Api(tags = "搜索")
@RestController
@RequestMapping("/search")
@Validated
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SearchController {

    private final ElasticsearchRestTemplate restTemplate;

    private final EsUtil esUtil;

    @ApiOperation("搜索商品")
    @PostMapping("/searchGoods")
    public R searchGoods(
            @ApiParam(value = "名字",required = true) @RequestParam("name") @NotBlank @NotNull String name,
            @Min(0) @NotNull(message = "页数不能为空")@ApiParam(value = "页码",required = true) @RequestParam("pageNum") int pageNum,
            @Min(0) @Max(30)@NotNull(message = "数量不能为空")@ApiParam(value = "数量",required = true) @RequestParam("pageSize") int pageSize
    ){
        String filed1 = "goodsType";
        String filed2 = "goodsVarieties";
//
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.queryStringQuery(name).defaultField(filed1).defaultField(filed2))
                .withPageable(PageRequest.of(pageNum,pageSize))
                .withHighlightBuilder(new HighlightBuilder()
                        .field(filed1)
                        .field(filed2)
                        .preTags("<span style='color:red'>")
                        .postTags("</span>"))
                .build();
        SearchHits<GoodEs> search = restTemplate.search(query, GoodEs.class);
        List<SearchHit<GoodEs>> searchHits = search.getSearchHits();

        ArrayList<GoodEs> list = new ArrayList<>();
        for (SearchHit<GoodEs> searchHit : searchHits) {
            GoodEs goodEs = searchHit.getContent();

            String goodsType = searchHit.getHighlightField(filed1).toString();
            String goodsVarieties = searchHit.getHighlightField(filed2).toString();
            searchHit.getHighlightField(filed2);
            if (!goodsType.equals("[]")){
                goodEs.setGoodsType(goodsType.substring(1,goodsType.length()-1));
            }
            if (!goodsVarieties.equals("[]")){
                goodEs.setGoodsVarieties(goodsVarieties.substring(1,goodsVarieties.length()-1));
            }

            list.add(goodEs);

        }
        List<GoodEs> collect = list.stream().sorted((e1, e2) -> e1.getGoodsCreateTime().compareTo(e2.getGoodsCreateTime())).collect(Collectors.toList());


        return R.ok(collect);

    }


    @ApiOperation("搜索需求")
    @PostMapping("/searchDemand")
    public R searchDemand(
            @ApiParam(value = "名字",required = true) @RequestParam("name") @NotBlank @NotNull String name,
            @Min(0) @NotNull(message = "页数不能为空")@ApiParam(value = "页码",required = true) @RequestParam("pageNum") int pageNum,
            @Min(0) @Max(30)@NotNull(message = "数量不能为空")@ApiParam(value = "数量",required = true) @RequestParam("pageSize") int pageSize
    ){
        String filed1 = "demandType";
        String filed2 = "demandVarieties";
//
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.queryStringQuery(name).defaultField(filed1).defaultField(filed2))
                .withPageable(PageRequest.of(pageNum,pageSize))
                .withHighlightBuilder(new HighlightBuilder()
                        .field(filed1)
                        .field(filed2)
                        .preTags("<span style='color:red'>")
                        .postTags("</span>"))
                .build();


        SearchHits<DemandEs> search = restTemplate.search(query, DemandEs.class);
        List<SearchHit<DemandEs>> searchHits = search.getSearchHits();

        ArrayList<DemandEs> list = new ArrayList<>();
        for (SearchHit<DemandEs> searchHit : searchHits) {
            DemandEs demandEs = searchHit.getContent();

            String goodsType = searchHit.getHighlightField(filed1).toString();
            String goodsVarieties = searchHit.getHighlightField(filed2).toString();
            searchHit.getHighlightField(filed2);
            if (!goodsType.equals("[]")){
                demandEs.setDemandType(goodsType.substring(1,goodsType.length()-1));
            }
            if (!goodsVarieties.equals("[]")){
                demandEs.setDemandVarieties(goodsVarieties.substring(1,goodsVarieties.length()-1));
            }

            list.add(demandEs);

        }
        return R.ok(list);
    }

    @ApiOperation("商品匹配")
    @GetMapping("/goodsMatch")
    public R goodsMatch(
            @ApiParam(value = "名字",required = true) @RequestParam("name") @NotBlank @NotNull String name
    ){
        return R.ok(esUtil.getGoodsMatchList(name,10));
    }

    @ApiOperation("需求匹配")
    @GetMapping("/demandMatch")
    public R demandMatch(
            @ApiParam(value = "名字",required = true) @RequestParam("name") @NotBlank @NotNull String name
    ){
        return R.ok(esUtil.getDemandMatchList(name,10));
    }
}
