package com.nongXingGang.service.impl;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.nongXingGang.pojo.BrowseRecords;
import com.nongXingGang.mapper.BrowseRecordsMapper;
import com.nongXingGang.pojo.Demand;
import com.nongXingGang.pojo.Goods;
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
    public R getBrowseRecords(String id) {
        try {
            List<Browse> resultList = new ArrayList<>();

            List<Map<String, Object>> goodsList = browseRecordsMapper.selectJoinMaps(new MPJLambdaWrapper<>()
                    .select(BrowseRecords::getBrUuid,BrowseRecords::getCreateTime,BrowseRecords::getThingUuid)
                    .selectAs(Goods::getGoodsVarieties,"name")
                    .selectAs(Goods::getGoodsMainImgUrl,"imgUrl")
                    .leftJoin(Goods.class, Goods::getGoodsUuid, BrowseRecords::getThingUuid)
                    .orderByDesc(BrowseRecords::getCreateTime)
                    .eq(BrowseRecords::getUserOpenid, id)
                    .eq(BrowseRecords::getStatus, Constants.GOODS));

            if (goodsList != null){
                for (Map<String, Object> goodsMap : goodsList) {
                    Browse browse = new Browse();
                    browse.setBrUUId(String.valueOf(goodsMap.get("brUuid")));
                    browse.setThingUUId(String.valueOf(goodsMap.get("thingUuid")));
                    browse.setImgUrl(String.valueOf(goodsMap.get("imgUrl")));
                    browse.setCreateTime((Date) goodsMap.get("createTime"));
                    browse.setStatus(Constants.GOODS);
                    resultList.add(browse);
                }
            }
            List<Map<String, Object>> demandList = browseRecordsMapper.selectJoinMaps(new MPJLambdaWrapper<>()
                    .select(BrowseRecords::getBrUuid,BrowseRecords::getCreateTime,BrowseRecords::getThingUuid)
                    .selectAs(Demand::getDemandType,"name")
                    .selectAs(Demand::getDemandImgUrl,"imgUrl")
                    .leftJoin(Demand.class, Demand::getDemandUuid, BrowseRecords::getThingUuid)
                    .orderByDesc(BrowseRecords::getCreateTime)
                    .eq(BrowseRecords::getUserOpenid, id)
                    .eq(BrowseRecords::getStatus, Constants.DEMAND));

            if(demandList != null){
                for (Map<String, Object> goodsMap : demandList) {
                    Browse browse = new Browse();
                    browse.setBrUUId(String.valueOf(goodsMap.get("brUuid")));
                    browse.setThingUUId(String.valueOf(goodsMap.get("thingUuid")));
                    browse.setImgUrl(String.valueOf(goodsMap.get("imgUrl")));
                    browse.setCreateTime((Date) goodsMap.get("createTime"));
                    browse.setStatus(Constants.DEMAND);
                    browse.setName(String.valueOf(goodsMap.get("name")));
                    resultList.add(browse);
                }
            }


            List<Browse> collect = resultList.stream().sorted((e1, e2) -> e1.getCreateTime().compareTo(e2.getCreateTime())).collect(Collectors.toList());
            return R.ok(collect);
        } catch (Exception e) {
            e.printStackTrace();
            return R.sqlError();
        }
    }
}
