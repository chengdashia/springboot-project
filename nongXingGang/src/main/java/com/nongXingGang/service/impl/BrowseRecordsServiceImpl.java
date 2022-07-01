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

    /**
     * 获取浏览记录
     * @param id                     用户的id
     * @param pageNum                页码
     * @param pageSize               数量
     * @return                         R
     */
    @Override
    public R getBrowseRecords(String id,int pageNum,int pageSize) {
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

    /**
     * 查询商品的浏览记录
     * @param id                 用户id
     * @param pageNum             页码
     * @param pageSize            数量
     * @return  R
     */
    @Override
    public R findMyBrowseGoodsList(String id, int pageNum, int pageSize) {

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
                    .ne(Store::getThingType, Constants.DEMANDS)
                    .orderByDesc(BrowseRecords::getCreateTime));

            return R.ok(goodsIPage);
        } catch (Exception e) {
            e.printStackTrace();
            return R.sqlError();
        }
    }

    /**
     * 查询需求的浏览记录
     * @param id                 用户id
     * @param pageNum             页码
     * @param pageSize            数量
     * @return  R
     */
    @Override
    public R findMyBrowseDemandList(String id, int pageNum, int pageSize) {
        try {
            IPage<Map<String, Object>> demandIPage = browseRecordsMapper.selectJoinMapsPage(new Page<>(pageNum, pageSize,false), new MPJLambdaWrapper<>()
                    .select(BrowseRecords::getBrUuid, BrowseRecords::getThingUuid, BrowseRecords::getCreateTime)
                    .selectAs(BrowseRecords::getThingUuid,"thingUUId")
                    .selectAs(Demand::getDemandPrice, "thingPrice")
                    .selectAs(Demand::getDemandImgUrl, "imgUrl")
                    .selectAs(Demand::getDemandVarieties, "varieties")
                    .selectAs(Demand::getDemandKilogram, "weight")
                    .leftJoin(Demand.class, Demand::getDemandUuid, BrowseRecords::getThingUuid)
                    .eq(Store::getUserOpenid, id)
                    .eq(Store::getThingType, Constants.DEMANDS)
                    .orderByDesc(BrowseRecords::getCreateTime));

            return R.ok(demandIPage);
        } catch (Exception e) {
            e.printStackTrace();
            return R.sqlError();
        }
    }
}
