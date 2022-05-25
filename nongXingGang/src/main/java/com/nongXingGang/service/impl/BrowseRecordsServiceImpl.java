package com.nongXingGang.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.nongXingGang.pojo.BrowseRecords;
import com.nongXingGang.mapper.BrowseRecordsMapper;
import com.nongXingGang.pojo.Demand;
import com.nongXingGang.pojo.Goods;
import com.nongXingGang.pojo.Store;
import com.nongXingGang.pojo.sql.Browse;
import com.nongXingGang.service.BrowseRecordsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nongXingGang.utils.result.Constants;
import com.nongXingGang.utils.result.R;
import com.nongXingGang.utils.result.StatusType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 成大事
 * @since 2022-03-18
 */
@Slf4j
@Service
public class BrowseRecordsServiceImpl extends ServiceImpl<BrowseRecordsMapper, BrowseRecords> implements BrowseRecordsService {


    @Resource
    private BrowseRecordsMapper browseRecordsMapper;

    @Override
    public R getBrowseRecords(String id,int pageNum,int pageSize) {
//        try {
//            List<Browse> resultList = new ArrayList<>();
//
//            List<Map<String, Object>> goodsList = browseRecordsMapper.selectJoinMaps(new MPJLambdaWrapper<>()
//                    .select(BrowseRecords::getBrUuid,BrowseRecords::getCreateTime,BrowseRecords::getThingUuid)
//                    .selectAs(Goods::getGoodsVarieties,"name")
//                    .selectAs(Goods::getGoodsMainImgUrl,"imgUrl")
//                    .leftJoin(Goods.class, Goods::getGoodsUuid, BrowseRecords::getThingUuid)
//                    .orderByDesc(BrowseRecords::getCreateTime)
//                    .eq(BrowseRecords::getUserOpenid, id)
//                    .ne(BrowseRecords::getStatus, Constants.DEMANDS));
//
//            if (goodsList != null){
//                for (Map<String, Object> goodsMap : goodsList) {
//                    Browse browse = new Browse();
//                    browse.setBrUUId(String.valueOf(goodsMap.get("brUuid")));
//                    browse.setThingUUId(String.valueOf(goodsMap.get("thingUuid")));
//                    browse.setImgUrl(String.valueOf(goodsMap.get("imgUrl")));
//                    browse.setCreateTime((Date) goodsMap.get("createTime"));
//                    browse.setStatus(Constants.GOODS);
//                    resultList.add(browse);
//                }
//            }
//            List<Map<String, Object>> demandList = browseRecordsMapper.selectJoinMaps(new MPJLambdaWrapper<>()
//                    .select(BrowseRecords::getBrUuid,BrowseRecords::getCreateTime,BrowseRecords::getThingUuid)
//                    .selectAs(Demand::getDemandType,"name")
//                    .selectAs(Demand::getDemandImgUrl,"imgUrl")
//                    .leftJoin(Demand.class, Demand::getDemandUuid, BrowseRecords::getThingUuid)
//                    .orderByDesc(BrowseRecords::getCreateTime)
//                    .eq(BrowseRecords::getUserOpenid, id)
//                    .eq(BrowseRecords::getStatus, Constants.DEMAND));
//
//            if(demandList != null){
//                for (Map<String, Object> goodsMap : demandList) {
//                    Browse browse = new Browse();
//                    browse.setBrUUId(String.valueOf(goodsMap.get("brUuid")));
//                    browse.setThingUUId(String.valueOf(goodsMap.get("thingUuid")));
//                    browse.setImgUrl(String.valueOf(goodsMap.get("imgUrl")));
//                    browse.setCreateTime((Date) goodsMap.get("createTime"));
//                    browse.setStatus(Constants.DEMAND);
//                    browse.setName(String.valueOf(goodsMap.get("name")));
//                    resultList.add(browse);
//                }
//            }
//            List<Browse> collect = resultList.stream().sorted((e1, e2) -> e1.getCreateTime().compareTo(e2.getCreateTime())).collect(Collectors.toList());
//            return R.ok(collect);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return R.sqlError();
//        }

        try {
            IPage<Map<String, Object>> goodsIPage = browseRecordsMapper.selectJoinMapsPage(new Page<>(pageNum, pageSize,false), new MPJLambdaWrapper<>()
                    .select(BrowseRecords::getBrUuid, BrowseRecords::getThingType, BrowseRecords::getCreateTime)
                    .selectAs(Store::getThingUuid,"thingUUId")
                    .selectAs(Goods::getGoodsPrice, "thingPrice")
                    .selectAs(Goods::getGoodsMainImgUrl, "imgUrl")
                    .selectAs(Goods::getGoodsVarieties, "varieties")
                    .selectAs(Goods::getGoodsKilogram, "weight")
                    .leftJoin(Goods.class, Goods::getGoodsUuid, BrowseRecords::getThingUuid)
                    .eq(Store::getUserOpenid, id)
                    .ne(Store::getThingType, Constants.DEMANDS));

            IPage<Map<String, Object>> demandIPage = browseRecordsMapper.selectJoinMapsPage(new Page<>(pageNum, pageSize,false), new MPJLambdaWrapper<>()
                    .select(BrowseRecords::getBrUuid, BrowseRecords::getThingUuid, BrowseRecords::getCreateTime)
                    .selectAs(BrowseRecords::getThingUuid,"thingUUId")
                    .selectAs(Demand::getDemandPrice, "thingPrice")
                    .selectAs(Demand::getDemandImgUrl, "imgUrl")
                    .selectAs(Demand::getDemandVarieties, "varieties")
                    .selectAs(Demand::getDemandKilogram, "weight")
                    .leftJoin(Demand.class, Demand::getDemandUuid, BrowseRecords::getThingUuid)
                    .eq(Store::getUserOpenid, id)
                    .eq(Store::getThingType, Constants.DEMANDS));

            List<Map<String, Object>> goodsList = goodsIPage.getRecords();
            List<Map<String, Object>> demandList = demandIPage.getRecords();

            List<Map<String, Object>> result = Stream.of(goodsList, demandList)
                    .flatMap(Collection::stream)
                    .sorted((o1,o2) -> {
                        Date colTime1 = (Date) o1.get("colTime");
                        Date colTime2 = (Date) o2.get("colTime");
                        return DateUtil.compare(colTime1,colTime2);
                    })
                    .collect(Collectors.toList());
            return R.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return R.sqlError();
        }
    }
}
