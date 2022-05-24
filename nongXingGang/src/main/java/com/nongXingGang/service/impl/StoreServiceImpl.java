package com.nongXingGang.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.nongXingGang.mapper.GoodsMapper;
import com.nongXingGang.pojo.Demand;
import com.nongXingGang.pojo.Goods;
import com.nongXingGang.pojo.Store;
import com.nongXingGang.mapper.StoreMapper;
import com.nongXingGang.service.StoreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nongXingGang.utils.result.Constants;
import com.nongXingGang.utils.result.R;
import com.nongXingGang.utils.result.StatusType;
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
@Service
public class StoreServiceImpl extends ServiceImpl<StoreMapper, Store> implements StoreService {

    @Resource
    private StoreMapper storeMapper;

    @Resource
    private GoodsMapper goodsMapper;

    //添加总的收藏列表
    @Override
    public R getMyStoreList(String id, int pageNum, int pageSize) {
        try {
            IPage<Map<String, Object>> goodsIPage = storeMapper.selectJoinMapsPage(new Page<>(pageNum, pageSize,false), new MPJLambdaWrapper<>()
                    .select(Store::getColUuid, Store::getThingType, Store::getColTime)
                    .selectAs(Store::getThingUuid,"thingUUId")
                    .selectAs(Goods::getGoodsPrice, "thingPrice")
                    .selectAs(Goods::getGoodsMainImgUrl, "imgUrl")
                    .selectAs(Goods::getGoodsVarieties, "varieties")
                    .selectAs(Goods::getGoodsKilogram, "weight")
                    .leftJoin(Goods.class, Goods::getGoodsUuid, Store::getThingUuid)
                    .eq(Store::getUserOpenid, id)
                    .ne(Store::getThingType, Constants.DEMANDS));

            IPage<Map<String, Object>> demandIPage = storeMapper.selectJoinMapsPage(new Page<>(pageNum, pageSize,false), new MPJLambdaWrapper<>()
                    .select(Store::getColUuid, Store::getThingType, Store::getColTime)
                    .selectAs(Store::getThingUuid,"thingUUId")
                    .selectAs(Demand::getDemandPrice, "thingPrice")
                    .selectAs(Demand::getDemandImgUrl, "imgUrl")
                    .selectAs(Demand::getDemandVarieties, "varieties")
                    .selectAs(Demand::getDemandKilogram, "weight")
                    .leftJoin(Demand.class, Demand::getDemandUuid, Store::getThingUuid)
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

    //添加收藏
    @Override
    public R addStore(String id, String thingUUId, int thingType) {
        Store one = storeMapper.selectOne(new QueryWrapper<Store>()
                .eq("thing_uuid", thingUUId)
                .eq("user_openid", id));
        if(one != null){
            one.setColTime(new Date());
            storeMapper.updateById(one);
            return R.ok();
        }else {
            Integer count = storeMapper.selectCount(new QueryWrapper<Store>().eq("user_openid", id));
            if(count > 200){
                List<Store> browseRecordsList = storeMapper.selectPage(new Page<>(200, count-200),
                        new QueryWrapper<Store>()
                                .orderByAsc("col_time")).getRecords();
                for (Store store : browseRecordsList) {
                    storeMapper.deleteById(store);
                }
            }else {
                Store store = new Store();
                store.setColUuid(IdUtil.fastUUID());
                store.setUserOpenid(id);
                store.setThingUuid(thingUUId);
                store.setThingType(thingType);
                int insert = storeMapper.insert(store);
                if (insert == 1){
                    return R.ok();
                }
            }
            return R.error();
        }
    }

    //查询商品的收藏记录
    @Override
    public R findMyStoreGoodsList(String id, int pageNum, int pageSize) {

        Map<String, Object> map = new HashMap<>();
        try {
            IPage<Map<String, Object>> goodsIPage = storeMapper.selectJoinMapsPage(new Page<>(pageNum, pageSize,false), new MPJLambdaWrapper<>()
                    .select(Store::getColUuid, Store::getThingType, Store::getColTime)
                    .selectAs(Store::getThingUuid,"thingUUId")
                    .selectAs(Goods::getGoodsPrice, "thingPrice")
                    .selectAs(Goods::getGoodsMainImgUrl, "imgUrl")
                    .selectAs(Goods::getGoodsVarieties, "varieties")
                    .selectAs(Goods::getGoodsKilogram, "weight")
                    .leftJoin(Goods.class, Goods::getGoodsUuid, Store::getThingUuid)
                    .eq(Store::getUserOpenid, id)
                    .ne(Store::getThingType, Constants.DEMANDS)
                    .orderByDesc(Store::getColTime));

            List<Map<String, Object>> goodsList = goodsIPage.getRecords();
            return R.ok(goodsList);
        } catch (Exception e) {
            e.printStackTrace();
            return R.sqlError();
        }
    }

    //查询需求的收藏记录
    @Override
    public R findMyStoreDemandList(String id, int pageNum, int pageSize) {
        Map<String, Object> map = new HashMap<>();
        try {
            IPage<Map<String, Object>> demandIPage = storeMapper.selectJoinMapsPage(new Page<>(pageNum, pageSize,false), new MPJLambdaWrapper<>()
                    .select(Store::getColUuid, Store::getThingType, Store::getColTime)
                    .selectAs(Store::getThingUuid,"thingUUId")
                    .selectAs(Demand::getDemandPrice, "thingPrice")
                    .selectAs(Demand::getDemandImgUrl, "imgUrl")
                    .selectAs(Demand::getDemandVarieties, "varieties")
                    .selectAs(Demand::getDemandKilogram, "weight")
                    .leftJoin(Demand.class, Demand::getDemandUuid, Store::getThingUuid)
                    .eq(Store::getUserOpenid, id)
                    .eq(Store::getThingType, Constants.DEMANDS)
                    .orderByDesc(Store::getColTime));
            List<Map<String, Object>> demandList = demandIPage.getRecords();
            return R.ok(demandList);
        } catch (Exception e) {
            e.printStackTrace();
            return R.sqlError();
        }
    }
}
